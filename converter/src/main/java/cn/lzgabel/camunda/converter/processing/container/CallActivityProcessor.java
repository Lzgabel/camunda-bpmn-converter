package cn.lzgabel.camunda.converter.processing.container;

import cn.lzgabel.camunda.converter.bean.subprocess.CallActivityDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.CallActivityBuilder;

/**
 * 〈功能简述〉<br>
 * 〈CallActivity节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class CallActivityProcessor
    implements BpmnElementProcessor<CallActivityDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, CallActivityDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final CallActivityBuilder callActivityBuilder =
        (CallActivityBuilder) createInstance(flowNodeBuilder, flowNode);
    callActivityBuilder.calledElement(flowNode.getCalledElement());
    return flowNode.getNodeId();
  }
}
