plugins {
    // Lets the Java Toolchain auto-provision the requested JDK (Temurin 21)
    // when it is not already installed on the build machine.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "prices-service"
