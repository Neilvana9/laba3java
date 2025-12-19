package com.library.library.dto;

import lombok.Data;

@Data
public class UpdateFineRequest {
    private Long readerId;
    private Long bookId;
    private double amount;
}