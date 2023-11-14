package com.example.petshelterg2.controller;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.model.CatOwners;
import com.example.petshelterg2.repository.CatOwnersRepository;
import com.example.petshelterg2.repository.DogOwnersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
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

    @Autowired
    private DogOwnersRepository dogOwnersRepository;

    @Autowired
    private CatOwnersRepository catOwnersRepository;

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

    public String getBotOwnerId() {
        return config.getOwnerId();
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
                case MAIN_MAIN:
                    mainMenu(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case CAT_SHELTER_BUTTON:
                    cat(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case DOG_SHELTER_BUTTON:
                    dog(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case CALL_VOLUNTEER_BUTTON:
                    callAVolunteer(chatId, update.getMessage().getChat().getUserName());
                    break;
                case SAVE_ADMIN: //показывает CHAT_ID в логи консоли (никуда не сохраняет данные)
                    showAdminChatId(update);
                    break;
                case CONTACT_WITH_ME_BUTTON:
                    callMe(update);
                    break;
                default:
                    prepareAndSendMessage(chatId, "Я пока не знаю как на это ответить!");
            }
        }
    }


    //метод для приветственного сообщения
    private void startCommand(long chatId, String name) {
        // добавление смайликов в строку (на сайте эмоджипедиа, либо можно зайти в телегу и навести на смайлик, он выдаст код)
        String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT_START, name);
        prepareAndSendMessageAndKeyboard(chatId, answer, startKeyboard());                    // вызываем метод подготовки сообщения
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }


    //метод подготовки сообщения и его отправки
    private void prepareAndSendMessageAndKeyboard(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        executeMessage(message); //вызываем метод отправки сообщения
    }

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

    private void mainMenu(long chatId, String name) {
        String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT, name);
        prepareAndSendMessageAndKeyboard(chatId, answer, startKeyboard());
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }

    private void dog(long chatId, String name) {//метод для перехода в собачий приют, с клавиатурой
        prepareAndSendMessageAndKeyboard(chatId, DOG_SHELTER_SELECT_TEXT, dogShelterKeyboard());
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }

    private void cat(long chatId, String name) {//метод для перехода в кошачий приют, с клавиатурой
        prepareAndSendMessageAndKeyboard(chatId, CAT_SHELTER_SELECT_TEXT, catShelterKeyboard());
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }

    private ReplyKeyboardMarkup startKeyboard() {//стартовая клавиатура
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();//создание клавиатуры
        List<KeyboardRow> keyboardRows = new ArrayList<>();//создание рядов в клавиатуре

        KeyboardRow row = new KeyboardRow();//первый ряд клавиатуры
        row.add(CAT_SHELTER_BUTTON);//добавление кнопок (слева будут первые созданные)
        row.add(DOG_SHELTER_BUTTON);
        keyboardRows.add(row);//добавляем в клавиатуру ряд
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
        row.add(CONTACT_WITH_ME_BUTTON);
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
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
        row.add(CONTACT_WITH_ME_BUTTON);
        row.add(CALL_VOLUNTEER_BUTTON);
        row.add(MAIN_MAIN);
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);
        return keyboardMarkup;
    }

    private void callAVolunteer(long chatId, String userName) {       //метод для вызова волонтера (суть метода: отправить волонтёру в личку ссылку на пользователя чтобы волнтёр законнектил чаты и начал общение)
        SendMessage messageVolunteer = new SendMessage();           //принимает два параметра: chatID пользователя и его никнейм
        SendMessage messageUser = new SendMessage();                //создаёт два сообщения, одно волонтеру, другое пользователю

        messageVolunteer.setChatId(getBotOwnerId());             // дергаю метод внутри класса, который вызывает getOwnerId (переменную из BotConfig),тот в свою очередь берет инфу о переменной из файла app.prop
        messageVolunteer.setText(VOLUNTEER_MESSAGE + userName);  //формируем сообщение для волонтёра
        messageUser.setChatId(String.valueOf(chatId));
        messageUser.setText(VOLUNTEER_WILL_WRITE_TO_YOU);       //заполняю сооб

        executeMessage(messageVolunteer);                        //отправляем сообщение контактными данными пользователя в личку волонтёру
        executeMessage(messageUser);
    }

    private void showAdminChatId(Update update) { //метод выводит в лог консоли ChatId админа, если была написана команда "сохранить админа"
        Long chatId = update.getMessage().getChatId();
        log.info("ADMIN CHAT_ID: " + chatId);
    }

    private void saveContactCat(Update update) {

        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getChat().getFirstName();
        String lastName = update.getMessage().getChat().getLastName();
        String userName = update.getMessage().getChat().getUserName();
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();

        CatOwners catOwners = new CatOwners(chatId, lastName, firstName, userName, phoneNumber, null, null);
        catOwnersRepository.save(catOwners);
        log.info("contact saved");

    }

    private ReplyKeyboardMarkup catContactMe() {
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        KeyboardButton keyboardButton = new KeyboardButton();

        keyboardButton.setText("Поделиться номером >");
        keyboardButton.setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private void callMe(Update update) {//метод для перехода в кошачий приют, с клавиатурой
        prepareAndSendMessageAndKeyboard(update.getMessage().getChatId(), "Оставьте свой номер телефона, нажав на кнопку снизу экрана", catContactMe());
        log.info("Replied to user " + update.getMessage().getChat().getFirstName());//лог о том что мы ответили пользователю
        saveContactCat(update);
    }
}
