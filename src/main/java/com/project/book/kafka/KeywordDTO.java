package com.project.book.kafka;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KeywordDTO {
    private String email;
    private String keyword;
    private LocalDateTime time;
}
