package com.hes.server.schedule;

import com.hes.server.service.AlertService;
import com.hes.server.service.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OpsMaintenanceScheduler {

    private static final Logger log = LoggerFactory.getLogger(OpsMaintenanceScheduler.class);

    private final CommandService commandService;
    private final AlertService alertService;

    public OpsMaintenanceScheduler(CommandService commandService, AlertService alertService) {
        this.commandService = commandService;
        this.alertService = alertService;
    }

    @Scheduled(fixedDelayString = "15000")
    public void sweepCommandTimeouts() {
        int count = commandService.markTimeouts();
        if (count > 0) {
            log.info("Marked {} commands as TIMEOUT", count);
        }
    }

    @Scheduled(fixedDelayString = "30000")
    public void scanOfflineAgents() {
        int count = alertService.scanOfflineDevices();
        if (count > 0) {
            log.debug("Offline scan touched {} devices", count);
        }
    }
}
