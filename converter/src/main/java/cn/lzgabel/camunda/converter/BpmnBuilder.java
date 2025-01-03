package cn.lzgabel.camunda.converter;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import cn.lzgabel.camunda.converter.bean.Process;
import cn.lzgabel.camunda.converter.bean.ProcessDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessors;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ProcessBuilder;
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;

/**
 * 〈功能简述〉<br>
 * 〈基于 json 格式自动生成 bpmn 文件〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class BpmnBuilder {

  public static BpmnModelInstance build(String json) {
    return build(ProcessDefinition.of(json));
  }

  public static BpmnModelInstance build(ProcessDefinition processDefinition) {
    if (processDefinition == null) {
      return null;
    }

    try {
      ProcessBuilder executableProcess = Bpmn.createExecutableProcess();

      Process process = processDefinition.getProcess();
      Optional.ofNullable(process.getName())
          .filter(StringUtils::isNotBlank)
          .ifPresent(executableProcess::name);

      Optional.ofNullable(process.getProcessId())
          .filter(StringUtils::isNotBlank)
          .ifPresent(executableProcess::id);

      StartEventBuilder startEventBuilder = executableProcess.startEvent();
      BaseDefinition processNode = processDefinition.getProcessNode();
      BpmnElementProcessor<BaseDefinition, AbstractFlowNodeBuilder> processor =
          BpmnElementProcessors.getProcessor(BpmnElementType.START_EVENT);
      //      String lastNode = processor.onCreate(startEventBuilder, processNode);
      //      processor.moveToNode(startEventBuilder, lastNode).endEvent();

      processor.onCreate(startEventBuilder, processNode);
      BpmnModelInstance modelInstance = startEventBuilder.done();
      Bpmn.validateModel(modelInstance);

      return modelInstance;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("创建失败: e=" + e.getMessage());
    }
  }
}
