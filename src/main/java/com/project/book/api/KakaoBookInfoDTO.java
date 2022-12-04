package com.project.book.api;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KakaoBookInfoDTO {
    private List<KakaoBooksDTO.Document> documents;
    @Data
    static class Document{
        private String title;
        private List<String> authors;
        private String contents;
        private String datetime;
        private String isbn;
        private Integer sale_price;
        private String publisher;
        private String thumbnail;
    }
}
