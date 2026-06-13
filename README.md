# Personalized Diet Manager — Backend

사용자의 신체 정보와 목표를 입력받아 **하루 권장 칼로리를 계산**하고, **식단을 기록**해 권장량 대비 섭취량을 비교하는 REST API 서버입니다.

핵심 공학 포인트는 **전략(Strategy) 패턴**으로, 목표별(다이어트·벌크업·체중 유지) 칼로리 계산 로직을 분리했습니다.

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
- 최초 기동 시 `data.sql`로 음식 데이터 20개가 자동 시드됩니다.
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
| `POST` | `/api/calories/recommendation` | 목표별 하루 권장 칼로리 계산 |
| `GET` | `/api/foods` | 음식 목록 조회 (`?keyword=` 로 검색) |
| `GET` | `/api/foods/{foodId}` | 음식 단건 조회 |
| `POST` | `/api/meal-logs` | 식단 기록 등록 |
| `GET` | `/api/meal-logs?profileId=&date=` | 날짜별 식단 기록 + 총 섭취 칼로리 조회 |
| `PUT` | `/api/meal-logs/{mealLogId}` | 식단 기록 수정 |
| `DELETE` | `/api/meal-logs/{mealLogId}` | 식단 기록 삭제 |
| `GET` | `/api/reports/daily?profileId=&date=` | 권장 칼로리 vs 섭취 칼로리 일일 리포트 |

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
```

## 칼로리 계산 로직 (Strategy 패턴)

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

새로운 목표를 추가하려면 `CalorieStrategy` 구현체 클래스를 추가하기만 하면 됩니다. `CalorieService`가 Spring DI로 전략 목록을 주입받아 `EnumMap`으로 관리하므로 분기문 수정이 필요 없습니다(OCP 준수).

## 테스트

```bash
./gradlew test
```

테스트 구성:

- `CalorieServiceTest` — 목표별 계산 + 하한선/활동량 분기 검증
- `UserProfileServiceTest`, `MealLogServiceTest`, `FoodServiceTest` — 서비스 계층
- `DailyReportFlowTest` — 프로필 저장 → 식단 등록 → 리포트까지 통합 흐름

HTML 테스트 리포트: `build/reports/tests/test/index.html`
