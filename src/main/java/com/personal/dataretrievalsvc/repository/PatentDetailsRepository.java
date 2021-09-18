package com.personal.dataretrievalsvc.repository;

import com.personal.dataretrievalsvc.entity.PatentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatentDetailsRepository extends CrudRepository<PatentDetails, String> {
    PatentDetails findByPatentApplicationNumber(String patentApplicationNumber);

    List<PatentDetails> findAllByWorkflowStatus(String workFlow);
}
