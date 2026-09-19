# Spring AI Agent

An intelligent chatbot built with Spring Boot that combines AI, local tools, remote APIs, and document knowledge.

## What It Does

This project creates an **AI agent** that can:
- Answer questions using an LLM (Mistral AI)
- Call local Java tools (e.g., get time in any timezone)
- Access external APIs via MCP servers (e.g., GitHub)
- Search and answer from uploaded PDF documents (RAG)

### Example Queries

```
"What is the current time in New York?"
→ Uses GetCurrentTime tool

"What is the return policy?"
→ Searches ingested FAQ PDF

"Show GitHub repositories"
→ Calls GitHub via MCP server
```

## Quick Start

### Prerequisites

- Java 17+
- Maven
- Docker (for Redis)
- API Keys: Mistral AI, GitHub

### 1. Clone & Build

```bash
git clone <repository-url>
cd spring-ai-agent
mvn clean install
```

### 2. Set Environment Variables

```bash
export MISTRAL-API-KEY=your_mistral_key
export GITHUB-API-KEY=your_github_token
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

### 3. Start Redis (Docker)

```bash
docker run -d -p 6379:6379 redis:latest
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

Server starts at `http://localhost:8080`

## API Endpoints

### 1. Ask the Agent a Question

```bash
curl -X POST http://localhost:8080/api/agent \
  -H "Content-Type: application/json" \
  -d '{"query": "What is the current time in New York?"}'
```

**Response:**
```json
{
  "answer": "2024-12-20T15:30:45.123-05:00[America/New_York]"
}
```

### 2. Get Agent Information

```bash
curl -X GET http://localhost:8080/api/agent
```

Shows available tools, advisors, LLM model, and vector store info.

### 3. Upload & Ingest a PDF

```bash
curl -X GET "http://localhost:8080/api/ingest/online_shopping_faq.pdf"
```

### 4. Verify Ingested Data

```bash
curl -X POST http://localhost:8080/api/verify-ingest \
  -H "Content-Type: application/json" \
  -d '{"query": "What is the return policy?"}'
```

## Project Structure

```
src/main/java/com/techietag/springaiagent/
├── config/
│   └── MCPClientCustomizaton.java    # MCP authentication setup
├── controller/
│   ├── AgentController.java          # Main API endpoints
│   └── RAGIngestionController.java   # PDF upload endpoints
├── service/
│   └── IngestionService.java         # PDF chunking & vector store
├── tools/
│   └── GetCurrentTime.java           # Example local tool
└── SpringAiAgentApplication.java     # Application entry point

src/main/resources/
├── application.properties             # Configuration
└── docs/
    └── online_shopping_faq.pdf       # Sample FAQ for RAG
```

## Key Components

| Component | Role |
|-----------|------|
| **AgentController** | REST API for queries and agent info |
| **IngestionService** | Reads PDFs, chunks them, stores in Redis |
| **GetCurrentTime** | Example tool the agent can invoke |
| **MCPClientCustomization** | Authenticates GitHub MCP requests |

## How It Works

1. **User sends query** → `POST /api/agent`
2. **Agent analyzes** what tools/knowledge it needs
3. **Retrieves information**:
   - Calls local Java tools
   - Searches vector store (PDF documents)
   - Queries MCP servers (GitHub, etc.)
4. **LLM synthesizes** final answer
5. **Response returned** to user

## Technology Stack

- **Framework:** Spring Boot 3.5.9
- **AI Model:** Mistral AI (Large)
- **Embeddings:** Mistral Embed
- **Vector Database:** Redis
- **Protocol:** Model Context Protocol (MCP)
- **Language:** Java 17

## Configuration

Edit `application.properties` to customize:

```properties
# Server port
server.port=8080

# Redis (vector store)
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Mistral AI
spring.ai.mistralai.chat.options.temperature=0.2
spring.ai.mistralai.chat.options.model=mistral-large-latest

# Vector store index
spring.ai.vectorstore.redis.index-name=my-redis-index
```

## Adding Custom Tools

Create a new tool by adding an `@Service` with an `@Tool` method:

```java
@Service
public class MyTool {
    @Tool(description = "My tool description", name = "MyCustomTool")
    public String doSomething(String input) {
        return "Result: " + input;
    }
}
```

The agent will automatically discover and use it.



## Troubleshooting

| Issue | Solution |
|-------|----------|
| Redis connection failed | Ensure Redis is running on configured host/port |
| API key errors | Check environment variables are set correctly |
| PDF not ingesting | Verify file exists in `resources/docs/` or upload via endpoint |
| Tool not recognized | Ensure `@Tool` annotation is present on method |

## Next Steps

- Add more custom tools
- Upload your own PDF documents
- Configure different LLM models
- Integrate with other MCP servers

## License

MIT


