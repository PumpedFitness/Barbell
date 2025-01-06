import java.net.URI

val kotlin_version = "2.1.0"
val ktor_version = "3.0.1"
val logback_version = "1.4.14"
val exposedVersion = "0.56.0"
val koin_version = "4.0.1"

plugins {
    kotlin("jvm") version "2.1.0"
    id("io.ktor.plugin") version "3.0.1"
    kotlin("plugin.serialization") version "2.1.0"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

group = "app.pumped"
version = "prerelease-0.0.1"

application {
    mainClass.set("app.pumped.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    maven { url = URI("https://jitpack.io") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")

    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-double-receive:$ktor_version")

    implementation("io.ktor:ktor-server-sessions:$ktor_version")

    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-request-validation-jvm:$ktor_version")

    //ktor third party plugins
    implementation("io.github.hmiyado:ktor-csrf-protection:2.0.1")
    implementation("com.github.StaticFX:ktor-middleware:v1.1.1")
    implementation("io.github.flaxoos:ktor-server-task-scheduling-core:2.1.1")
    implementation("io.github.flaxoos:ktor-server-task-scheduling-redis:2.1.1")
    implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koin_version"))
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.insert-koin:koin-core")

    //Database
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.2")

    //kser
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.github.StaticFX.kotlin-exposed-relationships:annotations:1.0.6")
    ksp("com.github.StaticFX.kotlin-exposed-relationships:processor:1.0.6")

    //util
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")

    //testing
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        java.srcDirs()
    }
}
