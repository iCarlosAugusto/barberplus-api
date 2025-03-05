package com.barberplusapi.demo.responses;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;

    @Nullable
    private List<EmployeeResponse> doneByEmployees;
}