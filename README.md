# Personalized Diet Manager — Backend

사용자의 신체 정보와 목표를 입력받아 **하루 권장 칼로리·탄단지(매크로)를 계산**하고, **식단·운동·체중을 기록**해 권장량 대비 섭취량과 소모량, 변화 추이를 비교하는 REST API 서버입니다.

핵심 공학 포인트는 **전략(Strategy) 패턴을 세 곳에 독립 적용**한 것입니다.

- **칼로리·매크로 계산** — 목표별(다이어트·벌크업·체중 유지) 계산 로직 분리 (`CalorieStrategy`)
- **음식 신호등 분류** — 같은 목표 기준으로 음식을 녹/노/적으로 분류 (`FoodGradeStrategy`)
- **운동 소모 칼로리 계산** — 운동 종류별(달리기·걷기·자전거·수영·웨이트 트레이닝) 전략 분리 (`ExerciseCalorieStrategy`)

세 전략 모두 Spring DI + `EnumMap`으로 관리해, 새 목표나 운동 종류를 추가해도 분기문 수정 없이 구현체만 추가하면 됩니다(OCP 준수).

---

## 주요 기능

- **인증/인가**: 회원가입, 로그인 및 JWT 기반 보안 액세스
- **신체 정보 관리**: 프로필 등록/조회/수정, 목표별 권장 칼로리·탄단지 계산
- **식단 기록**: 식단 기록 등록/조회/수정/삭제, 날짜별 총 섭취 칼로리 및 매크로 합산
- **운동 기록**: 운동 종류·시간·강도에 따른 실시간 소모 칼로리 계산 및 기록 저장
- **신호등 음식 분류**: 목표 기준 음식 신호등(GREEN/YELLOW/RED) 등급 부여
- **통계/리포트**: 일일 리포트(권장 vs 섭취/소모), 주간 칼로리 추이, 식단 기록 연속 스트릭 통계
- **체중 추적**: 날짜별 체중 기록 및 변화 추이 제공

---

## 기술 스택

- **Core**: Java 17, Spring Boot 4.0.6
- **Database**: MySQL (Production/Local), H2 Database (JUnit 테스트 격리용 `testRuntimeOnly` 적용)
- **Security**: Spring Security 6.x, JWT (JSON Web Token)
- **Documentation**: springdoc-openapi-starter (Swagger UI 3.0.2)
- **Build Tool**: Gradle

---

## 실행 및 설정 방법

### 1. 로컬 설정 파일 작성
프로젝트 보안 상 `application.yml` 파일은 깃에 커밋되지 않습니다. 
`src/main/resources/application.yml.txt` 템플릿 파일을 복사하여 [application.yml](file:///Users/parkhanbi/Desktop/personalized-diet-manager-backend/src/main/resources/application.yml) 파일을 새로 생성하고, 본인의 MySQL 정보를 입력합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/diet_manager?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: 본인의_mysql_username
    password: 본인의_mysql_password
```
> [!NOTE]
> `createDatabaseIfNotExist=true` 옵션이 켜져 있어 MySQL 연결이 성공하면 `diet_manager` 데이터베이스는 기동 시 자동으로 생성됩니다.

### 2. 프로젝트 실행
```bash
# 프로젝트 루트 디렉토리에서
./gradlew bootRun
```
- 서버 주소: <http://localhost:8080>
- **최초 실행 시** `application.yml`의 `spring.sql.init.mode`를 잠시 `always`로 구동하면 `data.sql` 스크립트를 통해 최신 엔티티 규격 테이블이 자동 생성되고, 20가지 초기 식품 데이터가 자동으로 주입(Seed)됩니다. 기동 이후에는 안전을 위해 `never`로 변경하는 것을 권장합니다.

---

## 주요 주소

| 용도 | 주소 |
|------|------|
| Swagger UI (API 명세서) | <http://localhost:8080/swagger-ui.html> |
| OpenAPI 문서 (JSON) | <http://localhost:8080/v3/api-docs> |

---

## 주요 API

모든 기능성 API(인증 제외)는 로그인 시 발급받은 **`Authorization: Bearer <JWT_TOKEN>`** 헤더를 필수로 요구합니다.

### 1. 인증 API
| Method | Path | 설명 |
|--------|------|------|
| `POST` | `/api/auth/signup` | 이메일, 비밀번호, 닉네임으로 회원가입 |
| `POST` | `/api/auth/login` | 로그인 후 JWT Access Token 발급 |

### 2. 프로필 및 칼로리 API
| Method | Path | 설명 |
|--------|------|------|
| `POST` | `/api/profiles` | 현재 로그인한 사용자의 프로필 최초 생성 |
| `GET` | `/api/profiles/me` | 로그인한 사용자의 프로필 조회 |
| `PUT` | `/api/profiles/me` | 로그인한 사용자의 프로필 수정 |
| `POST` | `/api/calories/recommendation` | 성별/나이/키/체중/활동량 기준 하루 권장 칼로리·매크로 모의 계산 |

### 3. 음식 API
| Method | Path | 설명 |
|--------|------|------|
| `GET` | `/api/foods` | 음식 목록 조회 (`?keyword=` 검색, `?goalType=` 지정 시 신호등 분류 포함) |
| `GET` | `/api/foods/{foodId}` | 음식 단건 상세 조회 |

### 4. 식단, 운동, 체중 기록 API
| Method | Path | 설명 |
|--------|------|------|
| `POST` | `/api/meal-logs` | 식단 섭취 기록 등록 |
| `GET` | `/api/meal-logs` | 특정 날짜(`?date=`)의 식단 목록 및 하루 섭취 칼로리 조회 |
| `PUT` | `/api/meal-logs/{mealLogId}` | 식단 기록 수정 (소유권 검증) |
| `DELETE` | `/api/meal-logs/{mealLogId}` | 식단 기록 삭제 |
| `POST` | `/api/exercise-logs` | 운동 시간·강도별 소모 칼로리 계산 및 기록 등록 |
| `GET` | `/api/exercise-logs` | 특정 날짜(`?profileId=&date=`)의 운동 기록 리스트 조회 |
| `DELETE` | `/api/exercise-logs/{exerciseLogId}` | 운동 기록 삭제 |
| `POST` | `/api/weight-logs` | 체중 기록 등록 및 수정 (하루 한 건 제한) |
| `GET` | `/api/weight-logs` | 특정 사용자의 전체 체중 기록 조회 (`?profileId=`) |

### 5. 통계 및 리포트 API
| Method | Path | 설명 |
|--------|------|------|
| `GET` | `/api/reports/daily` | 특정 날짜(`?date=`)의 권장 vs 섭취/소모 칼로리·탄단지 분석 리포트 |
| `GET` | `/api/reports/weekly` | 최근 7일 권장/섭취 칼로리 주간 추이 (`?endDate=`) |
| `GET` | `/api/reports/stats` | 연속 기록 스트릭 일수 및 최근 7일 목표 달성률 통계 조회 (`?profileId=`) |

---

## 디자인 패턴 — 전략(Strategy) 적용 사례

목표(`GoalType`)나 운동 종류(`ExerciseType`)에 따라 달라지는 비즈니스 규칙을 인터페이스로 추상화하여, Spring DI와 `EnumMap` 기반 구조로 분기문 없이 다형성을 구현했습니다.

### 1) 칼로리·매크로 계산 (`CalorieStrategy`)
- BMR(기초대사량)은 **Mifflin-St Jeor 공식**을 사용합니다.
- 목표에 따른 칼로리 조정 비율을 다르게 설정합니다:
  - **다이어트**: `max(TDEE * 0.8, BMR)` (기초대사량 이하 극단적 절식 방지 안전선)
  - **벌크업**: TDEE 대비 약 10~20% 잉여 배율 제공
  - **유지**: TDEE 그대로 적용

### 2) 음식 신호등 분류 (`FoodGradeStrategy`)
- 사용자의 목표에 따라 동일한 음식을 **GREEN(권장) / YELLOW(보통) / RED(주의)**로 다이나믹하게 분류합니다. (예: 다이어터에게 닭가슴살은 GREEN이지만 지방 비중이 높은 연어는 YELLOW로 격하됨)

### 3) 운동 소모 칼로리 계산 (`ExerciseCalorieStrategy`)
- 운동 강도(`Intensity`)와 운동 종류(`ExerciseType`)별 고유 MET 계수를 결합해 소모 칼로리를 산출합니다:
  - `달리기`, `걷기`, `자전거`, `수영`, `웨이트 트레이닝` 등 각 운동 종류에 매칭되는 물리 계수 공식을 전략 클래스별로 분리 관리합니다.

---

## 테스트

프로젝트는 로컬 DB 의존성 없이 JUnit 빌드가 가능하도록 메모리 기반 H2 DB로 테스트 환경이 자동 격리되어 있습니다.

```bash
# 전체 테스트 실행
./gradlew test
```

- **테스트 커버리지**: 컨트롤러 입력 유효성 검증(Validation), 목표/운동 유형별 전략 계산, 비즈니스 시나리오 통합 흐름(Daily/Weekly Report), 보안 토큰 인가 필터링 테스트 등 46개의 테스트 케이스를 모두 포함합니다.
- HTML 테스트 리포트 확인 경로: `build/reports/tests/test/index.html`
