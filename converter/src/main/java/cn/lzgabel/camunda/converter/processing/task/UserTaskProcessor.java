package cn.lzgabel.camunda.converter.processing.task;

import cn.lzgabel.camunda.converter.bean.listener.TaskListener;
import cn.lzgabel.camunda.converter.bean.task.UserTaskDefinition;
import cn.lzgabel.camunda.converter.processing.BpmnElementProcessor;
import com.google.common.collect.Lists;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.builder.UserTaskBuilder;

/**
 * 〈功能简述〉<br>
 * 〈UserTask节点类型详情设置〉
 *
 * @author lizhi
 * @since 1.0.0
 */
public class UserTaskProcessor
    implements BpmnElementProcessor<UserTaskDefinition, AbstractFlowNodeBuilder> {

  private static final List<String> EVENT_TYPES =
      Lists.newArrayList("create", "assignment", "complete", "delete", "update");

  @Override
  public String onComplete(AbstractFlowNodeBuilder flowNodeBuilder, UserTaskDefinition flowNode)
      throws InvocationTargetException, IllegalAccessException {
    String assignee = flowNode.getAssignee();
    String candidateUsers = flowNode.getCandidateUsers();
    String candidateGroups = flowNode.getCandidateGroups();

    final UserTaskBuilder userTaskBuilder =
        (UserTaskBuilder) createInstance(flowNodeBuilder, flowNode);

    if (StringUtils.isNotBlank(assignee)) {
      userTaskBuilder.camundaAssignee(assignee);
    }

    if (StringUtils.isNotBlank(candidateUsers)) {
      userTaskBuilder.camundaCandidateUsers(candidateUsers);
    }

    if (StringUtils.isNotBlank(candidateGroups)) {
      userTaskBuilder.camundaCandidateGroups(candidateGroups);
    }

    // create task listener
    createTaskListener(userTaskBuilder, flowNode);

    return flowNode.getNodeId();
  }

  /**
   * 创建任务监听器
   *
   * @param userTaskBuilder builder
   * @param flowNode 当前节点
   */
  private void createTaskListener(UserTaskBuilder userTaskBuilder, UserTaskDefinition flowNode) {
    final List<TaskListener> listeners = flowNode.getTaskListeners();
    if (CollectionUtils.isNotEmpty(listeners)) {
      listeners.forEach(
          listener -> {
            final String eventType = listener.getEventType();
            if (!EVENT_TYPES.contains(eventType)) {
              throw new IllegalArgumentException("Unsupported event of type " + eventType);
            }

            if (listener.isClass()) {
              userTaskBuilder.camundaTaskListenerClass(eventType, listener.getJavaClass());
            } else if (listener.isDelegateExpression()) {
              userTaskBuilder.camundaTaskListenerDelegateExpression(
                  eventType, listener.getDelegateExpression());
            } else if (listener.isExpression()) {
              userTaskBuilder.camundaTaskListenerExpression(eventType, listener.getExpression());
            }
          });
    }
  }
}
