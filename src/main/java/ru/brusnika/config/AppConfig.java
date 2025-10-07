package ru.brusnika.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableScheduling
public class AppConfig {

    @Value("${spring.datasource.url:jdbc:sqlite:vacation.db}")
    private String datasourceUrl;

    @Bean
    public String initializeDatabasePath() {
        // Извлекаем путь из JDBC URL (jdbc:sqlite:path/to/db.db)
        if (datasourceUrl.startsWith("jdbc:sqlite:")) {
            String dbPath = datasourceUrl.substring("jdbc:sqlite:".length());
            File dbFile = new File(dbPath);

            // Создаем родительскую директорию если нужно
            File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
                System.out.println("✅ Created database directory: " + parentDir.getAbsolutePath());
            }

            System.out.println("📁 Database path: " + dbFile.getAbsolutePath());
        }
        return "database_initialized";
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}