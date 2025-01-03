package cn.lzgabel.camunda.converter.transformation;

import cn.lzgabel.camunda.converter.transformation.bean.NodeDto;
import java.util.HashMap;
import java.util.Map;

public final class TransformationVisitor {

  private final Map<String, ElementTransformer> transformHandlers = new HashMap<>();

  private TransformContext context;

  public TransformContext getContext() {
    return context;
  }

  public void setContext(final TransformContext context) {
    this.context = context;
  }

  public void registerHandler(final String type, final ElementTransformer transformHandler) {
    transformHandlers.put(type, transformHandler);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  void visit(final NodeDto instance) {
    final ElementTransformer handler = transformHandlers.get(instance.getType());
    if (handler != null) {
      handler.transform(instance, context);
    }
  }
}
