package com.barberplusapi.demo.controllers;

import com.barberplusapi.demo.dto.EmployeeDTO;
import com.barberplusapi.demo.models.Employee;
import com.barberplusapi.demo.models.JobSchedule;
import com.barberplusapi.demo.models.WorkSchedule;
import com.barberplusapi.demo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByCompany(@PathVariable UUID companyId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByCompany(companyId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable UUID id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        if (employeeDTO.getWorkSchedule() == null) {
            employeeDTO.setWorkSchedule(new ArrayList<>());
        }
        
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable UUID id, @RequestBody EmployeeDTO employeeDTO) throws Exception {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        if (updatedEmployee != null) {
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    
    @GetMapping("/{employeeId}/time-slots")
    public ResponseEntity<List<String>> getEmployeeTimeSlots(
        @PathVariable UUID employeeId,
        @RequestParam LocalDate date
    ) {
        Optional<Employee> employeeOptional = employeeService.findById(employeeId);

    
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Employee employee = employeeOptional.get();

        List<JobSchedule> jobSchedules = employee.getJobSchedules().stream().filter(jobScheduleItem -> jobScheduleItem.getDate().equals(date)).collect(Collectors.toList());
      
        List<WorkSchedule> workSchedule = employee.getWorkSchedule();
        
        if (workSchedule == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        List<String> timeSlots = workSchedule.stream()
            .flatMap(schedule -> schedule.generateTimeSlots().stream())
            .collect(Collectors.toList());

        if(!jobSchedules.isEmpty()) {
            timeSlots.removeIf(
                h -> (h.compareTo(jobSchedules.get(0).getStartTime().toString()) >= 0 && h.compareTo(jobSchedules.get(0).getEndTime().toString()) <= 0)
            );
        }
        
        return ResponseEntity.ok(timeSlots);
    }
} 