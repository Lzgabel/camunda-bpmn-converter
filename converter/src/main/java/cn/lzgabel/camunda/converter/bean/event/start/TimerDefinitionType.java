package cn.lzgabel.camunda.converter.bean.event.start;

import java.util.stream.Stream;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public enum TimerDefinitionType {

  /** date */
  DATE("date"),

  /** cycle */
  CYCLE("cycle"),

  /** duration */
  DURATION("duration");

  private String value;

  TimerDefinitionType(String value) {
    this.value = value;
  }

  public boolean isEqual(String value) {
    return this.value.equals(value);
  }

  public String value() {
    return this.value;
  }

  public static TimerDefinitionType from(final String value) {
    return Stream.of(values()).filter(p -> p.value.equals(value)).findFirst().orElse(null);
  }
}
