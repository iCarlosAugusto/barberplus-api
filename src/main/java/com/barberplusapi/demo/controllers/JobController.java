package com.barberplusapi.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.barberplusapi.demo.models.Job;
import com.barberplusapi.demo.models.JobSchedule;
import com.barberplusapi.demo.repositories.JobRepository;
import com.barberplusapi.demo.request.CreateJobRequest;
import com.barberplusapi.demo.dto.BookJobDTO;
import com.barberplusapi.demo.responses.JobResponse;
import com.barberplusapi.demo.services.JobService;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    // @GetMapping("/employee/{employeeId}")
    // public ResponseEntity<List<JobResponse>> getJobsByEmployee(@PathVariable UUID employeeId) {
    //     List<JobResponse> jobs = jobService.getJobsByEmployee(employeeId);
    //     return new ResponseEntity<>(jobs, HttpStatus.OK);
    // }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Job>> getJobsByEmployee(@PathVariable("employeeId") UUID employeeId) {
        List<Job> jobs = jobRepository.findAll();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable("id") UUID id) {
        JobResponse job = jobService.getJobById(id);
        if (job != null) {
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JobResponse> createJob(@RequestBody CreateJobRequest createJobRequest) {
        JobResponse createdJob = jobService.createJob(createJobRequest);
        return new ResponseEntity<JobResponse>(createdJob, HttpStatus.CREATED);
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<JobResponse> updateJob(@PathVariable UUID id, @RequestBody JobResponse jobResponse) {
    //     JobResponse updatedJob = jobService.updateJob(id, jobResponse);
    //     if (updatedJob != null) {
    //         return new ResponseEntity<>(updatedJob, HttpStatus.OK);
    //     }
    //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") UUID id) {
        boolean deleted = jobService.deleteJob(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/book")
    public JobSchedule bookJob(@RequestBody BookJobDTO bookJobDTO) {
        return jobService.bookJob(bookJobDTO);
    }
} 