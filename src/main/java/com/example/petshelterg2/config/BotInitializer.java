package com.example.petshelterg2.config;

import com.example.petshelterg2.controller.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Класс для инициализации бота <p>
 * <b>КОД В ЭТОМ КЛАССЕ НЕ ТРОГАТЬ!</b>
 */
@Slf4j  //из библиотеки lombok реализует логирование через переменную log.
@Component
public class BotInitializer {

    @Autowired
    TelegramBot bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error occurred " + e.getMessage());               //логирование с помощью библиотеки Slf4j
        }
    }
}
