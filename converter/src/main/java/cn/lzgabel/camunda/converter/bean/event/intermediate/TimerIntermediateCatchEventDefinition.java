package cn.lzgabel.camunda.converter.bean.event.intermediate;

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
public class TimerIntermediateCatchEventDefinition extends IntermediateCatchEventDefinition {

  /** 时间定义表达式 */
  @NonNull private String timerDefinitionExpression;

  /** 时间定义类型：date/duration */
  @NonNull private String timerDefinitionType;

  @Override
  public String getEventType() {
    return "timer";
  }
}
