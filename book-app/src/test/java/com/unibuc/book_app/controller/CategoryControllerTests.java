package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.CategoryDto;
import com.unibuc.book_app.dto.CategorySummaryDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.service.CategoryService;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCategorys_Valid() throws Exception {
        List<CategorySummaryDto> categories = new ArrayList<>(List.of(
                new CategorySummaryDto(
                        1,
                        "Fantasy"
                ),
                new CategorySummaryDto(
                        2,
                        "Horror"
                )
        ));

        when(categoryService.findAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(categories.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(categories.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(categories.get(1).getName()));
    }

    @Test
    void testGetAllBooksByCategoryId_Valid() throws Exception {
        List<BookSummaryDto> books = List.of(
                new BookSummaryDto(1, "1984"),
                new BookSummaryDto(2, "Animal Farm")
        );

        when(categoryService.findAllBooksByCategoryId(1)).thenReturn(books);

        mockMvc.perform(get("/categories/books/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(books.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(books.get(1).getName()));
    }

    @Test
    void testGetCategoryById_Valid() throws Exception {
        CategorySummaryDto category = new CategorySummaryDto(
                1,
                "Fantasy"
        );

        when(categoryService.findCategoryById(1)).thenReturn(category);

        mockMvc.perform(get("/categories/{categoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    void testGetCategoryById_InvalidId() throws Exception {
        when(categoryService.findCategoryById(-1))
                .thenThrow(new NotFoundException("Category with id -1 not found"));

        mockMvc.perform(get("/categories/{categoryId}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Category with id -1 not found"));
    }

    @Test
    void testCreateCategory_Valid() throws Exception {
        CategoryDto createDto = new CategoryDto("Fantasy");

        CategorySummaryDto responseDto = new CategorySummaryDto(
                1,
                "Fantasy"
        );

        when(categoryService.createCategory(createDto)).thenReturn(responseDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }

    @Test
    void testCreateCategory_InvalidBody() throws Exception {
        String invalidJson = """
                {
                  "name": ""
                }
                """;

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("name is required and cannot be blank"));
    }

    @Test
    void testUpdateCategory_Valid() throws Exception {
        CategoryDto updateDto = new CategoryDto("Fantasy");

        CategorySummaryDto responseDto = new CategorySummaryDto(
                1,
                "Fantasy"
        );

        when(categoryService.updateCategory(1, updateDto)).thenReturn(responseDto);

        mockMvc.perform(put("/categories/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.name").value(responseDto.getName()));
    }

    @Test
    void testUpdateCategory_InvalidId() throws Exception {
        CategoryDto updateDto = new CategoryDto("Fantasy");

        when(categoryService.updateCategory(1, updateDto))
                .thenThrow(new NotFoundException("Category with id 1 not found"));

        mockMvc.perform(put("/categories/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Category with id 1 not found"));
    }

    @Test
    void testUpdateCategory_InvalidBody() throws Exception {
        String malformedJson = """
                {
                  "name": "Fantasy"
                """;

        mockMvc.perform(put("/categories/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Malformed JSON request"));
    }

    @Test
    void testDeleteCategory_Valid() throws Exception {
        doNothing().when(categoryService).deleteCategory(1);

        mockMvc.perform(delete("/categories/{categoryId}", 1))
                .andExpect(status().isNoContent());
    }
}
