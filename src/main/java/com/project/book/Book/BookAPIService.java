package com.project.book.Book;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.book.Exception.MyException;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Service
public class BookAPIService {

    @Value("${naver.book-api.client-id}")
    private String clientId;
    @Value("${naver.book-api.client-secret}")
    private String clientSecret;
    private int display = 10;

    private final RestTemplate restTemplate = new RestTemplate();



    public BookListDTO searchBookList(String keyword, int page){
        int start = 1 + (page-1) * this.display;    


        URI uri = UriComponentsBuilder
                    .fromUriString("https://openapi.naver.com")
                    .path("/v1/search/book.json")
                    .queryParam("query", keyword)
                    .queryParam("start", start)
                    .queryParam("display", this.display)
                    .encode().build().toUri();

        RequestEntity requestEntity = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", this.clientId)
            .header("X-Naver-Client-Secret", this.clientSecret)
            .build();
        
        try{
            return restTemplate.exchange(requestEntity, BookListDTO.class).getBody();
        }
        //naver api error
        catch(HttpStatusCodeException e){
            //or e.getREsponseBodyAsString()
            throw new MyException(e.getMessage(), e.getStatusCode());
        }

    }

    public BookInfoDTO searchBookInfo(String isbn){
        URI uri = UriComponentsBuilder
                    .fromUriString("https://openapi.naver.com")
                    .path("/v1/search/book_adv.json")
                    .queryParam("d_isbn", isbn)
                    .encode().build().toUri();
        
        RequestEntity requestEntity = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", this.clientId)
            .header("X-Naver-Client-Secret", this.clientSecret)
            .build();


        
        try{
            ResponseEntity<BookInfoDTO> bookInfoDto = restTemplate.exchange(requestEntity, BookInfoDTO.class);
            
            return bookInfoDto.getBody();
        }
        //naver api error
        catch(HttpStatusCodeException e){
            //or e.getREsponseBodyAsString()
            throw new MyException(e.getMessage(), e.getStatusCode());
        }
    }
    



    
}
