package com.example.petshelterg2.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Builder
@Data
public class CatReportPhoto {
    @Id
    @GeneratedValue
    private Long id;

    private String telegramFileId;

    @OneToOne
    @JoinColumn(name = "Id")
    private CatReport catReport;

    private Integer fileSize;

    private byte[] fileAsArrayOfBytes;
}

