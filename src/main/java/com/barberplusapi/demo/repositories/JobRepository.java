package com.barberplusapi.demo.repositories;

import com.barberplusapi.demo.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    // @Query("SELECT j FROM Job j JOIN j.employee e WHERE e.id = :employeeId")
    // List<Job> findByEmployeeId(UUID employeeId);
    // @Query("SELECT j FROM Job j JOIN j.employees e WHERE e.id = :employeeId")
    // List<Job> findByEmployeeId(UUID employeeId);


    List<Job> findByCompanyId(UUID companyId);
} 