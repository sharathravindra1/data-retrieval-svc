package com.personal.dataretrievalsvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;


@Service
@Slf4j
public class JmsExceptionHandler implements ErrorHandler {


    @Override
    public void handleError(Throwable throwable) {
        log.error("Error while processing JMS messages ", throwable);
    }
}

