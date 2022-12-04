package com.project.book.api;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KakaoBooksDTO {

    private List<Document> documents;
    private Meta meta;

    public void setStart(int page, int display){
        meta.setStart(1 + (page-1) * display);
    }

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

    @Data
    static class Meta{
        private Integer pageable_count;

        //custom
        private Integer start;
    }
}
