package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.TranslatorCreateDto;
import com.unibuc.book_app.dto.TranslatorResponseDto;
import com.unibuc.book_app.dto.TranslatorUpdateDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.service.TranslatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TranslatorController.class)
class TranslatorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TranslatorService translatorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTranslators_Valid() throws Exception {
        List<TranslatorResponseDto> translators = new ArrayList<>(List.of(
                new TranslatorResponseDto(
                        1,
                        "John",
                        "Doe"
                ),
                new TranslatorResponseDto(
                        2,
                        "Leo",
                        "Tolstoy"
                )
        ));

        when(translatorService.findAllTranslators()).thenReturn(translators);

        mockMvc.perform(get("/translators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(translators.get(0).getId()))
                .andExpect(jsonPath("$[0].firstName").value(translators.get(0).getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(translators.get(0).getLastName()))
                .andExpect(jsonPath("$[1].id").value(translators.get(1).getId()))
                .andExpect(jsonPath("$[1].firstName").value(translators.get(1).getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(translators.get(1).getLastName()));
    }

    @Test
    void testGetAllBooksByTranslatorId_Valid() throws Exception {
        List<BookSummaryDto> books = List.of(
                new BookSummaryDto(1, "1984"),
                new BookSummaryDto(2, "Animal Farm")
        );

        when(translatorService.findAllBooksByTranslatorId(1)).thenReturn(books);

        mockMvc.perform(get("/translators/books/{translatorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(books.get(1).getName()));
    }

    @Test
    void testGetTranslatorById_Valid() throws Exception {
        TranslatorResponseDto translator = new TranslatorResponseDto(
                1,
                "John",
                "Doe"
        );

        when(translatorService.findTranslatorById(1)).thenReturn(translator);

        mockMvc.perform(get("/translators/{translatorId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(translator.getId()))
                .andExpect(jsonPath("$.firstName").value(translator.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(translator.getLastName()));
    }

    @Test
    void testGetTranslatorById_InvalidId() throws Exception {
        when(translatorService.findTranslatorById(-1))
                .thenThrow(new NotFoundException("Translator with id -1 not found"));

        mockMvc.perform(get("/translators/{translatorId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Translator with id -1 not found"));
    }

    @Test
    void testCreateTranslator_Valid() throws Exception {
        TranslatorCreateDto createDto = new TranslatorCreateDto(
                "John",
                "Doe"
        );

        TranslatorResponseDto responseDto = new TranslatorResponseDto(
                1,
                "John",
                "Doe"
        );

        when(translatorService.createTranslator(createDto)).thenReturn(responseDto);

        mockMvc.perform(post("/translators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()));
    }

    @Test
    void testCreateTranslator_InvalidBody() throws Exception {
        String invalidJson = """
                {
                  "firstName": "John",
                  "lastName": ""
                }
                """;

        mockMvc.perform(post("/translators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("lastName is required and cannot be blank"));
    }

    @Test
    void testUpdateTranslator_Valid() throws Exception {
        TranslatorUpdateDto updateDto = new TranslatorUpdateDto(
                "John",
                "Doe"
        );

        TranslatorResponseDto responseDto = new TranslatorResponseDto(
                1,
                "John",
                "Doe"
        );

        when(translatorService.updateTranslator(1, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put("/translators/{translatorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.firstName").value(responseDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(responseDto.getLastName()));
    }

    @Test
    void testUpdateTranslator_InvalidId() throws Exception {
        TranslatorUpdateDto updateDto = new TranslatorUpdateDto(
                "John",
                "Doe"
        );

        when(translatorService.updateTranslator(1, updateDto))
                .thenThrow(new NotFoundException("Translator with id 1 not found"));

        mockMvc.perform(put("/translators/{translatorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Translator with id 1 not found"));
    }

    @Test
    void testUpdateTranslator_InvalidBody() throws Exception {
        String malformedJson = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                """;

        mockMvc.perform(put("/translators/{translatorId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void testDeleteTranslator_Valid() throws Exception {
        doNothing().when(translatorService).deleteTranslator(1);

        mockMvc.perform(delete("/translators/{translatorId}", 1))
                .andExpect(status().isNoContent());
    }
}
