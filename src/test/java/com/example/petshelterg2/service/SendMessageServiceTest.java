package com.example.petshelterg2.service;

import com.example.petshelterg2.controller.TelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.example.petshelterg2.constants.Constants.VOLUNTEER_MESSAGE;
import static com.example.petshelterg2.constants.Constants.VOLUNTEER_WILL_WRITE_TO_YOU;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendMessageServiceTest {

    @Mock
    TelegramBot bot;

    @InjectMocks
    SendMessageService service;

    SendMessage message = new SendMessage();

    long chaId = 12345;
    String chatIdAdmin = "54321";
    String name = "@name";
    String text = "textToSend";
    String firstButton = "Первая кнопка";
    String lastButton = "Вторая кнопка";

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(); //создание клавиатуры
    List<KeyboardRow> keyboardRows = new ArrayList<>();             //создание рядов в клавиатуре
    KeyboardRow row = new KeyboardRow();                            //первый ряд клавиатуры

    SendMessage messageVolunteer = new SendMessage();
    SendMessage messageUser = new SendMessage();

    {
        message.setChatId(String.valueOf(chaId));
        message.setText(text);
        row.add(firstButton);                                    //добавление кнопок (слева будут первые созданные)
        row.add(lastButton);
        keyboardRows.add(row);                                          //добавляем в клавиатуру ряд
        keyboardMarkup.setKeyboard(keyboardRows);
        messageVolunteer.setChatId(chatIdAdmin);
        messageVolunteer.setText(VOLUNTEER_MESSAGE + name);
        messageUser.setChatId(String.valueOf(chaId));
        messageUser.setText(VOLUNTEER_WILL_WRITE_TO_YOU);
    }

    @Test
    void executeMessageTest() throws TelegramApiException {
        service.executeMessage(message);
        ArgumentCaptor<SendMessage> argument = ArgumentCaptor.forClass(SendMessage.class); //создаем захват аргумента SendMessage
        Mockito.verify(bot).execute(argument.capture()); //захватываем наш аргумент в нужном нам методе через verify
        assertEquals("12345", argument.getValue().getChatId());
        assertEquals("textToSend", argument.getValue().getText());
    }

    @Test
    void prepareAndSendMessageTest() throws TelegramApiException {
        service.prepareAndSendMessage(chaId,text);
        ArgumentCaptor<SendMessage> argument = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(bot).execute(argument.capture());
        assertEquals("12345", argument.getValue().getChatId());
        assertEquals("textToSend", argument.getValue().getText());
    }

    @Test
    void prepareAndSendMessageAndKeyboardTest() throws TelegramApiException {
        service.prepareAndSendMessageAndKeyboard(chaId,text,keyboardMarkup);
        ArgumentCaptor<SendMessage> argument = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(bot).execute(argument.capture());
        assertEquals("12345", argument.getValue().getChatId());
        assertEquals("textToSend", argument.getValue().getText());
        assertEquals(keyboardMarkup,argument.getValue().getReplyMarkup());
    }

    @Test
    void callAVolunteerTest() throws TelegramApiException {
        service.callAVolunteer(chaId,name,chatIdAdmin);
        verify(bot,times(1)).execute(messageVolunteer); //один раз должен вызваться execute с месседжем для админа(волонтёра)
        verify(bot,times(1)).execute(messageUser); //один раз должен вызваться execute с месседжем для пользователя
    }
}