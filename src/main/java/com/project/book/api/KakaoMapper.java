package com.project.book.api;

import com.project.book.book.BookInfoDTO;
import com.project.book.book.BooksDTO;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KakaoMapper implements BookMapper{

    private final ModelMapper modelMapper;

    public KakaoMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.modelMapper.createTypeMap(KakaoBooksDTO.Document.class, BooksDTO.BookListItem.class).setConverter(getBookDocumentMapping());
        this.modelMapper.createTypeMap(KakaoBooksDTO.class, BooksDTO.class).setConverter(getBooksMapping());
        this.modelMapper.createTypeMap(KakaoBookInfoDTO.Document.class, BookInfoDTO.BookInfoItem.class).setConverter(getInfoDocumentMapping());

    }


    //start is custom....(need to page)
    @Override
    public BooksDTO mapBooks(Object source, Class<BooksDTO> target) {
        KakaoBooksDTO s = (KakaoBooksDTO) source;
        List<BooksDTO.BookListItem> items =
                s.getDocuments().stream().map(k -> {
                    BooksDTO.BookListItem i = modelMapper.map(k, BooksDTO.BookListItem.class);
                    String[] isbns = i.getIsbn().split(" ");
                    i.setIsbn(getIsbn(isbns));

                    return i;
                }).toList();

        BooksDTO books = modelMapper.map(s, target);

        books.setItems(items);

        return books;
    }

    @Override
    public BookInfoDTO mapInfo(Object source, Class<BookInfoDTO> target) {
        KakaoBookInfoDTO s = (KakaoBookInfoDTO) source;

        List<BookInfoDTO.BookInfoItem> items =
                s.getDocuments().stream().map(k -> {
                    BookInfoDTO.BookInfoItem i = modelMapper.map(k, BookInfoDTO.BookInfoItem.class);
                    String[] isbns = i.getIsbn().split(" ");
                    i.setIsbn(getIsbn(isbns));

                    return i;
                }).toList();

        BookInfoDTO bookInfo = new BookInfoDTO();
        bookInfo.setItems(items);

        return bookInfo;
    }

    private Converter<KakaoBooksDTO, BooksDTO> getBooksMapping() {
        Converter<KakaoBooksDTO, BooksDTO> converter = context -> {
            BooksDTO item = new BooksDTO();
            item.setDisplay(BookAPI.DISPLAY);
            item.setTotal(context.getSource().getMeta().getPageable_count());

            return item;
        };

        return converter;
    }

    private String getIsbn(String[] isbns){
        if(isbns.length == 2)
            return isbns[1];
        else if(isbns.length == 1)
            return isbns[0];

        return null;
    }

    private String getDate(String date){
        String[] dateSplit = date.split("-");
        dateSplit[2] = dateSplit[2].substring(0,2);

        return String.join("",dateSplit);
    }

    private Converter<KakaoBooksDTO.Document, BooksDTO.BookListItem> getBookDocumentMapping(){
        Converter<KakaoBooksDTO.Document, BooksDTO.BookListItem> converter = context -> {
            BooksDTO.BookListItem item = new BooksDTO.BookListItem();

            item.setPubdate(getDate(context.getSource().getDatetime()));
            item.setAuthor(context.getSource().getAuthors().get(0));
            item.setTitle(context.getSource().getTitle());
            item.setIsbn(context.getSource().getIsbn());
            item.setImage(context.getSource().getThumbnail());
            item.setPublisher(context.getSource().getPublisher());

            return item;
        };

        return converter;
    }

    private Converter<KakaoBookInfoDTO.Document, BookInfoDTO.BookInfoItem> getInfoDocumentMapping(){

        Converter<KakaoBookInfoDTO.Document, BookInfoDTO.BookInfoItem> converter = context -> {
            BookInfoDTO.BookInfoItem item = new BookInfoDTO.BookInfoItem();

            item.setPubdate(getDate(context.getSource().getDatetime()));
            item.setAuthor(context.getSource().getAuthors().get(0));
            item.setTitle(context.getSource().getTitle());
            item.setIsbn(context.getSource().getIsbn());
            item.setImage(context.getSource().getThumbnail());
            item.setPublisher(context.getSource().getPublisher());
            item.setDescription(context.getSource().getContents());
            item.setLink(context.getSource().getUrl());
            item.setDiscount(Integer.toString(context.getSource().getSale_price()));

            return item;
        };

        return converter;
    }


}
