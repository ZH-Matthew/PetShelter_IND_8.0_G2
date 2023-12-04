package com.example.petshelterg2.service;

import com.example.petshelterg2.model.Selection;
import com.example.petshelterg2.repository.SelectionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SelectionServiceTest {

    //вылетает NullPointer ХЗ почему , надо выяснять, грешу не Boolean

    @Mock
    SelectionRepository sRepository;
    @InjectMocks
    SelectionService service;

    Selection selection = new Selection();

    Boolean b = true;
    Integer i = 1;
    Long l = 123L;
    {
        selection.setSelection(true);
        selection.setCounter(i);
        selection.setChatId(l);
    }

    @Test
    void saveTest() {
        service.save(l,b,i);
        ArgumentCaptor<Selection> argument = ArgumentCaptor.forClass(Selection.class);
        Mockito.verify(sRepository).save(argument.capture());
        assertEquals(l, argument.getValue().getChatId());
        assertEquals(i, argument.getValue().getCounter());
        assertEquals(b, argument.getValue().getSelection());
    }
}