package org.mindera.fur.code.controller.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindera.fur.code.dto.form.FormTemplateDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for loading and saving form templates.
 * This class provides methods to interact with template JSON files stored in the classpath.
 */
@Component
public class TemplateLoaderUtil {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    /**
     * Constructs a new TemplateLoaderUtil with the specified ObjectMapper and ResourceLoader.
     *
     * @param objectMapper   The ObjectMapper used for JSON serialization and deserialization.
     * @param resourceLoader The ResourceLoader used to access classpath resources.
     */
    public TemplateLoaderUtil(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Loads a form template from a JSON file in the classpath.
     *
     * @param templateName The name of the template to load (without the .json extension).
     * @return A FormTemplateDTO object representing the loaded template.
     * @throws IOException If there's an error reading the template file or parsing the JSON.
     */
    public FormTemplateDTO loadTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName + ".json");
        return objectMapper.readValue(resource.getInputStream(), FormTemplateDTO.class);
    }

    /**
     * Saves a form template to a JSON file in the classpath.
     *
     * @param templateName The name of the template to save (without the .json extension).
     * @param template     The FormTemplateDTO object to save.
     * @throws IOException If there's an error writing the template file or serializing the JSON.
     */
    public void saveTemplate(String templateName, FormTemplateDTO template) throws IOException {
        String templatePath = resourceLoader.getResource("classpath:templates").getFile().getPath();
        Path path = Paths.get(templatePath, templateName + ".json");

        Files.createDirectories(path.getParent());

        objectMapper.writeValue(path.toFile(), template);
    }


    /**
     * Returns a list of all template names in the classpath.
     *
     * @return A list of template names.
     * @throws IOException If there's an error reading the template files.
     */
    public List<String> getAllTemplateNames() throws IOException {
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources("classpath:templates/*.json");

        return Arrays.stream(resources)
                .map(resource -> resource.getFilename().replace(".json", ""))
                .collect(Collectors.toList());
    }
}