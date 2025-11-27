# ViaGO Admin Services

This module is now a Spring Boot 3 service that proxies management actions to `viago-auth`. Instead of owning its own database, it calls the Auth service over HTTP for every user-oriented task.

## Implemented capabilities
- List all users or filter by role with optional pagination (`GET /admin/users`)
- Look up a user by email (`GET /admin/users/{email}`)
- Update core fields such as username/email (`PUT /admin/users`)
- Delete users by id or email (`DELETE /admin/users/{id}` or `DELETE /admin/users?email=...`)
- Suspend/activate accounts by toggling the `enabled` flag in Auth (`PATCH /admin/users/{id}/suspend` or `/activate`)

All DTOs mirror the Auth service payloads, including the `enabled` field.

## How it communicates with `viago-auth`
1. `auth-service.base-url` (see `application.yml`) points to the running Auth instance.
2. Every controller call is routed through `AuthServiceClient`, which issues REST calls with a shared admin token (Bearer header) if configured.
3. Responses are deserialized back into the shared `UserResponse<T>` wrappers so the Admin SPA sees the same shapes as Auth.
4. JWTs for end-user/admin authentication are minted by Auth. The admin SPA should:
   - call `viago-auth` to log in and obtain a JWT + refresh token
   - include that JWT when calling `viago-admin-services`
   - the admin service should validate that JWT using Auth’s public key (to be implemented in `SecurityConfig`) before allowing access.

## Suggested next features
- **Bulk actions**: accept CSV/JSON payloads to suspend batches of drivers or approve riders.
- **Audit trail**: record which admin performed each change by emitting events to Kafka or persisting to an `admin_audit` table.
- **Moderation queue**: endpoints for reviewing driver/rider reports, flagging fraud, and assigning cases.
- **Read models**: cache frequently accessed aggregates (e.g., daily signup counts per role) via Redis for dashboard speed.
- **Notification hooks**: trigger emails/SMS when accounts are locked or reactivated by calling a future notification service.

Once these additions are planned, split cross-service contracts (DTOs/events) into a dedicated `shared/contracts` module so Auth and Admin stay in sync without manual copying.

