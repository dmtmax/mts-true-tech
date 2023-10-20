package com.example.mtstruetech.model;

public record Price(
        Integer cost,
        Integer cpu,
        Integer id,
        String name,
        Integer ram,
        ResourceType type
) {}
