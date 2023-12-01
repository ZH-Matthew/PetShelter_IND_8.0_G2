package com.example.petshelterg2.service;

import com.example.petshelterg2.model.Selection;
import com.example.petshelterg2.repository.SelectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SelectionService {

    private final SelectionRepository sRepository;
    @Autowired
    public SelectionService(SelectionRepository sRepository) {
        this.sRepository = sRepository;
    }

    public Selection findById(long chatId){
        return sRepository.findById(chatId).get();
    }

    public void save(Selection selection){
        sRepository.save(selection);
    }


}
