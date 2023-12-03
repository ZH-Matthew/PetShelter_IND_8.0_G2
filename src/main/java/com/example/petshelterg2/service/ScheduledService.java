package com.example.petshelterg2.service;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.model.CatOwners;
import com.example.petshelterg2.model.DogOwners;
import com.example.petshelterg2.model.Probation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.petshelterg2.constants.Constants.*;
import static com.example.petshelterg2.constants.Constants.NOTICE_OF_LATE_REPORT_FOR_ADMIN;

@Slf4j
@Component
public class ScheduledService {
    BotConfig config;
    SendMessageService sendMService;
    CatService catService;
    DogService dogService;

    @Autowired
    public ScheduledService(SendMessageService sendMService, CatService catService, DogService dogService, BotConfig config) {
        this.sendMService = sendMService;
        this.catService = catService;
        this.dogService = dogService;
        this.config = config;
    }

    //cron = ("0 0/1 * * * ?") - каждую минуту (для теста)
    //cron = "@daily" - в полночь (для работы)
    //подумать над оптимизацией кода (есть ли смысл и возможность наследования и использования полиморфизма, есть ли смысл сократить количество классов)
    //метод проверки испытательного срока
    @Scheduled(cron = "@daily")
    private void findProbation() {
        log.info("daily search for probation statuses has begun");

        List<CatOwners> catOwners = catService.findAll(); //собрали всех пользователей по двум БД
        List<DogOwners> dogOwners = dogService.findAll();

        catOwners.forEach(catOwner -> {                                 //прошлись во всем пользователям CAT
            Long chatId = catOwner.getChatId();
            switch (catOwner.getProbation()) {                           //проверили на совпадение статуса испытательного срока
                case FAILED:                                            //не прошел - уведомили, сменили статус на завершенный с провалом
                    sendMService.prepareAndSendMessage(chatId, FAILED);
                    CatOwners owner1 = catService.findOwnerById(chatId); //взяли готового клиента, сменили статус испытательного срока и пересохранили
                    owner1.setProbation(Probation.COMPLETED_FAILED);
                    catService.saveOwner(owner1);
                    break;
                case PASSED:                                            //прошел - уведомили, сменили статус на завершенный с успехом
                    sendMService.prepareAndSendMessage(chatId, PROBATION_PASSED);
                    CatOwners owner2 = catService.findOwnerById(chatId);
                    owner2.setProbation(Probation.COMPLETED_SUCCESS);
                    catService.saveOwner(owner2);
                    break;
                case EXTENDED_14:                                       //уведомили о продлении и сменили статус на "в процессе)
                    sendMService.prepareAndSendMessage(chatId, EXTENDED_14);
                    CatOwners owner3 = catService.findOwnerById(chatId);
                    owner3.setProbation(Probation.IN_PROGRESS);
                    catService.saveOwner(owner3);
                    //тут нужно поставить логику по добавлению +14 дней к полю времени испытательного срока
                    break;
                case EXTENDED_30:
                    sendMService.prepareAndSendMessage(chatId, EXTENDED_30);
                    CatOwners owner4 = catService.findOwnerById(chatId);
                    owner4.setProbation(Probation.IN_PROGRESS);
                    catService.saveOwner(owner4); //тут нужно поставить логику по добавлению +30 дней к полю времени испытательного срока
                    break;
            }
        });

        dogOwners.forEach(dogOwner -> {
            Long chatId = dogOwner.getChatId();
            switch (dogOwner.getProbation()) {
                case FAILED:
                    sendMService.prepareAndSendMessage(chatId, FAILED);
                    DogOwners owner1 = dogService.findOwnerById(chatId);
                    owner1.setProbation(Probation.COMPLETED_FAILED);
                    dogService.saveOwner(owner1);
                    break;
                case PASSED:
                    sendMService.prepareAndSendMessage(chatId, PROBATION_PASSED);
                    DogOwners owner2 = dogService.findOwnerById(chatId);
                    owner2.setProbation(Probation.COMPLETED_SUCCESS);
                    dogService.saveOwner(owner2);
                    break;
                case EXTENDED_14:
                    sendMService.prepareAndSendMessage(chatId, EXTENDED_14);
                    DogOwners owner3 = dogService.findOwnerById(chatId);
                    owner3.setProbation(Probation.IN_PROGRESS);
                    dogService.saveOwner(owner3);
                    break;
                case EXTENDED_30:
                    sendMService.prepareAndSendMessage(chatId, EXTENDED_30);
                    DogOwners owner4 = dogService.findOwnerById(chatId);
                    owner4.setProbation(Probation.IN_PROGRESS);
                    dogService.saveOwner(owner4);
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

        List<CatOwners> catOwners = catService.findByProbation(Probation.IN_PROGRESS); //взяли всех пользователей у которых статус испытательного срока активный (это значит что они должны присылать отчет)
        List<DogOwners> dogOwners = dogService.findByProbation(Probation.IN_PROGRESS);
        LocalDate dateNow = LocalDate.now();                //сегодняшняя дата

        catOwners.forEach(catOwner -> {                      //взял всех пользователей кошек и пробежался по каждому
            List<LocalDate> dates = new ArrayList<>();      //создал для каждого массив для хранения дат репортов
            catOwner.getCatReports().forEach(catReport -> {  //пробежался по каждому значению сета репортов одного пользователя
                dates.add(catReport.getDate());             //сохранил все даты репортов в отдельный массив (созданный выше)
            });

            if (dates.contains(dateNow)) {                    //проверяю, есть ли в этих датах сегодняшняя дата (есть)
                CatOwners newCatOwners = catService.findOwnerById(catOwner.getChatId()); //взяли нашего пользователя
                newCatOwners.setDaysOverdueReport(0);                                             //установили дни просрочки на ноль
                catService.saveOwner(newCatOwners);                                          //перезаписали пользователя с новым количеством дней просрочки (это нужно для того чтобы у пользователя было именно 2 дня просрочки ПОДРЯД, а не 2 за месяц например, ведь он же может реабилитироваться)
            } else if (!(dates.contains(dateNow)) && catOwner.getDaysOverdueReport() < 1) {   //если нет, и просрочки 0 дней то
                sendMService.prepareAndSendMessage(catOwner.getChatId(), NOTICE_OF_LATE_REPORT);      //отправляем пользователю предупреждение
                CatOwners newCatOwners = catService.findOwnerById(catOwner.getChatId());
                newCatOwners.setDaysOverdueReport(1);                                             //добавили 1 день просрочки
                catService.saveOwner(newCatOwners);
            } else if (!(dates.contains(dateNow)) && catOwner.getDaysOverdueReport() == 1) {  //если нет и просрочки уже есть 1 день то
                sendMService.prepareAndSendMessage(Long.parseLong(config.getOwnerId()), NOTICE_OF_LATE_REPORT_FOR_ADMIN + "Чат ID: " + catOwner.getChatId() + " , Номер телефона : " + catOwner.getPhoneNumber()); //сообщение админу
                CatOwners newCatOwners = catService.findOwnerById(catOwner.getChatId());
                newCatOwners.setDaysOverdueReport(2);                                             //добавили 1 день просрочки
                catService.saveOwner(newCatOwners);
            }
        });

        dogOwners.forEach(dogOwner -> {
            List<LocalDate> dates = new ArrayList<>();
            dogOwner.getDogReports().forEach(dogReport -> dates.add(dogReport.getDate()));

            if (dates.contains(dateNow)) {
                DogOwners newDogOwners = dogService.findOwnerById(dogOwner.getChatId());
                newDogOwners.setDaysOverdueReport(0);
                dogService.saveOwner(newDogOwners);
            } else if (!(dates.contains(dateNow)) && dogOwner.getDaysOverdueReport() < 1) {
                sendMService.prepareAndSendMessage(dogOwner.getChatId(), NOTICE_OF_LATE_REPORT);
                DogOwners newDogOwners = dogService.findOwnerById(dogOwner.getChatId());
                newDogOwners.setDaysOverdueReport(1);
                dogService.saveOwner(newDogOwners);
            } else if (!(dates.contains(dateNow)) && dogOwner.getDaysOverdueReport() == 1) {
                sendMService.prepareAndSendMessage(Long.parseLong(config.getOwnerId()), NOTICE_OF_LATE_REPORT_FOR_ADMIN + "Чат ID: " + dogOwner.getChatId() + " , Номер телефона : " + dogOwner.getPhoneNumber());
                DogOwners newDogOwners = dogService.findOwnerById(dogOwner.getChatId());
                newDogOwners.setDaysOverdueReport(2);
                dogService.saveOwner(newDogOwners);
            }
        });
    }
}
