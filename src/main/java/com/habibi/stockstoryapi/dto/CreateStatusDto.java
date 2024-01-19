package com.habibi.stockstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateStatusDto {
    private Status status;
    private String id;

    public enum Status {
        SUCCESS,
        FAIL
    }
}