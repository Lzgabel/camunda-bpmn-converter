<div align="center">
<img src="./static/img/img.jpeg" width="400px"/>

<h1>Camunda BPMN Converter </h1>
<p>
Convert json to bpmn for Camunda Platform.
</p>
</div>


### 1. 节点类型（nodeType)
- serviceTask
    - service 任务节点
- userTask
  - userTask 任务节点
- parallelGateway
    - 并行节点
- exclusiveGateway
    - 排他节点

### 2. 数据结构

- serviceTask
```json
{
    "nodeName":"审核人1",
    "nodeType":"serviceTask",
    "nextNode": null
}
```

- userTask
```json
{
    "nodeName":"审核人1",
    "nodeType":"userTask",
    "assignee": "aaa",
    "candidateUsers": "bbb,ccc",
    "candidateGroups": "bbb,ddd",
    "nextNode": null
}
```

> 对于 parallelGateway/exclusiveGateway 类型，目前建议设置 nextNode 的 nodeType 类型一一对应

- exclusive
```json
{
    "nodeName":"排他",
    "nodeType":"exclusiveGateway",
    "nextNode":{
        "nodeName":"",
        "nodeType":"exclusiveGateway",
        "nextNode":null
    },
    "branchDefinitions":[
        {
            "nodeName":"条件1",
            "conditionExpression":"=id>1",
            "nextNode":{
                "nodeName":"审核人2.1",
                "nodeType":"serviceTask",
                "nextNode": null
            }
        },
        {
            "nodeName":"条件2",
            "conditionExpression":"=id<1",
            "nextNode":{
                "nodeName":"审核人2.2",
                "nodeType":"serviceTask",
                "nextNode":null
            }
        },
        {...}
    ]
}
```

- parallel
```json
{
    "nodeName":"并行任务",
    "nodeType":"parallelGateway",
    "nextNode":{
        "nodeName":"",
        "nodeType":"parallelGateway",
        "nextNode":null
    },
    "branchDefinitions":[
        {
            "nextNode":{
                "nodeName":"审核人2.1",
                "nodeType":"serviceTask",
                "nextNode": null
            }
        },
        {
            "nextNode":{
                "nodeName":"审核人2.2",
                "nodeType":"serviceTask",
                "nextNode":null
            }
        },
        {
            "nextNode":{
                "nodeName":"审核人2.3",
                "nodeType":"serviceTask",
                "nextNode":null
            }
        },
        {...}
    ]
}
```

- 完整数据结构
```json
{
    "process":{
        "processId":"work-flow-id",
        "name":"合同审批"
    },
    "processNode":{
        "nodeName":"审核人1",
        "nodeType":"serviceTask",
        "taskHeaders":{
            "a":"b",
            "e":"d"
        },
        "nextNode":{
            "nodeName":"排他",
            "nodeType":"exclusiveGateway",
            "nextNode":{
                "nodeName":"排他网关",
                "nodeType":"exclusiveGateway",
                "nextNode":null
            },
            "branchDefinitions":[
                {
                    "nodeName":"条件1",
                    "conditionExpression":"=id>1",
                    "nextNode":{
                        "nodeName":"审核人2.1",
                        "nodeType":"userTask",
                        "assignee": "aaa",
                        "candidateGroups": "bbb",
                        "nextNode":null
                    }
                },
                {
                    "nodeName":"条件2",
                    "conditionExpression":"=id<1",
                    "nextNode":{
                        "nodeName":"审核人2.2",
                        "nodeType":"serviceTask",
                        "nextNode":null
                    }
                }
            ]
        }
    }
}
```
### 3. 示例
* 启动服务
    * ``cd example && mvn clean package -DskipTests && mvn spring-boot:run``
    * http://localhost:8080
