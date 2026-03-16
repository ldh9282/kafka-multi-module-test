# Product Service

Kafka 기반 상품 생성 이벤트 예제를 위한 멀티 모듈 Spring Boot 프로젝트입니다.

- `products`: 상품 생성 API + Kafka Producer
- `notifications-email`: 상품 생성 이벤트 Kafka Consumer
- `common`: 공통 DTO, 이벤트 모델, 인터셉터

## 프로젝트 구조

```text
product-service/
  common/
  products/
  notifications-email/
```

## 기술 스택

- Java 25
- Spring Boot 4.0.3
- Spring Web MVC
- Spring for Apache Kafka
- Jakarta Validation
- Gradle (Multi-module)

## 사전 준비

1. JDK 25 설치
2. Kafka 실행 (`localhost:9092,localhost:9094`)

## 빌드

```powershell
.\gradlew.bat build
```

모듈 단위 빌드:

```powershell
.\gradlew.bat :products:build
.\gradlew.bat :notifications-email:build
```

## 실행

루트 디렉터리(`product-service`)에서 실행합니다.

```powershell
.\gradlew.bat :products:bootRun
.\gradlew.bat :notifications-email:bootRun
```

두 모듈 모두 기본 포트는 랜덤(`server.port: 0`)입니다.
필요 시 포트를 고정해 실행할 수 있습니다.

```powershell
.\gradlew.bat :products:bootRun --args="--server.port=8080"
.\gradlew.bat :notifications-email:bootRun --args="--server.port=8081"
```

## 테스트

```powershell
.\gradlew.bat test
```

모듈 단위 테스트:

```powershell
.\gradlew.bat :products:test
.\gradlew.bat :notifications-email:test
.\gradlew.bat :common:test
```

현재 테스트는 `contextLoads` 중심의 스모크 테스트 수준입니다.

## 이벤트 흐름

1. `POST /api/products` 요청 수신 (`products`)
2. `ProductCreatedEvent`를 `product-created-events-topic`으로 발행 (`products`)
3. 같은 토픽을 구독해 이벤트 로그 처리 (`notifications-email`)

## API 명세

### 상품 생성

- Method: `POST`
- Path: `/api/products`
- Content-Type: `application/json`

요청 예시:

```json
{
  "title": "Keyboard",
  "price": 129.99,
  "quantity": 10
}
```

검증 규칙:

- `title`: `@NotNull`, `@NotBlank`
- `price`: `@NotNull`, `@DecimalMin(0.01)`, `@Digits(integer=15, fraction=2)`
- `quantity`: `@Min(1)`

성공 응답 예시 (`201 Created`):

```json
{
  "header": {
    "ok": true,
    "status": 201,
    "reason": "Created",
    "error": false,
    "errorMsg": null,
    "auditor": "SYSTEM",
    "logId": "4f4633f5-e34f-4fd7-8e3a-8f6fe95f5f42",
    "timestamp": "20260306014010123456",
    "formattedTime": "2026-03-06 01:40:10.123456"
  },
  "body": {
    "productId": "a2b3c4d5-1111-2222-3333-444455556666",
    "title": "Keyboard",
    "price": 129.99,
    "quantity": 10
  }
}
```

오류 응답도 동일한 `BaseResponse` 구조를 사용하며, `body`는 `null`입니다.

## Kafka 설정 요약

- Producer (`products`):
  - bootstrap servers: `localhost:9092,localhost:9094`
  - key serializer: `StringSerializer`
  - value serializer: `JsonSerializer`
- Consumer (`notifications-email`):
  - bootstrap servers: `localhost:9092,localhost:9094`
  - group id: `product-created-events`
  - trusted package: `com.kafka01.common.event`

## 참고

- Swagger UI 의존성(`springdoc-openapi-starter-webmvc-ui`)이 포함되어 있습니다.
