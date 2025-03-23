package com.barberplusapi.demo.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalTime;

@Converter
public class IntervalsConverter implements AttributeConverter<List<Map<String, LocalTime>>, String> {
    
    private final ObjectMapper objectMapper;
    
    public IntervalsConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    @Override
    public String convertToDatabaseColumn(List<Map<String, LocalTime>> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
    
    @Override
    public List<Map<String, LocalTime>> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(dbData, new TypeReference<List<Map<String, LocalTime>>>() {});
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }
}