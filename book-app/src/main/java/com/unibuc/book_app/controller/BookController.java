package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.service.BookService;
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
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all books",
            description = "Retrieve a list of all books in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of books",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class)
                    ))
    })
    public ResponseEntity<List<BookResponseDto>> findAllBooks() {
        return ResponseEntity.ok(bookService.findAllBooks());
    }

    @GetMapping("/authors/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Get all authors by book",
            description = "Retrieve a list of all authors for a specific book by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of authors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorSummaryDto.class)
                    ))
    })
    public ResponseEntity<List<AuthorSummaryDto>> findAllAuthorsByBookId(
            @PathVariable Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findAllAuthorsByBookId(bookId));
    }

    @GetMapping("/editors/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Get all editors by book",
            description = "Retrieve a list of all editors for a specific book by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of editors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EditorSummaryDto.class)
                    ))
    })
    public ResponseEntity<List<EditorSummaryDto>> findAllEditorsByBookId(
            @PathVariable Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findAllEditorsByBookId(bookId));
    }

    @GetMapping("/categories/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Get all categories by book",
            description = "Retrieve a list of all categories for a specific book by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of categories",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategorySummaryDto.class)
                    ))
    })
    public ResponseEntity<List<CategorySummaryDto>> findAllCategoriesByBookId(
            @PathVariable Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findAllCategoriesByBookId(bookId));
    }

    @GetMapping("/publishers/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Get all publishers by book",
            description = "Retrieve a list of all publishers for a specific book by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of publishers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PublisherSummaryDto.class)
                    ))
    })
    public ResponseEntity<List<PublisherSummaryDto>> findAllPublishersByBookId(
            @PathVariable Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findAllPublishersByBookId(bookId));
    }

    @GetMapping("/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Get book by ID",
            description = "Retrieve a single book by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved book",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Book with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<BookResponseDto> findBookById(
            @PathVariable("bookId") Integer bookId
    ) {
        return ResponseEntity.ok(bookService.findBookById(bookId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new book",
            description = "Create a new book with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Book successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid book data\" }")
                    ))
    })
    public ResponseEntity<BookResponseDto> createBook(
            @RequestBody @Valid BookCreateDto bookCreateDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookCreateDto));
    }

    @PutMapping("/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Update a book",
            description = "Update an existing book by their ID with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Book successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid book data\" }")
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Book with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<BookResponseDto> updateBook(
            @PathVariable("bookId") Integer bookId,
            @RequestBody BookUpdateDto bookUpdateDto
    ) {
        return ResponseEntity.ok(bookService.updateBook(bookId, bookUpdateDto));
    }

    @DeleteMapping("/{bookId}")
    @ResponseBody
    @Operation(
            summary = "Delete a book",
            description = "Delete a book by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Book successfully deleted"
            ),
    })
    public ResponseEntity<Void> deleteBook(
            @PathVariable("bookId") Integer bookId
    ) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
