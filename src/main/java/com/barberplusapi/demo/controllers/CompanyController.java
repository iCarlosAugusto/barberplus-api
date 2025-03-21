package com.barberplusapi.demo.controllers;

import com.barberplusapi.demo.dto.CompanyDTO;
import com.barberplusapi.demo.dto.EmployeeDTO;
import com.barberplusapi.demo.models.Employee;
import com.barberplusapi.demo.models.JobSchedule;
import com.barberplusapi.demo.models.WorkSchedule;
import com.barberplusapi.demo.responses.CompanyResponse;
import com.barberplusapi.demo.responses.JobResponse;
import com.barberplusapi.demo.services.CompanyService;
import com.barberplusapi.demo.services.EmployeeService;
import com.barberplusapi.demo.services.JobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobService jobService;
    
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
    
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
        List<CompanyDTO> companies = companyService.getAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable UUID id) {
        CompanyDTO company = companyService.getCompanyById(id);
        if (company != null) {
            return new ResponseEntity<>(company, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CompanyResponse> getCompanyBySlug(@PathVariable String slug) {
        CompanyResponse company = companyService.getCompanyBySlug(slug);
        if (company != null) {
            return new ResponseEntity<>(company, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO createdCompany = companyService.createCompany(companyDTO);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable UUID id, @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
        if (updatedCompany != null) {
            return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable UUID id) {
        boolean deleted = companyService.deleteCompany(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CompanyDTO>> searchCompaniesByName(@RequestParam String name) {
        List<CompanyDTO> companies = companyService.searchCompaniesByName(name);
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{companyId}/jobs")
    public ResponseEntity<List<JobResponse>> getJobsByCompany(@PathVariable UUID companyId) {
        List<JobResponse> jobs = jobService.getJobsByCompany(companyId);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }


    @GetMapping("/{companyId}/time-slots")
    public ResponseEntity<List<String>> getAvailableTimeSlots(
        @PathVariable UUID companyId,
        @RequestParam LocalDate date
    ) {
        LocalDate today = LocalDate.now();
        if(date.isBefore(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data não pode ser anterior a hoje");
        }
        
        List<EmployeeDTO> employees = employeeService.getEmployeesByCompany(companyId);
        if (employees.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }   
        
        // Coletando todos os slots disponíveis de todos os funcionários
        List<String> allAvailableSlots = new ArrayList<>();
        
        for (EmployeeDTO employeeDTO : employees) {
            Optional<Employee> employeeOptional = employeeService.findById(employeeDTO.getId());
            
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                
                // Filtrando agendamentos para a data específica
                List<JobSchedule> jobSchedules = employee.getJobSchedules().stream()
                    .filter(jobSchedule -> jobSchedule.getDate().equals(date))
                    .collect(Collectors.toList());
                
                List<WorkSchedule> workSchedule = employee.getWorkSchedule();
                
                if (workSchedule != null && !workSchedule.isEmpty()) {
                    // Gerando todos os slots de tempo para o funcionário
                    List<String> employeeSlots = workSchedule.stream()
                        .flatMap(schedule -> schedule.generateTimeSlots().stream())
                        .collect(Collectors.toList());
                    
                    // Removendo slots que já estão agendados
                    if (!jobSchedules.isEmpty()) {
                        for (JobSchedule jobSchedule : jobSchedules) {
                            employeeSlots.removeIf(slot -> 
                                slot.compareTo(jobSchedule.getStartTime().toString()) >= 0 && 
                                slot.compareTo(jobSchedule.getEndTime().toString()) < 0
                            );
                        }
                    }
                    
                    // Adicionando os slots disponíveis à lista geral
                    allAvailableSlots.addAll(employeeSlots);
                }
            }
        }
        
        // Removendo duplicatas e ordenando
        List<String> uniqueSlots = allAvailableSlots.stream()
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(uniqueSlots);
    }
}
