package com.example.petshelterg2.service;

import com.example.petshelterg2.controller.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.example.petshelterg2.constants.Constants.*;

@Slf4j
@Component
public class SendMessageService {
    TelegramBot bot;

    public SendMessageService(@Lazy TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Точечный метод отправки сообщения <p>
     * Главная задача метода: принять собранный message и отправить его клиенту
     *
     * @param message (заранее собранный message с chatID пользователя и текстом сообщения)
     *                {@link TelegramApiException} обрабатывается через try/catch внутри метода
     */
    public void executeMessage(SendMessage message) {
        try {
            Message message1 = bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    /**
     * Метод подготовки и отправки сообщения пользователю <p>
     * <b><u>Без клавиатуры!</u></b> <p>
     * Собирает сообщение и дергает метод отправки: {@link SendMessageService#executeMessage(SendMessage)}
     *
     * @param chatId     (ID чата пользователя)
     * @param textToSend (текст для отправки пользователю)
     */
    public void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(textToSend);
        executeMessage(message); //вызываем метод отправки сообщения
    }

    /**
     * Метод подготовки и отправки сообщения пользователю
     * <b><u>Вместе с клавиатурой!</u></b> <p>
     * Собирает сообщение вместе с клавиатурой и дергает метод отправки: {@link SendMessageService#executeMessage(SendMessage)}
     *
     * @param chatId         (ID чата пользователя)
     * @param textToSend     (текст для отправки пользователю)
     * @param keyboardMarkup (клавиатура)
     */
    public void prepareAndSendMessageAndKeyboard(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        executeMessage(message); //вызываем метод отправки сообщения
    }

    /**
     * Метод для вызова волонтера <p>
     * Суть метода: отправить волонтёру в личку ссылку на пользователя чтобы волонтёр законнектил чаты и начал общение)<p>
     * Отправляет два сообщения: <p>
     * Одно волонтёру со ссылкой на чат клиента <p>
     * Второе клиенту, с уведомлением о том, что ему скоро напишут
     *
     * @param chatId   (chatID пользователя)
     * @param userName (никнейм пользователя)
     */
    public void callAVolunteer(long chatId, String userName,String adminId) {
        SendMessage messageVolunteer = new SendMessage();
        SendMessage messageUser = new SendMessage();                    //создаёт два сообщения, одно волонтеру, другое пользователю

        messageVolunteer.setChatId(adminId);
        messageVolunteer.setText(VOLUNTEER_MESSAGE + userName);         //формируем сообщение для волонтёра
        messageUser.setChatId(String.valueOf(chatId));
        messageUser.setText(VOLUNTEER_WILL_WRITE_TO_YOU);               //заполняю сообщение пользователю (чтобы он был вкурсе что его сообщение обработано)

        executeMessage(messageVolunteer);                               //отправляем сообщение контактными данными пользователя в личку волонтёру
        executeMessage(messageUser);
    }
}
