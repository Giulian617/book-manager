package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookPublisherDto;
import com.unibuc.book_app.dto.BookPublisherResponseDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookPublisherMapper;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookPublisher;
import com.unibuc.book_app.model.BookPublisher.BookPublisherId;
import com.unibuc.book_app.model.Publisher;
import com.unibuc.book_app.repository.BookPublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookPublisherService {
    private final BookPublisherRepository bookPublisherRepository;
    private final BookPublisherMapper bookPublisherMapper;
    private final BookService bookService;
    private final PublisherService publisherService;

    public List<BookPublisherResponseDto> findAllBookPublishers() {
        return bookPublisherRepository
                .findAll()
                .stream()
                .map(bookPublisherMapper::toResponseDto)
                .toList();
    }

    public BookPublisherResponseDto findBookPublisherById(Integer bookId, Integer publisherId) {
        BookPublisherId id = new BookPublisherId(bookId, publisherId);
        BookPublisher bookPublisher = bookPublisherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("BookPublisher with bookId=%d and publisherId=%d not found", bookId, publisherId)
                ));
        return bookPublisherMapper.toResponseDto(bookPublisher);
    }

    public BookPublisherResponseDto createBookPublisher(BookPublisherDto dto) {
        Book book = bookService.findBookEntityById(dto.getBookId());
        Publisher publisher = publisherService.findPublisherEntityById(dto.getPublisherId());
        BookPublisher bookPublisher = bookPublisherMapper.toEntity(book, publisher);

        return bookPublisherMapper.toResponseDto(bookPublisherRepository.save(bookPublisher));
    }

    public void deleteBookPublisher(Integer bookId, Integer publisherId) {
        bookPublisherRepository.deleteById(new BookPublisherId(bookId, publisherId));
    }
}
