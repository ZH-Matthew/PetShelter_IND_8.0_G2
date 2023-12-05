package com.example.petshelterg2.service;

import com.example.petshelterg2.model.Selection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.example.petshelterg2.constants.Constants.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneralServiceTest {

    @Mock
    SendMessageService sendMService;
    @Mock
    SelectionService sService;
    @Mock
    Keyboard keyboard;
    @Mock
    ReplyKeyboardMarkup replyKeyboardMarkup;
    @Mock
    Update update;
    @Mock
    DogService dogService;
    @Mock
    CatService catService;
    @Mock
    Message message;
    @InjectMocks
    GeneralService service;


    long chatId = 123L;
    String name = "name";
    String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT_START, name);

    String messageText = "message text";
    @Test
    public void startCommandTest(){
        when(keyboard.startKeyboard()).thenReturn(replyKeyboardMarkup);

        service.startCommand(chatId,name);

        verify(sendMService,times(1)).prepareAndSendMessageAndKeyboard(chatId,answer,replyKeyboardMarkup);
        verify(sService,times(1)).save(chatId, null, 0);
    }

    @Test
    public void saveContactTrueTest(){
        Selection selection = new Selection();
        selection.setSelection(true);
        when(sService.findById(chatId)).thenReturn(selection);

        service.saveContact(chatId,update);

        verify(dogService,times(1)).saveOwner(update);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, DATA_SAVED);
    }

    @Test
    public void saveContactFalseTest(){
        Selection selection = new Selection();
        selection.setSelection(false);
        when(sService.findById(chatId)).thenReturn(selection);

        service.saveContact(chatId,update);

        verify(catService,times(1)).saveOwner(update);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, DATA_SAVED);
    }

    @Test
    public void processPhotoTrueTest(){
        Selection selection = new Selection();
        selection.setCounter(1);
        selection.setSelection(true);

        when(sService.findById(chatId)).thenReturn(selection);
        when(update.getMessage()).thenReturn(message);

        service.processPhoto(chatId,update);

        verify(dogService,times(1)).processPhoto(message);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, DIET_DOG);
        verify(sService,times(1)).save(chatId, true, 2);
    }

    @Test
    public void processPhotoFalseTest(){
        Selection selection = new Selection();
        selection.setCounter(1);
        selection.setSelection(false);

        when(sService.findById(chatId)).thenReturn(selection);
        when(update.getMessage()).thenReturn(message);

        service.processPhoto(chatId,update);

        verify(catService,times(1)).processPhoto(message);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, DIET_CAT);
        verify(sService,times(1)).save(chatId, false, 2);
    }

    @Test
    public void processPhotoFailedTest(){
        Selection selection = new Selection();
        selection.setCounter(0); //каунтер нулевой (кнопка не была нажата)
        selection.setSelection(false);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processPhoto(chatId,update);

        verify(catService,times(0)).processPhoto(message);
        verify(sendMService,times(0)).prepareAndSendMessage(chatId, DIET_CAT);
        verify(sService,times(0)).save(chatId, false, 2);
    }

    @Test
    public void processReportCounter5Test(){
        Selection selection = new Selection();
        selection.setCounter(5);
        selection.setSelection(false);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(sService,times(1)).save(chatId, false, 0);
    }

    @Test
    public void prCounter2FalseTest(){
        Selection selection = new Selection();
        selection.setCounter(2);
        selection.setSelection(false);

        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(messageText);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(catService,times(1)).reportDiet(messageText, chatId);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, WELL_BEING_AND_ADAPTATION_CAT);
        verify(sService,times(1)).save(chatId, false, 3);
    }

    @Test
    public void prCounter3FalseTest(){
        Selection selection = new Selection();
        selection.setCounter(3);
        selection.setSelection(false);

        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(messageText);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(catService,times(1)).reportWellBeingAndAdaptation(messageText, chatId);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, CHANGES_BEHAVIOR_CAT);
        verify(sService,times(1)).save(chatId, false, 4);
    }

    @Test
    public void prCounter4FalseTest(){
        Selection selection = new Selection();
        selection.setCounter(4);
        selection.setSelection(false);

        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(messageText);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(catService,times(1)).reportChangesBehavior(messageText, chatId);
        verify(keyboard,times(1)).mainMenu(chatId, name);
        verify(sService,times(1)).save(chatId, false, 5);
    }

    @Test
    public void prCounter2TrueTest(){
        Selection selection = new Selection();
        selection.setCounter(2);
        selection.setSelection(true);

        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(messageText);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(dogService,times(1)).reportDiet(messageText, chatId);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, WELL_BEING_AND_ADAPTATION_DOG);
        verify(sService,times(1)).save(chatId, true, 3);
    }

    @Test
    public void prCounter3TrueTest(){
        Selection selection = new Selection();
        selection.setCounter(3);
        selection.setSelection(true);

        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(messageText);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(dogService,times(1)).reportWellBeingAndAdaptation(messageText, chatId);
        verify(sendMService,times(1)).prepareAndSendMessage(chatId, CHANGES_BEHAVIOR_DOG);
        verify(sService,times(1)).save(chatId, true, 4);
    }

    @Test
    public void prCounter4TrueTest(){
        Selection selection = new Selection();
        selection.setCounter(4);
        selection.setSelection(true);

        when(update.getMessage()).thenReturn(message);
        when(update.hasMessage()).thenReturn(true);
        when(message.hasText()).thenReturn(true);
        when(message.getText()).thenReturn(messageText);

        when(sService.findById(chatId)).thenReturn(selection);

        service.processReport(chatId,name,update);

        verify(dogService,times(1)).reportChangesBehavior(messageText, chatId);
        verify(keyboard,times(1)).mainMenu(chatId, name);
        verify(sService,times(1)).save(chatId, true, 5);
    }
}