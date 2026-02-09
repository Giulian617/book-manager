package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.PublisherCreateDto;
import com.unibuc.book_app.dto.PublisherResponseDto;
import com.unibuc.book_app.dto.PublisherUpdateDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.service.PublisherService;
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

@WebMvcTest(PublisherController.class)
class PublisherControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllPublishers_Valid() throws Exception {
        List<PublisherResponseDto> publishers = new ArrayList<>(List.of(
                new PublisherResponseDto(
                        1,
                        "Nemira",
                        LocalDate.of(1931, 2, 18)
                ),
                new PublisherResponseDto(
                        2,
                        "Sophia",
                        LocalDate.of(1965, 7, 31)
                )
        ));

        when(publisherService.findAllPublishers()).thenReturn(publishers);

        mockMvc.perform(get("/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(publishers.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(publishers.get(0).getName()))
                .andExpect(jsonPath("$[0].foundedDate").value(publishers.get(0).getFoundedDate().toString()))
                .andExpect(jsonPath("$[1].id").value(publishers.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(publishers.get(1).getName()))
                .andExpect(jsonPath("$[1].foundedDate").value(publishers.get(1).getFoundedDate().toString()));
    }

    @Test
    void testGetAllBooksByPublisherId_Valid() throws Exception {
        List<BookSummaryDto> books = List.of(
                new BookSummaryDto(1, "1984"),
                new BookSummaryDto(2, "Animal Farm")
        );

        when(publisherService.findAllBooksByPublisherId(1)).thenReturn(books);

        mockMvc.perform(get("/publishers/books/{publisherId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(books.get(1).getName()));
    }

    @Test
    void testGetPublisherById_Valid() throws Exception {
        PublisherResponseDto publisher = new PublisherResponseDto(
                1,
                "Nemira",
                LocalDate.of(1931, 2, 18)
        );

        when(publisherService.findPublisherById(1)).thenReturn(publisher);

        mockMvc.perform(get("/publishers/{publisherId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(publisher.getId()))
                .andExpect(jsonPath("$.name").value(publisher.getName()))
                .andExpect(jsonPath("$.foundedDate").value(publisher.getFoundedDate().toString()));
    }

    @Test
    void testGetPublisherById_InvalidId() throws Exception {
        when(publisherService.findPublisherById(-1))
                .thenThrow(new NotFoundException("Publisher with id -1 not found"));

        mockMvc.perform(get("/publishers/{publisherId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Publisher with id -1 not found"));
    }

    @Test
    void testCreatePublisher_Valid() throws Exception {
        PublisherCreateDto createDto = new PublisherCreateDto(
                "Nemira",
                LocalDate.of(1931, 2, 18)
        );

        PublisherResponseDto responseDto = new PublisherResponseDto(
                1,
                "Nemira",
                LocalDate.of(1931, 2, 18)
        );

        when(publisherService.createPublisher(createDto)).thenReturn(responseDto);

        mockMvc.perform(post("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.foundedDate").value(responseDto.getFoundedDate().toString()));
    }

    @Test
    void testCreatePublisher_InvalidBody() throws Exception {
        String invalidJson = """
                {
                  "name": "Nemira"
                }
                """;

        mockMvc.perform(post("/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("foundedDate is required"));
    }

    @Test
    void testUpdatePublisher_Valid() throws Exception {
        PublisherUpdateDto updateDto = new PublisherUpdateDto(
                "Nemira",
                LocalDate.of(1931, 2, 18)
        );

        PublisherResponseDto responseDto = new PublisherResponseDto(
                1,
                "Nemira",
                LocalDate.of(1931, 2, 18)
        );

        when(publisherService.updatePublisher(1, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put("/publishers/{publisherId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.foundedDate").value(responseDto.getFoundedDate().toString()));
    }

    @Test
    void testUpdatePublisher_InvalidId() throws Exception {
        PublisherUpdateDto updateDto = new PublisherUpdateDto(
                "Nemira",
                LocalDate.now()
        );

        when(publisherService.updatePublisher(1, updateDto))
                .thenThrow(new NotFoundException("Publisher with id 1 not found"));

        mockMvc.perform(put("/publishers/{publisherId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Publisher with id 1 not found"));
    }

    @Test
    void testUpdatePublisher_InvalidBody() throws Exception {
        String malformedJson = """
                {
                  "name": "Nemira",
                """;

        mockMvc.perform(put("/publishers/{publisherId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void testDeletePublisher_Valid() throws Exception {
        doNothing().when(publisherService).deletePublisher(1);

        mockMvc.perform(delete("/publishers/{publisherId}", 1))
                .andExpect(status().isNoContent());
    }
}
