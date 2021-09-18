package com.personal.dataretrievalsvc.service;

import com.personal.dataretrievalsvc.entity.PatentDetails;
import com.personal.dataretrievalsvc.model.PatentDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;

@Slf4j
@Service
public class MessageProcessingService {
    @Value("${uspto.developr.url}")
    private String url;
    @Value("${uspto.developer.apiKey}")
    private String apiKey;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PersistanceService persistanceService;
    @Autowired
    private FileHandlerService fileHandlerService;
    @Autowired
    private ImageProcessingService imageProcessingService;
    

    /***
     * Orcheastrates processing of message given an application number.
     *
     * @param applicationNumber
     * @throws Exception
     */
    public void process(String applicationNumber) throws Exception {
        log.info("Begin processing :{}", applicationNumber);
        PatentDetails patentDetails = persistanceService.getPatentDetails(applicationNumber);
        patentDetails = persistanceService.updateWorkflowAndReturn("processing", patentDetails);
        HttpEntity<?> entity = getHttpEntity();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("patentApplicationNumber", applicationNumber)
                .queryParam("api_key", apiKey);
        log.info("Invoking url:{}", builder.toUriString());
        ResponseEntity response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                PatentDataResponse.class);
        HttpStatus responseCode = response.getStatusCode();
        log.info("Received response-code = {}", responseCode);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Response code received for applicationNumber :" + applicationNumber + " is :" + responseCode);
        }
        PatentDataResponse responseBody = (PatentDataResponse) response.getBody();
        persistanceService.persistPatentMetadataDetails(responseBody, patentDetails);
        String filelocationURI = responseBody.getResults().get(0).getFilelocationURI();
        File patentDataFile = fileHandlerService.downloadFile(filelocationURI);
        patentDetails = persistanceService.updateWorkflowAndReturn("patent_file_ready", patentDetails);
        imageProcessingService.processFile(patentDataFile, patentDetails);
        persistanceService.updateWorkflowAndReturn("COMPLETED", patentDetails);

    }

    protected HttpEntity<?> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return entity;
    }


}
