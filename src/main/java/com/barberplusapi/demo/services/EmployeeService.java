package com.barberplusapi.demo.services;

import com.barberplusapi.demo.dto.EmployeeDTO;
import com.barberplusapi.demo.models.Company;
import com.barberplusapi.demo.models.Employee;
import com.barberplusapi.demo.models.Job;
import com.barberplusapi.demo.repositories.CompanyRepository;
import com.barberplusapi.demo.repositories.EmployeeRepository;
import com.barberplusapi.demo.repositories.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
        // return employeeRepository.findAll().stream()
        //         .map(this::convertToDTO)
        //         .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesByCompany(UUID companyId) {
        return employeeRepository.findByCompanyId(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO getEmployeeById(UUID id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(this::convertToDTO).orElse(null);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }

    public EmployeeDTO updateEmployee(UUID id, EmployeeDTO employeeDTO) throws Exception {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            
            employee.setName(employeeDTO.getName());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setPosition(employeeDTO.getPosition());

            if (employeeDTO.getJobId() != null) {
                Job job = jobRepository.findById(employeeDTO.getJobId()).orElseThrow(() -> new Exception("Job not found"));
                employee.getJobs().add(job);
            }
            
            if (employeeDTO.getCompanyId() != null) {
                Optional<Company> company = companyRepository.findById(employeeDTO.getCompanyId());
                company.ifPresent(employee::setCompany);
            }
            
            Employee updatedEmployee = employeeRepository.save(employee);
            return convertToDTO(updatedEmployee);
        }
        
        return null;
    }

    public boolean deleteEmployee(UUID id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Employee> findById(UUID id) {
        return employeeRepository.findById(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setPosition(employee.getPosition());
        dto.setWorkSchedule(employee.getWorkSchedule());
        dto.setJobSchedules(employee.getJobSchedules());
        
        if (employee.getCompany() != null) {
            dto.setCompanyId(employee.getCompany().getId());
        }
        
        return dto;
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setPosition(employeeDTO.getPosition());
        employee.setWorkSchedule(employeeDTO.getWorkSchedule());
        employee.setJobSchedules(employeeDTO.getJobSchedules());
        
        if (employeeDTO.getCompanyId() != null) {
            Optional<Company> company = companyRepository.findById(employeeDTO.getCompanyId());
            company.ifPresent(employee::setCompany);
        }
        
        return employee;
    }
} 