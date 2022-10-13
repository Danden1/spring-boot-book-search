package com.project.book.Book;

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
    public ResponseEntity getMainPage(){
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/search")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BookListDTO> searchBookList(HttpServletRequest request, @RequestParam int page, @RequestParam String keyword){
        String token = jwtTokenProvider.resolveToken(request);
        BookListDTO bookListDTO = bookAPIService.searchBookList(keyword, page);

        if(token != null){
            bookSearchService.saveKeyword(keyword, token);
        }

        bookSearchService.saveRank(keyword);

        return new ResponseEntity<BookListDTO>(bookListDTO, HttpStatus.OK);
        
    }

    @GetMapping("/search/book")
    @ApiImplicitParam(name = "X-Auth-Token", value = "Access Token", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<BookInfoDTO> searchBookInfo(@RequestParam String isbn){
        System.out.println(isbn);
        BookInfoDTO bookInfoDTO = bookAPIService.searchBookInfo(isbn);

        return new ResponseEntity<BookInfoDTO>(bookInfoDTO, HttpStatus.OK);

    }

}
