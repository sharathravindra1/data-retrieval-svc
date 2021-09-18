package com.personal.dataretrievalsvc.service;

import com.personal.dataretrievalsvc.entity.PatentDetails;
import com.personal.dataretrievalsvc.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PatentDetailsTransformer {
    /**
     * Transformer to transform Result from API to Patent Metadata
     * @param result
     * @param patentDetails
     * @return
     */
    public PatentDetails transformToPatentDetails(Result result, PatentDetails patentDetails) {

        patentDetails.setAbstractText(result.getAbstractText().stream().collect(Collectors.joining()));
        patentDetails.setArchiveURI(result.getArchiveURI());
        patentDetails.setAssigneeEntityName(result.getAssigneeEntityName());
        patentDetails.setAssigneePostalAddressText(result.getAssigneePostalAddressText());
        patentDetails.setClaimText(join(result.getClaimText()));
        patentDetails.setPatentNumber(result.getPatentNumber());
        patentDetails.setMainCPCSymbolText(result.getMainCPCSymbolText());
        patentDetails.setDescriptionText(join(result.getDescriptionText()));
        patentDetails.setFilelocationURI(result.getFilelocationURI());
        patentDetails.setGrantDate(result.getGrantDate());
        patentDetails.setGrantDocumentIdentifier(result.getGrantDocumentIdentifier());
        patentDetails.setFilingDate(result.getFilingDate());
        patentDetails.setFurtherCPCSymbolArrayText(join(result.getFurtherCPCSymbolArrayText()));
        patentDetails.setInventionSubjectMatterCategory(result.getInventionSubjectMatterCategory());
        patentDetails.setInventionTitle(result.getInventionTitle());
        patentDetails.setInventorNameArrayText(join(result.getInventorNameArrayText()));

        return patentDetails;
    }

    private String join(List<String> text){
        if(text == null || text.isEmpty()){
            log.info("No text found:Defaulting to empty");
            return "";
        }
        return text.stream().collect(Collectors.joining(","));
    }

}
