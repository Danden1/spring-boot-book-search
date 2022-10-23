package com.project.book.Book;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankDTO {

    RankDTO(Rank rank){
        this.keyword = rank.getKeyword();
        this.count = rank.getCount();
    }

    @ApiModelProperty("키워드")
    private String keyword;

    @ApiModelProperty("검색 횟수")
    private Integer count;

}
