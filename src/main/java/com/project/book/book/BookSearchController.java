package com.project.book.book;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.book.api.BookAPI;
import com.project.book.exception.MyException;
import com.project.book.history.History;
import com.project.book.history.HistoryDTO;
import com.project.book.kafka.Consumer;
import com.project.book.kafka.Producer;
import com.project.book.rank.Rank;
import com.project.book.rank.RankDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.book.security.JwtTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Api("Book Search Controller")
public class BookSearchController {
    private final BookSearchService bookSearchService;
    private final JwtTokenProvider jwtTokenProvider;

    private final List<BookAPI> bookAPIs;

    private final Producer producer;

    @GetMapping("/rank")
    public ResponseEntity<RankDTO> getMainPage(){
        List<Rank> rankList = bookSearchService.getRank();
        RankDTO rankDTO;

        rankDTO = new RankDTO(rankList);

        return new ResponseEntity<RankDTO>(rankDTO, HttpStatus.OK);
    }

    @GetMapping("/history")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<HistoryDTO> getHistory(HttpServletRequest request){
        String email = jwtTokenProvider.getAccessTokenEmail(jwtTokenProvider.resolveToken(request));

        List<History> historyList = bookSearchService.getHistory(email);
        HistoryDTO historyDTO = new HistoryDTO(historyList);

        return new ResponseEntity<HistoryDTO>(historyDTO, HttpStatus.OK);
    }

    @GetMapping("/books")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BooksDTO> searchBookList(HttpServletRequest request, @RequestParam int page, @RequestParam String keyword){
        String email = jwtTokenProvider.getAccessTokenEmail(jwtTokenProvider.resolveToken(request));
        BooksDTO bookListDTO = null;
        int max_count = bookAPIs.size();
        int count = 0;

        while(count < max_count) {
            try {
                 bookListDTO = bookAPIs.get(count).getBooks(keyword, page);

                 break;
            } catch (Exception e) {
                count += 1;
            }
        }
        if(count == max_count){
            throw new MyException("API Error!", HttpStatus.SERVICE_UNAVAILABLE);
        }

//        if(email != null){
//            bookSearchService.saveKeyword(keyword, email, LocalDateTime.now());
//        }
//        bookSearchService.saveRank(keyword);

        producer.produce(keyword, email, LocalDateTime.now());

        return new ResponseEntity<BooksDTO>(bookListDTO, HttpStatus.OK);
    }

    @GetMapping("/book")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BookInfoDTO> searchBookInfo(@RequestParam String isbn){
        int max_count = bookAPIs.size();
        int count = 0;

        while(count < max_count) {
            try {
                BookInfoDTO bookInfoDTO = bookAPIs.get(count).getBookInfo(isbn);

                return new ResponseEntity<BookInfoDTO>(bookInfoDTO, HttpStatus.OK);
            } catch (Exception e) {
                count += 1;
            }
        }

        throw new MyException("API Error!", HttpStatus.SERVICE_UNAVAILABLE);

    }

}
