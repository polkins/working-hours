package com.example.workinghours.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalTime;

/**
 * Deserializer for seconds in API.
 * Convert seconds into LocalTime.
 */
@Slf4j
public class SecondsToLocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            long seconds = jsonParser.getLongValue();
            return LocalTime.ofSecondOfDay(seconds);
        } catch (Exception e) {
            log.error("Wrong format of the value: {} ", jsonParser.getText(), e);
            throw new IllegalArgumentException("Wrong format of the value: " +
                    jsonParser.getCurrentName() + " " + jsonParser.getText());
        }
    }
}
