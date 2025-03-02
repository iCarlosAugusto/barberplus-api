package com.barberplusapi.demo.services;

import com.barberplusapi.demo.dto.JobDTO;
import com.barberplusapi.demo.models.Company;
import com.barberplusapi.demo.models.Employee;
import com.barberplusapi.demo.models.Job;
import com.barberplusapi.demo.repositories.CompanyRepository;
import com.barberplusapi.demo.repositories.EmployeeRepository;
import com.barberplusapi.demo.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<JobDTO> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByEmployee(UUID employeeId) {
        return jobRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<JobDTO> getJobsByCompany(UUID companyId) {
        return jobRepository.findByCompanyId(companyId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public JobDTO getJobById(UUID id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.map(this::convertToDTO).orElse(null);
    }

    public JobDTO createJob(JobDTO jobDTO) {
        Job job = convertToEntity(jobDTO);
        Job savedJob = jobRepository.save(job);
        return convertToDTO(savedJob);
    }

    public JobDTO updateJob(UUID id, JobDTO jobDTO) {
        Optional<Job> existingJob = jobRepository.findById(id);
        
        if (existingJob.isPresent()) {
            Job job = existingJob.get();
            
            job.setName(jobDTO.getName());
            job.setDescription(jobDTO.getDescription());
            job.setPrice(jobDTO.getPrice());
            job.setDurationMinutes(jobDTO.getDurationMinutes());
            
            if (jobDTO.getEmployeeId() != null) {
                Optional<Employee> employee = employeeRepository.findById(jobDTO.getEmployeeId());
                employee.ifPresent(job::setEmployee);
            }
            
            if (jobDTO.getCompanyId() != null) {
                Optional<Company> company = companyRepository.findById(jobDTO.getCompanyId());
                company.ifPresent(job::setCompany);
            }
            
            Job updatedJob = jobRepository.save(job);
            return convertToDTO(updatedJob);
        }
        
        return null;
    }

    public boolean deleteJob(UUID id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private JobDTO convertToDTO(Job job) {
        JobDTO dto = new JobDTO();
        dto.setId(job.getId());
        dto.setName(job.getName());
        dto.setDescription(job.getDescription());
        dto.setPrice(job.getPrice());
        dto.setDurationMinutes(job.getDurationMinutes());
        
        if (job.getEmployee() != null) {
            dto.setEmployeeId(job.getEmployee().getId());
        }
        
        if (job.getCompany() != null) {
            dto.setCompanyId(job.getCompany().getId());
        }
        
        return dto;
    }

    private Job convertToEntity(JobDTO jobDTO) {
        Job job = new Job();
        job.setName(jobDTO.getName());
        job.setDescription(jobDTO.getDescription());
        job.setPrice(jobDTO.getPrice());
        job.setDurationMinutes(jobDTO.getDurationMinutes());
        
        if (jobDTO.getEmployeeId() != null) {
            Optional<Employee> employee = employeeRepository.findById(jobDTO.getEmployeeId());
            employee.ifPresent(job::setEmployee);
        }
        
        if (jobDTO.getCompanyId() != null) {
            Optional<Company> company = companyRepository.findById(jobDTO.getCompanyId());
            company.ifPresent(job::setCompany);
        }
        
        return job;
    }
} 