package com.barberplusapi.demo.responses;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.barberplusapi.demo.models.WorkSchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

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
