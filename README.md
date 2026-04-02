# Prices Service

Microservicio REST desarrollado con **Spring Boot 3.4** y **Java 21** que gestiona precios de productos aplicando **Arquitectura Hexagonal** y principios **SOLID**.

## 🏗️ Arquitectura

El proyecto sigue una **Arquitectura Hexagonal (Ports & Adapters)** con separación clara de responsabilidades:

```
com.bcnc.prices/
├── domain/                          # Capa de Dominio (Core)
│   ├── model/                       # Entidades de dominio puras
│   │   └── Price.java              # Record inmutable sin dependencias
│   └── repository/                  # Puertos de salida (interfaces)
│       └── PriceRepository.java
├── application/                     # Capa de Aplicación
│   ├── port/in/                    # Puertos de entrada (casos de uso)
│   │   └── GetApplicablePriceQuery.java
│   └── service/                    # Servicios de aplicación
│       └── PriceQueryService.java
└── infrastructure/                  # Capa de Infraestructura
    ├── adapter/
    │   ├── in/rest/                # Adaptador REST (Controllers, DTOs)
    │   │   ├── PriceController.java
    │   │   ├── GlobalExceptionHandler.java
    │   │   └── dto/
    │   └── out/persistence/        # Adaptador de Persistencia (JPA)
    │       ├── PriceEntity.java
    │       ├── PriceMapper.java
    │       ├── JpaPriceRepository.java
    │       └── PriceRepositoryAdapter.java
    └── config/                     # Configuración de Framework
        ├── ApplicationConfig.java
        ├── SecurityConfig.java
        └── OpenApiConfig.java
```

## 🚀 Stack Tecnológico

- **Java 21** (Virtual Threads habilitados)
- **Spring Boot 3.4.0**
- **Gradle 8.10** (Kotlin DSL)
- **H2 Database** (In-Memory)
- **Spring Security** (OAuth2 Resource Server con JWT)
- **OpenAPI/Swagger** (Documentación interactiva)
- **JUnit 5** (Testing)

## 📋 Requisitos Previos

- **JDK 21 Embebida** (Eclipse Temurin) - Ver instrucciones de instalación abajo
- **Gradle 8.10+** (incluido wrapper)

### ⚙️ Configuración de JDK Embebida (Portable Build)

Este proyecto utiliza una **JDK 21 embebida** para garantizar builds reproducibles y portables, independientes del JDK instalado en el sistema.

**Pasos de configuración:**

1. **Descargar Eclipse Temurin JDK 21 LTS** (formato ZIP):
   - Windows: https://adoptium.net/temurin/releases/?version=21
   - Seleccionar: `Windows x64 JDK .zip`

2. **Extraer el contenido** en la raíz del proyecto:
   ```
   bcnc202604/
   ├── jdk21/              ← Extraer aquí el contenido del ZIP
   │   ├── bin/
   │   ├── lib/
   │   └── ...
   ├── src/
   ├── build.gradle.kts
   └── ...
   ```

3. **Verificar la configuración**:
   ```powershell
   .\check-env.ps1
   ```
   
   Este script verifica que Gradle esté usando la JDK embebida y no la del sistema.

**¿Por qué JDK embebida?**
- ✅ **Portabilidad**: El proyecto es autocontenido y no depende del PATH del sistema
- ✅ **Reproducibilidad**: Todos los evaluadores usan exactamente la misma versión de JDK
- ✅ **Aislamiento**: Evita conflictos con otras versiones de Java instaladas (ej: JDK 25)

**Configuración técnica:**
- `gradle.properties` → `org.gradle.java.home=./jdk21`
- `build.gradle.kts` → Java Toolchain configurado para JDK 21 (Eclipse Temurin)

## 🔧 Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd bcnc202604
```

### 2. Compilar el proyecto
```bash
./gradlew clean build
```

### 3. Ejecutar la aplicación
```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación API

Una vez iniciada la aplicación, accede a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console

### Endpoint Principal

**GET** `/api/v1/prices`

**Parámetros:**
- `applicationDate` (required): Fecha de aplicación en formato ISO-8601 (ej: `2020-06-14T10:00:00`)
- `productId` (required): ID del producto (ej: `35455`)
- `brandId` (required): ID de la cadena (ej: `1`)

**Ejemplo de petición:**
```bash
curl -X GET "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1" \
  -H "Authorization: Bearer <token>"
```

**Respuesta exitosa (200):**
```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.50,
  "currency": "EUR"
}
```

## 🧪 Testing

El proyecto incluye tests de integración completos que cubren los 5 escenarios requeridos:

```bash
./gradlew test
```

### Escenarios de Test

1. **Test 1**: Petición a las 10:00 del día 14 → Tarifa 1 (35.50€)
2. **Test 2**: Petición a las 16:00 del día 14 → Tarifa 2 (25.45€)
3. **Test 3**: Petición a las 21:00 del día 14 → Tarifa 1 (35.50€)
4. **Test 4**: Petición a las 10:00 del día 15 → Tarifa 3 (30.50€)
5. **Test 5**: Petición a las 21:00 del día 16 → Tarifa 4 (38.95€)

## 🔐 Seguridad

La aplicación implementa **Spring Security** como **Resource Server** con validación JWT:

- Los endpoints `/api/v1/prices/**` requieren autenticación JWT
- Los endpoints de documentación (`/swagger-ui.html`, `/api-docs`) están públicos
- Los endpoints de autenticación (`/auth/**`) están públicos
- JWT decoder con clave RSA estática generada al inicio

### 🔑 Obtener un Token JWT

Para acceder a los endpoints protegidos, primero debes obtener un token JWT:

**Endpoint:** `POST /auth/token`

**Request:**
```bash
curl -X POST "http://localhost:8080/auth/token" \
  -H "Content-Type: application/json" \
  -d '{"username": "test-user"}'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwcmljZXMtc2VydmljZSIsInN1YiI6InRlc3QtdXNlciIsImV4cCI6MTcxMjA2NDAwMCwiaWF0IjoxNzEyMDYwNDAwLCJzY29wZSI6InJlYWQgd3JpdGUifQ...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

**Características del token:**
- ✅ Válido por **1 hora** (3600 segundos)
- ✅ Firmado con **RSA-256**
- ✅ Contiene claims: `issuer`, `subject`, `scope`, `exp`, `iat`

### 🔒 Usar el Token

Una vez obtenido el token, inclúyelo en el header `Authorization` de tus peticiones:

```bash
curl -X GET "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1" \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 📋 Flujo de Autenticación Completo

```
1. Cliente solicita token → POST /auth/token {"username": "test-user"}
2. Servidor genera JWT firmado con clave privada RSA
3. Cliente recibe token válido por 1 hora
4. Cliente incluye token en header: Authorization: Bearer <token>
5. Servidor valida token con clave pública RSA
6. Si es válido, permite acceso al endpoint protegido
```

**Nota:** El token es generado automáticamente sin validación de credenciales (mock para desarrollo). En producción, este endpoint debería validar usuario/contraseña contra una base de datos o servicio de identidad.

## 🐳 Docker

### Construir imagen
```bash
docker build -t prices-service:latest .
```

### Ejecutar contenedor
```bash
docker run -p 8080:8080 prices-service:latest
```

### Docker Compose
```bash
docker-compose up
```

## ☸️ Kubernetes (Helm)

Los Helm Charts están disponibles en la carpeta `/charts`:

```bash
helm install prices-service ./charts/prices-service
```

## 📊 Datos de Prueba

La base de datos H2 se inicializa automáticamente con los siguientes datos:

| BRAND_ID | START_DATE          | END_DATE            | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|---------------------|---------------------|------------|------------|----------|-------|------|
| 1        | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1          | 35455      | 0        | 35.50 | EUR  |
| 1        | 2020-06-14 15:00:00 | 2020-06-14 18:30:00 | 2          | 35455      | 1        | 25.45 | EUR  |
| 1        | 2020-06-15 00:00:00 | 2020-06-15 11:00:00 | 3          | 35455      | 1        | 30.50 | EUR  |
| 1        | 2020-06-15 16:00:00 | 2020-12-31 23:59:59 | 4          | 35455      | 1        | 38.95 | EUR  |

## 🎯 Lógica de Negocio

- Si múltiples tarifas coinciden en un rango de fechas, se aplica la de **mayor prioridad** (valor numérico más alto)
- El endpoint devuelve siempre un **único resultado**
- Validación estricta de parámetros de entrada

## 📝 Principios Aplicados

- ✅ **SOLID**: Responsabilidad única, inversión de dependencias, segregación de interfaces
- ✅ **Clean Code**: Código legible, nombres descriptivos, funciones pequeñas
- ✅ **DRY**: Sin duplicación de lógica
- ✅ **Hexagonal Architecture**: Desacoplamiento total entre capas
- ✅ **Virtual Threads**: Optimización de rendimiento con Java 21

## 📄 Licencia

Este proyecto es una prueba técnica para BCNC.
