package org.mindera.fur.code.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindera.fur.code.dto.file.FileUploadDTO;
import org.mindera.fur.code.service.FileService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerUnitTest {

    private FileService fileService;
    private FileController fileController;

    @BeforeEach
    void setUp() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Nested
    class FileControllerUnitTests {
        @Test
        void fileUploadController_givenInput_shouldSucceed() {

            FileUploadDTO fileUploadDTO = new FileUploadDTO();
            doNothing().when(fileService).uploadImagePet(anyString(), any(FileUploadDTO.class), anyLong());

            ResponseEntity<Void> response = fileController.uploadImagePet(1L, fileUploadDTO);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
            verify(fileService, times(1)).uploadImagePet(anyString(), any(FileUploadDTO.class), anyLong());
        }

        @Test
        void fileDownloadImagePetController_givenInput_shouldSuccess() {
            byte[] fileData = "fileData".getBytes();
            String fileName = "image.jpg";
            when(fileService.downloadImagePet(anyString(), anyLong())).thenReturn(fileData);
            when(fileService.getFileMimeTypeFromFileName(fileName)).thenReturn("image/jpeg");

            ResponseEntity<Resource> response = fileController.downloadImagePet(1L, fileName);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody() instanceof ByteArrayResource);
            verify(fileService, times(1)).downloadImagePet(anyString(), anyLong());
            verify(fileService, times(1)).getFileMimeTypeFromFileName(fileName);
        }
    }
}
