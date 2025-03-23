package com.barberplusapi.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.barberplusapi.demo.responses.EmployeeResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private List<WorkScheduleTeste> workSchedule;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<JobSchedule> jobSchedules;
    
    @ManyToOne
    @JoinColumn()
    @JsonIgnoreProperties("employees")
    private Company company;
    
    @ManyToMany()
    @JoinTable(
        name = "employee_jobs",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "job_id")
    )

    @JsonIgnoreProperties("employees")
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

    public EmployeeResponse toResponse(){
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(id);
        employeeResponse.setName(name);
        employeeResponse.setEmail(email);
        employeeResponse.setPhone(phone);
        employeeResponse.setPosition(position);
        return employeeResponse;
    }
} 