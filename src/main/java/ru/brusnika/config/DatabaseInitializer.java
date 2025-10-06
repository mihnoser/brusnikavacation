package ru.brusnika.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Проверка и создание базы данных...");
        try {
            // Пытаемся выполнить запрос к таблице users, если ее нет - будет исключение
            jdbcTemplate.execute("SELECT count(*) FROM users");
            logger.info("База данных и таблица users уже существуют.");
        } catch (Exception e) {
            // Если таблицы нет, создаем ее
            logger.info("Таблица users не найдена. Создание таблицы...");
            String sql = """
                CREATE TABLE users (
                    telegram_id INTEGER PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    first_name_patronymic TEXT,
                    vacation1_date TEXT,
                    vacation1_days INTEGER,
                    vacation1_notified BOOLEAN DEFAULT 0,
                    vacation2_date TEXT,
                    vacation2_days INTEGER,
                    vacation2_notified BOOLEAN DEFAULT 0,
                    vacation3_date TEXT,
                    vacation3_days INTEGER,
                    vacation3_notified BOOLEAN DEFAULT 0,
                    vacation4_date TEXT,
                    vacation4_days INTEGER,
                    vacation4_notified BOOLEAN DEFAULT 0,
                    vacation5_date TEXT,
                    vacation5_days INTEGER,
                    vacation5_notified BOOLEAN DEFAULT 0
                )
                """;
            jdbcTemplate.execute(sql);
            logger.info("Таблица users успешно создана.");
        }
    }
}