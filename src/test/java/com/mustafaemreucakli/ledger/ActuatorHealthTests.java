package com.mustafaemreucakli.ledger;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
class ActuatorHealthTests {

	@Autowired
	private MockMvcTester mvc;

	@Test
	void healthEndpointReportsUp() {
		assertThat(mvc.get().uri("/actuator/health"))
				.hasStatus(HttpStatus.OK)
				.bodyJson()
				.extractingPath("$.status")
				.isEqualTo("UP");
	}

	@Test
	void healthEndpointDoesNotExposeDetails() {
		assertThat(mvc.get().uri("/actuator/health"))
				.bodyJson()
				.doesNotHavePath("$.components")
				.doesNotHavePath("$.details");
	}

	@ParameterizedTest
	@ValueSource(strings = { "env", "configprops", "beans", "heapdump", "threaddump", "loggers", "mappings" })
	void sensitiveEndpointsAreNotExposed(String endpoint) {
		assertThat(mvc.get().uri("/actuator/{endpoint}", endpoint))
				.hasStatus(HttpStatus.NOT_FOUND);
	}

}
