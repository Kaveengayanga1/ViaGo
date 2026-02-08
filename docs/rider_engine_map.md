# Viago Rider Engine - Completion Map & Service Guide

This document outlines the roadmap to complete the `viago-rider-engine` and explains the specific purpose of each service component.

## 🏗️ Current State Analysis
- **Structure**: The project follows a standard layered architecture (`Controller` -> `Service` -> `Repository` -> `Database`).
- **Services**: Service interfaces exist, but implementations (`impl/*.java`) are currently **stubs** returning hardcoded/dummy data (`Object` return types).
- **Data Models**: Entities (`DriverEntity`, `TripEntity`, etc.) exist, which is a good starting point.
- **Missing**: Real business logic, database interactions, inter-service communication, and integration with external APIs (Maps, Payments).

## 🧩 Service Purposes & Responsibilities

### 1. `DriverMatchingService`
*   **Purpose**: The "brain" of the dispatch system. It matches a trip request with the most suitable driver.
*   **Key Responsibilities**:
    *   Finding candidates based on location (radius search).
    *   Filtering available/online drivers.
    *   Ranking drivers (ETA, rating, vehicle type).
    *   Managing the "offer" lifecycle (sending offer -> driver accepts/rejects).

### 2. `TripService`
*   **Purpose**: Manages the lifecycle and state of a ride.
*   **Key Responsibilities**:
    *   **State Machine**: `REQUESTED` -> `SEARCHING` -> `ASSIGNED` -> `ARRIVED` -> `IN_PROGRESS` -> `COMPLETED` / `CANCELLED`.
    *   Storing trip history.
    *   Validating status transitions (e.g., can't start a trip that hasn't been assigned).

### 3. `LocationService`
*   **Purpose**: Handles real-time geospatial data.
*   **Key Responsibilities**:
    *   **Ingestion**: Receiving driver GPS updates (high frequency).
    *   **Storage**: Storing latest locations (likely in **Redis/Geospatial** for speed, not just SQL).
    *   **Queries**: Finding "drivers within X km" (Used by Matching Service).
    *   **Tracking**: Broadcasting location to the assigned rider (WebSocket/PubSub).

### 4. `PricingService`
*   **Purpose**: Calculates the cost of the ride.
*   **Key Responsibilities**:
    *   **Estimation**: Upfront price before booking (Base + Distance * Rate + Time * Rate).
    *   **Dynamic/Surge Pricing**: Adjusting rates based on demand/supply.
    *   **Final Calculation**: Actual cost based on actual route taken.

### 5. `RoutingService`
*   **Purpose**: helper for navigation and distance/time calculations.
*   **Key Responsibilities**:
    *   Integration with Maps APIs (Google Maps, Mapbox, OSRM).
    *   Calculating Polyline (route path) to display on map.
    *   Estimating ETA (Time) and Distance for pricing.

### 6. `PaymentService`
*   **Purpose**: Handles financial transactions.
*   **Key Responsibilities**:
    *   Interfacing with Payment Gateway (Stripe, PayPal, etc.).
    *   Processing driver payouts.
    *   Handling refunds or cancellation fees.

### 7. `NotificationService`
*   **Purpose**: Keeps users informed.
*   **Key Responsibilities**:
    *   Push Notifications (FCM/APNS) for "Driver Found", "Arriving Now", etc.
    *   In-app updates via WebSockets.
    *   SMS/Email backups (optional).

### 8. `UserService`
*   **Purpose**: Manages Rider and Driver profiles within this context.
*   **Key Responsibilities**:
    *   Retrieving profile details (name, rating, vehicle info).
    *   Updating ratings/reviews after trips.
    *   Updating preferences (saved addresses).

---

## 🗺️ Completion Roadmap

### Phase 1: Foundation Refactoring (High Priority)
1.  **Type Safety**: Remove `Object` return types from Services and Controllers. Create specific DTOs (e.g., `TripRequestDTO`, `DriverMatchResponseDTO`).
2.  **Database Layer**: Ensure JpaRepositories are properly defined for all Entities.
3.  **Configuration**: Verify database connection (PostgreSQL/MySQL) and Redis configuration.

### Phase 2: Core Logic Implementation
4.  **Trip Lifecycle**: Implement `TripService` to handle creation and status updates using the Database.
5.  **Location Tracking**: Implement `LocationService` using **Redis GEO** commands to store and retrieve active driver locations.
6.  **Basic Matching**: Implement `DriverMatchingService` to query `LocationService` for nearby drivers and assign one to a trip.

### Phase 3: Integration & Computation
7.  **Routing**: Connect `RoutingService` to a real map provider (or mock it accurately for dev) to get distances.
8.  **Pricing**: Implement the pricing formula in `PricingService` using data from `RoutingService`.
9.  **Controller Wiring**: Update Controllers to pass real data to/from these services.

### Phase 4: Polish
10. **Exception Handling**: Global exception handler for "DriverNotFound", "PaymentFailed", etc.
11. **Concurrency**: Ensure driver matching is thread-safe (prevent two trips grabbing the same driver).
