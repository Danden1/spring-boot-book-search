package com.project.book.Book;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.book.Exception.MyException;
import com.project.book.Security.JwtTokenProvider;
import com.project.book.User.MyUser;
import com.project.book.User.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BookSearchService {
     
    final private HistoryRepository historyRepository;
    final private RankRepository rankRepository;
    final private UserRepository userRepository;
    final private JwtTokenProvider jwtTokenProvider;


    public void saveKeyword(String keyword, String token){
        MyUser user = userRepository.findByEmail(jwtTokenProvider.getEmail(token));
        History history;
        
        if(user == null){
            throw new MyException("not exist user", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        history = historyRepository.findByMyUser_userIdAndKeyword(user.getId(), keyword);
        
        if(history == null){
            history = new History();
            history.setKeyword(keyword);
            history.setSearchTime(LocalDateTime.now());
        }
        else{
            history.setSearchTime(LocalDateTime.now());
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

    
}
