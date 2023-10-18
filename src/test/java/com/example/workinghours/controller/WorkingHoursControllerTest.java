package com.example.workinghours.controller;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class WorkingHoursControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Test
    void happyPath() throws Exception {
        // when
        var resp = mvc.perform(MockMvcRequestBuilders
                        .post("/working-hours")
                        .content(fromFile("happy_path.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(resp).isEqualTo(
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
    void happyPathWriteTypeAndDayOfAWeekInDifferentCases() throws Exception {
        // when
        var resp = mvc.perform(MockMvcRequestBuilders
                        .post("/working-hours")
                        .content(fromFile("happy_path_2.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(resp).isEqualTo(
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
    void empty() throws Exception {
        // when
        var resp = mvc.perform(MockMvcRequestBuilders
                        .post("/working-hours")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(resp).isEqualTo(EMPTY);
    }

    @Test
    void wrongNameOfDayOfWeek() throws Exception {
        // when
        var resp = mvc.perform(MockMvcRequestBuilders
                        .post("/working-hours")
                        .content(fromFile("wrong_name_day_of_week.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(resp).contains("Invalid dayOfWeek value: monday123 you should specify" +
                " day like this: monday, tuesday, wednesday, thursday, friday, saturday, sunday");
    }

    @Test
    void bigAmountOfTime() throws Exception {
        // when
        var resp = mvc.perform(MockMvcRequestBuilders
                        .post("/working-hours")
                        .content(fromFile("big_amount_of_time.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(resp).contains("Please, try to fix your input data: \\nJSON parse error: Wrong format of the value: value 100000000000");
    }

    @Test
    void wrongTypeOfSchedule() throws Exception {
        // when
        var resp = mvc.perform(MockMvcRequestBuilders
                        .post("/working-hours")
                        .content(fromFile("wrong_type_of_schedule.json"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        Assertions.assertThat(resp).contains("Invalid type value: OPEN1234 you should specify type like this: open, close.");
    }

    @SneakyThrows
    private byte[] fromFile(String path) {
        return new ClassPathResource(path).getInputStream().readAllBytes();
    }
}