plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "co.edu.uniquindio"
version = "1.0-SNAPSHOT"
description =  "Firma ROT5:  QwE Pjanfyfr(Nicolas Cabrera Serrano)"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.hibernate:hibernate-core:6.4.0.Final")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("com.sun.mail:javax.mail:1.6.2") // Java Mail Sender
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("org.postgresql:postgresql:42.5.0") // Driver de PostgreSQL
    implementation("org.postgresql:postgresql:42.7.1") // 🔹 Driver JDBC para PostgreSQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}