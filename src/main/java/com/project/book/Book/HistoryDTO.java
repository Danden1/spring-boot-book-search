package com.project.book.Book;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class HistoryDTO {


    HistoryDTO(History history){
        this.keyword = history.getKeyword();
        this.searchTime = history.getSearchTime();
    }

    @ApiModelProperty("키워드")
    private String keyword;


    @ApiModelProperty("시간")
    private LocalDateTime searchTime;


    public String getSearchTime(){
        return searchTime.toString();
    }

}
