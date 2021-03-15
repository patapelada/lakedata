package com.bartock.lakedata.jackson;

import java.io.IOException;

import com.bartock.lakedata.dto.MeasurementTypeDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MeasurementTypeSerializer extends StdSerializer<MeasurementTypeDto> {

    private static final long serialVersionUID = 1L;

    protected MeasurementTypeSerializer() {
        super(MeasurementTypeDto.class);
    }

    @Override
    public void serialize(MeasurementTypeDto value, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {
        gen.writeString(value.getId());
    }
}
