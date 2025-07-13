# LangChain4J AI 对话集成项目

[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
[![Java Version](https://img.shields.io/badge/java-17%2B-orange)](https://openjdk.org/)

一款基于LangChain4J框架开发的智能对话系统，支持与多种AI模型集成，提供灵活的对话管理能力。

## ✨ 功能特性

- **多模型支持**：支持通义千问(Qianwen)、Gemini系列、Ollama本地LLM等主流模型
- **对话记忆**：支持短期/长期记忆上下文管理
- **Rag增强**：添加本地知识库
- **模块化设计**：可插拔的组件架构
- **RESTful API**：提供标准化的HTTP接口
- **统一接口**：通过MCP（Model Control Protocol）协议实现多模型标准化调用

## 📦 快速开始

### 环境要求
- JDK 21+
- Maven 3.6+
- (可选) OpenAI API Key 或其他AI服务凭证

### 安装步骤
```bash
git clone https://github.com/xiaoniu667/ai-code-helper.git
cd ai-code-helper
mvn clean install