**目前处于Beta版本，因此不建议用到正式环境！！！**
欢迎大家提Issues，看到后我会回复的
## 1. `runflow`是什么
runflow是一个基于逻辑流转的轻量级流程引擎。
runflow支持BPMN规范，能够把复杂的业务逻辑可视化。开发人员可以通过流程编辑器设计自己的业务流程，为业务设计人员与开发工程师架起一座桥梁。
## 2. 特性
**规则轻量**：只要稍微了解bmpn规范的人员就快速上手。三分钟入门，一看既懂。
**丰富业务场景**：支持排他网关，并行网关。可以应用到各种业务场景。
**多线程编排**：通过并行网关与异步任务，可以对多线程进行编排。
**支持流程设计图**：支持导入导出，可视化编辑流程图
## 3. 界面

###### **排他网关**

![img_1.png](img_1.png)

###### **并行网关**

![img.png](img.png)

###### **调用活动**

![img_2.png](img_2.png)

## 4. 快速开始
**引入pom文件**
`<dependency>
<groupId>com.runflow</groupId>
<artifactId>runflow</artifactId>
<version>0.0.1-Beta</version>
</dependency>`

