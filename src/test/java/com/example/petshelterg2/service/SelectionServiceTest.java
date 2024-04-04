package com.example.petshelterg2.service;

import com.example.petshelterg2.model.Selection;
import com.example.petshelterg2.repository.SelectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelectionServiceTest {

    @Mock
    SelectionRepository sRepository;

    @InjectMocks
    SelectionService service;

    Long chatId = 123L;

    Selection selection = new Selection();


    @Test
    public void findByIdTest() {

        selection.setChatId(chatId);
        selection.setSelection(true);
        selection.setCounter(1);

        when(sRepository.findById(any(Long.class))).thenReturn(Optional.of(selection));

        assertEquals(selection, service.findById(chatId));

    }

    @Test
    public void findByIdFailTest() {
        selection.setChatId(chatId);
        selection.setSelection(true);
        selection.setCounter(1);

        String message = "Поиск не дал результатов! Пользователь с chatId : " + 5555 + " отсутствует! Логика программы нарушена,потому что он должен там быть!";

        Exception exception = assertThrows(
                NoSuchElementException.class,
                () -> {
                    service.findById(5555);
                }
        );

        assertEquals(message, exception.getMessage());

    }

    @Test
    public void saveSelectionTest() {

        when(sRepository.save(any(Selection.class))).thenReturn(selection);

        service.save(chatId, true, 1);

        ArgumentCaptor<Selection> argument = ArgumentCaptor.forClass(Selection.class);
        verify(sRepository).save(argument.capture());
        assertEquals(chatId, argument.getValue().getChatId());
        assertEquals(true, argument.getValue().getSelection());
        assertEquals(1, argument.getValue().getCounter());
    }

}