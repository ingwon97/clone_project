package com.project.cloneproject.controller;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PhotoDto {
    private String key;

    private String path;
}
