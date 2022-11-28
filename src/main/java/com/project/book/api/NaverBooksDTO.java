package com.project.book.api;

import com.project.book.book.BooksDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NaverBooksDTO {

    private Integer total;


    private Integer start;


    private Integer display;


    private BookListItem[] items;


    @Data
    static class BookListItem {

        private String title;


        private String isbn;


        private String author;


        private String image;


        private String pubdate;


        private String publisher;
    }
}
