package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookMapper {
    public Book toEntity(BookCreateDto dto) {
        return Book.builder()
                .name(dto.getName())
                .isbn(dto.getIsbn())
                .noPages(dto.getNoPages())
                .price(dto.getPrice())
                .language(dto.getLanguage())
                .publishDate(dto.getPublishDate())
                .build();
    }

    public void updateEntityFromDto(BookUpdateDto dto, Book book) {
        Optional.ofNullable(dto.getName()).ifPresent(book::setName);
        Optional.ofNullable(dto.getIsbn()).ifPresent(book::setIsbn);
        Optional.ofNullable(dto.getNoPages()).ifPresent(book::setNoPages);
        Optional.ofNullable(dto.getPrice()).ifPresent(book::setPrice);
        Optional.ofNullable(dto.getLanguage()).ifPresent(book::setLanguage);
        Optional.ofNullable(dto.getPublishDate()).ifPresent(book::setPublishDate);
    }

    public BookSummaryDto toSummaryDto(Book book) {
        return BookSummaryDto.builder()
                .id(book.getId())
                .name(book.getName())
                .build();
    }

    public BookResponseDto toResponseDto(Book book) {
        TranslatorSummaryDto translatorDto = null;
        if (book.getTranslator() != null) {
            translatorDto = new TranslatorSummaryDto(
                    book.getTranslator().getId(),
                    book.getTranslator().getFirstName(),
                    book.getTranslator().getLastName()
            );
        }

        return BookResponseDto.builder()
                .id(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .noPages(book.getNoPages())
                .price(book.getPrice())
                .language(book.getLanguage())
                .publishDate(book.getPublishDate())
                .translator(translatorDto)
                .build();
    }
}