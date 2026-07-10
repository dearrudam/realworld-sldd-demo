/// # Health
/// > Provide application-wide liveness and readiness signals.
///
/// ## Boundary
/// - `check-liveness` — report whether the application process is live
/// - `check-readiness` — report whether the application is ready to receive work
///
/// ## Requirements
/// ### R1: Check liveness
/// - R1.1 — When liveness is checked, the BC shall report the application as live.
///
/// ### R2: Check readiness
/// - R2.1 — When readiness is checked, the BC shall report the application as ready.
///
/// ## Out of scope
/// - BC-specific health checks, dependency-specific diagnostics, and detailed operational metrics.
package dearrudam.realworld.health;
