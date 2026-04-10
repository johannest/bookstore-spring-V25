# Bookstore Example — Vaadin 25 (Flow)

## Project Overview

A Vaadin Flow bookstore demo application. Pure Java UI (no Spring Boot) running on an embedded Jetty servlet container. Demonstrates Vaadin's MVP pattern, routing, authentication, and data binding.

- **Vaadin version:** 25.2-SNAPSHOT
- **Java:** 21
- **Build/runtime:** Maven + Jetty 11 (WAR packaging)
- **Package root:** `org.vaadin.example.bookstore`

## Key Architecture

```
src/main/java/org/vaadin/example/bookstore/
├── authentication/       # AccessControl interface + BasicAccessControl (mock)
├── backend/
│   ├── DataService.java  # Abstract service (singleton pattern)
│   ├── data/             # Domain model: Product, Category, Availability
│   └── mock/             # In-memory MockDataService + MockDataGenerator
└── ui/
    ├── MainLayout.java   # AppLayout shell with drawer nav + logout
    ├── LoginScreen.java
    ├── AdminView.java    # Only accessible to "admin" role (registered dynamically)
    ├── inventory/        # InventoryView, InventoryViewLogic (MVP logic), ProductForm, ProductGrid, ProductDataProvider
    └── about/            # AboutView
```

**No database** — all data is in-memory via `MockDataService` (singleton).

## Authentication

Mock auth: any username where `password == username` is accepted. Username `admin` gets the admin role. Implemented in `BasicAccessControl`.

## Common Commands

```bash
# Run in development mode (hot reload, opens http://localhost:8080/)
mvn

# Full build
mvn install

# Production WAR
mvn package

# Run production WAR
mvn jetty:run-war

# Integration tests (requires TestBench license + Chrome)
mvn verify -Pit
```

## Frontend

- CSS in `src/main/frontend/styles/`
- `src/main/frontend/index.html` — entry point
- Webpack config present (`webpack.config.js`) alongside TypeScript config (`tsconfig.json`) — used by Vaadin's frontend build toolchain, not for standalone TS development

## Testing

- Unit tests: `src/test/java/...backend/DataServiceTest.java`
- Integration tests (TestBench/Selenium): `src/test/java/...crud/SampleCrudViewIT.java`, `LoginScreenIT.java`, `AboutViewIT.java`
- IT tests run under Maven profile `it` via `mvn verify -Pit`

## Notes

- `AdminView` is registered into the session-scoped route registry dynamically at login (not statically annotated) — only admins see it in the nav
- `ProductDataProvider` extends Vaadin's `ListDataProvider` for grid filtering
- `InventoryViewLogic` holds all view logic separately from the `InventoryView` component (MVP pattern)
