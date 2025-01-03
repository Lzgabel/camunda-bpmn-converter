package cn.lzgabel.camunda.converter.bean.event.end;

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
public class NoneEndEventDefinition extends EndEventDefinition {

  @Override
  public String getEventType() {
    return EventType.NONE.value();
  }
}
