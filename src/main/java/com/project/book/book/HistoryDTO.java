package com.project.book.book;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class HistoryDTO {

    HistoryDTO(List<History> histories){
        this.histories = histories.stream().map(HistoryItem::new).collect(Collectors.toList());
    }

    @ApiModelProperty("사용자 검색 기록")
    private List<HistoryItem> histories;


    static class HistoryItem {

        HistoryItem(History history){
            keyword = history.getKeyword();
            searchTime = history.getSearchTime();
        }
        @ApiModelProperty("키워드")
        private final String keyword;


        @ApiModelProperty("시간")
        private final LocalDateTime searchTime;

        public String getSearchTime(){
            return searchTime.toString();
        }
        public String getKeyword(){ return keyword;}
    }



}
