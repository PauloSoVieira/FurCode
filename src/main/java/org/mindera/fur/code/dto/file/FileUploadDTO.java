package org.mindera.fur.code.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "FileUploadDTO", description = "DTO for uploading a file")
public class FileUploadDTO {

    @Schema(description = "File name", example = "image1.jpg")
    @NotNull(message = "File name is required")
    private String fileName;

    @Schema(description = "File data in base64 encoding", example = "/9j/4AAQSkZJRgABAQEASABIAAD//gATQ3JlYXRlZCB3aXRoIEdJTVD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAABAAEDAREAAhEBAxEB/8QAFAABAAAAAAAAAAAAAAAAAAAAC//EABQQAQAAAAAAAAAAAAAAAAAAAAD/xAAUAQEAAAAAAAAAAAAAAAAAAAAA/8QAFBEBAAAAAAAAAAAAAAAAAAAAAP/aAAwDAQACEQMRAD8AP/B//9k=")
    @NotNull(message = "File data as base64 encoding is required")
    private String fileData;

    @Schema(description = "MD5 checksum of base64 file data", example = "2561e75981bd91d1e9ac3fee5e17f183")
    @NotNull(message = "Base64 checksum as MD5 is required")
    private String md5;
}
