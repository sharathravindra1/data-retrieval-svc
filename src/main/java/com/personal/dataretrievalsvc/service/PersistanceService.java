package com.personal.dataretrievalsvc.service;

import com.personal.dataretrievalsvc.entity.PatentDetails;
import com.personal.dataretrievalsvc.model.PatentDataResponse;
import com.personal.dataretrievalsvc.model.Result;
import com.personal.dataretrievalsvc.repository.PatentDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PersistanceService {

    @Autowired
    private PatentDetailsRepository patentDetailsRepository;

    @Autowired
    private PatentDetailsTransformer patentDetailsTransformer;

    /**
     * Persist Patent info into Patent_Details table
     * @param responseBody
     * @param patentDetails
     */
    public void persistPatentMetadataDetails(PatentDataResponse responseBody, PatentDetails patentDetails)  {
       // log.info("ResponseBody :{}", responseBody);
        Result result = responseBody.getResults().get(0);
        String applicationNumber = result.getPatentApplicationNumber();
        log.info("Begin updating patent details:{}", applicationNumber);
        patentDetails = transformToPatentDetails(result, patentDetails);
        patentDetails.setWorkflowStatus("metadata_read");
        patentDetailsRepository.save(patentDetails);
        log.info("Finished updating patent details:{}",applicationNumber);
    }

    private PatentDetails transformToPatentDetails(Result result, PatentDetails patentDetails) {
        return  patentDetailsTransformer.transformToPatentDetails(result, patentDetails);

    }

    /***
     * Generic method to update status for a Patent
     * @param status
     * @param patentDetails
     * @return
     */
    public PatentDetails updateWorkflowAndReturn(String status, PatentDetails patentDetails) {
        patentDetails.setWorkflowStatus(status);
        patentDetails.setUpdatedDate(new Date());
        patentDetailsRepository.save(patentDetails);
        return patentDetails;
    }

    /**
     * Generic method returning Patent data by application number
     * @param applicationNumber
     * @return
     */
    public PatentDetails getPatentDetails(String applicationNumber) {
        PatentDetails patentDetails =patentDetailsRepository.findByPatentApplicationNumber(applicationNumber);
        if(patentDetails == null){
            throw new RuntimeException("Application number "+ applicationNumber + " is not found");
        }
        return patentDetails;
    }

    public PatentDetails save(PatentDetails patentDetails){
       return patentDetailsRepository.save(patentDetails);
    }

    public void updateWorkflowForAppNumber(String workflow, String appNum ){
        PatentDetails patentDetails = patentDetailsRepository.findByPatentApplicationNumber(appNum);
        updateWorkflowAndReturn(workflow, patentDetails);
    }

    public List<PatentDetails> getPatentDetailsByStatus(String workFlow) {
       return patentDetailsRepository.findAllByWorkflowStatus(workFlow);


    }

}


