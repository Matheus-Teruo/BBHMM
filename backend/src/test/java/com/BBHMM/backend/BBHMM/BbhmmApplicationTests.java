package com.BBHMM.backend.BBHMM;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.BBHMM.backend.BBHMM.config.S3TestConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(S3TestConfig.class)
class BbhmmApplicationTests {

	@Test
	void contextLoads() {
	}

}
