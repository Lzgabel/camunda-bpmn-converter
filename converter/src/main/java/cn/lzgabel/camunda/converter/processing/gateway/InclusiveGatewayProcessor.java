package cn.lzgabel.camunda.converter.processing.gateway;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.InclusiveGatewayDefinition;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.InclusiveGatewayBuilder;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

/**
 * 〈功能简述〉<br>
 * 〈InclusiveGateway节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class InclusiveGatewayProcessor
    extends AbstractGatewayProcessor<InclusiveGatewayDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(
      AbstractFlowNodeBuilder flowNodeBuilder, InclusiveGatewayDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final InclusiveGatewayBuilder inclusiveGatewayBuilder =
        (InclusiveGatewayBuilder) createInstance(flowNodeBuilder, flowNode);

    String id = flowNode.getNodeId();
    List<BranchDefinition> branchDefinitions = flowNode.getBranchDefinitions();
    if (CollectionUtils.isNotEmpty(flowNode.getBranchDefinitions())) {
      for (BranchDefinition branchDefinition : branchDefinitions) {
        BaseDefinition nextNode = branchDefinition.getNextNode();
        onCreate(moveToNode(inclusiveGatewayBuilder, id), nextNode);
        inclusiveGatewayBuilder
            .getElement()
            .getOutgoing()
            .forEach(
                sequenceFlow ->
                    conditionExpression(sequenceFlow, inclusiveGatewayBuilder, branchDefinition));
      }
    }
    return id;
  }

  private void conditionExpression(
      SequenceFlow sequenceFlow,
      InclusiveGatewayBuilder inclusiveGatewayBuilder,
      BranchDefinition condition) {
    if (condition.isDefault()) {
      inclusiveGatewayBuilder.defaultFlow(sequenceFlow);
    }

    createConditionExpression(sequenceFlow, inclusiveGatewayBuilder, condition);
  }
}
