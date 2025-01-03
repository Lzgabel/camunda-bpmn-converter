package cn.lzgabel.camunda.converter.processing.task;

import cn.lzgabel.camunda.converter.bean.task.ServiceTaskDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ServiceTaskBuilder;

/**
 * 〈功能简述〉<br>
 * 〈ServiceTask节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class ServiceTaskProcessor
    implements BpmnElementProcessor<ServiceTaskDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, ServiceTaskDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final ServiceTaskBuilder serviceTaskBuilder =
        (ServiceTaskBuilder) createInstance(flowNodeBuilder, flowNode);

    // set implementation
    Optional.ofNullable(flowNode.getTopic())
        .filter(StringUtils::isNotBlank)
        .ifPresent(serviceTaskBuilder::camundaExternalTask);

    Optional.ofNullable(flowNode.getExpression())
        .filter(StringUtils::isNotBlank)
        .map(
            expression -> {
              serviceTaskBuilder.camundaExpression(expression);
              return flowNode.getResultVariable();
            })
        .filter(StringUtils::isNotBlank)
        .ifPresent(serviceTaskBuilder::camundaResultVariable);

    Optional.ofNullable(flowNode.getDelegateExpression())
        .filter(StringUtils::isNotBlank)
        .ifPresent(serviceTaskBuilder::camundaDelegateExpression);

    Optional.ofNullable(flowNode.getJavaClass())
        .filter(StringUtils::isNotBlank)
        .ifPresent(serviceTaskBuilder::camundaClass);

    return flowNode.getNodeId();
  }
}
