package cn.lzgabel.camunda.converter.processing.task;

import cn.lzgabel.camunda.converter.bean.task.ManualTaskDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;

/**
 * 〈功能简述〉<br>
 * 〈ManualTask节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class ManualTaskProcessor
    implements BpmnElementProcessor<ManualTaskDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, ManualTaskDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    createInstance(flowNodeBuilder, flowNode);
    return flowNode.getNodeId();
  }
}
