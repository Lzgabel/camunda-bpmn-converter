package cn.lzgabel.camunda.converter.bean.event;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
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
public abstract class EventDefinition extends BaseDefinition {

  private String eventType;

  public String getEventType() {
    return EventType.NONE.value();
  }
}
