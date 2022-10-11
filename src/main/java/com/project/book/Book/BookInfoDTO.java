package com.project.book.Book;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookInfoDTO {

    @ApiModelProperty("겸색 결과")
    private Item1[] items;

    @Data
    static class Item1{
        @ApiModelProperty("제목")
        private String title;

        @ApiModelProperty("isbn")
        private String isbn;

        @ApiModelProperty("작가")
        private String author;

        @ApiModelProperty("이미지 링크")
        private String image;

        @ApiModelProperty("출판일")
        private String pubdate;

        @ApiModelProperty("출판사")
        private String publisher;

        @ApiModelProperty("가격")
        private String discount;

        @ApiModelProperty("책 소개")
        private String description;

        @ApiModelProperty("구매 링크")
        private String link;
    }
}
