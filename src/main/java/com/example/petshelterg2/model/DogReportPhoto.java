package com.example.petshelterg2.model;

import javax.persistence.*;

@Entity
public class DogReportPhoto {
    @Id
    @GeneratedValue
    private Long id;

    private String telegramFileId;

    @OneToOne
    @JoinColumn(name = "Id")
    private DogReport dogReport;

    private Integer fileSize;
}
