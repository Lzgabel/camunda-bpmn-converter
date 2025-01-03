package cn.lzgabel.camunda.converter.transformation;

import cn.lzgabel.camunda.converter.transformation.bean.NodeDto;

public interface ElementTransformer {

  void transform(final NodeDto element, final TransformContext context);
}
