package org.mindera.fur.code.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FileUploadDTO {

    @NotNull(message = "File name is required")
    private String fileName;

    @NotNull(message = "File data as base64 encoding is required")
    private String fileData;

    @NotNull(message = "Base64 checksum as MD5 is required")
    private String md5;
}
