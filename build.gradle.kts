import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jpa)
}

group = "es.unizar.webeng"
version = "2025-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.validation)
    developmentOnly(libs.spring.boot.devtools)
    implementation(libs.bootstrap)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation("org.springframework.security:spring-security-crypto")
    runtimeOnly(libs.h2)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
    }
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootRun> {
	sourceResources(sourceSets["main"])
}