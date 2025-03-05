package com.barberplusapi.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import com.barberplusapi.demo.models.Job;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private UUID companyId;


    public Job toEntity(JobDTO jobDTO) {
        Job job = new Job();
        job.setId(jobDTO.getId());
        job.setName(jobDTO.getName());
        job.setDescription(jobDTO.getDescription());
        job.setPrice(jobDTO.getPrice());
        job.setDurationMinutes(jobDTO.getDurationMinutes());
        return job;
    }
} 