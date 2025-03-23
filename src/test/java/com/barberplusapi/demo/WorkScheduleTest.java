package com.barberplusapi.demo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.barberplusapi.demo.models.WorkScheduleTeste;

public class WorkScheduleTest {

    @Test
    public void testGenerateTimeSlots() {
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        WorkScheduleTeste workSchedule = new WorkScheduleTeste();
        workSchedule.setDayOfWeek(DayOfWeek.SUNDAY);
        workSchedule.setStartTime(startTime);
        workSchedule.setEndTime(endTime);

        workSchedule.setIntervals(Arrays.asList(
            Map.of("startat", LocalTime.of(12, 0), "endAt", LocalTime.of(14, 0))
        ));

        List<LocalTime> timeSlots = workSchedule.generateTimeSlots();
        System.out.println(timeSlots);
    }
}
