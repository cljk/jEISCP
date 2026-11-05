#!/usr/bin/env sh
# Builds the jEISCP starter: target/dist/jEISCP with bin/jEISCP and its own Java runtime, and a tar.gz of it.
# Needs Maven and a JDK 21+ with jpackage; the JDK from JAVA_HOME is used, otherwise the one on the PATH.
# jpackage builds for this system only (on Windows use dist.ps1). The tests are skipped; run mvn test for them.
set -eu

cd "$(dirname "$0")"

if [ -n "${JAVA_HOME:-}" ]; then
	java="$JAVA_HOME/bin/java"
	jpackage="$JAVA_HOME/bin/jpackage"
else
	java=java
	jpackage=jpackage
fi
for tool in "$java" "$jpackage" mvn; do
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
mvn -q -Pdist -DskipTests package

image=target/dist/jEISCP
cfg=$(find "$image" -name jEISCP.cfg | head -n 1)
app_version=$(sed -n 's/.*app-version=//p' "$cfg" | head -n 1 | tr -d '\r')
case "$(uname -s)" in
	Linux) platform=linux ;;
	Darwin) platform=mac ;;
	*) platform=$(uname -s | tr '[:upper:]' '[:lower:]') ;;
esac
archive="target/dist/jEISCP-$app_version-$platform.tar.gz"
echo "Packing the archive ..."
tar -czf "$archive" -C target/dist jEISCP	# keeps the launcher executable

echo "Starter: $image/bin/jEISCP"
echo "Archive: $archive"
