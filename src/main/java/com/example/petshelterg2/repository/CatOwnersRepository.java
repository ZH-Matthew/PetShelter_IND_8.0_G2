package com.example.petshelterg2.repository;

import com.example.petshelterg2.model.CatOwners;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CatOwnersRepository extends JpaRepository<CatOwners, Long> {
    Collection<CatOwners> findByPhoneNumber(String phoneNumber);
}
