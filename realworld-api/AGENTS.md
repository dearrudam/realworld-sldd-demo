# Agent Instructions

Scope: `realworld-api/`.

- This Quarkus application owns the RealWorld backend API.
- Use `org.soujava.demo.sldd` as the Java package root unless a later approved SLDD design changes it.
- Keep REST resources in the API project only; system-test clients belong in `realworld-api-st`.
- Keep MongoDB/JNoSQL usage inside the API project.
- Do not introduce RealWorld endpoint behavior before an approved SLDD workflow defines acceptance criteria and tests.
