# Smoke demo (PowerShell)
# Prerequisites: JDK 21 + Maven; server running on :8080

$base = "http://localhost:8080"
$deviceId = "HES-SMOKE-001"

$register = @{
  protocolVersion = "1.0"
  type = "AGENT_REGISTER"
  messageId = [guid]::NewGuid().ToString()
  deviceId = $deviceId
  timestamp = (Get-Date).ToUniversalTime().ToString("o")
  payload = @{
    model = "HES-BAT-10K"
    firmwareVersion = "1.0.0"
    siteCode = "SITE-SMOKE"
    siteName = "Smoke Household"
  }
} | ConvertTo-Json -Depth 5

$reg = Invoke-RestMethod -Method Post -Uri "$base/api/v1/agent/messages" -ContentType "application/json" -Body $register
$reg | ConvertTo-Json
$apiKey = $reg.payload.apiKey
Write-Host "apiKey=$apiKey"

$headers = @{ "X-Api-Key" = $apiKey; "Content-Type" = "application/json" }

$heartbeat = @{
  protocolVersion = "1.0"
  type = "HEARTBEAT"
  messageId = [guid]::NewGuid().ToString()
  deviceId = $deviceId
  timestamp = (Get-Date).ToUniversalTime().ToString("o")
  payload = @{ uptimeSec = 1 }
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Method Post -Uri "$base/api/v1/agent/messages" -Headers $headers -Body $heartbeat | ConvertTo-Json

$telemetry = @{
  protocolVersion = "1.0"
  type = "TELEMETRY_REPORT"
  messageId = [guid]::NewGuid().ToString()
  deviceId = $deviceId
  timestamp = (Get-Date).ToUniversalTime().ToString("o")
  payload = @{
    socPercent = 42.5
    batteryKwh = 4.25
    inverterWatts = 1200
    gridWatts = -1200
    homeLoadWatts = 800
    batteryVoltage = 51.2
    batteryCurrent = 23.4
    faultCode = 0
    operatingMode = "DISCHARGING"
  }
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Method Post -Uri "$base/api/v1/agent/messages" -Headers $headers -Body $telemetry | ConvertTo-Json
Invoke-RestMethod -Uri "$base/api/v1/ops/fleet" | ConvertTo-Json
Invoke-RestMethod -Uri "$base/api/v1/ops/devices/$deviceId" | ConvertTo-Json
