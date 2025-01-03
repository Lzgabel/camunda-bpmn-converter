package cn.lzgabel.camunda.converter.processing;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.BpmnElementType;
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.GatewayDefinition;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

/**
 * 〈功能简述〉<br>
 * 〈完成基于 JSON 格式转 BPMN 元素业务逻辑转换〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public interface BpmnElementProcessor<E extends BaseDefinition, T extends AbstractFlowNodeBuilder> {

  /**
   * 创建新的节点
   *
   * @param flowNodeBuilder builder
   * @param flowNode 流程节点参数
   * @return 最后一个节点id
   * @throws InvocationTargetException invocationTargetException
   * @throws IllegalAccessException illegalAccessException
   */
  default String onCreate(T flowNodeBuilder, BaseDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final BpmnModelElementInstance element = flowNodeBuilder.getElement();
    final ModelInstance modelInstance = element.getModelInstance();
    final ModelElementInstance model = modelInstance.getModelElementById(flowNode.getNodeId());

    if (Objects.nonNull(model)) {
      flowNodeBuilder.connectTo(flowNode.getNodeId());
      return flowNode.getNodeId();
    }

    String nodeType = flowNode.getNodeType();
    BpmnElementType elementType = BpmnElementType.bpmnElementTypeFor(nodeType);
    BpmnElementProcessor<BaseDefinition, AbstractFlowNodeBuilder> processor =
        BpmnElementProcessors.getProcessor(elementType);
    processor.onComplete(flowNodeBuilder, flowNode);

    return finalizeCompletion(flowNodeBuilder, flowNode);
  }

  /**
   * 完成当前节点详情设置
   *
   * @param flowNodeBuilder builder
   * @param flowNode 流程节点参数
   * @return 最后一个节点id
   * @throws InvocationTargetException invocationTargetException
   * @throws IllegalAccessException illegalAccessException
   */
  String onComplete(T flowNodeBuilder, E flowNode)
      throws InvocationTargetException, IllegalAccessException;

  /**
   * 完成后继节点创建
   *
   * @param flowNodeBuilder builder
   * @param flowNode 流程节点参数
   * @throws InvocationTargetException invocationTargetException
   * @throws IllegalAccessException illegalAccessException
   */
  default String finalizeCompletion(T flowNodeBuilder, BaseDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    final String id = flowNode.getNodeId();
    // 如果还有后续任务，则遍历创建后续任务
    final BaseDefinition nextNode = flowNode.getNextNode();
    if (Objects.nonNull(nextNode)) {
      return onCreate(moveToNode(flowNodeBuilder, id), nextNode);
    }

    // 非网关节点，且存在多分支出度情况
    final List<BranchDefinition> branchDefinitions = flowNode.getBranchDefinitions();
    if (!(flowNode instanceof GatewayDefinition) && CollectionUtils.isNotEmpty(branchDefinitions)) {
      for (BranchDefinition branchDefinition : branchDefinitions) {
        onCreate(moveToNode(flowNodeBuilder, id), branchDefinition.getNextNode());
      }
      return id;
    }

    return id;
  }

  /**
   * 循环向上转型, 获取对象的 DeclaredMethod
   *
   * @param object : 子类对象
   * @param methodName : 父类中的方法名
   * @param parameterTypes : 父类中的方法参数类型
   * @return 父类中的方法对象
   */
  default Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
    Method method;
    for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        method = clazz.getDeclaredMethod(methodName, parameterTypes);
        return method;
      } catch (Exception ignore) {
      }
    }
    return null;
  }

  /**
   * 移动到指定节点
   *
   * @param flowNodeBuilder builder
   * @param id 目标节点位移标识
   * @return 目标节点类型 builder
   */
  @SuppressWarnings("unchecked")
  default T moveToNode(T flowNodeBuilder, String id) {
    return (T) flowNodeBuilder.moveToNode(id);
  }

  /**
   * 创建指定类型实例
   *
   * @param flowNodeBuilder builder
   * @param flowNode 节点
   * @return 指定类型实例 builder
   */
  @SuppressWarnings("unchecked")
  default AbstractFlowNodeBuilder createInstance(
      AbstractFlowNodeBuilder<?, ?> flowNodeBuilder, BaseDefinition flowNode) {
    // 自动生成id
    // Method createTarget = getDeclaredMethod(flowNodeBuilder, "createTarget", Class.class);
    // 手动传入id
    Method createTarget =
        getDeclaredMethod(flowNodeBuilder, "createTarget", Class.class, String.class);
    try {
      final var nodeType = flowNode.getNodeType();
      createTarget.setAccessible(true);
      Class<? extends FlowNode> clazz =
          BpmnElementType.bpmnElementTypeFor(nodeType)
              .getElementTypeClass()
              .orElseThrow(
                  () -> new RuntimeException("Unsupported BPMN element of type " + nodeType));

      final var instance =
          clazz.cast(createTarget.invoke(flowNodeBuilder, clazz, flowNode.getNodeId()));
      instance.setName(flowNode.getNodeName());
      final var builder = instance.builder();

      // 创建监听器
      createExecutionListener(builder, flowNode);
      return builder;
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 创建监听器
   *
   * @param flowNodeBuilder builder
   * @param flowNode 当前节点
   */
  default void createExecutionListener(
      AbstractFlowNodeBuilder<?, ?> flowNodeBuilder, BaseDefinition flowNode) {
    flowNode.getExecutionListeners().stream()
        .filter(Objects::nonNull)
        .forEach(
            listener -> {
              if (listener.isClass()) {
                flowNodeBuilder.camundaExecutionListenerClass(
                    listener.getEventType(), listener.getJavaClass());
              } else if (listener.isDelegateExpression()) {
                flowNodeBuilder.camundaExecutionListenerDelegateExpression(
                    listener.getEventType(), listener.getDelegateExpression());
              } else if (listener.isExpression()) {
                flowNodeBuilder.camundaExecutionListenerExpression(
                    listener.getEventType(), listener.getExpression());
              }
            });
  }
}
