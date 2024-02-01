package com.sparta.secureschedulerappserver.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ScheduleListResponseDto {
    private Map<String, List<ScheduleResponseDto>> scheduleByName;

    public ScheduleListResponseDto(Map<String, List<ScheduleResponseDto>> scheduleByName) {
        this.scheduleByName = scheduleByName.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(scheduleResponseDto -> new ScheduleResponseDto(scheduleResponseDto))
                    .collect(Collectors.toList())
            ));
    }
}

