package com.bartock.lakedata.jackson;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {

    private static final long serialVersionUID = 1L;
    private DateTimeFormatter zoneDateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss z"); // TODO: Fix

    protected ZonedDateTimeSerializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        gen.writeString(value.format(zoneDateTimeFormatter));
    }
}
