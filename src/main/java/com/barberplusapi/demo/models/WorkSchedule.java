package com.barberplusapi.demo.models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

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
public class WorkSchedule {
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    
    private LocalTime startTime;
    private LocalTime endTime;

    /**
     * Gera uma lista de horários em intervalos de 10 minutos do início ao fim do expediente
     * @return Lista de horários formatados como strings (ex: "08:00", "08:10", etc.)
     */
    public List<String> generateTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        
        // Verificar se os horários de início e fim estão definidos
        if (startTime == null || endTime == null) {
            return timeSlots;
        }
        
        LocalTime currentTime = startTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        
        // Adicionar o horário inicial
        timeSlots.add(currentTime.format(formatter));
        
        // Gerar intervalos de 10 minutos até o horário final
        while (currentTime.isBefore(endTime)) {
            currentTime = currentTime.plusMinutes(10);
            
            // Se passou do horário final, parar
            if (currentTime.isAfter(endTime)) {
                timeSlots.add(endTime.format(formatter));
                break;
            }
            
            timeSlots.add(currentTime.format(formatter));
        }
        
        // Garantir que o horário final esteja na lista
        if (!timeSlots.contains(endTime.format(formatter))) {
            timeSlots.add(endTime.format(formatter));
        }
        
        return timeSlots;
    }
}