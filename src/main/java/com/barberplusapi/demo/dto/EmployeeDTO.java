package com.barberplusapi.demo.dto;

import java.util.List;
import java.util.UUID;

import com.barberplusapi.demo.models.JobSchedule;
import com.barberplusapi.demo.models.WorkSchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String position;
    private UUID companyId;
    private List<WorkSchedule> workSchedule;
    private List<JobSchedule> jobSchedules;
    private UUID jobId;
} 