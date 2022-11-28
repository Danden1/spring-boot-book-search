package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NaverBookInfoDTO {

    private BookInfoItem[] items;

    @Data
    static class BookInfoItem {

        private String title;

        private String isbn;

        private String author;

        private String image;

        private String pubdate;


        private String publisher;


        private String discount;


        private String description;


        private String link;
    }
}
