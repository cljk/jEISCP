#!/usr/bin/env sh
# Starts jEISCP: the ISCP console and the JavaFX remote control.
# All arguments are passed to the application, e.g. ./run.sh -a 192.168.2.29 (-? lists them).
# Needs Maven and Java 21+; the JDK from JAVA_HOME is used for build and run, otherwise the one on the PATH.
# Maven only builds; java runs in the foreground of this shell, so Ctrl+C ends the application.
set -eu

cd "$(dirname "$0")"

if [ -n "${JAVA_HOME:-}" ]; then
	java="$JAVA_HOME/bin/java"
else
	java=java
fi
for tool in "$java" mvn; do
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

mvn -q compile dependency:build-classpath@javafx-module-path dependency:build-classpath@app-class-path

# Maven writes the paths with the platform separator, which is ';' for the Windows java under Git Bash
case "$(uname -s)" in
	MINGW* | MSYS* | CYGWIN*) sep=';' ;;
	*) sep=':' ;;
esac

# keep JDK 23+ quiet about JavaFX's native access and sun.misc.Unsafe use
unsafe=""
if [ "$version" -ge 23 ]; then
	unsafe="--sun-misc-unsafe-memory-access=allow"
fi

# shellcheck disable=SC2086 # $unsafe is empty or one word
exec "$java" --enable-native-access=javafx.graphics $unsafe \
	--module-path "$(cat target/javafx-module-path.txt)" --add-modules javafx.controls \
	--class-path "target/classes$sep$(cat target/class-path.txt)" \
	de.csmp.jeiscp.app.ThreadedConsoleApp "$@"
