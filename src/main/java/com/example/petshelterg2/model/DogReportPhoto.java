package com.example.petshelterg2.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Builder
public class DogReportPhoto {
    @Id
    @GeneratedValue
    private Long id;

    private String telegramFileId;

    @OneToOne
    @JoinColumn(name = "Id")
    private DogReport dogReport;

    private Integer fileSize;

    private byte[] fileAsArrayOfBytes;
}
