# Personalized Diet Manager — Backend

사용자의 신체 정보와 목표를 입력받아 **하루 권장 칼로리·탄단지(매크로)를 계산**하고, **식단과 체중을 기록**해 권장량 대비 섭취량과 변화 추이를 비교하는 REST API 서버입니다.

핵심 공학 포인트는 **전략(Strategy) 패턴을 두 곳에 독립 적용**한 것입니다.

- **칼로리·매크로 계산** — 목표별(다이어트·벌크업·체중 유지) 계산 로직 분리 (`CalorieStrategy`)
- **음식 신호등 분류** — 같은 목표 기준으로 음식을 녹/노/적으로 분류 (`FoodGradeStrategy`)

두 전략 모두 Spring DI + `EnumMap`으로 관리해, 새 목표를 추가해도 분기문 수정 없이 구현체만 추가하면 됩니다(OCP 준수).

## 주요 기능

- 프로필 등록/조회/수정, 목표별 권장 칼로리·탄단지 계산
- 식단 기록 등록/조회/수정/삭제, 날짜별 총 섭취 칼로리·매크로 합산
- 목표 기준 음식 신호등(GREEN/YELLOW/RED) 분류
- 일일 리포트(권장 vs 섭취) 및 최근 7일 칼로리 추이
- 체중 기록(날짜별 1건)과 변화 추적

## 기술 스택

- Java 17, Spring Boot 4.0.6
- Spring Data JPA, H2 (in-memory)
- springdoc-openapi (Swagger UI)
- Gradle

## 실행 방법

```bash
# 프로젝트 루트(personalized-diet-manager-backend)에서
./gradlew bootRun
```

- 서버 주소: <http://localhost:8080>
- 최초 기동 시 `data.sql`로 음식 데이터 20개(탄단지 포함)가 자동 시드됩니다.
- H2는 in-memory라 서버를 내리면 데이터가 초기화됩니다.

## 주요 주소

| 용도 | 주소 |
|------|------|
| Swagger UI | <http://localhost:8080/swagger-ui.html> |
| OpenAPI 문서(JSON) | <http://localhost:8080/v3/api-docs> |
| H2 Console | <http://localhost:8080/h2-console> |

H2 Console 접속 정보:

- JDBC URL: `jdbc:h2:mem:diet-manager`
- User: `sa` / Password: (비움)

## 주요 API

| Method | Path | 설명 |
|--------|------|------|
| `POST` | `/api/profiles` | 사용자 프로필 등록 |
| `GET` | `/api/profiles/{profileId}` | 프로필 조회 |
| `PUT` | `/api/profiles/{profileId}` | 프로필 수정 |
| `POST` | `/api/calories/recommendation` | 목표별 하루 권장 칼로리·탄단지 계산 |
| `GET` | `/api/foods` | 음식 목록 조회 (`?keyword=` 검색, `?goalType=` 시 신호등 등급 포함) |
| `GET` | `/api/foods/{foodId}` | 음식 단건 조회 |
| `POST` | `/api/meal-logs` | 식단 기록 등록 |
| `GET` | `/api/meal-logs?profileId=&date=` | 날짜별 식단 기록 + 총 섭취 칼로리 조회 |
| `PUT` | `/api/meal-logs/{mealLogId}` | 식단 기록 수정 |
| `DELETE` | `/api/meal-logs/{mealLogId}` | 식단 기록 삭제 |
| `GET` | `/api/reports/daily?profileId=&date=` | 권장 vs 섭취 칼로리·탄단지 일일 리포트 |
| `GET` | `/api/reports/weekly?profileId=&endDate=` | 최근 7일 권장/섭취 칼로리 추이 (`endDate` 기본값: 오늘) |
| `POST` | `/api/weight-logs` | 체중 기록 (같은 날짜는 갱신) |
| `GET` | `/api/weight-logs?profileId=` | 체중 기록 전체 조회 (날짜순) |

### 요청 예시

```bash
# 1) 프로필 등록
curl -X POST http://localhost:8080/api/profiles \
  -H 'Content-Type: application/json' \
  -d '{"gender":"FEMALE","age":23,"height":162,"weight":55,"activityLevel":"NORMAL","goalType":"WEIGHT_LOSS"}'

# 2) 목표별 권장 칼로리 계산
curl -X POST http://localhost:8080/api/calories/recommendation \
  -H 'Content-Type: application/json' \
  -d '{"gender":"FEMALE","age":23,"height":162,"weight":55,"activityLevel":"NORMAL","goalType":"WEIGHT_LOSS"}'

# 3) 일일 리포트
curl "http://localhost:8080/api/reports/daily?profileId=1&date=2026-06-14"

# 4) 최근 7일 칼로리 추이
curl "http://localhost:8080/api/reports/weekly?profileId=1&endDate=2026-06-14"

# 5) 목표 기준 음식 신호등 분류 조회
curl "http://localhost:8080/api/foods?goalType=WEIGHT_LOSS"

# 6) 체중 기록
curl -X POST http://localhost:8080/api/weight-logs \
  -H 'Content-Type: application/json' \
  -d '{"profileId":1,"logDate":"2026-06-14","weight":54.5}'
```

## 디자인 패턴 — 전략(Strategy)을 두 곳에 적용

목표(`GoalType`)에 따라 달라지는 동작을 인터페이스로 추상화하고, 목표별 구현체를 Spring DI로 주입받아 `EnumMap`으로 관리합니다. 분기문(`if`/`switch`) 없이 구현체 추가만으로 확장되므로 **OCP(개방-폐쇄 원칙)**를 만족합니다.

| 적용 위치 | 인터페이스 | 관리 서비스 | 구현체 |
|-----------|------------|-------------|--------|
| 칼로리·매크로 계산 | `CalorieStrategy` | `CalorieService` | `WeightLossStrategy` / `MuscleGainStrategy` / `MaintainStrategy` |
| 음식 신호등 분류 | `FoodGradeStrategy` | `FoodGradeService` | `WeightLossGradeStrategy` / `MuscleGainGradeStrategy` / `MaintainGradeStrategy` |

### 1) 칼로리·매크로 계산 (`CalorieStrategy`)

기초대사량(BMR)은 **Mifflin-St Jeor 공식**으로 계산하고, 활동량 계수를 곱해 TDEE를 구한 뒤 목표별 전략을 적용합니다.

```
BMR(남) = 10×체중 + 6.25×키 − 5×나이 + 5
BMR(여) = 10×체중 + 6.25×키 − 5×나이 − 161
TDEE    = BMR × 활동량 계수(LOW 1.2 / NORMAL 1.55 / HIGH 1.725)
```

| 목표 | 전략 | 계산 |
|------|------|------|
| 다이어트 | `WeightLossStrategy` | `max(TDEE × 0.8, BMR)` — 기초대사량 미만은 권장하지 않음(안전 하한선) |
| 벌크업 | `MuscleGainStrategy` | `TDEE × 잉여배율` — 활동량별 차등(LOW 1.10 / NORMAL 1.15 / HIGH 1.20) |
| 체중 유지 | `MaintainStrategy` | `TDEE` |

권장 칼로리에 목표별 **탄단지 비율**을 적용해 권장 매크로(g)도 함께 계산합니다(단백질·탄수 4kcal/g, 지방 9kcal/g).

| 목표 | 단백질 / 탄수 / 지방 |
|------|----------------------|
| 다이어트 | 40% / 35% / 25% (근손실 방지 고단백) |
| 벌크업 | 30% / 50% / 20% (운동 회복 고탄수) |
| 체중 유지 | 30% / 40% / 30% (균형) |

### 2) 음식 신호등 분류 (`FoodGradeStrategy`)

같은 목표 기준으로 음식의 적합도를 **GREEN(권장) / YELLOW(적당히) / RED(주의)**로 분류합니다. `GET /api/foods?goalType=` 호출 시 각 음식에 `grade`가 포함됩니다.

| 목표 | 분류 기준(요약) |
|------|-----------------|
| 다이어트 | 저칼로리·고단백 → GREEN, 고칼로리·고지방 → RED |
| 벌크업 | 고단백·충분한 에너지 → GREEN, 저단백·고지방 → RED |
| 체중 유지 | 지방 비중 낮고 칼로리 적당 → GREEN, 고칼로리·고지방 → RED |

칼로리 계산과 **동일한 패턴·동일한 OCP 구조**를 음식 분류라는 다른 책임에 재사용한 사례입니다. 새 목표가 생기면 두 전략의 구현체만 추가하면 됩니다.

## 테스트

```bash
./gradlew test
```

테스트 구성:

- `CalorieServiceTest` — 목표별 계산 + 하한선/활동량 분기 검증
- `UserProfileServiceTest` — 프로필 등록/수정(성별 포함 전체 필드) 검증
- `FoodServiceTest` — 음식 조회·검색 + 목표 기준 신호등 등급 부여 검증
- `MealLogServiceTest`, `WeightLogServiceTest` — 식단/체중 기록 서비스 (체중은 날짜별 upsert)
- `DailyReportResponseTest` — 섭취 상태 OVER/UNDER/MATCH 경계값
- `DailyReportFlowTest`, `WeeklyReportFlowTest` — 식단 등록 → 일일/주간 리포트 통합 흐름
- `ValidationApiTest` — 필수값/0 이하 값 400, 미존재 리소스 404 (웹 계층)

HTML 테스트 리포트: `build/reports/tests/test/index.html`
