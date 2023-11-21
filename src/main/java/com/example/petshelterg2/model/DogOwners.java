package com.example.petshelterg2.model;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;

/**
 * Хранит данные о владельцах собак
 */
@Data
@Entity(name = "DogOwners")
public class DogOwners {
    @Id
    private Long chatId; //(Primary Key)

    private String lastName;
    private String firstName;
    private String userName;
    private String phoneNumber;
    private Probation probation; //enum-поле испытательного срока

    //    Как только период в 30 дней заканчивается, волонтеры принимают решение о том, остается животное у хозяина или нет.
//    Испытательный срок может быть пройден, может быть продлен на срок еще 14 или 30 дней, а может быть не пройден.
//    Нам нужно реализовать это задание через этот параметр. Параметр скорее всего будет хранить конечный день (30й) испытательного срока
//    скорее всего нужно будет добавить метод по шедулду который проверяет наступил ли этот день или нет, и если да, отправляет сообщения о прохождении
//    и еще пару методов которые добавляют 14 или 30 дней к испытательному сроку
    private LocalDateTime dateTime;
    private String status;
//    @OneToMany(mappedBy = "dogOwners")
//    private Set<DogReport> dogReports;
}
