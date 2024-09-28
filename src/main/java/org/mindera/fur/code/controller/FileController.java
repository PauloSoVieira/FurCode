package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mindera.fur.code.dto.file.FileUploadDTO;
import org.mindera.fur.code.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Tag(name = "File Operations", description = "Uploads and downloads files.")
@RestController
@Schema(name = "File Operations", description = "Uploads and downloads files.")
public class FileController {

    private final FileService fileService;

    /**
     * Constructor for FileController
     *
     * @param fileService
     */
    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Uploads a pet image
     *
     * @param id
     * @param file
     * @return
     */
    @Schema(name = "Upload a pet image", description = "Uploads a pet image")
    @Operation(summary = "Upload a pet image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file, pet not found"),
    })
    @PostMapping("api/v1/upload/pet/{id}/image/")
    public ResponseEntity<Void> uploadImagePet(
            @Parameter(description = "Pet ID", required = true)
            @PathVariable("id") Long id, @RequestBody FileUploadDTO file) {
        String filePath = String.format("/pet/%s/image/", id);
        fileService.uploadImagePet(filePath, file, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Downloads a pet image
     *
     * @param id
     * @param fileName
     * @return
     */
    @Schema(name = "Download a pet image", description = "Downloads a pet image")
    @Operation(summary = "Download a pet image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = MediaType.IMAGE_JPEG_VALUE),
                            @Content(mediaType = MediaType.IMAGE_PNG_VALUE),
                            @Content(mediaType = "image/webp")
                    },
                    description = "File downloaded successfully"),
            @ApiResponse(responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                    description = "File not found")
    })
    @GetMapping("api/v1/download/pet/{id}/image/{fileName}")
    public ResponseEntity<Resource> downloadImagePet(
            @Parameter(description = "Pet ID", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "File name", required = true)
            @PathVariable("fileName") String fileName) {
        String filePath = String.format("/pet/%s/image/%s", id, fileName);
        byte[] file = fileService.downloadImagePet(filePath, id);

        Resource resource = new ByteArrayResource(file);
        String mimeType = fileService.getMimeTypeFromBytes(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"") // "inline" to display in browser
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length))
                .body(resource);
    }

    @GetMapping("api/v1/download/pet/{id}/image/")
    public ResponseEntity<List<Map<String, String>> > getAllImagesFromPet(
            @Parameter(description = "Pet ID", required = true)
            @PathVariable("id") Long id ) {
        List<Map<String, String>>  imageUrls = fileService.getAllImagesFromPetAsBase64(id);

        return ResponseEntity.ok(imageUrls);
    }

    @GetMapping("api/v1/download/pet/{id}/image/{fileName}/base64")
    public ResponseEntity<String> downloadImagePetAsBase64(
            @Parameter(description = "Pet ID", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "File name", required = true)
            @PathVariable("fileName") String fileName) {
        String filePath = String.format("/pet/%s/image/%s", id, fileName);
        byte[] file = fileService.downloadImagePet(filePath, id);

        String mimeType = fileService.getMimeTypeFromBytes(file);
        String base64Image = Base64.getEncoder().encodeToString(file);
        String base64DataUrl = String.format("data:%s;base64,%s", mimeType, base64Image);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(base64DataUrl.length()))
                .body(base64DataUrl);
    }

    @DeleteMapping("api/v1/pet/{id}/image/{fileName}")
    public ResponseEntity<Void> deleteImagePet(
            @Parameter(description = "Pet ID", required = true)
            @PathVariable("id") Long id,

            @Parameter(description = "File name", required = true)
            @PathVariable("fileName") String fileName) {
        String filePath = String.format("/pet/%s/image/%s", id, fileName);

        try {
            fileService.deleteImagePet(filePath, id);
            return ResponseEntity.noContent().build(); // Return 204 No Content on successful deletion
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 Internal Server Error on failure
        }
    }

}
