package com.example.mtstruetech.model;

public record PostResource(
        Integer cpu,
        Integer ram,
        ResourceType type
) {
}
