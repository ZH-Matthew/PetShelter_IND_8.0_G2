package com.example.petshelterg2.controller;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.service.GeneralService;
import com.example.petshelterg2.service.Keyboard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.*;
import java.util.List;
import static com.example.petshelterg2.constants.Constants.MAIN_MAIN;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    Update update;
    @Mock
    Chat chat;
    @Mock
    Message message;
    @Mock
    GeneralService gService;
    @Mock
    List<PhotoSize> photo;
    @Mock
    Contact contact;
    @Mock
    BotConfig config;
    @Mock
    Keyboard keyboard;
    @InjectMocks
    TelegramBot bot;

    String name = "name";
    long chatId = 123;

    String chatIdAdmin = "123";
    String textStart = "/start";
    String textSend = "/send";

    @Test
    void onURStartTest() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(name);

        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(textStart);

        bot.onUpdateReceived(update);

        verify(gService,(times(1))).startCommand(chatId, name);
    }

    @Test
    void onURContactTest() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(name);

        when(message.getContact()).thenReturn(contact);

        bot.onUpdateReceived(update);

        verify(gService,(times(1))).saveContact(chatId, update);
    }

    @Test
    void onURProcessReportTest() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(name);

        when(message.getPhoto()).thenReturn(photo);

        bot.onUpdateReceived(update);

        verify(gService,(times(1))).processPhoto(chatId, update);
    }

    @Test
    void onURPhotoTest() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(name);

        when(gService.getSelectionCounter(chatId)).thenReturn(1);

        bot.onUpdateReceived(update);

        verify(gService,(times(1))).processReport(chatId, name, update);
    }

    @Test
    void onURSendAdminMessageTest() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(name);

        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(gService.getSelectionCounter(chatId)).thenReturn(0);
        when(message.getText()).thenReturn(textSend);
        when(config.getOwnerId()).thenReturn(chatIdAdmin);

        bot.onUpdateReceived(update);

        verify(gService,(times(1))).sendAdminMessage(textSend);
    }

    @Test
    void onURSendGeneralMethodTest() {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(name);

        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(gService.getSelectionCounter(chatId)).thenReturn(0);
        when(message.getText()).thenReturn(MAIN_MAIN);

        bot.onUpdateReceived(update);

        verify(keyboard,(times(1))).mainMenu(chatId, name);
    }
}