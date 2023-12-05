package com.example.petshelterg2.service;

import com.example.petshelterg2.config.BotConfig;
import com.example.petshelterg2.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.petshelterg2.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledServiceTest {
    @Mock
    CatService catService;
    @Mock
    DogService dogService;
    @Mock
    BotConfig config;
    @Mock
    SendMessageService sendMService;

    @InjectMocks
    ScheduledService service;

    Long chatId = 123L;

    //для первого метода
    CatOwners catOwnersFAILED = new CatOwners();
    CatOwners catOwnersPASSED = new CatOwners();
    CatOwners catOwnersEXTENDED_14 = new CatOwners();
    CatOwners catOwnersEXTENDED_30 = new CatOwners();

    DogOwners dogOwnersFAILED = new DogOwners();
    DogOwners dogOwnersPASSED = new DogOwners();
    DogOwners dogOwnersEXTENDED_14 = new DogOwners();
    DogOwners dogOwnersEXTENDED_30 = new DogOwners();

    //для второго метода
    CatOwners catOwners = new CatOwners();
    DogOwners dogOwners = new DogOwners();
    CatReport catReport = new CatReport();
    DogReport dogReport = new DogReport();
    Set<CatReport> setCatR = new HashSet<>();
    Set<DogReport> setDogR = new HashSet<>();
    LocalDate dateNow = LocalDate.now();
    LocalDate dateNotNow = LocalDate.of(2023,11,23); //произвольный день отличный от сегодняшнего

    String adminIdString = "321";
    Long adminIdLong = 321L;

    String phoneNumber = "88005553535";

    {
        //метод findProbation
        catOwnersFAILED.setProbation(Probation.FAILED);
        catOwnersFAILED.setChatId(chatId);
        catOwnersPASSED.setProbation(Probation.PASSED);
        catOwnersPASSED.setChatId(chatId);
        catOwnersEXTENDED_14.setProbation(Probation.EXTENDED_14);
        catOwnersEXTENDED_14.setChatId(chatId);
        catOwnersEXTENDED_30.setProbation(Probation.EXTENDED_30);
        catOwnersEXTENDED_30.setChatId(chatId);

        dogOwnersFAILED.setProbation(Probation.FAILED);
        dogOwnersFAILED.setChatId(chatId);
        dogOwnersPASSED.setProbation(Probation.PASSED);
        dogOwnersPASSED.setChatId(chatId);
        dogOwnersEXTENDED_14.setProbation(Probation.EXTENDED_14);
        dogOwnersEXTENDED_14.setChatId(chatId);
        dogOwnersEXTENDED_30.setProbation(Probation.EXTENDED_30);
        dogOwnersEXTENDED_30.setChatId(chatId);

        //2 метод
        catOwners.setProbation(Probation.IN_PROGRESS);
        dogOwners.setProbation(Probation.IN_PROGRESS);
    }

// findProbation метод -----------------------------------------------------------------------------
    @Test
    public void catFailedTest() {
        List<CatOwners> list = new ArrayList<>(List.of(catOwnersFAILED));
        when(catService.findAll()).thenReturn(list);
        when(catService.findOwnerById(chatId)).thenReturn(catOwnersFAILED);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,FAILED);

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.COMPLETED_FAILED, argument.getValue().getProbation());
    }

    @Test
    public void catPASSEDTest() {
        List<CatOwners> list = new ArrayList<>(List.of(catOwnersPASSED));
        when(catService.findAll()).thenReturn(list);
        when(catService.findOwnerById(chatId)).thenReturn(catOwnersPASSED);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,PROBATION_PASSED);

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.COMPLETED_SUCCESS, argument.getValue().getProbation());
    }

    @Test
    public void catEXTENDED_14Test() {
        List<CatOwners> list = new ArrayList<>(List.of(catOwnersEXTENDED_14));
        when(catService.findAll()).thenReturn(list);
        when(catService.findOwnerById(chatId)).thenReturn(catOwnersEXTENDED_14);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,EXTENDED_14);

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.IN_PROGRESS, argument.getValue().getProbation());
    }

    @Test
    public void catEXTENDED_30Test() {
        List<CatOwners> list = new ArrayList<>(List.of(catOwnersEXTENDED_30));
        when(catService.findAll()).thenReturn(list);
        when(catService.findOwnerById(chatId)).thenReturn(catOwnersEXTENDED_30);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,EXTENDED_30);

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.IN_PROGRESS, argument.getValue().getProbation());
    }

    @Test
    public void dogFailedTest() {
        List<DogOwners> list = new ArrayList<>(List.of(dogOwnersFAILED));
        when(dogService.findAll()).thenReturn(list);
        when(dogService.findOwnerById(chatId)).thenReturn(dogOwnersFAILED);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,FAILED);

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.COMPLETED_FAILED, argument.getValue().getProbation());
    }

    @Test
    public void dogPASSEDTest() {
        List<DogOwners> list = new ArrayList<>(List.of(dogOwnersPASSED));
        when(dogService.findAll()).thenReturn(list);
        when(dogService.findOwnerById(chatId)).thenReturn(dogOwnersPASSED);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,PROBATION_PASSED);

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.COMPLETED_SUCCESS, argument.getValue().getProbation());
    }

    @Test
    public void dogEXTENDED_14Test() {
        List<DogOwners> list = new ArrayList<>(List.of(dogOwnersEXTENDED_14));
        when(dogService.findAll()).thenReturn(list);
        when(dogService.findOwnerById(chatId)).thenReturn(dogOwnersEXTENDED_14);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,EXTENDED_14);

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.IN_PROGRESS, argument.getValue().getProbation());
    }

    @Test
    public void dogEXTENDED_30Test() {
        List<DogOwners> list = new ArrayList<>(List.of(dogOwnersEXTENDED_30));
        when(dogService.findAll()).thenReturn(list);
        when(dogService.findOwnerById(chatId)).thenReturn(dogOwnersEXTENDED_30);

        service.findProbation();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,EXTENDED_30);

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(Probation.IN_PROGRESS, argument.getValue().getProbation());
    }

//    Теты для метода checkReport ------------------------------------------------------------------
    @Test
    public void catDateNowTest(){
        List<CatOwners> lisCatOwners = new ArrayList<>();
        catReport.setDate(dateNow); //меняется в зависимости от теста
        setCatR.add(catReport);
        catOwners.setCatReports(setCatR);
        catOwners.setDaysOverdueReport(1); //даже если у пользователя 1 день просрочки, но он прислал отчет, то просрочка должна обнулиться
        catOwners.setChatId(chatId);
        lisCatOwners.add(catOwners);

        when(catService.findByProbation(Probation.IN_PROGRESS)).thenReturn(lisCatOwners);
        when(catService.findOwnerById(any(Long.class))).thenReturn(catOwners);

        service.checkReport();

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(0, argument.getValue().getDaysOverdueReport()); //вот тут должна обнулиться просрочка
    }

    @Test
    public void catDateNotNow1DayOverdueTest(){
        List<CatOwners> lisCatOwners = new ArrayList<>();
        catReport.setDate(dateNotNow); //меняется в зависимости от теста
        setCatR.add(catReport);
        catOwners.setCatReports(setCatR);
        catOwners.setDaysOverdueReport(0); //у пользователя нет просрочки, а сейчас будет первая
        catOwners.setChatId(chatId);
        lisCatOwners.add(catOwners);

        when(catService.findByProbation(Probation.IN_PROGRESS)).thenReturn(lisCatOwners);
        when(catService.findOwnerById(any(Long.class))).thenReturn(catOwners);

        service.checkReport();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,NOTICE_OF_LATE_REPORT); //проверка что мы отправили пользователю напоминание

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(1, argument.getValue().getDaysOverdueReport()); //должен добавиться 1 день просрочки
    }

    @Test
    public void catDateNotNow2DayOverdueTest(){
        List<CatOwners> lisCatOwners = new ArrayList<>();
        catReport.setDate(dateNotNow); //меняется в зависимости от теста
        setCatR.add(catReport);
        catOwners.setCatReports(setCatR);
        catOwners.setDaysOverdueReport(1); //у пользователя уже есть просрочка, а сейчас будет уже вторая
        catOwners.setChatId(chatId);
        catOwners.setPhoneNumber(phoneNumber);
        lisCatOwners.add(catOwners);

        when(catService.findByProbation(Probation.IN_PROGRESS)).thenReturn(lisCatOwners);
        when(catService.findOwnerById(any(Long.class))).thenReturn(catOwners);
        when(config.getOwnerId()).thenReturn(adminIdString);

        service.checkReport();

        verify(sendMService,times(1)).prepareAndSendMessage(adminIdLong,NOTICE_OF_LATE_REPORT_FOR_ADMIN + "Чат ID: " + chatId + " , Номер телефона : " + phoneNumber);

        ArgumentCaptor<CatOwners> argument = ArgumentCaptor.forClass(CatOwners.class);
        Mockito.verify(catService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(2, argument.getValue().getDaysOverdueReport()); //должен добавиться еще 1 день просрочки, итого 2
    }

    @Test
    public void dogDateNowTest(){
        List<DogOwners> lisDogOwners = new ArrayList<>();
        dogReport.setDate(dateNow); //меняется в зависимости от теста
        setDogR.add(dogReport);
        dogOwners.setDogReports(setDogR);
        dogOwners.setDaysOverdueReport(1); //даже если у пользователя 1 день просрочки, но он прислал отчет, то просрочка должна обнулиться
        dogOwners.setChatId(chatId);
        lisDogOwners.add(dogOwners);

        when(dogService.findByProbation(Probation.IN_PROGRESS)).thenReturn(lisDogOwners);
        when(dogService.findOwnerById(any(Long.class))).thenReturn(dogOwners);

        service.checkReport();

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(0, argument.getValue().getDaysOverdueReport()); //вот тут должна обнулиться просрочка
    }

    @Test
    public void dogDateNotNow1DayOverdueTest(){
        List<DogOwners> lisDogOwners = new ArrayList<>();
        dogReport.setDate(dateNotNow); //меняется в зависимости от теста
        setDogR.add(dogReport);
        dogOwners.setDogReports(setDogR);
        dogOwners.setDaysOverdueReport(0); //у пользователя нет просрочки, а сейчас будет первая
        dogOwners.setChatId(chatId);
        lisDogOwners.add(dogOwners);

        when(dogService.findByProbation(Probation.IN_PROGRESS)).thenReturn(lisDogOwners);
        when(dogService.findOwnerById(any(Long.class))).thenReturn(dogOwners);

        service.checkReport();

        verify(sendMService,times(1)).prepareAndSendMessage(chatId,NOTICE_OF_LATE_REPORT); //проверка что мы отправили пользователю напоминание

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(1, argument.getValue().getDaysOverdueReport()); //должен добавиться 1 день просрочки
    }

    @Test
    public void dogDateNotNow2DayOverdueTest(){
        List<DogOwners> lisDogOwners = new ArrayList<>();
        dogReport.setDate(dateNotNow); //меняется в зависимости от теста
        setDogR.add(dogReport);
        dogOwners.setDogReports(setDogR);
        dogOwners.setDaysOverdueReport(1); //у пользователя уже есть просрочка, а сейчас будет уже вторая
        dogOwners.setChatId(chatId);
        dogOwners.setPhoneNumber(phoneNumber);
        lisDogOwners.add(dogOwners);

        when(dogService.findByProbation(Probation.IN_PROGRESS)).thenReturn(lisDogOwners);
        when(dogService.findOwnerById(any(Long.class))).thenReturn(dogOwners);
        when(config.getOwnerId()).thenReturn(adminIdString);

        service.checkReport();

        verify(sendMService,times(1)).prepareAndSendMessage(adminIdLong,NOTICE_OF_LATE_REPORT_FOR_ADMIN + "Чат ID: " + chatId + " , Номер телефона : " + phoneNumber);

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogService).saveOwner(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(2, argument.getValue().getDaysOverdueReport()); //должен добавиться еще 1 день просрочки, итого 2
    }
}