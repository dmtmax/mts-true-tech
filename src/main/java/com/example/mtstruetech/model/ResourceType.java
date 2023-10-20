package com.example.mtstruetech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ResourceType {
    @JsonProperty("db")
    DB,
    @JsonProperty("vm")
    VM;
}
