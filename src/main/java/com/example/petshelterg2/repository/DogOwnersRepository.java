package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.DogOwners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogOwnersRepository extends JpaRepository<DogOwners, Long> {
}
