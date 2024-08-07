package org.mindera.fur.code.service;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.mindera.fur.code.dto.file.FileUploadDTO;
import org.mindera.fur.code.exceptions.file.FileException;
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
import java.util.Base64;

@Service
public class FileService {

    private static final String BUCKET_NAME = "furcode";

    private final MinioClient minioClient;
    private final PetService petService;

    @Autowired
    public FileService(MinioClient minioClient, PetService petService) {
        this.minioClient = minioClient;
        this.petService = petService;
    }

    /**
     * Uploads a file to the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param id       the ID of the pet associated with the file.
     * @param file     the file to upload.
     */
    public void uploadImagePet(String filePath, FileUploadDTO file, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Pet ID must be provided");
        }

        // disable just for testing
        /*
        if (petService.findPetById(id) == null) {
            throw new IllegalArgumentException("Pet not found");
        }
         */

        //check if the file is valid
        checkFileValidity(file);

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
        if (file.getFileName() == null) {
            throw new FileException("File name and base64 content must be provided");
        }

        if (file.getMd5() == null) {
            throw new FileException("File checksum must be provided");
        }

        checkFileChecksum(file.getFileData(), file.getMd5());
    }

    /**
     * Downloads a file from the Minio bucket.
     *
     * @param filePath the path to the file in the Minio bucket.
     * @param id       the ID of the pet associated with the file.
     * @return the downloaded file.
     */
    public byte[] downloadImagePet(String filePath, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Pet ID must be provided");
        }

        // disable just for testing
        /*
        if (petService.findPetById(id) == null) {
            throw new IllegalArgumentException("Pet not found");
        }
         */

        return downloadFileFromBucket(filePath);
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
}
