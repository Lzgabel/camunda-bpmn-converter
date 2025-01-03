package cn.lzgabel.camunda.converter.processing.task;

import cn.lzgabel.camunda.converter.bean.DecisionRefBindingType;
import cn.lzgabel.camunda.converter.bean.task.BusinessRuleTaskDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.BusinessRuleTaskBuilder;

/**
 * 〈功能简述〉<br>
 * 〈BusinessRuleTask节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class BusinessRuleTaskProcessor
    implements BpmnElementProcessor<BusinessRuleTaskDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(
      AbstractFlowNodeBuilder flowNodeBuilder, BusinessRuleTaskDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    String decisionRef = flowNode.getDecisionRef();
    String decisionRefBinding = flowNode.getDecisionRefBinding();
    String decisionRefTenantId = flowNode.getDecisionRefTenantId();
    String resultVariable = flowNode.getResultVariable();

    // 创建 businessRuleTask
    final BusinessRuleTaskBuilder businessRuleTaskBuilder =
        (BusinessRuleTaskBuilder) createInstance(flowNodeBuilder, flowNode);
    businessRuleTaskBuilder.camundaDecisionRef(decisionRef).camundaResultVariable(resultVariable);

    if (DecisionRefBindingType.VERSION.isEquals(decisionRefBinding)) {
      businessRuleTaskBuilder.camundaDecisionRefVersion(flowNode.getDecisionRefVersion());
    }

    if (DecisionRefBindingType.VERSION_TAG.isEquals(decisionRefBinding)) {
      businessRuleTaskBuilder.camundaDecisionRefVersionTag(flowNode.getDecisionRefVersionTag());
    }

    if (StringUtils.isNotBlank(decisionRefTenantId)) {
      businessRuleTaskBuilder.camundaDecisionRefTenantId(decisionRefTenantId);
    }
    return flowNode.getNodeId();
  }
}
