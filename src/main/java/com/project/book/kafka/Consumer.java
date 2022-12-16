package com.project.book.kafka;

import com.project.book.book.BookSearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Consumer {
    private final BookSearchService bookSearchService;
    private final static String HISTORY = "history";
    private final static String RANK = "rank";
    private final static String ID = "book";
    private final Producer producer;

    @Async
    @KafkaListener(
            topics = HISTORY,
            groupId = ID
    )
    public void consumeHistory(KeywordDTO keywordDTO){
        System.out.println("consumeHisotry");
        try {
            bookSearchService.saveKeyword(keywordDTO.getKeyword(), keywordDTO.getEmail(), keywordDTO.getTime());
        }catch (Exception e){
            producer.produce(keywordDTO.getKeyword(), keywordDTO.getEmail(), keywordDTO.getTime());
        }
    }

    @Async
    @KafkaListener(
            topics = RANK,
            groupId = ID
    )
    public void consumeRank(String rank){
        System.out.println("consumeRank");
        try {
            bookSearchService.saveRank(rank);
        }catch (Exception e){
            producer.produce(rank, null, null);
        }

    }
}
