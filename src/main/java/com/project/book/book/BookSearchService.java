package com.project.book.book;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.book.history.History;
import com.project.book.history.HistoryRepository;
import com.project.book.rank.Rank;
import com.project.book.rank.RankRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.book.exception.MyException;
import com.project.book.security.JwtTokenProvider;
import com.project.book.user.MyUser;
import com.project.book.user.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BookSearchService {
     
    final private HistoryRepository historyRepository;
    final private RankRepository rankRepository;
    final private UserRepository userRepository;
    
    @Value("${rank.keyword-number}")
    private int keywordNumber;


    public void saveKeyword(String keyword, String email, LocalDateTime time){
        MyUser user = userRepository.findByEmail(email);
        History history;
        
        if(user == null){
            throw new MyException("not exist user or Invalid token.", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        history = historyRepository.findByUserIdAndKeyword(user.getId(), keyword);
        
        if(history == null){
            history = new History();
            history.setKeyword(keyword);
            history.setSearchTime(time);
            history.setUser(user);
        }
        else{
            history.setSearchTime(time);
        }
        historyRepository.save(history);

    }

    public void saveRank(String keyword){
        Rank rank = rankRepository.findByKeyword(keyword);

        if(rank == null){
            rank = new Rank();
            rank.setKeyword(keyword);
            rank.setCount(1);
        }
        else{
            Integer count = rank.getCount();
            if(count + 1 > count){
                count +=1;
            }
            rank.setCount(count);

        }
        rankRepository.save(rank);
    }

    public List<Rank> getRank(){
        List<Rank> rankList = rankRepository.findByOrderByCountDesc();

        if(rankList != null && rankList.size() > this.keywordNumber){
            rankList = new ArrayList<>(rankList.subList(0, this.keywordNumber));
        }

        return rankList;
    }

    public List<History> getHistory(String email){
        MyUser user = userRepository.findByEmail(email);
        
        List<History> historyList = historyRepository.findByUserIdOrderBySearchTimeDesc(user.getId());
        
        if(historyList != null && historyList.size() > this.keywordNumber){
            historyList = new ArrayList<>(historyList.subList(0, this.keywordNumber));
        }
        
        return historyList;
    }

    
}
