package cn.lzgabel.camunda.converter.transformation;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import java.util.HashMap;
import java.util.Map;

public final class TransformContext {

  private final Map<String, BaseDefinition> definitions = new HashMap<>();
  private BaseDefinition start = null;

  public void addDefinition(final BaseDefinition definition) {
    if (BpmnElementType.START_EVENT.isEquals(definition.getNodeType())) {
      start = definition;
    }
    definitions.put(definition.getNodeId(), definition);
  }

  public BaseDefinition definition(final String id) {
    return definitions.get(id);
  }

  public BaseDefinition start() {
    return start;
  }
}
