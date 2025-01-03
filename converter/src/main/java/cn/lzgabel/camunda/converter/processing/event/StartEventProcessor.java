package cn.lzgabel.camunda.converter.processing.event;

import cn.lzgabel.camunda.converter.bean.event.EventType;
import cn.lzgabel.camunda.converter.bean.event.start.*;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import org.camunda.bpm.model.bpmn.builder.StartEventBuilder;

/**
 * 〈功能简述〉<br>
 * 〈StartEvent节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class StartEventProcessor
    implements BpmnElementProcessor<StartEventDefinition, StartEventBuilder> {

  @Override
  public String onComplete(StartEventBuilder startEventBuilder, StartEventDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    return createStartEvent(startEventBuilder, flowNode);
  }

  private String createStartEvent(
      final StartEventBuilder startEventBuilder, final StartEventDefinition flowNode) {
    startEventBuilder.id(flowNode.getNodeId()).name(flowNode.getNodeName());
    // 事件类型 timer/message 默认：none
    return switch (EventType.from(flowNode.getEventType())) {
      case TIMER -> createTimerStartEvent(startEventBuilder, (TimerStartEventDefinition) flowNode);
      case MESSAGE ->
          createMessageStartEvent(startEventBuilder, (MessageStartEventDefinition) flowNode);
      default -> flowNode.getNodeId();
    };
  }

  private String createMessageStartEvent(
      final StartEventBuilder startEventBuilder, final MessageStartEventDefinition flowNode) {
    startEventBuilder.message(flowNode.getMessageName());
    return flowNode.getNodeId();
  }

  private String createTimerStartEvent(
      final StartEventBuilder startEventBuilder, final TimerStartEventDefinition flowNode) {
    switch (TimerDefinitionType.from(flowNode.getTimerDefinitionType())) {
      case DATE -> {
        String timerDefinition = flowNode.getTimerDefinitionExpression();
        startEventBuilder.timerWithDate(timerDefinition);
      }
      case CYCLE -> {
        String timerDefinition = flowNode.getTimerDefinitionExpression();
        startEventBuilder.timerWithCycle(timerDefinition);
      }
      case DURATION -> {
        String timerDefinition = flowNode.getTimerDefinitionExpression();
        startEventBuilder.timerWithDuration(timerDefinition);
      }
    }
    return flowNode.getNodeId();
  }
}
