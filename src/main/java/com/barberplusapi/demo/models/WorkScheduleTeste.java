package com.barberplusapi.demo.models;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkScheduleTeste {
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = IntervalsConverter.class)
    private List<Map<String, LocalTime>> intervals = new ArrayList<>();


    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        validateTimeRange(this.startTime, this.endTime);
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        validateTimeRange(this.startTime, this.endTime);
    }

    
    private void validateTimeRange(LocalTime startAt, LocalTime endAt) {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
    }


    public List<LocalTime> generateTimeSlots() {
        List<LocalTime> timeSlots = new ArrayList<>();
        LocalTime current = startTime;
        
        while (!current.isAfter(endTime)) {
            timeSlots.add(current);
            current = current.plus(Duration.ofMinutes(10));
        }

        for(Map<String, LocalTime> interval : intervals) {
            timeSlots.removeIf(slot -> slot.isAfter(interval.get("startAt")) && slot.isBefore(interval.get("endAt")));
        }
        
        return timeSlots;
    }

}