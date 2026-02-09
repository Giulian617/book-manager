package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.service.AuthorService;
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

@WebMvcTest(AuthorController.class)
class AuthorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllAuthors_Valid() throws Exception {
        List<AuthorResponseDto> authors = new ArrayList<>(List.of(
                new AuthorResponseDto(
                        1,
                        "John",
                        "Doe",
                        LocalDate.of(1931, 2, 18)
                ),
                new AuthorResponseDto(
                        2,
                        "Leo",
                        "Tolstoy",
                        LocalDate.of(1965, 7, 31)
                )
        ));

        when(authorService.findAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(authors.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(authors.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(authors.get(0).getLastName()))
                .andExpect(jsonPath("$[0].debutDate").value(authors.get(0).getDebutDate().toString()))
                .andExpect(jsonPath("$[1].id").value(authors.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(authors.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(authors.get(1).getLastName()))
                .andExpect(jsonPath("$[1].debutDate").value(authors.get(1).getDebutDate().toString()));
    }

    @Test
    void testGetAllBooksByAuthorId_Valid() throws Exception {
        List<BookSummaryDto> books = List.of(
                new BookSummaryDto(1, "1984"),
                new BookSummaryDto(2, "Animal Farm")
        );

        when(authorService.findAllBooksByAuthorId(1)).thenReturn(books);

        mockMvc.perform(get("/authors/books/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(books.get(1).getName()));
    }

    @Test
    void testGetAllEditorsByAuthorId_Valid() throws Exception {
        List<EditorSummaryDto> editors = List.of(
                new EditorSummaryDto(1, "John", "Doe"),
                new EditorSummaryDto(2, "Leo", "Tolstoy")
        );

        when(authorService.findAllEditorsByAuthorId(1)).thenReturn(editors);

        mockMvc.perform(get("/authors/editors/{authorId}", 1)
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
    void testGetAuthorById_Valid() throws Exception {
        AuthorResponseDto author = new AuthorResponseDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        when(authorService.findAuthorById(1)).thenReturn(author);

        mockMvc.perform(get("/authors/{authorId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(author.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(author.getLastName()))
                .andExpect(jsonPath("$.debutDate").value(author.getDebutDate().toString()));
    }

    @Test
    void testGetAuthorById_InvalidId() throws Exception {
        when(authorService.findAuthorById(-1))
                .thenThrow(new NotFoundException("Author with id -1 not found"));

        mockMvc.perform(get("/authors/{authorId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Author with id -1 not found"));
    }

    @Test
    void testCreateAuthor_Valid() throws Exception {
        AuthorCreateDto createDto = new AuthorCreateDto(
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        AuthorResponseDto responseDto = new AuthorResponseDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        when(authorService.createAuthor(createDto)).thenReturn(responseDto);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()))
                .andExpect(jsonPath("$.debutDate").value(responseDto.getDebutDate().toString()));
    }

    @Test
    void testCreateAuthor_InvalidBody() throws Exception {
        String invalidJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe"
                }
                """;

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("debutDate is required"));
    }

    @Test
    void testUpdateAuthor_Valid() throws Exception {
        AuthorUpdateDto updateDto = new AuthorUpdateDto(
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        AuthorResponseDto responseDto = new AuthorResponseDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        when(authorService.updateAuthor(1, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put("/authors/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()))
                .andExpect(jsonPath("$.debutDate").value(responseDto.getDebutDate().toString()));
    }

    @Test
    void testUpdateAuthor_InvalidId() throws Exception {
        AuthorUpdateDto updateDto = new AuthorUpdateDto(
                "John",
                "Doe",
                LocalDate.now()
        );

        when(authorService.updateAuthor(1, updateDto))
                .thenThrow(new NotFoundException("Author with id 1 not found"));

        mockMvc.perform(put("/authors/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Author with id 1 not found"));
    }

    @Test
    void testUpdateAuthor_InvalidBody() throws Exception {
        String malformedJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                """;

        mockMvc.perform(put("/authors/{authorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void testDeleteAuthor_Valid() throws Exception {
        doNothing().when(authorService).deleteAuthor(1);

        mockMvc.perform(delete("/authors/{authorId}", 1))
                .andExpect(status().isNoContent());
    }
}
