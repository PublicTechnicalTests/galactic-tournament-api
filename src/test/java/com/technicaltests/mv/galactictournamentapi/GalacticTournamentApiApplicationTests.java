package com.technicaltests.mv.galactictournamentapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
	"springdoc.swagger-ui.enabled=false",
	"springdoc.api-docs.enabled=false",
	"spring.ai.mcp.client.enabled=false"
})
class GalacticTournamentApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
