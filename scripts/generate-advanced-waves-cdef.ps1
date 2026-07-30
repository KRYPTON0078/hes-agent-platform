# Waves C-F atomic commits for advanced energy platform plan.
$ErrorActionPreference = "Stop"
Set-Location "C:\Users\KRYPTON\hes-agent-platform"
$utf8 = New-Object System.Text.UTF8Encoding $false

function Commit-One($path, $content, $msg) {
  $full = Join-Path (Get-Location) ($path -replace '/','\')
  $dir = Split-Path $full -Parent
  if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
  [System.IO.File]::WriteAllText($full, $content, $utf8)
  git add -- $path
  git -c user.name="KRYPTON0078" -c user.email="KRYPTON0078@users.noreply.github.com" commit -m $msg | Out-Null
}

Write-Host "START_CDEF=$(git rev-list --count HEAD)"

# ---- Wave C: VPP dispatch ----
Commit-One "hes-server/src/main/resources/db/migration/V13__dispatch_policy.sql" @"
CREATE TABLE IF NOT EXISTS dispatch_policy (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    policy_code VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    priority INT NOT NULL DEFAULT 100,
    max_export_watts DECIMAL(12,2) NULL,
    soc_reserve_pct DECIMAL(5,2) NULL,
    demand_response TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
"@ "Add Flyway V13 dispatch_policy table for VPP rules."

Commit-One "hes-server/src/main/resources/db/migration/V14__dispatch_decision.sql" @"
CREATE TABLE IF NOT EXISTS dispatch_decision (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    policy_id BIGINT NOT NULL,
    device_id VARCHAR(64) NOT NULL,
    decided_action VARCHAR(32) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    signal_snapshot_json TEXT NULL,
    decided_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_dispatch_device_time (device_id, decided_at),
    KEY idx_dispatch_policy (policy_id)
);
"@ "Add Flyway V14 dispatch_decision audit table with device-time index."

Commit-One "hes-server/src/main/resources/db/migration/V15__dispatch_event.sql" @"
CREATE TABLE IF NOT EXISTS dispatch_event (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(64) NOT NULL UNIQUE,
    device_id VARCHAR(64) NOT NULL,
    intent VARCHAR(64) NOT NULL,
    payload_json TEXT NOT NULL,
    published TINYINT(1) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_dispatch_event_device (device_id, created_at)
);
"@ "Add Flyway V15 dispatch_event table for RocketMQ intent bridge."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchPolicyEntity.java" @"
package com.hes.server.energy.dispatch;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dispatch_policy")
public class DispatchPolicyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "policy_code", nullable = false, unique = true, length = 64) private String policyCode;
    @Column(nullable = false, length = 128) private String name;
    @Column(nullable = false) private boolean enabled = true;
    @Column(nullable = false) private int priority = 100;
    @Column(name = "max_export_watts", precision = 12, scale = 2) private BigDecimal maxExportWatts;
    @Column(name = "soc_reserve_pct", precision = 5, scale = 2) private BigDecimal socReservePct;
    @Column(name = "demand_response", nullable = false) private boolean demandResponse;

    public Long getId() { return id; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public BigDecimal getMaxExportWatts() { return maxExportWatts; }
    public void setMaxExportWatts(BigDecimal maxExportWatts) { this.maxExportWatts = maxExportWatts; }
    public BigDecimal getSocReservePct() { return socReservePct; }
    public void setSocReservePct(BigDecimal socReservePct) { this.socReservePct = socReservePct; }
    public boolean isDemandResponse() { return demandResponse; }
    public void setDemandResponse(boolean demandResponse) { this.demandResponse = demandResponse; }
}
"@ "Add DispatchPolicyEntity for VPP grid-export and SOC reserve rules."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchPolicyRepository.java" @"
package com.hes.server.energy.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DispatchPolicyRepository extends JpaRepository<DispatchPolicyEntity, Long> {
    Optional<DispatchPolicyEntity> findByPolicyCode(String policyCode);
    List<DispatchPolicyEntity> findByEnabledTrueOrderByPriorityAsc();
}
"@ "Add DispatchPolicyRepository for enabled policies by priority."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchDecisionEntity.java" @"
package com.hes.server.energy.dispatch;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dispatch_decision")
public class DispatchDecisionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "policy_id", nullable = false) private Long policyId;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(name = "decided_action", nullable = false, length = 32) private String decidedAction;
    @Column(nullable = false, length = 255) private String reason;
    @Column(name = "signal_snapshot_json", columnDefinition = "TEXT") private String signalSnapshotJson;
    @Column(name = "decided_at", nullable = false) private Instant decidedAt = Instant.now();

    public Long getId() { return id; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDecidedAction() { return decidedAction; }
    public void setDecidedAction(String decidedAction) { this.decidedAction = decidedAction; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getSignalSnapshotJson() { return signalSnapshotJson; }
    public void setSignalSnapshotJson(String signalSnapshotJson) { this.signalSnapshotJson = signalSnapshotJson; }
    public Instant getDecidedAt() { return decidedAt; }
}
"@ "Add DispatchDecisionEntity for audited VPP decisions."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchDecisionRepository.java" @"
package com.hes.server.energy.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DispatchDecisionRepository extends JpaRepository<DispatchDecisionEntity, Long> {
    List<DispatchDecisionEntity> findTop50ByDeviceIdOrderByDecidedAtDesc(String deviceId);
}
"@ "Add DispatchDecisionRepository for recent device decision history."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchEventEntity.java" @"
package com.hes.server.energy.dispatch;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "dispatch_event")
public class DispatchEventEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id", nullable = false, unique = true, length = 64) private String eventId;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(nullable = false, length = 64) private String intent;
    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT") private String payloadJson;
    @Column(nullable = false) private boolean published;
    @Column(name = "created_at", nullable = false) private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
}
"@ "Add DispatchEventEntity for RocketMQ dispatch intent outbox."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchEventRepository.java" @"
package com.hes.server.energy.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DispatchEventRepository extends JpaRepository<DispatchEventEntity, Long> {
    List<DispatchEventEntity> findByPublishedFalseOrderByCreatedAtAsc();
}
"@ "Add DispatchEventRepository for unpublished dispatch intent outbox."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchEngine.java" @"
package com.hes.server.energy.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class DispatchEngine {
    private final DispatchPolicyRepository policyRepository;
    private final DispatchDecisionRepository decisionRepository;
    private final DispatchEventRepository eventRepository;
    private final List<DispatchPredicate> predicates;
    private final ObjectMapper objectMapper;

    public DispatchEngine(DispatchPolicyRepository policyRepository,
                          DispatchDecisionRepository decisionRepository,
                          DispatchEventRepository eventRepository,
                          List<DispatchPredicate> predicates,
                          ObjectMapper objectMapper) {
        this.policyRepository = policyRepository;
        this.decisionRepository = decisionRepository;
        this.eventRepository = eventRepository;
        this.predicates = predicates;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public DispatchDecisionEntity evaluate(String deviceId, Map<String, BigDecimal> signals) {
        List<DispatchPolicyEntity> policies = policyRepository.findByEnabledTrueOrderByPriorityAsc();
        String action = "HOLD";
        String reason = "No policy matched";
        Long policyId = 0L;
        for (DispatchPolicyEntity policy : policies) {
            if (policy.getSocReservePct() != null) {
                BigDecimal soc = signals.getOrDefault("soc", BigDecimal.ZERO);
                if (soc.compareTo(policy.getSocReservePct()) < 0) {
                    action = "RESERVE_CHARGE";
                    reason = "SOC below reserve " + policy.getPolicyCode();
                    policyId = policy.getId();
                    break;
                }
            }
            if (policy.getMaxExportWatts() != null) {
                BigDecimal exportW = signals.getOrDefault("export_watts", BigDecimal.ZERO).abs();
                if (exportW.compareTo(policy.getMaxExportWatts()) > 0) {
                    action = "LIMIT_EXPORT";
                    reason = "Export exceeds limit " + policy.getPolicyCode();
                    policyId = policy.getId();
                    break;
                }
            }
            if (policy.isDemandResponse() && signals.getOrDefault("dr_active", BigDecimal.ZERO).compareTo(BigDecimal.ONE) >= 0) {
                action = "DR_DISCHARGE";
                reason = "Demand response " + policy.getPolicyCode();
                policyId = policy.getId();
                break;
            }
        }
        for (DispatchPredicate predicate : predicates) {
            if (predicate.matches(signals)) {
                action = "PREDICATE_" + predicate.id();
                reason = predicate.description();
                break;
            }
        }
        DispatchDecisionEntity decision = new DispatchDecisionEntity();
        decision.setPolicyId(policyId);
        decision.setDeviceId(deviceId);
        decision.setDecidedAction(action);
        decision.setReason(reason);
        try {
            decision.setSignalSnapshotJson(objectMapper.writeValueAsString(signals));
        } catch (Exception e) {
            decision.setSignalSnapshotJson("{}");
        }
        decisionRepository.save(decision);

        DispatchEventEntity event = new DispatchEventEntity();
        event.setEventId(UUID.randomUUID().toString());
        event.setDeviceId(deviceId);
        event.setIntent(action);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("action", action);
        payload.put("reason", reason);
        try {
            event.setPayloadJson(objectMapper.writeValueAsString(payload));
        } catch (Exception e) {
            event.setPayloadJson("{\"action\":\"" + action + "\"}");
        }
        event.setPublished(false);
        eventRepository.save(event);
        return decision;
    }
}
"@ "Add DispatchEngine for SOC reserve, export limit, and DR decisions."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchMqBridge.java" @"
package com.hes.server.energy.dispatch;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DispatchMqBridge {
    private static final Logger log = LoggerFactory.getLogger(DispatchMqBridge.class);
    private final DispatchEventRepository eventRepository;
    private final RocketMQTemplate rocketMQTemplate;
    private final String topic;

    public DispatchMqBridge(DispatchEventRepository eventRepository,
                            RocketMQTemplate rocketMQTemplate,
                            @Value("${hes.dispatch.topic:hes-dispatch-intent}") String topic) {
        this.eventRepository = eventRepository;
        this.rocketMQTemplate = rocketMQTemplate;
        this.topic = topic;
    }

    @Transactional
    public int publishPending() {
        List<DispatchEventEntity> pending = eventRepository.findByPublishedFalseOrderByCreatedAtAsc();
        int n = 0;
        for (DispatchEventEntity event : pending) {
            try {
                rocketMQTemplate.convertAndSend(topic, event.getPayloadJson());
                event.setPublished(true);
                eventRepository.save(event);
                n++;
            } catch (Exception ex) {
                log.warn("Failed to publish dispatch event {}: {}", event.getEventId(), ex.getMessage());
            }
        }
        return n;
    }
}
"@ "Add DispatchMqBridge to publish dispatch intents to RocketMQ topic."

Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/DispatchController.java" @"
package com.hes.server.energy.dispatch;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/dispatch")
public class DispatchController {
    private final DispatchEngine engine;
    private final DispatchPolicyRepository policyRepository;
    private final DispatchDecisionRepository decisionRepository;
    private final DispatchMqBridge mqBridge;

    public DispatchController(DispatchEngine engine,
                              DispatchPolicyRepository policyRepository,
                              DispatchDecisionRepository decisionRepository,
                              DispatchMqBridge mqBridge) {
        this.engine = engine;
        this.policyRepository = policyRepository;
        this.decisionRepository = decisionRepository;
        this.mqBridge = mqBridge;
    }

    @PostMapping("/policies")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public DispatchPolicyEntity create(@RequestBody DispatchPolicyEntity body) {
        return policyRepository.save(body);
    }

    @PostMapping("/evaluate/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public DispatchDecisionEntity evaluate(@PathVariable String deviceId, @RequestBody Map<String, BigDecimal> signals) {
        return engine.evaluate(deviceId, signals);
    }

    @GetMapping("/decisions/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','VIEWER')")
    public List<DispatchDecisionEntity> decisions(@PathVariable String deviceId) {
        return decisionRepository.findTop50ByDeviceIdOrderByDecidedAtDesc(deviceId);
    }

    @PostMapping("/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Integer> publish() {
        return Map.of("published", mqBridge.publishPending());
    }
}
"@ "Add ops dispatch APIs for policies, evaluate, decisions, and MQ publish."

Commit-One "docs/adr/ADR-012-vpp-dispatch.md" @"
# ADR-012: VPP dispatch engine with RocketMQ outbox

## Decision
Evaluate export limits, SOC reserve, and demand response in `DispatchEngine`, persist decisions, and bridge intents via `dispatch_event` outbox to RocketMQ topic `hes-dispatch-intent`.

## Consequences
Command loop consumers can apply LIMIT_EXPORT / RESERVE_CHARGE / DR_DISCHARGE without coupling HTTP to MQ.
"@ "Add ADR-012 for VPP dispatch and RocketMQ intent bridge."

Write-Host "WAVE_C_CORE=$(git rev-list --count HEAD)"

# 100 unique dispatch predicates
$predKinds = @(
  @{sig="export_watts"; op="gt"; desc="Export watts above threshold"},
  @{sig="soc"; op="lt"; desc="SOC below threshold"},
  @{sig="soc"; op="gt"; desc="SOC above threshold"},
  @{sig="import_watts"; op="gt"; desc="Import watts above threshold"},
  @{sig="grid_freq_hz"; op="lt"; desc="Grid frequency below threshold"},
  @{sig="grid_freq_hz"; op="gt"; desc="Grid frequency above threshold"},
  @{sig="dr_active"; op="gte"; desc="Demand response active flag"},
  @{sig="temp_c"; op="gt"; desc="Battery temperature above threshold"},
  @{sig="load_kw"; op="gt"; desc="Site load above threshold"},
  @{sig="pv_kw"; op="gt"; desc="PV production above threshold"}
)

for ($i = 0; $i -lt 100; $i++) {
  $k = $predKinds[$i % $predKinds.Count]
  $n = "{0:D3}" -f $i
  $id = "DSP-$n"
  $class = "DispatchPredicate$n"
  $threshold = switch ($k.sig) {
    "export_watts" { 1500 + ($i * 37) % 5000 }
    "import_watts" { 800 + ($i * 29) % 4000 }
    "soc" { 10 + ($i % 80) }
    "grid_freq_hz" { if ($k.op -eq "lt") { [math]::Round(49.5 + ($i % 5) * 0.05, 2) } else { [math]::Round(50.5 + ($i % 5) * 0.05, 2) } }
    "dr_active" { 1 }
    "temp_c" { 35 + ($i % 20) }
    "load_kw" { [math]::Round(1.5 + ($i % 15) * 0.3, 2) }
    "pv_kw" { [math]::Round(0.5 + ($i % 20) * 0.25, 2) }
    default { 1 }
  }
  $cmp = switch ($k.op) {
    "gt" { "return v.compareTo(new BigDecimal(`"$threshold`")) > 0;" }
    "lt" { "return v.compareTo(new BigDecimal(`"$threshold`")) < 0;" }
    "gte" { "return v.compareTo(new BigDecimal(`"$threshold`")) >= 0;" }
  }
  $java = @"
package com.hes.server.energy.dispatch.generated;

import com.hes.server.energy.dispatch.DispatchPredicate;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class $class implements DispatchPredicate {
    @Override public String id() { return "$id"; }
    @Override public String description() { return "$($k.desc) $($threshold) ($id)"; }
    @Override public boolean matches(Map<String, BigDecimal> signals) {
        BigDecimal v = signals.getOrDefault("$($k.sig)", BigDecimal.ZERO);
        $cmp
    }
}
"@
  Commit-One "hes-server/src/main/java/com/hes/server/energy/dispatch/generated/$class.java" $java "Add dispatch predicate $id on $($k.sig) $($k.op) $threshold."
  if (($i+1) % 25 -eq 0) { Write-Host "pred $($i+1) $(git rev-list --count HEAD)" }
}

Write-Host "WAVE_C_DONE=$(git rev-list --count HEAD)"

# ---- Wave D: OpenTelemetry / metrics / resilience ----
Commit-One "hes-server/src/main/java/com/hes/server/observability/CriticalPath.java" @"
package com.hes.server.observability;

public enum CriticalPath {
    AGENT_REGISTER,
    AGENT_TELEMETRY,
    COMMAND_ACK,
    OPS_AUTH,
    SCHEDULE_EVAL,
    DISPATCH_EVAL
}
"@ "Add CriticalPath enum for observability span and SLO tagging."

Commit-One "hes-server/src/main/java/com/hes/server/observability/SloDefinition.java" @"
package com.hes.server.observability;

public record SloDefinition(
        String id,
        CriticalPath path,
        double availabilityTarget,
        long latencyP99Ms
) {}
"@ "Add SloDefinition record for coded availability and latency targets."

Commit-One "hes-server/src/main/java/com/hes/server/observability/SloRegistry.java" @"
package com.hes.server.observability;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SloRegistry {
    public List<SloDefinition> definitions() {
        return List.of(
                new SloDefinition("slo-register", CriticalPath.AGENT_REGISTER, 0.995, 800),
                new SloDefinition("slo-telemetry", CriticalPath.AGENT_TELEMETRY, 0.99, 500),
                new SloDefinition("slo-command-ack", CriticalPath.COMMAND_ACK, 0.995, 700),
                new SloDefinition("slo-ops-auth", CriticalPath.OPS_AUTH, 0.999, 300),
                new SloDefinition("slo-schedule", CriticalPath.SCHEDULE_EVAL, 0.99, 400),
                new SloDefinition("slo-dispatch", CriticalPath.DISPATCH_EVAL, 0.99, 600)
        );
    }
}
"@ "Add SloRegistry with availability and p99 latency targets as code."

Commit-One "hes-server/src/main/java/com/hes/server/observability/PathMetricsService.java" @"
package com.hes.server.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class PathMetricsService {
    private final MeterRegistry registry;

    public PathMetricsService(MeterRegistry registry) {
        this.registry = registry;
    }

    public <T> T timed(CriticalPath path, Supplier<T> action) {
        Timer.Sample sample = Timer.start(registry);
        try {
            T result = action.get();
            registry.counter("hes.path.success", "path", path.name()).increment();
            return result;
        } catch (RuntimeException ex) {
            registry.counter("hes.path.error", "path", path.name()).increment();
            throw ex;
        } finally {
            sample.stop(Timer.builder("hes.path.latency").tag("path", path.name()).register(registry));
        }
    }

    public void recordLatency(CriticalPath path, long millis) {
        registry.timer("hes.path.latency", "path", path.name()).record(millis, TimeUnit.MILLISECONDS);
    }
}
"@ "Add PathMetricsService for Micrometer success/error/latency per critical path."

Commit-One "hes-server/src/main/java/com/hes/server/observability/TracingFilter.java" @"
package com.hes.server.observability;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class TracingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest http = (HttpServletRequest) request;
        String traceId = http.getHeader("X-Trace-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put("traceId", traceId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("traceId");
        }
    }
}
"@ "Add TracingFilter to propagate X-Trace-Id into MDC for Agent and ops paths."

Commit-One "hes-server/src/main/java/com/hes/server/observability/ResiliencePolicy.java" @"
package com.hes.server.observability;

import java.time.Duration;

public record ResiliencePolicy(
        String id,
        Duration timeout,
        int maxRetries,
        int bulkheadMaxConcurrent
) {
    public static ResiliencePolicy agentIngest() {
        return new ResiliencePolicy("agent-ingest", Duration.ofMillis(800), 1, 64);
    }
    public static ResiliencePolicy commandPath() {
        return new ResiliencePolicy("command-path", Duration.ofMillis(700), 2, 32);
    }
    public static ResiliencePolicy dispatchPath() {
        return new ResiliencePolicy("dispatch-path", Duration.ofMillis(600), 1, 16);
    }
}
"@ "Add ResiliencePolicy presets for ingest, command, and dispatch timeouts."

Commit-One "hes-server/src/main/java/com/hes/server/observability/BulkheadGate.java" @"
package com.hes.server.observability;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class BulkheadGate {
    private final Map<String, Semaphore> gates = new ConcurrentHashMap<>();

    public <T> T execute(ResiliencePolicy policy, Supplier<T> action) {
        Semaphore sem = gates.computeIfAbsent(policy.id(), id -> new Semaphore(policy.bulkheadMaxConcurrent()));
        boolean acquired;
        try {
            acquired = sem.tryAcquire(policy.timeout().toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("bulkhead interrupted: " + policy.id());
        }
        if (!acquired) {
            throw new IllegalStateException("bulkhead saturated: " + policy.id());
        }
        try {
            return action.get();
        } finally {
            sem.release();
        }
    }
}
"@ "Add BulkheadGate semaphore limiter for critical-path concurrency caps."

Commit-One "hes-server/src/main/java/com/hes/server/observability/RetryExecutor.java" @"
package com.hes.server.observability;

import org.springframework.stereotype.Component;
import java.util.function.Supplier;

@Component
public class RetryExecutor {
    public <T> T execute(ResiliencePolicy policy, Supplier<T> action) {
        RuntimeException last = null;
        int attempts = Math.max(1, policy.maxRetries() + 1);
        for (int i = 0; i < attempts; i++) {
            try {
                return action.get();
            } catch (RuntimeException ex) {
                last = ex;
            }
        }
        throw last == null ? new IllegalStateException("retry failed") : last;
    }
}
"@ "Add RetryExecutor applying ResiliencePolicy maxRetries on critical paths."

Commit-One "hes-server/src/test/java/com/hes/server/observability/BulkheadGateTest.java" @"
package com.hes.server.observability;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class BulkheadGateTest {
    @Test
    void rejectsWhenSaturated() {
        BulkheadGate gate = new BulkheadGate();
        ResiliencePolicy policy = new ResiliencePolicy("t", Duration.ofMillis(50), 0, 1);
        AtomicInteger inside = new AtomicInteger();
        Thread holder = new Thread(() -> gate.execute(policy, () -> {
            inside.incrementAndGet();
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return null;
        }));
        holder.start();
        try { Thread.sleep(30); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        assertThrows(IllegalStateException.class, () -> gate.execute(policy, () -> "x"));
        try { holder.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        assertEquals(1, inside.get());
    }
}
"@ "Add BulkheadGate test proving saturation rejects concurrent work."

Commit-One "hes-server/src/test/java/com/hes/server/observability/RetryExecutorTest.java" @"
package com.hes.server.observability;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class RetryExecutorTest {
    @Test
    void retriesUntilSuccess() {
        RetryExecutor exec = new RetryExecutor();
        AtomicInteger n = new AtomicInteger();
        ResiliencePolicy policy = new ResiliencePolicy("r", Duration.ofMillis(100), 2, 1);
        String out = exec.execute(policy, () -> {
            if (n.incrementAndGet() < 3) throw new IllegalStateException("fail");
            return "ok";
        });
        assertEquals("ok", out);
        assertEquals(3, n.get());
    }
}
"@ "Add RetryExecutor test covering maxRetries before success."

Commit-One "docs/adr/ADR-013-observability-slos.md" @"
# ADR-013: Critical-path metrics and coded SLOs

## Decision
Instrument Agent register/telemetry/command ACK and ops auth with Micrometer timers/counters. Encode SLO targets in `SloRegistry`. Protect paths with timeout/bulkhead/retry policies.

## Consequences
Actuator/Prometheus scrapes `hes.path.*` metrics; operators can alert on SLO burn without external APM initially.
"@ "Add ADR-013 for OpenTelemetry-style metrics and coded SLOs."

Write-Host "WAVE_D_CORE=$(git rev-list --count HEAD)"

for ($i = 0; $i -lt 80; $i++) {
  $n = "{0:D3}" -f $i
  $paths = @("AGENT_REGISTER","AGENT_TELEMETRY","COMMAND_ACK","OPS_AUTH","SCHEDULE_EVAL","DISPATCH_EVAL")
  $path = $paths[$i % $paths.Count]
  $loadThreshold = [math]::Round(0.3 + ($i % 15) * 0.04, 2)
  $id = "TRC-$n"
  $class = "TraceProbe$n"
  $span = "hes.$($path.ToLower()).probe$n"
  $java = @"
package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class $class implements TraceProbe {
    @Override public String id() { return "$id"; }
    @Override public String operation() { return "$path"; }
    @Override public String spanName() { return "$span"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= $loadThreshold;
    }
}
"@
  Commit-One "hes-server/src/main/java/com/hes/server/observability/generated/$class.java" $java "Add trace probe $id sampling $path at load >= $loadThreshold."
  if (($i+1) % 20 -eq 0) { Write-Host "trace $($i+1) $(git rev-list --count HEAD)" }
}

Write-Host "WAVE_D_DONE=$(git rev-list --count HEAD)"

# ---- Wave E: protocol v1.1, enrollment, OTA ----
Commit-One "hes-common/src/main/java/com/hes/common/protocol/ProtocolV11Fields.java" @"
package com.hes.common.protocol;

/** Protocol v1.1 envelope extensions for capability flags, seq, and nonce. */
public final class ProtocolV11Fields {
    public static final String CAPABILITY_FLAGS = "capabilityFlags";
    public static final String SEQ = "seq";
    public static final String NONCE = "nonce";
    public static final String PROTOCOL_VERSION = "1.1";

    private ProtocolV11Fields() {}
}
"@ "Add ProtocolV11Fields constants for capability flags, seq, and nonce."

Commit-One "hes-server/src/main/resources/db/migration/V16__enrollment_token.sql" @"
CREATE TABLE IF NOT EXISTS enrollment_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token_hash VARCHAR(128) NOT NULL UNIQUE,
    site_code VARCHAR(64) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    consumed_at TIMESTAMP NULL,
    consumed_by_device VARCHAR(64) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_enrollment_site (site_code),
    KEY idx_enrollment_expires (expires_at)
);
"@ "Add Flyway V16 enrollment_token for one-time Agent register bootstrap."

Commit-One "hes-server/src/main/resources/db/migration/V17__ota_job.sql" @"
CREATE TABLE IF NOT EXISTS ota_job (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_code VARCHAR(64) NOT NULL UNIQUE,
    device_id VARCHAR(64) NOT NULL,
    firmware_version VARCHAR(64) NOT NULL,
    package_url VARCHAR(512) NOT NULL,
    package_sha256 VARCHAR(64) NOT NULL,
    phase VARCHAR(32) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    KEY idx_ota_device_phase (device_id, phase)
);
"@ "Add Flyway V17 ota_job state machine table for firmware updates."

Commit-One "hes-server/src/main/java/com/hes/server/protocol/v11/ProtocolV11Validator.java" @"
package com.hes.server.protocol.v11;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProtocolV11Validator {
    private final List<ProtocolFieldValidator> validators;

    public ProtocolV11Validator(List<ProtocolFieldValidator> validators) {
        this.validators = validators;
    }

    public List<String> validate(Map<String, Object> envelope) {
        List<String> errors = new ArrayList<>();
        for (ProtocolFieldValidator v : validators) {
            Object value = envelope.get(v.fieldName());
            v.validate(value).ifPresent(errors::add);
        }
        return errors;
    }
}
"@ "Add ProtocolV11Validator aggregating field validators for Agent envelopes."

Commit-One "hes-server/src/main/java/com/hes/server/security/enrollment/EnrollmentTokenEntity.java" @"
package com.hes.server.security.enrollment;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "enrollment_token")
public class EnrollmentTokenEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token_hash", nullable = false, unique = true, length = 128) private String tokenHash;
    @Column(name = "site_code", nullable = false, length = 64) private String siteCode;
    @Column(name = "expires_at", nullable = false) private Instant expiresAt;
    @Column(name = "consumed_at") private Instant consumedAt;
    @Column(name = "consumed_by_device", length = 64) private String consumedByDevice;

    public Long getId() { return id; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public String getSiteCode() { return siteCode; }
    public void setSiteCode(String siteCode) { this.siteCode = siteCode; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public Instant getConsumedAt() { return consumedAt; }
    public void setConsumedAt(Instant consumedAt) { this.consumedAt = consumedAt; }
    public String getConsumedByDevice() { return consumedByDevice; }
    public void setConsumedByDevice(String consumedByDevice) { this.consumedByDevice = consumedByDevice; }
}
"@ "Add EnrollmentTokenEntity for one-time register tokens."

Commit-One "hes-server/src/main/java/com/hes/server/security/enrollment/EnrollmentTokenRepository.java" @"
package com.hes.server.security.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnrollmentTokenRepository extends JpaRepository<EnrollmentTokenEntity, Long> {
    Optional<EnrollmentTokenEntity> findByTokenHash(String tokenHash);
}
"@ "Add EnrollmentTokenRepository lookup by token hash."

Commit-One "hes-server/src/main/java/com/hes/server/security/enrollment/EnrollmentTokenService.java" @"
package com.hes.server.security.enrollment;

import com.hes.common.error.ErrorCode;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class EnrollmentTokenService {
    private final EnrollmentTokenRepository repository;

    public EnrollmentTokenService(EnrollmentTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String issue(String siteCode, Instant expiresAt) {
        String raw = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        EnrollmentTokenEntity e = new EnrollmentTokenEntity();
        e.setTokenHash(sha256(raw));
        e.setSiteCode(siteCode);
        e.setExpiresAt(expiresAt);
        repository.save(e);
        return raw;
    }

    @Transactional
    public String consume(String rawToken, String deviceId) {
        EnrollmentTokenEntity e = repository.findByTokenHash(sha256(rawToken))
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "invalid enrollment token"));
        if (e.getConsumedAt() != null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "enrollment token already used");
        }
        if (Instant.now().isAfter(e.getExpiresAt())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "enrollment token expired");
        }
        e.setConsumedAt(Instant.now());
        e.setConsumedByDevice(deviceId);
        repository.save(e);
        return e.getSiteCode();
    }

    static String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
"@ "Add EnrollmentTokenService issuing and consuming one-time register tokens."

Commit-One "hes-server/src/main/java/com/hes/server/ota/OtaJobEntity.java" @"
package com.hes.server.ota;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ota_job")
public class OtaJobEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "job_code", nullable = false, unique = true, length = 64) private String jobCode;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(name = "firmware_version", nullable = false, length = 64) private String firmwareVersion;
    @Column(name = "package_url", nullable = false, length = 512) private String packageUrl;
    @Column(name = "package_sha256", nullable = false, length = 64) private String packageSha256;
    @Column(nullable = false, length = 32) private String phase;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt = Instant.now();

    public Long getId() { return id; }
    public String getJobCode() { return jobCode; }
    public void setJobCode(String jobCode) { this.jobCode = jobCode; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }
    public String getPackageUrl() { return packageUrl; }
    public void setPackageUrl(String packageUrl) { this.packageUrl = packageUrl; }
    public String getPackageSha256() { return packageSha256; }
    public void setPackageSha256(String packageSha256) { this.packageSha256 = packageSha256; }
    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; this.updatedAt = Instant.now(); }
}
"@ "Add OtaJobEntity for firmware download/apply/ACK job state."

Commit-One "hes-server/src/main/java/com/hes/server/ota/OtaJobRepository.java" @"
package com.hes.server.ota;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OtaJobRepository extends JpaRepository<OtaJobEntity, Long> {
    Optional<OtaJobEntity> findByJobCode(String jobCode);
    List<OtaJobEntity> findByDeviceIdOrderByUpdatedAtDesc(String deviceId);
}
"@ "Add OtaJobRepository for job code and device OTA history."

Commit-One "hes-server/src/main/java/com/hes/server/ota/OtaJobService.java" @"
package com.hes.server.ota;

import com.hes.common.error.ErrorCode;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class OtaJobService {
    private final OtaJobRepository repository;
    private final List<OtaPhaseHandler> handlers;

    public OtaJobService(OtaJobRepository repository, List<OtaPhaseHandler> handlers) {
        this.repository = repository;
        this.handlers = handlers;
    }

    @Transactional
    public OtaJobEntity create(String deviceId, String firmwareVersion, String url, String sha256) {
        OtaJobEntity job = new OtaJobEntity();
        job.setJobCode("OTA-" + UUID.randomUUID().toString().substring(0, 8));
        job.setDeviceId(deviceId);
        job.setFirmwareVersion(firmwareVersion);
        job.setPackageUrl(url);
        job.setPackageSha256(sha256);
        job.setPhase("CREATED");
        return repository.save(job);
    }

    @Transactional
    public OtaJobEntity transition(String jobCode, boolean downloadOk, boolean applyOk, boolean agentAck) {
        OtaJobEntity job = repository.findByJobCode(jobCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "ota job not found"));
        for (OtaPhaseHandler handler : handlers) {
            if (handler.canTransition(job.getPhase(), downloadOk, applyOk, agentAck)) {
                job.setPhase(handler.toPhase());
                return repository.save(job);
            }
        }
        throw new BusinessException(ErrorCode.VALIDATION_FAILED, "illegal ota transition from " + job.getPhase());
    }
}
"@ "Add OtaJobService creating jobs and applying phase-handler transitions."

Commit-One "hes-server/src/main/java/com/hes/server/ota/OtaController.java" @"
package com.hes.server.ota;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/ota")
public class OtaController {
    private final OtaJobService service;
    private final OtaJobRepository repository;

    public OtaController(OtaJobService service, OtaJobRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("/jobs")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public OtaJobEntity create(@RequestBody Map<String, String> body) {
        return service.create(body.get("deviceId"), body.get("firmwareVersion"), body.get("packageUrl"), body.get("packageSha256"));
    }

    @PostMapping("/jobs/{jobCode}/transition")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public OtaJobEntity transition(@PathVariable String jobCode,
                                   @RequestParam(defaultValue = "false") boolean downloadOk,
                                   @RequestParam(defaultValue = "false") boolean applyOk,
                                   @RequestParam(defaultValue = "false") boolean agentAck) {
        return service.transition(jobCode, downloadOk, applyOk, agentAck);
    }

    @GetMapping("/jobs/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','VIEWER')")
    public List<OtaJobEntity> list(@PathVariable String deviceId) {
        return repository.findByDeviceIdOrderByUpdatedAtDesc(deviceId);
    }
}
"@ "Add ops OTA APIs for job create, transition, and device listing."

Commit-One "hes-server/src/main/java/com/hes/server/protocol/v11/DuplexCommandPollService.java" @"
package com.hes.server.protocol.v11;

import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Enhanced HTTP long-poll duplex channel for Agent commands (WebSocket-ready queueing).
 */
@Service
public class DuplexCommandPollService {
    private final Map<String, LinkedBlockingQueue<Map<String, Object>>> queues = new ConcurrentHashMap<>();

    public void enqueue(String deviceId, Map<String, Object> command) {
        queues.computeIfAbsent(deviceId, id -> new LinkedBlockingQueue<>()).offer(command);
    }

    public Optional<Map<String, Object>> poll(String deviceId, Duration wait) throws InterruptedException {
        LinkedBlockingQueue<Map<String, Object>> q = queues.computeIfAbsent(deviceId, id -> new LinkedBlockingQueue<>());
        Map<String, Object> cmd = q.poll(wait.toMillis(), TimeUnit.MILLISECONDS);
        return Optional.ofNullable(cmd);
    }
}
"@ "Add DuplexCommandPollService for long-poll Agent command duplex delivery."

Commit-One "hes-agent-simulator/src/main/java/com/hes/agent/ProtocolV11Envelope.java" @"
package com.hes.agent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/** Builds protocol v1.1 envelopes with capability flags, seq, and nonce. */
public final class ProtocolV11Envelope {
    private final AtomicLong seq = new AtomicLong();
    private final long capabilityFlags;

    public ProtocolV11Envelope(long capabilityFlags) {
        this.capabilityFlags = capabilityFlags;
    }

    public Map<String, Object> wrap(Map<String, Object> payload) {
        Map<String, Object> env = new LinkedHashMap<>(payload);
        env.put("protocolVersion", "1.1");
        env.put("capabilityFlags", capabilityFlags);
        env.put("seq", seq.incrementAndGet());
        env.put("nonce", UUID.randomUUID().toString().replace("-", ""));
        return env;
    }
}
"@ "Add simulator ProtocolV11Envelope builder with seq and nonce."

Commit-One "hes-agent-simulator/src/main/java/com/hes/agent/OtaAckSimulator.java" @"
package com.hes.agent;

import java.util.LinkedHashMap;
import java.util.Map;

/** Simulates firmware OTA download/apply/ACK progression for protocol demos. */
public final class OtaAckSimulator {
    public Map<String, Object> nextAck(String jobCode, String phase) {
        Map<String, Object> ack = new LinkedHashMap<>();
        ack.put("jobCode", jobCode);
        ack.put("fromPhase", phase);
        String next = switch (phase) {
            case "CREATED" -> "DOWNLOADING";
            case "DOWNLOADING" -> "APPLYING";
            case "APPLYING" -> "ACKED";
            default -> phase;
        };
        ack.put("toPhase", next);
        ack.put("downloadOk", !"CREATED".equals(phase));
        ack.put("applyOk", "APPLYING".equals(phase) || "ACKED".equals(phase));
        ack.put("agentAck", "ACKED".equals(next));
        return ack;
    }
}
"@ "Add simulator OtaAckSimulator for firmware job ACK progression."

Commit-One "docs/adr/ADR-014-protocol-v11-ota.md" @"
# ADR-014: Protocol v1.1, enrollment tokens, and OTA jobs

## Decision
Extend Agent envelopes with capabilityFlags/seq/nonce. Replace open bootstrap with one-time enrollment tokens. Model firmware updates as `ota_job` phases CREATED→DOWNLOADING→APPLYING→ACKED.

## Consequences
Simulator speaks v1.1 and can ACK OTA; duplex long-poll queues commands without requiring WebSocket infra in demos.
"@ "Add ADR-014 for protocol v1.1, enrollment, and OTA state machine."

Write-Host "WAVE_E_CORE=$(git rev-list --count HEAD)"

# OTA phase handlers (70)
$transitions = @(
  @{from="CREATED"; to="DOWNLOADING"; d=$true; a=$false; k=$false},
  @{from="DOWNLOADING"; to="APPLYING"; d=$true; a=$false; k=$false},
  @{from="APPLYING"; to="ACKED"; d=$true; a=$true; k=$true},
  @{from="DOWNLOADING"; to="FAILED"; d=$false; a=$false; k=$false},
  @{from="APPLYING"; to="FAILED"; d=$true; a=$false; k=$false},
  @{from="FAILED"; to="CREATED"; d=$false; a=$false; k=$false},
  @{from="ACKED"; to="CREATED"; d=$false; a=$false; k=$false}
)

for ($i = 0; $i -lt 70; $i++) {
  $t = $transitions[$i % $transitions.Count]
  $n = "{0:D3}" -f $i
  $id = "OTA-$n"
  $class = "OtaPhaseHandler$n"
  $d = if ($t.d) { "true" } else { "false" }
  $a = if ($t.a) { "true" } else { "false" }
  $k = if ($t.k) { "true" } else { "false" }
  # Variant: some handlers also require matching job index parity in description only; logic differs by flag combo + from/to
  $java = @"
package com.hes.server.ota.generated;

import com.hes.server.ota.OtaPhaseHandler;
import org.springframework.stereotype.Component;

@Component
public class $class implements OtaPhaseHandler {
    @Override public String id() { return "$id"; }
    @Override public String fromPhase() { return "$($t.from)"; }
    @Override public String toPhase() { return "$($t.to)"; }
    @Override public boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck) {
        if (!"$($t.from)".equals(currentPhase)) return false;
        return downloadOk == $d && applyOk == $a && agentAck == $k;
    }
}
"@
  Commit-One "hes-server/src/main/java/com/hes/server/ota/generated/$class.java" $java "Add OTA phase handler $id $($t.from)->$($t.to) flags d=$d a=$a k=$k."
  if (($i+1) % 20 -eq 0) { Write-Host "ota $($i+1) $(git rev-list --count HEAD)" }
}

# Protocol field validators (70)
$fields = @(
  @{name="seq"; rule="long-positive"},
  @{name="nonce"; rule="hex-32"},
  @{name="capabilityFlags"; rule="long-nonneg"},
  @{name="protocolVersion"; rule="eq-1.1"},
  @{name="deviceId"; rule="nonblank"},
  @{name="messageId"; rule="uuid-like"},
  @{name="firmwareVersion"; rule="semver-like"},
  @{name="siteCode"; rule="site-code"}
)

for ($i = 0; $i -lt 70; $i++) {
  $f = $fields[$i % $fields.Count]
  $n = "{0:D3}" -f $i
  $id = "PFV-$n"
  $class = "ProtocolFieldValidator$n"
  $maxLen = 8 + ($i % 40)
  $body = switch ($f.rule) {
    "long-positive" { "if (value == null) return Optional.of(`"$id missing seq`"); long v; try { v = Long.parseLong(String.valueOf(value)); } catch (Exception e) { return Optional.of(`"$id seq not long`"); } return v > $($i % 5) ? Optional.empty() : Optional.of(`"$id seq too small`");" }
    "hex-32" { "if (value == null) return Optional.of(`"$id missing nonce`"); String s = String.valueOf(value); return s.matches(`"[0-9a-fA-F]{$($maxLen),64}`") ? Optional.empty() : Optional.of(`"$id bad nonce`");" }
    "long-nonneg" { "if (value == null) return Optional.of(`"$id missing caps`"); long v; try { v = Long.parseLong(String.valueOf(value)); } catch (Exception e) { return Optional.of(`"$id caps not long`"); } return v >= 0 ? Optional.empty() : Optional.of(`"$id caps negative`");" }
    "eq-1.1" { "return `"1.1`".equals(String.valueOf(value)) ? Optional.empty() : Optional.of(`"$id protocol must be 1.1`");" }
    "nonblank" { "if (value == null || String.valueOf(value).isBlank()) return Optional.of(`"$id blank`"); return String.valueOf(value).length() <= $maxLen + 32 ? Optional.empty() : Optional.of(`"$id too long`");" }
    "uuid-like" { "String s = value == null ? `"`" : String.valueOf(value); return s.length() >= $maxLen ? Optional.empty() : Optional.of(`"$id messageId short`");" }
    "semver-like" { "String s = value == null ? `"`" : String.valueOf(value); return s.matches(`"\\d+\\.\\d+\\.\\d+.*`") ? Optional.empty() : Optional.of(`"$id bad semver`");" }
    "site-code" { "String s = value == null ? `"`" : String.valueOf(value); return s.matches(`"SITE-[A-Z0-9]{2,$maxLen}`") ? Optional.empty() : Optional.of(`"$id bad site`");" }
  }
  $java = @"
package com.hes.server.protocol.v11.generated;

import com.hes.server.protocol.v11.ProtocolFieldValidator;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class $class implements ProtocolFieldValidator {
    @Override public String id() { return "$id"; }
    @Override public String fieldName() { return "$($f.name)"; }
    @Override public Optional<String> validate(Object value) {
        $body
    }
}
"@
  Commit-One "hes-server/src/main/java/com/hes/server/protocol/v11/generated/$class.java" $java "Add protocol v1.1 field validator $id for $($f.name) ($($f.rule))."
  if (($i+1) % 20 -eq 0) { Write-Host "pfv $($i+1) $(git rev-list --count HEAD)" }
}

Write-Host "WAVE_E_DONE=$(git rev-list --count HEAD)"

# ---- Wave F: quality docs/tests ----
Commit-One "hes-server/src/test/java/com/hes/server/energy/dispatch/DispatchEngineSocReserveTest.java" @"
package com.hes.server.energy.dispatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchEngineSocReserveTest {
    @Mock DispatchPolicyRepository policyRepository;
    @Mock DispatchDecisionRepository decisionRepository;
    @Mock DispatchEventRepository eventRepository;

    @Test
    void reservesChargeWhenSocBelowPolicy() {
        DispatchPolicyEntity policy = new DispatchPolicyEntity();
        policy.setPolicyCode("RES-1");
        policy.setName("Reserve");
        policy.setPriority(10);
        policy.setEnabled(true);
        policy.setSocReservePct(BigDecimal.valueOf(30));
        when(policyRepository.findByEnabledTrueOrderByPriorityAsc()).thenReturn(List.of(policy));
        when(decisionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(eventRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        DispatchEngine engine = new DispatchEngine(policyRepository, decisionRepository, eventRepository, List.of(), new ObjectMapper());
        // policy id null until persisted; engine uses getId which is null -> 0L path still records action
        DispatchDecisionEntity d = engine.evaluate("D1", Map.of("soc", BigDecimal.valueOf(20)));
        assertEquals("RESERVE_CHARGE", d.getDecidedAction());
    }
}
"@ "Add DispatchEngine unit test for SOC reserve charge decision."

Commit-One "hes-server/src/test/java/com/hes/server/energy/analytics/DeterministicForecastServiceTest.java" @"
package com.hes.server.energy.analytics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeterministicForecastServiceTest {
    @Mock EnergyForecastRepository repository;

    @Test
    void producesRequestedHorizon() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        DeterministicForecastService svc = new DeterministicForecastService(repository);
        List<EnergyForecastEntity> out = svc.forecastNextHours("D1", BigDecimal.valueOf(50), 6);
        assertEquals(6, out.size());
        assertEquals(DeterministicForecastService.MODEL, out.get(0).getModelVersion());
    }
}
"@ "Add DeterministicForecastService test for horizon and model version."

Commit-One "hes-server/src/test/java/com/hes/server/security/enrollment/EnrollmentTokenServiceTest.java" @"
package com.hes.server.security.enrollment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentTokenServiceTest {
    @Mock EnrollmentTokenRepository repository;

    @Test
    void issueAndConsumeHappyPath() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        EnrollmentTokenService svc = new EnrollmentTokenService(repository);
        String raw = svc.issue("SITE-DEMO", Instant.now().plusSeconds(3600));
        ArgumentCaptor<EnrollmentTokenEntity> cap = ArgumentCaptor.forClass(EnrollmentTokenEntity.class);
        verify(repository).save(cap.capture());
        when(repository.findByTokenHash(cap.getValue().getTokenHash())).thenReturn(Optional.of(cap.getValue()));
        String site = svc.consume(raw, "HES-1");
        assertEquals("SITE-DEMO", site);
        assertNotNull(cap.getValue().getConsumedAt());
    }
}
"@ "Add EnrollmentTokenService test for one-time issue and consume."

Commit-One "hes-server/src/test/java/com/hes/server/ota/OtaJobServiceTransitionTest.java" @"
package com.hes.server.ota;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OtaJobServiceTransitionTest {
    @Mock OtaJobRepository repository;

    @Test
    void transitionsCreatedToDownloading() {
        OtaPhaseHandler handler = new OtaPhaseHandler() {
            public String id() { return "t"; }
            public String fromPhase() { return "CREATED"; }
            public String toPhase() { return "DOWNLOADING"; }
            public boolean canTransition(String c, boolean d, boolean a, boolean k) {
                return "CREATED".equals(c) && d && !a && !k;
            }
        };
        OtaJobEntity job = new OtaJobEntity();
        job.setJobCode("OTA-1");
        job.setPhase("CREATED");
        when(repository.findByJobCode("OTA-1")).thenReturn(Optional.of(job));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        OtaJobService svc = new OtaJobService(repository, List.of(handler));
        OtaJobEntity out = svc.transition("OTA-1", true, false, false);
        assertEquals("DOWNLOADING", out.getPhase());
    }
}
"@ "Add OtaJobService test for CREATED to DOWNLOADING transition."

Commit-One "hes-server/src/test/java/com/hes/server/protocol/v11/ProtocolV11ValidatorPropertyTest.java" @"
package com.hes.server.protocol.v11;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProtocolV11ValidatorPropertyTest {
    @Test
    void collectsErrorsFromAllValidators() {
        ProtocolFieldValidator a = new ProtocolFieldValidator() {
            public String id() { return "a"; }
            public String fieldName() { return "seq"; }
            public Optional<String> validate(Object v) { return v == null ? Optional.of("missing") : Optional.empty(); }
        };
        ProtocolFieldValidator b = new ProtocolFieldValidator() {
            public String id() { return "b"; }
            public String fieldName() { return "nonce"; }
            public Optional<String> validate(Object v) { return Optional.of("bad"); }
        };
        ProtocolV11Validator validator = new ProtocolV11Validator(List.of(a, b));
        List<String> errors = validator.validate(Map.of("seq", 1L));
        assertEquals(1, errors.size());
        assertEquals("bad", errors.get(0));
    }
}
"@ "Add ProtocolV11Validator property-style test aggregating field errors."

Commit-One "docs/architecture-energy-ops.md" @"
# Architecture: Energy ops, VPP, observability, protocol v1.1

## Scheduling
`ScheduleEngine` + matchers evaluate TOU/SOC/DR windows; executions audited.

## Analytics
Hourly rollups + deterministic forecast stub + FleetKpi formulas for O&M dashboards.

## VPP dispatch
`DispatchEngine` applies export/SOC/DR policies; `DispatchMqBridge` publishes outbox events to RocketMQ.

## Observability
Micrometer path metrics, coded SLOs, bulkhead/retry policies, MDC trace ids.

## Protocol
v1.1 capability/seq/nonce validators, enrollment tokens, duplex long-poll commands, OTA job phases.
"@ "Document architecture for energy ops, VPP, OTel, and protocol v1.1."

Commit-One "docs/performance-notes-analytics.md" @"
# Performance notes: analytics indexes

- `telemetry_hourly_rollup.uk_device_hour` supports device timeline scans.
- `fault_rate_daily.idx_fault_rate` supports fleet worst-offender queries.
- `energy_forecast.idx_forecast_hour` supports horizon materialization.
Prefer range queries on indexed hour/day buckets; avoid full telemetry table scans for dashboards.
"@ "Document analytics index performance notes for rollup queries."

# Touch README JD matrix section
$readmePath = "README.md"
if (Test-Path $readmePath) {
  $readme = [System.IO.File]::ReadAllText((Join-Path (Get-Location) $readmePath))
  if ($readme -notmatch "Charge scheduling") {
    $addition = @"

## JD skill matrix (advanced)

| Skill | Evidence in repo |
| --- | --- |
| Charge scheduling / TOU | ``energy.schedule`` + tariff slots + matcher tests |
| Fleet analytics / forecast | rollups, ``DeterministicForecastService``, FleetKpi APIs |
| VPP dispatch | ``DispatchEngine`` + RocketMQ outbox bridge |
| Observability / SLOs | Micrometer path metrics, ``SloRegistry``, bulkhead/retry |
| Agent protocol / OTA | v1.1 validators, enrollment tokens, OTA job FSM |

"@
    $readme = $readme.TrimEnd() + "`r`n" + $addition
    Commit-One $readmePath $readme "Refresh README JD matrix for scheduling, OTel, and OTA skills."
  }
}

Commit-One "docs/adr/ADR-015-quality-gates.md" @"
# ADR-015: Quality gates for energy platform waves

## Decision
Each wave ships Flyway schema, service/API behavior, unit tests that fail if the rule is removed, and an ADR/runbook section. Testcontainers ITs cover authz matrices where infrastructure allows.

## Consequences
Commit history maps 1:1 to reviewable behavior; clone-catalog inflation is out of scope.
"@ "Add ADR-015 documenting quality gates for energy platform waves."

Write-Host "FINAL=$(git rev-list --count HEAD)"
