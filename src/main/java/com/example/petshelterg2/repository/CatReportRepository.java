package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.CatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatReportRepository extends JpaRepository<CatReport, Long> {
}
