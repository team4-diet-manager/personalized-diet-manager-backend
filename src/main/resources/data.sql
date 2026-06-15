-- 테이블 생성 DDL
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_profile (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    age INT NOT NULL,
    height DOUBLE NOT NULL,
    weight DOUBLE NOT NULL,
    activity_level VARCHAR(20) NOT NULL,
    goal_type VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS food (
    food_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    calories INT NOT NULL,
    serving_size VARCHAR(50) NOT NULL,
    protein_grams INT NOT NULL,
    carb_grams INT NOT NULL,
    fat_grams INT NOT NULL,
    sugar_grams INT NOT NULL,
    sodium_mg INT NOT NULL,
    saturated_fat_grams INT NOT NULL,
    fiber_grams INT NOT NULL,
    created_at DATETIME(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS meal_log (
    meal_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    meal_date DATE NOT NULL,
    meal_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    total_calories INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES food(food_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS exercise_log (
    exercise_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    exercise_date DATE NOT NULL,
    exercise_type VARCHAR(20) NOT NULL,
    duration_minutes INT NOT NULL,
    intensity VARCHAR(10) NOT NULL,
    burned_calories INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS weight_log (
    weight_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    profile_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    weight DOUBLE NOT NULL,
    created_at DATETIME(6) NOT NULL,
    UNIQUE KEY uk_profile_date (profile_id, log_date),
    FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id) ON DELETE CASCADE
);

-- 초기 데이터 삽입
INSERT INTO food (name, calories, serving_size, protein_grams, carb_grams, fat_grams, sugar_grams, sodium_mg, saturated_fat_grams, fiber_grams, created_at) VALUES
('닭가슴살', 165, '100g', 31, 0, 4, 0, 60, 1, 0, CURRENT_TIMESTAMP),
('고구마', 130, '1개', 2, 30, 0, 9, 40, 0, 4, CURRENT_TIMESTAMP),
('현미밥', 300, '1공기', 6, 65, 2, 0, 5, 0, 3, CURRENT_TIMESTAMP),
('계란', 70, '1개', 6, 1, 5, 0, 70, 2, 0, CURRENT_TIMESTAMP),
('바나나', 90, '1개', 1, 23, 0, 14, 1, 0, 3, CURRENT_TIMESTAMP),
('사과', 95, '1개', 0, 25, 0, 19, 2, 0, 4, CURRENT_TIMESTAMP),
('두부', 80, '100g', 8, 2, 5, 1, 10, 1, 1, CURRENT_TIMESTAMP),
('연어', 208, '100g', 20, 0, 13, 0, 50, 3, 0, CURRENT_TIMESTAMP),
('참치', 132, '100g', 28, 0, 1, 0, 40, 0, 0, CURRENT_TIMESTAMP),
('우유', 120, '200ml', 6, 10, 5, 10, 100, 3, 0, CURRENT_TIMESTAMP),
('그릭요거트', 100, '100g', 10, 4, 4, 4, 36, 3, 0, CURRENT_TIMESTAMP),
('아몬드', 160, '30g', 6, 6, 14, 1, 0, 1, 4, CURRENT_TIMESTAMP),
('샐러드', 120, '1접시', 3, 10, 7, 4, 150, 1, 3, CURRENT_TIMESTAMP),
('김밥', 350, '1줄', 8, 55, 10, 5, 600, 3, 3, CURRENT_TIMESTAMP),
('라면', 500, '1개', 10, 70, 18, 5, 1800, 8, 4, CURRENT_TIMESTAMP),
('피자', 285, '1조각', 12, 30, 12, 4, 600, 6, 2, CURRENT_TIMESTAMP),
('햄버거', 550, '1개', 25, 45, 28, 8, 1000, 10, 3, CURRENT_TIMESTAMP),
('파스타', 600, '1인분', 20, 80, 18, 8, 700, 6, 4, CURRENT_TIMESTAMP),
('닭갈비', 450, '1인분', 35, 25, 22, 10, 1200, 7, 3, CURRENT_TIMESTAMP),
('된장찌개', 200, '1그릇', 12, 12, 10, 4, 1300, 3, 4, CURRENT_TIMESTAMP);
