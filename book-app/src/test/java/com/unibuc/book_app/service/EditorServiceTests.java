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
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import com.unibuc.book_app.repository.EditorRepository;
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

class EditorServiceTests {

    @Mock
    private EditorRepository editorRepository;

    @Mock
    private EditorMapper editorMapper;

    @Mock
    private BookAuthorEditorRepository bookAuthorEditorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private EditorService editorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllEditors_Valid() {
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
        EditorResponseDto dto1 = new EditorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));
        EditorResponseDto dto2 = new EditorResponseDto(2, "Leo", "Tolstoy", LocalDate.of(1965, 7, 31));

        when(editorRepository.findAll()).thenReturn(List.of(editor1, editor2));
        when(editorMapper.toResponseDto(editor1)).thenReturn(dto1);
        when(editorMapper.toResponseDto(editor2)).thenReturn(dto2);

        List<EditorResponseDto> result = editorService.findAllEditors();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllBooksByEditorId_Valid() {
        Book book1 = Book.builder()
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(
                        Translator.builder()
                                .id(1)
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
                                .id(2)
                                .firstName("Leo")
                                .lastName("Tolstoy")
                                .build()
                )
                .build();
        BookSummaryDto dto1 = new BookSummaryDto(1, "1984");
        BookSummaryDto dto2 = new BookSummaryDto(2, "Animal Farm");

        when(bookAuthorEditorRepository.findAllBooksByEditorId(1)).thenReturn(List.of(book1, book2));
        when(bookMapper.toSummaryDto(book1)).thenReturn(dto1);
        when(bookMapper.toSummaryDto(book2)).thenReturn(dto2);

        List<BookSummaryDto> result = editorService.findAllBooksByEditorId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllAuthorsByEditorId_Valid() {
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
        AuthorSummaryDto dto1 = new AuthorSummaryDto(1, "John", "Doe");
        AuthorSummaryDto dto2 = new AuthorSummaryDto(2, "Leo", "Tolstoy");

        when(bookAuthorEditorRepository.findAllAuthorsByEditorId(1)).thenReturn(List.of(author1, author2));
        when(authorMapper.toSummaryDto(author1)).thenReturn(dto1);
        when(authorMapper.toSummaryDto(author2)).thenReturn(dto2);

        List<AuthorSummaryDto> result = editorService.findAllAuthorsByEditorId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindEditorEntityById_Valid() {
        Editor editor = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        when(editorRepository.findById(1)).thenReturn(Optional.of(editor));

        Editor result = editorService.findEditorEntityById(1);

        assertEquals(editor, result);
    }

    @Test
    void testFindEditorEntityById_Invalid() {
        when(editorRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> editorService.findEditorEntityById(1));

        assertEquals("Editor with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindEditorById_Valid() {
        Editor editor = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        EditorResponseDto dto = new EditorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));

        when(editorRepository.findById(1)).thenReturn(Optional.of(editor));
        when(editorMapper.toResponseDto(editor)).thenReturn(dto);

        EditorResponseDto result = editorService.findEditorById(1);

        assertEquals(dto, result);
    }

    @Test
    void testCreateEditor_Valid() {
        EditorCreateDto createDto = new EditorCreateDto("John", "Doe", LocalDate.of(1931, 2, 18));
        Editor editor = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        Editor savedEditor = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        EditorResponseDto responseDto = new EditorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));

        when(editorMapper.toEntity(createDto)).thenReturn(editor);
        when(editorRepository.save(editor)).thenReturn(savedEditor);
        when(editorMapper.toResponseDto(savedEditor)).thenReturn(responseDto);

        EditorResponseDto result = editorService.createEditor(createDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateEditor_Valid() {
        EditorUpdateDto updateDto = new EditorUpdateDto("John", "Doe", LocalDate.of(1931, 2, 18));
        Editor editor = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        Editor updatedEditor = Editor.builder()
                .firstName("John")
                .lastName("Doe")
                .debutDate(LocalDate.of(1931, 2, 18))
                .build();
        EditorResponseDto responseDto = new EditorResponseDto(1, "John", "Doe", LocalDate.of(1931, 2, 18));

        when(editorRepository.findById(1)).thenReturn(Optional.of(editor));
        doNothing().when(editorMapper).updateEntityFromDto(updateDto, editor);
        when(editorRepository.save(editor)).thenReturn(updatedEditor);
        when(editorMapper.toResponseDto(updatedEditor)).thenReturn(responseDto);

        EditorResponseDto result = editorService.updateEditor(1, updateDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateEditor_Invalid() {
        EditorUpdateDto updateDto = new EditorUpdateDto("John", "Doe", LocalDate.now());

        when(editorRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> editorService.updateEditor(1, updateDto));

        assertEquals("Editor with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteEditor_Valid() {
        doNothing().when(editorRepository).deleteById(1);

        editorService.deleteEditor(1);

        verify(editorRepository, times(1)).deleteById(1);
    }
}
