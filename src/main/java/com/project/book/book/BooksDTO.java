package com.project.book.book;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BooksDTO {
    @ApiModelProperty("총 검색 결과 개수")
    private Integer total;

    @ApiModelProperty("검색 시작 위치")
    private Integer start;

    @ApiModelProperty("한 번에 표시할 검색 결과 개수")
    private Integer display;

    @ApiModelProperty("겸색 결과 목록")
    private BookListItem[] items;


    @Data
    static class BookListItem {
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
    }

}
