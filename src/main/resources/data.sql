-- data.sql
-- 앱 시작 시 샘플 데이터 자동 삽입

-- 영양소 3개
INSERT INTO nutrient (name, description, kcal_per_gram) VALUES
                                                            ('단백질',   '근육 합성에 필요한 영양소',  4.0),
                                                            ('탄수화물', '주요 에너지원',              4.0),
                                                            ('지방',     '호르몬 합성에 필요한 영양소', 9.0);

-- 예시 음식 (시연용 샘플)
INSERT INTO food_example (nutrient_id, name, amount, calories) VALUES
                                                                   (1, '닭가슴살',   '100g',   165),
                                                                   (1, '계란',       '1개',     78),
                                                                   (1, '두부',       '100g',    76),
                                                                   (1, '참치캔',     '1캔',    100),
                                                                   (2, '흰쌀밥',    '한공기',  300),
                                                                   (2, '고구마',     '100g',    86),
                                                                   (2, '오트밀',     '100g',   389),
                                                                   (3, '아보카도',   '1/2개',  120),
                                                                   (3, '견과류',     '한줌',   180),
                                                                   (3, '올리브오일', '1큰술',  119);

-- 테스트용 사용자
INSERT INTO users (name, email, password) VALUES
    ('홍길동', 'test@test.com', '1234');

INSERT INTO user_body_info
(user_id, age, gender, height, weight, activity_level)
VALUES (1, 25, 'MALE', 175.0, 70.0, 'MODERATE');

INSERT INTO user_goal
(user_id, goal_type, target_calories, target_protein, target_carb, target_fat)
VALUES (1, 'DIET', 1500, 150.0, 130.0, 40.0);