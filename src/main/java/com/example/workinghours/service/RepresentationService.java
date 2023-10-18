package com.example.workinghours.service;

import com.example.workinghours.dto.OpeningAndCloseHours;
import com.example.workinghours.enumeration.DayOfWeek;

import java.util.List;
import java.util.Map;

/**
 * Service for creating representation of working hours.
 */
public interface RepresentationService {
    String representDaysOfWeek(Map<DayOfWeek, List<OpeningAndCloseHours>> dayOfWeekMap);
}
