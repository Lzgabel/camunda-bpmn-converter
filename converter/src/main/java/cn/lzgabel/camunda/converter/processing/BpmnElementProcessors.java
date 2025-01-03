package cn.lzgabel.camunda.converter.processing;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import cn.lzgabel.camunda.converter.processing.container.CallActivityProcessor;
import cn.lzgabel.camunda.converter.processing.container.SubProcessProcessor;
import cn.lzgabel.camunda.converter.processing.event.EndEventProcessor;
import cn.lzgabel.camunda.converter.processing.event.IntermediateCatchEventProcessor;
import cn.lzgabel.camunda.converter.processing.event.StartEventProcessor;
import cn.lzgabel.camunda.converter.processing.gateway.ExclusiveGatewayProcessor;
import cn.lzgabel.camunda.converter.processing.gateway.InclusiveGatewayProcessor;
import cn.lzgabel.camunda.converter.processing.gateway.ParallelGatewayProcessor;
import cn.lzgabel.camunda.converter.processing.task.*;
import java.util.EnumMap;
import java.util.Map;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;

public final class BpmnElementProcessors {

  private static final Map<BpmnElementType, BpmnElementProcessor<?, ?>> processors =
      new EnumMap<>(BpmnElementType.class);

  static {
    // tasks
    processors.put(BpmnElementType.BUSINESS_RULE_TASK, new BusinessRuleTaskProcessor());
    processors.put(BpmnElementType.SCRIPT_TASK, new ScriptTaskProcessor());
    processors.put(BpmnElementType.SERVICE_TASK, new ServiceTaskProcessor());
    processors.put(BpmnElementType.USER_TASK, new UserTaskProcessor());
    processors.put(BpmnElementType.RECEIVE_TASK, new ReceiveTaskProcessor());
    processors.put(BpmnElementType.MANUAL_TASK, new ManualTaskProcessor());

    // gateways
    processors.put(BpmnElementType.EXCLUSIVE_GATEWAY, new ExclusiveGatewayProcessor());
    processors.put(BpmnElementType.INCLUSIVE_GATEWAY, new InclusiveGatewayProcessor());
    processors.put(BpmnElementType.PARALLEL_GATEWAY, new ParallelGatewayProcessor());

    // containers
    processors.put(BpmnElementType.SUB_PROCESS, new SubProcessProcessor());
    processors.put(BpmnElementType.CALL_ACTIVITY, new CallActivityProcessor());

    // events
    processors.put(BpmnElementType.START_EVENT, new StartEventProcessor());
    processors.put(BpmnElementType.END_EVENT, new EndEventProcessor());
    processors.put(BpmnElementType.INTERMEDIATE_CATCH_EVENT, new IntermediateCatchEventProcessor());
  }

  public static <E extends BaseDefinition, T extends AbstractFlowNodeBuilder>
      BpmnElementProcessor<E, T> getProcessor(final BpmnElementType bpmnElementType) {

    final BpmnElementProcessor processor = processors.get(bpmnElementType);
    if (processor == null) {
      throw new UnsupportedOperationException(
          String.format(
              "Expected to find a BPMN element processor for the BPMN element type '%s' but not found.",
              bpmnElementType));
    }
    return processor;
  }
}
