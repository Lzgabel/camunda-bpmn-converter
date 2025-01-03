package cn.lzgabel.camunda.converter.processing.event;

import cn.lzgabel.camunda.converter.bean.event.EventType;
import cn.lzgabel.camunda.converter.bean.event.intermediate.IntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.MessageIntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.TimerIntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.IntermediateCatchEventBuilder;

/**
 * 〈功能简述〉<br>
 * 〈IntermediateCatchEvent节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class IntermediateCatchEventProcessor
    implements BpmnElementProcessor<IntermediateCatchEventDefinition, AbstractFlowNodeBuilder> {

  @Override
  public String onComplete(
      final AbstractFlowNodeBuilder flowNodeBuilder,
      final IntermediateCatchEventDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    return createIntermediateCatchEvent(flowNodeBuilder, flowNode);
  }

  private String createIntermediateCatchEvent(
      final AbstractFlowNodeBuilder flowNodeBuilder,
      final IntermediateCatchEventDefinition flowNode) {
    final String eventType = flowNode.getEventType();
    final IntermediateCatchEventBuilder intermediateCatchEventBuilder =
        (IntermediateCatchEventBuilder) createInstance(flowNodeBuilder, flowNode);
    return switch (EventType.from(eventType)) {
      case TIMER -> createTimerIntermediateCatchEvent(intermediateCatchEventBuilder, flowNode);
      case MESSAGE -> createMessageIntermediateCatchEvent(intermediateCatchEventBuilder, flowNode);
      default -> throw new IllegalArgumentException(String.format("暂不支持: %s 类型", eventType));
    };
  }

  private String createMessageIntermediateCatchEvent(
      final IntermediateCatchEventBuilder intermediateCatchEventBuilder,
      final IntermediateCatchEventDefinition flowNode) {
    MessageIntermediateCatchEventDefinition message =
        (MessageIntermediateCatchEventDefinition) flowNode;
    intermediateCatchEventBuilder.message(message.getMessageName());
    return flowNode.getNodeId();
  }

  private String createTimerIntermediateCatchEvent(
      final IntermediateCatchEventBuilder intermediateCatchEventBuilder,
      final IntermediateCatchEventDefinition flowNode) {

    final TimerIntermediateCatchEventDefinition timer =
        (TimerIntermediateCatchEventDefinition) flowNode;

    final String expression = timer.getTimerDefinitionExpression();
    switch (timer.getTimerDefinitionType()) {
      case "date" -> intermediateCatchEventBuilder.timerWithDate(expression);
      case "duration" -> intermediateCatchEventBuilder.timerWithDuration(expression);
      default ->
          throw new IllegalArgumentException(
              "未知 timer definition type: " + timer.getTimerDefinitionType());
    }

    return flowNode.getNodeId();
  }
}
