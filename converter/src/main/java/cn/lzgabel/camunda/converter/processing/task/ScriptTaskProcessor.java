package cn.lzgabel.camunda.converter.processing.task;

import cn.lzgabel.camunda.converter.bean.task.ScriptTaskDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ScriptTaskBuilder;

/**
 * 〈功能简述〉<br>
 * 〈ScriptTask节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class ScriptTaskProcessor
    implements BpmnElementProcessor<ScriptTaskDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, ScriptTaskDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    String scriptFormat = flowNode.getScriptFormat();
    String scriptText = flowNode.getScriptText();
    String resultVariable = flowNode.getResultVariable();

    // 创建 ScriptTask
    final ScriptTaskBuilder scriptTaskBuilder =
        (ScriptTaskBuilder) createInstance(flowNodeBuilder, flowNode);

    scriptTaskBuilder
        .scriptFormat(scriptFormat)
        .camundaResultVariable(resultVariable)
        .scriptText(scriptText);

    return flowNode.getNodeId();
  }
}
