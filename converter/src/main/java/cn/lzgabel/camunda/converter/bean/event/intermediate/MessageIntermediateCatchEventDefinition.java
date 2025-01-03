package cn.lzgabel.camunda.converter.bean.event.intermediate;

import cn.lzgabel.camunda.converter.bean.event.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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
public class MessageIntermediateCatchEventDefinition extends IntermediateCatchEventDefinition {

  @NonNull private String messageName;

  @Override
  public String getEventType() {
    return EventType.MESSAGE.value();
  }
}
