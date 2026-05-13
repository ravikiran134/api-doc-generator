# API Doc Generator

> AI-powered API documentation generator for Spring Boot applications using local LLMs.

Automatically generates comprehensive Markdown documentation from Spring Boot REST controllers. Runs entirely offline using [Ollama](https://ollama.com) - no API keys, no cloud costs, no data leaving your machine.

## Why?

API documentation is critical but constantly drifts from code. Writing it manually is tedious; maintaining it is worse. This tool reads Spring Boot controllers and produces ready-to-use API docs in seconds.

**Key benefits:**
- 🔒 **Private** - Code never leaves your machine
- 💸 **Free** - No API costs, runs locally
- ⚡ **Fast** - Generate docs for an entire controller in minutes
- 🔌 **Flexible** - LLM-agnostic architecture (swap Ollama for Claude/GPT-4)

## Example Output

**Input** - a Spring Boot controller:
```java
@RestController
public class UserController {
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequestDTO request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered.");
    }
    
    @GetMapping("/users/{userId}/projects")
    public ResponseEntity<?> getProjectsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getProjectsForUser(userId));
    }
}
```

**Output** - generated Markdown:

```markdown
## POST /register
**Description:** Registers a new user based on RegistrationRequestDTO
**Request Body:** RegistrationRequestDTO (see DTO class for fields)
**Response:** 201 Created with confirmation message

## GET /users/{userId}/projects
**Description:** Retrieves all projects for the specified user
**Path Variables:** userId (UUID)
**Response:** 200 OK with list of ProjectSummaryDTO
```

See [`output/UserController.md`](output/UserController.md) for full sample output.

## Architecture

## Architecture

```
┌──────────────────┐     HTTP POST      ┌─────────────────┐
│  Python Script   │ ─────────────────> │  Ollama Service │
│ generate_docs.py │   localhost:11434  │  qwen2.5-coder  │
└──────────────────┘ <───────────────── └─────────────────┘
        │            Generated Markdown
        ▼
   output/*.md
```

**Service-oriented design:** The Python application is decoupled from the LLM backend via HTTP. The same code can target local Ollama, Claude API, OpenAI, or any LLM service by changing one endpoint.

## Tech Stack

- **Python 3.10+** - Core application logic
- **Ollama** - Local LLM runtime
- **Qwen2.5-Coder 3B** - Code-specialized language model (~2GB)
- **Prompt Engineering** - Custom templates with anti-hallucination guardrails

## Quick Start

### Prerequisites

1. Install [Ollama](https://ollama.com/download)
2. Pull the model:
```bash
   ollama pull qwen2.5-coder:3b
```
3. Python 3.10+ installed

### Installation

```bash
# Clone the repo
git clone https://github.com/ravikiran134/api-doc-generator.git
cd api-doc-generator

# Create virtual environment
python -m venv venv
venv\Scripts\activate    # Windows
# source venv/bin/activate  # Mac/Linux

# Install dependencies
pip install -r requirements.txt
```

### Usage

```bash
python generate_docs.py samples/UserController.java
```

Generated documentation appears in `output/UserController.md`.

## Design Decisions

### Why Local LLM (Ollama)?

- **No vendor lock-in** - Not dependent on OpenAI/Anthropic billing
- **Privacy** - Production code can be processed without data exposure concerns
- **Cost** - Zero per-request costs
- **Trade-off** - Smaller models hallucinate more; addressed via strict prompting

### Why Service-Oriented Architecture?

Treating the LLM as an HTTP service (not a library) provides:
- Easy backend swapping (Ollama → Claude API → OpenAI)
- Independent scaling of LLM and application logic  
- Mirrors production AI system patterns

### Anti-Hallucination Prompt Engineering

The prompt template includes explicit constraints:
- "Only document what you see in the code"
- "Mark inferences with (inferred)"
- "Do not invent endpoints, parameters, or request bodies"

This reduces but doesn't eliminate hallucinations - see Limitations below.

## Known Limitations

This tool uses a small local LLM (3B parameters), which has trade-offs:

- **Path/method confusion**: May occasionally conflate route paths with method names (e.g., `/add` method named `addStock()` may be documented as `/addStock`)
- **DTO field visibility**: Cannot inspect referenced DTO classes - field names must be cross-referenced manually
- **Inference markers**: Generated docs flag inferred details, but human review is recommended

**For production use:** Swap the backend to Claude API or GPT-4 by modifying the request format in `generate_docs.py`. Larger models significantly reduce these issues.

## Roadmap

- [ ] Multi-file batch processing
- [ ] Claude API backend support  
- [ ] OpenAPI/Swagger spec output
- [ ] HTML output format
- [ ] DTO class resolution for accurate field documentation
- [ ] CI/CD integration (GitHub Actions)

## Author

**Leo (Ravi Kiran)** - Senior Software Engineer  
Exploring practical AI integration in developer workflows.

[GitHub](https://github.com/ravikiran134) · [LinkedIn](https://www.linkedin.com/in/ravi-kiran-thalluri)

## License

MIT