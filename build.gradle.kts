
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project

plugins {
    kotlin("jvm") version "1.9.24"
    id("io.ktor.plugin") version "2.3.11"
}

group = "com.caritos"
version = "0.0.1"

application {
    mainClass.set("com.caritos.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("at.favre.lib:bcrypt:0.9.0") // bcrypt for password hashing
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-server-freemarker:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version") // Replace with your Ktor version
    implementation("io.ktor:ktor-server-netty:$ktor_version") // Replace with your Ktor version
    implementation("io.ktor:ktor-server-html-builder:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}