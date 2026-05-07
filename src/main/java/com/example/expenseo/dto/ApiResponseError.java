package com.example.expenseo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ApiResponseError {

    private LocalDateTime timeStamp;

    private int status;

    private String error;

    private String path;

    private String message;

    private Map<String,String> validationsErrors;
}
