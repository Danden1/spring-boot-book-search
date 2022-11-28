package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@NoArgsConstructor
@Service
public class KakaoBookAPI implements BookAPI{

    @Override
    public BookInfoDTO getBookInfo(String isbn) {
        return null;
    }

    @Override
    public BooksDTO getBooks(String keyword, int page) {
        return null;
    }
}
