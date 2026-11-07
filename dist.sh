#!/usr/bin/env sh
# Builds the jEISCP starter with its own Java runtime and an archive of it: on Linux target/dist/jEISCP with
# bin/jEISCP and a tar.gz, on macOS target/dist/jEISCP.app and a zip.
# Needs a JDK 21+ (Maven comes with the wrapper ./mvnw) with jpackage; the JDK from JAVA_HOME is used, otherwise the one on the PATH.
# jpackage builds for this system and architecture only (on Windows use dist.ps1). The tests are skipped; run ./mvnw test for them.
set -eu

cd "$(dirname "$0")"

if [ -n "${JAVA_HOME:-}" ]; then
	java="$JAVA_HOME/bin/java"
	jpackage="$JAVA_HOME/bin/jpackage"
else
	java=java
	jpackage=jpackage
fi
for tool in "$java" "$jpackage"; do
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

echo "Building the starter (jpackage) ..."
./mvnw -q -Pdist -DskipTests package

if [ "$(uname -s)" = Darwin ]; then
	image=target/dist/jEISCP.app
else
	image=target/dist/jEISCP
fi
cfg=$(find "$image" -name jEISCP.cfg | head -n 1)
app_version=$(sed -n 's/.*app-version=//p' "$cfg" | head -n 1 | tr -d '\r')

echo "Packing the archive ..."
case "$(uname -s)" in
	Darwin)
		# the jlinked runtime is built for this Mac's architecture only
		case "$(uname -m)" in
			arm64) arch=aarch64 ;;
			*) arch=x64 ;;
		esac
		archive="target/dist/jEISCP-$app_version-mac-$arch.zip"
		rm -f "$archive"
		ditto -c -k --keepParent "$image" "$archive"	# the zip Finder makes; keeps permissions and symlinks
		echo "Starter: $image (open $image)"
		;;
	*)
		platform=$(uname -s | tr '[:upper:]' '[:lower:]')
		archive="target/dist/jEISCP-$app_version-$platform.tar.gz"
		tar -czf "$archive" -C target/dist jEISCP	# keeps the launcher executable
		echo "Starter: $image/bin/jEISCP"
		;;
esac
echo "Archive: $archive"
