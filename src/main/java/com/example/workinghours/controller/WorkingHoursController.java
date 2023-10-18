package com.example.workinghours.controller;

import com.example.workinghours.dto.OpeningAndCloseHours;
import com.example.workinghours.enumeration.DayOfWeek;
import com.example.workinghours.service.RepresentationService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/working-hours")
public class WorkingHoursController {

    private final RepresentationService representationService;

    /**
     * Create readable representation of working hours.
     */
    @NotNull
    @PostMapping
    public String representation(@NotNull @RequestBody Map<DayOfWeek, List<OpeningAndCloseHours>> dayOfWeekMap) {

        if (dayOfWeekMap.isEmpty()) {
            log.info("No days of a week were indicated");
            return EMPTY;
        }

        return representationService.representDaysOfWeek(dayOfWeekMap);
    }
}
