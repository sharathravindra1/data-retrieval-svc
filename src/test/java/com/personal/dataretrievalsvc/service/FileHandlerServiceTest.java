package com.personal.dataretrievalsvc.service;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;


public class FileHandlerServiceTest {

    @Test(description = "Successful File handling")
    public void testFileHandlerServiceHappy() throws Exception {
        FileHandlerService fileHandlerService = new FileHandlerService() {
            protected HttpEntity<?> getHttpEntity() {
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<?> entity = new HttpEntity<>(headers);
                return entity;
            }

        };
        RestTemplate restTemplate = PowerMock.createMock(RestTemplate.class);
        ReflectionTestUtils.setField(fileHandlerService,"restTemplate", restTemplate);

        String path = "/somemockpath";
        byte[] response = "content".getBytes();
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        EasyMock.expect(restTemplate.exchange(path,HttpMethod.GET, fileHandlerService.getHttpEntity(), byte[].class)).andReturn(responseEntity);
        EasyMock.replay(restTemplate);
        File f = fileHandlerService.downloadFile(path);
        Assert.assertTrue(f.exists());

    }

    @Test(description = "Downstream API throws error",expectedExceptions = {RuntimeException.class})
    public void testFileHandlerServiceBad() throws Exception {
        FileHandlerService fileHandlerService = new FileHandlerService() {
            protected HttpEntity<?> getHttpEntity() {
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<?> entity = new HttpEntity<>(headers);
                return entity;
            }

        };
        RestTemplate restTemplate = PowerMock.createMock(RestTemplate.class);
        ReflectionTestUtils.setField(fileHandlerService,"restTemplate", restTemplate);

        String path = "/somemockpath";

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        EasyMock.expect(restTemplate.exchange(path,HttpMethod.GET, fileHandlerService.getHttpEntity(), byte[].class)).andReturn(responseEntity);
        EasyMock.replay(restTemplate);
        File f = fileHandlerService.downloadFile(path);
        Assert.assertTrue(!f.exists());
        Assert.fail();

    }


}
