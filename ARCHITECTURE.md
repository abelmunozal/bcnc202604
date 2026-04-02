# Arquitectura Hexagonal - Prices Service

## 📐 Estructura del Proyecto

```
prices-service/
├── src/main/java/com/bcnc/prices/
│   ├── domain/                                    # ⬡ CAPA DE DOMINIO (CORE)
│   │   ├── model/
│   │   │   └── Price.java                        # Record inmutable (Java 21)
│   │   └── repository/
│   │       └── PriceRepository.java              # Puerto de salida (interface)
│   │
│   ├── application/                               # ⬡ CAPA DE APLICACIÓN
│   │   ├── port/in/
│   │   │   └── GetApplicablePriceQuery.java      # Puerto de entrada (use case)
│   │   └── service/
│   │       └── PriceQueryService.java            # Implementación del caso de uso
│   │
│   └── infrastructure/                            # ⬡ CAPA DE INFRAESTRUCTURA
│       ├── adapter/
│       │   ├── in/rest/                          # Adaptador REST (entrada)
│       │   │   ├── PriceController.java
│       │   │   ├── AuthController.java           # Endpoint de autenticación
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   └── dto/
│       │   │       ├── PriceResponse.java
│       │   │       ├── ErrorResponse.java
│       │   │       ├── TokenRequest.java         # DTO para solicitud de token
│       │   │       └── TokenResponse.java        # DTO para respuesta de token
│       │   └── out/persistence/                  # Adaptador JPA (salida)
│       │       ├── PriceEntity.java
│       │       ├── PriceMapper.java
│       │       ├── JpaPriceRepository.java
│       │       └── PriceRepositoryAdapter.java
│       ├── config/
│       │   ├── ApplicationConfig.java            # Inyección de dependencias
│       │   ├── SecurityConfig.java               # Spring Security + JWT
│       │   └── OpenApiConfig.java                # Swagger/OpenAPI
│       └── security/
│           └── JwtTokenService.java              # Servicio de generación de tokens JWT
│
├── src/main/resources/
│   ├── application.yml                           # Configuración (Virtual Threads)
│   ├── schema.sql                                # DDL de base de datos
│   └── data.sql                                  # Datos de prueba
│
├── src/test/java/
│   └── com/bcnc/prices/infrastructure/adapter/in/rest/
│       └── PriceControllerIntegrationTest.java   # 5 tests de integración
│
├── charts/prices-service/                        # Helm Charts para Kubernetes
├── Dockerfile                                    # Imagen Docker
├── docker-compose.yml                            # Orquestación local
└── build.gradle.kts                              # Gradle con Kotlin DSL
```

## 🎯 Principios SOLID Aplicados

### 1. **Single Responsibility Principle (SRP)**
- `Price.java`: Solo representa el modelo de dominio
- `PriceQueryService.java`: Solo ejecuta la lógica de búsqueda de precios
- `PriceController.java`: Solo maneja peticiones HTTP
- `PriceRepositoryAdapter.java`: Solo adapta la persistencia

### 2. **Open/Closed Principle (OCP)**
- Nuevos adaptadores pueden añadirse sin modificar el dominio
- Nuevos casos de uso se implementan como nuevos puertos

### 3. **Liskov Substitution Principle (LSP)**
- `PriceRepositoryAdapter` implementa `PriceRepository` sin alterar el contrato
- Mock implementations pueden sustituir implementaciones reales en tests

### 4. **Interface Segregation Principle (ISP)**
- `GetApplicablePriceQuery`: Interface específica para un caso de uso
- `PriceRepository`: Interface mínima con un solo método

### 5. **Dependency Inversion Principle (DIP)**
- El dominio NO depende de la infraestructura
- La infraestructura depende del dominio a través de interfaces (puertos)
- `ApplicationConfig.java` inyecta las dependencias

## 🔄 Flujo de Datos (Hexagonal)

### Flujo de Autenticación JWT

```
[POST /auth/token] 
    ↓
[AuthController] (Adaptador REST - Público)
    ↓
[JwtTokenService] (Servicio de Seguridad)
    ↓
[KeyPair RSA] (Bean de Configuración)
    ↓
[JWT Token Generado] (Firmado con clave privada RSA)
    ↓
[Cliente recibe token válido por 1 hora]
```

### Flujo de Consulta de Precios (Autenticado)

```
[GET /api/v1/prices + JWT Token] 
    ↓
[Spring Security Filter] (Validación JWT con clave pública RSA)
    ↓
[PriceController] (Adaptador REST - Puerto de Entrada)
    ↓
[GetApplicablePriceQuery] (Puerto de Entrada - Interface)
    ↓
[PriceQueryService] (Caso de Uso - Aplicación)
    ↓
[PriceRepository] (Puerto de Salida - Interface)
    ↓
[PriceRepositoryAdapter] (Adaptador JPA - Puerto de Salida)
    ↓
[JpaPriceRepository] (Spring Data JPA)
    ↓
[H2 Database]
```

## 🧩 Desacoplamiento Total

### Dominio (Core)
- **Sin dependencias** de Spring, JPA, o cualquier framework
- Solo Java puro (Records, interfaces)
- Reglas de negocio encapsuladas

### Aplicación
- Orquesta casos de uso
- Depende solo del dominio
- No conoce detalles de infraestructura

### Infraestructura
- Implementa los puertos definidos por el dominio
- Contiene toda la lógica técnica (REST, JPA, Security)
- Puede ser reemplazada sin afectar el core

## 🚀 Características Modernas

- ✅ **Java 21** con Virtual Threads
- ✅ **Records** para inmutabilidad
- ✅ **Spring Boot 3.4** con las últimas optimizaciones
- ✅ **Gradle Kotlin DSL** para configuración type-safe
- ✅ **OAuth2 Resource Server** con JWT (RSA-256)
- ✅ **Endpoint de autenticación** para generación de tokens JWT
- ✅ **OpenAPI 3.0** con Swagger UI
- ✅ **Spring Boot Actuator** para health checks
- ✅ **Docker** y **Kubernetes** ready

## 🔐 Seguridad JWT

### Arquitectura de Autenticación

La aplicación implementa un sistema de autenticación basado en **JWT (JSON Web Tokens)** con firma **RSA-256**:

#### Componentes

1. **SecurityConfig** (`infrastructure/config/`)
   - Configura Spring Security como OAuth2 Resource Server
   - Genera par de claves RSA (pública/privada) al inicio
   - Define reglas de autorización por endpoint
   - Configura JwtDecoder con clave pública

2. **JwtTokenService** (`infrastructure/security/`)
   - Servicio para generar tokens JWT
   - Firma tokens con clave privada RSA
   - Configura claims: issuer, subject, scope, exp, iat
   - Tokens válidos por 1 hora

3. **AuthController** (`infrastructure/adapter/in/rest/`)
   - Endpoint público `POST /auth/token`
   - Recibe username y genera token JWT
   - Mock para desarrollo (sin validación de credenciales)

#### Flujo de Seguridad

```
┌─────────────────────────────────────────────────────────────┐
│ 1. GENERACIÓN DE TOKEN                                      │
├─────────────────────────────────────────────────────────────┤
│ Cliente → POST /auth/token {"username": "test-user"}        │
│ AuthController → JwtTokenService.generateToken()            │
│ JwtTokenService → Firma JWT con clave privada RSA           │
│ Cliente ← Token JWT válido por 1 hora                       │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│ 2. VALIDACIÓN DE TOKEN                                      │
├─────────────────────────────────────────────────────────────┤
│ Cliente → GET /api/v1/prices + Header: Authorization Bearer │
│ SecurityFilterChain → Intercepta request                    │
│ JwtDecoder → Valida firma con clave pública RSA             │
│ JwtDecoder → Verifica expiración y claims                   │
│ Si válido → Permite acceso al endpoint                      │
│ Si inválido → 401 Unauthorized                              │
└─────────────────────────────────────────────────────────────┘
```

#### Endpoints Públicos vs Protegidos

**Públicos (sin autenticación):**
- `/auth/**` - Generación de tokens
- `/swagger-ui/**` - Documentación Swagger
- `/api-docs/**` - OpenAPI docs
- `/h2-console/**` - Consola H2
- `/actuator/**` - Health checks

**Protegidos (requieren JWT):**
- `/api/v1/prices/**` - Consulta de precios

#### Características de Seguridad

- ✅ **Stateless**: No se almacenan sesiones en servidor
- ✅ **RSA-256**: Algoritmo de firma asimétrica robusto
- ✅ **Expiración**: Tokens válidos por 1 hora
- ✅ **Claims estándar**: issuer, subject, exp, iat, scope
- ✅ **Mock IDP**: Sin dependencias externas para desarrollo

## 📊 Resolución de Prioridades

La lógica de negocio implementa:
1. Búsqueda de precios por fecha, producto y marca
2. Ordenación por prioridad DESC
3. Selección del primer resultado (mayor prioridad)

```java
@Query("SELECT p FROM PriceEntity p " +
       "WHERE p.productId = :productId " +
       "AND p.brandId = :brandId " +
       "AND p.startDate <= :applicationDate " +
       "AND p.endDate >= :applicationDate " +
       "ORDER BY p.priority DESC")
```

## 🧪 Testing

5 tests de integración que validan:
- ✅ Test 1: 2020-06-14 10:00 → 35.50€ (Tarifa 1)
- ✅ Test 2: 2020-06-14 16:00 → 25.45€ (Tarifa 2)
- ✅ Test 3: 2020-06-14 21:00 → 35.50€ (Tarifa 1)
- ✅ Test 4: 2020-06-15 10:00 → 30.50€ (Tarifa 3)
- ✅ Test 5: 2020-06-16 21:00 → 38.95€ (Tarifa 4)

Plus tests adicionales para:
- Producto no encontrado (404)
- Acceso sin autenticación (401)
- Formato de fecha inválido (400)
- Parámetros faltantes (400)
