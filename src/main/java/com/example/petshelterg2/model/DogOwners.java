package com.example.petshelterg2.model;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;

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
    private int daysOverdueReport; //дни просрочки отчёта (ожидаемые значения 0/1/2)
    private Probation probation; //enum-поле испытательного срока


    private LocalDateTime dateTime;
    private String status;
    @OneToMany(mappedBy = "dogOwners")
    private Set<DogReport> dogReports;
}
