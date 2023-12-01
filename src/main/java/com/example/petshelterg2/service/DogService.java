package com.example.petshelterg2.service;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.model.DogOwners;
import com.example.petshelterg2.model.DogReport;
import com.example.petshelterg2.model.Probation;
import com.example.petshelterg2.repository.DogOwnersRepository;
import com.example.petshelterg2.repository.DogReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
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
public class DogService {
    private final BotConfig config;
    private final DogOwnersRepository dogOwnersRepository;
    private final DogReportRepository dogReportRepository;

    @Autowired
    public DogService(DogOwnersRepository dogOwnersRepository, DogReportRepository dogReportRepository, BotConfig config) {
        this.dogOwnersRepository = dogOwnersRepository;
        this.dogReportRepository = dogReportRepository;
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

    public DogOwners findOwnerById(long chatId){
        return dogOwnersRepository.findById(chatId).orElseThrow(()-> new NoSuchElementException("Поиск не дал результатов! Пользователь с chatId : "+ chatId +" отсутствует! Логика программы нарушена,потому что он должен там быть!"));
    }
    public List<DogOwners> findByProbation(Probation probation){
        return dogOwnersRepository.findByProbation(probation);
    }

    public List<DogOwners> findAll(){
        return dogOwnersRepository.findAll();
    }

    public void saveOwner(DogOwners owner){
        dogOwnersRepository.save(owner);
    }

    /**
     * Метод сохранения пользователя в БД (с собаками):<p>
     * {@link DogOwners}
     *
     * @param update
     */
    public void saveOwner(Update update) {
        Long chatId = update.getMessage().getChatId();
        String firstName = update.getMessage().getChat().getFirstName();
        String lastName = update.getMessage().getChat().getLastName();
        String userName = update.getMessage().getChat().getUserName();
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
        java.time.LocalDateTime currentDateTime = java.time.LocalDateTime.now();
        String status = "необходимо связаться";

        DogOwners dogOwners = new DogOwners();
        dogOwners.setUserName(userName);
        dogOwners.setChatId(chatId);
        dogOwners.setFirstName(firstName);
        dogOwners.setLastName(lastName);
        dogOwners.setPhoneNumber(phoneNumber);
        dogOwners.setDateTime(currentDateTime);
        dogOwners.setStatus(status);
        dogOwners.setProbation(Probation.NOT_ASSIGNED);              // указали поле "не назначен" чтобы там не было null
        dogOwnersRepository.save(dogOwners);
        log.info("contact saved " + dogOwners);
    }

    public void processPhoto(Message telegramMessage) {
        var photoSizeCount = telegramMessage.getPhoto().size();
        var photoIndex = photoSizeCount > 1 ? telegramMessage.getPhoto().size() - 1 : 0;
        var telegramPhoto = telegramMessage.getPhoto().get(photoIndex);
        var fileId = telegramPhoto.getFileId();
        var response = getFilePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            var filePath = getFilePath(response);
            var fileInByte = downloadFiles(filePath);
            DogReport dogReport = new DogReport();
            dogReport.setDogOwners(dogOwnersRepository.findById(telegramMessage.getChatId()).get());
            dogReport.setFileAsArrayOfBytes(fileInByte);
            dogReport.setDate(LocalDate.now());
            dogReportRepository.save(dogReport);
        } else {
            throw new RuntimeException(telegramPhoto.getFileId() + "Bad response from telegram service: " + response);
        }

    }

    private String getFilePath(ResponseEntity<String> response) {//достаем file_path
        var jsonObject = new JSONObject(response.getBody());
        return String.valueOf(jsonObject
                .getJSONObject("result")
                .getString("file_path"));
    }

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

    public void reportDiet(String diet, Long chatId) {
        DogReport dogReport = dogReportRepository.findFirstByDogOwnersAndDate(dogOwnersRepository.findById(chatId).get(), LocalDate.now());
        dogReport.setDiet(diet);
        dogReportRepository.save(dogReport);
    }

    public void reportWellBeingAndAdaptation(String wellBeingAndAdaptation, Long chatId) {
        DogReport dogReport = dogReportRepository.findFirstByDogOwnersAndDate(dogOwnersRepository.findById(chatId).get(), LocalDate.now());
        dogReport.setWellBeingAndAdaptation(wellBeingAndAdaptation);
        dogReportRepository.save(dogReport);
    }

    public void reportChangesBehavior(String wellBeingAndAdaptation, Long chatId) {
        DogReport dogReport = dogReportRepository.findFirstByDogOwnersAndDate(dogOwnersRepository.findById(chatId).get(), LocalDate.now());
        dogReport.setChangesBehavior(wellBeingAndAdaptation);
        dogReportRepository.save(dogReport);
    }
}
