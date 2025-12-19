package com.library.library.dto;

import lombok.Data;

@Data
public class BorrowRequest {
    private Long bookId;
    private Long readerId;
}