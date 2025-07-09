# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Architecture Overview

This is a full-stack education platform called "Spot Teacher" with:

- **Backend**: Kotlin/Spring Boot with GraphQL API
  - Multi-module Gradle project structure
  - Admin and Teacher modules
  - Uses Spring WebFlux (reactive), JOOQ, R2DBC, Spring Security
  - GraphQL schema generation with Expedia GraphQL Kotlin
  - Database: MySQL with Flyway migrations
  - Testing: Kotest framework

- **Frontend**: Next.js 15 with React 19
  - TypeScript with Apollo GraphQL client
  - UI components built with Radix UI and Tailwind CSS
  - Form handling with React Hook Form and Zod validation
  - GraphQL code generation from backend schema

## Development Commands

### Backend Development
```bash
# Build project locally
make build-local

# Run with Docker Compose (includes MySQL)
make run

# Database operations
make db-migrate           # Run migrations + code generation
make mysql-reset         # Reset local MySQL database
make db-repair           # Repair Flyway migration issues

# Code generation
make jooq-codegen        # Generate JOOQ database classes
make admin-graphql-codegen # Generate GraphQL schema

# Code quality
make detekt              # Run Kotlin linting with auto-correct
./gradlew test           # Run tests
```

### Frontend Development
```bash
cd frontend

# Development server
npm run dev              # Next.js dev server with Turbopack
npm run local            # Use .env.local environment

# Production build
npm run build
npm run start

# Code quality
npm run lint
```

## Project Structure

### Backend Module Organization
- `backend/admin/` - Admin GraphQL API server
- `backend/teacher/` - Teacher-specific functionality
- `backend/shared/` - Common domain models and utilities
- `backend/db/` - Database schema and JOOQ generated code
- `backend/testUtil/` - Testing utilities

Each feature follows Clean Architecture:
- `domain/` - Business entities and repository interfaces
- `usecase/` - Application business logic
- `infra/` - Infrastructure implementations (repositories, external APIs)
- `handler/` - GraphQL resolvers (queries/mutations)

### Frontend Structure
- `src/app/` - Next.js App Router pages
  - `admin/` - Admin interface pages
  - `teacher/` - Teacher interface pages
  - `auth/` - Authentication pages
- `src/components/` - Reusable UI components
- `src/gql/` - Generated GraphQL types and queries
- `src/lib/` - Utilities and configurations
- `src/hooks/` - Custom React hooks

## Key Technologies

### Backend Stack
- **Kotlin 2.1.20** with Spring Boot 3.4.4
- **GraphQL** with Expedia GraphQL Kotlin
- **Database**: MySQL 8.0 with JOOQ 3.20.4 for type-safe queries
- **Migration**: Flyway 11.8.2
- **Security**: Spring Security with JWT authentication
- **Cloud**: AWS SDK for S3, CloudFront integration
- **Testing**: Kotest 5.9.1, SpringMockK

### Frontend Stack
- **Next.js 15.3.2** with React 19
- **GraphQL**: Apollo Client 3.13.8 with code generation
- **UI**: Radix UI components, Tailwind CSS 4, Framer Motion
- **Forms**: React Hook Form with Zod validation
- **Node**: 22.14.0 (managed by Volta)

## Database Schema
The system manages:
- Admin users and authentication
- Companies and schools
- Products and lesson plans
- Teachers and students
- File uploads (S3 integration)
- Lesson reports and reservations

## GraphQL Development
- Backend generates schema automatically from Kotlin types
- Frontend uses codegen to generate TypeScript types from schema
- Schema file: `backend/admin/graphql/schema.graphql`
- Frontend codegen config: `frontend/codegen.ts`

## Environment Setup
- Local MySQL runs via Docker Compose on port 3306
- Frontend dev server runs on standard Next.js port
- Backend admin server configuration in `application-*.yml` files
- Use `.env.local` for frontend local development overrides

## Testing
- Backend: Run `./gradlew test` for all modules
- Kotest framework with Spring integration
- Database tests use test containers or test profiles
- Frontend: Standard Jest/React Testing Library (configured via Next.js)