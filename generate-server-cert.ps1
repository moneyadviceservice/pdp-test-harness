# Generates a self-signed server certificate for running the C&A Stub with SSL/mTLS.
# Outputs server-certificate.pem and server-certificate.key in the current directory.
# Safe to re-run: skips generation if both files already exist.

$CertFile = "server-certificate.pem"
$KeyFile  = "server-certificate.key"
$DaysValid = 365
$CN = "localhost"

if (-not (Get-Command openssl -ErrorAction SilentlyContinue)) {
    Write-Error ">>> openssl not found on PATH. Install Git for Windows or OpenSSL and try again."
    exit 1
}

if ((Test-Path $CertFile) -and (Test-Path $KeyFile)) {
    Write-Host ">>> $CertFile and $KeyFile already exist, skipping generation."
    exit 0
}

Write-Host ">>> Generating self-signed server certificate (RSA 4096, CN=$CN, valid $DaysValid days)..."

openssl req `
    -x509 `
    -newkey rsa:4096 `
    -keyout $KeyFile `
    -out $CertFile `
    -days $DaysValid `
    -nodes `
    -subj "/CN=$CN"

if ($LASTEXITCODE -ne 0) {
    Write-Error ">>> openssl failed. Ensure openssl is installed and available on PATH."
    exit 1
}

Write-Host ">>> Done. Generated:"
Write-Host "      $CertFile"
Write-Host "      $KeyFile"
