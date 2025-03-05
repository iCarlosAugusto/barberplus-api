package com.barberplusapi.demo.models;

import jakarta.persistence.*;
import com.barberplusapi.demo.responses.JobResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer durationMinutes;
    
    @ManyToMany(mappedBy = "jobs")
    @JsonIgnoreProperties("jobs")
    private List<Employee> employees = new ArrayList<Employee>();
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public JobResponse toResponse() {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(id);
        jobResponse.setName(name);
        jobResponse.setDescription(description);
        jobResponse.setPrice(price);
        jobResponse.setDurationMinutes(durationMinutes);
        jobResponse.setDoneByEmployees(employees.stream().map(Employee::toResponse).collect(Collectors.toList()));
        return jobResponse;
    }
} 