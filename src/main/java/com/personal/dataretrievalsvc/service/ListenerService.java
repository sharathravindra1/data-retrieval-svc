package com.personal.dataretrievalsvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;


@Slf4j
@Service
@EnableJms
public class ListenerService {

    @Autowired
    private MessageProcessingService processingService;

    @Autowired
    private PersistanceService persistanceService;

    /***
     * SQS Service listener, listening to events from the ingestion-service.
     * Process the message via MessageProcessingService.
     * Concurrency & queue is externalized.
     * @param message
     */
    @JmsListener(destination = "${sqs.listener.queue-name}", concurrency = "${sqs.listener.concurrency}", containerFactory = "sqsconnectionfactory")
    public void onMessage(TextMessage message)  {
       String applicationNumber = null;

       try {
           applicationNumber = message.getText();
           message.acknowledge();
           log.info("Application number recieved :{}", message.getText());
           processingService.process(applicationNumber);
        }
        catch (Exception e){
            log.error("Failed to persist:{}", applicationNumber);
            persistanceService.updateWorkflowForAppNumber("FAILED", applicationNumber);
        }

    }

}
