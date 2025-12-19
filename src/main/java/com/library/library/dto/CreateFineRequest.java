package com.library.library.dto;

import lombok.Data;

@Data
public class CreateFineRequest {
    private Long readerId;
    private Long bookId;
    private double amount;
}