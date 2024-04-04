package com.example.petshelterg2.controller;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.example.petshelterg2.constants.Constants.*;

@Slf4j //из библиотеки lombok реализует логирование через переменную log.
@Component //аннотация позволяет автоматически создать экземпляр
public class TelegramBot extends TelegramLongPollingBot {  //есть еще класс WebHookBot (разница в том что WebHook уведомляет нас каждый раз при написании сообщения пользователе, LongPolling сам проверяет не написали ли ему (он более простой)

    private final BotConfig config;
    private final GeneralService gService;
    private final Keyboard keyboard;

    @Autowired
    public TelegramBot(BotConfig config, GeneralService gService, Keyboard keyboard) {
        this.config = config;
        this.gService = gService;
        this.keyboard = keyboard;
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
            gService.startCommand(chatId, name);
        }
        //проверка наличия телефона (если он есть проверка на то какую сторону выбрал пользователь, и далее сохранение в БД
        if (update.getMessage().getContact() != null) {         //проверяет есть ли у пользователя контакт, если есть, сохраняет его.
            gService.saveContact(chatId, update);
        }                                                       //если пользователь выслал фото, оно сохраняется в репорт и запускается процесс очередности репорта
        if (update.getMessage().getPhoto() != null) {
            gService.processPhoto(chatId, update);
        }
        if (gService.getSelectionCounter(chatId) != 0) {
            gService.processReport(chatId, name, update);
        }

        if (update.hasMessage() && update.getMessage().hasText() && gService.getSelectionCounter(chatId) == 0) { //проверяем что сообщение пришло и там есть текст
            String messageText = update.getMessage().getText();

            if (messageText.contains("/send") && config.getOwnerId().equals(Long.toString(chatId))) {       //условие для отправки сообщение от админа (формат /send 16728471824)(может быть расширено для большего количества сообщений админа, для этого нужно вынести проверку на /send в отдельное вложенное условие)
                gService.sendAdminMessage(messageText);
                return;
            }

            switch (messageText) {
                case "/start":
                    break;
                case MAIN_MAIN:
                    keyboard.mainMenu(chatId, name);
                    break;
                case CAT_SHELTER_BUTTON:
                    keyboard.cat(chatId);
                    break;
                case DOG_SHELTER_BUTTON:
                    keyboard.dog(chatId);
                    break;
                case ABOUT_SHELTER_BUTTON_CAT:
                    keyboard.informationCatShelter(chatId);
                    break;
                case ABOUT_SHELTER_BUTTON_DOG:
                    keyboard.informationDogShelter(chatId);
                    break;
                case SHELTER_SECOND_STEP_BUTTON_CAT:
                    keyboard.takeAnCat(chatId);
                    break;
                case SHELTER_SECOND_STEP_BUTTON_DOG:
                    keyboard.takeAnDog(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON1_CAT:
                    keyboard.recommendationsHomeCat(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON1_DOG:
                    keyboard.recommendationsHomeDog(chatId);
                    break;
                case SHELTER_SCHEDULE_BUTTON_CAT:
                    keyboard.catShelterWork(chatId);
                    break;
                case TIPS_DOG_HANDLER_AND_WHY_THEY_MAY_REFUSE_TAKE_ANIMAL:
                    keyboard.tipsFromDog(chatId);
                    break;
                case SHELTER_SCHEDULE_BUTTON_DOG:
                    keyboard.dogShelterWork(chatId);
                    break;
                case SECURITY_CONTACTS_BUTTON_CAT:
                    keyboard.catShelterSecurityContacts(chatId);
                    break;
                case SECURITY_CONTACTS_BUTTON_DOG:
                    keyboard.dogShelterSecurityContacts(chatId);
                    break;
                case RULES_FOR_GETTING_KNOW_CAT:
                    keyboard.safetyNotesRulesForFirstMetCat(chatId);
                    break;
                case RULES_FOR_GETTING_KNOW_DOG:
                    keyboard.safetyNotesRulesForFirstMetDog(chatId);
                    break;
                case LIST_DOCUMENTS_TAKE_ANIMAL_DOG:
                case LIST_DOCUMENTS_TAKE_ANIMAL_CAT:
                    keyboard.listOfDocumentsForAdoption(chatId);
                    break;
                case RECOMMENDATIONS_TRANSPORTATION_CAT:
                    keyboard.transportingRecommendationsCat(chatId);
                    break;
                case RECOMMENDATIONS_TRANSPORTATION_DOG:
                    keyboard.transportingRecommendationsDog(chatId);
                    break;
                case RECOMMENDATIONS_HOME_KITTY:
                    keyboard.arrangingHomeRecommendationsKitty(chatId);
                    break;
                case RECOMMENDATIONS_HOME_PUPPY:
                    keyboard.arrangingHomeRecommendationsPuppy(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON2_CAT:
                    keyboard.arrangingHomeRecommendationsCat(chatId);
                    break;
                case RECOMMENDATIONS_HOME_BUTTON2_DOG:
                    keyboard.arrangingHomeRecommendationsDog(chatId);
                    break;
                case RECOMMENDATIONS_HOME_CAT_WITH_DISABILITIES:
                    keyboard.arrangingHomeRecommendationsDisabledCat(chatId);
                    break;
                case RECOMMENDATIONS_HOME_DOG_WITH_DISABILITIES:
                    keyboard.arrangingHomeRecommendationsDisabledDog(chatId);
                    break;
                case SAFETY_NOTES_BUTTON_CAT:
                    keyboard.safetyNotesCat(chatId);
                    break;
                case SAFETY_NOTES_BUTTON_DOG:
                    keyboard.safetyNotesDog(chatId);
                    break;
                case TIPS_DOG_HANDLER_COMMUNICATE_WITH_DOG:
                    keyboard.initialDogHandlerAdvice(chatId);
                    break;
                case RECOMMENDATIONS_FURTHER_REFERENCE_THEM:
                    keyboard.dogHandlerRecommendation(chatId);
                    break;
                case LIST_OF_REASONS_WHY_THEY_MAY_REFUSE_DOG:
                    keyboard.refusalReasonsList(chatId);
                    break;
                case SHELTER_THIRD_STEP_BUTTON_CAT:
                    gService.shelterThirdCat(chatId);
                    break;
                case SHELTER_THIRD_STEP_BUTTON_DOG:
                    gService.shelterThirdDog(chatId);
                    break;
                case CALL_VOLUNTEER_BUTTON:
                    gService.callAVolunteer(chatId, update, getBotOwnerId());
                    break;
                case SAVE_ADMIN: //показывает CHAT_ID в логи консоли (никуда не сохраняет данные)
                    showAdminChatId(update);
                    break;
                default:
                    keyboard.defaultAnswer(chatId);
            }
        }
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
}


