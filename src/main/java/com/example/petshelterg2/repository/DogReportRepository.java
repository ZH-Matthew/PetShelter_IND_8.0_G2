package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.DogReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogReportRepository extends JpaRepository<DogReport, Long> {
}
