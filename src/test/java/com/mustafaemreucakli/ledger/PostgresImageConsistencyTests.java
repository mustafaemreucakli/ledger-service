package com.mustafaemreucakli.ledger;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class PostgresImageConsistencyTests {

	private static final Pattern POSTGRES_IMAGE_LINE = Pattern.compile("^\\s*image:\\s*[\"']?(postgres:[^\\s\"']+)",
			Pattern.MULTILINE);

	@Test
	void composeUsesSamePostgresImageAsTests() throws IOException {
		String compose = Files.readString(Path.of("compose.yaml"));
		Matcher matcher = POSTGRES_IMAGE_LINE.matcher(compose);

		assertThat(matcher.find()).as("compose.yaml declares a postgres image").isTrue();
		assertThat(matcher.group(1))
				.as("compose.yaml image must match AbstractIntegrationTest.POSTGRES_IMAGE")
				.isEqualTo(AbstractIntegrationTest.POSTGRES_IMAGE);
	}

}
