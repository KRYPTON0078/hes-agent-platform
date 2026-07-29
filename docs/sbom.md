# SBOM and supply-chain notes

## Generate SBOM (CycloneDX)

```bash
mvn org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom
```

## Expectations
- Pin dependency versions in parent POM
- Run Trivy FS scan in CI on every PR
- Prefer Temurin JDK 21 official images in Dockerfiles
- Document any intentional CVEs with expiry in security ADRs

## Signing (future)
Release artifacts should be signed (Sigstore/cosign) before production distribution.
