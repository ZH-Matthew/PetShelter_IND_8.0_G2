package com.example.petshelterg2.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

//класс для информации о ежедневном отчете по собакам
@Data
@Entity
public class DogReport {
    @Id
    @GeneratedValue
    private Long id;

    private String diet;//    - *Рацион животного.*
    private String wellBeingAndAdaptation;//    - *Общее самочувствие и привыкание к новому месту.*
    private String ChangesBehavior;//    - *Изменение в поведении: отказ от старых привычек, приобретение новых.*
    private LocalDate date; // только дата без времени когда пользователь загрузил отчёт
    private byte[] fileAsArrayOfBytes;//Фото массив байт

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private DogOwners dogOwners;
}
