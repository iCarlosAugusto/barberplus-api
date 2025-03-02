package com.barberplusapi.demo.repositories;

import com.barberplusapi.demo.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    List<Company> findByNameContainingIgnoreCase(String name);
    boolean existsByEmail(String email);
} 