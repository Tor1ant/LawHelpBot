package com.github.tor1ant.lawhelpbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class BotConfig {
    @Value("${bot.name}")
    private final String botName;
    @Value("${bot.key}")
    private final String token;

    public BotConfig(@Value("${bot.name}") String botName, @Value("${bot.key}") String token) {
        this.botName = botName;
        this.token = token;
    }
}
