# Drug Interactions API: initial implementation with openFDA integration

## Overview
This PR implements a REST API for drug interaction analysis, combining openFDA adverse event signals with user-contributed notes. The implementation follows hexagonal architecture principles for maintainability and testability.

## Key Features

### REST API Endpoints

1. Fetch Drug Interaction Signals
```http
GET /api/interactions/signals?drugA={drug}&drugB={drug}
Accept: application/json
```
- Queries openFDA for adverse event signals
- Optional `limit` parameter (default: 10)
- Returns top reactions by frequency
- Example: `/api/interactions/signals?drugA=warfarin&drugB=amiodarone&limit=50`

2. Save Interaction Notes
```http
POST /api/interactions
Content-Type: application/json

{
  "drugA": "warfarin",
  "drugB": "amiodarone",
  "note": "Increased anticoagulation effect, requires close monitoring"
}
```

3. Retrieve Saved Notes
```http
GET /api/interactions?drugA={drug}&drugB={drug}
```

### Input Validation
- Drug names: 3-60 characters, letters/spaces/hyphens only
- RFC 7807 Problem Details for validation errors
- Sanitization of drug names before FDA API calls

## Architecture

### Hexagonal Architecture Implementation
- **Domain Layer**: Pure business logic, defines ports
  - `DrugInteractionService`
  - `OpenFdaClient` port
  - `DrugInteractionRepository` port

- **Adapters Layer**: External integrations
  - `OpenFdaAdapter`: Reactive WebClient integration
  - `InMemoryDrugInteractionRepository`: Thread-safe storage

- **Application Layer**: REST API and configuration
  - Validation
  - Error handling
  - DTOs and mapping

### SOLID Principles
- **Single Responsibility**: Each adapter handles one concern
- **Open/Closed**: New storage or API adapters can be added
- **Liskov Substitution**: Adapters properly implement ports
- **Interface Segregation**: Focused port interfaces
- **Dependency Inversion**: Dependencies point inward

## Resilience Features
- Exponential backoff retry for FDA API
- Circuit breaking for outages
- Configurable timeouts
- Thread-safe in-memory storage

## Testing
- Unit tests: Domain logic, validation
- Integration tests: OpenFDA adapter with WireMock
- API tests: End-to-end validation
- Test fixtures for reuse

## Configuration
```yaml
openfda:
  client:
    base-url: https://api.fda.gov
    connect-timeout: 5s
    read-timeout: 10s
    retry:
      max-attempts: 3
      initial-interval: 100ms
      max-interval: 1s
      multiplier: 2.0
```

## TODOs
- [ ] Add caching for FDA API responses
- [ ] Implement rate limiting
- [ ] Add API documentation with OpenAPI
- [ ] Add metrics and monitoring
