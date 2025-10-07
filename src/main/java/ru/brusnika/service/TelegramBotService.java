package ru.brusnika.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.brusnika.config.BotConfig;

import java.io.File;

@Component
public class TelegramBotService extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
    private final BotConfig botConfig;

    public TelegramBotService(BotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        logger.info("TelegramBotService инициализирован для бота: {}", botConfig.getUsername());
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            Long userId = update.getMessage().getFrom().getId();

            logger.debug("Получено сообщение от пользователя {} (ID: {}): {}",
                    update.getMessage().getFrom().getFirstName(), userId, messageText);

            switch (messageText) {
                case "/start":
                    sendMessage(chatId,
                            "👋 Привет! Я бот для уведомлений об отпусках.\n\n" +
                                    "📋 Доступные команды:\n" +
                                    "/myid - узнать свой числовой ID\n" +
                                    "/test - тестовое сообщение\n" +
                                    "Уведомления приходят автоматически за 21 день до отпуска.");
                    logger.info("Пользователь ID: {} запустил бота командой /start", userId);
                    break;

                case "/myid":
                    String userInfo = String.format(
                            "👤 Ваш ID для базы данных: %d\n\n" +
                                    "📞 Передайте этот номер администратору для подключения уведомлений.",
                            userId
                    );
                    sendMessage(chatId, userInfo);
                    logger.info("Пользователь ID: {} запросил свой ID", userId);
                    break;

                case "/test":
                    sendMessage(chatId, "✅ Бот работает! Тестовое сообщение доставлено.");
                    logger.info("Тестовое сообщение отправлено пользователю ID: {}", userId);
                    break;

                case "/Привет":
                case "/привет":
                case "/hello":
                case "/Hello":
                    String greeting = String.format(
                            "👋 Привет, %s!😊 \n\n" +
                                    "Сходи налей пока-что себе кофе, а я пробегусь посмотрю кого отправить в отпуск! \uD83C\uDFDD \n",
                            update.getMessage().getFrom().getFirstName()
                    );
                    sendMessage(chatId, greeting);
                    logger.info("Приветствие отправлено пользователю: {} (ID: {})",
                            update.getMessage().getFrom().getFirstName(), userId);
                    break;

                default:
                    logger.debug("Необработанная команда: {} от пользователя ID: {}", messageText, userId);
                    break;
            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
            logger.debug("Сообщение отправлено пользователю {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Ошибка отправки сообщения пользователю {}: {}", chatId, e.getMessage());
        }
    }

    public void sendDocument(Long chatId, File document) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId.toString());
        sendDocument.setDocument(new InputFile(document));
        sendDocument.setCaption("Официальное уведомление об отпуске!\n" +
                "Вам необходимо:\n" +
                "1. Распечатать и подписать уведомление!\n" +
                "2. Отправить скан Солодовой Е.С.(@solodovakat), оригинал передать в офис!\n" +
                "3. Обязательно отправить заявление на отпуск в КЭДО");

        try {
            execute(sendDocument);
            logger.info("Документ отправлен пользователю {}", chatId);
        } catch (TelegramApiException e) {
            logger.error("Ошибка отправки документа пользователю {}: {}", chatId, e.getMessage());
        }
    }
}