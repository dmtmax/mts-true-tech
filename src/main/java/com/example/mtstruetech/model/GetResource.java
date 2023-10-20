package com.example.mtstruetech.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetResource(
        Integer id,
        Integer cost,
        Integer cpu,
        @JsonProperty("cpu_load")
        Double cpuLoad,
        Boolean failed,
        Integer ram,
        @JsonProperty("ram_load")
        Double ramLoad,
        ResourceType type
) {
}
