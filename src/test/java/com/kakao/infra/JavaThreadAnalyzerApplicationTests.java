package com.kakao.infra;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JavaThreadDumpAnalyzerApplication.class)
@WebIntegrationTest({"config.baseurl=http://localhost:9000",
		"spring.datasource.url=jdbc:h2:mem",
		"spring.datasource.driverClassName=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"server.port=9000"
})
public class JavaThreadAnalyzerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
