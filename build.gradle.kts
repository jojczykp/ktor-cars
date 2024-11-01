val kotlin_version: String by project
val kodein_version: String by project
val logback_version: String by project
val jdbi_version: String by project
val flyway_version: String by project
val jupiter_version: String by project
val mockk_version: String by project
val kotest_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
}

group = "org.alterbit.ktorcars"
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

    // JSON Serialization
    implementation("io.ktor:ktor-server-resources-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    // Dependency Injection
    implementation("org.kodein.di:kodein-di-jvm:$kodein_version")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodein_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.9.0")

    // DB
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation("org.flywaydb:flyway-database-postgresql:$flyway_version")
    implementation("org.jdbi:jdbi3-core:$jdbi_version")  // Use the latest version
    implementation("org.jdbi:jdbi3-sqlobject:$jdbi_version")
    implementation("org.jdbi:jdbi3-kotlin:$jdbi_version")
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:$jdbi_version")

    // Tests
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk-jvm:$mockk_version")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotest_version")
    testImplementation("io.kotest:kotest-framework-api-jvm:$kotest_version")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
    testImplementation("org.testcontainers:postgresql:1.20.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-api:$jupiter_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiter_version")
    testRuntimeOnly("io.kotest:kotest-runner-junit5-jvm:$kotest_version")
}

tasks.test {
    useJUnitPlatform()
}
