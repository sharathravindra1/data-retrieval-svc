package com.personal.dataretrievalsvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class FileHandlerService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Download Patent file in PDF format using the patent-api
     * @param path
     * @return Patent
     * @throws Exception if response code is not 2XX
     */
    public File downloadFile(String path) throws Exception {
        log.info("Begin Downloading file from: {}", path);
        HttpEntity<?> entity = getHttpEntity();
        ResponseEntity<byte[]> response  = restTemplate.exchange(path, HttpMethod.GET, entity, byte[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Finished downloading file");
            // create a temporary file
            Path tempFile = Files.createTempFile(null, ".pdf");
            File patentFile = Files.write(tempFile, response.getBody()).toFile();
            log.info("Returning patent file:{}", patentFile.getAbsolutePath());
            return patentFile;
        }
        throw new RuntimeException("Exception occured calling API "+ response.getStatusCode());

    }

    // stub for Junits
    protected HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return entity;
    }
}


