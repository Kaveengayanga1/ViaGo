# Google OAuth2 Implementation Plan

## Overview

Implement Google OAuth2 authentication with separate `/auth/google/login` and `/auth/google/signup` endpoints. Only RIDER role users can register/login via Google OAuth. After successful authentication, users receive JWT tokens like regular authentication.

## Implementation Steps

### 1. Add Dependencies

- Add `spring-boot-starter-oauth2-client` dependency to `pom.xml`

### 2. Update UserEntity

- Add `provider` field (String) to store "google"
- Add `providerId` field (String) to store Google user ID (sub)

### 3. Update UserRepository

- Add method: `Optional<UserEntity> findByEmailAndProvider(String email, String provider)`
- Add method: `Optional<UserEntity> findByProviderId(String providerId)`

### 4. Create OAuth2 Configuration Classes

- **OAuth2UserService** (`config/OAuth2UserService.java`):
- Extends `DefaultOAuth2UserService`
- Extracts email, name, and provider ID from Google OAuth2 response
- Stores provider info in session/request for later use in handlers

- **OAuth2LoginSuccessHandler** (`config/OAuth2LoginSuccessHandler.java`):
- Handles OAuth2 callback for login flow
- Finds existing user by email and provider="google"
- Validates user has RIDER role
- Generates JWT token and returns in response

- **OAuth2SignupSuccessHandler** (`config/OAuth2SignupSuccessHandler.java`):
- Handles OAuth2 callback for signup flow
- Creates new user with RIDER role if email doesn't exist
- Stores provider and providerId
- Generates JWT token and returns in response

- **OAuth2FailureHandler** (`config/OAuth2FailureHandler.java`):
- Handles OAuth2 authentication failures
- Returns error response

### 5. Update SecurityConfig

- Add OAuth2 login configuration with two separate success handlers
- Use state parameter to distinguish between login and signup flows
- Allow OAuth2 callback endpoints: `/oauth2/**`, `/login/oauth2/**`

### 6. Add OAuth2 Configuration to YAML

- Add Google OAuth2 client configuration to `application-dev.yml`:
- Use `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` from environment
- Configure redirect URIs for login and signup flows
- Set scopes: email, profile

### 7. Add Controller Endpoints

- Update `AuthController.java`:
- Add `GET /auth/google/login` - redirects to Google OAuth with login state
- Add `GET /auth/google/signup` - redirects to Google OAuth with signup state

### 8. Update UserService (if needed)

- Ensure OAuth2 users are created with RIDER role only
- Handle username generation from Google name/email

## Key Implementation Details

- **State Parameter**: Use OAuth2 state parameter to distinguish between login and signup flows
- **Role Restriction**: Only allow RIDER role for OAuth2 users
- **JWT Token**: Generate same JWT tokens as regular authentication
- **Provider Storage**: Store "google" in provider field and Google sub in providerId field
- **Error Handling**: Return appropriate error messages for non-RIDER roles or existing users in signup

## Files to Modify/Create

**New Files:**

- `src/main/java/com/viago/auth/config/OAuth2UserService.java`
- `src/main/java/com/viago/auth/config/OAuth2LoginSuccessHandler.java`
- `src/main/java/com/viago/auth/config/OAuth2SignupSuccessHandler.java`
- `src/main/java/com/viago/auth/config/OAuth2FailureHandler.java`

**Modified Files:**

- `pom.xml` - Add OAuth2 client dependency
- `src/main/java/com/viago/auth/entity/UserEntity.java` - Add provider fields
- `src/main/java/com/viago/auth/repository/UserRepository.java` - Add OAuth2 query methods
- `src/main/java/com/viago/auth/config/SecurityConfig.java` - Add OAuth2 configuration
- `src/main/java/com/viago/auth/controller/AuthController.java` - Add Google OAuth endpoints
- `src/main/resources/application-dev.yml` - Add OAuth2 client configuration
- `src/main/resources/application-prod.yml` - Add OAuth2 client configuration (if needed)