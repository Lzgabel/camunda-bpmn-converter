package cn.lzgabel.camunda.converter.processing.gateway;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.ParallelGatewayDefinition;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ParallelGatewayBuilder;

/**
 * 〈功能简述〉<br>
 * 〈ParallelGateway节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class ParallelGatewayProcessor
    extends AbstractGatewayProcessor<ParallelGatewayDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(
      AbstractFlowNodeBuilder flowNodeBuilder, ParallelGatewayDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final ParallelGatewayBuilder parallelGatewayBuilder =
        (ParallelGatewayBuilder) createInstance(flowNodeBuilder, flowNode);

    String id = flowNode.getNodeId();
    List<BranchDefinition> branchDefinitions = flowNode.getBranchDefinitions();
    if (CollectionUtils.isNotEmpty(branchDefinitions)) {
      for (BranchDefinition branchDefinition : branchDefinitions) {
        BaseDefinition nextNode = branchDefinition.getNextNode();
        onCreate(moveToNode(parallelGatewayBuilder, id), nextNode);
      }
    }

    return id;
  }
}
