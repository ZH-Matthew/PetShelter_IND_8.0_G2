package com.example.petshelterg2.service;

import com.example.petshelterg2.model.Probation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.example.petshelterg2.constants.Constants.*;

@Slf4j
@Component
public class GeneralService {
    SendMessageService sendMService;
    SelectionService sService;
    Keyboard keyboard;
    CatService catService;
    DogService dogService;

    @Autowired
    public GeneralService(SendMessageService sendMService, SelectionService sService, Keyboard keyboard, CatService catService, DogService dogService) {
        this.sendMService = sendMService;
        this.sService = sService;
        this.keyboard = keyboard;
        this.catService = catService;
        this.dogService = dogService;
    }

    /**
     * Метод обрабатывающий команду <b>/start</b>
     * <p>
     * Собирает текст ответа и отправляет его в метод: {@link SendMessageService#prepareAndSendMessageAndKeyboard(long, String, ReplyKeyboardMarkup)}
     *
     * @param chatId (ID чата пользователя)
     * @param name   (имя пользователя)
     */
    public void startCommand(long chatId, String name) {
        // добавление смайликов в строку (на сайте эмоджипедиа, либо можно зайти в телегу и навести на смайлик, он выдаст код)
        String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT_START, name);
        sendMService.prepareAndSendMessageAndKeyboard(chatId, answer, keyboard.startKeyboard());                    // вызываем метод подготовки сообщения
        sService.save(chatId, null, 0);
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }


    //метод сохранения контакта по кнопке "Оставить контактные данные
    public void saveContact(long chatId, Update update) {
        Boolean selection = sService.findById(chatId).getSelection();
        if (selection) {
            dogService.saveOwner(update);                           //вызывает метод сохранения пользователя в БД к владельцам собак
            sendMService.prepareAndSendMessage(chatId, DATA_SAVED);
        } else {
            catService.saveOwner(update);                           //вызывает метод сохранения пользователя в БД к владельцам кошек
            sendMService.prepareAndSendMessage(chatId, DATA_SAVED);
        }
    }

    //метод сохранения фотографии и запуска процесса отчёта
    public void processPhoto(long chatId, Update update) {
        boolean counter = sService.findById(chatId).getCounter() == 1;
        boolean selection = sService.findById(chatId).getSelection();

        if (counter && !selection) {//кошки
            catService.processPhoto(update.getMessage());
            sendMService.prepareAndSendMessage(chatId, DIET_CAT);
            sService.save(chatId, false, 2);
        }
        if (counter && selection) {//собаки
            dogService.processPhoto(update.getMessage());
            sendMService.prepareAndSendMessage(chatId, DIET_DOG);
            sService.save(chatId, true, 2);
        }
    }

    public void processReport(long chatId,String name, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Integer counter = sService.findById(chatId).getCounter();
            boolean selection = sService.findById(chatId).getSelection();
            String messageText = update.getMessage().getText();
            if (counter != null && !selection) { //кошки
                switch (counter) {
                    case 2:
                        catService.reportDiet(messageText, chatId);
                        sendMService.prepareAndSendMessage(chatId, WELL_BEING_AND_ADAPTATION_CAT);
                        sService.save(chatId, false, 3);
                        break;
                    case 3:
                        catService.reportWellBeingAndAdaptation(messageText, chatId);
                        sendMService.prepareAndSendMessage(chatId, CHANGES_BEHAVIOR_CAT);
                        sService.save(chatId, false, 4);
                        break;
                    case 4:
                        catService.reportChangesBehavior(messageText, chatId);
                        sService.save(chatId, false, 0);
                        keyboard.mainMenu(chatId, name);
                        break;
                }
            }
            if (counter != null && selection) { //собаки
                switch (counter) {
                    case 2:
                        dogService.reportDiet(messageText, chatId);
                        sendMService.prepareAndSendMessage(chatId, WELL_BEING_AND_ADAPTATION_DOG);
                        sService.save(chatId, true, 3);
                        break;
                    case 3:
                        dogService.reportWellBeingAndAdaptation(messageText, chatId);
                        sendMService.prepareAndSendMessage(chatId, CHANGES_BEHAVIOR_DOG);
                        sService.save(chatId, true, 4);
                        break;
                    case 4:
                        dogService.reportChangesBehavior(messageText, chatId);
                        sService.save(chatId, true, 0);
                        keyboard.mainMenu(chatId, name);
                        break;
                }
            }
        }
    }

    public void sendAdminMessage(String messageText){
        String[] message = messageText.split(" ");                                            //разделили сообщение на части по пробелам !!!добавить в сплит параметр split(" ", 2), так строк будет только 2 , первая до пробела и вторая после.
        long userChatId = Long.parseLong(message[1]);                                                 //преобразовали строку с chatId в лонг
        sendMService.prepareAndSendMessage(userChatId, MESSAGE_BAD_REPORT);                                       //отправили сообщение пользователю
        log.info("The admin sent a message about the poor quality of the report. ChatID: " + userChatId);
    }

    public void shelterThirdCat(long chatId) {
        Probation ownerProbation = catService.findOwnerById(chatId).getProbation();
        if (ownerProbation.equals(Probation.IN_PROGRESS)) {
            sendMService.prepareAndSendMessage(chatId, SHELTER_THIRD_STEP_CAT);
            sService.save(chatId, false, 1);
            log.info("Replied to user " + chatId);
        } else {
            sendMService.prepareAndSendMessage(chatId, NO_NEED_TO_SEND_A_REPORT);
        }
    }

    public void shelterThirdDog(long chatId) {
        Probation ownerProbation = dogService.findOwnerById(chatId).getProbation();
        if (ownerProbation.equals(Probation.IN_PROGRESS)) {
            sendMService.prepareAndSendMessage(chatId, SHELTER_THIRD_STEP_DOG);
            sService.save(chatId, true, 1);
            log.info("Replied to user " + chatId);
        } else {
            sendMService.prepareAndSendMessage(chatId, NO_NEED_TO_SEND_A_REPORT);
        }
    }

    public void callAVolunteer(long chatId,Update update,String adminId){
        String userName = update.getMessage().getChat().getUserName();
        sendMService.callAVolunteer(chatId,userName,adminId);
    }

    public int getSelectionCounter(long chatId){
        return sService.findById(chatId).getCounter();
    }
}
