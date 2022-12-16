package com.project.book.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class Producer {

    private final static String HISTORY = "history";
    private final static String RANK = "rank";

    private final KafkaTemplate<String, KeywordDTO> kafkaHistoryTemplate;
    private final KafkaTemplate<String, String> kafkaRankTemplate;

    @Async
    public void produce(String keyword, String email, LocalDateTime time){
        KeywordDTO keywordDTO = new KeywordDTO();
        int count = 0;

        keywordDTO.setKeyword(keyword);
        keywordDTO.setEmail(email);
        keywordDTO.setTime(time);

        if(keywordDTO.getEmail() != null){
            while(count < 5) {
                System.out.println("produce");
                try {
                    kafkaHistoryTemplate.send(HISTORY, keywordDTO);
                    break;
                } catch (Exception e) {
                    count++;
                }
            }
        }

        while(count < 5) {
            try {
                kafkaRankTemplate.send(RANK, keywordDTO.getKeyword());
                break;
            } catch (Exception e) {
                count++;
            }
        }
    }
}
