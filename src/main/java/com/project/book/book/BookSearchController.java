package com.project.book.book;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.book.api.BookAPI;
import com.project.book.api.NaverBookAPI;
import com.project.book.history.History;
import com.project.book.history.HistoryDTO;
import com.project.book.rank.Rank;
import com.project.book.rank.RankDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final NaverBookAPI bookAPIService;

    private final List<BookAPI> bookAPIs;

    @GetMapping("/")
    public ResponseEntity<RankDTO> getMainPage(){
        List<Rank> rankList = bookSearchService.getRank();
        RankDTO rankDTO;



        rankDTO = new RankDTO(rankList);

        return new ResponseEntity<RankDTO>(rankDTO, HttpStatus.OK);
    }

    @GetMapping("/search/history")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<HistoryDTO> getHistory(HttpServletRequest request){
        String email = jwtTokenProvider.getAccessTokenEmail(jwtTokenProvider.resolveToken(request));

        List<History> historyList = bookSearchService.getHistory(email);
        HistoryDTO historyDTO = new HistoryDTO(historyList);

        return new ResponseEntity<HistoryDTO>(historyDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BooksDTO> searchBookList(HttpServletRequest request, @RequestParam int page, @RequestParam String keyword){
        String email = jwtTokenProvider.getAccessTokenEmail(jwtTokenProvider.resolveToken(request));
        BooksDTO bookListDTO = bookAPIService.getBooks(keyword, page);

        if(email != null){
            bookSearchService.saveKeyword(keyword, email);
        }

        bookSearchService.saveRank(keyword);

        return new ResponseEntity<BooksDTO>(bookListDTO, HttpStatus.OK);
        
    }

    @GetMapping("/search/book")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BookInfoDTO> searchBookInfo(@RequestParam String isbn){
        BookInfoDTO bookInfoDTO = bookAPIService.getBookInfo(isbn);

        return new ResponseEntity<BookInfoDTO>(bookInfoDTO, HttpStatus.OK);

    }

}
