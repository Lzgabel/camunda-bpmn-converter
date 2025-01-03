package cn.lzgabel.camunda.converter.cli;

import cn.lzgabel.camunda.converter.cli.util.ValidateUtil;
import cn.lzgabel.camunda.converter.transformation.BpmnTransformer;
import cn.lzgabel.camunda.converter.transformation.bean.ProcessDefinitionDto;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Callable;


@Command(
        name = "validate",
        mixinStandardHelpOptions = true,
        description = "验证输入数据")
public class ValidateCommand implements Callable<Integer> {

    @Option(
            names = {"-i", "--input"},
            paramLabel = "INPUT_DATA_PATH",
            description = "The path to the input data",
            required = true,
            scope = ScopeType.INHERIT)
    private Path inputPath;

    @Override
    public Integer call() throws IOException {
        // 校验是否符合规范
        if (ValidateUtil.validate(inputPath)) {
            System.out.println("ok");
            return 0;
        }
        return 1;
    }


}
