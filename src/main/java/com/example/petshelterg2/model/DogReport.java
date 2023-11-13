package com.example.petshelterg2.model;

import javax.persistence.*;

//класс для информации о ежедневном отчете по собакам
@Entity
public class DogReport {
    @Id
    @GeneratedValue
    private Long id;

    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;

    //текстовая информация о:
//    - *Рацион животного.*
//    - *Общее самочувствие и привыкание к новому месту.*
//    - *Изменение в поведении: отказ от старых привычек, приобретение новых.*
    private String info;

    @ManyToOne
    @JoinColumn(name = "chatId")
    private DogOwners dogOwners;
}
