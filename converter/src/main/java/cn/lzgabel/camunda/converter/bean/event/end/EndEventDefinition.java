package cn.lzgabel.camunda.converter.bean.event.end;

import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import cn.lzgabel.camunda.converter.bean.event.EventDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author lizhi
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class EndEventDefinition extends EventDefinition {

  @Override
  public String getNodeType() {
    return BpmnElementType.END_EVENT.getElementTypeName().get();
  }
}
