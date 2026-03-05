# Product Service

`product-service`는 Spring Boot 기반의 상품 이벤트 발행 서비스입니다.
`POST /api/products` 요청을 받아 Kafka 토픽으로 상품 생성 이벤트를 비동기로 전송합니다.

## 프로젝트 구성

- `products`: API, Kafka Producer, 예외 처리
- `common`: 공통 응답(`BaseResponse`), 이벤트 모델, 로깅 인터셉터

```text
product-service/
  common/
  products/
```

## 기술 스택

- Java 25
- Spring Boot 4.0.3
- Spring Web MVC
- Spring for Apache Kafka
- Jakarta Validation
- Gradle

## 모듈 관계

`products/settings.gradle`에서 `common` 모듈을 포함하도록 설정되어 있습니다.

- `include 'common'`
- `project(':common').projectDir = new File(settingsDir.parent, 'common')`

실행과 테스트는 `products` 모듈에서 수행하면 됩니다.

## 사전 준비

1. JDK 25 설치
2. Kafka 클러스터 실행
3. `products/src/main/resources/application.yaml` 확인

기본 Kafka 설정:

- `bootstrap-servers: localhost:9092,localhost:9094`
- key serializer: `StringSerializer`
- value serializer: `JsonSerializer`

애플리케이션 시작 시 아래 토픽을 생성합니다.

- topic: `product-created-events-topic`
- partitions: `3`
- replicas: `3`
- `min.insync.replicas=2`

## 실행 방법

```powershell
cd products
.\gradlew.bat bootRun
```

기본 포트는 랜덤(`server.port: 0`)입니다. 고정 포트로 실행:

```powershell
cd products
.\gradlew.bat bootRun --args="--server.port=8080"
```

## 테스트

```powershell
cd products
.\gradlew.bat test
```

현재 테스트 코드는 `contextLoads` 수준의 기본 스모크 테스트만 포함합니다.

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
  "body": "success"
}
```

실패 응답도 동일한 `BaseResponse` 구조를 사용하며, `body`는 `null`입니다.

## 예외 처리

`products/exception/GlobalExceptionHandler`에서 아래 상태 코드를 공통 처리합니다.

- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`
- `500 Internal Server Error`

검증 실패(`MethodArgumentNotValidException`) 시 첫 번째 필드 에러 메시지를 `header.errorMsg`에 담아 반환합니다.

## 로깅/추적

- `common/interceptor/LogInterceptor`
- 요청 시작 시 `logId`(UUID) 생성 후 MDC 저장
- 컨트롤러 시작/종료 로그 및 실행 시간(ms) 출력
- `BaseResponse.Header.logId`에 동일한 MDC `logId` 포함
- Kafka 이벤트(`ProductCreatedEvent`)에도 `logId` 포함

로그 포맷은 `products/src/main/resources/logback-spring.xml`에 정의되어 있습니다.

## Kafka 이벤트 스키마

토픽 `product-created-events-topic`으로 발행되는 이벤트 모델:

```json
{
  "logId": "uuid",
  "productId": "uuid",
  "title": "string",
  "price": 129.99,
  "quantity": 10
}
```

`productId`는 서버에서 UUID로 생성됩니다.
