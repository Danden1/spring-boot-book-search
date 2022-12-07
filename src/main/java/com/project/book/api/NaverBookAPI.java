package com.project.book.api;

import java.net.URI;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.book.exception.MyException;


@Component
@Order(1)
@Service
public class NaverBookAPI implements BookAPI {

    @Value("${naver.book-api.client-id}")
    private String clientId;
    @Value("${naver.book-api.client-secret}")
    private String clientSecret;

    private final String defaultUrl = "https://openapi.naver.com";

    private final RestTemplate restTemplate = new RestTemplate();

    private final NaverMapper naverMapper;

    public NaverBookAPI(NaverMapper naverMapper) {
        this.naverMapper = naverMapper;
    }



    @Override
    public BooksDTO getBooks(String keyword, int page){
        int start = 1 + (page-1) * DISPLAY;    

        URI uri = UriComponentsBuilder
                    .fromUriString(defaultUrl)
                    .path("/v1/search/book.json")
                    .queryParam("query", keyword)
                    .queryParam("start", start)
                    .queryParam("display", DISPLAY)
                    .encode().build().toUri();

        RequestEntity requestEntity = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", this.clientId)
            .header("X-Naver-Client-Secret", this.clientSecret)
            .build();
        
        try{
            NaverBooksDTO naverBooksDTO = restTemplate.exchange(requestEntity, NaverBooksDTO.class).getBody();
            return naverMapper.mapBooks(naverBooksDTO, BooksDTO.class);
        }
        //naver api error
        catch(HttpStatusCodeException e){
            throw new MyException(e.getMessage(), e.getStatusCode());
        }
    }

    @Override
    public BookInfoDTO getBookInfo(String isbn){
        URI uri = UriComponentsBuilder
                    .fromUriString(defaultUrl)
                    .path("/v1/search/book_adv.json")
                    .queryParam("d_isbn", isbn)
                    .encode().build().toUri();
        
        RequestEntity requestEntity = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", this.clientId)
            .header("X-Naver-Client-Secret", this.clientSecret)
            .build();

        try{
            NaverBookInfoDTO naverBookInfoDTO = restTemplate.exchange(requestEntity, NaverBookInfoDTO.class).getBody();
            return naverMapper.mapInfo(naverBookInfoDTO, BookInfoDTO.class);
        }
        //naver api error
        catch(HttpStatusCodeException e){
            throw new MyException(e.getMessage(), e.getStatusCode());
        }
    }
    
}
