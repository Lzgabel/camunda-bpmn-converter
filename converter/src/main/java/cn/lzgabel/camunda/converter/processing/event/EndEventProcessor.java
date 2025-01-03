package cn.lzgabel.camunda.converter.processing.event;

import cn.lzgabel.camunda.converter.bean.event.end.EndEventDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;

/**
 * 〈功能简述〉<br>
 * 〈EndEvent节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class EndEventProcessor
    implements BpmnElementProcessor<EndEventDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder builder, EndEventDefinition flowNode) {
    createInstance(builder, flowNode);
    return flowNode.getNodeId();
  }
}
