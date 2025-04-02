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
    implementation("com.oracle.database.jdbc:ojdbc8:21.9.0.0") // Oracle SQL

    testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")


}

tasks.test {
    useJUnitPlatform()
}