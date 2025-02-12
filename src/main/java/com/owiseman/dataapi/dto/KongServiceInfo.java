package com.owiseman.dataapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KongServiceInfo {
    @JsonProperty("id")
    private String id;

    public String getId() { return id; }
}

