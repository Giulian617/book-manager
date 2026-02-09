package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.AuthorMapper;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.EditorMapper;
import com.unibuc.book_app.model.Author;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.Editor;
import com.unibuc.book_app.model.Translator;
import com.unibuc.book_app.repository.AuthorRepository;
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorServiceTests {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private BookAuthorEditorRepository bookAuthorEditorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private EditorMapper editorMapper;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllAuthors_Valid() {
        Author author1 = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        Author author2 = Author.builder()
                .firstName("Leo")
                .lastName("Tolstoy")
                .debutDate(LocalDate.of(1965, 7, 31))
                .build();
        AuthorResponseDto dto1 = new AuthorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));
        AuthorResponseDto dto2 = new AuthorResponseDto(2, "Leo", "Tolstoy", LocalDate.of(1965, 7, 31));

        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));
        when(authorMapper.toResponseDto(author1)).thenReturn(dto1);
        when(authorMapper.toResponseDto(author2)).thenReturn(dto2);

        List<AuthorResponseDto> result = authorService.findAllAuthors();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllBooksByAuthorId_Valid() {
        Book book1 = Book.builder()
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(
                        Translator.builder()
                                .firstName("John")
                                .lastName("Doe")
                                .build()
                )
                .build();
        Book book2 = Book.builder()
                .name("The Shadow of the Wind")
                .isbn("978-0143126393")
                .noPages(450)
                .price(15)
                .language("english")
                .publishDate(LocalDate.of(2023, 11, 5))
                .translator(
                        Translator.builder()
                                .firstName("Leo")
                                .lastName("Tolstoy")
                                .build()
                )
                .build();
        BookSummaryDto dto1 = new BookSummaryDto(1, "1984");
        BookSummaryDto dto2 = new BookSummaryDto(2, "Animal Farm");

        when(bookAuthorEditorRepository.findAllBooksByAuthorId(1)).thenReturn(List.of(book1, book2));
        when(bookMapper.toSummaryDto(book1)).thenReturn(dto1);
        when(bookMapper.toSummaryDto(book2)).thenReturn(dto2);

        List<BookSummaryDto> result = authorService.findAllBooksByAuthorId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllEditorsByAuthorId_Valid() {
        Editor editor1 = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        Editor editor2 = Editor.builder()
                .firstName("Leo")
                .lastName("Tolstoy")
                .debutDate(LocalDate.of(1965, 7, 31))
                .build();
        EditorSummaryDto dto1 = new EditorSummaryDto(1, "John", "Doe");
        EditorSummaryDto dto2 = new EditorSummaryDto(2, "Leo", "Tolstoy");

        when(bookAuthorEditorRepository.findAllEditorsByAuthorId(1)).thenReturn(List.of(editor1, editor2));
        when(editorMapper.toSummaryDto(editor1)).thenReturn(dto1);
        when(editorMapper.toSummaryDto(editor2)).thenReturn(dto2);

        List<EditorSummaryDto> result = authorService.findAllEditorsByAuthorId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAuthorEntityById_Valid() {
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        Author result = authorService.findAuthorEntityById(1);

        assertEquals(author, result);
    }

    @Test
    void testFindAuthorEntityById_Invalid() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authorService.findAuthorEntityById(1));

        assertEquals("Author with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindAuthorById_Valid() {
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        AuthorResponseDto dto = new AuthorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(authorMapper.toResponseDto(author)).thenReturn(dto);

        AuthorResponseDto result = authorService.findAuthorById(1);

        assertEquals(dto, result);
    }

    @Test
    void testCreateAuthor_Valid() {
        AuthorCreateDto createDto = new AuthorCreateDto("John", "Doe", LocalDate.of(1931, 2, 18));
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        Author savedAuthor = Author.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        AuthorResponseDto responseDto = new AuthorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));

        when(authorMapper.toEntity(createDto)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);
        when(authorMapper.toResponseDto(savedAuthor)).thenReturn(responseDto);

        AuthorResponseDto result = authorService.createAuthor(createDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateAuthor_Valid() {
        AuthorUpdateDto updateDto = new AuthorUpdateDto("John", "Doe", LocalDate.of(1931, 2, 18));
        Author author = Author.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        Author updatedAuthor = Author.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        AuthorResponseDto responseDto = new AuthorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        doNothing().when(authorMapper).updateEntityFromDto(updateDto, author);
        when(authorRepository.save(author)).thenReturn(updatedAuthor);
        when(authorMapper.toResponseDto(updatedAuthor)).thenReturn(responseDto);

        AuthorResponseDto result = authorService.updateAuthor(1, updateDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateAuthor_Invalid() {
        AuthorUpdateDto updateDto = new AuthorUpdateDto("John", "Doe", LocalDate.now());

        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authorService.updateAuthor(1, updateDto));

        assertEquals("Author with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteAuthor_Valid() {
        doNothing().when(authorRepository).deleteById(1);

        authorService.deleteAuthor(1);

        verify(authorRepository, times(1)).deleteById(1);
    }
}
