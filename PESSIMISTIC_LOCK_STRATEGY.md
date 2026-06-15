# Pessimistic Lock Strategy - Race Condition Prevention

## 🎯 Problem Statement

**Race Condition in Concurrent Booking Creation**

Khi 2 khách hàng cùng đặt lịch vào một khung giờ của cùng một salon:

```
Timeline:
T1: Request A checks availability ✓ (no conflicts found)
T2: Request B checks availability ✓ (no conflicts found) 
T3: Request A creates booking (Database saved)
T4: Request B creates booking (Database saved) ❌ DUPLICATE BOOKING!
```

Cả hai request đều pass qua check availability vì chúng chạy song song và chưa lock dữ liệu. Điều này dẫn đến **overbooking** - 2 booking cùng thời gian cho 1 salon.

---

## ✅ Solution: Pessimistic Lock (WRITE Lock)

### Cơ chế hoạt động:

```
Timeline with Pessimistic Lock:
T1: Request A acquires WRITE lock on conflicting rows
T2: Request B attempts to acquire WRITE lock → BLOCKED (waits)
T3: Request A checks availability (exclusive access)
T4: Request A creates booking & commits → lock released
T5: Request B acquires WRITE lock (now available)
T6: Request B checks availability → finds Request A's booking
T7: Request B throws exception → slot not available ✅
```

### Các thành phần được cập nhật:

#### 1. **Booking Entity** - Thêm @Version & Indexes
```java
@Version
private Long version;  // Hỗ trợ optimistic locking nếu cần

@Table(name = "bookings", indexes = {
    @Index(name = "idx_salon_start_end_time", columnList = "salonId,startTime,endTime")
})
```

**Tại sao?**
- `@Version` cho phép detect concurrent modifications nếu switch sang optimistic locking
- Composite index trên `(salonId, startTime, endTime)` tối ưu hóa query hiệu suất khi lock

---

#### 2. **BookingRepo** - Thêm Locked Queries

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT b FROM Booking b WHERE b.salonId = :salonId 
    AND b.status != 'CANCELLED' 
    AND b.startTime < :endTime AND b.endTime > :startTime")
List<Booking> findConflictingBookingsWithLock(
    Long salonId, LocalDateTime startTime, LocalDateTime endTime);
```

**Tại sao PESSIMISTIC_WRITE?**
- `PESSIMISTIC_WRITE`: Lock độc quyền, ngăn chặn cả read và write từ transaction khác
- Không phải `PESSIMISTIC_READ`: vì cần bảo vệ khỏi cả write operations
- SQL sẽ thêm `FOR UPDATE` → Database level lock (MySQL: `SELECT ... FOR UPDATE`)

---

#### 3. **BookingServiceImpl** - Transactional với Isolation Level

```java
@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = false)
public BookingResponse createBooking(...) {
    Boolean isAvailable = isTimeSlotAvailableWithLock(...);
    // ... create booking ...
}
```

**Tại sao SERIALIZABLE?**
- `READ_UNCOMMITTED` ❌ Dirty reads có thể xảy ra
- `READ_COMMITTED` ❌ Non-repeatable reads (booking bị change giữa checks)
- `REPEATABLE_READ` ⚠️ Phantom reads có thể xảy ra
- `SERIALIZABLE` ✅ Toàn bộ transaction sequence được serialize

**Isolation Levels trong transaction:**
```
SERIALIZABLE (Strongest)
    ↓
REPEATABLE_READ
    ↓
READ_COMMITTED (Default Spring)
    ↓
READ_UNCOMMITTED (Weakest)
```

---

## 🔄 Flow Diagram

```
POST /api/v1/bookings (Request A & B)
         ↓
   [Transaction START with SERIALIZABLE isolation]
         ↓
   isTimeSlotAvailableWithLock()
         ↓
   findConflictingBookingsWithLock() 
   → Acquires PESSIMISTIC_WRITE lock on rows
         ↓
   [Request B BLOCKED here if same time slot]
         ↓
   Verify no conflicts found
         ↓
   Create new Booking entity
         ↓
   [Transaction COMMIT → Lock released]
         ↓
   Return BookingResponse
```

---

## 📊 Performance Impact

### Trade-offs:

| Aspek | Impact | Giải pháp |
|-------|--------|----------|
| **Latency** | ⚠️ Lock wait time tăng | Bù lại bằng data consistency |
| **Throughput** | ⚠️ Sequential processing | Không có cách tránh khi cần strong consistency |
| **Lock Contention** | ⚠️ Cao nếu many concurrent requests | Giới hạn concurrent booking per salon |
| **Database Load** | ✅ Giảm rollback/retry | Tổng overhead thấp hơn optimistic locking |

### Benchmark (ước tính):

```
Scenario: 10 concurrent booking requests cho cùng 1 slot

WITHOUT Pessimistic Lock:
- 10 requests pass availability check (race condition)
- 10 bookings created (8 duplicates invalid)
- Require application-level cleanup + refund logic
- Total time: ~500ms + recovery time

WITH Pessimistic Lock:
- Request 1: Lock acquired, booking created (~50ms)
- Request 2-10: Wait in queue, then rejected (~400ms total)
- No duplicates, no recovery needed
- Total time: ~450ms (sequential, but cleaner)
```

---

## 🛡️ Deadlock Prevention

### Risk: Circular wait

```
Scenario:
T1: Request A locks Salon 1, waits for Salon 2
T2: Request B locks Salon 2, waits for Salon 1
→ DEADLOCK!
```

### Mitigation:

1. **Lock ordering** - Always lock salon in ascending ID order
2. **Lock timeout** - MySQL: `innodb_lock_wait_timeout`
3. **Application retry** - Exponential backoff trên `LockTimeoutException`

---

## 💡 Alternative Approaches

### 1. Optimistic Locking (Version field)
```java
@Version
private Long version;  // Auto-incremented on update

// Throws OptimisticLockException if version mismatch
booking.setStatus(CONFIRMED);  // Fails if another transaction updated
repository.save(booking);
```

**Pros:** ✅ Cao throughput, ✅ Không block
**Cons:** ❌ Retry logic needed, ❌ Network roundtrips

### 2. Message Queue (Async Processing)
```
POST /bookings → Enqueue to RabbitMQ → Sequential processor
```

**Pros:** ✅ Decoupled, ✅ Easy retry
**Cons:** ❌ User waits for queue processing, ❌ Complex error handling

### 3. Distributed Lock (Redis)
```java
@Lock(value = "salon:{salonId}:{date}")
public BookingResponse createBooking(...) { }
```

**Pros:** ✅ Works across microservices
**Cons:** ❌ Redis dependency, ❌ Clock skew issues

---

## 🚀 Implementation Details

### Query Lock Mechanism

```sql
-- Actual SQL generated by Hibernate
SELECT b.* FROM bookings b 
WHERE b.salon_id = ? 
  AND b.status != 'CANCELLED'
  AND b.start_time < ? 
  AND b.end_time > ? 
FOR UPDATE;  -- Pessimistic WRITE lock
```

### Transaction Lifecycle

```java
// 1. Transaction BEGIN
// 2. Lock acquired (WRITE)
List<Booking> conflicts = repository.findConflictingBookingsWithLock(...);

// 3. Business logic
if (conflicts.isEmpty()) {
    Booking newBooking = new Booking(...);
    repository.save(newBooking);  // Still within transaction
}

// 4. Transaction COMMIT → Lock released atomically
// If error: ROLLBACK → Lock released + changes reverted
```

---

## ⚙️ Configuration (application.yaml)

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
          fetch_size: 50
        order_inserts: true
        order_updates: true
  datasource:
    hikari:
      maximum-pool-size: 20
      connection-timeout: 20000
```

---

## 🧪 Testing the Solution

### Concurrency Test
```java
@Test
void testConcurrentBookingCreation() {
    ExecutorService executor = Executors.newFixedThreadPool(5);
    
    for (int i = 0; i < 5; i++) {
        executor.submit(() -> {
            try {
                bookingService.createBooking(
                    request,  // Same time slot
                    user,
                    salon,
                    services
                );
            } catch (IllegalArgumentException e) {
                // Expected for requests 2-5
                assertThat(e.getMessage()).contains("not available");
            }
        });
    }
    
    executor.awaitTermination(10, TimeUnit.SECONDS);
    
    // Verify only 1 booking created
    List<Booking> bookings = repository.findBySalonId(salonId);
    assertThat(bookings).hasSize(1);
}
```

---

## 📋 Checklist Before Production

- [ ] Database supports row-level locks (MySQL, PostgreSQL)
- [ ] Connection pool size configured (min 10, max 30)
- [ ] Lock timeout configured (`innodb_lock_wait_timeout`)
- [ ] Deadlock monitoring enabled
- [ ] Logging configured for lock wait times
- [ ] Load testing completed
- [ ] Rollback/recovery procedures documented

---

## 🔍 Monitoring Locks

### MySQL
```sql
-- View active locks
SHOW ENGINE INNODB STATUS;

-- Check lock wait time
SELECT * FROM performance_schema.events_waits_current 
WHERE event_name LIKE 'wait/lock%';
```

### Application
```java
@RestController
@RequestMapping("/actuator")
public class LockMetricsController {
    @GetMapping("/locks")
    public Map<String, Object> getLockMetrics() {
        // Return lock contention stats
    }
}
```

---

## 📚 References

- JPA Specification - Lock Modes: https://en.wikibooks.org/wiki/Java_Persistence/Locking
- Hibernate Locks: https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#locking
- ACID - Isolation Levels: https://en.wikipedia.org/wiki/Isolation_(database_systems)
- MySQL Row Locks: https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html

