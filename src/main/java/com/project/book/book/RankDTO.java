package com.project.book.book;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RankDTO {

    @ApiModelProperty("인기 검색어")
    private List<RankItem> ranks;

    RankDTO(List<Rank> ranks){
        this.ranks = ranks.stream().map(RankItem::new).collect(Collectors.toList());
    }

    static class RankItem {
        RankItem(Rank rank){
            this.keyword = new String(rank.getKeyword());
            this.count = rank.getCount();
        }
        @ApiModelProperty("키워드")
        private final String keyword;

        @ApiModelProperty("검색 횟수")
        private final Integer count;

        public String getKeyword(){
            return keyword;
        }

        public Integer getCount(){
            return count;
        }
    }
}
