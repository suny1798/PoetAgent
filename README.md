# Poet AI Agent

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.1.2-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

基于 Spring AI 和阿里云通义千问的智能 AI Agent 框架

[功能特性](#功能特性) • [快速开始](#快速开始) • [项目结构](#项目结构) • [配置说明](#配置说明)

</div>

## 📖 项目简介

Poet AI Agent 是一个基于 Spring AI 框架和阿里云通义千问大模型开发的智能 AI Agent 系统。项目集成了多种 AI 能力，包括对话管理、工具调用、RAG（检索增强生成）、向量存储等功能，旨在为开发者提供一个灵活、可扩展的 AI Agent 开发平台。

### 核心特性

- 🤖 **多模型支持**：集成阿里云通义千问大模型，支持对话和文本嵌入
- 🔧 **丰富的工具调用**：内置文件操作、终端命令、网络搜索、邮件发送等多种工具
- 🧠 **RAG 能力**：支持向量存储和检索增强生成，提升回答准确性
- 💬 **对话记忆管理**：支持基于文件和内存的对话记忆
- 🎯 **Agent 框架**：提供 ReAct 和工具调用 Agent，支持复杂任务编排
- 🌐 **MCP 协议支持**：支持 Model Context Protocol，可扩展更多外部工具
- 📊 **向量存储**：支持 PgVector 向量数据库，实现高效的语义检索

## 🚀 功能特性

### 1. 智能对话

- 基于通义千问大模型的多轮对话能力
- 支持结构化输出（JSON 格式）
- 自定义系统提示词
- 对话记忆管理（内存/文件）

### 2. 工具调用

项目内置多种实用工具：

- **CurrentTimeTool**：获取当前时间
- **FileOperationTool**：文件读写操作
- **QQMailTool**：QQ 邮件发送
- **ResourceDownloadTool**：资源下载
- **TerminalTool**：终端命令执行
- **WebSearchTool**：网络搜索
- **WebScrapeTool**：网页内容抓取
- **TerminateTool**：终止 Agent 执行

### 3. RAG 检索增强

- 文档加载与切分
- 向量嵌入与存储
- 自定义检索策略
- 查询重写与增强
- 元数据过滤

### 4. Agent 框架

- **BaseAgent**：Agent 基础抽象类
- **ReActAgent**：ReAct（推理-行动）模式 Agent
- **ToolCallAgent**：工具调用 Agent
- 自定义状态管理
- 步骤执行控制

### 5. MCP 支持

- MCP 客户端：连接外部 MCP 服务器
- MCP 服务端：提供 MCP 工具服务
- 内置地图服务（高德地图）
- 内置图片搜索服务（Pexels） 
> 注：图片搜索功能需要单独运行 <image-search-sever> 应用。
## 📦 快速开始

### 环境要求

- JDK 21+
- Maven 3.9
- PostgreSQL 13+（用于向量存储）
- Node.js 22

### 安装步骤

1. **克隆项目**

```bash
git clone https://github.com/suny1798/PoetAgent.git
cd PoetAgent
```

2. **配置环境**

修改 `src/main/resources/application-local.yml` 文件，配置以下信息：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-dashscope-api-key  # 替换为你的通义千问 API Key
      chat:
        options:
          model: qwen-plus
      embedding:
        options:
          model: text-embedding-v3
  datasource:
    url: jdbc:postgresql://your-host:5432/sun_ai_agent
    username: your-username
    password: your-password

qq:
  auth_code: your-qq-auth-code
  from_mail: your-qq-email

search-api:
  api-key: your-search-api-key
```

## 📁 项目结构

```
sun-ai-agent/
├── src/
│   ├── main/
│   │   ├── java/com/suny/sunaiagent/
│   │   │   ├── SunAiAgentApplication.java          # 主应用类
│   │   │   ├── advisor/                            # Advisor 配置
│   │   │   │   ├── MyLoggerAdvisor.java
│   │   │   │   └── MyReReadingAdvisor.java
│   │   │   ├── agent/                              # Agent 实现
│   │   │   │   ├── BaseAgent.java
│   │   │   │   ├── ReActAgent.java
│   │   │   │   ├── ToolCallAgent.java
│   │   │   │   └── model/
│   │   │   ├── app/                                # 应用服务
│   │   │   │   └── PoetApp.java
│   │   │   ├── chatmemory/                         # 对话记忆
│   │   │   │   └── FileBaseChatMemory.java
│   │   │   ├── controller/                         # 控制器
│   │   │   ├── rag/                                # RAG 相关
│   │   │   │   ├── PoetAppDocLoader.java
│   │   │   │   ├── PoetAppVectorStoreConfig.java
│   │   │   │   ├── PgVectorStoreConfig.java
│   │   │   │   └── QueryWriter.java
│   │   │   └── tools/                              # 工具实现
│   │   │       ├── ToolsRegistrationConfig.java    # 工具注册
│   │   │       ├── CurrentTimeTool.java
│   │   │       ├── FileOperationTool.java
│   │   │       ├── QQMailTool.java
│   │   │       ├── ResourceDownloadTool.java
│   │   │       ├── TerminalTool.java
│   │   │       ├── TerminateTool.java
│   │   │       ├── WebScrapeTool.java
│   │   │       └── WebSearchTool.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-local.yml
│   │       ├── mcp-servers.json
│   │       └── doc/
│   └── test/
└── image-search-sever/                         # 图片搜索 MCP 服务
    └── src/
        └── main/
            ├── java/com/suny/imagesearchsever/
            │   ├── ImageSearchSeverApplication.java
            │   └── tools/
            │       └── ImageSearchTool.java
            └── resources/
                ├── application.yml
                ├── application-sse.yml
                └── application-stdio.yml
```

## ⚙️ 配置说明

### 应用配置

主要配置文件说明：

- `application.yml`：基础配置
- `application-local.yml`：本地环境配置（包含敏感信息，不提交到版本控制）
- `mcp-servers.json`：高德地图 MCP 配置
- `application-sse.yml`：MCP 服务器配置

### 向量存储配置

项目支持两种向量存储方式：

1. **内存向量存储**：适合开发测试
2. **PgVector**：适合生产环境，支持持久化和大规模数据

### MCP 服务器配置

在 `mcp-servers.json` 中配置 MCP 服务器：

```json
{
  "mcpServers": {
    "amap-maps": {
      "command": "npx.cmd",
      "args": ["-y", "@amap/amap-maps-mcp-server"],
      "env": {
        "AMAP_MAPS_API_KEY": "your-api-key"
      }
    },
    "image-search": {
      "command": "java",
      "args": ["-jar", "image-search-sever/target/image-search-sever-0.0.1-SNAPSHOT.jar"],
      "env": {}
    }
  }
}
```


## 🔧 开发指南

### 添加新工具

1. 创建工具类，继承 `@Tool` 注解：

```java
@Service
public class MyTool {
    @Tool(description = "工具描述")
    public String myMethod(@ToolParam(description = "参数描述") String param) {
        // 工具实现
        return "result";
    }
}
```

2. 在 `ToolsRegistrationConfig` 中注册工具：

```java
@Bean
public ToolCallback[] allTools() {
    return ToolCallbacks.from(
        new MyTool(),
        // 其他工具...
    );
}
```

### 创建自定义 Agent

1. 继承 `BaseAgent` 或 `ReActAgent`
2. 实现 `step()` 方法
3. 配置系统提示词和工具

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 📄 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 📞 联系方式

- 项目主页：[GitHub Repository](https://github.com/suny1798/PoetAgent)
- 问题反馈：[Issues](https://github.com/suny1798/PoetAgent/issues)

## 🙏 致谢

- [Spring AI](https://spring.io/projects/spring-ai)
- [阿里云通义千问](https://tongyi.aliyun.com/)
- [DashScope SDK](https://github.com/aliyun)
- [Knife4j](https://doc.xiaominfo.com/)
