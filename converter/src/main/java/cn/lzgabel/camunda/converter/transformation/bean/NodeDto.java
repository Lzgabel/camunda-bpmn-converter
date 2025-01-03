package cn.lzgabel.camunda.converter.transformation.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeDto {
  /** 节点 */
  private String id;

  /** 节点名称 */
  private String name;

  /** 节点类型 */
  private String type;
}
