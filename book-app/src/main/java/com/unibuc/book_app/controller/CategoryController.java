package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.CategoryDto;
import com.unibuc.book_app.dto.CategorySummaryDto;
import com.unibuc.book_app.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of categories",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategorySummaryDto.class)
                    ))
    })
    public ResponseEntity<List<CategorySummaryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @GetMapping("/books/{categoryId}")
    @ResponseBody
    @Operation(
            summary = "Get all books by category",
            description = "Retrieve a list of all books for a specific category by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of books",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookSummaryDto.class)
                    ))
    })
    public ResponseEntity<List<BookSummaryDto>> findAllBooksByCategoryId(
            @PathVariable Integer categoryId
    ) {
        return ResponseEntity.ok(categoryService.findAllBooksByCategoryId(categoryId));
    }

    @GetMapping("/{categoryId}")
    @ResponseBody
    @Operation(
            summary = "Get category by ID",
            description = "Retrieve a single category by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategorySummaryDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Category with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<CategorySummaryDto> getCategoryById(
            @PathVariable("categoryId") Integer categoryId
    ) {
        return ResponseEntity.ok(categoryService.findCategoryById(categoryId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new category",
            description = "Create a new category with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Category successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategorySummaryDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid category data\" }")
                    ))
    })
    public ResponseEntity<CategorySummaryDto> createCategory(
            @RequestBody @Valid CategoryDto categoryDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
    }

    @PutMapping("/{categoryId}")
    @ResponseBody
    @Operation(
            summary = "Update a category",
            description = "Update an existing category by their ID with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategorySummaryDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid category data\" }")
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Category with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<CategorySummaryDto> updateCategory(
            @PathVariable("categoryId") Integer categoryId,
            @RequestBody @Valid CategoryDto categoryDto
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryId, categoryDto));
    }

    @DeleteMapping("/{categoryId}")
    @ResponseBody
    @Operation(
            summary = "Delete a category",
            description = "Delete a category by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Category successfully deleted"
            ),
    })
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("categoryId") Integer categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
