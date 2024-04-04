package com.example.petshelterg2.service;

import com.example.petshelterg2.model.DogOwners;
import com.example.petshelterg2.model.DogReport;
import com.example.petshelterg2.model.Probation;
import com.example.petshelterg2.repository.DogOwnersRepository;
import com.example.petshelterg2.repository.DogReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DogServiceTest {
    @Mock
    DogOwnersRepository dogOwnersRepository;
    @Mock
    DogReportRepository dogReportRepository;
    @Mock
    Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;
    @Mock
    Contact contact;

    @InjectMocks
    DogService service;

    Long chatId = 123L;

    DogReport dogReport = new DogReport();
    DogOwners dogOwners = new DogOwners();

    {
        dogOwners.setChatId(chatId);
    }
    @Test
    public void saveOwnerTest(){
        String firstName = "fname";
        String lastName = "lname";
        String userName = "uname";
        String phoneNumber = "88005553535";
        String status = "необходимо связаться";

        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getChat()).thenReturn(chat);
        when(message.getContact()).thenReturn(contact);
        when(chat.getFirstName()).thenReturn(firstName);
        when(chat.getLastName()).thenReturn(lastName);
        when(chat.getUserName()).thenReturn(userName);
        when(contact.getPhoneNumber()).thenReturn(phoneNumber);

        service.saveOwner(update);

        ArgumentCaptor<DogOwners> argument = ArgumentCaptor.forClass(DogOwners.class);
        Mockito.verify(dogOwnersRepository).save(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(firstName, argument.getValue().getFirstName());
        assertEquals(lastName, argument.getValue().getLastName());
        assertEquals(userName, argument.getValue().getUserName());
        assertEquals(phoneNumber, argument.getValue().getPhoneNumber());
        assertEquals(status, argument.getValue().getStatus());
        assertEquals(Probation.NOT_ASSIGNED, argument.getValue().getProbation());
    }
    @Test
    public void reportDietTest(){
        String diet = "diet";
        Optional<DogOwners> opt = Optional.of(dogOwners);

        when(dogOwnersRepository.findById(any(Long.class))).thenReturn(opt);
        when(dogReportRepository.findFirstByDogOwnersAndDate(any(DogOwners.class),any(LocalDate.class))).thenReturn(dogReport);

        service.reportDiet(diet,chatId);

        ArgumentCaptor<DogReport> argument = ArgumentCaptor.forClass(DogReport.class);
        Mockito.verify(dogReportRepository).save(argument.capture());
        assertEquals(diet, argument.getValue().getDiet());
    }
    @Test
    public void reportWellBeingAndAdaptationTest(){
        String wellBeingAndAdaptation = "BeingAndAdaptation";
        Optional<DogOwners> opt = Optional.of(dogOwners);

        when(dogOwnersRepository.findById(any(Long.class))).thenReturn(opt);
        when(dogReportRepository.findFirstByDogOwnersAndDate(any(DogOwners.class),any(LocalDate.class))).thenReturn(dogReport);

        service.reportWellBeingAndAdaptation(wellBeingAndAdaptation,chatId);

        ArgumentCaptor<DogReport> argument = ArgumentCaptor.forClass(DogReport.class);
        Mockito.verify(dogReportRepository).save(argument.capture());
        assertEquals(wellBeingAndAdaptation, argument.getValue().getWellBeingAndAdaptation());
    }

    @Test
    public void reportChangesBehaviorTest(){
        String changesBehavior = "ChangesBehavior";
        Optional<DogOwners> opt = Optional.of(dogOwners);

        when(dogOwnersRepository.findById(any(Long.class))).thenReturn(opt);
        when(dogReportRepository.findFirstByDogOwnersAndDate(any(DogOwners.class),any(LocalDate.class))).thenReturn(dogReport);

        service.reportChangesBehavior(changesBehavior,chatId);

        ArgumentCaptor<DogReport> argument = ArgumentCaptor.forClass(DogReport.class);
        Mockito.verify(dogReportRepository).save(argument.capture());
        assertEquals(changesBehavior, argument.getValue().getChangesBehavior());

    }

}