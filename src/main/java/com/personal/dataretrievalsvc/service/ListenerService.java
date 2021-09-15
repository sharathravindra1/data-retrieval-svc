package com.personal.dataretrievalsvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListenerService {

    @SqsListener(value = "${orders.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void processMessage(String message) {
        try {
            log.info("Received new SQS message: {}", message );

        } catch (Exception e) {
            throw new RuntimeException("Cannot process message from SQS", e);
        }
    }

}
