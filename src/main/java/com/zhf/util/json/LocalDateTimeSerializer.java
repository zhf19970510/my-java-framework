package com.zhf.util.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> implements ContextualSerializer {

    public static LocalDateTimeSerializer INSTANCE = new LocalDateTimeSerializer();

    private DateTimeFormatter formatter;

    private final boolean useTimestamp;

    public LocalDateTimeSerializer() {
        this(true, null);
    }

    public LocalDateTimeSerializer(DateTimeFormatter formatter) {
        this(false, formatter);
    }

    public LocalDateTimeSerializer(boolean useTimestamp, DateTimeFormatter formatter) {
        this.useTimestamp = useTimestamp;
        this.formatter = formatter;
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (useTimestamp) {
            gen.writeNumber(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } else {
            gen.writeString(value.format(formatter));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            JsonFormat.Value format = prov.getAnnotationIntrospector().findFormat(property.getMember());
            if (format != null) {
                if (format.getShape().isNumeric()) {
                    return new LocalDateTimeSerializer();
                }
                if (format.hasPattern()) {
                    return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(format.getPattern()));
                }
            }
        }
        return null;
    }
}
