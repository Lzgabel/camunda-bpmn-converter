package cn.lzgabel.camunda.converter.cli.util;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class ObjectMapperComponentSupplier {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
            .build();

    private ObjectMapperComponentSupplier() {
    }

    public static ObjectMapper getCopy() {
        return MAPPER.copy();
    }
}
