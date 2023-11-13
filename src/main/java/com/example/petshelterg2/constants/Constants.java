package com.example.petshelterg2.constants;


import com.vdurmont.emoji.EmojiParser;

public class Constants {
    public static final String ERROR_TEXT = "Error occurred: ";
    // emoji
    public final static String BLUSH_EMOJI = "\uD83D\uDE0A";
    public final static String CAT_EMOJI = "\uD83D\uDE3A";
    public final static String DOG_EMOJI = "🐶";
    public final static String VOLUNTEER_EMOJI = "\uD83D\uDE4B";
    public final static String SCHEDULE_EMOJI = "⏰";
    public final static String GUARD_EMOJI = "\uD83D\uDC6E";
    public final static String SHELTER_EMOJI = "\uD83C\uDFEC";
    public final static String PHONE_EMOJI = "\uD83D\uDCDE";
    public final static String BACK_EMOJI = "⏩";

    // messages
    public final static String GREETING_PLUS_SELECT_SHELTER_TEXT_START = "Привет, %s, твой будущий питомец скучает по тебе!" + " " + BLUSH_EMOJI + " Выберите приют ";
    public final static String GREETING_PLUS_SELECT_SHELTER_TEXT = "Снова здравствуйте, %s, выберите приют, ваш ждет питомец!" + " " + BLUSH_EMOJI ;
    public final static String CAT_SHELTER_SELECT_TEXT = "Вы выбрали приют для кошек" + " " + CAT_EMOJI + " " + " чем мы можем помочь? ";
    public final static String DOG_SHELTER_SELECT_TEXT = "Вы выбрали приют для собак" + " " + DOG_EMOJI + " " + " чем мы можем помочь? ";
    public final static String ABOUT_CAT_SHELTER_TEXT = "Здесь будет храниться информация о приюте для кошек";
    public final static String ABOUT_DOG_SHELTER_TEXT = "Здесь будет храниться информация о приюте для собак";
    public final static String CAT_SHELTER_WORK_SCHEDULE = "Здесь хранится расписание работы приюта, адрес, схему проезда";
    public final static String DOG_SHELTER_WORK_SCHEDULE = "Здесь хранится расписание работы приюта, адрес, схему проезда";
    public final static String CAT_SHELTER_SECURITY_CONTACTS = "Контактные данные охраны для оформления пропуска на машину";
    public final static String DOG_SHELTER_SECURITY_CONTACTS = "Контактные данные охраны для оформления пропуска на машину";
    public final static String SAFETY_NOTES = "Общие рекомендации о технике безопасности на территории приюта";
    public final static String RULES_FOR_FIRST_MET = "Правила знакомства с животным до того, как забрать его из приюта";
    public final static String LIST_OF_DOCUMENTS_FOR_ADOPTION = "Список документов, необходимых для того, чтобы взять животное из приюта";
    public final static String TRANSPORTING_RECOMMENDATIONS = "Список рекомендаций по транспортировке животного";
    public final static String ARRANGING_HOME_RECOMMENDATIONS_YOUNG = "Список рекомендаций по обустройству дома для щенка/котенка";
    public final static String ARRANGING_HOME_RECOMMENDATIONS_ADULT = "Список рекомендаций по обустройству дома для взрослого животного";
    public final static String ARRANGING_HOME_RECOMMENDATIONS_DISABLED = "Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)";
    public final static String INITIAL_DOG_HANDLER_ADVICE = "Советы кинолога по первичному общению с собакой";
    public final static String DOG_HANDLER_RECOMMENDATION = "Рекомендации по проверенным кинологам для дальнейшего обращения к ним";
    public final static String REFUSAL_REASONS_LIST = "Список причин, почему могут отказать и не дать забрать собаку из приюта";
    public final static String VOLUNTEER_MESSAGE = "Пользователю требуется помощь! \n t.me/";
    public final static String SAVE_ADMIN = "Сохранить админа";
    //buttons
    public final static String MAIN_MAIN = "Вернуться в главное меню" + " " + BACK_EMOJI;
    public final static String CAT_SHELTER_BUTTON = "Приют для кошек" + " " + CAT_EMOJI;
    public final static String DOG_SHELTER_BUTTON = "Приют для собак" + " " + DOG_EMOJI;
    public final static String SHELTER_FIRST_STEP_BUTTON_DOG = "Узнать информацию о приюте";
    public final static String SHELTER_SECOND_STEP_BUTTON_DOG = "Как взять животное из приюта";
    public final static String SHELTER_THIRD_STEP_BUTTON_DOG = "Прислать отчет о питомце";
    public final static String SHELTER_FIRST_STEP_BUTTON_CAT = "Узнать информацию о приюте";
    public final static String SHELTER_SECOND_STEP_BUTTON_CAT = "Как взять животное из приюта";
    public final static String SHELTER_THIRD_STEP_BUTTON_CAT = "Прислать отчет о питомце";
    public final static String CALL_VOLUNTEER_BUTTON = "Позвать волонтера" + " " + VOLUNTEER_EMOJI;
    public final static String ABOUT_SHELTER_BUTTON = "Информация о приюте" + " " + SHELTER_EMOJI;
    public final static String SHELTER_SCHEDULE_BUTTON = "График работы" + " " + SCHEDULE_EMOJI;
    public final static String SECURITY_CONTACTS_BUTTON = "Контакты охраны" + " " + GUARD_EMOJI;
    public final static String SAFETY_NOTES_BUTTON = "Техника безопасности на территории приюта";
    public final static String CONTACT_WITH_ME_BUTTON = "Свяжитесь со мной" + " " + PHONE_EMOJI; // Бот может принять и записать контактные данные для связи.


}
