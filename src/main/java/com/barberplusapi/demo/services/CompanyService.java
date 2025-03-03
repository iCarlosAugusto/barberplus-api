package com.barberplusapi.demo.services;

import com.barberplusapi.demo.dto.CompanyDTO;
import com.barberplusapi.demo.models.Company;
import com.barberplusapi.demo.repositories.CompanyRepository;
import com.barberplusapi.demo.responses.CompanyResponse;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CompanyDTO getCompanyById(UUID id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.map(this::convertToDTO).orElse(null);
    }

    public CompanyResponse getCompanyBySlug(String slug) {
        Optional<Company> company = companyRepository.findBySlug(slug);
        return company.map(Company::toResponse).orElse(null);
    }
    
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = convertToEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);
        return convertToDTO(savedCompany);
    }
    
    public CompanyDTO updateCompany(UUID id, CompanyDTO companyDTO) {
        if (companyRepository.existsById(id)) {
            Company company = convertToEntity(companyDTO);
            company.setId(id);
            Company updatedCompany = companyRepository.save(company);
            return convertToDTO(updatedCompany);
        }
        return null;
    }
    
    public boolean deleteCompany(UUID id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<CompanyDTO> searchCompaniesByName(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private CompanyDTO convertToDTO(Company company) {
        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getSlug(),
                company.getDescription(),
                company.getAddress(),
                company.getPhone(),
                company.getEmail(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
    
    private Company convertToEntity(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setDescription(companyDTO.getDescription());
        company.setAddress(companyDTO.getAddress());
        company.setPhone(companyDTO.getPhone());
        company.setEmail(companyDTO.getEmail());
        company.setSlug(companyDTO.getSlug());
        return company;
    }
} 