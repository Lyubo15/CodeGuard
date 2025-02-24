package com.tu.codeguard.service.impl;

import com.tu.codeguard.config.properties.ClamAVProperties;
import com.tu.codeguard.exceptions.MaliciousFileException;
import com.tu.codeguard.service.ClamAVService;
import fi.solita.clamav.ClamAVClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.tu.codeguard.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClamAVServiceImpl implements ClamAVService {

    private final ClamAVClient clamAVClient;
    private final ClamAVProperties clamAVProperties;

    @Override
    public void checkForMaliciousFile(MultipartFile multipartFile) throws IOException, MaliciousFileException {
        if (isVirusScanningDisabled()) return;
        String fileName = multipartFile.getOriginalFilename();
        try {
            checkScanResults(ClamAVClient.isCleanReply(clamAVClient.scan(multipartFile.getInputStream())), fileName);
        } catch (IOException e) {
            log.error(String.format(FILE_IO_EXCEPTION, multipartFile.getOriginalFilename()));
            throw e;
        }
    }


    @Override
    public void checkForMaliciousFile(String fileName, InputStream inputStream) throws IOException {
        if (isVirusScanningDisabled()) return;
        try (inputStream) {
            checkScanResults(ClamAVClient.isCleanReply(clamAVClient.scan(inputStream)), fileName);
        } catch (IOException e) {
            log.error(String.format(FILE_IO_EXCEPTION, fileName));
            throw e;
        }
    }

    @Override
    public void checkForMaliciousFile(String fileName, byte[] fileContent) throws IOException {
        if (isVirusScanningDisabled()) return;
        try {
            checkScanResults(ClamAVClient.isCleanReply(clamAVClient.scan(fileContent)), fileName);
        } catch (IOException e) {
            log.error(String.format(FILE_IO_EXCEPTION, fileName));
            throw e;
        }
    }

    private void checkScanResults(boolean cleanReply, String fileName) {
        if (!cleanReply) {
            String error = String.format(FILE_MALICIOUS_EXCEPTION, fileName);
            log.error(error);
            throw new MaliciousFileException(error);
        }
        log.info(CLAMAV_FILE_CLEAN);
    }

    private boolean isVirusScanningDisabled() {
        if (!clamAVProperties.isEnabled()) {
            log.warn(CLAMAV_NOT_ENABLED);
            return true;
        }
        return false;
    }
}
