plugins {
    java
    // 🔹 Plugin para Spring Boot, necesario para construir y ejecutar la aplicación
    id("org.springframework.boot") version "3.2.0"
    // 🔹 Plugin para la gestión de dependencias en proyectos Spring
    id("io.spring.dependency-management") version "1.1.4"
}

// 🔹 Configuración del grupo y versión del proyecto
group = "co.edu.uniquindio"
version = "1.0-SNAPSHOT"
// 🔹 Descripción del proyecto incluyendo la firma ROT5
description =  "Firma ROT5:  QwE Pjanfyfr(Nicolas Cabrera Serrano)"

java {
    toolchain {
        // 🔹 Define la versión de Java a utilizar (Java 21)
        languageVersion = JavaLanguageVersion.of(22)
    }
}

configurations {
        compileOnly {
            // 🔹 Asegura que los procesadores de anotaciones solo se incluyan en tiempo de compilación
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

repositories {
    // 🔹 Define Maven Central como el repositorio para las dependencias
    mavenCentral()
}


dependencies {
    // 🔹 Starter Web de Spring Boot: Proporciona funcionalidades web básicas
    implementation("org.springframework.boot:spring-boot-starter-web")

    // 🔹 Starter de Seguridad de Spring Boot: Agrega funciones de autenticación y seguridad
    implementation("org.springframework.boot:spring-boot-starter-security")

    // 🔹 Hibernate Core: Framework ORM para trabajar con bases de datos en Java
    implementation("org.hibernate:hibernate-core:6.4.0.Final")

    // 🔹 Para validaciones con @NotBlank, @NotNull, etc.
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // 🔹 Para validaciones de Hibernate como @Length
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    // 🔹 Starter JPA de Spring Boot: Habilita funcionalidades JPA con Hibernate
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // 🔹 Lombok: Reduce el código repetitivo en clases Java (Getters, Setters, etc.)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // 🔹 Mapper: Crea instancias de mapeo.
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // 🔹 libphonenumber: Librería para los prefijos y los paises
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.30")

    // 🔹 Cloudinary: Librería para el guardado de imágenes en la web
    implementation("com.cloudinary:cloudinary-http44:1.32.2")

    // 🔹 Units of Measurement API: librería para manejo de unidades físicas
    implementation("tech.units:indriya:2.1.3")

    implementation("ognl:ognl:3.2.21") // versión compatible


    // 🔹 Librería para visualizar los logs
    implementation("org.springframework.boot:spring-boot-starter-logging") // Logback
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    // Libreria para generar PDF
    implementation("org.thymeleaf:thymeleaf-spring5")
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22")
    implementation("com.lowagie:itext:2.1.7") // requerido por Flying Saucer
    implementation("org.thymeleaf:thymeleaf-spring5:3.0.15.RELEASE")
    implementation("com.sun.activation:jakarta.activation:1.2.2")


    implementation("com.google.api-client:google-api-client:2.4.1")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.3")


    // Soporte para JSON via Jackson
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // 🔹 Simple Java Mail: Librería para enviar correos electrónicos
    implementation("org.simplejavamail:simple-java-mail:8.12.5")
    implementation("org.simplejavamail:batch-module:8.12.5")

    // 🔹 Kotlin Reflection: Soporta reflexión en tiempo de ejecución en Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // 🔹 Librería estándar de Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // Oracle JDBC Driver
    implementation("com.oracle.database.jdbc:ojdbc11:21.8.0.0")

    // 🔹 Starter JDBC de Spring Boot: Habilita funcionalidades JDBC
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // 🔹 Librería Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Dependencia de prueba
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Librería para enviar correos electrónicos
    implementation("org.apache.commons:commons-email2-jakarta:2.0.0-M1")
}

tasks.test {
    useJUnitPlatform()
}