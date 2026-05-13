import requests
import sys
import os

OLLAMA_URL = "http://localhost:11434/api/generate"
MODEL = "qwen2.5-coder:3b"

PROMPT_TEMPLATE = """You are an API documentation expert. Analyze this Spring Boot controller and generate accurate API documentation in markdown format.

CRITICAL RULES:
1. ONLY document what you can see in the code - DO NOT invent endpoints, parameters, or request bodies
2. If a method has no @RequestBody, do NOT include a request body section
3. If you cannot determine the exact field names of DTOs, note this with: "_See DTO class for exact fields_"
4. Mark inferred information clearly with "(inferred)"
5. Skip helper methods, fields, loggers, and constructors

For EACH endpoint, document:
- ## [HTTP_METHOD] [PATH]
- **Description:** What the endpoint does (based on method name and code)
- **Authentication:** Required roles from @PreAuthorize, or "None" if not specified
- **Path Variables:** From @PathVariable annotations
- **Query Parameters:** From @RequestParam annotations  
- **Request Body:** Only if @RequestBody is present, reference the DTO class
- **Response:** Based on ResponseEntity return type
- **Status Codes:** Based on HttpStatus values in the code
- **Example:** Realistic curl command

JAVA CODE:
```java
{code}
```

Generate accurate markdown documentation now. Remember: do not invent anything not in the code."""


def call_ollama(prompt):
    """Send prompt to Ollama and return response."""
    print("Calling Ollama (this may take 30-90 seconds)...")
    response = requests.post(
        OLLAMA_URL,
        json={
            "model": MODEL,
            "prompt": prompt,
            "stream": False
        },
        timeout=300  # 5 min timeout
    )
    response.raise_for_status()
    return response.json()['response']


def generate_docs(java_file_path):
    """Read Java file and generate API docs."""
    # Read the Java code
    with open(java_file_path, 'r', encoding='utf-8') as f:
        java_code = f.read()
    
    print(f"Read {len(java_code)} characters from {java_file_path}")
    
    # Build prompt
    prompt = PROMPT_TEMPLATE.format(code=java_code)
    
    # Call LLM
    documentation = call_ollama(prompt)
    
    # Write output
    output_dir = "output"
    os.makedirs(output_dir, exist_ok=True)
    
    filename = os.path.basename(java_file_path).replace('.java', '.md')
    output_path = os.path.join(output_dir, filename)
    
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(f"# API Documentation: {filename.replace('.md', '')}\n\n")
        f.write(documentation)
    
    print(f"\n✓ Documentation generated: {output_path}")
    return output_path


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python generate_docs.py <path-to-java-file>")
        print("Example: python generate_docs.py samples/UserController.java")
        sys.exit(1)
    
    java_file = sys.argv[1]
    
    if not os.path.exists(java_file):
        print(f"Error: File not found: {java_file}")
        sys.exit(1)
    
    generate_docs(java_file)