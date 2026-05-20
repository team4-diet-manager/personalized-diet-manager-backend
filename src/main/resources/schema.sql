-- schema.sql
-- Spring Boot 시작 시 자동으로 테이블 생성

CREATE TABLE users (
                       id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name       VARCHAR(50)  NOT NULL,
                       email      VARCHAR(100) NOT NULL UNIQUE,
                       password   VARCHAR(255) NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_body_info (
                                id             BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id        BIGINT NOT NULL UNIQUE,
                                age            INT,
                                gender         VARCHAR(10),
                                height         FLOAT,
                                weight         FLOAT,
                                activity_level VARCHAR(20),
                                updated_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE user_goal (
                           id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                           user_id         BIGINT NOT NULL UNIQUE,
                           goal_type       VARCHAR(20) NOT NULL,
                           target_calories INT,
                           target_protein  FLOAT,
                           target_carb     FLOAT,
                           target_fat      FLOAT,
                           FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE nutrient (
                          id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name          VARCHAR(20) NOT NULL,
                          description   VARCHAR(100),
                          kcal_per_gram FLOAT
);

CREATE TABLE food_example (
                              id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                              nutrient_id BIGINT NOT NULL,
                              name        VARCHAR(50) NOT NULL,
                              amount      VARCHAR(20),
                              calories    INT,
                              FOREIGN KEY (nutrient_id) REFERENCES nutrient(id)
);

CREATE TABLE meal_log (
                          id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id     BIGINT NOT NULL,
                          nutrient_id BIGINT NOT NULL,
                          meal_type   VARCHAR(20),
                          amount_gram FLOAT,
                          eaten_at    DATE,
                          FOREIGN KEY (user_id)     REFERENCES users(id),
                          FOREIGN KEY (nutrient_id) REFERENCES nutrient(id)
);