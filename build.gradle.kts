import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

val exposed_version: String by project
val h2_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_version: String by project
val flyway_version: String by project
val koin_version: String by project
val akkurate_version: String by project
val jupiter_version: String by project
val test_container_version: String by project
val ktor_server_tests_version: String by project

plugins {
    kotlin("jvm") version "2.2.0"
    id("io.ktor.plugin") version "3.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
    id("org.flywaydb.flyway") version "11.11.2"
    id("com.google.devtools.ksp") version "2.1.21-2.0.2"
}

repositories {
    mavenCentral()
    maven { url = URI("https://jitpack.io") }
}

kotlin {
    jvmToolchain(21)
}

group = "org.pumped"
version = "1.0.0"

application {
    mainClass = "ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
    maven {
        url = uri("https://packages.confluent.io/maven")
        name = "confluence"
    }
}

sourceSets {
    create("integrationTest") {
        kotlin {
            srcDir("src/integration/kotlin")
        }
        resources {
            srcDir("src/integration/resources")
        }
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

tasks.withType<Test>().configureEach {
    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
        )
        showExceptions = true
        showCauses = true
        showStackTraces = true
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

dependencies {
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-cors:$ktor_version")
    implementation("io.ktor:ktor-server-default-headers:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")

    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")
    implementation("io.ktor:ktor-server-resources:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:${ktor_version}")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposed_version")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.4")

    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
    implementation("io.ktor:ktor-client-cio:${ktor_version}")
    implementation("io.github.hansanto:kault:1.5.2")
    // required for kault

    implementation("org.flywaydb:flyway-core:$flyway_version")
    implementation("org.flywaydb:flyway-mysql:$flyway_version")

    implementation("com.ucasoft.ktor:ktor-simple-cache:0.55.3")
    implementation("com.ucasoft.ktor:ktor-simple-redis-cache:0.55.3")
    implementation("io.github.flaxoos:ktor-server-rate-limiting:2.2.1")
    implementation("io.github.smiley4:ktor-openapi:5.2.0")
    implementation("io.github.smiley4:ktor-swagger-ui:5.2.0")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.github.StaticFX:ktor-middleware:v1.1.1")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    implementation("dev.nesk.akkurate:akkurate-ktor-server:$akkurate_version")
    implementation("dev.nesk.akkurate:akkurate-core:$akkurate_version")
    implementation("io.ktor:ktor-server-metrics:$ktor_version")
    implementation("dev.hayden:khealth:3.0.2")
    implementation("io.github.flaxoos:ktor-server-task-scheduling-core:2.2.1")
    implementation("io.github.flaxoos:ktor-server-task-scheduling-redis:2.2.1")
    implementation("io.github.damirdenis-tudor:ktor-server-rabbitmq:1.3.6")
    ksp("dev.nesk.akkurate:akkurate-ksp-plugin:0.11.0")

    implementation("at.favre.lib:bcrypt:0.10.2")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("com.h2database:h2:${h2_version}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:1.14.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiter_version")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$jupiter_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("org.testcontainers:testcontainers:$test_container_version")
    testImplementation("org.testcontainers:mariadb:$test_container_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_server_tests_version")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_server_tests_version")
    testImplementation("io.ktor:ktor-client-cio:$ktor_server_tests_version")
    implementation("com.redis.testcontainers:testcontainers-redis:1.6.4")
    testImplementation("org.junit.platform:junit-platform-launcher:1.12.2") // ist dependent auf die junit version hat aber ein anderen Release
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true

    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-XXLanguage:+BreakContinueInInlineLambdas"))
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    useJUnitPlatform()
}

tasks.named("check") {
    dependsOn("integrationTest")
}

// For unit tests
tasks.named<Test>("test") {
    testLogging {
        events("passed", "skipped", "failed")
        displayGranularity = 2
        showStackTraces = true
        showExceptions = true
        showCauses = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.named<Test>("integrationTest") {
    testLogging {
        events("passed", "skipped", "failed")
        displayGranularity = 2
        showStackTraces = true
        showExceptions = true
        showCauses = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

jib {
    from {
        image = "eclipse-temurin:21-jdk"
    }
    to {
        image = System.getenv("DOCKER_IMAGE")
    }
}