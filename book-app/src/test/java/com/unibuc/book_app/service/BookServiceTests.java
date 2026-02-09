package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.*;
import com.unibuc.book_app.model.*;
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import com.unibuc.book_app.repository.BookCategoryRepository;
import com.unibuc.book_app.repository.BookPublisherRepository;
import com.unibuc.book_app.repository.BookRepository;
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

class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookAuthorEditorRepository bookAuthorEditorRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private BookPublisherRepository bookPublisherRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private EditorMapper editorMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private PublisherMapper publisherMapper;

    @InjectMocks
    private BookService bookService;

    @Mock
    private TranslatorService translatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllBooks_Valid() {
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
        BookResponseDto dto1 = new BookResponseDto(
                1,
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10),
                new TranslatorSummaryDto(
                        1,
                        "John",
                        "Doe"
                )
        );
        BookResponseDto dto2 = new BookResponseDto(
                2,
                "The Shadow of the Wind",
                "978-0143126393",
                450,
                15,
                "english",
                LocalDate.of(2023, 11, 5),
                new TranslatorSummaryDto(
                        2,
                        "Leo",
                        "Tolstoy"
                )
        );

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        when(bookMapper.toResponseDto(book1)).thenReturn(dto1);
        when(bookMapper.toResponseDto(book2)).thenReturn(dto2);

        List<BookResponseDto> result = bookService.findAllBooks();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllAuthorsByBookId_Valid() {
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

        when(bookAuthorEditorRepository.findAllAuthorsByBookId(1)).thenReturn(List.of(author1, author2));
        when(authorMapper.toSummaryDto(author1)).thenReturn(dto1);
        when(authorMapper.toSummaryDto(author2)).thenReturn(dto2);

        List<AuthorSummaryDto> result = bookService.findAllAuthorsByBookId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllEditorsByBookId_Valid() {
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

        when(bookAuthorEditorRepository.findAllEditorsByBookId(1)).thenReturn(List.of(editor1, editor2));
        when(editorMapper.toSummaryDto(editor1)).thenReturn(dto1);
        when(editorMapper.toSummaryDto(editor2)).thenReturn(dto2);

        List<EditorSummaryDto> result = bookService.findAllEditorsByBookId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllCategoriesByBookId_Valid() {
        Category category1 = Category.builder()
                .name("Fantasy")
                .build();
        Category category2 = Category.builder()
                .name("Horror")
                .build();
        CategorySummaryDto dto1 = new CategorySummaryDto(1, "Fantasy");
        CategorySummaryDto dto2 = new CategorySummaryDto(2, "Horror");

        when(bookCategoryRepository.findAllCategoriesByBookId(1)).thenReturn(List.of(category1, category2));
        when(categoryMapper.toResponseDto(category1)).thenReturn(dto1);
        when(categoryMapper.toResponseDto(category2)).thenReturn(dto2);

        List<CategorySummaryDto> result = bookService.findAllCategoriesByBookId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllPublishersByBookId_Valid() {
        Publisher publisher1 = Publisher.builder()
                .name("Nemira")
                .foundedDate(LocalDate.of(1931, 2, 18))
                .build();
        Publisher publisher2 = Publisher.builder()
                .name("Sophia")
                .foundedDate(LocalDate.of(1965, 7, 31))
                .build();
        PublisherSummaryDto dto1 = new PublisherSummaryDto(1, "Nemira");
        PublisherSummaryDto dto2 = new PublisherSummaryDto(2, "Sophia");

        when(bookPublisherRepository.findAllPublishersByBookId(1)).thenReturn(List.of(publisher1, publisher2));
        when(publisherMapper.toSummaryDto(publisher1)).thenReturn(dto1);
        when(publisherMapper.toSummaryDto(publisher2)).thenReturn(dto2);

        List<PublisherSummaryDto> result = bookService.findAllPublishersByBookId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindBookEntityById_Valid() {
        Book book = Book.builder()
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
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookService.findBookEntityById(1);

        assertEquals(book, result);
    }

    @Test
    void testFindBookEntityById_Invalid() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookService.findBookEntityById(1));

        assertEquals("Book with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindBookById_Valid() {
        Book book = Book.builder()
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
        BookResponseDto dto = new BookResponseDto(
                1,
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10),
                new TranslatorSummaryDto(
                        1,
                        "John",
                        "Doe"
                )
        );

        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookMapper.toResponseDto(book)).thenReturn(dto);

        BookResponseDto result = bookService.findBookById(1);

        assertEquals(dto, result);
    }

    @Test
    void testCreateBook_Valid() {
        Translator translator = Translator.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .build();
        BookCreateDto createDto = new BookCreateDto(
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10),
                1
        );
        Book book = Book.builder()
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(translator)
                .build();
        Book savedBook = Book.builder()
                .id(1)
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(translator)
                .build();
        BookResponseDto responseDto = new BookResponseDto(
                1,
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10),
                new TranslatorSummaryDto(
                        1,
                        "John",
                        "Doe"
                )
        );

        when(translatorService.findTranslatorEntityById(1)).thenReturn(translator);
        when(bookMapper.toEntity(createDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toResponseDto(savedBook)).thenReturn(responseDto);

        BookResponseDto result = bookService.createBook(createDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateBook_Valid() {
        BookUpdateDto updateDto = new BookUpdateDto(
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10)
        );
        Book book = Book.builder()
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
        Book updatedBook = Book.builder()
                .id(1)
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
        BookResponseDto responseDto = new BookResponseDto(
                1,
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10),
                new TranslatorSummaryDto(
                        1,
                        "John",
                        "Doe"
                )
        );
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateEntityFromDto(updateDto, book);
        when(bookRepository.save(book)).thenReturn(updatedBook);
        when(bookMapper.toResponseDto(updatedBook)).thenReturn(responseDto);

        BookResponseDto result = bookService.updateBook(1, updateDto);

        assertEquals(responseDto, result);
    }

    @Test
    void testUpdateBook_Invalid() {
        BookUpdateDto updateDto = new BookUpdateDto(
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10)
        );

        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookService.updateBook(1, updateDto));

        assertEquals("Book with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteBook_Valid() {
        doNothing().when(bookRepository).deleteById(1);

        bookService.deleteBook(1);

        verify(bookRepository, times(1)).deleteById(1);
    }
}
