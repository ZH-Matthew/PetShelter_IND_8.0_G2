package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.CatOwners;
import com.example.petshelterg2.model.DogOwners;
import com.example.petshelterg2.model.Probation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogOwnersRepository extends JpaRepository<DogOwners, Long> {
    List<DogOwners> findByProbation(Probation probation); //поиск по статусу испытательного срока
}
