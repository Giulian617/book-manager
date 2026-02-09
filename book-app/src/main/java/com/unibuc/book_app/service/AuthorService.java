package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.AuthorMapper;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.EditorMapper;
import com.unibuc.book_app.model.Author;
import com.unibuc.book_app.repository.AuthorRepository;
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookAuthorEditorRepository bookAuthorEditorRepository;
    private final BookMapper bookMapper;
    private final EditorMapper editorMapper;

    public List<AuthorResponseDto> findAllAuthors() {
        return authorRepository
                .findAll()
                .stream()
                .map(authorMapper::toResponseDto)
                .toList();
    }

    public List<BookSummaryDto> findAllBooksByAuthorId(Integer authorId) {
        return bookAuthorEditorRepository.findAllBooksByAuthorId(authorId)
                .stream()
                .map(bookMapper::toSummaryDto)
                .toList();
    }

    public List<EditorSummaryDto> findAllEditorsByAuthorId(Integer authorId) {
        return bookAuthorEditorRepository.findAllEditorsByAuthorId(authorId)
                .stream()
                .map(editorMapper::toSummaryDto)
                .toList();
    }

    public Author findAuthorEntityById(Integer authorId) {
        return authorRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException(String.format("Author with id %d not found", authorId))
        );
    }

    public AuthorResponseDto findAuthorById(Integer authorId) {
        return authorMapper.toResponseDto(authorRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException(String.format("Author with id %d not found", authorId))
        ));
    }

    public AuthorResponseDto createAuthor(AuthorCreateDto authorCreateDto) {
        Author author = authorMapper.toEntity(authorCreateDto);
        return authorMapper.toResponseDto(authorRepository.save(author));
    }

    public AuthorResponseDto updateAuthor(Integer authorId, AuthorUpdateDto authorUpdateDto) {
        Author author = authorRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException(String.format("Author with id %d not found", authorId))
        );
        authorMapper.updateEntityFromDto(authorUpdateDto, author);
        return authorMapper.toResponseDto(authorRepository.save(author));
    }

    public void deleteAuthor(Integer authorId) {
        authorRepository.deleteById(authorId);
    }
}
