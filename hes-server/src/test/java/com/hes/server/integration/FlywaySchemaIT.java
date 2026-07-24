package com.hes.server.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies Flyway V1 schema applies cleanly on real MySQL 8.
 * Skipped automatically when Docker is unavailable.
 */
@Testcontainers(disabledWithoutDocker = true)
@EnabledIf("dockerAvailable")
class FlywaySchemaIT {

    @Container
    @SuppressWarnings("resource")
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("hes")
            .withUsername("hes")
            .withPassword("hes_secret");

    static boolean dockerAvailable() {
        try {
            return DockerClientFactory.instance().isDockerAvailable();
        } catch (Throwable t) {
            return false;
        }
    }

    @Test
    void flywayMigrationCreatesCoreTablesAndIndexes() throws Exception {
        // Apply the same SQL Flyway ships, via container copy + mysql client for isolation from Spring context.
        mysql.copyFileToContainer(
                MountableFile.forClasspathResource("db/migration/V1__init_schema.sql"),
                "/tmp/V1__init_schema.sql"
        );
        org.testcontainers.containers.Container.ExecResult result = mysql.execInContainer(
                "bash", "-c",
                "mysql -uhes -phes_secret hes < /tmp/V1__init_schema.sql"
        );
        assertTrue(result.getExitCode() == 0, result.getStderr());

        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setUrl(mysql.getJdbcUrl());
        ds.setUsername(mysql.getUsername());
        ds.setPassword(mysql.getPassword());
        JdbcTemplate jdbc = new JdbcTemplate(ds);

        List<Map<String, Object>> tables = jdbc.queryForList(
                "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'hes'"
        );
        List<String> names = tables.stream().map(r -> String.valueOf(r.get("TABLE_NAME")).toLowerCase()).toList();
        assertTrue(names.contains("device"));
        assertTrue(names.contains("telemetry_history"));
        assertTrue(names.contains("command_record"));
        assertTrue(names.contains("alert_record"));

        Integer indexCount = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.STATISTICS " +
                        "WHERE TABLE_SCHEMA='hes' AND INDEX_NAME='idx_telemetry_device_reported'",
                Integer.class
        );
        assertTrue(indexCount != null && indexCount > 0);
    }
}
