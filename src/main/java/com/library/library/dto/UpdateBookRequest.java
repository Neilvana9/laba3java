package com.library.library.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateBookRequest {
    private List<Long> authorIds;
    private String title;
}