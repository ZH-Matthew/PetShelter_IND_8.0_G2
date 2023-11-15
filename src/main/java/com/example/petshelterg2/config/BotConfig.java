package com.example.petshelterg2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Класс для конфигурации бота <p>
 * Содержит 3 поля со значениями: <p>
 * Название бота, токен бота и chatID админа
 */
@Configuration
@Data //из библиотеки Lombok, реализует геттеры /сеттеры, переопределяет tostring
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;
    /**
     * Переменная хранит чат-id админа (волонтёра)
     * Подтягивая значение из файла <u>application.properties</u>
     */
    @Value("${bot.owner}")
    String ownerId;

}