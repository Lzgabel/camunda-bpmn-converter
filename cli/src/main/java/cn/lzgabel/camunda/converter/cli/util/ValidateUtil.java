package cn.lzgabel.camunda.converter.cli.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public class ValidateUtil {

    private static final ObjectMapper MAPPER = ObjectMapperComponentSupplier.getCopy();

    public static boolean validate(Path inputPath) throws IOException {
        JsonNode schemaJsonNode = getJsonNodeFromClasspath("/schema.json");
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
        JsonSchema schema = factory.getSchema(schemaJsonNode);

        JsonNode dataJsonNode = MAPPER.readTree(inputPath.toFile());
        Set<ValidationMessage> errors = schema.validate(dataJsonNode);
        if (!errors.isEmpty()) {
            errors.forEach(vm -> System.out.println(vm.getMessage()));
            return false;
        }
        return true;
    }

    public static JsonNode getJsonNodeFromClasspath(String name) throws IOException {
        JsonNode jsonNode;
        try (InputStream inputStream = ValidateUtil.class.getResourceAsStream(name)) {
            jsonNode = MAPPER.readTree(inputStream);
        }
        return jsonNode;
    }

}
