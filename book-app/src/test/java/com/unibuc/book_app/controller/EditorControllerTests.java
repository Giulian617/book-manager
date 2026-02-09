package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.service.EditorService;
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

@WebMvcTest(EditorController.class)
class EditorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EditorService editorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEditors_Valid() throws Exception {
        List<EditorResponseDto> editors = new ArrayList<>(List.of(
                new EditorResponseDto(
                        1,
                        "John",
                        "Doe",
                        LocalDate.of(1931, 2, 18)
                ),
                new EditorResponseDto(
                        2,
                        "Leo",
                        "Tolstoy",
                        LocalDate.of(1965, 7, 31)
                )
        ));

        when(editorService.findAllEditors()).thenReturn(editors);

        mockMvc.perform(get("/editors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(editors.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(editors.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(editors.get(0).getLastName()))
                .andExpect(jsonPath("$[0].debutDate").value(editors.get(0).getDebutDate().toString()))
                .andExpect(jsonPath("$[1].id").value(editors.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(editors.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(editors.get(1).getLastName()))
                .andExpect(jsonPath("$[1].debutDate").value(editors.get(1).getDebutDate().toString()));
    }

    @Test
    void testGetAllBooksByEditorId_Valid() throws Exception {
        List<BookSummaryDto> books = List.of(
                new BookSummaryDto(1, "1984"),
                new BookSummaryDto(2, "Animal Farm")
        );

        when(editorService.findAllBooksByEditorId(1)).thenReturn(books);

        mockMvc.perform(get("/editors/books/{editorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(books.get(1).getName()));
    }

    @Test
    void testGetAllAuthorsByEditorId_Valid() throws Exception {
        List<AuthorSummaryDto> authors = List.of(
                new AuthorSummaryDto(1, "John", "Doe"),
                new AuthorSummaryDto(2, "Leo", "Tolstoy")
        );

        when(editorService.findAllAuthorsByEditorId(1)).thenReturn(authors);

        mockMvc.perform(get("/editors/authors/{authorId}", 1)
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
    void testGetEditorById_Valid() throws Exception {
        EditorResponseDto editor = new EditorResponseDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        when(editorService.findEditorById(1)).thenReturn(editor);

        mockMvc.perform(get("/editors/{editorId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(editor.getId()))
                .andExpect(jsonPath("$.firstName").value(editor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(editor.getLastName()))
                .andExpect(jsonPath("$.debutDate").value(editor.getDebutDate().toString()));
    }

    @Test
    void testGetEditorById_InvalidId() throws Exception {
        when(editorService.findEditorById(-1))
                .thenThrow(new NotFoundException("Editor with id -1 not found"));

        mockMvc.perform(get("/editors/{editorId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Editor with id -1 not found"));
    }

    @Test
    void testCreateEditor_Valid() throws Exception {
        EditorCreateDto createDto = new EditorCreateDto(
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        EditorResponseDto responseDto = new EditorResponseDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        when(editorService.createEditor(createDto)).thenReturn(responseDto);

        mockMvc.perform(post("/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()))
                .andExpect(jsonPath("$.debutDate").value(responseDto.getDebutDate().toString()));
    }

    @Test
    void testCreateEditor_InvalidBody() throws Exception {
        String invalidJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe"
                }
                """;

        mockMvc.perform(post("/editors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("debutDate is required"));
    }

    @Test
    void testUpdateEditor_Valid() throws Exception {
        EditorUpdateDto updateDto = new EditorUpdateDto(
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        EditorResponseDto responseDto = new EditorResponseDto(
                1,
                "John",
                "Doe",
                LocalDate.of(1931, 2, 18)
        );

        when(editorService.updateEditor(1, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put("/editors/{editorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()))
                .andExpect(jsonPath("$.debutDate").value(responseDto.getDebutDate().toString()));
    }

    @Test
    void testUpdateEditor_InvalidId() throws Exception {
        EditorUpdateDto updateDto = new EditorUpdateDto(
                "John",
                "Doe",
                LocalDate.now()
        );

        when(editorService.updateEditor(1, updateDto))
                .thenThrow(new NotFoundException("Editor with id 1 not found"));

        mockMvc.perform(put("/editors/{editorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Editor with id 1 not found"));
    }

    @Test
    void testUpdateEditor_InvalidBody() throws Exception {
        String malformedJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                """;

        mockMvc.perform(put("/editors/{editorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void testDeleteEditor_Valid() throws Exception {
        doNothing().when(editorService).deleteEditor(1);

        mockMvc.perform(delete("/editors/{editorId}", 1))
                .andExpect(status().isNoContent());
    }
}
