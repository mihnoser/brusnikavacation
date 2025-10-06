package ru.brusnika.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        String url = "jdbc:sqlite:vacation.db";
        return new org.springframework.jdbc.datasource.SingleConnectionDataSource() {
            {
                setDriverClassName("org.sqlite.JDBC");
                setUrl(url);
                setSuppressClose(true);
            }
        };
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}