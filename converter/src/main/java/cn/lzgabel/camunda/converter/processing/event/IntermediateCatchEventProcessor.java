package cn.lzgabel.camunda.converter.processing.event;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.IntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.MessageIntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.TimerIntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.start.EventType;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
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
    // 创建中级捕获节点
    final String id = createIntermediateCatchEvent(flowNodeBuilder, flowNode);

    // 如果还有后续任务，则遍历创建后续任务
    final BaseDefinition nextNode = flowNode.getNextNode();
    if (Objects.nonNull(nextNode)) {
      return onCreate(moveToNode(flowNodeBuilder, id), nextNode);
    } else {
      return id;
    }
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
    String messageName = message.getMessageName();
    return intermediateCatchEventBuilder.message(messageName).getElement().getId();
  }

  private String createTimerIntermediateCatchEvent(
      final IntermediateCatchEventBuilder intermediateCatchEventBuilder,
      final IntermediateCatchEventDefinition flowNode) {

    final TimerIntermediateCatchEventDefinition timer =
        (TimerIntermediateCatchEventDefinition) flowNode;

    final String expression = timer.getTimerDefinitionExpression();
    return switch (timer.getTimerDefinitionType()) {
      case "date" -> intermediateCatchEventBuilder.timerWithDate(expression).getElement().getId();
      case "duration" ->
          intermediateCatchEventBuilder.timerWithDuration(expression).getElement().getId();
      default ->
          throw new IllegalArgumentException(
              "未知 timer definition type: " + timer.getTimerDefinitionType());
    };
  }
}
