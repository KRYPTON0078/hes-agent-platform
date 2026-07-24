package com.hes.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hes")
public class HesProperties {

    private final Rocketmq rocketmq = new Rocketmq();
    private final Agent agent = new Agent();
    private final Alert alert = new Alert();

    public Rocketmq getRocketmq() { return rocketmq; }
    public Agent getAgent() { return agent; }
    public Alert getAlert() { return alert; }

    public static class Rocketmq {
        private boolean enabled;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class Agent {
        private long heartbeatTtlSeconds = 90;
        private long commandTimeoutSeconds = 30;
        public long getHeartbeatTtlSeconds() { return heartbeatTtlSeconds; }
        public void setHeartbeatTtlSeconds(long heartbeatTtlSeconds) { this.heartbeatTtlSeconds = heartbeatTtlSeconds; }
        public long getCommandTimeoutSeconds() { return commandTimeoutSeconds; }
        public void setCommandTimeoutSeconds(long commandTimeoutSeconds) { this.commandTimeoutSeconds = commandTimeoutSeconds; }
    }

    public static class Alert {
        private double lowSocThreshold = 15.0;
        public double getLowSocThreshold() { return lowSocThreshold; }
        public void setLowSocThreshold(double lowSocThreshold) { this.lowSocThreshold = lowSocThreshold; }
    }
}
