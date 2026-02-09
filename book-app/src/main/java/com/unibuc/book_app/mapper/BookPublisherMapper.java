package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.BookPublisherResponseDto;
import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.PublisherSummaryDto;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookPublisher;
import com.unibuc.book_app.model.BookPublisher.BookPublisherId;
import com.unibuc.book_app.model.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookPublisherMapper {
    public BookPublisher toEntity(Book book, Publisher publisher) {
        BookPublisherId id = BookPublisherId.builder()
                .bookId(book.getId())
                .publisherId(publisher.getId())
                .build();

        return BookPublisher.builder()
                .bookPublisherId(id)
                .book(book)
                .publisher(publisher)
                .build();
    }

    public BookPublisherResponseDto toResponseDto(BookPublisher bookPublisher) {
        BookSummaryDto bookSummaryDto = BookSummaryDto.builder()
                .id(bookPublisher.getBook().getId())
                .name(bookPublisher.getBook().getName())
                .build();

        PublisherSummaryDto publisherSummaryDto = PublisherSummaryDto.builder()
                .id(bookPublisher.getPublisher().getId())
                .name(bookPublisher.getPublisher().getName())
                .build();

        return BookPublisherResponseDto.builder()
                .book(bookSummaryDto)
                .publisher(publisherSummaryDto)
                .build();
    }
}