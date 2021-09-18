package com.personal.dataretrievalsvc.model;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PatentDataResponse {

    public List<Result> results;
    public int recordTotalQuantity;
}
