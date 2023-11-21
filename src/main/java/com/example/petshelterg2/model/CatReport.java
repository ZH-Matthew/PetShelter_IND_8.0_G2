package com.example.petshelterg2.model;

import javax.persistence.*;

//класс для информации о ежедневном отчете по кошкам
@Entity
public class CatReport {
    @Id
    @GeneratedValue
    private Long id;

    private String diet;//    - *Рацион животного.*
    private String wellBeingAndAdaptation;//    - *Общее самочувствие и привыкание к новому месту.*
    private String ChangesBehavior;//    - *Изменение в поведении: отказ от старых привычек, приобретение новых.*

    @ManyToOne
    @JoinColumn(name = "chatId")
    private CatOwners catOwners;

}
