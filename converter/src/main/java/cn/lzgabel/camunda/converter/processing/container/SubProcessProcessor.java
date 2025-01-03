package cn.lzgabel.camunda.converter.processing.container;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.subprocess.SubProcessDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.EmbeddedSubProcessBuilder;
import org.camunda.bpm.model.bpmn.builder.SubProcessBuilder;

/**
 * 〈功能简述〉<br>
 * 〈SubProcess节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class SubProcessProcessor
    implements BpmnElementProcessor<SubProcessDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, SubProcessDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final SubProcessBuilder subProcessBuilder =
        (SubProcessBuilder) createInstance(flowNodeBuilder, flowNode);
    EmbeddedSubProcessBuilder embeddedSubProcessBuilder = subProcessBuilder.embeddedSubProcess();

    // 子流程内部创建开始
    // StartEventBuilder startEventBuilder = embeddedSubProcessBuilder.startEvent().id()
    subProcessBuilder.getElement().setName(flowNode.getNodeName());
    // String lastNode = startEventBuilder.getElement().getId();

    // 创建子流程节点
    BaseDefinition childNode = flowNode.getChildNode();
    // 创建默认开始节点
    embeddedSubProcessBuilder.startEvent().id(childNode.getNodeId()).name(childNode.getNodeName());
    onCreate(moveToNode(subProcessBuilder, childNode.getNodeId()), childNode.getNextNode());

    // 子流程内部创建结束
    // moveToNode(startEventBuilder, lastNode).endEvent();

    return flowNode.getNodeId();
  }
}
