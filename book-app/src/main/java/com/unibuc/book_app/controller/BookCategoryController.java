package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookCategoryDto;
import com.unibuc.book_app.dto.BookCategoryResponseDto;
import com.unibuc.book_app.service.BookCategoryService;
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
@RequestMapping("book_categories")
@RequiredArgsConstructor
public class BookCategoryController {
    private final BookCategoryService bookCategoryService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all book categories",
            description = "Retrieve a list of all book-category relationships in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of book categories",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookCategoryResponseDto.class)
                    ))
    })
    public ResponseEntity<List<BookCategoryResponseDto>> findAllBookCategories() {
        return ResponseEntity.ok(bookCategoryService.findAllBookCategories());
    }

    @GetMapping("/{bookId}/{categoryId}")
    @ResponseBody
    @Operation(
            summary = "Get a book category by IDs",
            description = "Retrieve a single book-category relationship by book ID and category ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved book category",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookCategoryResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "BookCategory not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"BookCategory with bookId=1 and categoryId=1 not found\" }"
                            )
                    ))
    })
    public ResponseEntity<BookCategoryResponseDto> findBookCategoryById(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("categoryId") Integer categoryId
    ) {
        return ResponseEntity.ok(bookCategoryService.findBookCategoryById(bookId, categoryId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new book category",
            description = "Create a new book-category relationship with the provided book and category IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "BookCategory successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookCategoryResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Invalid book or category ID\" }"
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book or Category not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Book with id 1 not found\" }"
                            )
                    ))
    })
    public ResponseEntity<BookCategoryResponseDto> createBook(
            @RequestBody @Valid BookCategoryDto bookCategoryDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCategoryService.createBookCategory(bookCategoryDto));
    }

    @DeleteMapping("/{bookId}/{categoryId}")
    @ResponseBody
    @Operation(
            summary = "Delete a book category",
            description = "Delete a book-category relationship by book ID and category ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "BookCategory successfully deleted"
            )
    })
    public ResponseEntity<Void> deleteBook(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("categoryId") Integer categoryId
    ) {
        bookCategoryService.deleteBookCategory(bookId, categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
