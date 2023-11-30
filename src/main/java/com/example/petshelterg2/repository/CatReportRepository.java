package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.CatOwners;
import com.example.petshelterg2.model.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CatReportRepository extends JpaRepository<CatReport, Long> {
    CatReport findFirstByCatOwnersAndDate(CatOwners catOwners, LocalDate date);
}
