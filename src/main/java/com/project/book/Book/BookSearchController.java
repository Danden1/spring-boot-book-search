package com.project.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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
@CrossOrigin(origins = "*")
@Api("Book Search Controller")
public class BookSearchController {
    final private BookSearchService bookSearchService;
    final private JwtTokenProvider jwtTokenProvider;
    final private BookAPIService bookAPIService;
    
    @GetMapping("/")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<SearchDTO> getMainPage(HttpServletRequest request){
        String email = jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request));

        List<History> historyList;
        List<Rank> rankList = bookSearchService.getRank();

        List<HistoryDTO> historyDTOList;
        List<RankDTO> rankDTOList;
        SearchDTO searchDTO;

        if(email == null){
            historyList = new ArrayList<>();
        }
        else{
            historyList = bookSearchService.getHistory(email);
        }



        historyDTOList = historyList.stream()
            .map(m-> new HistoryDTO(m))
            .collect(Collectors.toList());


        rankDTOList = rankList.stream()
            .map(m-> new RankDTO(m))
            .collect(Collectors.toList());

        searchDTO = new SearchDTO(historyDTOList, rankDTOList);
        
        
        return new ResponseEntity<SearchDTO>(searchDTO, HttpStatus.OK);
    }

    @GetMapping("/search")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BookListDTO> searchBookList(HttpServletRequest request, @RequestParam int page, @RequestParam String keyword){
        String email = jwtTokenProvider.getEmail(jwtTokenProvider.resolveToken(request));
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
