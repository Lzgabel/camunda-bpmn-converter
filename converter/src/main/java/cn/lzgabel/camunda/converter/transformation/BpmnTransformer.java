package cn.lzgabel.camunda.converter.transformation;

import cn.lzgabel.camunda.converter.BpmnBuilder;
import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import cn.lzgabel.camunda.converter.bean.ProcessDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.GatewayDefinition;
import cn.lzgabel.camunda.converter.transformation.bean.FlowDto;
import cn.lzgabel.camunda.converter.transformation.bean.ProcessDefinitionDto;
import cn.lzgabel.camunda.converter.transformation.transformer.*;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
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
    visitor.setContext(context);

    Optional.ofNullable(request.getNodes()).orElse(List.of()).forEach(visitor::visit);

    Optional.ofNullable(request.getFlows())
        .orElse(List.of())
        .forEach(flow -> this.handleFlow(flow, context));

    final var processDefinition =
        ProcessDefinition.builder()
            .name(request.getName())
            .processId(request.getProcessId())
            .processNode(context.start())
            .build();

    return BpmnBuilder.build(processDefinition);
  }

  private void handleFlow(final FlowDto flow, final TransformContext context) {
    final BaseDefinition source = context.definition(flow.getSource());
    final BaseDefinition target = context.definition(flow.getTarget());
    if (source instanceof final GatewayDefinition gatewayDefinition) {
      final List<BranchDefinition> branchDefinitions =
          Optional.ofNullable(gatewayDefinition.getBranchDefinitions())
              .orElse(Lists.newArrayList());

      final var builder = BranchDefinition.builder();
      final String conditionExpression = flow.getConditionExpression();
      if (StringUtils.isNotBlank(conditionExpression)) {
        builder.conditionExpression(conditionExpression);
      } else {
        builder.isDefault(flow.isDefaultFlow());
      }
      final BranchDefinition branchDefinition = builder.build();
      branchDefinition.setNextNode(target);

      branchDefinitions.add(branchDefinition);
      gatewayDefinition.setBranchDefinitions(branchDefinitions);
      return;
    }

    // 如果非网关节点，存在多条分支路径, 将下游节点清空
    if (Objects.nonNull(source.getNextNode())) {
      final List<BranchDefinition> branchDefinitions =
          Optional.ofNullable(source.getBranchDefinitions()).orElse(Lists.newArrayList());
      final BranchDefinition branchDefinition =
          BranchDefinition.builder().nextNode(source.getNextNode()).build();
      branchDefinitions.add(branchDefinition);
      source.setBranchDefinitions(branchDefinitions);
      source.setNextNode(null);
    }

    if (CollectionUtils.isNotEmpty(source.getBranchDefinitions())) {
      source.getBranchDefinitions().add(BranchDefinition.builder().nextNode(target).build());
      return;
    }

    // 设置下游节点
    source.setNextNode(target);
  }
}
