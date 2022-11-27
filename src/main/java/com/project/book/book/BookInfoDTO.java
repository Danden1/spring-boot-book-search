package com.project.book.book;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BookInfoDTO {

    @ApiModelProperty("겸색 결과")
    private BookInfoItem[] items;

    @Data
    static class BookInfoItem {
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
