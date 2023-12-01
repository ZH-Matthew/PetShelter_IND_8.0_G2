package com.example.petshelterg2.service;

import lombok.extern.slf4j.Slf4j;
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
}
