# AGENTS.md -- realworld-api

This directory contains the Quarkus RealWorld backend API application.

## Boundaries

- Own REST API resources, application services, domain entities, persistence adapters, and API configuration for future RealWorld behavior.
- Use Quarkus REST JSON-B for HTTP JSON endpoints.
- Use the approved JNoSQL MongoDB extension for future MongoDB persistence work unless a later SLDD step changes that decision.
- Do not add dependencies on `realworld-api-st`.
- Follow `docs/domain-model.md` before changing entities, DTOs, repositories, validation rules, persistence mappings, or relationship behavior.

## Current Scaffold Contract

- Generated starter resources are scaffold code only and are not part of the RealWorld API contract.
- Authentication, users, profiles, articles, comments, favorites, feeds, and tags are deferred to later SLDD workflows.
- Keep implementation changes minimal and traceable to approved SLDD artifacts.

## Verification

- Build this project from this directory with Maven.
- Keep tests inside this project focused on API internals and API integration behavior.
