package com.personal.dataretrievalsvc.service;

import com.amazon.sqs.javamessaging.message.SQSTextMessage;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.Test;

import javax.jms.TextMessage;

public class ListenerServiceTest {

    @Test(description = "Listener happy path")
    public void testListenerServiceHappy() throws Exception {
        ListenerService listenerService = new ListenerService();

        MessageProcessingService processingService = PowerMock.createMock(MessageProcessingService.class);
        PersistanceService persistanceService = PowerMock.createMock(PersistanceService.class);
        ReflectionTestUtils.setField(listenerService, "processingService", processingService);
        ReflectionTestUtils.setField(listenerService, "persistanceService", persistanceService);
        TextMessage textMessage = new SQSTextMessage();
        textMessage.setText("US-test-app-number");
        processingService.process(EasyMock.anyString());
        EasyMock.expectLastCall();
        EasyMock.replay(processingService, persistanceService);
        listenerService.onMessage(textMessage);
    }

    @Test(description = "Listener processing fail due to Message Processing Svc error")
    public void testListenerServiceFail() throws Exception {
        ListenerService listenerService = new ListenerService();
        MessageProcessingService processingService = PowerMock.createMock(MessageProcessingService.class);
        PersistanceService persistanceService = PowerMock.createMock(PersistanceService.class);
        ReflectionTestUtils.setField(listenerService, "processingService", processingService);
        ReflectionTestUtils.setField(listenerService, "persistanceService", persistanceService);
        TextMessage textMessage = new SQSTextMessage();
        textMessage.setText("US-test-app-number");
        processingService.process(EasyMock.anyString());
        EasyMock.expectLastCall().andThrow(new RuntimeException());
        persistanceService.updateWorkflowForAppNumber(EasyMock.anyString(), EasyMock.anyString());
        EasyMock.expectLastCall();
        EasyMock.replay(processingService, persistanceService);
        listenerService.onMessage(textMessage);
    }
}
