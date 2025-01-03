package cn.lzgabel.camunda.converter.cli;

import cn.lzgabel.camunda.converter.cli.util.ObjectMapperComponentSupplier;
import cn.lzgabel.camunda.converter.cli.util.ValidateUtil;
import cn.lzgabel.camunda.converter.transformation.BpmnTransformer;
import cn.lzgabel.camunda.converter.transformation.bean.ProcessDefinitionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;


@Command(
        name = "convert",
        mixinStandardHelpOptions = true,
        description = "Convert json to bpmn for Camunda Platform")
public class ConvertCommand implements Callable<Integer> {

    private static final ObjectMapper MAPPER = ObjectMapperComponentSupplier.getCopy();

    @Option(
            names = {"-i", "--input"},
            paramLabel = "INPUT_DATA_PATH",
            description = "The path to the input data",
            required = true,
            scope = ScopeType.INHERIT)
    private Path inputPath;

    @Option(
            names = {"-o", "--output"},
            paramLabel = "OUT_DATA_PATH",
            description = "The path to the output data",
            required = true,
            scope = ScopeType.INHERIT)
    private Path outputPath;


    @Override
    public Integer call() throws IOException {
        // 校验是否符合规范
        if (!ValidateUtil.validate(inputPath)) {
            return 1;
        }

        final ProcessDefinitionDto processDefinitionDto = MAPPER.readValue(new FileInputStream(inputPath.toFile()), ProcessDefinitionDto.class);
        BpmnTransformer transformer = new BpmnTransformer();
        final BpmnModelInstance modelInstance = transformer.transformDefinitions(processDefinitionDto);
        Files.writeString(outputPath, Bpmn.convertToString(modelInstance));
        return 0;
    }


}
