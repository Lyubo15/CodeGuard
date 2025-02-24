package com.tu.codeguard.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * The {@code ClamAVService} interface holds the declarations of all ClamAV related methods.
 */
public interface ClamAVService {
    /**
     * Method which checks if the file sent as a method argument has a malicious content.
     *
     * @param multipartFile MultipartFile
     */
    void checkForMaliciousFile(MultipartFile multipartFile) throws IOException;

    /***
     * Method which checks if the InputStream sent as a method argument has a malicious content.
     * @param fileName the name of file
     * @param inputStream the file as input stream
     */
    void checkForMaliciousFile(String fileName, InputStream inputStream) throws IOException;

    /***
     * Method which checks if the byte array content sent as a method argument has a malicious content.
     * @param fileName the name of file
     * @param fileContent the file as byte array
     */
    void checkForMaliciousFile(String fileName, byte[] fileContent) throws IOException;
}
