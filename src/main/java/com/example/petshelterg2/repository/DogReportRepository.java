package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.DogOwners;
import com.example.petshelterg2.model.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Long> {
    DogReport findFirstByDogOwnersAndDate(DogOwners dogOwners, LocalDate date);
}
