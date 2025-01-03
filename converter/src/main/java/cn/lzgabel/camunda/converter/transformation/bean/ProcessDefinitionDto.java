package cn.lzgabel.camunda.converter.transformation.bean;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDefinitionDto {

  /** bpmn process id */
  private String processId;

  /** process name */
  private String name;

  /** nodes */
  private List<NodeDto> nodes;

  /** flows */
  private List<FlowDto> flows;
}
