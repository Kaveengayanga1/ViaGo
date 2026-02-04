# 🎯 ViaGO Project Completion Roadmap

> **Comprehensive checklist of features to implement for completing the ViaGO ride-sharing platform**

---

## 📊 Current Project Status

| Service | Status | Completion |
|---------|--------|------------|
| **viago-auth** | ✅ Mostly Complete | ~85% |
| **viago-rider-engine** | 🔴 Stubs Only | ~15% |
| **viago-admin-services** | 🟡 Proxy Ready | ~60% |
| **viago-service-discovery** | ✅ Complete | 100% |

---

## ✅ Completed Features

### Auth Service (viago-auth)
- [x] User signup/registration
- [x] User login with JWT
- [x] Google OAuth2 login/signup
- [x] JWT token generation and validation
- [x] User entity and repository
- [x] Eureka service registration

### Admin Services (viago-admin-services)
- [x] Proxy to Auth service
- [x] List users with pagination
- [x] Find user by email
- [x] Update user details
- [x] Delete users
- [x] Suspend/Activate accounts

### Service Discovery (viago-service-discovery)
- [x] Eureka Server configuration

---

## 🔴 Features TODO (By Priority)

### 🚨 HIGH PRIORITY - Core Business Logic

#### 1. Trip Service Implementation
**File:** `viago-rider-engine/.../service/impl/TripServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `createTrip()` | Create trip request with pickup/dropoff | 4h |
| [ ] `getTripById()` | Fetch trip by ID | 1h |
| [ ] `getUserTrips()` | Get all trips for user | 2h |
| [ ] `updateTripStatus()` | Update trip status (REQUESTED→COMPLETED) | 2h |
| [ ] `cancelTrip()` | Cancel with reason | 2h |
| [ ] `getActiveTrip()` | Find user's current active trip | 1h |
| [ ] `completeTrip()` | Mark trip complete, trigger payment | 2h |
| [ ] `startTrip()` | Driver starts trip | 1h |
| [ ] `getDriverActiveTrip()` | Get driver's current trip | 1h |
| [ ] `getDriverTrips()` | Driver trip history | 2h |

**Database Requirements:**
- Create `trips` table with proper indexes
- Add trip state machine validation

---

#### 2. Driver Matching Service
**File:** `viago-rider-engine/.../service/impl/DriverMatchingServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `matchDriverForTrip()` | Find nearest available driver | 4h |
| [ ] `setDriverAvailability()` | Toggle driver online/offline | 1h |
| [ ] `acceptTrip()` | Driver accepts ride request | 2h |
| [ ] `rejectTrip()` | Driver rejects with reason | 1h |
| [ ] `getNearbyTripRequests()` | Trips near driver location | 2h |

**Algorithm Required:**
- Geospatial query for nearby drivers
- Driver scoring (rating, acceptance rate, distance)

---

#### 3. Location Service
**File:** `viago-rider-engine/.../service/impl/LocationServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `updateDriverLocation()` | Store GPS coordinates | 2h |
| [ ] `updatePassengerLocation()` | Store passenger GPS | 1h |
| [ ] `getDriverLocation()` | Fetch current driver position | 1h |
| [ ] `getPassengerLocation()` | Fetch passenger position | 1h |
| [ ] `getTripLocations()` | All participants' locations | 1h |
| [ ] `getNearbyDrivers()` | Geo-query for drivers | 3h |
| [ ] `getDriverLocationHistory()` | Historical tracking | 2h |
| [ ] `subscribeToLocationUpdates()` | WebSocket subscription | 4h |

**Infrastructure Required:**
- Redis for real-time location caching
- Consider PostGIS or MongoDB for geospatial queries

---

### 🟠 MEDIUM PRIORITY - Revenue & Communication

#### 4. Payment Service
**File:** `viago-rider-engine/.../service/impl/PaymentServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `processPayment()` | Integrate payment gateway | 6h |
| [ ] `addPaymentMethod()` | Store card/wallet | 3h |
| [ ] `getPaymentMethods()` | List saved methods | 1h |
| [ ] `setDefaultPaymentMethod()` | Set default card | 1h |
| [ ] `removePaymentMethod()` | Delete payment method | 1h |
| [ ] `getPaymentHistory()` | Transaction history | 2h |
| [ ] `getTripPayment()` | Get trip's payment | 1h |
| [ ] `refundPayment()` | Process refunds | 3h |
| [ ] `getPaymentStatus()` | Check payment state | 1h |
| [ ] `handleWebhook()` | Payment gateway callbacks | 3h |

**Integration Options:**
- Stripe API
- PayPal SDK
- Local payment gateways (for Sri Lanka)

---

#### 5. Notification Service
**File:** `viago-rider-engine/.../service/impl/NotificationServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `sendNotification()` | Push via FCM/WebSocket | 4h |
| [ ] `sendTripNotification()` | Trip-specific alerts | 2h |
| [ ] `registerDevice()` | Store FCM token | 2h |
| [ ] `unregisterDevice()` | Remove device token | 1h |
| [ ] `getNotificationPreferences()` | User preferences | 1h |
| [ ] `updateNotificationPreferences()` | Update settings | 1h |
| [ ] `getNotificationHistory()` | Past notifications | 2h |
| [ ] `markAsRead()` | Mark notification read | 1h |
| [ ] `markAllAsRead()` | Bulk mark as read | 1h |
| [ ] `sendBulkNotifications()` | Mass notifications | 3h |

**Integration Required:**
- Firebase Cloud Messaging (FCM)
- WebSocket for real-time updates

---

#### 6. Pricing Service
**File:** `viago-rider-engine/.../service/impl/PricingServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `calculateFareEstimate()` | Basic fare calculation | 3h |
| [ ] `calculateFareEstimateWithRoute()` | Fare with route details | 2h |
| [ ] `calculateFinalFare()` | Actual trip fare | 2h |
| [ ] `getFareBreakdown()` | Itemized breakdown | 2h |
| [ ] `applyDiscount()` | Promo code logic | 3h |
| [ ] `getSurgePricing()` | Dynamic pricing | 4h |

**Formula to Implement:**
```
Fare = Base Fare + (Distance × Per KM Rate) + (Duration × Per Min Rate) × Surge Multiplier - Discount
```

---

#### 7. Routing Service
**File:** `viago-rider-engine/.../service/impl/RoutingServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `getRoute()` | Get route between points | 3h |
| [ ] `getRouteByCoordinates()` | Route by lat/lng | 2h |
| [ ] `getETA()` | Estimated time of arrival | 2h |
| [ ] `getETAByCoordinates()` | ETA by coordinates | 1h |
| [ ] `getTripRoute()` | Saved trip route | 1h |
| [ ] `getDriverETA()` | Driver arrival time | 2h |
| [ ] `getAlternativeRoutes()` | Multiple route options | 3h |
| [ ] `calculateDistance()` | Distance calculation | 2h |
| [ ] `calculateDuration()` | Duration estimation | 2h |

**API Integration Required:**
- Google Maps Directions API
- OpenRouteService (free alternative)
- Mapbox API

---

### 🟢 LOW PRIORITY - User Experience

#### 8. User Service (Rider Engine)
**File:** `viago-rider-engine/.../service/impl/UserServiceImpl.java`

| Task | Description | Effort |
|------|-------------|--------|
| [ ] `getProfile()` | User profile details | 1h |
| [ ] `updateProfile()` | Update user info | 2h |
| [ ] `getUserInfo()` | Basic user info | 1h |
| [ ] `getUserStatistics()` | Trips, spending stats | 3h |
| [ ] `updatePreferences()` | User preferences | 1h |
| [ ] `getPreferences()` | Fetch preferences | 1h |
| [ ] `getSavedAddresses()` | Home, work, etc. | 1h |
| [ ] `addSavedAddress()` | Add new address | 1h |
| [ ] `removeSavedAddress()` | Delete address | 1h |
| [ ] `updateProfilePicture()` | Upload avatar | 2h |

---

## 🏗️ Infrastructure Requirements

### Database Setup
```sql
-- Required Tables for viago-rider-engine
CREATE TABLE trips (
    id UUID PRIMARY KEY,
    passenger_id VARCHAR(255),
    driver_id VARCHAR(255),
    pickup_lat DECIMAL(10,8),
    pickup_lng DECIMAL(11,8),
    dropoff_lat DECIMAL(10,8),
    dropoff_lng DECIMAL(11,8),
    status VARCHAR(50),
    fare DECIMAL(10,2),
    created_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE TABLE drivers (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255),
    vehicle_type VARCHAR(50),
    license_plate VARCHAR(20),
    is_available BOOLEAN,
    current_lat DECIMAL(10,8),
    current_lng DECIMAL(11,8),
    rating DECIMAL(3,2)
);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    trip_id UUID REFERENCES trips(id),
    amount DECIMAL(10,2),
    status VARCHAR(50),
    payment_method VARCHAR(50),
    transaction_id VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    user_id VARCHAR(255),
    title VARCHAR(255),
    body TEXT,
    type VARCHAR(50),
    is_read BOOLEAN,
    created_at TIMESTAMP
);
```

### External Services to Integrate
| Service | Purpose | Priority |
|---------|---------|----------|
| Google Maps API | Routing, ETA, geocoding | High |
| Firebase FCM | Push notifications | High |
| Stripe/PayPal | Payment processing | High |
| Redis | Location caching | Medium |
| WebSocket | Real-time updates | Medium |
| Kafka | Event streaming (optional) | Low |

---

## 📋 Implementation Order (Recommended)

### Phase 1: Core Trip Flow (Week 1-2)
1. Database schema setup
2. Trip Service implementation
3. Driver Matching Service
4. Basic Location Service

### Phase 2: Money & Communication (Week 3-4)
5. Pricing Service
6. Payment Service (Stripe integration)
7. Notification Service (FCM)

### Phase 3: Maps & Routes (Week 5)
8. Routing Service (Google Maps)
9. Advanced Location features

### Phase 4: Polish (Week 6)
10. User Service enhancements
11. Testing and bug fixes
12. Admin dashboard improvements

---

## 📊 Effort Estimation

| Category | Tasks | Estimated Hours |
|----------|-------|-----------------|
| Trip Management | 10 | ~18h |
| Driver Matching | 5 | ~10h |
| Location | 8 | ~15h |
| Payment | 10 | ~22h |
| Notification | 10 | ~18h |
| Pricing | 6 | ~16h |
| Routing | 9 | ~18h |
| User Service | 10 | ~14h |
| **Total** | **68** | **~131 hours** |

---

## 🚀 Quick Win: Start Here

1. **Set up PostgreSQL database** with the schema above
2. **Implement `TripServiceImpl.createTrip()`** - This unlocks the core flow
3. **Add Redis** for location caching
4. **Implement basic `LocationServiceImpl`** methods

Would you like me to start implementing any specific service?
