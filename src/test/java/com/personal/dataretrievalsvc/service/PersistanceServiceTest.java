package com.personal.dataretrievalsvc.service;

import com.personal.dataretrievalsvc.repository.PatentDetailsRepository;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

public class PersistanceServiceTest {

    PersistanceService persistanceService = new PersistanceService();
    PatentDetailsRepository patentDetailsRepository = PowerMock.createMock(PatentDetailsRepository.class);
    PatentDetailsTransformer patentDetailsTransformer = PowerMock.createMock(PatentDetailsTransformer.class);

    @BeforeMethod
    public void setUp() {
        ReflectionTestUtils.setField(persistanceService, "patentDetailsRepository", patentDetailsRepository);
        ReflectionTestUtils.setField(persistanceService, "patentDetailsTransformer", patentDetailsTransformer);
    }

    @Test(description = "Get patent details by status Happy")
    public void testGetPatentDetailsByStatusHappy() {
        EasyMock.expect(patentDetailsRepository.findAllByWorkflowStatus("my-workflow")).andReturn(Collections.emptyList());
        EasyMock.replay(patentDetailsRepository);
        persistanceService.getPatentDetailsByStatus("my-workflow");

    }


}
