package com.personal.dataretrievalsvc.controller;

import com.personal.dataretrievalsvc.entity.PatentDetails;
import com.personal.dataretrievalsvc.service.MessageProcessingService;
import com.personal.dataretrievalsvc.service.PersistanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/retrieve")
@Slf4j
@CrossOrigin("*")
public class DataRetrivalController {

    @Autowired
    PersistanceService persistanceService;

    @Autowired
    MessageProcessingService messageProcessingService;

    @GetMapping(path = "/patent")
    public ResponseEntity retreivePatentInfo(@RequestParam String applicationNumber){
      PatentDetails patentDetails = persistanceService.getPatentDetails(applicationNumber);
        return new ResponseEntity<>(patentDetails, HttpStatus.OK);
    }

    @PostMapping(path = "/retry")
    public ResponseEntity retryFailed()  {

         CompletableFuture.runAsync(() -> {
            List<PatentDetails> patentDetailsList = persistanceService.getPatentDetailsByStatus("FAILED");
            for(PatentDetails patentDetails : patentDetailsList){
                try {
                    messageProcessingService.process(patentDetails.getPatentApplicationNumber());
                } catch (Exception e) {
                   log.error("Failed to process: {}", e);
                }
            }
        });

        return new ResponseEntity<>("processing-failed-batch", HttpStatus.OK);

    }
}
