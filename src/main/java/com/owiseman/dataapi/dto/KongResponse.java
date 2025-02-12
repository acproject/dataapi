package com.owiseman.dataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class KongResponse {
    @JsonProperty("next")
    private String next;

    @JsonProperty("data")
    private List<KongData> data;

    public String getNext() { return next; }
    public List<KongData> getData() { return data; }
}


