# 9. Gradle Toolchains instead of an in-repo JDK

- Status: Accepted
- Date: 2026-06-09

## Context

The build pinned `org.gradle.java.home=./jdk21`, pointing at a folder that is git-ignored and
therefore absent on a fresh clone — the build failed until a JDK was manually extracted there.
The aim was reproducibility on machines whose system JDK is incompatible (here, JDK 25).

## Decision

Pin the **compile/run** JDK with the Gradle Java Toolchain (Temurin 21) in `build.gradle.kts`,
and add the foojay resolver in `settings.gradle.kts` so the toolchain is auto-provisioned when
not installed. Gradle itself only needs any supported JDK (17+) on the PATH to bootstrap.

## Consequences

- A fresh clone builds with no manual JDK setup; the compile JDK is reproducible and explicit.
- Provisioning the toolchain needs network access the first time if Temurin 21 is absent, and
  the Gradle bootstrap JDK must be a version Gradle 8.11 supports.
