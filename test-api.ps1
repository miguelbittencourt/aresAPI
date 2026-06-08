Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "  AresApp Backend - Testes de API" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Cyan

$ApiUrl = "http://localhost:8080"

# Teste 1: Registro
Write-Host "`n[1/4] POST /api/auth/register" -ForegroundColor Yellow
$regBody = '{"email":"user1@test.com","password":"password123"}'
try {
    $regResp = Invoke-WebRequest -Uri "$ApiUrl/api/auth/register" -Method POST -ContentType "application/json" -Body $regBody
    $regData = $regResp.Content | ConvertFrom-Json
    Write-Host "OK - Usuario registrado" -ForegroundColor Green
    $TOKEN = $regData.token
    Write-Host "  Email: $($regData.email)"
} catch {
    Write-Host "FALHA: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

# Teste 2: Login
Write-Host "`n[2/4] POST /api/auth/login" -ForegroundColor Yellow
$loginBody = '{"email":"user1@test.com","password":"password123"}'
try {
    $loginResp = Invoke-WebRequest -Uri "$ApiUrl/api/auth/login" -Method POST -ContentType "application/json" -Body $loginBody
    $loginData = $loginResp.Content | ConvertFrom-Json
    Write-Host "OK - Login bem-sucedido" -ForegroundColor Green
    $TOKEN = $loginData.token
} catch {
    Write-Host "FALHA" -ForegroundColor Red
}

# Teste 3: Criar Treino
Write-Host "`n[3/4] POST /api/workouts" -ForegroundColor Yellow
$workoutBody = '{"gymName":"Academia Fit","date":"2026-03-24","exercises":[{"id":"ex1","orderIndex":1,"name":"Supino","sets":[{"reps":10,"weight":20,"unit":"kg"}]}]}'
try {
    $workoutResp = Invoke-WebRequest -Uri "$ApiUrl/api/workouts" -Method POST -ContentType "application/json" -Body $workoutBody -Headers @{Authorization="Bearer $TOKEN"}
    $workoutData = $workoutResp.Content | ConvertFrom-Json
    Write-Host "OK - Treino criado" -ForegroundColor Green
    Write-Host "  ID: $($workoutData.id)"
} catch {
    Write-Host "FALHA" -ForegroundColor Red
}

# Teste 4: Listar
Write-Host "`n[4/4] GET /api/workouts" -ForegroundColor Yellow
try {
    $listResp = Invoke-WebRequest -Uri "$ApiUrl/api/workouts" -Headers @{Authorization="Bearer $TOKEN"}
    Write-Host "OK - Treinos listados" -ForegroundColor Green
} catch {
    Write-Host "FALHA" -ForegroundColor Red
}

Write-Host "`n===============================================" -ForegroundColor Green
Write-Host "API rodando: $ApiUrl" -ForegroundColor Cyan
Write-Host "Console H2: $ApiUrl/h2-console" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Green
