<#
.SYNOPSIS
Builds the jEISCP starter: target\dist\jEISCP with jEISCP.exe and its own Java runtime, and a zip of it.

.DESCRIPTION
Needs Maven and a JDK 21 or newer with jpackage. The JDK from JAVA_HOME is used, otherwise the one
on the PATH. A missing command catalogue submodule is fetched first. The tests are skipped; run
mvn test for them. jpackage builds for this system only; build the Linux starter with dist.sh.

.EXAMPLE
.\dist.ps1
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
$jpackage = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME 'bin\jpackage.exe' } else { 'jpackage' }
foreach ($tool in @($java, $jpackage, 'mvn')) {
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

    $distDir = Join-Path $PSScriptRoot 'target\dist'
    $running = Get-Process jEISCP -ErrorAction SilentlyContinue |
        Where-Object { $_.Path -and $_.Path.StartsWith($distDir, [StringComparison]::OrdinalIgnoreCase) }
    if ($running) {
        Write-Error 'jEISCP from target\dist is running and locks its files; quit it first.'
    }

    Write-Host 'Building the starter (jpackage) ...'
    & mvn -q -Pdist -DskipTests package
    $exitCode = $LASTEXITCODE
    if ($exitCode -eq 0) {
        $image = 'target\dist\jEISCP'
        $appVersion = (Select-String -Path "$image\app\jEISCP.cfg" -Pattern 'app-version=(.+)$').Matches[0].Groups[1].Value
        $zip = "target\dist\jEISCP-$appVersion-windows.zip"
        Write-Host 'Packing the zip ...'
        Compress-Archive -Path $image -DestinationPath $zip -Force
        Write-Host "Starter: $image\jEISCP.exe"
        Write-Host "Archive: $zip"
    }
} finally {
    Pop-Location
}
exit $exitCode
