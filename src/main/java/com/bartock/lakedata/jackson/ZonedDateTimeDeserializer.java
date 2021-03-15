package com.bartock.lakedata.jackson;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    private static final long serialVersionUID = 1L;
    private DateTimeFormatter zoneDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss z"); // TODO: Fix

    protected ZonedDateTimeDeserializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return ZonedDateTime.parse(jp.readValueAs(String.class), zoneDateTimeFormatter);
    }

}
