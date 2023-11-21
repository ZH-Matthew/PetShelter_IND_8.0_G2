package com.example.petshelterg2.model;

import javax.persistence.*;

@Entity
public class CatReportPhoto {
    @Id
    @GeneratedValue
    private Long id;

    private String telegramFileId;
    @OneToOne
    @JoinColumn(name = "Id")
    private CatReport catReport;

    private Integer fileSize;
}
