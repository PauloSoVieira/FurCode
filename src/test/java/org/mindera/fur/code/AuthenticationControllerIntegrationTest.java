package org.mindera.fur.code;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.model.Person;
import org.mindera.fur.code.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)

public class AuthenticationControllerIntegrationTest {
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "correctpassword";

    @LocalServerPort
    private Integer port;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        createTestUser();
    }

    private void createTestUser() {
        Person testPerson = new Person();
        testPerson.setEmail(TEST_EMAIL);
        testPerson.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        personRepository.save(testPerson);
    }

    @Nested
    class LoginTest {
        @Test
        public void testSuccessfulLogin() {
            String requestBody = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """, TEST_EMAIL, TEST_PASSWORD);
            String responseBody = given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .extract().body().asString();

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(200)
                    .body("token", not(emptyOrNullString()));
        }

        @Test
        public void testWrongEmail() {
            String requestBody = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """, "johndoe@example.com", TEST_PASSWORD);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(401);
        }

        @Test
        public void testWrongPassword() {
            String requestBody = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """, TEST_EMAIL, "wrongpassword");

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(400);
        }

        @Test
        public void testWrongEmailAndPassword() {
            String requestBody = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """, "johndoe@example.com", "wrongpassword");

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(401);
        }

        @Test
        public void testWrongToken() {
            String requestBody = String.format("""
                    {
                        "token": "%s"
                    }
                    """, "wrongtoken");

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(401);
        }

        @Test
        public void testPasswordBiggerThanAllowed() {
            String requestBody = String.format("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """, "johndoe@example.com", "loremipsumdolorsitametconsecteturadipiscingelitseddoeiusmodtemporincididuntutlaboreetdolore");

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(401);

        }

        @Test
        public void testNullEmail() {
            String requestBody = String.format("""
                    {
                        "email": null,
                        "password": "%s"
                    }
                    """, TEST_PASSWORD);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(401);
        }

        @Test
        public void testNullPassword() {
            String requestBody = String.format("""
                    {
                        "email": "%s",
                        "password": null
                    }
                    """, TEST_EMAIL);

            given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/v1/auth/login")
                    .then()
                    .statusCode(401);
        }
    }
}