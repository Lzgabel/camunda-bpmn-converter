package cn.lzgabel.camunda.converter.bean.event.start;

import cn.lzgabel.camunda.converter.bean.event.EventType;
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
public class NoneStartEventDefinition extends StartEventDefinition {

  @Override
  public String getEventType() {
    return EventType.NONE.value();
  }
}
