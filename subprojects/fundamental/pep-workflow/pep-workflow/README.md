pep-workflow
============

Proper Enterprise Platform

Workflow Module

基于 [Flowable](https://github.com/flowable/flowable-engine) 构建平台的工作流模块

Web 流程设计器使用 `Flowable` 项目中的 [flowable-ui-modeler](https://github.com/flowable/flowable-engine/tree/master/modules/flowable-ui-modeler) 模块提供的流程设计器，以 jar 包形式为平台所依赖。可通过 [proper-* branch](https://github.com/propersoft-cn/flowable-engine) 来打包这些静态资源


配置
----

引用此模块的项目需要进行一些额外的配置，以保证流程设计器的可用

### 代理(ng2中使用到workflow中的静态资源时需要配置)

因为流程设计器的前端资源是集成在 jar 包中的，需要通过后端服务器的响应返回给前端，故为能够在前端使用流程设计器，需要在后端服务前增加代理服务器。以 nginx 举例，配置如下（假设前端资源上下文根为 `/ng2`）：

- [下载nginx](http://nginx.org/en/download.html "下载nginx")
- 修改nginx配置，路径为conf/nginx.conf
```
#server下添加
#本地静态文件存放地址（具体地址自行修改）
location ^~ /ng2/ {
    proxy_pass http://frontend_server:port/;
}
#服务端静态文件存放地址（具体地址自行修改）
location ^~ /ng2/workflow {
    proxy_pass http://backend_server:port/pep/workflow;
}
```
- 启动nginx，访问[http://127.0.0.1](http://127.0.0.1 "http://127.0.0.1")，看到welcom to nginx,启动成功
- 访问代理路径[http://127.0.0.1/ng2](http://127.0.0.1/ng2 "http://127.0.0.1/ng2")，看到协同管理平台页面，代理成功

### Context Root

流程设计器的默认上下文根在 `app-cfg.js` 中配置成了 `pep`。项目中使用时，须更改此默认配置为项目 **前端** 服务上下文根。修改方法为：

将 [app-cfg.js](./src/main/resources/META-INF/resources/editor-app/app-cfg.js) 复制到项目 **后端** 代码库相同路径下，并将 `contextRoot` 值中的 `pep` 修改为项目 **前端** 服务实际上下文根。

> 平台前端项目开发时会自动启动一个代理服务，将 `/pep` 的请求转发至平台后端服务。实际项目中使用时，并没有这个代理服务，故需要将此处配置为 **前端** 服务的上下文根


平台流程自定义配置
----
获取流程变量: `${variableName}`, variableName 为想获取变量的名称(表单中的字段名称)

平台为流程设计器提供了几个自定义操作
- 流程提供全局变量一览

  流程变量名|流程变量含义
  ---|---
  initiator|发起人Id
  initiatorName|发起人姓名
  processDefinitionName|流程定义名称
  processTitle|流程显示标题
  processCreateTime|流程发起时间

- 自定义流程标题

  在流程设计器中选择 > 数据对象 > 增加一条数据对象 > `Id` 设置为 `processTitle`, `名称` 设置为 `processTitle`, `类型` 为 `string`, `默认值` 设置为需要显示的流程标题(如: `${initiatorName}的${processDefinitionName}`)
  ```
  xml:
  <process id="testProcess" name="测试流程" isExecutable="true">
     <dataObject id="processTitle" name="processTitle" itemSubjectRef="xsd:string">
       <extensionElements>
         <flowable:value>${initiatorName}的${processDefinitionName}</flowable:value>
       </extensionElements>
     </dataObject>
   ......
   ```
   流程标题中 `${initiatorName}` 变量名可以为表单配置的所有变量以及流程提供的所有全局变量
   > 不设置则使用系统默认流程标题 `XX(发起人)的XX(流程定义名称)`

- 自定义待办消息提醒

  1. 在流程设计器中选择 > 数据对象 > 增加一条数据对象 > `Id` 设置为 `taskAssigneeNoticeCode`, `名称` 设置为 `taskAssigneeNoticeCode`, `类型` 为 `string`, `默认值` 设置为需要使用模板的`code`(如: `TestTaskAssignee`)
     ```
     xml:
     <process id="testProcess" name="测试流程" isExecutable="true">
         <dataObject id="taskAssigneeNoticeCode" name="taskAssigneeNoticeCode" itemSubjectRef="xsd:string">
           <extensionElements>
             <flowable:value>TestTaskAssignee</flowable:value>
           </extensionElements>
         </dataObject>
     ......
     ```
     
     > 不设置则使用系统默认待办提醒模板, 默认待办提醒模板内容为: 1. 邮件内容：`【xx(发起人)】发起的【xx(流程定义名称)】已经到达您的代办，请您处理【xx(待办节点名称)】节点。` 2. 推送提醒: `xx(发起人)的xx(流程定义名称)`
     
  2. 在需要获取待办提醒的任务节点 > 任务监听器 > 增加一条任务监听 > `事件` 设置为 `create`, `表达式` 设置为 `#{taskAssigneeOrCandidateNotice.notice(task)}`
  
- 自定义流程抢办提醒
  
  在需要获取抢办提醒的任务节点 > 任务监听器 > 增加一条任务监听 > `事件` 设置为 `complete`, `表达式` 设置为 `#{taskCompletedNotice.notice(task)}`
  
- 自定义流程结束提醒
  1. 在流程设计器中选择 > 数据对象 > 增加一条数据对象 > `Id` 设置为 `endNoticeCode`, `名称` 设置为 `endNoticeCode`, `类型` 为 `string`, `默认值` 设置为需要使用模板的`code`(如: `TestEndCode`)
     ```
     xml:
     <process id="testProcess" name="测试流程" isExecutable="true">
         <dataObject id="endNoticeCode" name="endNoticeCode" itemSubjectRef="xsd:string">
           <extensionElements>
             <flowable:value>TestEndCode</flowable:value>
           </extensionElements>
         </dataObject>
     ......
     ```
  
     > 不设置则使用系统默认流程结束提醒模板, 默认结束提醒模板内容为: 1. 邮件内容：`您的【xx(流程定义名称)】已完成，详情请在【发起历史】中查看。` 2. 推送提醒: `您的xx(流程定义名称)`
     
  2. 在流程设计器中选择 > 数据对象 > 增加一条数据对象 > `Id` 设置为 `endNoticeUserId`, `名称` 设置为 `endNoticeUserId`, `类型` 为 `string`, `默认值` 设置为流程中的`变量名`(如: `userId`)
     ```
     xml:
     <process id="testProcess" name="测试流程" isExecutable="true">
         <dataObject id="endNoticeUserId" name="endNoticeUserId" itemSubjectRef="xsd:string">
           <extensionElements>
             <flowable:value>userId</flowable:value>
           </extensionElements>
         </dataObject>
     ......
     ```
  
     > 不设置则提醒流程发起人
  
  3. 在结束节点 > 节点监听器 > 增加一条执行监听 > `事件` 设置为 `end`, `表达式` 设置为 `#{endNotice.notice(execution)}`

- 配置节点跳过

  在流程中两个连续的任务节点经办人为同一个人时, 需要配置节点跳过。配置详情:
  
  在需要设置跳过的任务节点 > 跳过表达 > 设置为 `#{sameAssigneeSkip.skip(execution)}`。如果该节点在某些条件下不需要跳过, 如 a 发起人发起的流程不需要跳过, 表达式可设置为 `#{initiator=="a"?false:sameAssigneeSkip.skip(execution)}`

- 动态配置经办人、候选人/用户组/角色
  
  平台提供动态配置 经办人、候选人/用户组/角色 接口。
  
  获取经办人需要实现 [AssigneeByOrganizationRule](./src/main/java/com/proper/enterprise/platform/workflow/rule/AssigneeByOrganizationRule.java) 来获取经办人Id, 产品中则实现`AssigneeByOrganizationRule.execute(organization)`, 通过组织查找相应的经办人。
  
  获取 候选人/用户组/角色 实现 [CandidateByOrganizationRule](./src/main/java/com/proper/enterprise/platform/workflow/rule/CandidateByOrganizationRule.java)来获取候选人/用户组/角色Id集合。 
  
  >产品提供了部门负责人的获取 #{assigneeByOrgHeadRule.execute(organizationId)}
  部门副总的获取 #{assigneeByOrgVicePresidentRule.execute(organizationId)}
  
- 全局变量自定义初始化
  
  平台提供以上全局变量之外, 还提供自定义添加全局变量。
  
  使用方法:
  
  实现 [GlobalVariableInitHandler](./src/main/java/com/proper/enterprise/platform/workflow/handler/GlobalVariableInitHandler.java) 即可, 例子可见 [GlobalVariableInitHandlerImpl](./src/test/groovy/com/proper/enterprise/platform/workflow/frame/handler/GlobalVariableInitHandlerImpl.java)
  
  >产品中提供了 organizationId organizationName organizationEmail 三个全局变量的提供

- 自动归档
  
  在流程设计器中选择 > 脚本任务节点
  配置脚本任务节点：
  1. 脚本格式设置为 `groovy`
  2. 脚本编写为 `com.proper.enterprise.platform.workflow.plugin.util.ArchiveUtil.archiveToMongo(execution, 'testForm')`
     >其中 testForm 为需要归档的表单数据的表单关键字。
      需要归档多个表单则后面增加表单关键字即可, 如 archiveToMongo(execution, 'testForm', 'test2Form')
