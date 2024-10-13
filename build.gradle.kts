val kotlin_version: String by project
val kodein_version: String by project
val logback_version: String by project
val jupiter_version: String by project
val mockito_version: String by project
val assertj_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

group = "org.alterbit"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Netty Server
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-config-yaml")

    // Dependency Injection
    implementation("org.kodein.di:kodein-di-jvm:$kodein_version")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodein_version")

    // Logback Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // JSON Serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    // Tests
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockito_version")
    testImplementation("org.assertj:assertj-core:$assertj_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiter_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiter_version")
}

tasks.test {
    useJUnitPlatform()
}
