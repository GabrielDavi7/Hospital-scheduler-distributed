$ErrorActionPreference = 'Stop'
$raizProjeto = Split-Path -Parent $PSScriptRoot
Push-Location $raizProjeto
try {
    # Este script apenas baixa as bibliotecas; a compilacao e feita separadamente.
    New-Item -ItemType Directory -Force lib | Out-Null
    $artefatos = @(
        'org/apache/xmlrpc/xmlrpc-common/3.1.3/xmlrpc-common-3.1.3.jar',
        'org/apache/xmlrpc/xmlrpc-client/3.1.3/xmlrpc-client-3.1.3.jar',
        'org/apache/xmlrpc/xmlrpc-server/3.1.3/xmlrpc-server-3.1.3.jar',
        'org/apache/ws/commons/util/ws-commons-util/1.0.2/ws-commons-util-1.0.2.jar',
        'commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar'
    )
    foreach ($artefato in $artefatos) {
        $destino = Join-Path 'lib' (Split-Path -Leaf $artefato)
        if (-not (Test-Path -LiteralPath $destino)) {
            Invoke-WebRequest -UseBasicParsing -Uri "https://repo.maven.apache.org/maven2/$artefato" -OutFile "$destino.download"
            Move-Item -LiteralPath "$destino.download" -Destination $destino
        }
    }
    Write-Host 'Bibliotecas disponiveis em lib/.'
} finally {
    Pop-Location
}
