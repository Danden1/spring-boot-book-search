package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;

import java.util.List;

public interface BookMapper {
    public BooksDTO mapBooks(Object source, Class<BooksDTO> target);
    public BookInfoDTO mapInfo(Object source, Class<BookInfoDTO> target);
}
