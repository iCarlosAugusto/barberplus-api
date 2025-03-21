package com.barberplusapi.demo.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

import com.barberplusapi.demo.responses.CompanyResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String name;

    private String slug;
    
    private String description;
    
    private String address;
    
    private String phone;
    
    private String email;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Job> jobs;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }


    public CompanyResponse toResponse() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setId(id);
        companyResponse.setName(name);
        companyResponse.setSlug(slug);
        companyResponse.setDescription(description);
        companyResponse.setAddress(address);
        companyResponse.setPhone(phone);
        companyResponse.setEmail(email);
        companyResponse.setCreatedAt(createdAt);
        companyResponse.setUpdatedAt(updatedAt);
        return companyResponse;
    }
} 