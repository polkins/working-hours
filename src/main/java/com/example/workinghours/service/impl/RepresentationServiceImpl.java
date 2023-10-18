package com.example.workinghours.service.impl;

import com.example.workinghours.dto.OpeningAndCloseHours;
import com.example.workinghours.enumeration.DayOfWeek;
import com.example.workinghours.enumeration.Type;
import com.example.workinghours.service.RepresentationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import static com.example.workinghours.enumeration.Type.CLOSE;
import static java.util.stream.Collectors.joining;

@Slf4j
@Service
public class RepresentationServiceImpl implements RepresentationService {
    private static final DateTimeFormatter formatterWithMinutes = DateTimeFormatter.ofPattern("h:m a");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h a");

    /**
     * Represents given map into the readable schedule for all days of a week.
     * Also validates input.
     *
     * @param dayOfWeekMap - map with days of a week with closing and opening hours
     * @return representation like schedule of working hours
     */
    @Override
    public String representDaysOfWeek(Map<DayOfWeek, List<OpeningAndCloseHours>> dayOfWeekMap) {
        log.info("Get dayOfWeekDTOList: " + dayOfWeekMap);
        log.info("Start creating representation");

        checkAllClose(dayOfWeekMap);

        var representations = new ArrayList<String>();
        var daysOfWeek = DayOfWeek.values();

        for (int i = 0; i < daysOfWeek.length; i++) {
            var day = daysOfWeek[i];
            var schedule = getWorkingHours(dayOfWeekMap, day);
            var newSchedule = new ArrayList<>(schedule);

            if (!newSchedule.isEmpty() && newSchedule.get(0).getType() == CLOSE) {
                newSchedule.remove(0);
            }

            if (!newSchedule.isEmpty() && newSchedule.get(newSchedule.size() - 1).getType() == Type.OPEN) {
                var nextDay = i == daysOfWeek.length - 1
                        ? daysOfWeek[0]
                        : daysOfWeek[i + 1];

                var closeTimeOfNextDay = getWorkingHours(dayOfWeekMap, nextDay).get(0);
                var closeTime = closeTimeOfNextDay.getValue();

                newSchedule.add(new OpeningAndCloseHours(CLOSE, closeTime));
            }

            representations.add(represent(newSchedule, day));
        }

        var representation = String.join("\n", representations);
        log.info("Representation is successfully done: " + representation);
        return representation;
    }

    /**
     * Checks that all OpeningAndCloseHours in map do not contain type = CLOSE
     *
     * @param dayOfWeekMap - map with schedules for each day of the week
     */
    private void checkAllClose(Map<DayOfWeek, List<OpeningAndCloseHours>> dayOfWeekMap) {
        Map<DayOfWeek, List<OpeningAndCloseHours>> sortedMap = new TreeMap<>(Comparator.comparingInt(DayOfWeek::ordinal));
        sortedMap.putAll(dayOfWeekMap);

        var types = sortedMap.values()
                .stream()
                .flatMap(List::stream)
                .map(OpeningAndCloseHours::getType)
                .toList();

        if (!types.isEmpty()) {
            if (types.get(0) == types.get(types.size() - 1))
                throw new IllegalArgumentException(types.get(0) + " after " + types.get(types.size() - 1) + " could not be specified!");

            for (int i = 1; i < types.size(); i++) {
                var prev = types.get(i - 1);
                var curr = types.get(i);

                if (curr == prev) {
                    throw new IllegalArgumentException(curr + " after " + prev + " could not be specified!");
                }
            }
        }
        log.info("All elements are valid.");
    }

    /**
     * Represent given hours and day into representation like:
     * Monday: Closed
     * Thursday: 8:15 AM - 1 PM, 2 PM - 6:15 PM, 11:55 PM - 4 PM
     *
     * @param hours list of OpeningAndCloseHours
     * @param day   - day of a week
     * @return - representation of schedule  for one day of a week
     */
    private String represent(List<OpeningAndCloseHours> hours, DayOfWeek day) {
        return hours.isEmpty()
                ? day.getValue() + ": Closed"
                : day.getValue() + ":" + hours.stream().map(this::format).collect(joining()).substring(1);
    }

    /**
     * Format give hours.
     * For opening hours: ,8:15 AM -
     * For closing hours: 8:15 AM
     *
     * @param hours - hours in schedule of the day
     * @return representation of closing or opening hours
     */
    private String format(OpeningAndCloseHours hours) {
        var formattedTime = hours.getValue().getMinute() != 0
                ? formatterWithMinutes.format(hours.getValue())
                : formatter.format(hours.getValue());

        return hours.getType() == Type.OPEN ? ", " + formattedTime + " - " : formattedTime;
    }

    /**
     * @param dayOfWeekMap map with schedule for days of a week
     * @param day          - name of a day of a week
     * @return list of opening and closing hours or throws exception if there is no schedule for the specified day
     */
    private List<OpeningAndCloseHours> getWorkingHours(Map<DayOfWeek, List<OpeningAndCloseHours>> dayOfWeekMap, DayOfWeek day) {
        return Optional.ofNullable(dayOfWeekMap.get(day))
                .orElseThrow(() -> new IllegalArgumentException("No schedule for day: " + day));
    }
}
