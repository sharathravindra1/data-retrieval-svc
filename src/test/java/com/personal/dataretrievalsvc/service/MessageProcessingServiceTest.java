package com.personal.dataretrievalsvc.service;

import com.personal.dataretrievalsvc.entity.PatentDetails;
import com.personal.dataretrievalsvc.model.PatentDataResponse;
import com.personal.dataretrievalsvc.model.Result;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Collections;

public class MessageProcessingServiceTest {


    @Test(description = "Successful Processing of message")
    public void testMessageProcessingServiceHappy() throws Exception {
        MessageProcessingService messageProcessingService = new MessageProcessingService(){
            protected HttpEntity<?> getHttpEntity() {
                    HttpHeaders headers = new HttpHeaders();
                    HttpEntity<?> entity = new HttpEntity<>(headers);
                    return entity;
                }

            };
        RestTemplate restTemplate = PowerMock.createMock(RestTemplate.class);
        PersistanceService persistanceService = PowerMock.createMock(PersistanceService.class);
        FileHandlerService fileHandlerService = PowerMock.createMock(FileHandlerService.class);
        ImageProcessingService imageProcessingService = PowerMock.createMock(ImageProcessingService.class);
        String url = "http://spoofer.com";

        ReflectionTestUtils.setField(messageProcessingService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(messageProcessingService, "persistanceService", persistanceService);
        ReflectionTestUtils.setField(messageProcessingService, "fileHandlerService", fileHandlerService);
        ReflectionTestUtils.setField(messageProcessingService, "imageProcessingService", imageProcessingService);
        ReflectionTestUtils.setField(messageProcessingService, "url", url);

        String path = "http://spoofer.com?patentApplicationNumber=US-Test-App&api_key";
        PatentDataResponse response = new PatentDataResponse();
        response.setRecordTotalQuantity(1);
        Result result = new Result();
        result.setFilelocationURI("http://spoofer.com/files");
        response.setResults(Collections.singletonList(result));
        PatentDetails patentDetails = PowerMock.createMock(PatentDetails.class);
        ResponseEntity<PatentDataResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        EasyMock.expect(restTemplate.exchange(path, HttpMethod.GET, messageProcessingService.getHttpEntity(), PatentDataResponse.class)).andReturn(responseEntity);

        EasyMock.expect(persistanceService.getPatentDetails(EasyMock.anyString())).andReturn(patentDetails);
        EasyMock.expect(persistanceService.updateWorkflowAndReturn(EasyMock.anyObject(String.class),EasyMock.anyObject(PatentDetails.class))).andReturn(patentDetails).anyTimes();

        persistanceService.persistPatentMetadataDetails(EasyMock.anyObject(PatentDataResponse.class), EasyMock.anyObject(PatentDetails.class));
        EasyMock.expectLastCall();

        EasyMock.expect(fileHandlerService.downloadFile(EasyMock.anyString())).andReturn(EasyMock.anyObject(File.class));

        imageProcessingService.processFile(EasyMock.anyObject(File.class), patentDetails);
        EasyMock.expectLastCall();
        EasyMock.replay(restTemplate,persistanceService,fileHandlerService,imageProcessingService);

        messageProcessingService.process("US-Test-App");

    }

    @Test(description = "Downstream API throws Bad Gateway",expectedExceptions = RuntimeException.class)
    public void testMessageProcessingServiceFail() throws Exception {
        MessageProcessingService messageProcessingService = new MessageProcessingService(){
            protected HttpEntity<?> getHttpEntity() {
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<?> entity = new HttpEntity<>(headers);
                return entity;
            }

        };
        RestTemplate restTemplate = PowerMock.createMock(RestTemplate.class);
        PersistanceService persistanceService = PowerMock.createMock(PersistanceService.class);

        String url = "http://spoofer.com";

        ReflectionTestUtils.setField(messageProcessingService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(messageProcessingService, "persistanceService", persistanceService);
        ReflectionTestUtils.setField(messageProcessingService, "url", url);

        String path = "http://spoofer.com?patentApplicationNumber=US-Test-App&api_key";

        PatentDetails patentDetails = PowerMock.createMock(PatentDetails.class);
        ResponseEntity<PatentDataResponse> responseEntity = new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        EasyMock.expect(restTemplate.exchange(path, HttpMethod.GET, messageProcessingService.getHttpEntity(), PatentDataResponse.class)).andReturn(responseEntity);
        EasyMock.expect(persistanceService.getPatentDetails(EasyMock.anyString())).andReturn(patentDetails);
        EasyMock.expect(persistanceService.updateWorkflowAndReturn(EasyMock.anyObject(String.class),EasyMock.anyObject(PatentDetails.class))).andReturn(patentDetails).anyTimes();
        persistanceService.persistPatentMetadataDetails(EasyMock.anyObject(PatentDataResponse.class), EasyMock.anyObject(PatentDetails.class));
        EasyMock.expectLastCall();
        EasyMock.replay(restTemplate,persistanceService);

        messageProcessingService.process("US-Test-App");

    }
}
