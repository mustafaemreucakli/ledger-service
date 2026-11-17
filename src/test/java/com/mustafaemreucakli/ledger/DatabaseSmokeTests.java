package com.mustafaemreucakli.ledger;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

class DatabaseSmokeTests extends AbstractIntegrationTest {

	@Autowired
	private JdbcTemplate jdbc;

	@Test
	void runsAgainstExpectedPostgresVersion() {
		String expectedVersion = POSTGRES_IMAGE.substring(POSTGRES_IMAGE.indexOf(':') + 1);

		String serverVersion = jdbc.queryForObject("show server_version", String.class);

		assertThat(serverVersion).startsWith(expectedVersion + " ");
	}

	@Test
	void flywayAppliedBaselineMigration() {
		Map<String, Object> baseline = jdbc.queryForMap(
				"select description, success from flyway_schema_history where version = '1'");

		assertThat(baseline)
				.containsEntry("description", "baseline")
				.containsEntry("success", true);
	}

}
