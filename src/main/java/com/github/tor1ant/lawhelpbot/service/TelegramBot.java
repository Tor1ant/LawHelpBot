package com.github.tor1ant.lawhelpbot.service;

import com.github.tor1ant.lawhelpbot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();
            switch (messageText) {
                case "/start" -> startCommandReceived(chatId, firstName);
                default -> sendMessage(chatId, "Ну и иди нахуй");
            }
        }
    }

    private void startCommandReceived(Long chatId, String firstName) {
        String answer = "Привет! " + firstName + ", Как твоё разъебанное очко?";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(message)
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Что-то наебнулось");
            throw new RuntimeException(e);
        }
    }
}
