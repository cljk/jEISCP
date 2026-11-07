#!/usr/bin/env sh
# Starts jEISCP: the ISCP console and the JavaFX remote control.
# All arguments are passed to the application, e.g. ./run.sh -a 192.168.2.29 (-? lists them).
# Needs Java 21+ (Maven comes with the wrapper ./mvnw); the JDK from JAVA_HOME is used for build and run, otherwise the one on the PATH.
# Maven only builds; java runs in the foreground of this shell, so Ctrl+C ends the application.
set -eu

cd "$(dirname "$0")"

if [ -n "${JAVA_HOME:-}" ]; then
	java="$JAVA_HOME/bin/java"
else
	java=java
fi
for tool in "$java"; do
	if ! command -v "$tool" >/dev/null 2>&1; then
		echo "Not found: $tool" >&2
		exit 1
	fi
done

version=$("$java" -XshowSettings:properties -version 2>&1 | sed -n 's/^ *java\.specification\.version = \([0-9]*\).*/\1/p')
if [ "${version:-0}" -lt 21 ]; then
	echo "jEISCP needs Java 21 or newer, found ${version:-none} ($java)" >&2
	exit 1
fi

if [ ! -f src/main/resources/miracle2k-onkyo-eiscp/eiscp-commands.yaml ]; then
	echo "Fetching the command catalogue submodule ..."
	git submodule update --init
fi

./mvnw -q compile dependency:build-classpath@javafx-module-path dependency:build-classpath@app-class-path

# Maven writes the paths with the platform separator, which is ';' for the Windows java under Git Bash
case "$(uname -s)" in
	MINGW* | MSYS* | CYGWIN*) sep=';' ;;
	*) sep=':' ;;
esac

# GNOME takes a window's dock name and icon only from a desktop entry whose StartupWMClass matches its WM_CLASS
# (jEISCP, see Ui.setApplicationName). NoDisplay keeps the entry out of the app menu.
if [ "$(uname -s)" = Linux ]; then
	apps="${XDG_DATA_HOME:-$HOME/.local/share}/applications"
	entry="$apps/jeiscp.desktop"
	desktop=$(cat <<EOF
[Desktop Entry]
Type=Application
Name=jEISCP
Comment=Remote control for Onkyo and Integra AV receivers
Exec="$(pwd)/run.sh"
Icon=$(pwd)/src/main/resources/de/csmp/jeiscp/app/fx/icons/icon-256.png
Terminal=true
NoDisplay=true
StartupWMClass=jEISCP
EOF
)
	if [ "$(cat "$entry" 2>/dev/null)" != "$desktop" ]; then
		mkdir -p "$apps" && printf '%s\n' "$desktop" >"$entry" || echo "Could not write $entry; the dock shows no icon" >&2
	fi
fi

# keep JDK 23+ quiet about JavaFX's native access and sun.misc.Unsafe use
unsafe=""
if [ "$version" -ge 23 ]; then
	unsafe="--sun-misc-unsafe-memory-access=allow"
fi

set -- de.csmp.jeiscp.app.ThreadedConsoleApp "$@"
# macOS: JavaFX takes the Dock icon from -Xdock:icon; the name in the Dock may stay "java", the app menu says jEISCP
if [ "$(uname -s)" = Darwin ]; then
	set -- -Xdock:name=jEISCP "-Xdock:icon=$(pwd)/src/main/resources/de/csmp/jeiscp/app/fx/icons/icon-256.png" "$@"
fi

# the export lets the app name its windows "jEISCP" (WM_CLASS) instead of "java", see Ui.setApplicationName
# shellcheck disable=SC2086 # $unsafe is empty or one word
exec "$java" --enable-native-access=javafx.graphics -Djeiscp.logDir=. $unsafe \
	--add-exports javafx.graphics/com.sun.glass.ui=ALL-UNNAMED \
	--module-path "$(cat target/javafx-module-path.txt)" --add-modules javafx.controls \
	--class-path "target/classes$sep$(cat target/class-path.txt)" \
	"$@"
