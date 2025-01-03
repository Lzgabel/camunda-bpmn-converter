package cn.lzgabel.camunda.converter;

import cn.lzgabel.camunda.converter.bean.BaseDefinition;
import cn.lzgabel.camunda.converter.bean.DecisionRefBindingType;
import cn.lzgabel.camunda.converter.bean.ProcessDefinition;
import cn.lzgabel.camunda.converter.bean.event.end.NoneEndEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.MessageIntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.intermediate.TimerIntermediateCatchEventDefinition;
import cn.lzgabel.camunda.converter.bean.event.start.*;
import cn.lzgabel.camunda.converter.bean.gateway.BranchDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.ExclusiveGatewayDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.InclusiveGatewayDefinition;
import cn.lzgabel.camunda.converter.bean.gateway.ParallelGatewayDefinition;
import cn.lzgabel.camunda.converter.bean.listener.ExecutionListener;
import cn.lzgabel.camunda.converter.bean.listener.TaskListener;
import cn.lzgabel.camunda.converter.bean.subprocess.CallActivityDefinition;
import cn.lzgabel.camunda.converter.bean.subprocess.SubProcessDefinition;
import cn.lzgabel.camunda.converter.bean.task.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class BpmnBuilderTest {

  @Rule public TestName testName = new TestName();

  private static final String OUT_PATH = "target/out/";

  private static final String PROCESS_ID = "process";
  private static final String PROCESS_NAME = "process";

  private <E extends BaseDefinition> ProcessDefinition buildProcessDefinitionWithNoneStartEvent(
      Supplier<E> supplier) {
    return ProcessDefinition.builder()
        .name(PROCESS_NAME)
        .processId(PROCESS_ID)
        .processNode(
            NoneStartEventDefinition.builder()
                .nodeId("start")
                .nodeName("开始节点")
                .nextNode(supplier.get())
                .build())
        .build();
  }

  // ------  from processDefinition --------
  @Test
  public void timer_start_event_with_cycle() throws IOException {
    TimerStartEventDefinition processNode =
        TimerStartEventDefinition.builder()
            .nodeId("timer_start")
            .nodeName("timer start")
            .timerDefinitionType(TimerDefinitionType.CYCLE.value())
            .timerDefinitionExpression("R1/PT5M")
            .nextNode(NoneEndEventDefinition.builder().nodeId("end").nodeName("end").build())
            .build();

    ProcessDefinition processDefinition =
        ProcessDefinition.builder()
            .name(PROCESS_NAME)
            .processId(PROCESS_ID)
            .processNode(processNode)
            .build();

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);

    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void timer_start_event_with_duration() throws IOException {
    TimerStartEventDefinition processNode =
        TimerStartEventDefinition.builder()
            .nodeId("timer_start")
            .nodeName("timer start")
            .timerDefinitionType(TimerDefinitionType.CYCLE.value())
            .timerDefinitionExpression("PT5M")
            .nextNode(NoneEndEventDefinition.builder().nodeId("end").nodeName("end").build())
            .build();

    ProcessDefinition processDefinition =
        ProcessDefinition.builder()
            .name(PROCESS_NAME)
            .processId(PROCESS_ID)
            .processNode(processNode)
            .build();

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);

    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void timer_start_event_with_date() throws IOException {
    TimerStartEventDefinition processNode =
        TimerStartEventDefinition.builder()
            .nodeId("timer_start")
            .nodeName("timer start")
            .timerDefinitionType(TimerDefinitionType.CYCLE.value())
            .timerDefinitionExpression("2024-09-08T10:00:00")
            .nextNode(NoneEndEventDefinition.builder().nodeId("end").nodeName("end").build())
            .build();

    ProcessDefinition processDefinition =
        ProcessDefinition.builder()
            .name(PROCESS_NAME)
            .processId(PROCESS_ID)
            .processNode(processNode)
            .build();

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);

    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void message_start_event() throws IOException {
    MessageStartEventDefinition processNode =
        MessageStartEventDefinition.builder()
            .nodeId("message_start")
            .nodeName("message start")
            .messageName("test-message-name")
            .nextNode(NoneEndEventDefinition.builder().nodeId("end").nodeName("end").build())
            .build();

    ProcessDefinition processDefinition =
        ProcessDefinition.builder()
            .name(PROCESS_NAME)
            .processId(PROCESS_ID)
            .processNode(processNode)
            .build();

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);

    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void timer_intermediate_catch_event_with_duration() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                TimerIntermediateCatchEventDefinition.builder()
                    .nodeName("timer catch a")
                    .nodeId("timer")
                    .timerDefinitionType("duration")
                    .timerDefinitionExpression("PT4M")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void timer_intermediate_catch_event_with_date() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                TimerIntermediateCatchEventDefinition.builder()
                    .nodeName("timer catch a")
                    .nodeId("timer")
                    .timerDefinitionType("date")
                    .timerDefinitionExpression("2024-09-08T10:00:00")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void message_intermediate_catch_event() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                MessageIntermediateCatchEventDefinition.builder()
                    .nodeId("message_intermediate_catch_event")
                    .nodeName("catch message a")
                    .messageName("test-message-name")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void receive_task() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                ReceiveTaskDefinition.builder()
                    .nodeId("receive_task")
                    .nodeName("receive task a")
                    .messageName("test-receive-message-name")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);

    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void script_task() throws IOException {

    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                ScriptTaskDefinition.builder()
                    .nodeId("script_task")
                    .nodeName("script task a")
                    .resultVariable("res")
                    .scriptFormat("groovy")
                    .scriptText("a + b")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void user_task() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                UserTaskDefinition.builder()
                    .nodeId("user_task")
                    .nodeName("user task a")
                    .assignee("lizhi")
                    .candidateUsers("lizhi,shuwen")
                    .candidateGroups("admin,member")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void user_task_with_execution_listener() throws IOException {
    ExecutionListener start =
        new ExecutionListener().setEventType("start").setJavaClass("com.lzgabel.Test");
    ExecutionListener end =
        new ExecutionListener().setEventType("end").setJavaClass("com.lzgabel.Test");
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                UserTaskDefinition.builder()
                    .nodeId("user_task")
                    .nodeName("user task a")
                    .assignee("lizhi")
                    .candidateGroups("lizhi")
                    .executionlistener(start)
                    .executionlistener(end)
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }

    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void user_task_with_task_listener() throws IOException {
    List<TaskListener> taskListeners =
        Collections.singletonList(
            new TaskListener().setEventType("create").setJavaClass("com.lzgabel.Test"));

    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                UserTaskDefinition.builder()
                    .nodeId("user_task")
                    .nodeName("user task a")
                    .assignee("lizhi")
                    .candidateGroups("lizhi")
                    .taskListeners(taskListeners)
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }

    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void service_task() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                ServiceTaskDefinition.builder()
                    .nodeId("service_task")
                    .nodeName("service task a")
                    .topic("test")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void business_rule_task() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                BusinessRuleTaskDefinition.builder()
                    .nodeId("business_rule_task")
                    .nodeName("business rule task a")
                    .decisionRef("test-id")
                    .decisionRefBinding(DecisionRefBindingType.VERSION.value())
                    .decisionRefVersion("1")
                    .resultVariable("res")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void manual_task() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                ManualTaskDefinition.builder()
                    .nodeId("manual_task")
                    .nodeName("manual rule task a")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void sub_process() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                SubProcessDefinition.builder()
                    .nodeId("sub_process")
                    .nodeName("sub-process")
                    .childNode(
                        NoneStartEventDefinition.builder()
                            .nodeId("sub_start")
                            .nodeName("sub start")
                            .nextNode(
                                UserTaskDefinition.builder()
                                    .nodeId("sub_user_task")
                                    .nodeName("user task")
                                    .assignee("lizhi2")
                                    .nextNode(
                                        NoneEndEventDefinition.builder()
                                            .nodeId("sub_end")
                                            .nodeName("sub end")
                                            .build())
                                    .build())
                            .build())
                    .nextNode(
                        UserTaskDefinition.builder()
                            .nodeId("user_task")
                            .nodeName("user task")
                            .nextNode(
                                NoneEndEventDefinition.builder()
                                    .nodeId("end")
                                    .nodeName("end")
                                    .build())
                            .build())
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void call_activity() throws IOException {
    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                CallActivityDefinition.builder()
                    .nodeId("call_activity")
                    .nodeName("call mediax process")
                    .calledElement("call-process-id")
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void exclusive_gateway() throws IOException {
    BranchDefinition branchDefinition1 =
        BranchDefinition.builder()
            .nodeName("分支1")
            .conditionExpression("${id>1}")
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_a")
                    .nodeName("user a")
                    .assignee("lizhi01")
                    .nextNode(
                        ExclusiveGatewayDefinition.builder()
                            .nodeId("exclusive_gateway_join")
                            .nodeName("exclusive gateway end")
                            .build())
                    .build())
            .build();

    BranchDefinition branchDefinition2 =
        BranchDefinition.builder()
            .nodeName("分支2")
            .isDefault(true)
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_b")
                    .nodeName("user b")
                    .assignee("lizhi02")
                    .nextNode(
                        ExclusiveGatewayDefinition.builder()
                            .nodeId("exclusive_gateway_join")
                            .nodeName("exclusive gateway end")
                            .build())
                    .build())
            .build();

    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                ExclusiveGatewayDefinition.builder()
                    .nodeId("exclusive_gateway_split")
                    .nodeName("exclusive gateway start")
                    .branchDefinition(branchDefinition1)
                    .branchDefinition(branchDefinition2)
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void parallel_gateway() throws IOException {

    BranchDefinition branchDefinition1 =
        BranchDefinition.builder()
            .nodeName("分支1")
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_a")
                    .nodeName("user a")
                    .assignee("lizhi01")
                    .nextNode(
                        ParallelGatewayDefinition.builder()
                            .nodeId("parallel_gateway_join")
                            .nodeName("parallel gateway end")
                            .build())
                    .build())
            .build();

    BranchDefinition branchDefinition2 =
        BranchDefinition.builder()
            .nodeName("分支2")
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_b")
                    .nodeName("user b")
                    .assignee("lizhi02")
                    .nextNode(
                        ParallelGatewayDefinition.builder()
                            .nodeId("parallel_gateway_join")
                            .nodeName("parallel gateway end")
                            .build())
                    .build())
            .build();

    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                ParallelGatewayDefinition.builder()
                    .nodeId("parallel_gateway_split")
                    .nodeName("parallel gateway start")
                    .branchDefinition(branchDefinition1)
                    .branchDefinition(branchDefinition2)
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }

  @Test
  public void inclusive_gateway() throws IOException {

    BranchDefinition branchDefinition1 =
        BranchDefinition.builder()
            .nodeName("分支1")
            .conditionExpression("${id>1}")
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_a")
                    .nodeName("user a")
                    .assignee("lizhi01")
                    .nextNode(
                        InclusiveGatewayDefinition.builder()
                            .nodeId("inclusive_gateway_join")
                            .nodeName("inclusive gateway end")
                            .build())
                    .build())
            .build();

    BranchDefinition branchDefinition2 =
        BranchDefinition.builder()
            .nodeName("分支2")
            .conditionExpression("${id<1}")
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_b")
                    .nodeName("user b")
                    .assignee("lizhi02")
                    .nextNode(
                        InclusiveGatewayDefinition.builder()
                            .nodeId("inclusive_gateway_join")
                            .nodeName("inclusive gateway end")
                            .build())
                    .build())
            .build();

    BranchDefinition branchDefinition3 =
        BranchDefinition.builder()
            .nodeName("默认分支")
            .isDefault(true)
            .nextNode(
                UserTaskDefinition.builder()
                    .nodeId("user_task_c")
                    .nodeName("user c")
                    .assignee("lizhi03")
                    .nextNode(
                        InclusiveGatewayDefinition.builder()
                            .nodeId("inclusive_gateway_join")
                            .nodeName("inclusive gateway end")
                            .build())
                    .build())
            .build();

    ProcessDefinition processDefinition =
        buildProcessDefinitionWithNoneStartEvent(
            () ->
                InclusiveGatewayDefinition.builder()
                    .nodeId("inclusive_gateway_split")
                    .nodeName("inclusive gateway start")
                    .branchDefinition(branchDefinition1)
                    .branchDefinition(branchDefinition2)
                    .branchDefinition(branchDefinition3)
                    .build());

    BpmnModelInstance bpmnModelInstance = BpmnBuilder.build(processDefinition);
    Path path = Paths.get(OUT_PATH + testName.getMethodName() + ".bpmn");
    if (path.toFile().exists()) {
      path.toFile().delete();
    }
    Files.createDirectories(path.getParent());
    Bpmn.writeModelToFile(Files.createFile(path).toFile(), bpmnModelInstance);
  }
}
