package org.mindera.fur.code.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.mindera.fur.code.aspect.roleauth.RequiresRole;
import org.mindera.fur.code.dto.file.FileUploadDTO;
import org.mindera.fur.code.exceptions.file.FileException;
import org.mindera.fur.code.model.Role;
import org.mindera.fur.code.service.pet.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Schema(description = "The file service")
@Service
public class FileService {

    private static final String BUCKET_NAME = "furcode";
    private static final int MAX_FILE_UPLOAD_SIZE = 10000000;
    private static final List<String> SUPPORTED_EXTENSIONS = List.of("jpg", "png", "gif", "pdf");

    private final MinioClient minioClient;
    private final PetService petService;
    private final ShelterService shelterService;

    @Autowired
    public FileService(MinioClient minioClient, PetService petService, ShelterService shelterService) {
        this.minioClient = minioClient;
        this.petService = petService;
        this.shelterService = shelterService;
    }

    /**
     * Uploads a pet image to the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param id       the ID of the pet associated with the file.
     * @param file     the file to upload.
     */
    @RequiresRole(value = Role.ADMIN, isPetOperation = true, petIdParam = 2)
    public void uploadImagePet(String filePath, FileUploadDTO file, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Pet ID must be provided");
        }

        //if (petService.findPetById(id) == null) {
        //    throw new IllegalArgumentException("Pet not found");
        //}

        checkFileValidity(file);
        checkImageType(file.getFileData());

        File newFile = convertBase64ToFile(file.getFileData(), file.getMd5());
        uploadFileToBucket(filePath, file, newFile);
        newFile.delete();
    }

    /**
     * Uploads a shelter image to the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param id       the ID of the shelter associated with the file.
     * @param file     the file to upload.
     */
    //@RequiresRole(value = Role.ADMIN) TODO: enable this after testing
    public void uploadImageShelter(String filePath, FileUploadDTO file, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Shelter ID must be provided");
        }

        //if (shelterService.findShelterById(id) == null) {
        //    throw new IllegalArgumentException("Shelter not found");
        //}

        checkFileValidity(file);
        checkImageType(file.getFileData());

        File newFile = convertBase64ToFile(file.getFileData(), file.getMd5());
        uploadFileToBucket(filePath, file, newFile);
        newFile.delete();
    }

    /**
     * Checks the validity of a file.
     *
     * @param file the file to check.
     * @throws FileException if the file is invalid.
     */
    private void checkFileValidity(FileUploadDTO file) {
        if (file.getFileName() == null || file.getFileName().isEmpty()) {
            throw new FileException("File name must be provided");
        }

        if (file.getFileData() == null || file.getFileData().isEmpty()) {
            throw new FileException("File base64 content must be provided");
        }

        if (file.getMd5() == null || file.getMd5().isEmpty()) {
            throw new FileException("File checksum must be provided");
        }

        checkFileChecksum(file.getFileData(), file.getMd5());
        checkFileSize(file.getFileData());
    }

    /**
     * Checks the type of image.
     * It uses the first 16 bytes of the file to check the type, using file signatures.
     * PNG, JPG, and WEBP are supported. Others throw an exception.
     *
     * @param fileData Base64-encoded string.
     * @throws FileException if the file type is not an image.
     */
    private void checkImageType(String fileData) {
        String fileSignature = StringUtils.left(fileData, 16);
        // Todo: change this to a better place
        List<String> fileSignatures = List.of(
                "iVBORw0KGgo", // png
                "/9j/", //jpeg, jpg
                "U"); //webp

        if (fileSignatures.stream()
                .noneMatch(fileSignature::startsWith)) {
            throw new FileException("File type must be an image. Images allowed are png, jpg, and webp.");
        }
    }

    /**
     * Checks the size of a file.
     *
     * @param fileData the Base64-encoded string.
     * @throws FileException if the file size is greater than the maximum allowed size.
     */
    private void checkFileSize(String fileData) {
        int fileSize = fileSize(fileData);
        if (fileSize > MAX_FILE_UPLOAD_SIZE) {
            throw new FileException("File size must be less than " + MAX_FILE_UPLOAD_SIZE + " bytes. Current file size: " + fileSize + " bytes" +
                    " Max upload in MB: " + (MAX_FILE_UPLOAD_SIZE / 1000000) + ". File size in MB: " + (fileSize / 1000000));
        }
    }

    /**
     * Calculates the size of a file.
     *
     * @param fileData the Base64-encoded string.
     * @return the size of the file.
     */
    private int fileSize(String fileData) {
        byte[] decodedContent = Base64.getDecoder().decode(fileData.getBytes(StandardCharsets.UTF_8));
        return decodedContent.length;
    }

    /**
     * Downloads a pet image from the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param id       the ID of the pet associated with the file.
     * @return the downloaded file.
     */
    public byte[] downloadImagePet(String filePath, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Pet ID must be provided");
        }

        //if (petService.findPetById(id) == null) {
        //    throw new IllegalArgumentException("Pet not found");
        //}

        byte[] fileData = null;
        try {
            fileData = downloadFileFromBucket(filePath);
            return fileData;
        } catch (FileException e) {
            for (String extension : SUPPORTED_EXTENSIONS) {
                try {
                    String fileWithExtension = filePath + "." + extension;
                    fileData = downloadFileFromBucket(fileWithExtension);
                    return fileData; // Return if successful
                } catch (FileException ignored) {
                    // Ignore the error and try the next extension
                }
            }
        }
        throw new FileException("File not found for name: " + filePath + " with any supported extensions");
    }

    /**
     * Downloads a shelter image from the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param id       the ID of the pet associated with the file.
     * @return the downloaded file.
     */
    public byte[] downloadImageShelter(String filePath, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Shelter ID must be provided");
        }

        //if (shelterService.findShelterById(id) == null) {
        //    throw new IllegalArgumentException("Shelter not found");
        //}

        byte[] fileData = null;
        try {
            fileData = downloadFileFromBucket(filePath);
            return fileData;
        } catch (FileException e) {
            for (String extension : SUPPORTED_EXTENSIONS) {
                try {
                    String fileWithExtension = filePath + "." + extension;
                    fileData = downloadFileFromBucket(fileWithExtension);
                    return fileData; // Return if successful
                } catch (FileException ignored) {
                    // Ignore the error and try the next extension
                }
            }
        }
        throw new FileException("File not found for name: " + filePath + " with any supported extensions");
    }

    /**
     * Gets the MIME type of file from its stream.
     *
     * @param file the file as Resource.
     * @return the MIME type of the file.
     */
    public String getFileMimeTypeFromStream(Resource file) {
        String mimeType = null;
        try {
            InputStream is = new BufferedInputStream(file.getInputStream());
            mimeType = URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        } finally {
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
        }

        return mimeType;
    }

    /**
     * Gets the MIME type of file from its file name.
     *
     * @param fileName the name of the file.
     * @return the MIME type of the file.
     */
    public String getFileMimeTypeFromFileName(String fileName) {
        return URLConnection.guessContentTypeFromName(fileName);
    }

    /**
     * Gets the MIME type of file from its file path.
     *
     * @param filePath the name of the file.
     * @return the MIME type of the file.
     */
    public String getFileMimeTypeFromFilePath(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        System.out.println(fileName);
        return URLConnection.guessContentTypeFromName(fileName);
    }

    /**
     * Checks the checksum of a file.
     *
     * @param fileData the Base64-encoded string.
     * @param checksum the checksum of the file.
     * @throws FileException if the checksum does not match.
     */
    private void checkFileChecksum(String fileData, String checksum) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(fileData.getBytes(StandardCharsets.UTF_8));
            String calculatedChecksum = new BigInteger(1, hash).toString(16);

            if (!checksum.equals(calculatedChecksum)) {
                throw new FileException("Checksum does not match: " + checksum + " vs " + calculatedChecksum);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new FileException(e.getMessage());
        }
    }

    /**
     * Converts a Base64-encoded string to a file.
     *
     * @param fileData the Base64-encoded string.
     * @param fileName the name of the file.
     * @return the file.
     */
    private File convertBase64ToFile(String fileData, String fileName) {
        byte[] decodedContent = Base64.getDecoder().decode(fileData.getBytes(StandardCharsets.UTF_8));
        return bytesToFile(decodedContent, fileName);
    }

    /**
     * Converts a byte array to a file.
     *
     * @param content  the byte array.
     * @param fileName the name of the file.
     * @return the file.
     */
    private File bytesToFile(byte[] content, String fileName) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
            return file;
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
    }

    /**
     * Gets an input stream from a file.
     *
     * @param file the file.
     * @return the input stream.
     */
    private FileInputStream getFileInputStreamFromFile(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileException(e.getMessage());
        }
    }

    /**
     * Gets the MIME type of file from its byte array.
     *
     * @param fileData the byte array.
     * @return the MIME type of the file.
     */
    public String getMimeTypeFromBytes(byte[] fileData) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData)) {
            return URLConnection.guessContentTypeFromStream(inputStream);
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    /**
     * Uploads a file to the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param file     the file to upload.
     * @param newFile  the file to upload.
     */
    private void uploadFileToBucket(String filePath, FileUploadDTO file, File newFile) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(filePath + file.getFileName())
                    .stream(getFileInputStreamFromFile(newFile), newFile.length(), -1)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new FileException(e.getMessage());
        }
    }

    /**
     * Downloads a file from the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @return the downloaded file.
     */
    private byte[] downloadFileFromBucket(String filePath) {
        try {
            GetObjectResponse file = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(filePath)
                    .build());
            return file.readAllBytes();

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new FileException(e.getMessage());
        }
    }


    public List<String> getAllImagesFromPet(Long petId) {
        List<String> imageUrls = new ArrayList<>();
        String prefix = String.format("pet/%s/image/", petId);

        try {
            for (Result<Item> result : minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(prefix)
                    .build())) {
                Item item = result.get(); // Extract the Item from the Result
                String objectName = item.objectName();

                String imageUrl = String.format("/api/v1/download/%s", objectName);
                imageUrls.add(imageUrl);
            }
        } catch (Exception e) {
            throw new FileException("Error listing pet images: " + e.getMessage());
        }

        return imageUrls;
    }

    public List<Map<String, String>> getAllImagesFromPetAsBase64(Long petId) {
        List<Map<String, String>> imageList = new ArrayList<>();
        String prefix = String.format("pet/%s/image/", petId);

        try {
            for (Result<Item> result : minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(prefix)
                    .build())) {
                Item item = result.get();
                String objectName = item.objectName();

                byte[] fileBytes = downloadFileFromBucket(objectName);
                String mimeType = getMimeTypeFromBytes(fileBytes);
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                String base64DataUrl = String.format("data:%s;base64,%s", mimeType, base64Image);

                // Create a map for the image
                Map<String, String> imageMap = new HashMap<>();
                imageMap.put("id", item.objectName());
                imageMap.put("name", objectName);
                imageMap.put("data", base64DataUrl);

                imageList.add(imageMap);
            }
        } catch (Exception e) {
            throw new FileException("Error listing pet images: " + e.getMessage());
        }

        return imageList;
    }

    public List<Map<String, String>> getAllImagesFromShelterAsBase64(Long petId) {
        List<Map<String, String>> imageList = new ArrayList<>();
        String prefix = String.format("shelter/%s/image/", petId);

        try {
            for (Result<Item> result : minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(prefix)
                    .build())) {
                Item item = result.get();
                String objectName = item.objectName();

                byte[] fileBytes = downloadFileFromBucket(objectName);
                String mimeType = getMimeTypeFromBytes(fileBytes);
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);
                String base64DataUrl = String.format("data:%s;base64,%s", mimeType, base64Image);

                // Create a map for the image
                Map<String, String> imageMap = new HashMap<>();
                imageMap.put("id", item.objectName());
                imageMap.put("name", objectName);
                imageMap.put("data", base64DataUrl);

                imageList.add(imageMap);
            }
        } catch (Exception e) {
            throw new FileException("Error listing pet images: " + e.getMessage());
        }

        return imageList;
    }


    @RequiresRole(value = Role.ADMIN, isPetOperation = true, petIdParam = 1)
    public void deleteImagePet(String filePath, Long petId) {
        try {
            // Logic to delete the file from the storage (e.g., Minio, S3)
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            throw new FileException("Error deleting image: " + e.getMessage());
        }
    }

}
