package com.barberplusapi.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String name;
    private String email;
    private String phone;
    private String position;
    
    @ElementCollection
    @CollectionTable(name = "employee_work_schedule", joinColumns = @JoinColumn(name = "employee_id"))
    private List<WorkSchedule> workSchedule;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<JobSchedule> jobSchedules;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Job> jobs;
    
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
} 