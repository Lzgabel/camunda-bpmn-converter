package cn.lzgabel.camunda.converter.transformation.transformer;

import cn.lzgabel.camunda.converter.bean.gateway.ExclusiveGatewayDefinition;
import cn.lzgabel.camunda.converter.transformation.ElementTransformer;
import cn.lzgabel.camunda.converter.transformation.TransformContext;
import cn.lzgabel.camunda.converter.transformation.bean.NodeDto;

public class ExclusiveGatewayTransformer implements ElementTransformer {

  @Override
  public void transform(final NodeDto element, final TransformContext context) {
    final var definition =
        ExclusiveGatewayDefinition.builder()
            .nodeName(element.getName())
            .nodeId(element.getId())
            .build();
    context.addDefinition(definition);
  }
}
