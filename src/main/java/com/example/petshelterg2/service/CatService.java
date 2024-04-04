package com.example.petshelterg2.service;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.model.CatOwners;
import com.example.petshelterg2.model.CatReport;
import com.example.petshelterg2.model.Probation;
import com.example.petshelterg2.repository.CatOwnersRepository;
import com.example.petshelterg2.repository.CatReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CatService {
    private final BotConfig config;
    private final CatOwnersRepository catOwnersRepository;
    private final CatReportRepository catReportRepository;

    @Autowired
    public CatService(CatOwnersRepository catOwnersRepository, CatReportRepository catReportRepository, BotConfig config) {
        this.catOwnersRepository = catOwnersRepository;
        this.catReportRepository = catReportRepository;
        this.config = config;
    }

    public String getBotToken() {
        return config.getToken();
    }

    public String getFileStorageUri() {
        return config.getFileStorageUri();
    }

    public String getFileInfoUri() {
        return config.getFileInfoUri();
    }

    public CatOwners findOwnerById(long chatId){
        return catOwnersRepository.findById(chatId).orElseThrow(()-> new NoSuchElementException("Поиск не дал результатов! Пользователь с chatId : "+ chatId +" отсутствует! Логика программы нарушена,потому что он должен там быть!"));
    }
    public List<CatOwners> findByProbation(Probation probation){
        return catOwnersRepository.findByProbation(probation);
    }

    public List<CatOwners> findAll(){
        return catOwnersRepository.findAll();
    }

    public void saveOwner(CatOwners owner){
        catOwnersRepository.save(owner);
    }

    /**
     * Метод сохранения пользователя в БД (с кошками):<p>
     * {@link CatOwners}
     *
     */
    public void saveOwner(Update update) {
        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getChat().getFirstName();
        String lastName = update.getMessage().getChat().getLastName();
        String userName = update.getMessage().getChat().getUserName();
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
        java.time.LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        String status = "необходимо связаться";

        CatOwners catOwners = new CatOwners();
        catOwners.setUserName(userName);
        catOwners.setChatId(chatId);
        catOwners.setFirstName(firstName);
        catOwners.setLastName(lastName);
        catOwners.setPhoneNumber(phoneNumber);
        catOwners.setDateTime(currentDateTime);
        catOwners.setStatus(status);
        catOwners.setProbation(Probation.NOT_ASSIGNED);          // указали поле "не назначен" чтобы там не было null
        catOwnersRepository.save(catOwners);
        log.info("contact saved " + catOwners);
    }
    /**
     * Метод сохранения фото животного в БД отчета (с кошками):<p>
     * {@link CatReport}
     *
     * @param telegramMessage (сообщение отправленное пользователем)
     */
    public void processPhoto(Message telegramMessage) {
        var photoSizeCount = telegramMessage.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMessage.getPhoto().size() - 1 : 0;
        var telegramPhoto = telegramMessage.getPhoto().get(photoIndex);
        var fileId = telegramPhoto.getFileId();//
        var response = getFilePath(fileId);//Запрос HTTP
        if (response.getStatusCode() == HttpStatus.OK) {
            var filePath = getFilePath(response);//Преобразуем файл в JSON
            var fileInByte = downloadFiles(filePath);//Достаем данные из JSON, а именно массив байт
            CatReport catReport = new CatReport();
            catReport.setCatOwners(findOwnerById(telegramMessage.getChatId()));
            catReport.setFileAsArrayOfBytes(fileInByte);
            catReport.setDate(LocalDate.now());
            catReportRepository.save(catReport);
            log.info("photo saved " + telegramMessage.getChatId());
        } else {
            throw new RuntimeException(telegramPhoto.getFileId() + "Bad response from telegram service: " + response);
        }
    }
    /**
     * Метод преобразования в JSON:<p>
     *
     * @param response (ответ полученный от пользователя)
     */
    private String getFilePath(ResponseEntity<String> response) {//достаем file_path
        var jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }
    /**
     * Метод конвертирует файл в формате JSON в массив byte[]:<p>
     */
    private byte[] downloadFiles(String filePath) {
        var fullUri = getFileStorageUri().replace("{bot.token}", getBotToken())
                .replace("{filePath}", filePath);
        URL urlObj = null;
        try {
            urlObj = new URL(fullUri);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try (InputStream is = urlObj.openStream()) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(urlObj.toExternalForm(), e);
        }
    }
    /**
     * Метод для получения HTTP запроса:<p>
     */
    private ResponseEntity<String> getFilePath(String fileId) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        var request = new HttpEntity<>(headers);

        return restTemplate.exchange(//Передаются данные для запроса
                getFileInfoUri(),
                HttpMethod.GET,
                request,
                String.class,
                getBotToken(), fileId
        );
    }
    /**
     * Метод для сохранения информации о рационе животного:<p>
     * {@link CatReport}
     *
     * @param diet   (сообщение от пользователя)
     * @param chatId (Id чата пользователя)
     */
    public void reportDiet(String diet, Long chatId) {
        CatReport catReport = catReportRepository.findFirstByCatOwnersAndDate(catOwnersRepository.findById(chatId).get(), LocalDate.now());
        catReport.setDiet(diet);
        catReportRepository.save(catReport);
        log.info("saved info about diet " + chatId);
    }
    /**
     * Метод для сохранения информации об общем самочувствии животного:<p>
     * {@link CatReport}
     *
     * @param wellBeingAndAdaptation (сообщение от пользователя)
     * @param chatId                 (Id чата пользователя)
     */
    public void reportWellBeingAndAdaptation(String wellBeingAndAdaptation, Long chatId) {
        CatReport catReport = catReportRepository.findFirstByCatOwnersAndDate(catOwnersRepository.findById(chatId).get(), LocalDate.now());
        catReport.setWellBeingAndAdaptation(wellBeingAndAdaptation);
        catReportRepository.save(catReport);
        log.info("saved info about wellBeingAndAdaptation " + chatId);
    }
    /**
     * Метод для сохранения информации об изменении в поведении животного:<p>
     * {@link CatReport}
     *
     * @param changesBehavior (сообщение от пользователя)
     * @param chatId          (Id чата пользователя)
     */
    public void reportChangesBehavior(String changesBehavior, Long chatId) {
        CatReport catReport = catReportRepository.findFirstByCatOwnersAndDate(catOwnersRepository.findById(chatId).get(), LocalDate.now());
        catReport.setChangesBehavior(changesBehavior);
        catReportRepository.save(catReport);
        log.info("saved info about changesBehavior " + chatId);
    }
}
