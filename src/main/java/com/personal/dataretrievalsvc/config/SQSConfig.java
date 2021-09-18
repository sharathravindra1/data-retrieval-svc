package com.personal.dataretrievalsvc.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.personal.dataretrievalsvc.service.JmsExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
@Slf4j
public class SQSConfig {

    @Value("${aws.credentials.region}")
    private String awsRegion;
    @Autowired
    BasicAWSCredentials basicAWSCredentials;

    @Autowired
    JmsExceptionHandler jmsExceptionHandler;
    @Value("${sqs.listener.concurrency}")
    private String concurrency;

    @Bean
    public AmazonSQS amazonSQS() {
        log.info("Setting up sqs client");
        AmazonSQS amazonSQS =  AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.fromName(awsRegion))
                .withRequestHandlers()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials)).build();
        log.info("Finished setting up sqs client");
        return amazonSQS;

    }

    @Bean(name = "sqsconnectionfactory")
    DefaultJmsListenerContainerFactory riskAnalysisMessageListenerContainerFactory() {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        defaultJmsListenerContainerFactory.setConnectionFactory(new SQSConnectionFactory(new ProviderConfiguration(), amazonSQS()));
        defaultJmsListenerContainerFactory.setDestinationResolver(new DynamicDestinationResolver());
        defaultJmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        defaultJmsListenerContainerFactory.setErrorHandler(jmsExceptionHandler);
        defaultJmsListenerContainerFactory.setAutoStartup(true);
        defaultJmsListenerContainerFactory.setConcurrency(concurrency);
        return defaultJmsListenerContainerFactory;
    }
}
