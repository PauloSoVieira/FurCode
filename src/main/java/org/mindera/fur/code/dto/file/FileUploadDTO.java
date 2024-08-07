package org.mindera.fur.code.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FileUploadDTO {

    @NotNull(message = "File name is required")
    private String fileName;

    @NotNull(message = "File Base64 encoding is required")
    private String base64;

    @NotNull(message = "Base64 checksum is required")
    private String checksum;
}
