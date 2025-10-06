package ru.brusnika.repository;

import ru.brusnika.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    // Выносим форматтер на уровень класса, чтобы использовать во всех методах
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setTelegramId(rs.getLong("telegram_id"));
            user.setFullName(rs.getString("full_name"));
            user.setFirstNameAndPatronymic(rs.getString("first_name_patronymic"));

            user.setVacation1(getLocalDate(rs, "vacation1_date"));
            user.setVacation1Days(getInteger(rs, "vacation1_days"));
            user.setVacation1Notified(getBoolean(rs, "vacation1_notified"));

            user.setVacation2(getLocalDate(rs, "vacation2_date"));
            user.setVacation2Days(getInteger(rs, "vacation2_days"));
            user.setVacation2Notified(getBoolean(rs, "vacation2_notified"));

            user.setVacation3(getLocalDate(rs, "vacation3_date"));
            user.setVacation3Days(getInteger(rs, "vacation3_days"));
            user.setVacation3Notified(getBoolean(rs, "vacation3_notified"));

            user.setVacation4(getLocalDate(rs, "vacation4_date"));
            user.setVacation4Days(getInteger(rs, "vacation4_days"));
            user.setVacation4Notified(getBoolean(rs, "vacation4_notified"));

            user.setVacation5(getLocalDate(rs, "vacation5_date"));
            user.setVacation5Days(getInteger(rs, "vacation5_days"));
            user.setVacation5Notified(getBoolean(rs, "vacation5_notified"));

            return user;
        }

        private LocalDate getLocalDate(ResultSet rs, String column) throws SQLException {
            String dateStr = rs.getString(column);
            if (dateStr != null) {
                try {
                    // Парсим дату в формате dd.MM.yyyy
                    return LocalDate.parse(dateStr, dateFormatter);
                } catch (Exception e) {
                    System.err.println("Ошибка парсинга даты '" + dateStr + "' для колонки " + column + ": " + e.getMessage());
                    return null;
                }
            }
            return null;
        }

        private Integer getInteger(ResultSet rs, String column) throws SQLException {
            int value = rs.getInt(column);
            return rs.wasNull() ? null : value;
        }

        private Boolean getBoolean(ResultSet rs, String column) throws SQLException {
            return rs.getBoolean(column);
        }
    };

    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public List<User> findUsersWithUpcomingVacations(int daysBefore) {
        LocalDate targetDate = LocalDate.now().plusDays(daysBefore);
        String targetDateStr = targetDate.format(dateFormatter);

        String sql = """
        SELECT * FROM users 
        WHERE (vacation1_date = ? AND vacation1_notified = 0) 
           OR (vacation2_date = ? AND vacation2_notified = 0)
           OR (vacation3_date = ? AND vacation3_notified = 0) 
           OR (vacation4_date = ? AND vacation4_notified = 0) 
           OR (vacation5_date = ? AND vacation5_notified = 0)
        """;
        return jdbcTemplate.query(sql, userRowMapper,
                targetDateStr, targetDateStr, targetDateStr, targetDateStr, targetDateStr);
    }

    public List<User> findUsersWithMissedNotifications(int daysBefore) {
        LocalDate checkDate = LocalDate.now().minusDays(daysBefore);
        String checkDateStr = checkDate.format(dateFormatter);

        String sql = """
            SELECT * FROM users 
            WHERE ((vacation1_date = ? AND vacation1_notified = 0) 
                OR (vacation2_date = ? AND vacation2_notified = 0)
                OR (vacation3_date = ? AND vacation3_notified = 0) 
                OR (vacation4_date = ? AND vacation4_notified = 0) 
                OR (vacation5_date = ? AND vacation5_notified = 0))
            """;
        return jdbcTemplate.query(sql, userRowMapper,
                checkDateStr, checkDateStr, checkDateStr, checkDateStr, checkDateStr);
    }

    // НОВЫЙ МЕТОД - поиск всех пропущенных уведомлений
    public List<User> findUsersWithAllMissedNotifications() {
        LocalDate today = LocalDate.now();
        String todayStr = today.format(dateFormatter);

        String sql = """
            SELECT * FROM users 
            WHERE (
                (vacation1_date IS NOT NULL AND vacation1_date < ? AND vacation1_notified = 0) 
                OR (vacation2_date IS NOT NULL AND vacation2_date < ? AND vacation2_notified = 0)
                OR (vacation3_date IS NOT NULL AND vacation3_date < ? AND vacation3_notified = 0) 
                OR (vacation4_date IS NOT NULL AND vacation4_date < ? AND vacation4_notified = 0) 
                OR (vacation5_date IS NOT NULL AND vacation5_date < ? AND vacation5_notified = 0)
            )
            """;
        return jdbcTemplate.query(sql, userRowMapper, todayStr, todayStr, todayStr, todayStr, todayStr);
    }

    public void markNotificationSent(Long telegramId, int vacationNumber) {
        String column = "vacation" + vacationNumber + "_notified";
        String sql = "UPDATE users SET " + column + " = 1 WHERE telegram_id = ?";
        jdbcTemplate.update(sql, telegramId);
    }
}