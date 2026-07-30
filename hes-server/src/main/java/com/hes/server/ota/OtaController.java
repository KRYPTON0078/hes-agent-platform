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