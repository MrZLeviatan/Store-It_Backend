<p align="center">
  <img src="https://res.cloudinary.com/dehltwwbu/image/upload/v1745107200/assets_task_01js87pt2kf1wtk7aw2n5b5dy8_img_0_q4tyju.webp" alt="Store-It Logo" width="300"/>
</p>

# Store-It! Backend

**Store-It!** es un sistema de gestión de bodegas de clase mundial desarrollado por nuestra consultora para empresas de almacenaje que buscan modernizar y digitalizar la administración de sus espacios, contratos y movimientos de productos. Este repositorio contiene el código fuente del backend desarrollado con **Spring Boot** y tecnologías modernas que garantizan escalabilidad, mantenibilidad y seguridad.

---

## 📌 Descripción

Store-It! permite a las empresas de almacenaje:

- Registrar clientes, agentes de venta y contratos de almacenamiento.
- Controlar el ingreso, ubicación y retiro de productos dentro de las bodegas.
- Emitir documentos físicos y digitales para cada operación de entrada/salida.
- Consultar en línea el estado del contrato, los productos almacenados y los movimientos realizados.
- Generar facturación mensual automática y gestionada por el agente de ventas.
- Integrar fácilmente almacenamiento de imágenes, validación de teléfonos internacionales y generación de PDFs.

---

## ⚙️ Características del Sistema

- ✅ Registro de clientes y asignación de sectores con capacidad contratada.
- ✅ Ingreso de productos mediante personal de bodega, con cálculo automático de ubicación.
- ✅ Documentación detallada para entradas y salidas de productos.
- ✅ Consultas en línea de productos y movimientos por cliente autenticado.
- ✅ Facturación mensual con emisión de facturas físicas y digitales.
- ✅ Validación internacional de teléfonos con `libphonenumber`.
- ✅ Almacenamiento de imágenes en la nube con `Cloudinary`.
- ✅ Generación de documentos en PDF.
- ✅ Seguridad robusta con Spring Security y JWT.
- ✅ Panel de control para agentes de ventas.

---

## 🧩 Patrones de Diseño Utilizados

### 1. 🧱 Arquitectura General: **Arquitectura en Capas** (Layered Architecture)

**Descripción:**  
Este patrón organiza el sistema en capas separadas: **Presentación**, **Lógica de Negocio**, **Acceso a Datos** y **Modelo de Dominio**.  
Cada capa tiene una responsabilidad clara y se comunica únicamente con la capa inmediata inferior o superior.

**Ventajas:**  
- Separación de responsabilidades  
- Mayor facilidad de prueba y mantenimiento  
- Mejor escalabilidad del proyecto  

---

### 2. 🧠 Backend Spring Boot: **Stereotype-based Component Model**

**Descripción:**  
Spring aplica internamente un patrón de organización de componentes usando anotaciones como `@Component`, `@Service`, `@Repository` y `@Controller`.  
Estas anotaciones permiten una gestión automatizada de dependencias y separación de responsabilidades.

**Ventajas:**  
- Facilidad para aplicar inyección de dependencias  
- Identificación clara del rol de cada clase  
- Modularidad y reutilización de componentes  

---

### 3. 🔀 Patrón Strategy

**Descripción:**  
Permite definir una familia de algoritmos, encapsular cada uno y hacerlos intercambiables sin alterar el código que los utiliza.  
Store-It! emplea este patrón en casos como:
- Generación de documentos PDF
- Validación de formatos de datos (teléfonos, correos, etc.)
- Envío de correos electrónicos

**Ventajas:**  
- Aislamiento de lógica específica  
- Flexibilidad para cambiar comportamientos en tiempo de ejecución  
- Mayor limpieza en el código frente a estructuras `if/else` complejas  

---

## 🙌 Agradecimientos

Queremos expresar un profundo agradecimiento a todos los desarrolladores, testers, diseñadores y visionarios que contribuyeron a la creación de **Store-It!**. Este sistema representa la unión de buenas prácticas de ingeniería de software, visión empresarial y compromiso con la excelencia.

También agradecemos a las comunidades de **Spring**, **Oracle**, **MapStruct**, **Cloudinary**, y **libphonenumber**, cuyos aportes han sido fundamentales para el desarrollo de esta plataforma moderna y robusta.

> ✨ _“Los grandes sistemas no se construyen con suerte, sino con propósito, colaboración y buen diseño.”_

---

## 📚 Librerías y Tecnologías Utilizadas

```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") // Web básico
    implementation("org.springframework.boot:spring-boot-starter-security") // Seguridad
    implementation("org.hibernate:hibernate-core:6.4.0.Final") // ORM
    implementation("jakarta.validation:jakarta.validation-api:3.0.2") // Validaciones
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final") // Validaciones avanzadas
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") // JPA + Hibernate

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.30") // Teléfonos internacionales
    implementation("com.cloudinary:cloudinary-http44:1.32.2") // Imágenes en la nube
    implementation("tech.units:indriya:2.1.3") // Unidades de medida
    implementation("ognl:ognl:3.2.21")

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4") // Logs

    implementation("org.thymeleaf:thymeleaf-spring5")
    implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22") // PDF
    implementation("com.lowagie:itext:2.1.7")
    implementation("com.sun.activation:jakarta.activation:1.2.2")

    implementation("com.google.api-client:google-api-client:2.4.1")
    implementation("com.google.http-client:google-http-client-jackson2:1.43.3")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6") // JWT

    implementation("org.simplejavamail:simple-java-mail:8.12.5")
    implementation("org.simplejavamail:batch-module:8.12.5") // Correos

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib") // Kotlin

    implementation("com.oracle.database.jdbc:ojdbc11:21.8.0.0") // Oracle
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0") // Swagger

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.apache.commons:commons-email2-jakarta:2.0.0-M1") // Email
}
'''


