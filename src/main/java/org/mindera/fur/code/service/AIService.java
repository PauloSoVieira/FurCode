package org.mindera.fur.code.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mindera.fur.code.dto.pet.PetDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.mindera.fur.code.config.ai.AIPrompts.*;

@Service
public class AIService {

    private final ChatClient chatClient;
    private final OpenAiChatModel chatModel;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public AIService(ChatClient.Builder chatClientBuilder, OpenAiChatModel chatModel) {
        this.chatClient = chatClientBuilder.build();
        this.chatModel = chatModel;
    }

    /**
     * Generates a new pet description based on a pet.
     * The pet is a petDTO object that contains the pet details.
     * Based on the pet, a new pet description is generated using the OpenAI Chat API.
     *
     * @param pet the pet.
     * @return the generated pet description in JSON format.
     */
    public String generateNewPetDescription(PetDTO pet) {
        return this.chatClient.prompt()
                .user(PET_DESCRIPTION_CREATION +
                        " with the following JSON details, specially the 'observations' field, if available: "
                        + objectToJSONString(pet))
                .call()
                .content();
    }

    /**
     * Generates a new pet search query based on a search query.
     * The search query is a natural language query that is used to search for pets.
     * Based on the search query, a SQL query is generated to retrieve pets from the database.
     *
     * @param searchQuery the search query.
     * @return the generated pet search query in JSON format.
     */
    public String generateNewPetSearchQuery(String searchQuery) {

        String userText = PET_SEARCH_USER_TEXT.formatted(searchQuery);
        String systemText = PET_SEARCH_USER_SYSTEM_TEXT_V2.formatted(PET_SEARCH_USER_RESPONSE_JSON_SCHEMA, PET_SEARCH_USER_DB_SCHEMA);

        Message userMessage = new UserMessage(userText);
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage();

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage),
                OpenAiChatOptions.builder()
                        .withResponseFormat(new OpenAiApi.ChatCompletionRequest.ResponseFormat("json_object"))
                        .build()
        );

        // Call OpenAI Chat API with the prompt
        List<Generation> response = chatModel.call(prompt).getResults();

        JsonNode jsonNode = getJSONNodeFromResponse(response);

        if (!jsonNode.get("properties").get("success").asBoolean()) {
            throw new RuntimeException("Invalid search. Reason: " + jsonNode.get("properties").get("reason").asText());
        }

        String query = jsonNode.get("properties").get("query").asText();
        if (!validateSQLQuery(query)) {
            throw new RuntimeException("Furcode said this is an invalid query. Please try again.");
        }

        //TODO: maybe change this to a custom repository
        //run sql query on the database
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);

        // create a JSON object with the results
        ArrayNode resultsArray = objectMapper.valueToTree(results);
        ObjectNode rootNode = objectMapper.createObjectNode();

        rootNode.put("status", "success");
        rootNode.put("recordCount", results.size());
        rootNode.put("message", "Query executed successfully");
        rootNode.put("query", query);
        rootNode.set("results", resultsArray);

        return objectNodeToJSONPrettyString(rootNode);
    }

    /**
     * Extracts the JSON node from a list of Generation objects.
     *
     * @param response the list of Generation objects.
     * @return the JSON node extracted from the Generation objects.
     */
    private JsonNode getJSONNodeFromResponse(List<Generation> response) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(response.get(0).getOutput().getContent());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode;
    }

    /**
     * Converts an ObjectNode to a JSON string with pretty formatting.
     *
     * @param object the ObjectNode to be converted.
     * @return the JSON string representation of the ObjectNode with pretty formatting.
     */
    private String objectNodeToJSONPrettyString(ObjectNode object) {
        String jsonResults = null;
        try {
            jsonResults = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonResults;
    }

    /**
     * Converts an object to a JSON string.
     *
     * @param object the object to be converted.
     * @return the JSON string representation of the object.
     */
    private String objectToJSONString(Object object) {
        String jsonResults = null;
        try {
            jsonResults = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonResults;
    }

    /**
     * Validates a SQL query to prevent from messing up the database.
     *
     * @param query the SQL query to be validated.
     * @return true if the query is safe, false otherwise.
     */
    private Boolean validateSQLQuery(String query) {
        // TODO: add more safeguards
        // safeguards to prevent from messing up the database
        if (query.contains("CREATE") || query.contains("ALTER")
                || query.contains("INSERT") || query.contains("DELETE")) {
            return false;
        }

        if (query.contains("drop") || query.contains("create") || query.contains("alter")
                || query.contains("insert") || query.contains("delete")) {
            return false;
        }

        return true;
    }
}
