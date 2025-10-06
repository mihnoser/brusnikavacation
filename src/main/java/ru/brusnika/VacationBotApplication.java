package ru.brusnika;

import ru.brusnika.service.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@SuppressWarnings("unused") // Добавляем чтобы убрать предупреждение
public class VacationBotApplication {

    private static final Logger logger = LoggerFactory.getLogger(VacationBotApplication.class);
    private final TelegramBotService telegramBotService;

    public VacationBotApplication(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    public static void main(String[] args) {
        SpringApplication.run(VacationBotApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void registerBotOnStartup() { // Переименовал метод для ясности
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBotService);
            logger.info("✅ Бот успешно зарегистрирован и запущен!");
            logger.info("✅ База данных инициализирована");
            logger.info("✅ Планировщик уведомлений активирован");

            // Тестовый вывод в консоль
            System.out.println("=========================================");
            System.out.println("🤖 БОТ УСПЕШНО ЗАПУЩЕН!");
            System.out.println("📅 Проверка отпусков активирована");
            System.out.println("⏰ Уведомления будут в 9:00 ежедневно");
            System.out.println("=========================================");

        } catch (Exception e) {
            logger.error("❌ Ошибка регистрации бота: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}