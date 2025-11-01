Write-Host "=== TESTANDO ENCURTADOR DE URL ===" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

Write-Host "1. Testando endpoint de saude..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/saude" -Method Get
    Write-Host "   OK: $response" -ForegroundColor Green
} catch {
    Write-Host "   ERRO: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "2. Criando URL curta (Google)..." -ForegroundColor Yellow
try {
    $body = @{
        urlOriginal = "https://www.google.com"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/api/encurtar" -Method Post -Body $body -ContentType "application/json"
    $codigoCurto = $response.codigoCurto
    Write-Host "   OK: URL curta criada -> $($response.urlCurta)" -ForegroundColor Green
    Write-Host "   Codigo: $codigoCurto" -ForegroundColor Cyan
} catch {
    Write-Host "   ERRO: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "3. Testando validacao (URL invalida)..." -ForegroundColor Yellow
try {
    $body = @{
        urlOriginal = "url-sem-protocolo"
    } | ConvertTo-Json
    
    $response = Invoke-RestMethod -Uri "$baseUrl/api/encurtar" -Method Post -Body $body -ContentType "application/json"
    Write-Host "   ERRO: Deveria ter falhado!" -ForegroundColor Red
} catch {
    Write-Host "   OK: Validacao funcionou (erro esperado)" -ForegroundColor Green
}
Write-Host ""

Write-Host "4. Testando redirecionamento..." -ForegroundColor Yellow
if ($codigoCurto) {
    Write-Host "   Acesse no navegador: $baseUrl/$codigoCurto" -ForegroundColor Cyan
    Write-Host "   (Deve redirecionar para Google)" -ForegroundColor Gray
} else {
    Write-Host "   AVISO: Nenhum codigo disponivel" -ForegroundColor Yellow
}
Write-Host ""

Write-Host "5. Listando URLs (COM autenticacao)..." -ForegroundColor Yellow
try {
    $credentials = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:123456"))
    $headers = @{
        Authorization = "Basic $credentials"
    }
    
    $response = Invoke-RestMethod -Uri "$baseUrl/api/admin/urls" -Method Get -Headers $headers
    Write-Host "   OK: $($response.Count) URL(s) encontrada(s)" -ForegroundColor Green
    $response | ForEach-Object {
        Write-Host "      - $($_.urlOriginal) -> $($_.codigoCurto) ($($_.acessos) acessos)" -ForegroundColor Gray
    }
} catch {
    Write-Host "   ERRO: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "6. Testando endpoint admin SEM autenticacao..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/admin/urls" -Method Get
    Write-Host "   ERRO: Deveria ter falhado (401)!" -ForegroundColor Red
} catch {
    Write-Host "   OK: Bloqueado corretamente (401 Unauthorized)" -ForegroundColor Green
}
Write-Host ""

Write-Host "7. Obtendo estatisticas..." -ForegroundColor Yellow
try {
    $credentials = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:123456"))
    $headers = @{
        Authorization = "Basic $credentials"
    }
    
    $response = Invoke-RestMethod -Uri "$baseUrl/api/admin/estatisticas" -Method Get -Headers $headers
    Write-Host "   OK: Estatisticas obtidas" -ForegroundColor Green
    Write-Host "      Total URLs: $($response.totalUrls)" -ForegroundColor Gray
    Write-Host "      Total Acessos: $($response.totalAcessos)" -ForegroundColor Gray
} catch {
    Write-Host "   ERRO: $_" -ForegroundColor Red
}
Write-Host ""

Write-Host "=== TESTES CONCLUIDOS ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "Proximos passos:" -ForegroundColor Yellow
Write-Host "1. Acesse $baseUrl/$codigoCurto no navegador para testar redirect" -ForegroundColor Gray
Write-Host "2. Verifique o console do IntelliJ para ver os logs SQL" -ForegroundColor Gray
Write-Host "3. Teste deletar uma URL com:" -ForegroundColor Gray
Write-Host "   curl -X DELETE $baseUrl/api/admin/urls/1 -u admin:123456" -ForegroundColor Cyan
