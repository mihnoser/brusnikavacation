package ru.brusnika.model;

import java.time.LocalDate;

public class User {
    private Long telegramId;
    private String fullName;
    private String firstNameAndPatronymic;

    private LocalDate vacation1;
    private Integer vacation1Days;
    private Boolean vacation1Notified;

    private LocalDate vacation2;
    private Integer vacation2Days;
    private Boolean vacation2Notified;

    private LocalDate vacation3;
    private Integer vacation3Days;
    private Boolean vacation3Notified;

    private LocalDate vacation4;
    private Integer vacation4Days;
    private Boolean vacation4Notified;

    private LocalDate vacation5;
    private Integer vacation5Days;
    private Boolean vacation5Notified;

    // Конструкторы
    public User() {}

    // Геттеры и сеттеры
    public Long getTelegramId() { return telegramId; }
    public void setTelegramId(Long telegramId) { this.telegramId = telegramId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getFirstNameAndPatronymic() { return firstNameAndPatronymic; }
    public void setFirstNameAndPatronymic(String firstNameAndPatronymic) { this.firstNameAndPatronymic = firstNameAndPatronymic; }

    public LocalDate getVacation1() { return vacation1; }
    public void setVacation1(LocalDate vacation1) { this.vacation1 = vacation1; }
    public Integer getVacation1Days() { return vacation1Days; }
    public void setVacation1Days(Integer vacation1Days) { this.vacation1Days = vacation1Days; }
    public Boolean getVacation1Notified() { return vacation1Notified; }
    public void setVacation1Notified(Boolean vacation1Notified) { this.vacation1Notified = vacation1Notified; }

    public LocalDate getVacation2() { return vacation2; }
    public void setVacation2(LocalDate vacation2) { this.vacation2 = vacation2; }
    public Integer getVacation2Days() { return vacation2Days; }
    public void setVacation2Days(Integer vacation2Days) { this.vacation2Days = vacation2Days; }
    public Boolean getVacation2Notified() { return vacation2Notified; }
    public void setVacation2Notified(Boolean vacation2Notified) { this.vacation2Notified = vacation2Notified; }

    public LocalDate getVacation3() { return vacation3; }
    public void setVacation3(LocalDate vacation3) { this.vacation3 = vacation3; }
    public Integer getVacation3Days() { return vacation3Days; }
    public void setVacation3Days(Integer vacation3Days) { this.vacation3Days = vacation3Days; }
    public Boolean getVacation3Notified() { return vacation3Notified; }
    public void setVacation3Notified(Boolean vacation3Notified) { this.vacation3Notified = vacation3Notified; }

    public LocalDate getVacation4() { return vacation4; }
    public void setVacation4(LocalDate vacation4) { this.vacation4 = vacation4; }
    public Integer getVacation4Days() { return vacation4Days; }
    public void setVacation4Days(Integer vacation4Days) { this.vacation4Days = vacation4Days; }
    public Boolean getVacation4Notified() { return vacation4Notified; }
    public void setVacation4Notified(Boolean vacation4Notified) { this.vacation4Notified = vacation4Notified; }

    public LocalDate getVacation5() { return vacation5; }
    public void setVacation5(LocalDate vacation5) { this.vacation5 = vacation5; }
    public Integer getVacation5Days() { return vacation5Days; }
    public void setVacation5Days(Integer vacation5Days) { this.vacation5Days = vacation5Days; }
    public Boolean getVacation5Notified() { return vacation5Notified; }
    public void setVacation5Notified(Boolean vacation5Notified) { this.vacation5Notified = vacation5Notified; }
}