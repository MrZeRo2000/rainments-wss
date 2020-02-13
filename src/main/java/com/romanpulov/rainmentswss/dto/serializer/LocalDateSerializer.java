package com.romanpulov.rainmentswss.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends StdSerializer<LocalDate> {
    public LocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    public LocalDateSerializer() {
        this(null);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.atStartOfDay().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
