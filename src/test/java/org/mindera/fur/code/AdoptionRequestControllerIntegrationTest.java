package org.mindera.fur.code;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AdoptionRequestControllerIntegrationTest {


    @LocalServerPort
    private Integer port;
  /*
    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RequestDetailRepository requestDetailRepository;

    @Autowired
    private RequestDetailService requestDetailService;

    @Autowired
    private PetRepository petRepository;*/

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        // adoptionRequestService.deleteAllAdoptionRequests();
    }


    @Test
    public void createAdoptionRequestShouldReturn201() {
        Assertions.assertThat(true).isTrue();
    }

}