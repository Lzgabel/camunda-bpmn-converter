package cn.lzgabel.camunda.converter.transformation;

import cn.lzgabel.camunda.converter.BpmnBuilder;
import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import cn.lzgabel.camunda.converter.bean.ProcessDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.BranchNode;
import cn.lzgabel.camunda.converter.bean.gateway.GatewayDefinition;
import cn.lzgabel.camunda.converter.transformation.bean.FlowDto;
import cn.lzgabel.camunda.converter.transformation.bean.ProcessDefinitionDto;
import cn.lzgabel.camunda.converter.transformation.transformer.*;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public final class BpmnTransformer {
  private final TransformationVisitor visitor;

  public BpmnTransformer() {
    visitor = new TransformationVisitor();
    visitor.registerHandler(
        BpmnElementType.START_EVENT.getElementTypeName().get(), new StartEventTransformer());
    visitor.registerHandler(
        BpmnElementType.END_EVENT.getElementTypeName().get(), new EndEventTransformer());
    visitor.registerHandler(
        BpmnElementType.USER_TASK.getElementTypeName().get(), new UserTaskTransformer());
    visitor.registerHandler(
        BpmnElementType.SERVICE_TASK.getElementTypeName().get(), new ServiceTaskTransformer());
    visitor.registerHandler(
        BpmnElementType.EXCLUSIVE_GATEWAY.getElementTypeName().get(),
        new ExclusiveGatewayTransformer());
    visitor.registerHandler(
        BpmnElementType.INCLUSIVE_GATEWAY.getElementTypeName().get(),
        new InclusiveGatewayTransformer());
    visitor.registerHandler(
        BpmnElementType.PARALLEL_GATEWAY.getElementTypeName().get(),
        new ParallelGatewayTransformer());
  }

  public BpmnModelInstance transformDefinitions(final ProcessDefinitionDto request) {
    final var context = new TransformContext();
    final Consumer<FlowDto> consumer = (flow) -> handleFlow(flow, context);
    visitor.setContext(context);

    // 处理 node
    Optional.ofNullable(request.getNodes()).orElse(List.of()).forEach(visitor::visit);

    // 处理 flow
    Optional.ofNullable(request.getFlows()).orElse(List.of()).forEach(consumer);

    final var processDefinition =
        ProcessDefinition.builder()
            .name(request.getName())
            .processId(request.getProcessId())
            .processNode(context.start())
            .build();

    final var instance = BpmnBuilder.build(processDefinition);
    return instance;
  }

  private void handleFlow(final FlowDto flow, final TransformContext context) {
    final BaseDefinition source = context.definition(flow.getSource());
    final BaseDefinition target = context.definition(flow.getTarget());
    if (source instanceof final GatewayDefinition gatewayDefinition) {
      final List<BranchNode> branchNodes =
          Optional.of(gatewayDefinition.getBranchNodes()).orElse(Lists.newArrayList());

      final var builder = BranchNode.builder();
      final String conditionExpression = flow.getConditionExpression();
      if (StringUtils.isNotBlank(conditionExpression)) {
        builder.conditionExpression(conditionExpression);
      } else {
        builder.isDefault(flow.isDefaultFlow());
      }
      final BranchNode branchNode = builder.build();
      branchNode.setNextNode(target);

      branchNodes.add(branchNode);
      gatewayDefinition.setBranchNodes(branchNodes);
    } else {
      source.setNextNode(target);
    }
  }
}
