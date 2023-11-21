package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.DogReport;
import com.example.petshelterg2.model.Selection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionRepository extends JpaRepository<Selection, Long> {
}
