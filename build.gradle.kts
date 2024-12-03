val kotlin_version = "2.1.0"
val ktor_version = "3.0.1"
val logback_version = "1.4.14"

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.1"
}

group = "app.pumped"
version = "prerelease-0.0.1"

application {
    mainClass.set("app.pumped.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    implementation("io.ktor:ktor-server-sessions:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    //ktor third party plugins
    implementation("io.github.hmiyado:ktor-csrf-protection:2.0.1")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    //testing
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
