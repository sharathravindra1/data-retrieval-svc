package com.personal.dataretrievalsvc.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Result {
    private String inventionSubjectMatterCategory;
    private String patentApplicationNumber;
    private String filingDate;
    private String mainCPCSymbolText;
    private List<String> furtherCPCSymbolArrayText;
    private List<String> inventorNameArrayText;
    private List<String> abstractText;
    private String assigneeEntityName;
    private String assigneePostalAddressText;
    private String inventionTitle;
    private String filelocationURI;
    private String archiveURI;
    private List<String> claimText;
    private List<String> descriptionText;
    private String grantDocumentIdentifier;
    private String grantDate;
    private String patentNumber;
}
