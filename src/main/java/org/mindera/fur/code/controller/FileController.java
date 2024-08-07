package org.mindera.fur.code.controller;

import org.mindera.fur.code.dto.file.FileUploadDTO;
import org.mindera.fur.code.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/image/pet/{id}")
    public void uploadFile(@RequestParam("id") Long id,
                           @RequestBody FileUploadDTO file
    ) {
        String filePath = "pet/image/" + id;
        fileService.uploadFile(filePath, id, file);
    }
}
