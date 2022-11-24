package com.project.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.book.Security.JwtTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Api("Book Search Controller")
public class BookSearchController {
    private final BookSearchService bookSearchService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BookAPIService bookAPIService;

    private final ModelMapper modelMapper;
    
    @GetMapping("/")
    public ResponseEntity<RankDTO> getMainPage(){
        List<Rank> rankList = bookSearchService.getRank();
        RankDTO rankDTO;

        SearchDTO searchDTO;


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
    public ResponseEntity<BookListDTO> searchBookList(HttpServletRequest request, @RequestParam int page, @RequestParam String keyword){
        String email = jwtTokenProvider.getAccessTokenEmail(jwtTokenProvider.resolveToken(request));
        BookListDTO bookListDTO = bookAPIService.searchBookList(keyword, page);

        if(email != null){
            bookSearchService.saveKeyword(keyword, email);
        }

        bookSearchService.saveRank(keyword);

        return new ResponseEntity<BookListDTO>(bookListDTO, HttpStatus.OK);
        
    }

    @GetMapping("/search/book")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BookInfoDTO> searchBookInfo(@RequestParam String isbn){
        BookInfoDTO bookInfoDTO = bookAPIService.searchBookInfo(isbn);

        return new ResponseEntity<BookInfoDTO>(bookInfoDTO, HttpStatus.OK);

    }

}
