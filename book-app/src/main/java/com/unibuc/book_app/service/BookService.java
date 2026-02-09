package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.*;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.Translator;
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import com.unibuc.book_app.repository.BookCategoryRepository;
import com.unibuc.book_app.repository.BookPublisherRepository;
import com.unibuc.book_app.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final TranslatorService translatorService;
    private final BookAuthorEditorRepository bookAuthorEditorRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookPublisherRepository bookPublisherRepository;
    private final AuthorMapper authorMapper;
    private final EditorMapper editorMapper;
    private final CategoryMapper categoryMapper;
    private final PublisherMapper publisherMapper;

    public List<BookResponseDto> findAllBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toResponseDto)
                .toList();
    }

    public List<AuthorSummaryDto> findAllAuthorsByBookId(Integer bookId) {
        return bookAuthorEditorRepository.findAllAuthorsByBookId(bookId)
                .stream()
                .map(authorMapper::toSummaryDto)
                .toList();
    }

    public List<EditorSummaryDto> findAllEditorsByBookId(Integer bookId) {
        return bookAuthorEditorRepository.findAllEditorsByBookId(bookId)
                .stream()
                .map(editorMapper::toSummaryDto)
                .toList();
    }

    public List<CategorySummaryDto> findAllCategoriesByBookId(Integer bookId) {
        return bookCategoryRepository.findAllCategoriesByBookId(bookId)
                .stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    public List<PublisherSummaryDto> findAllPublishersByBookId(Integer bookId) {
        return bookPublisherRepository.findAllPublishersByBookId(bookId)
                .stream()
                .map(publisherMapper::toSummaryDto)
                .toList();
    }

    public Book findBookEntityById(Integer bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException(String.format("Book with id %d not found", bookId))
        );
    }

    public BookResponseDto findBookById(Integer bookId) {
        return bookMapper.toResponseDto(bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException(String.format("Book with id %d not found", bookId))
        ));
    }

    public BookResponseDto createBook(BookCreateDto bookCreateDto) {
        Book book = bookMapper.toEntity(bookCreateDto);

        if (bookCreateDto.getTranslatorId() != null) {
            Translator translator = translatorService.findTranslatorEntityById(bookCreateDto.getTranslatorId());
            book.setTranslator(translator);
        }

        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    public BookResponseDto updateBook(Integer bookId, BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NotFoundException(String.format("Book with id %d not found", bookId))
        );
        bookMapper.updateEntityFromDto(bookUpdateDto, book);
        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    public void deleteBook(Integer bookId) {
        bookRepository.deleteById(bookId);
    }
}
