package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;
import com.project.book.exception.MyException;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@Service
public class KakaoBookAPI implements BookAPI{

    @Value("${kakao.book-api.Authorization}")
    private String authorization;


    private final String defaultUrl = "https://dapi.kakao.com/v3/search/book";

    private final RestTemplate restTemplate = new RestTemplate();

    private final KakaoMapper kakaoMapper;

    public KakaoBookAPI(KakaoMapper kakaoMapper){
        this.kakaoMapper = kakaoMapper;
    }



    @Override
    public BookInfoDTO getBookInfo(String isbn) {
        URI uri = UriComponentsBuilder
                .fromUriString(defaultUrl)
                .queryParam("target", "isbn")
                .queryParam("query", isbn)
                .encode().build().toUri();

        RequestEntity requestEntity = RequestEntity
                .get(uri)
                .header("Authorization", authorization)
                .build();

        try{
            KakaoBookInfoDTO kakaoBookInfoDTO = restTemplate.exchange(requestEntity, KakaoBookInfoDTO.class).getBody();
            return kakaoMapper.mapInfo(kakaoBookInfoDTO, BookInfoDTO.class);
        }
        //naver api error
        catch(HttpStatusCodeException e){
            throw new MyException(e.getMessage(), e.getStatusCode());
        }
    }

    @Override
    public BooksDTO getBooks(String keyword, int page) {

        URI uri = UriComponentsBuilder
                .fromUriString(defaultUrl)
                .queryParam("query", keyword)
                .queryParam("size", DISPLAY)
                .queryParam("page", page)
                .encode().build().toUri();

        RequestEntity requestEntity = RequestEntity
                .get(uri)
                .header("Authorization", authorization)
                .build();

        try{
            KakaoBooksDTO kakaoBooksDTO = restTemplate.exchange(requestEntity, KakaoBooksDTO.class).getBody();
            assert kakaoBooksDTO != null;
            kakaoBooksDTO.setStart(page, DISPLAY);
            System.out.println(kakaoBooksDTO.getMeta());
            System.out.println(kakaoBooksDTO.getDocuments());
            return kakaoMapper.mapBooks(kakaoBooksDTO, BooksDTO.class);
        }
        //naver api error
        catch(HttpStatusCodeException e){
            throw new MyException(e.getMessage(), e.getStatusCode());
        }
    }
}
