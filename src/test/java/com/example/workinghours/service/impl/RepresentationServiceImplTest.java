package com.example.workinghours.service.impl;

import com.example.workinghours.dto.OpeningAndCloseHours;
import com.example.workinghours.enumeration.DayOfWeek;
import com.example.workinghours.enumeration.Type;
import com.example.workinghours.service.RepresentationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.example.workinghours.enumeration.DayOfWeek.FRIDAY;
import static com.example.workinghours.enumeration.DayOfWeek.MONDAY;
import static com.example.workinghours.enumeration.DayOfWeek.SATURDAY;
import static com.example.workinghours.enumeration.DayOfWeek.SUNDAY;
import static com.example.workinghours.enumeration.DayOfWeek.THURSDAY;
import static com.example.workinghours.enumeration.DayOfWeek.TUESDAY;
import static com.example.workinghours.enumeration.DayOfWeek.WEDNESDAY;
import static java.time.LocalTime.of;
import static java.time.LocalTime.ofSecondOfDay;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RepresentationServiceImplTest {

    private final RepresentationService instance = new RepresentationServiceImpl();

    @Test
    void happyPathFromTask() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(75600)))
        );

        // given
        var output = instance.representDaysOfWeek(mapToTest);

        // then
        Assertions.assertThat(output).isEqualTo(
                "Monday: Closed\n" +
                        "Tuesday: 10 AM - 6 PM\n" +
                        "Wednesday: Closed\n" +
                        "Thursday: 10:30 AM - 6 PM\n" +
                        "Friday: 10 AM - 1 AM\n" +
                        "Saturday: 10 AM - 1 AM\n" +
                        "Sunday: 12 PM - 9 PM"
        );
    }

    @Test
    void noCosingTimeBeforeAllClosedDay() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(75600)))
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void noCosingTimeInNextWorkingDay() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(75600)))
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void allOpen() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(75600)))
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void twoOpenFromSundayToMonday() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(75600)))
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void twoCloseFromSundayToMonday() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(75600)))
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void twoClose() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(75600)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(80000))
                )
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void twoOpen() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.OPEN, ofSecondOfDay(75600)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(80000))
                )
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void allClosedWithoutError() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, emptyList(),
                WEDNESDAY, emptyList(),
                THURSDAY, emptyList(),
                FRIDAY, emptyList(),
                SATURDAY, emptyList(),
                SUNDAY, emptyList());

        // given
        var output = instance.representDaysOfWeek(mapToTest);

        // then
        Assertions.assertThat(output).isEqualTo(
                "Monday: Closed\n" +
                        "Tuesday: Closed\n" +
                        "Wednesday: Closed\n" +
                        "Thursday: Closed\n" +
                        "Friday: Closed\n" +
                        "Saturday: Closed\n" +
                        "Sunday: Closed"
        );
    }

    @Test
    void allClose() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, emptyList(),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(36000)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(37800)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(64800))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(36000))
                ),
                SATURDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(3600)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(36000))
                ),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(43200)),
                        new OpeningAndCloseHours(Type.CLOSE, ofSecondOfDay(75600)))
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }

    @Test
    void happyPathWhenMondayIsClosedBecauseSundayWorksTillMonday() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, of(7, 25))
                ),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, of(9, 30)),
                        new OpeningAndCloseHours(Type.CLOSE, of(23, 25))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, of(8, 15)),
                        new OpeningAndCloseHours(Type.CLOSE, of(13, 0)),
                        new OpeningAndCloseHours(Type.OPEN, of(14, 0)),
                        new OpeningAndCloseHours(Type.CLOSE, of(18, 15)),
                        new OpeningAndCloseHours(Type.OPEN, of(23, 55))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, of(16, 0)),
                        new OpeningAndCloseHours(Type.OPEN, of(20, 22)),
                        new OpeningAndCloseHours(Type.CLOSE, of(22, 56))
                ),
                SATURDAY, emptyList(),
                SUNDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, of(20, 0))
                )
        );

        // given
        var output = instance.representDaysOfWeek(mapToTest);

        // then
        Assertions.assertThat(output).isEqualTo(
                "Monday: Closed\n" +
                        "Tuesday: 9:30 AM - 11:25 PM\n" +
                        "Wednesday: Closed\n" +
                        "Thursday: 8:15 AM - 1 PM, 2 PM - 6:15 PM, 11:55 PM - 4 PM\n" +
                        "Friday: 8:22 PM - 10:56 PM\n" +
                        "Saturday: Closed\n" +
                        "Sunday: 8 PM - 7:25 AM"
        );
    }

    @Test
    void isClosedOnTuesdayButInApiWeDoNotSeeIt() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, of(9, 30))
                ),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, of(23, 25))
                ),
                WEDNESDAY, emptyList(),
                THURSDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, of(8, 15)),
                        new OpeningAndCloseHours(Type.CLOSE, of(13, 0)),
                        new OpeningAndCloseHours(Type.OPEN, of(14, 0)),
                        new OpeningAndCloseHours(Type.CLOSE, of(18, 15)),
                        new OpeningAndCloseHours(Type.OPEN, of(23, 55))
                ),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, of(16, 0)),
                        new OpeningAndCloseHours(Type.OPEN, of(20, 22)),
                        new OpeningAndCloseHours(Type.CLOSE, of(22, 56))
                ),
                SATURDAY, emptyList(),
                SUNDAY, emptyList()
        );

        // given
        var output = instance.representDaysOfWeek(mapToTest);

        // then
        Assertions.assertThat(output).isEqualTo(
                "Monday: 9:30 AM - 11:25 PM\n" +
                        "Tuesday: Closed\n" +
                        "Wednesday: Closed\n" +
                        "Thursday: 8:15 AM - 1 PM, 2 PM - 6:15 PM, 11:55 PM - 4 PM\n" +
                        "Friday: 8:22 PM - 10:56 PM\n" +
                        "Saturday: Closed\n" +
                        "Sunday: Closed"
        );
    }


    @Test
    void oneDayMissed() {
        // when
        Map<DayOfWeek, List<OpeningAndCloseHours>> mapToTest = Map.of(
                MONDAY, List.of(
                        new OpeningAndCloseHours(Type.OPEN, of(9, 30))
                ),
                TUESDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, of(23, 25))
                ),
                WEDNESDAY, emptyList(),
                FRIDAY, List.of(
                        new OpeningAndCloseHours(Type.CLOSE, of(16, 0)),
                        new OpeningAndCloseHours(Type.OPEN, of(20, 22)),
                        new OpeningAndCloseHours(Type.CLOSE, of(22, 56))
                ),
                SATURDAY, emptyList(),
                SUNDAY, emptyList()
        );

        // then
        assertThrows(IllegalArgumentException.class, () -> instance.representDaysOfWeek(mapToTest));
    }
}