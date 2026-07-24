# Security notes

- Agent API keys stored as SHA-256 hashes only
- Never commit real secrets; use .env.example
- Nginx adds nosniff and frame deny headers
- Prod profile hides actuator details
- Request IDs support incident correlation
