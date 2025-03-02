package com.barberplusapi.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookJobDTO {
    private UUID employeeId;
    private UUID jobId;
    private LocalDate date;
    private LocalTime time;
}
