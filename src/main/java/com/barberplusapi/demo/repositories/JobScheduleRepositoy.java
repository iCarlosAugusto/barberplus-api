package com.barberplusapi.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barberplusapi.demo.models.JobSchedule;

public interface JobScheduleRepositoy extends JpaRepository<JobSchedule, UUID> {
    
}
