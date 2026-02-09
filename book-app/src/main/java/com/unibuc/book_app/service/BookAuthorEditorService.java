package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookAuthorEditorDto;
import com.unibuc.book_app.dto.BookAuthorEditorResponseDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookAuthorEditorMapper;
import com.unibuc.book_app.model.Author;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookAuthorEditor;
import com.unibuc.book_app.model.BookAuthorEditor.BookAuthorEditorId;
import com.unibuc.book_app.model.Editor;
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookAuthorEditorService {
    private final BookAuthorEditorRepository bookAuthorEditorRepository;
    private final BookAuthorEditorMapper bookAuthorEditorMapper;
    private final BookService bookService;
    private final AuthorService authorService;
    private final EditorService editorService;

    public List<BookAuthorEditorResponseDto> findAllBookAuthorEditors() {
        return bookAuthorEditorRepository
                .findAll()
                .stream()
                .map(bookAuthorEditorMapper::toResponseDto)
                .toList();
    }

    public BookAuthorEditorResponseDto findBookAuthorEditorById(Integer bookId, Integer authorId, Integer editorId) {
        BookAuthorEditorId id = new BookAuthorEditorId(bookId, authorId, editorId);
        BookAuthorEditor bookAuthorEditor = bookAuthorEditorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("BookAuthorEditor with bookId=%d and authorId=%d and editorId=%d not found", bookId, authorId, editorId)
                ));
        return bookAuthorEditorMapper.toResponseDto(bookAuthorEditor);
    }

    public BookAuthorEditorResponseDto createBookAuthorEditor(BookAuthorEditorDto dto) {
        Book book = bookService.findBookEntityById(dto.getBookId());
        Author author = authorService.findAuthorEntityById(dto.getAuthorId());
        Editor editor = editorService.findEditorEntityById(dto.getEditorId());
        BookAuthorEditor bookAuthorEditor = bookAuthorEditorMapper.toEntity(book, author, editor);

        return bookAuthorEditorMapper.toResponseDto(bookAuthorEditorRepository.save(bookAuthorEditor));
    }

    public void deleteBookAuthorEditor(Integer bookId, Integer authorId, Integer editorId) {
        bookAuthorEditorRepository.deleteById(new BookAuthorEditorId(bookId, authorId, editorId));
    }
}
