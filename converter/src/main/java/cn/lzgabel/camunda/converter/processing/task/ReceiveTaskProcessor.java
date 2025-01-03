package cn.lzgabel.camunda.converter.processing.task;

import cn.lzgabel.camunda.converter.bean.task.ReceiveTaskDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.ReceiveTaskBuilder;

/**
 * 〈功能简述〉<br>
 * 〈ReceiveTask节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class ReceiveTaskProcessor
    implements BpmnElementProcessor<ReceiveTaskDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, ReceiveTaskDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    String messageName = flowNode.getMessageName();

    // 创建 ReceiveTask
    final ReceiveTaskBuilder receiveTaskBuilder =
        (ReceiveTaskBuilder) createInstance(flowNodeBuilder, flowNode);
    receiveTaskBuilder.message(messageName);
    return flowNode.getNodeId();
  }
}
