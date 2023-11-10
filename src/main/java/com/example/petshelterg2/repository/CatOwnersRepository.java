package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.CatOwners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatOwnersRepository extends JpaRepository<CatOwners, Long> {
}
