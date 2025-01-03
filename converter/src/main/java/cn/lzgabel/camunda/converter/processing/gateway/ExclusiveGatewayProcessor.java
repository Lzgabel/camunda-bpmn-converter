package cn.lzgabel.camunda.converter.processing.gateway;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.ExclusiveGatewayDefinition;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ExclusiveGatewayBuilder;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

/**
 * 〈功能简述〉<br>
 * 〈ExclusiveGateway节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class ExclusiveGatewayProcessor
    extends AbstractGatewayProcessor<ExclusiveGatewayDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(
      AbstractFlowNodeBuilder flowNodeBuilder, ExclusiveGatewayDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {

    final ExclusiveGatewayBuilder exclusiveGatewayBuilder =
        (ExclusiveGatewayBuilder) createInstance(flowNodeBuilder, flowNode);

    String id = flowNode.getNodeId();
    List<BranchDefinition> branchDefinitions = flowNode.getBranchDefinitions();
    if (CollectionUtils.isNotEmpty(flowNode.getBranchDefinitions())) {
      for (BranchDefinition branchDefinition : branchDefinitions) {
        BaseDefinition nextNode = branchDefinition.getNextNode();
        onCreate(moveToNode(exclusiveGatewayBuilder, id), nextNode);
        exclusiveGatewayBuilder
            .getElement()
            .getOutgoing()
            .forEach(
                sequenceFlow ->
                    conditionExpression(sequenceFlow, exclusiveGatewayBuilder, branchDefinition));
      }
    }
    return id;
  }

  private void conditionExpression(
      SequenceFlow sequenceFlow,
      ExclusiveGatewayBuilder exclusiveGatewayBuilder,
      BranchDefinition condition) {
    if (condition.isDefault()) {
      exclusiveGatewayBuilder.defaultFlow(sequenceFlow);
      return;
    }
    createConditionExpression(sequenceFlow, exclusiveGatewayBuilder, condition);
  }
}
