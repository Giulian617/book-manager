package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBooks_Valid() throws Exception {
        List<BookResponseDto> books = new ArrayList<>(List.of(
                new BookResponseDto(
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
                ),
                new BookResponseDto(
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
                )
        ));

        when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$[0].isbn").value(books.get(0).getIsbn()))
                .andExpect(jsonPath("$[0].noPages").value(books.get(0).getNoPages()))
                .andExpect(jsonPath("$[0].price").value(books.get(0).getPrice()))
                .andExpect(jsonPath("$[0].language").value(books.get(0).getLanguage()))
                .andExpect(jsonPath("$[0].publishDate").value(books.get(0).getPublishDate().toString()))
                .andExpect(jsonPath("$[0].translator.id").value(books.get(0).getTranslator().getId()))
                .andExpect(jsonPath("$[0].translator.firstName").value(books.get(0).getTranslator().getFirstName()))
                .andExpect(jsonPath("$[0].translator.lastName").value(books.get(0).getTranslator().getLastName()))
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(books.get(1).getName()))
                .andExpect(jsonPath("$[1].isbn").value(books.get(1).getIsbn()))
                .andExpect(jsonPath("$[1].noPages").value(books.get(1).getNoPages()))
                .andExpect(jsonPath("$[1].price").value(books.get(1).getPrice()))
                .andExpect(jsonPath("$[1].language").value(books.get(1).getLanguage()))
                .andExpect(jsonPath("$[1].publishDate").value(books.get(1).getPublishDate().toString()))
                .andExpect(jsonPath("$[1].translator.id").value(books.get(1).getTranslator().getId()))
                .andExpect(jsonPath("$[1].translator.firstName").value(books.get(1).getTranslator().getFirstName()))
                .andExpect(jsonPath("$[1].translator.lastName").value(books.get(1).getTranslator().getLastName()));
    }

    @Test
    void testGetAllAuthorsByBookId_Valid() throws Exception {
        List<AuthorSummaryDto> authors = List.of(
                new AuthorSummaryDto(1, "John", "Doe"),
                new AuthorSummaryDto(2, "Leo", "Tolstoy")
        );

        when(bookService.findAllAuthorsByBookId(1)).thenReturn(authors);

        mockMvc.perform(get("/books/authors/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(authors.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(authors.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(authors.get(0).getLastName()))
                .andExpect(jsonPath("$[1].id").value(authors.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(authors.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(authors.get(1).getLastName()));
    }

    @Test
    void testGetAllEditorsByBookId_Valid() throws Exception {
        List<EditorSummaryDto> editors = List.of(
                new EditorSummaryDto(1, "John", "Doe"),
                new EditorSummaryDto(2, "Leo", "Tolstoy")
        );

        when(bookService.findAllEditorsByBookId(1)).thenReturn(editors);

        mockMvc.perform(get("/books/editors/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(editors.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(editors.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(editors.get(0).getLastName()))
                .andExpect(jsonPath("$[1].id").value(editors.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(editors.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(editors.get(1).getLastName()));
    }

    @Test
    void testGetAllCategoriesByBookId_Valid() throws Exception {
        List<CategorySummaryDto> categories = List.of(
                new CategorySummaryDto(1, "Fantasy"),
                new CategorySummaryDto(2, "Horror")
        );

        when(bookService.findAllCategoriesByBookId(1)).thenReturn(categories);

        mockMvc.perform(get("/books/categories/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(categories.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(categories.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(categories.get(1).getName()));
    }

    @Test
    void testGetAllPublishersByBookId_Valid() throws Exception {
        List<PublisherSummaryDto> publishers = List.of(
                new PublisherSummaryDto(1, "Nemira"),
                new PublisherSummaryDto(2, "Sophia")
        );

        when(bookService.findAllPublishersByBookId(1)).thenReturn(publishers);

        mockMvc.perform(get("/books/publishers/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(publishers.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(publishers.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(publishers.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(publishers.get(1).getName()));
    }

    @Test
    void testGetBookById_Valid() throws Exception {
        BookResponseDto book = new BookResponseDto(
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

        when(bookService.findBookById(1)).thenReturn(book);

        mockMvc.perform(get("/books/{bookId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.name").value(book.getName()))
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.noPages").value(book.getNoPages()))
                .andExpect(jsonPath("$.price").value(book.getPrice()))
                .andExpect(jsonPath("$.language").value(book.getLanguage()))
                .andExpect(jsonPath("$.publishDate").value(book.getPublishDate().toString()))
                .andExpect(jsonPath("$.translator.id").value(book.getTranslator().getId()))
                .andExpect(jsonPath("$.translator.firstName").value(book.getTranslator().getFirstName()))
                .andExpect(jsonPath("$.translator.lastName").value(book.getTranslator().getLastName()));
    }

    @Test
    void testGetBookById_InvalidId() throws Exception {
        when(bookService.findBookById(-1))
                .thenThrow(new NotFoundException("Book with id -1 not found"));

        mockMvc.perform(get("/books/{bookId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book with id -1 not found"));
    }

    @Test
    void testCreateBook_Valid() throws Exception {
        BookCreateDto createDto = new BookCreateDto(
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                27,
                "romanian",
                LocalDate.of(2024, 3, 10),
                1
        );

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

        when(bookService.createBook(createDto)).thenReturn(responseDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.isbn").value(responseDto.getIsbn()))
                .andExpect(jsonPath("$.noPages").value(responseDto.getNoPages()))
                .andExpect(jsonPath("$.price").value(responseDto.getPrice()))
                .andExpect(jsonPath("$.language").value(responseDto.getLanguage()))
                .andExpect(jsonPath("$.publishDate").value(responseDto.getPublishDate().toString()))
                .andExpect(jsonPath("$.translator.id").value(responseDto.getTranslator().getId()))
                .andExpect(jsonPath("$.translator.firstName").value(responseDto.getTranslator().getFirstName()))
                .andExpect(jsonPath("$.translator.lastName").value(responseDto.getTranslator().getLastName()));
    }

    @Test
    void testCreateBook_InvalidBody() throws Exception {
        String invalidJson = """
                {
                  "name": "John",
                  "isbn": "973-9344-56-9",
                  "noPages": 302,
                  "price": 27,
                  "publishDate": "2024-03-10",
                  "translatorId": 1
                }
                """;

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("language is required and cannot be blank"));
    }

    @Test
    void testUpdateBook_Valid() throws Exception {
        BookUpdateDto updateDto = new BookUpdateDto(
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                23,
                "romanian",
                LocalDate.of(2024, 3, 10)
        );

        BookResponseDto responseDto = new BookResponseDto(
                1,
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                23,
                "romanian",
                LocalDate.of(2024, 3, 10),
                new TranslatorSummaryDto(
                        1,
                        "John",
                        "Doe"
                )
        );

        when(bookService.updateBook(1, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put("/books/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.isbn").value(responseDto.getIsbn()))
                .andExpect(jsonPath("$.noPages").value(responseDto.getNoPages()))
                .andExpect(jsonPath("$.price").value(responseDto.getPrice()))
                .andExpect(jsonPath("$.language").value(responseDto.getLanguage()))
                .andExpect(jsonPath("$.publishDate").value(responseDto.getPublishDate().toString()))
                .andExpect(jsonPath("$.translator.id").value(responseDto.getTranslator().getId()))
                .andExpect(jsonPath("$.translator.firstName").value(responseDto.getTranslator().getFirstName()))
                .andExpect(jsonPath("$.translator.lastName").value(responseDto.getTranslator().getLastName()));
    }

    @Test
    void testUpdateBook_InvalidId() throws Exception {
        BookUpdateDto updateDto = new BookUpdateDto(
                "Intre iadul deznadejdii si iadul smereniei",
                "973-9344-56-9",
                302,
                23,
                "romanian",
                LocalDate.now()
        );

        when(bookService.updateBook(1, updateDto))
                .thenThrow(new NotFoundException("Book with id 1 not found"));

        mockMvc.perform(put("/books/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Book with id 1 not found"));
    }

    @Test
    void testUpdateBook_InvalidBody() throws Exception {
        String malformedJson = """
                {
                  "name": "John",
                """;

        mockMvc.perform(put("/books/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void testDeleteBook_Valid() throws Exception {
        doNothing().when(bookService).deleteBook(1);

        mockMvc.perform(delete("/books/{bookId}", 1))
                .andExpect(status().isNoContent());
    }
}
