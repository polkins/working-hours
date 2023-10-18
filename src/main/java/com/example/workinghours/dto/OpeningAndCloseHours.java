package com.example.workinghours.dto;

import com.example.workinghours.deserializer.SecondsToLocalTimeDeserializer;
import com.example.workinghours.enumeration.Type;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OpeningAndCloseHours {

    /**
     * close or open
     */
    private Type type;

    /**
     * converted time from seconds
     */
    @JsonDeserialize(using = SecondsToLocalTimeDeserializer.class)
    private LocalTime value;
}
