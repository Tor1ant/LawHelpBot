package com.github.tor1ant.lawhelpbot.service.bot.command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class InfoCommand extends BotCommand {

    public InfoCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String info = """
                Данный бот предоставлен волонтёрской организацией Сбербанка. Здесь вы можете получить\
                 юридическую справочную помощь по следующим областям права:\s
                1) Гражданское право
                2) Семейное право
                3) Налоговое право
                                
                Введите интересующий вас вопрос и ожидайте ответ:""";

        SendMessage sendMessage = new SendMessage(String.valueOf(chat.getId()), info);
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
