package cn.lzgabel.camunda.converter.transformation.transformer;

import cn.lzgabel.camunda.converter.bean.event.start.NoneStartEventDefinition;
import cn.lzgabel.camunda.converter.transformation.ElementTransformer;
import cn.lzgabel.camunda.converter.transformation.TransformContext;
import cn.lzgabel.camunda.converter.transformation.bean.NodeDto;

public class StartEventTransformer implements ElementTransformer {
  @Override
  public void transform(final NodeDto element, final TransformContext context) {
    final var definition =
        NoneStartEventDefinition.builder()
            .nodeName(element.getName())
            .nodeId(element.getId())
            .build();
    context.addDefinition(definition);
  }
}
