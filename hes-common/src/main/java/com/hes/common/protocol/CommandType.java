package com.hes.common.protocol;

/**
 * Remote control actions that the cloud can send to an energy-storage Agent.
 */
public enum CommandType {
    SET_CHARGE_POWER,
    SET_DISCHARGE_POWER,
    SET_SOC_LIMIT,
    START_CHARGE,
    STOP_CHARGE,
    REBOOT_AGENT,
    FIRMWARE_CHECK
}
