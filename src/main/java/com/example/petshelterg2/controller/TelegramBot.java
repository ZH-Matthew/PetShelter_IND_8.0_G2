package com.example.petshelterg2.controller;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.model.*;
import com.example.petshelterg2.repository.*;
import com.example.petshelterg2.service.CatService;
import com.example.petshelterg2.service.DogService;
import com.example.petshelterg2.service.Keyboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.petshelterg2.constants.Constants.*;

@Slf4j //из библиотеки lombok реализует логирование через переменную log.
@Component //аннотация позволяет автоматически создать экземпляр
public class TelegramBot extends TelegramLongPollingBot {  //есть еще класс WebHookBot (разница в том что WebHook уведомляет нас каждый раз при написании сообщения пользователе, LongPolling сам проверяет не написали ли ему (он более простой)

    private final BotConfig config;
    private final DogOwnersRepository dogOwnersRepository;
    private final CatOwnersRepository catOwnersRepository;
    private final SelectionRepository selectionRepository;
    private final Keyboard keyboard;
    private final CatService catService;
    private final DogService dogService;

    @Autowired
    public TelegramBot(BotConfig config, DogOwnersRepository dogOwnersRepository, CatOwnersRepository catOwnersRepository, SelectionRepository selectionRepository, Keyboard keyboard, CatService catService, DogService dogService) {
        this.config = config;
        this.dogOwnersRepository = dogOwnersRepository;
        this.catOwnersRepository = catOwnersRepository;
        this.selectionRepository = selectionRepository;
        this.keyboard = keyboard;
        this.dogService = dogService;
        this.catService = catService;
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

    /**
     * Единственная задача метода: <p>
     * Запросить из {@link BotConfig} значение <b>ownerId</b>
     *
     * @return String (chatID админа/волонтёра)
     */
    public String getBotOwnerId() {
        return config.getOwnerId();
    }

    //реализация основного метода общения с пользователем (главный метод приложения)
    @Override
    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getChat().getFirstName();

        if (update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
            startCommand(chatId, name);
        }
        //проверка наличия телефона (если он есть проверка на то какую сторону выбрал пользователь, и далее сохранение в БД
        if (update.getMessage().getContact() != null) {         //проверяет есть ли у пользователя контакт, если есть, сохраняет его.
            Boolean selection = selectionRepository.findById(chatId).get().getSelection();
            if (selection) {
                dogService.saveOwner(update);                           //вызывает метод сохранения пользователя в БД к владельцам собак
                prepareAndSendMessage(chatId, DATA_SAVED);
            } else {
                catService.saveOwner(update);                           //вызывает метод сохранения пользователя в БД к владельцам кошек
                prepareAndSendMessage(chatId, DATA_SAVED);
            }
        }
        if (update.getMessage().getPhoto() != null) {
            boolean counter = selectionRepository.findById(chatId).get().getCounter() == 1;
            boolean selection = selectionRepository.findById(chatId).get().getSelection();

            if (counter && !selection) {//кошки
                catService.processPhoto(update.getMessage());
                photoShelterThirdCat(chatId, name);
            }
            if (counter && selection) {//собаки
                dogService.processPhoto(update.getMessage());
                photoShelterThirdDog(chatId, name);
            }

        }
        if (selectionRepository.findById(chatId).get().getCounter() != 0) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Integer counter = selectionRepository.findById(chatId).get().getCounter();
                boolean selection = selectionRepository.findById(chatId).get().getSelection();
                String messageText = update.getMessage().getText();
                if (counter != null && !selection) { //кошки
                    switch (counter) {
                        case 2:
                            catService.reportDiet(messageText, chatId);
                            dietShelterThirdCat(chatId, name);
                            break;
                        case 3:
                            catService.reportWellBeingAndAdaptation(messageText, chatId);
                            changesBehaviorShelterThirdCat(chatId, name);
                            break;
                        case 4:
                            catService.reportChangesBehavior(messageText, chatId);
                            saveSelection(chatId, false, 0);
                            mainMenu(chatId, name);
                            break;
                    }
                }
                if (counter != null && selection) { //собаки
                    switch (counter) {
                        case 2:
                            dogService.reportDiet(messageText, chatId);
                            dietShelterThirdDog(chatId, name);
                            break;
                        case 3:
                            dogService.reportWellBeingAndAdaptation(messageText, chatId);
                            changesBehaviorShelterThirdDog(chatId, name);
                            break;
                        case 4:
                            dogService.reportChangesBehavior(messageText, chatId);
                            saveSelection(chatId, true, 0);
                            mainMenu(chatId, name);
                            break;
                    }
                }
            }
        }

        if (update.hasMessage() && update.getMessage().hasText() && selectionRepository.findById(chatId).get().getCounter() == 0) { //проверяем что сообщение пришло и там есть текст
            String messageText = update.getMessage().getText();

            if (messageText.contains("/send") && config.getOwnerId().equals(Long.toString(chatId))) {       //условие для отправки сообщение от админа (может быть расширено для большего количества сообщений админа, для этого нужно вынести проверку на /send в отдельное вложенное условие)
                String[] message = messageText.split(" ");                                            //разделили сообщение на части по пробелам !!!добавить в сплит параметр split(" ", 2), так строк будет только 2 , первая до пробела и вторая после.
                long userChatId = Long.parseLong(message[1]);                                                 //преобразовали строку с chatId в лонг
                prepareAndSendMessage(userChatId, MESSAGE_BAD_REPORT);                                       //отправили сообщение пользователю
                log.info("The admin sent a message about the poor quality of the report. ChatID: " + userChatId);
                return;
            }


            switch (messageText) {
                case "/start":
                    break;
                case MAIN_MAIN:
                    mainMenu(chatId, name);
                    break;
                case CAT_SHELTER_BUTTON:
                    cat(chatId);
                    break;
                case DOG_SHELTER_BUTTON:
                    dog(chatId);
                    break;
                case ABOUT_SHELTER_BUTTON_CAT:
                    informationCatShelter(chatId);
                    break;
                case ABOUT_SHELTER_BUTTON_DOG:
                    informationDogShelter(chatId);
                    break;
                case SHELTER_SECOND_STEP_BUTTON_CAT:
                    takeAnCat(chatId);
                    break;
                case SHELTER_SECOND_STEP_BUTTON_DOG:
                    takeAnDog(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON1_CAT:
                    recommendationsHomeCat(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON1_DOG:
                    recommendationsHomeDog(chatId);
                    break;
                case SHELTER_SCHEDULE_BUTTON_CAT:
                    catShelterWork(chatId);
                    break;
                case TIPS_DOG_HANDLER_AND_WHY_THEY_MAY_REFUSE_TAKE_ANIMAL:
                    tipsFromDog(chatId);
                    break;
                case SHELTER_SCHEDULE_BUTTON_DOG:
                    dogShelterWork(chatId);
                    break;
                case SECURITY_CONTACTS_BUTTON_CAT:
                    catShelterSecurityContacts(chatId);
                    break;
                case SECURITY_CONTACTS_BUTTON_DOG:
                    dogShelterSecurityContacts(chatId);
                    break;
                case RULES_FOR_GETTING_KNOW_CAT:
                    safetyNotesRulesForFirstMetCat(chatId);
                    break;
                case RULES_FOR_GETTING_KNOW_DOG:
                    safetyNotesRulesForFirstMetDog(chatId);
                    break;
                case LIST_DOCUMENTS_TAKE_ANIMAL_DOG:
                    listOfDocumentsForAdoption(chatId);
                    break;
                case LIST_DOCUMENTS_TAKE_ANIMAL_CAT:
                    listOfDocumentsForAdoption(chatId);
                    break;
                case RECOMMENDATIONS_TRANSPORTATION_CAT:
                    transportingRecommendationsCat(chatId);
                    break;
                case RECOMMENDATIONS_TRANSPORTATION_DOG:
                    transportingRecommendationsDog(chatId);
                    break;
                case RECOMMENDATIONS_HOME_KITTY:
                    arrangingHomeRecommendationsKitty(chatId);
                    break;
                case RECOMMENDATIONS_HOME_PUPPY:
                    arrangingHomeRecommendationsPuppy(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON2_CAT:
                    arrangingHomeRecommendationsCat(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON2_DOG:
                    arrangingHomeRecommendationsDog(chatId);
                    break;
                case RECOMMENDATIONS_HOME_CAT_WITH_DISABILITIES:
                    arrangingHomeRecommendationsDisabledCat(chatId);
                    break;
                case RECOMMENDATIONS_HOME_DOG_WITH_DISABILITIES:
                    arrangingHomeRecommendationsDisabledDog(chatId);
                    break;
                case SAFETY_NOTES_BUTTON_CAT:
                    safetyNotesCat(chatId);
                    break;
                case SAFETY_NOTES_BUTTON_DOG:
                    safetyNotesDog(chatId);
                    break;
                case TIPS_DOG_HANDLER_COMMUNICATE_WITH_DOG:
                    initialDogHandlerAdvice(chatId);
                    break;
                case RECOMMENDATIONS_FURTHER_REFERENCE_THEM:
                    dogHandlerRecommendation(chatId);
                    break;
                case LIST_OF_REASONS_WHY_THEY_MAY_REFUSE_DOG:
                    refusalReasonsList(chatId);
                    break;
                case SHELTER_THIRD_STEP_BUTTON_CAT:
                    shelterThirdCat(chatId, name);
                    break;
                case SHELTER_THIRD_STEP_BUTTON_DOG:
                    shelterThirdDog(chatId, name);
                    break;
                case CALL_VOLUNTEER_BUTTON:
                    callAVolunteer(chatId, update.getMessage().getChat().getUserName());
                    break;
                case SAVE_ADMIN: //показывает CHAT_ID в логи консоли (никуда не сохраняет данные)
                    showAdminChatId(update);
                    break;
                default:
                    prepareAndSendMessage(chatId, "Я пока не знаю как на это ответить!");
            }
        }
    }

    //    НАЧАЛО БЛОКА КЛАВИАТУР---------------------------------------------------------------
    private void mainMenu(long chatId, String name) { //метод отправки главного меню
        String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT, name);
        prepareAndSendMessageAndKeyboard(chatId, answer, keyboard.startKeyboard());
    }

    private void dog(long chatId) {//метод для перехода в собачий приют, с клавиатурой
        prepareAndSendMessageAndKeyboard(chatId, DOG_SHELTER_SELECT_TEXT, keyboard.dogShelterKeyboard());
        saveSelection(chatId, true, 0);                      //сохранили клиента в БД Selection с выбором собак
    }

    private void cat(long chatId) {//метод для перехода в кошачий приют, с клавиатурой
        prepareAndSendMessageAndKeyboard(chatId, CAT_SHELTER_SELECT_TEXT, keyboard.catShelterKeyboard());
        saveSelection(chatId, false, 0);                      //сохранили клиента в БД Selection с выбором кошек
    }

    private void informationCatShelter(long chatId) {//метод для перехода в информацию о кошачьем приюте, с клавиатурой
        prepareAndSendMessageAndKeyboard(chatId, ABOUT_CAT_SHELTER_TEXT, keyboard.informationCatShelterKeyboard());
    }

    private void informationDogShelter(long chatId) {//метод для перехода в информацию о собачьем приюте, с клавиатурой
        prepareAndSendMessageAndKeyboard(chatId, ABOUT_DOG_SHELTER_TEXT, keyboard.informationDogShelterKeyboard());
    }

    private void takeAnCat(long chatId) { //переход в меню как взять кошку из приюта
        prepareAndSendMessageAndKeyboard(chatId, CAT_TAKING_ANIMAL_FROM_SHELTER, keyboard.takeAnCatShelterKeyboard());
    }

    private void takeAnDog(long chatId) { //переход в меню как взять собаку из приюта
        prepareAndSendMessageAndKeyboard(chatId, DOG_TAKING_ANIMAL_FROM_SHELTER, keyboard.takeAnDogShelterKeyboard());
    }

    private void recommendationsHomeDog(long chatId) { //переход в меню обустройство дома для собаки
        prepareAndSendMessageAndKeyboard(chatId, ARRANGING_HOME_RECOMMENDATIONS, keyboard.recommendationsHomeDogKeyboard());
    }

    private void recommendationsHomeCat(long chatId) { //переход в меню обустройство дома кошки
        prepareAndSendMessageAndKeyboard(chatId, ARRANGING_HOME_RECOMMENDATIONS, keyboard.recommendationsHomeCatKeyboard());
    }

    private void tipsFromDog(long chatId) { //переход в меню советы кинолога и почему могут отказать забрать собаку из приюта
        prepareAndSendMessageAndKeyboard(chatId, TIPS_DOG_HANDLER, keyboard.tipsFromDogKeyboard());
    }

    private void catShelterWork(long chatId) {
        prepareAndSendMessage(chatId, CAT_SHELTER_WORK_SCHEDULE);
    }

    private void dogShelterWork(long chatId) {
        prepareAndSendMessage(chatId, DOG_SHELTER_WORK_SCHEDULE);
    }

    private void catShelterSecurityContacts(long chatId) {
        prepareAndSendMessage(chatId, CAT_SHELTER_SECURITY_CONTACTS);
    }

    private void dogShelterSecurityContacts(long chatId) {
        prepareAndSendMessage(chatId, DOG_SHELTER_SECURITY_CONTACTS);
    }

    private void safetyNotesDog(long chatId) {
        prepareAndSendMessage(chatId, SAFETY_NOTES_DOG);
    }

    private void safetyNotesCat(long chatId) {
        prepareAndSendMessage(chatId, SAFETY_NOTES_CAT);
    }

    private void safetyNotesRulesForFirstMetCat(long chatId) {
        prepareAndSendMessage(chatId, RULES_FOR_FIRST_MET_CAT);
    }

    private void safetyNotesRulesForFirstMetDog(long chatId) {
        prepareAndSendMessage(chatId, RULES_FOR_FIRST_MET_DOG);
    }

    private void listOfDocumentsForAdoption(long chatId) {
        prepareAndSendMessage(chatId, LIST_OF_DOCUMENTS_FOR_ADOPTION);
    }

    private void transportingRecommendationsCat(long chatId) {
        prepareAndSendMessage(chatId, TRANSPORTING_RECOMMENDATIONS_CAT);
    }

    private void transportingRecommendationsDog(long chatId) {
        prepareAndSendMessage(chatId, TRANSPORTING_RECOMMENDATIONS_DOG);
    }

    private void arrangingHomeRecommendationsKitty(long chatId) {
        prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_KITTY);
    }

    private void arrangingHomeRecommendationsPuppy(long chatId) {
        prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_PUPPY);
    }

    private void arrangingHomeRecommendationsCat(long chatId) {
        prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_CAT);
    }

    private void arrangingHomeRecommendationsDog(long chatId) {
        prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_DOG);
    }

    private void arrangingHomeRecommendationsDisabledCat(long chatId) {
        prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_DISABLED_CAT);
    }

    private void arrangingHomeRecommendationsDisabledDog(long chatId) {
        prepareAndSendMessage(chatId, ARRANGING_HOME_RECOMMENDATIONS_DISABLED_DOG);
    }

    private void initialDogHandlerAdvice(long chatId) {
        prepareAndSendMessage(chatId, INITIAL_DOG_HANDLER_ADVICE);
    }

    private void dogHandlerRecommendation(long chatId) {
        prepareAndSendMessage(chatId, DOG_HANDLER_RECOMMENDATION);
    }

    private void refusalReasonsList(long chatId) {
        prepareAndSendMessage(chatId, REFUSAL_REASONS_LIST);
    }
//  ОКОНЧАНИЕ БЛОКА КЛАВИАТУР --------------------------------------------------------------------------------------

    //  НАЧАЛО БЛОКА РАБОТЫ С ФАЙЛАМИ-----------------------------------------------------------------------------------
    private void shelterThirdCat(long chatId, String name) {
        Probation ownerProbation = catOwnersRepository.findById(chatId).get().getProbation();
        if (ownerProbation.equals(Probation.IN_PROGRESS)) {
            prepareAndSendMessage(chatId, SHELTER_THIRD_STEP_CAT);
            saveSelection(chatId, false, 1);
            log.info("Replied to user " + name);
        } else {
            prepareAndSendMessage(chatId, NO_NEED_TO_SEND_A_REPORT);
        }
    }

    private void photoShelterThirdCat(long chatId, String name) {
        prepareAndSendMessage(chatId, DIET_CAT);
        saveSelection(chatId, false, 2);
        log.info("Replied to user " + name);
    }

    private void dietShelterThirdCat(long chatId, String name) {
        prepareAndSendMessage(chatId, WELL_BEING_AND_ADAPTATION_CAT);
        saveSelection(chatId, false, 3);
        log.info("Replied to user " + name);
    }

    private void changesBehaviorShelterThirdCat(long chatId, String name) {
        prepareAndSendMessage(chatId, CHANGES_BEHAVIOR_CAT);
        saveSelection(chatId, false, 4);
        log.info("Replied to user " + name);
    }

    private void shelterThirdDog(long chatId, String name) {
        Probation ownerProbation = dogOwnersRepository.findById(chatId).get().getProbation();
        if (ownerProbation.equals(Probation.IN_PROGRESS)) {
            prepareAndSendMessage(chatId, SHELTER_THIRD_STEP_DOG);
            saveSelection(chatId, true, 1);
            log.info("Replied to user " + name);
        } else {
            prepareAndSendMessage(chatId, NO_NEED_TO_SEND_A_REPORT);
        }
    }

    private void photoShelterThirdDog(long chatId, String name) {
        prepareAndSendMessage(chatId, DIET_DOG);
        saveSelection(chatId, true, 2);
        log.info("Replied to user " + name);
    }

    private void dietShelterThirdDog(long chatId, String name) {
        prepareAndSendMessage(chatId, WELL_BEING_AND_ADAPTATION_DOG);
        saveSelection(chatId, true, 3);
        log.info("Replied to user " + name);
    }

    private void changesBehaviorShelterThirdDog(long chatId, String name) {
        prepareAndSendMessage(chatId, CHANGES_BEHAVIOR_DOG);
        saveSelection(chatId, true, 4);
        log.info("Replied to user " + name);
    }
//  ОКОНЧАНИЕ БЛОКА РАБОТЫ С ФАЙЛАМИ -------------------------------------------------------------------------

//  НАЧАЛО БЛОКА ОБЩИХ МЕТОДОВ ------------------------------------------------------------------------------

    /**
     * Метод обрабатывающий команду <b>/start</b>
     * <p>
     * Собирает текст ответа и отправляет его в метод: {@link TelegramBot#prepareAndSendMessageAndKeyboard(long, String, ReplyKeyboardMarkup)}
     *
     * @param chatId (ID чата пользователя)
     * @param name   (имя пользователя)
     */
    private void startCommand(long chatId, String name) {
        // добавление смайликов в строку (на сайте эмоджипедиа, либо можно зайти в телегу и навести на смайлик, он выдаст код)
        String answer = String.format(GREETING_PLUS_SELECT_SHELTER_TEXT_START, name);
        prepareAndSendMessageAndKeyboard(chatId, answer, keyboard.startKeyboard());                    // вызываем метод подготовки сообщения
        saveSelection(chatId, null, 0);
        log.info("Replied to user " + name);                     //лог о том что мы ответили пользователю
    }


    /**
     * Метод подготовки и отправки сообщения пользователю
     * <b><u>Вместе с клавиатурой!</u></b> <p>
     * Собирает сообщение вместе с клавиатурой и дергает метод отправки: {@link TelegramBot#executeMessage(SendMessage)}
     *
     * @param chatId         (ID чата пользователя)
     * @param textToSend     (текст для отправки пользователю)
     * @param keyboardMarkup (клавиатура)
     */
    private void prepareAndSendMessageAndKeyboard(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        executeMessage(message); //вызываем метод отправки сообщения
    }


    /**
     * Метод подготовки и отправки сообщения пользователю <p>
     * <b><u>Без клавиатуры!</u></b> <p>
     * Собирает сообщение и дергает метод отправки: {@link TelegramBot#executeMessage(SendMessage)}
     *
     * @param chatId     (ID чата пользователя)
     * @param textToSend (текст для отправки пользователю)
     */
    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); //!!! chatID на входе всегда Long, а на выходе всегда String
        message.setText(textToSend);
        executeMessage(message); //вызываем метод отправки сообщения
    }

    /**
     * Точечный метод отправки сообщения <p>
     * Главная задача метода: принять собранный message и отправить его клиенту
     *
     * @param message (заранее собранный message с chatID пользователя и текстом сообщения)
     *                {@link TelegramApiException} обрабатывается через try/catch внутри метода
     */
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }


    /**
     * Метод для вызова волонтера <p>
     * Суть метода: отправить волонтёру в личку ссылку на пользователя чтобы волонтёр законнектил чаты и начал общение)<p>
     * Метод дергает {@link #getBotOwnerId()}
     * Отправляет два сообщения: <p>
     * Одно волонтёру со ссылкой на чат клиента <p>
     * Второе клиенту, с уведомлением о том, что ему скоро напишут
     *
     * @param chatId   (chatID пользователя)
     * @param userName (никнейм пользователя)
     */
    private void callAVolunteer(long chatId, String userName) {
        SendMessage messageVolunteer = new SendMessage();
        SendMessage messageUser = new SendMessage();                    //создаёт два сообщения, одно волонтеру, другое пользователю

        messageVolunteer.setChatId(getBotOwnerId());
        messageVolunteer.setText(VOLUNTEER_MESSAGE + userName);         //формируем сообщение для волонтёра
        messageUser.setChatId(String.valueOf(chatId));
        messageUser.setText(VOLUNTEER_WILL_WRITE_TO_YOU);               //заполняю сообщение пользователю (чтобы он был вкурсе что его сообщение обработано)

        executeMessage(messageVolunteer);                               //отправляем сообщение контактными данными пользователя в личку волонтёру
        executeMessage(messageUser);
    }

    /**
     * Технический метод не для пользователей <p>
     * Метод выводит в лог консоли ChatId админа, если была написана команда "сохранить админа" <p>
     * После этого из лога можно сохранить ChatId в application.properties
     */
    private void showAdminChatId(Update update) {
        Long chatId = update.getMessage().getChatId();
        log.info("ADMIN CHAT_ID: " + chatId);
    }


    private void saveSelection(long chatId, Boolean selection, Integer counter) {
        Selection newSelection = new Selection();
        newSelection.setSelection(selection);
        newSelection.setChatId(chatId);
        newSelection.setCounter(counter);
        selectionRepository.save(newSelection);
    }
//  ОКОНЧАНИЕ БЛОКА ОБЩИХ МЕТОДОВ -------------------------------------------------------------------------------

//  НАЧАЛО БЛОКА Scheduled МЕТОДОВ ------------------------------------------------------------------------------

    //cron = ("0 0/1 * * * ?") - каждую минуту (для теста)
    //cron = "@daily" - в полночь (для работы)
    //подумать над оптимизацией кода (есть ли смысл и возможность наследования и использования полиморфизма, есть ли смысл сократить количество классов)
    //метод проверки испытательного срока
    @Scheduled(cron = "@daily")
    private void findProbation() {
        log.info("daily search for probation statuses has begun");

        List<CatOwners> catOwners = catOwnersRepository.findAll(); //собрали всех пользователей по двум БД
        List<DogOwners> dogOwners = dogOwnersRepository.findAll();

        catOwners.forEach(catOwner -> {                                 //прошлись во всем пользователям CAT
            Long chatId = catOwner.getChatId();
            switch (catOwner.getProbation()) {                           //проверили на совпадение статуса испытательного срока
                case FAILED:                                            //не прошел - уведомили, сменили статус на завершенный с провалом
                    prepareAndSendMessage(chatId, FAILED);
                    CatOwners owner1 = catOwnersRepository.findById(chatId).get(); //взяли готового клиента, сменили статус испытательного срока и пересохранили
                    owner1.setProbation(Probation.COMPLETED_FAILED);
                    catOwnersRepository.save(owner1);
                    break;
                case PASSED:                                            //прошел - уведомили, сменили статус на завершенный с успехом
                    prepareAndSendMessage(chatId, PROBATION_PASSED);
                    CatOwners owner2 = catOwnersRepository.findById(chatId).get();
                    owner2.setProbation(Probation.COMPLETED_SUCCESS);
                    catOwnersRepository.save(owner2);
                    break;
                case EXTENDED_14:                                       //уведомили о продлении и сменили статус на "в процессе)
                    prepareAndSendMessage(chatId, EXTENDED_14);
                    CatOwners owner3 = catOwnersRepository.findById(chatId).get();
                    owner3.setProbation(Probation.IN_PROGRESS);
                    catOwnersRepository.save(owner3);
                    //тут нужно поставить логику по добавлению +14 дней к полю времени испытательного срока
                    break;
                case EXTENDED_30:
                    prepareAndSendMessage(chatId, EXTENDED_30);
                    CatOwners owner4 = catOwnersRepository.findById(chatId).get();
                    owner4.setProbation(Probation.IN_PROGRESS);
                    catOwnersRepository.save(owner4);
                    catOwner.setProbation(Probation.IN_PROGRESS);       //тут нужно поставить логику по добавлению +30 дней к полю времени испытательного срока
                    break;
            }
        });

        dogOwners.forEach(dogOwner -> {
            Long chatId = dogOwner.getChatId();
            switch (dogOwner.getProbation()) {
                case FAILED:
                    prepareAndSendMessage(chatId, FAILED);
                    DogOwners owner1 = dogOwnersRepository.findById(chatId).get();
                    owner1.setProbation(Probation.COMPLETED_FAILED);
                    dogOwnersRepository.save(owner1);
                    break;
                case PASSED:
                    prepareAndSendMessage(chatId, PROBATION_PASSED);
                    DogOwners owner2 = dogOwnersRepository.findById(chatId).get();
                    owner2.setProbation(Probation.COMPLETED_SUCCESS);
                    dogOwnersRepository.save(owner2);
                    break;
                case EXTENDED_14:
                    prepareAndSendMessage(chatId, EXTENDED_14);
                    DogOwners owner3 = dogOwnersRepository.findById(chatId).get();
                    owner3.setProbation(Probation.IN_PROGRESS);
                    dogOwnersRepository.save(owner3);
                    break;
                case EXTENDED_30:
                    prepareAndSendMessage(chatId, EXTENDED_30);
                    DogOwners owner4 = dogOwnersRepository.findById(chatId).get();
                    owner4.setProbation(Probation.IN_PROGRESS);
                    dogOwnersRepository.save(owner4);
                    break;
            }
        });
    }

    //cron = ("0 0/1 * * * ?") - каждую минуту (для теста)
    //cron = ("0 55 23 * * ?") - за 5 минут до полуночи (для работы)
    //метод проверки отчёта на просрочку (1 день /2 дня)
    @Scheduled(cron = "0 55 23 * * ?")
    private void checkReport() {
        log.info("the report has been checked for any delays");

        List<CatOwners> catOwners = catOwnersRepository.findByProbation(Probation.IN_PROGRESS); //взяли всех пользователей у которых статус испытательного срока активный (это значит что они должны присылать отчет)
        List<DogOwners> dogOwners = dogOwnersRepository.findByProbation(Probation.IN_PROGRESS);
        LocalDate dateNow = LocalDate.now();                //сегодняшняя дата

        catOwners.forEach(catOwner -> {                      //взял всех пользователей кошек и пробежался по каждому
            List<LocalDate> dates = new ArrayList<>();      //создал для каждого массив для хранения дат репортов
            catOwner.getCatReports().forEach(catReport -> {  //пробежался по каждому значению сета репортов одного пользователя
                dates.add(catReport.getDate());             //сохранил все даты репортов в отдельный массив (созданный выше)
            });

            if (dates.contains(dateNow)) {                    //проверяю, есть ли в этих датах сегодняшняя дата (есть)
                CatOwners newCatOwner = catOwnersRepository.findById(catOwner.getChatId()).get(); //взяли нашего пользователя
                newCatOwner.setDaysOverdueReport(0);                                             //установили дни просрочки на ноль
                catOwnersRepository.save(newCatOwner);                                          //перезаписали пользователя с новым количеством дней просрочки (это нужно для того чтобы у пользователя было именно 2 дня просрочки ПОДРЯД, а не 2 за месяц например, ведь он же может реабилитироваться)
            } else if (!(dates.contains(dateNow)) && catOwner.getDaysOverdueReport() < 1) {   //если нет, и просрочки 0 дней то
                prepareAndSendMessage(catOwner.getChatId(), NOTICE_OF_LATE_REPORT);      //отправляем пользователю предупреждение
                CatOwners newCatOwner = catOwnersRepository.findById(catOwner.getChatId()).get();
                newCatOwner.setDaysOverdueReport(1);                                             //добавили 1 день просрочки
                catOwnersRepository.save(newCatOwner);
            } else if (!(dates.contains(dateNow)) && catOwner.getDaysOverdueReport() == 1) {  //если нет и просрочки уже есть 1 день то
                prepareAndSendMessage(Long.parseLong(config.getOwnerId()), NOTICE_OF_LATE_REPORT_FOR_ADMIN + "Чат ID: " + catOwner.getChatId() + " , Номер телефона : " + catOwner.getPhoneNumber()); //сообщение админу
                CatOwners newCatOwner = catOwnersRepository.findById(catOwner.getChatId()).get();
                newCatOwner.setDaysOverdueReport(2);                                             //добавили 1 день просрочки
                catOwnersRepository.save(newCatOwner);
            }
        });

        dogOwners.forEach(dogOwner -> {
            List<LocalDate> dates = new ArrayList<>();
            dogOwner.getDogReports().forEach(dogReport -> {
                dates.add(dogReport.getDate());
            });

            if (dates.contains(dateNow)) {
                DogOwners newDogOwner = dogOwnersRepository.findById(dogOwner.getChatId()).get();
                newDogOwner.setDaysOverdueReport(0);
                dogOwnersRepository.save(newDogOwner);
            } else if (!(dates.contains(dateNow)) && dogOwner.getDaysOverdueReport() < 1) {
                prepareAndSendMessage(dogOwner.getChatId(), NOTICE_OF_LATE_REPORT);
                DogOwners newDogOwner = dogOwnersRepository.findById(dogOwner.getChatId()).get();
                newDogOwner.setDaysOverdueReport(1);
                dogOwnersRepository.save(newDogOwner);
            } else if (!(dates.contains(dateNow)) && dogOwner.getDaysOverdueReport() == 1) {
                prepareAndSendMessage(Long.parseLong(config.getOwnerId()), NOTICE_OF_LATE_REPORT_FOR_ADMIN + "Чат ID: " + dogOwner.getChatId() + " , Номер телефона : " + dogOwner.getPhoneNumber());
                DogOwners newDogOwner = dogOwnersRepository.findById(dogOwner.getChatId()).get();
                newDogOwner.setDaysOverdueReport(2);
                dogOwnersRepository.save(newDogOwner);
            }
        });
    }
}


