package org.mindera.fur.code.controller.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindera.fur.code.dto.form.FormTemplateDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class TemplateLoaderUtil {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;


    public TemplateLoaderUtil(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

    public FormTemplateDTO loadTemplate(String templateName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + templateName + ".json");
        return objectMapper.readValue(resource.getInputStream(), FormTemplateDTO.class);
    }


    public void saveTemplate(String templateName, FormTemplateDTO template) throws IOException {
        String templatePath = resourceLoader.getResource("classpath:templates").getFile().getPath();
        Path path = Paths.get(templatePath, templateName + ".json");

        // Ensure the directory exists
        Files.createDirectories(path.getParent());

        // Write the updated template to the file
        objectMapper.writeValue(path.toFile(), template);
    }
}