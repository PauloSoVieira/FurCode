package org.mindera.fur.code;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class profileTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private Environment env;

    @Test
    public void testPropertySourcesAreCorrect() {
        assertEquals("jdbc:postgresql://localhost:5433/furcode_test", env.getProperty("spring.datasource.url"));
        assertEquals("localhost", env.getProperty("spring.data.redis.host"));
        assertEquals("6380", env.getProperty("spring.data.redis.port"));
        assertEquals("http://localhost:9090", env.getProperty("minio.endpoint"));
        System.out.println("Active profiles: " + Arrays.toString(env.getActiveProfiles()));
    }
}
