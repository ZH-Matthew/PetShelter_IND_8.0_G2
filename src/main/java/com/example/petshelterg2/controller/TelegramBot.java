package com.example.petshelterg2.controller;

import com.example.petshelterg2.config.BotConfig;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
import static com.example.petshelterg2.constants.Constants.*;


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
                case "Приют кошек":
                    cat(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "Приют собак":
                    dog(chatId, update.getMessage().getChat().getFirstName());
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
        message.setReplyMarkup(startKeyboard());
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

    private void dog(long chatId,String name) {//метод для перехода в собачий приют, с клавиатурой
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(DOG_SHELTER_SELECT_TEXT);
        message.setReplyMarkup(dogShelterKeyboard());//вызов метода для получения клавиатуры
        executeMessage(message);
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }
    private void cat(long chatId,String name) {//метод для перехода в кошачий приют, с клавиатурой
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(CAT_SHELTER_SELECT_TEXT);
        message.setReplyMarkup(catShelterKeyboard());//вызов метода для получения клавиатуры
        executeMessage(message);
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }

    private ReplyKeyboardMarkup startKeyboard() {//стартовая клавиатура
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();//создание клавиатуры
        List<KeyboardRow> keyboardRows = new ArrayList<>();//создание рядов в клавиатуре

        KeyboardRow row = new KeyboardRow();//первый ряд клавиатуры
        row.add("Приют кошек");//добавление кнопок (слева будут первые созданные)
        row.add("Приют собак");
        keyboardRows.add(row);//добавляем в клавиатуру ряд

        row = new KeyboardRow();
        row.add("/start");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
    private ReplyKeyboardMarkup dogShelterKeyboard() {//клавиатура для собачено приюта
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(SHELTER_FIRST_STEP_BUTTON_DOG);
        row.add(SHELTER_SECOND_STEP_BUTTON_DOG);
        row.add(SHELTER_THIRD_STEP_BUTTON_DOG);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add("/start");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
    private ReplyKeyboardMarkup catShelterKeyboard() {//клавиатура для кошачьего приюта
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(SHELTER_FIRST_STEP_BUTTON_CAT);
        row.add(SHELTER_SECOND_STEP_BUTTON_CAT);
        row.add(SHELTER_THIRD_STEP_BUTTON_CAT);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add("/start");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }
}
