package com.bartock.lakedata.jackson;

import java.io.IOException;

import com.bartock.lakedata.dto.MeasurementTypeDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MeasurementTypeDeserializer extends StdDeserializer<MeasurementTypeDto> {

    private static final long serialVersionUID = -1694049338874961102L;

    protected MeasurementTypeDeserializer() {
        super(MeasurementTypeDto.class);
    }

    @Override
    public MeasurementTypeDto deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return new MeasurementTypeDto(jp.readValueAs(String.class));
    }

}
