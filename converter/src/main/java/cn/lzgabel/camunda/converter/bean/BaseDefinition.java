package cn.lzgabel.camunda.converter.bean;

import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.listener.ExecutionListener;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * 〈功能简述〉<br>
 * 〈基础元素定义〉
 *
 * @author lizhi
 * @since 1.0.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseDefinition implements Serializable {

  /** 节点ID */
  @NonNull private String nodeId;

  /** 节点名称 */
  private String nodeName;

  /** 后继节点 */
  private BaseDefinition nextNode;

  /** 分支节点 */
  private List<BranchDefinition> branchDefinitions;

  /** 执行监听器 */
  private List<ExecutionListener> executionListeners;

  public abstract String getNodeType();

  public abstract static class BaseDefinitionBuilder<
      C extends BaseDefinition, B extends BaseDefinition.BaseDefinitionBuilder<C, B>> {

    public BaseDefinitionBuilder() {
      this.executionListeners = Lists.newArrayList();
    }

    public B nodeName(String nodeName) {
      this.nodeName = nodeName;
      return self();
    }

    public B nextNode(BaseDefinition nextNode) {
      this.nextNode = nextNode;
      return self();
    }

    public B executionlistener(ExecutionListener listener) {
      this.executionListeners.add(listener);
      return self();
    }

    public B executionlistener(Supplier<ExecutionListener> supplier) {
      this.executionListeners.add(supplier.get());
      return self();
    }
  }
}
