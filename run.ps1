<#
.SYNOPSIS
Starts jEISCP: the ISCP console and the JavaFX remote control.

.DESCRIPTION
All arguments are passed to the application:

  -a <ip-address>[:port]  connect to this receiver, no autodiscovery
  -nogui            console only
  -noconsole        GUI only

Needs Java 21 or newer; Maven comes with the wrapper mvnw.cmd. The JDK from JAVA_HOME is used for build and run,
otherwise the one on the PATH. A missing command catalogue submodule is fetched first.
Maven only builds; java runs in this console, so Ctrl+C ends the application.

.EXAMPLE
.\run.ps1 -a 192.168.2.29
#>

$ErrorActionPreference = 'Stop'

function Get-JavaMajorVersion([string] $java) {
    $ErrorActionPreference = 'Continue'   # java -version writes to stderr
    $output = & $java -XshowSettings:properties -version 2>&1 | ForEach-Object { "$_" }
    foreach ($line in $output) {
        if ($line -match 'java\.specification\.version = (\d+)') {
            return [int] $Matches[1]
        }
    }
    return 0
}

$java = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME 'bin\java.exe' } else { 'java' }
foreach ($tool in @($java)) {
    if (-not (Get-Command $tool -ErrorAction SilentlyContinue)) {
        Write-Error "Not found: $tool"
    }
}

$version = Get-JavaMajorVersion $java
if ($version -lt 21) {
    Write-Error "jEISCP needs Java 21 or newer, found $version ($java)"
}

Push-Location $PSScriptRoot
try {
    if (-not (Test-Path 'src/main/resources/miracle2k-onkyo-eiscp/eiscp-commands.yaml')) {
        Write-Host 'Fetching the command catalogue submodule ...'
        git submodule update --init
        if ($LASTEXITCODE -ne 0) {
            Write-Error 'git submodule update --init failed'
        }
    }

    & .\mvnw.cmd -q compile 'dependency:build-classpath@javafx-module-path' 'dependency:build-classpath@app-class-path'
    $exitCode = $LASTEXITCODE
    if ($exitCode -eq 0) {
        $sep = [IO.Path]::PathSeparator
        $javaArgs = @('--enable-native-access=javafx.graphics', '-Djeiscp.logDir=.')
        if ($version -ge 23) {
            # keep JDK 23+ quiet about JavaFX's sun.misc.Unsafe use
            $javaArgs += '--sun-misc-unsafe-memory-access=allow'
        }
        $javaArgs += @(
            '--module-path', (Get-Content -Raw 'target/javafx-module-path.txt').Trim(),
            '--add-modules', 'javafx.controls',
            # lets the app name its windows jEISCP instead of java, see Ui.setApplicationName
            '--add-exports', 'javafx.graphics/com.sun.glass.ui=ALL-UNNAMED',
            '--class-path', ('target/classes' + $sep + (Get-Content -Raw 'target/class-path.txt').Trim()),
            'de.csmp.jeiscp.app.ThreadedConsoleApp'
        )
        & $java @javaArgs @args
        $exitCode = $LASTEXITCODE
    }
} finally {
    Pop-Location
}
exit $exitCode
