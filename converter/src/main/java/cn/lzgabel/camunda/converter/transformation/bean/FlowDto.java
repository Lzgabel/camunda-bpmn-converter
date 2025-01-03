package cn.lzgabel.camunda.converter.transformation.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowDto {
  private String source;
  private String target;

  private String conditionExpression;

  private boolean defaultFlow;
}
