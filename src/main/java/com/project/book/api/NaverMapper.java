package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class NaverMapper implements BookMapper{

    final ModelMapper modelMapper;

    public NaverMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }


    @Override
    public BooksDTO mapBooks(Object source, Class<BooksDTO> target) {

        return modelMapper.map((NaverBooksDTO)source, target);
    }

    @Override
    public BookInfoDTO mapInfo(Object source, Class<BookInfoDTO> target) {
        return modelMapper.map((NaverBookInfoDTO)source, target);
    }
}
