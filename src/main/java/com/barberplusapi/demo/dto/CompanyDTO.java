package com.barberplusapi.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;
import java.util.List;
import com.barberplusapi.demo.models.WorkSchedule;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String address;
    private String phone;
    private String email;
    private Date createdAt;
    private Date updatedAt;
    private List<WorkSchedule> workSchedule;
} 