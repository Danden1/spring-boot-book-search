package com.project.book.book;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SearchDTO {

    SearchDTO(List<HistoryDTO> histories, List<RankDTO> ranks){
        this.histories = histories;
        this.ranks = ranks;
    }

    @ApiModelProperty("사용자 검색 목록")
    private List<HistoryDTO> histories;

    @ApiModelProperty("인기 검색어 목록")
    private List<RankDTO> ranks;
}
