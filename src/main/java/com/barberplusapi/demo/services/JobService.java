package com.barberplusapi.demo.services;

import com.barberplusapi.demo.dto.BookJobDTO;
import com.barberplusapi.demo.dto.JobDTO;
import com.barberplusapi.demo.models.Company;
import com.barberplusapi.demo.models.Employee;
import com.barberplusapi.demo.models.Job;
import com.barberplusapi.demo.models.JobSchedule;
import com.barberplusapi.demo.repositories.CompanyRepository;
import com.barberplusapi.demo.repositories.EmployeeRepository;
import com.barberplusapi.demo.repositories.JobRepository;
import com.barberplusapi.demo.repositories.JobScheduleRepositoy;
import com.barberplusapi.demo.request.CreateJobRequest;
import com.barberplusapi.demo.responses.JobResponse;

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
    private JobScheduleRepositoy jobScheduleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(Job::toResponse)
                .collect(Collectors.toList());
    }

    // public List<JobResponse> getJobsByEmployee(UUID employeeId) {
    //     return jobRepository.findByEmployeeId(employeeId).stream()
    //             .map(Job::toResponse)
    //             .collect(Collectors.toList());
    // }

    public List<JobResponse> getJobsByCompany(UUID companyId) {
        return jobRepository.findByCompanyId(companyId).stream()
                .map(Job::toResponse)
                .collect(Collectors.toList());
    }

    public JobResponse getJobById(UUID id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.map(Job::toResponse).orElse(null);
    }

    public JobResponse createJob(CreateJobRequest createJobRequest) {
        Job job = createJobRequest.toEntity();
        Job savedJob = jobRepository.save(job);
        return savedJob.toResponse();
    }

    public JobDTO updateJob(UUID id, JobDTO jobDTO) {
        // Optional<Job> existingJob = jobRepository.findById(id);
        
        // if (existingJob.isPresent()) {
        //     Job job = existingJob.get();
            
        //     job.setName(jobDTO.getName());
        //     job.setDescription(jobDTO.getDescription());
        //     job.setPrice(jobDTO.getPrice());
        //     job.setDurationMinutes(jobDTO.getDurationMinutes());
            
        //     if (jobDTO.getEmployeeId() != null) {
        //         Optional<Employee> employee = employeeRepository.findById(jobDTO.getEmployeeId());
        //         employee.ifPresent(job::addEmployee);
        //     }
            
        //     if (jobDTO.getCompanyId() != null) {
        //         Optional<Company> company = companyRepository.findById(jobDTO.getCompanyId());
        //         company.ifPresent(job::setCompany);
        //     }
            
        //     Job updatedJob = jobRepository.save(job);
        //     return convertToDTO(updatedJob);
        // }
        
        return null;
    }

    public boolean deleteJob(UUID id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // private JobDTO convertToDTO(Job job) {
    //     JobDTO dto = new JobDTO();
    //     dto.setId(job.getId());
    //     dto.setName(job.getName());
    //     dto.setDescription(job.getDescription());
    //     dto.setPrice(job.getPrice());
    //     dto.setDurationMinutes(job.getDurationMinutes());
        
    //     if (job.getEmployees() != null) {
    //         dto.setEmployeeId(job.getEmployees().get(0).getId());
    //     }
        
    //     if (job.getCompany() != null) {
    //         dto.setCompanyId(job.getCompany().getId());
    //     }
        
    //     return dto;
    // }

    // private Job convertToEntity(JobDTO jobDTO) {
    //     Job job = new Job();
    //     job.setName(jobDTO.getName());
    //     job.setDescription(jobDTO.getDescription());
    //     job.setPrice(jobDTO.getPrice());
    //     job.setDurationMinutes(jobDTO.getDurationMinutes());
        
    //     if (jobDTO.getEmployeeId() != null) {
    //         Optional<Employee> employee = employeeRepository.findById(jobDTO.getEmployeeId());
    //         employee.ifPresent(job::addEmployee);
    //     }
        
    //     if (jobDTO.getCompanyId() != null) {
    //         Optional<Company> company = companyRepository.findById(jobDTO.getCompanyId());
    //         company.ifPresent(job::setCompany);
    //     }
        
    //     return job;
    // }

    public JobSchedule bookJob(BookJobDTO bookJobDTO) {
        Optional<Employee> employee = employeeRepository.findById(bookJobDTO.getEmployeeId());
        Optional<Job> job = jobRepository.findById(bookJobDTO.getJobId());

        if(employee.isEmpty() || job.isEmpty()) {
            throw new RuntimeException("Employee or job not found");
        }

        JobSchedule jobSchedule = new JobSchedule();
        jobSchedule.setEmployee(employee.get());    
        jobSchedule.setDate(bookJobDTO.getDate());
        jobSchedule.setStartTime(bookJobDTO.getTime());
        jobSchedule.setEndTime(bookJobDTO.getTime().plusMinutes(job.get().getDurationMinutes()));

        employee.get().getJobSchedules().add(jobSchedule);

        return jobScheduleRepository.save(jobSchedule);
    }
} 