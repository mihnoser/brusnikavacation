package ru.brusnika.service;

import ru.brusnika.model.User;
import ru.brusnika.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final UserRepository userRepository;
    private final TelegramBotService telegramBotService;
    private final DocumentService documentService;
    private final int NOTIFICATION_DAYS_BEFORE = 21;

    public NotificationService(UserRepository userRepository, TelegramBotService telegramBotService,
                               DocumentService documentService) {
        this.userRepository = userRepository;
        this.telegramBotService = telegramBotService;
        this.documentService = documentService;
    }

    @Scheduled(cron = "0 15 10 * * ?") // Ежедневно в 9:00
    public void checkUpcomingVacations() {
        logger.info("=== ПРОВЕРКА ПРЕДСТОЯЩИХ ОТПУСКОВ ===");

        List<User> users = userRepository.findUsersWithUpcomingVacations(NOTIFICATION_DAYS_BEFORE);
        logger.info("Найдено пользователей для уведомления: {}", users.size());

        for (User user : users) {
            sendVacationReminder(user);
        }
    }

    private void sendVacationReminder(User user) {
        logger.info("Отправка уведомления пользователю: {}", user.getFullName());

        // Проверяем все 5 отпусков
        checkAndSendForVacation(user, user.getVacation1(), user.getVacation1Days(),
                user.getVacation1Notified(), 1);
        checkAndSendForVacation(user, user.getVacation2(), user.getVacation2Days(),
                user.getVacation2Notified(), 2);
        checkAndSendForVacation(user, user.getVacation3(), user.getVacation3Days(),
                user.getVacation3Notified(), 3);
        checkAndSendForVacation(user, user.getVacation4(), user.getVacation4Days(),
                user.getVacation4Notified(), 4);
        checkAndSendForVacation(user, user.getVacation5(), user.getVacation5Days(),
                user.getVacation5Notified(), 5);
    }

    private void checkAndSendForVacation(User user, LocalDate vacationDate, Integer vacationDays,
                                         Boolean notified, int vacationNumber) {
        if (shouldSendNotification(vacationDate, vacationDays, notified)) {
            sendDocumentNotification(user, vacationDate, vacationDays, vacationNumber);
        }
    }

    private boolean shouldSendNotification(LocalDate vacationDate, Integer vacationDays, Boolean notified) {
        if (vacationDate == null || vacationDays == null) {
            return false;
        }
        if (notified != null && notified) {
            return false;
        }

        LocalDate targetDate = LocalDate.now().plusDays(NOTIFICATION_DAYS_BEFORE);
        return vacationDate.equals(targetDate);
    }

    private void sendDocumentNotification(User user, LocalDate vacationDate, Integer vacationDays,
                                          int vacationNumber) {
        try {
            File documentFile = documentService.generateVacationNotice(
                    user, vacationDate, vacationDays, vacationNumber, NOTIFICATION_DAYS_BEFORE);

            String textMessage = String.format(
                    "Уважаемый(ая) %s! До вашего отпуска остался %d день. " +
                            "Отправляем официальное уведомление. 📄",
                    user.getFirstNameAndPatronymic() != null ?
                            user.getFirstNameAndPatronymic() : user.getFullName(),
                    NOTIFICATION_DAYS_BEFORE
            );

            // Сначала отправляем текстовое сообщение
            telegramBotService.sendMessage(user.getTelegramId(), textMessage);

            // Затем отправляем HTML документ
            telegramBotService.sendDocument(user.getTelegramId(), documentFile);

            // Помечаем как отправленное
            userRepository.markNotificationSent(user.getTelegramId(), vacationNumber);

            logger.info("Уведомление отправлено пользователю {} (отпуск #{})",
                    user.getTelegramId(), vacationNumber);

            // Удаляем временный файл
            documentFile.delete();

        } catch (Exception e) {
            logger.error("Ошибка отправки уведомления пользователю {}: {}",
                    user.getTelegramId(), e.getMessage(), e);
        }
    }
}