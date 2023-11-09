package com.example.petshelterg2.service;

import com.example.petshelterg2.config.BotConfig;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.petshelterg2.service.Constants.*;


@Slf4j //из библиотеки lombok реализует логирование через переменную log.
@Component //аннотация позволяет автоматически создать экземпляр
public class TelegramBot extends TelegramLongPollingBot {  //есть еще класс WebHookBot (разница в том что WebHook уведомляет нас каждый раз при написании сообщения пользователе, LongPolling сам проверяет не написали ли ему (он более простой)

    @Autowired
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    //реализация метода LongPooling
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    //реализация метода LongPooling
    @Override
    public String getBotToken() {
        return config.getToken();
    }

    //реализация основного метода общения с пользователем (главный метод приложения)
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) { //проверяем что сообщение пришло и там есть текст
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;
                //тут будут ещё кейсы на другие команды (поэтому switch, а не if)

                default:
                    prepareAndSendMessage(chatId, "Я пока не знаю как на это ответить!");
            }
        }
    }


    //метод для приветственного сообщения
    private void startCommand(long chatId, String name) {
        // добавление смайликов в строку (на сайте эмоджипедиа, либо можно зайти в телегу и навести на смайлик, он выдаст код)
        String answer = EmojiParser.parseToUnicode("Привет, " + name + ", твой будущий питомец скучает по тебе!" + " :blush:");
        prepareAndSendMessage(chatId, answer);                    // вызываем метод подготовки сообщения
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }


    //метод подготовки сообщения и его отправки
    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(textToSend);
        executeMessage(message); //вызываем метод отправки сообщения
    }

    //метод только для отправки готового сообщения
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }
}
