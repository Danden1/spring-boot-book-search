package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;

public interface BookAPI {

    public static final int DISPLAY = 10;

    public BookInfoDTO getBookInfo(String isbn);
    public BooksDTO getBooks(String keyword, int page);
}
