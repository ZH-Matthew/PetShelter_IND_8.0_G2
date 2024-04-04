package com.example.petshelterg2.service;

import com.example.petshelterg2.model.Selection;
import com.example.petshelterg2.repository.SelectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class SelectionService {

    private final SelectionRepository sRepository;
    @Autowired
    public SelectionService(SelectionRepository sRepository) {
        this.sRepository = sRepository;
    }

    public Selection findById(long chatId){
        return sRepository.findById(chatId).orElseThrow(()-> new NoSuchElementException("Поиск не дал результатов! Пользователь с chatId : "+ chatId +" отсутствует! Логика программы нарушена,потому что он должен там быть!"));
    }

    public void save(Selection selection){
        sRepository.save(selection);
    }

    public void save(long chatId, Boolean selection, Integer counter) {
        Selection newSelection = new Selection();
        newSelection.setSelection(selection);
        newSelection.setChatId(chatId);
        newSelection.setCounter(counter);
        sRepository.save(newSelection);
    }


}
