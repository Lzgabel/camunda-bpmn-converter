package cn.lzgabel.camunda.converter.processing.gateway;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author lizhi
 * @since 1.0.0
 */
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.GatewayDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.instance.ConditionExpression;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

public abstract class AbstractGatewayProcessor<
        E extends GatewayDefinition, T extends AbstractFlowNodeBuilder>
    implements BpmnElementProcessor<E, T> {

  protected void createConditionExpression(
      SequenceFlow sequenceFlow,
      AbstractFlowNodeBuilder flowNodeBuilder,
      BranchDefinition condition) {
    String nodeName = condition.getNodeName();
    String expression = condition.getConditionExpression();
    if (StringUtils.isBlank(sequenceFlow.getName()) && StringUtils.isNotBlank(nodeName)) {
      sequenceFlow.setName(nodeName);
    }
    // 设置条件表达式
    if (Objects.isNull(sequenceFlow.getConditionExpression())
        && StringUtils.isNotBlank(expression)) {
      ConditionExpression conditionExpression =
          createInstance(flowNodeBuilder, ConditionExpression.class);
      conditionExpression.setTextContent(expression);
      sequenceFlow.setConditionExpression(conditionExpression);
    }
  }

  private <T extends ModelElementInstance> T createInstance(
      AbstractFlowNodeBuilder<?, ?> abstractFlowNodeBuilder, Class<T> clazz) {
    return abstractFlowNodeBuilder.getElement().getModelInstance().newInstance(clazz);
  }
}
