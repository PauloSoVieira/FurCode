package org.mindera.fur.code.controller;

import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "File Operations", description = "Uploads and downloads files.")
@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Upload a pet image")
    @PostMapping("api/v1/upload/pet/{id}/image/")
    public ResponseEntity<Void> uploadImagePet(@PathVariable("id") Long id, @RequestBody FileUploadDTO file) {
        String filePath = String.format("/pet/%s/image/", id);
        fileService.uploadImagePet(filePath, file, id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "Download a pet image")
    @GetMapping("api/v1/download/pet/{id}/image/{fileName}")
    public ResponseEntity<Resource> downloadImagePet(@PathVariable("id") Long id,
                                                     @PathVariable("fileName") String fileName) {
        String filePath = String.format("/pet/%s/image/%s", id, fileName);
        byte[] file = fileService.downloadImagePet(filePath, id);

        Resource resource = new ByteArrayResource(file);
        String mimeType = fileService.getFileMimeTypeFromFileName(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length))
                .body(resource);
    }

    //TODO: add a list all files/image from a pet
}
