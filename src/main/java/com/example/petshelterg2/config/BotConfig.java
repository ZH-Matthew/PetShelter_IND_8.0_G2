package com.example.petshelterg2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data //из библиотеки Lombok, реализует геттеры /сеттеры, переопределяет tostring
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

    @Value("${bot.owner}")  //чат-id админа (волонтёра)
    String ownerId;

}