package com.example.petshelterg2.constants;

/**
 * Хранит все письменные данные и emoji в виде публичных констант
 */
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
    public final static String HOME_EMOJI = "\uD83C\uDFDA";
    public final static String SAVE_EMOJI = "\uD83D\uDCBE";

    // messages
    public final static String GREETING_PLUS_SELECT_SHELTER_TEXT_START =
            "Привет!" + BLUSH_EMOJI +" \n "+
            "\n " +
            "Я бот приюта для домашних животных! \n " +
            "\n " +
            "Моя цель - внести в твой мир хаос, " +
            "суету и бессонные ночи, а точнее - " +
            "помочь тебе забрать мохнатого из приюта! \n"+
            "\n " +
            "Я с радостью предоставлю информацию о приюте, "+
            "дам советы по уходу за мохнатым, "+
            "расскажу как забрать ушастого из приюта, "+
            "а также помогу с ежедневными отчётами \n"+
            "\n " +
            " Для начала выбери приют: ";
    public final static String GREETING_PLUS_SELECT_SHELTER_TEXT = "Снова здравствуйте, %s, выберите приют, ваc ждет питомец!" + " " + BLUSH_EMOJI;
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
    public final static String VOLUNTEER_WILL_WRITE_TO_YOU = "Волонтёр получил уведомление и скоро вам напишет!";
    public final static String DATA_SAVED = "Данные сохранены" + " " + SAVE_EMOJI;
    //buttons
    public final static String MAIN_MAIN = "Вернуться в главное меню" + " " + BACK_EMOJI;
    public final static String CAT_SHELTER_BUTTON = "Приют для кошек" + " " + CAT_EMOJI;
    public final static String DOG_SHELTER_BUTTON = "Приют для собак" + " " + DOG_EMOJI;
    public final static String SHELTER_SECOND_STEP_BUTTON_DOG = "Как взять животное из приюта" + DOG_EMOJI;
    public final static String SHELTER_THIRD_STEP_BUTTON_DOG = "Прислать отчет о питомце" + DOG_EMOJI;
    public final static String SHELTER_SECOND_STEP_BUTTON_CAT = "Как взять животное из приюта" + CAT_EMOJI;
    public final static String SHELTER_THIRD_STEP_BUTTON_CAT = "Прислать отчет о питомце" + CAT_EMOJI;
    public final static String CALL_VOLUNTEER_BUTTON = "Позвать волонтера" + " " + VOLUNTEER_EMOJI;
    public final static String ABOUT_SHELTER_BUTTON_CAT = "Информация о приюте кошек" + " " + SHELTER_EMOJI;
    public final static String SHELTER_SCHEDULE_BUTTON_CAT = "График работы приюта кошек" + " " + SCHEDULE_EMOJI;
    public final static String SECURITY_CONTACTS_BUTTON_CAT = "Контакты охраны приюта кошек" + " " + GUARD_EMOJI;
    public final static String SAFETY_NOTES_BUTTON_CAT = "Техника безопасности на территории приюта кошек";
    public final static String ABOUT_SHELTER_BUTTON_DOG = "Информация о приюте собак" + " " + SHELTER_EMOJI;
    public final static String SHELTER_SCHEDULE_BUTTON_DOG = "График работы приюта собак" + " " + SCHEDULE_EMOJI;
    public final static String SECURITY_CONTACTS_BUTTON_DOG = "Контакты охраны приюта собак" + " " + GUARD_EMOJI;
    public final static String RULES_FOR_GETTING_KNOW_DOG = "Правила знакомства с " + " " + DOG_EMOJI;
    public final static String RULES_FOR_GETTING_KNOW_CAT = "Правила знакомства с " + " " + CAT_EMOJI;
    public final static String LIST_DOCUMENTS_TAKE_ANIMAL_DOG = "Список документов, чтобы взять" + " " + DOG_EMOJI;
    public final static String LIST_DOCUMENTS_TAKE_ANIMAL_CAT = "Список документов, чтобы взять" + " " + CAT_EMOJI;
    public final static String RECOMMENDATIONS_TRANSPORTATION_CAT = "Рекомендаций по транспортировке" + " " + CAT_EMOJI;
    public final static String RECOMMENDATIONS_TRANSPORTATION_DOG = "Рекомендаций по транспортировке" + " " + DOG_EMOJI;
    public final static String RECOMMENDATIONS_HOME_BUTTON1_CAT = "Pекомендации по обустройству дома" + " " + CAT_EMOJI;
    public final static String RECOMMENDATIONS_HOME_BUTTON1_DOG = "Рекомендации по обустройству дома" + " " + DOG_EMOJI;
    public final static String RECOMMENDATIONS_HOME_BUTTON2_CAT = "Pекомендации по обустройству " + HOME_EMOJI + " для котенка" + CAT_EMOJI;
    public final static String RECOMMENDATIONS_HOME_BUTTON2_DOG = "Рекомендации по обустройству " + HOME_EMOJI + " для щенка " + DOG_EMOJI;
    public final static String RECOMMENDATIONS_HOME_KITTY = "Pекомендации по обустройству " + HOME_EMOJI + " для взрослого" + CAT_EMOJI;
    public final static String RECOMMENDATIONS_HOME_PUPPY = "Рекомендации по обустройству " + HOME_EMOJI + " для взрослого " + DOG_EMOJI;
    public final static String RECOMMENDATIONS_HOME_CAT_WITH_DISABILITIES = "Pекомендации по обустройству " + HOME_EMOJI + " для " + CAT_EMOJI + " c ограниченными возможностями";
    public final static String RECOMMENDATIONS_HOME_DOG_WITH_DISABILITIES = "Рекомендации по обустройству " + HOME_EMOJI + " для " + DOG_EMOJI + " c ограниченными возможностями";
    public final static String TIPS_DOG_HANDLER_AND_WHY_THEY_MAY_REFUSE_TAKE_ANIMAL = "Советы кинолога и почему вам могут отказать " + DOG_EMOJI;
    public final static String TIPS_DOG_HANDLER_COMMUNICATE_WITH_DOG = "Советы кинолога по первичному общению с собакой ";
    public final static String RECOMMENDATIONS_FURTHER_REFERENCE_THEM = "Рекомендации по проверенным кинологам для дальнейшего обращения к ним ";
    public final static String LIST_OF_REASONS_WHY_THEY_MAY_REFUSE_DOG = "Список причин, почему могут отказать и не дать забрать собаку из приюта ";
    public final static String SAFETY_NOTES_BUTTON_DOG = "Техника безопасности на территории приюта собак";
    public final static String CONTACT_WITH_ME_BUTTON = "Оставить контактные данные" + " " + PHONE_EMOJI; // Бот может принять и записать контактные данные для связи.


}
