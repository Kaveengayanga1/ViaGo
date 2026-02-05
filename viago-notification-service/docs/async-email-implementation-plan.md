# Implement Async Email Sending

## Overview

Implementing asynchronous email sending to improve API responsiveness and application performance. Email sending is an I/O-bound operation that can take several seconds - by making it async, we prevent blocking HTTP responses while emails are being sent.

**Benefits:**
- ⚡ Faster API response times
- 🔄 Better resource utilization
- 📈 Higher throughput for concurrent requests
- 🛡️ Isolated email failures don't block application flow

## Proposed Changes

### Enable Async Support

#### [MODIFY] [Main.java](file:///d:/becs/year_03/semester_06/becs_32263_full_stack/project/ViaGO/backend/viago-notification-service/src/main/java/com/viago/service/Main.java)

Add `@EnableAsync` annotation to enable Spring's async processing:
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class Main { ... }
```

---

### Async Configuration

#### [NEW] [AsyncConfig.java](file:///d:/becs/year_03/semester_06/becs_32263_full_stack/project/ViaGO/backend/viago-notification-service/src/main/java/com/viago/service/config/AsyncConfig.java)

Create custom async configuration with dedicated thread pool:
- Core pool size: 5 threads
- Max pool size: 10 threads
- Queue capacity: 100 tasks
- Thread name prefix: "email-async-"
- Graceful shutdown on application stop

This prevents email sending from consuming all application threads.

---

### Update Email Service

#### [MODIFY] [EmailService.java](file:///d:/becs/year_03/semester_06/becs_32263_full_stack/project/ViaGO/backend/viago-notification-service/src/main/java/com/viago/service/email/EmailService.java)

Add `@Async` annotation to all email methods:
- `sendSimpleEmail()` - Returns `CompletableFuture<Void>`
- `sendHtmlEmail()` - Returns `CompletableFuture<Void>`
- `sendNotification()` - Returns `CompletableFuture<Void>`

Methods will execute in separate thread pool, freeing up request threads immediately.

---

### Update Controllers

#### [MODIFY] [NotificationController.java](file:///d:/becs/year_03/semester_06/becs_32263_full_stack/project/ViaGO/backend/viago-notification-service/src/main/java/com/viago/service/controller/NotificationController.java)

Controllers will call async methods without waiting for completion:
- Email sending happens in background
- Immediate response to client: "Email queued for sending"
- Actual sending status logged asynchronously

#### [MODIFY] [TestEmailController.java](file:///d:/becs/year_03/semester_06/becs_32263_full_stack/project/ViaGO/backend/viago-notification-service/src/main/java/com/viago/service/controller/TestEmailController.java)

Update test controller to work with async methods.

---

## Verification Plan

### Testing

1. **Response Time Test:**
   - Send email via API
   - Verify response returns immediately (< 100ms)
   - Check logs to confirm email sent in background thread

2. **Concurrent Load Test:**
   ```bash
   # Send 10 emails concurrently
   for i in {1..10}; do
     curl -X POST http://localhost:8083/api/notify/send \
       -H "Content-Type: application/json" \
       -d '{"to":"test@example.com","type":"WELCOME","data":{"name":"User'$i'"}}' &
   done
   ```
   - All requests should return quickly
   - Check logs for parallel execution in thread pool

3. **Error Handling Test:**
   - Send email with invalid address
   - Verify API still returns success
   - Check logs for async error handling

### Expected Behavior

**Before (Sync):**
- Request time: ~2-5 seconds (waiting for SMTP)
- Thread blocked during email send

**After (Async):**
- Request time: < 100ms
- Email sent in background
- Log message: "Email queued for sending"
