package com.barberplusapi.demo.request;

import java.math.BigDecimal;
import java.util.UUID;

import com.barberplusapi.demo.models.Job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private UUID companyId;

    public Job toEntity() {
        Job job = new Job();    
        job.setName(name);
        job.setDescription(description);
        job.setPrice(price);
        job.setDurationMinutes(durationMinutes);
        return job;
    }
}
