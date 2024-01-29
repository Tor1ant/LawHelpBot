package com.github.tor1ant.lawhelpbot.service.bot;

import com.github.tor1ant.lawhelpbot.config.BotConfig;
import com.github.tor1ant.lawhelpbot.service.bot.command.InfoCommand;
import com.github.tor1ant.lawhelpbot.service.bot.command.StartCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramBot extends TelegramLongPollingCommandBot {

    private final BotConfig botConfig;
    @Value("${chat.id}")
    private String lawChatId;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
        register(new StartCommand("start", "Старт бота"));
        register(new InfoCommand("info", "получение информации о боте"));
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isReply()) {
                replyToMessage(message);
                return;
            }
            sendMessageToLawGroup(lawChatId, update.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private void sendMessageToLawGroup(String chatId, Message message) {
        String defaultInfo = message.getChat().getId() + "\n";
        String userInfo =
                "Сообщение от: " + message.getFrom().getFirstName() + " " + message.getFrom().getLastName() + "\n";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(defaultInfo + userInfo + message.getText())
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Что-то наебнулось");
            throw new RuntimeException(e);
        }
    }

    private void replyToMessage(Message message) {
        String chatId = getUserChatId(message);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message.getText())
                .build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUserChatId(Message message) {
        return message.getReplyToMessage().getText().split("\n")[0];
    }
}
