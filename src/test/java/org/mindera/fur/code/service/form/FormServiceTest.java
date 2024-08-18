package org.mindera.fur.code.service.form;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mindera.fur.code.dto.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class FormServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FormService formService;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    void tearDown() {
        formService.deleteAllForms();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class CrudForms {
        @Test
        void createForm_returns201() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("Test Form");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("DEFAULT");
            formCreateDTO.setInitialField(new FormFieldCreateDTO());

            FormDTO formDTO = given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            assertNotNull(formDTO);
            assertNotNull(formDTO.getId());
            assertEquals(formCreateDTO.getName(), formDTO.getName());
            assertEquals(formCreateDTO.getType(), formDTO.getType());

            FormDTO savedForm = given()
                    .when()
                    .get("/api/v1/forms/" + formDTO.getId())
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            assertEquals(formDTO.getId(), savedForm.getId());
            assertEquals(formDTO.getName(), savedForm.getName());
            assertEquals(formDTO.getType(), savedForm.getType());
        }

        @Test
        void getForm_returns200() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setInitialField(new FormFieldCreateDTO());

            String formIdString = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath()
                    .getString("id");

            Long formId = Long.parseLong(formIdString);

            FormDTO getFormDTO = given()
                    .when()
                    .get("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            assertEquals(formId, getFormDTO.getId());
        }

        @Test
        void getAllForms_returns200() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setInitialField(new FormFieldCreateDTO());

            given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body();

            List<FormDTO> forms = given()
                    .when()
                    .get("/api/v1/forms/all")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .jsonPath()
                    .getList(".", FormDTO.class);

            assertEquals(1, forms.size());
        }

        @Test
        void deleteForm_returns204() {
            FormCreateDTO formcreateDTO = new FormCreateDTO();
            formcreateDTO.setName("Test Form");
            formcreateDTO.setCreatedAt(LocalDateTime.now());
            formcreateDTO.setType("DEFAULT");
            formcreateDTO.setInitialField(new FormFieldCreateDTO());

            String formIdString = given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .jsonPath()
                    .getString("id");

            Long formId = Long.parseLong(formIdString);

            given()
                    .contentType(ContentType.JSON)
                    .body(formcreateDTO)
                    .when()
                    .delete("/api/v1/forms/" + formId)
                    .then()
                    .statusCode(204);
        }
    }

    @Nested
    class FormValidations {
        @Test
        void createForm_withNullName_returns400() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName(null);
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("DEFAULT");
            formCreateDTO.setInitialField(new FormFieldCreateDTO());

            given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(400);
        }

        @Test
        void createForm_withEmptyName_returns400() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("DEFAULT");
            formCreateDTO.setInitialField(new FormFieldCreateDTO());

            given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(400)
                    .body(containsString("name"));
        }

        @Test
        void createForm_withNullType_returns400() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("Test Form");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType(null);
            formCreateDTO.setInitialField(new FormFieldCreateDTO());

            given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(400)
                    .body(containsString("type"));
        }
    }

    @Nested
    class FormTemplateOperations {
        @Test
        void createFormFromTemplate_withValidTemplate_returns201() {
            FormDTO formDTO = given()
                    .when()
                    .post("/api/v1/forms/template/adoption-template")
                    .then()
                    .statusCode(201)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            assertNotNull(formDTO);
            assertEquals("Pet Adoption Application", formDTO.getName());
            assertEquals("ADOPTION_TEMPLATE", formDTO.getType());
        }

        @Test
        void createFormFromTemplate_withInvalidTemplate_returns400() {
            given()
                    .when()
                    .post("/api/v1/forms/template/invalid-template")
                    .then()
                    .statusCode(400);
        }

        @Test
        void addFieldToTemplate_withValidField_returns201() {
            String uniqueQuestion = "uniqueQuestion" + System.currentTimeMillis();
            FormFieldCreateDTO newField = new FormFieldCreateDTO();
            newField.setFieldType("TEXT");
            newField.setQuestion(uniqueQuestion);

            given()
                    .contentType(ContentType.JSON)
                    .body(newField)
                    .when()
                    .post("/api/v1/forms/template/adoption-template/field")
                    .then()
                    .statusCode(201);

            FormTemplateDTO updatedTemplateDTO = given()
                    .when()
                    .get("/api/v1/forms/template/adoption-template")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormTemplateDTO.class);

            assertTrue(updatedTemplateDTO.getFields().stream()
                            .anyMatch(field -> field.getQuestion().equals(uniqueQuestion)),
                    "The new field should exist in the updated template");

        }

        @Test
        void addDuplicateFieldToTemplate_returns400() {
            String uniqueQuestion = "Duplicate Question " + UUID.randomUUID().toString();
            FormFieldCreateDTO newField = new FormFieldCreateDTO();
            newField.setFieldType("TEXT");
            newField.setQuestion(uniqueQuestion);

            given()
                    .contentType(ContentType.JSON)
                    .body(newField)
                    .when()
                    .post("/api/v1/forms/template/adoption-template/field")
                    .then()
                    .statusCode(201);

            given()
                    .contentType(ContentType.JSON)
                    .body(newField)
                    .when()
                    .post("/api/v1/forms/template/adoption-template/field")
                    .then()
                    .statusCode(400);
        }

        @Test
        void removeFieldFromTemplate_withExistingField_returns200() {
            FormTemplateDTO template = formService.getTemplate("adoption-template");
            String questionToRemove = template.getFields().get(0).getQuestion();

            FormDTO updatedTemplate = given()
                    .contentType(ContentType.JSON)
                    .queryParam("question", questionToRemove)
                    .when()
                    .delete("/api/v1/forms/template/adoption-template/field")
                    .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .as(FormDTO.class);

            assertNotNull(updatedTemplate);
            assertFalse(updatedTemplate.getFormFieldAnswers().stream()
                    .anyMatch(field -> field.getQuestion().equals(questionToRemove)));
        }
    }

    @Nested
    class FormFieldOperations {
        @Test
        void addFieldToForm_returns201() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("Test Form");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("DEFAULT");
            formCreateDTO.setInitialField(new FormFieldCreateDTO());

            Long formId = given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .jsonPath()
                    .getLong("id");

            FormFieldCreateDTO newField = new FormFieldCreateDTO();
            newField.setFieldType("TEXT");

            FormDTO updatedForm = given()
                    .contentType(ContentType.JSON)
                    .body(newField)
                    .when()
                    .post("/api/v1/forms/" + formId + "/field")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(FormDTO.class);

            assertNotNull(updatedForm);
            assertEquals(2, updatedForm.getFormFieldAnswers().size());
        }

        @Test
        void addMultipleFieldsToForm_returns201() {
            FormCreateDTO formCreateDTO = new FormCreateDTO();
            formCreateDTO.setName("Test Form");
            formCreateDTO.setCreatedAt(LocalDateTime.now());
            formCreateDTO.setType("DEFAULT");
            formCreateDTO.setInitialField(new FormFieldCreateDTO());

            Long formId = given()
                    .contentType(ContentType.JSON)
                    .body(formCreateDTO)
                    .when()
                    .post("/api/v1/forms")
                    .then()
                    .statusCode(201)
                    .extract()
                    .jsonPath()
                    .getLong("id");

            List<FormFieldCreateDTO> newFields = new ArrayList<>();
            newFields.add(new FormFieldCreateDTO("TEXT", "Question 1"));
            newFields.add(new FormFieldCreateDTO("NUMBER", "Question 2"));
            newFields.add(new FormFieldCreateDTO("BOOLEAN", "Question 3"));

            FormDTO updatedForm = null;

            for (FormFieldCreateDTO field : newFields) {
                updatedForm = given()
                        .contentType(ContentType.JSON)
                        .body(field)
                        .when()
                        .post("/api/v1/forms/" + formId + "/field")
                        .then()
                        .statusCode(201)
                        .extract()
                        .as(FormDTO.class);
            }

            assertNotNull(updatedForm);
            assertEquals(4, updatedForm.getFormFieldAnswers().size());

        }

    }

    @Nested
    class FormAnswerSubmission {
        @Test
        void submitFormAnswers_withValidAnswers_returns201() {
            FormDTO form = given()
                    .when()
                    .post("/api/v1/forms/template/adoption-template")
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(FormDTO.class);

            List<FormFieldAnswerDTO> answers = new ArrayList<>();
            for (FormFieldAnswerDTO field : form.getFormFieldAnswers()) {
                FormFieldAnswerDTO answer = new FormFieldAnswerDTO();
                answer.setFormFieldId(field.getFormFieldId());
                answer.setAnswer("Test Answer");
                answers.add(answer);
            }
            form.setFormFieldAnswers(answers);

            // Submit answers
            FormDTO updatedForm = given()
                    .contentType(ContentType.JSON)
                    .body(form)
                    .when()
                    .post("/api/v1/forms/submit/" + form.getId())
                    .then()
                    .statusCode(201)  // Changed from 200 to 201
                    .extract()
                    .as(FormDTO.class);

            assertNotNull(updatedForm);
            assertTrue(updatedForm.getFormFieldAnswers().stream()
                    .allMatch(field -> field.getAnswer().equals("Test Answer")));
        }
    }
}
