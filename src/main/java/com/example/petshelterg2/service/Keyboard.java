package com.example.petshelterg2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.petshelterg2.constants.Constants.*;


@Slf4j
@Component
public class Keyboard {
    SendMessageService messageService;
    SelectionService selectionService;
    @Autowired
    public Keyboard(SendMessageService messageService,SelectionService selectionService) {
        this.messageService = messageService;
        this.selectionService = selectionService;
    }

    /**
     * Метод собирает стартовую клавиатуру <p>
     * Реализуя две кнопки на основе: <p>
     * {@link com.example.petshelterg2.constants.Constants#CAT_SHELTER_BUTTON} <p>
     * {@link com.example.petshelterg2.constants.Constants#DOG_SHELTER_BUTTON} <p>
     *
     * @return <b>ReplyKeyboardMarkup</b> (собранная клавиатура)
     */
    public ReplyKeyboardMarkup startKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(); //создание клавиатуры
        List<KeyboardRow> keyboardRows = new ArrayList<>();             //создание рядов в клавиатуре

        KeyboardRow row = new KeyboardRow();                            //первый ряд клавиатуры
        row.add(CAT_SHELTER_BUTTON);                                    //добавление кнопок (слева будут первые созданные)
        row.add(DOG_SHELTER_BUTTON);
        keyboardRows.add(row);                                          //добавляем в клавиатуру ряд
        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    /**
     * Метод создает клавиатуру для собачено приюта <p>
     * Кнопка: <p>
     * {@value  com.example.petshelterg2.constants.Constants#CONTACT_WITH_ME_BUTTON_DOG} <p>
     * Является функциональной и запрашивает контакт у пользователя
     *
     * @return <b>ReplyKeyboardMarkup</b>
     */
    public ReplyKeyboardMarkup dogShelterKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(ABOUT_SHELTER_BUTTON_DOG);
        row.add(SHELTER_SECOND_STEP_BUTTON_DOG);
        row.add(SHELTER_THIRD_STEP_BUTTON_DOG);
        keyboardRows.add(row);

        row = new KeyboardRow();

        KeyboardButton keyboardButtonDog = new KeyboardButton();   //создал функциональную кнопку
        keyboardButtonDog.setText(CONTACT_WITH_ME_BUTTON_DOG);                   //добавил в кнопку отображаемый текст
        keyboardButtonDog.setRequestContact(true);                 //добавил в кнопку запрос контакта у пользователя

        row.add(keyboardButtonDog);
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;

    }

    /**
     * Метод создает клавиатуру для кошачьего приюта <p>
     * Кнопка: <p>
     * {@value  com.example.petshelterg2.constants.Constants#CONTACT_WITH_ME_BUTTON_CAT} <p>
     * Является функциональной и запрашивает контакт у пользователя
     *
     * @return <b>ReplyKeyboardMarkup</b>
     */
    public ReplyKeyboardMarkup catShelterKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(ABOUT_SHELTER_BUTTON_CAT);
        row.add(SHELTER_SECOND_STEP_BUTTON_CAT);
        row.add(SHELTER_THIRD_STEP_BUTTON_CAT);
        keyboardRows.add(row);

        row = new KeyboardRow();

        KeyboardButton keyboardButtonCat = new KeyboardButton();   //создал функциональную кнопку
        keyboardButtonCat.setText(CONTACT_WITH_ME_BUTTON_CAT);                   //добавил в кнопку отображаемый текст
        keyboardButtonCat.setRequestContact(true);                 //добавил в кнопку запрос контакта у пользователя

        row.add(keyboardButtonCat);
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup informationCatShelterKeyboard() {//клавиатура с информацией о кошачьем приюте
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(SHELTER_SCHEDULE_BUTTON_CAT);
        row.add(SECURITY_CONTACTS_BUTTON_CAT);
        row.add(SAFETY_NOTES_BUTTON_CAT);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup informationDogShelterKeyboard() {//клавиатура с информацией о собачьем приюте
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(SHELTER_SCHEDULE_BUTTON_DOG);
        row.add(SECURITY_CONTACTS_BUTTON_DOG);
        row.add(SAFETY_NOTES_BUTTON_DOG);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup takeAnDogShelterKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(RULES_FOR_GETTING_KNOW_DOG);
        row.add(LIST_DOCUMENTS_TAKE_ANIMAL_DOG);
        row.add(RECOMMENDATIONS_TRANSPORTATION_DOG);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_HOME_BUTTON1_DOG);
        row.add(TIPS_DOG_HANDLER_AND_WHY_THEY_MAY_REFUSE_TAKE_ANIMAL);
        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup takeAnCatShelterKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(RULES_FOR_GETTING_KNOW_CAT);
        row.add(LIST_DOCUMENTS_TAKE_ANIMAL_CAT);
        row.add(RECOMMENDATIONS_TRANSPORTATION_CAT);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_HOME_BUTTON1_CAT);
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup recommendationsHomeDogKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(RECOMMENDATIONS_HOME_BUTTON2_DOG);
        row.add(RECOMMENDATIONS_HOME_PUPPY);
        row.add(RECOMMENDATIONS_HOME_DOG_WITH_DISABILITIES);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup recommendationsHomeCatKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(RECOMMENDATIONS_HOME_BUTTON2_CAT);
        row.add(RECOMMENDATIONS_HOME_KITTY);
        row.add(RECOMMENDATIONS_HOME_CAT_WITH_DISABILITIES);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup tipsFromDogKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(TIPS_DOG_HANDLER_COMMUNICATE_WITH_DOG);
        row.add(RECOMMENDATIONS_FURTHER_REFERENCE_THEM);
        row.add(LIST_OF_REASONS_WHY_THEY_MAY_REFUSE_DOG);
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    //    НАЧАЛО БЛОКА КЛАВИАТУР---------------------------------------------------------------
    public void mainMenu(long chatId, String name) { //метод отправки главного меню
        String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT, name);
        messageService.prepareAndSendMessageAndKeyboard(chatId, answer, startKeyboard());
    }

    public void dog(long chatId) {//метод для перехода в собачий приют, с клавиатурой
        messageService.prepareAndSendMessageAndKeyboard(chatId, DOG_SHELTER_SELECT_TEXT, dogShelterKeyboard());
        selectionService.save(chatId, true, 0);                      //сохранили клиента в БД Selection с выбором собак
    }

    public void cat(long chatId) {//метод для перехода в кошачий приют, с клавиатурой
        messageService.prepareAndSendMessageAndKeyboard(chatId, CAT_SHELTER_SELECT_TEXT, catShelterKeyboard());
        selectionService.save(chatId, false, 0);                      //сохранили клиента в БД Selection с выбором кошек
    }

    public void informationCatShelter(long chatId) {//метод для перехода в информацию о кошачьем приюте, с клавиатурой
        messageService.prepareAndSendMessageAndKeyboard(chatId, ABOUT_CAT_SHELTER_TEXT, informationCatShelterKeyboard());
    }

    public void informationDogShelter(long chatId) {//метод для перехода в информацию о собачьем приюте, с клавиатурой
        messageService.prepareAndSendMessageAndKeyboard(chatId, ABOUT_DOG_SHELTER_TEXT, informationDogShelterKeyboard());
    }

    public void takeAnCat(long chatId) { //переход в меню как взять кошку из приюта
        messageService.prepareAndSendMessageAndKeyboard(chatId, CAT_TAKING_ANIMAL_FROM_SHELTER, takeAnCatShelterKeyboard());
    }

    public void takeAnDog(long chatId) { //переход в меню как взять собаку из приюта
        messageService.prepareAndSendMessageAndKeyboard(chatId, DOG_TAKING_ANIMAL_FROM_SHELTER, takeAnDogShelterKeyboard());
    }

    public void recommendationsHomeDog(long chatId) { //переход в меню обустройство дома для собаки
        messageService.prepareAndSendMessageAndKeyboard(chatId, ARRANGING_HOME_RECOMMENDATIONS, recommendationsHomeDogKeyboard());
    }

    public void recommendationsHomeCat(long chatId) { //переход в меню обустройство дома кошки
        messageService.prepareAndSendMessageAndKeyboard(chatId, ARRANGING_HOME_RECOMMENDATIONS, recommendationsHomeCatKeyboard());
    }

    public void tipsFromDog(long chatId) { //переход в меню советы кинолога и почему могут отказать забрать собаку из приюта
        messageService.prepareAndSendMessageAndKeyboard(chatId, TIPS_DOG_HANDLER, tipsFromDogKeyboard());
    }

    public void catShelterWork(long chatId) {
        messageService.prepareAndSendMessage(chatId, CAT_SHELTER_WORK_SCHEDULE);
    }

    public void dogShelterWork(long chatId) {
        messageService.prepareAndSendMessage(chatId, DOG_SHELTER_WORK_SCHEDULE);
    }

    public void catShelterSecurityContacts(long chatId) {
        messageService.prepareAndSendMessage(chatId, CAT_SHELTER_SECURITY_CONTACTS);
    }

    public void dogShelterSecurityContacts(long chatId) {
        messageService.prepareAndSendMessage(chatId, DOG_SHELTER_SECURITY_CONTACTS);
    }

    public void safetyNotesDog(long chatId) {
        messageService.prepareAndSendMessage(chatId, SAFETY_NOTES_DOG);
    }

    public void safetyNotesCat(long chatId) {
        messageService.prepareAndSendMessage(chatId, SAFETY_NOTES_CAT);
    }

    public void safetyNotesRulesForFirstMetCat(long chatId) {
        messageService.prepareAndSendMessage(chatId, RULES_FOR_FIRST_MET_CAT);
    }

    public void safetyNotesRulesForFirstMetDog(long chatId) {
        messageService.prepareAndSendMessage(chatId, RULES_FOR_FIRST_MET_DOG);
    }

    public void listOfDocumentsForAdoption(long chatId) {
        messageService.prepareAndSendMessage(chatId, LIST_OF_DOCUMENTS_FOR_ADOPTION);
    }

    public void transportingRecommendationsCat(long chatId) {
        messageService.prepareAndSendMessage(chatId, TRANSPORTING_RECOMMENDATIONS_CAT);
    }

    public void transportingRecommendationsDog(long chatId) {
        messageService.prepareAndSendMessage(chatId, TRANSPORTING_RECOMMENDATIONS_DOG);
    }

    public void arrangingHomeRecommendationsKitty(long chatId) {
        messageService.prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_KITTY);
    }

    public void arrangingHomeRecommendationsPuppy(long chatId) {
        messageService.prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_PUPPY);
    }

    public void arrangingHomeRecommendationsCat(long chatId) {
        messageService.prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_CAT);
    }

    public void arrangingHomeRecommendationsDog(long chatId) {
        messageService.prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_DOG);
    }

    public void arrangingHomeRecommendationsDisabledCat(long chatId) {
        messageService.prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_DISABLED_CAT);
    }

    public void arrangingHomeRecommendationsDisabledDog(long chatId) {
        messageService.prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_DISABLED_DOG);
    }

    public void initialDogHandlerAdvice(long chatId) {
        messageService.prepareAndSendMessage(chatId, INITIAL_DOG_HANDLER_ADVICE);
    }

    public void dogHandlerRecommendation(long chatId) {
        messageService.prepareAndSendMessage(chatId, DOG_HANDLER_RECOMMENDATION);
    }

    public void refusalReasonsList(long chatId) {
        messageService.prepareAndSendMessage(chatId, REFUSAL_REASONS_LIST);
    }

    public void defaultAnswer(long chatId){
        messageService.prepareAndSendMessage(chatId, "Я пока не знаю как на это ответить!");
    }
//  ОКОНЧАНИЕ БЛОКА КЛАВИАТУР --------------------------------------------------------------------------------------
}
