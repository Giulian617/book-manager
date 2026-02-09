package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookAuthorEditorDto;
import com.unibuc.book_app.dto.BookAuthorEditorResponseDto;
import com.unibuc.book_app.service.BookAuthorEditorService;
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
@RequestMapping("book_author_editors")
@RequiredArgsConstructor
public class BookAuthorEditorController {
    private final BookAuthorEditorService bookAuthorEditorService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all book author editors",
            description = "Retrieve a list of all book-author-editor relationships in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of book author editors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookAuthorEditorResponseDto.class)
                    ))
    })
    public ResponseEntity<List<BookAuthorEditorResponseDto>> findAllBookAuthorEditors() {
        return ResponseEntity.ok(bookAuthorEditorService.findAllBookAuthorEditors());
    }

    @GetMapping("/{bookId}/{authorId}/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Get a book author editor by IDs",
            description = "Retrieve a single book-author-editor relationship by book ID and author ID and editor ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved book author editor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookAuthorEditorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "BookAuthorEditor not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"BookAuthorEditor with bookId=1 and authorId=1 and editorId=1 not found\" }"
                            )
                    ))
    })
    public ResponseEntity<BookAuthorEditorResponseDto> findBookAuthorEditorById(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("authorId") Integer authorId,
            @PathVariable("editorId") Integer editorId
    ) {
        return ResponseEntity.ok(bookAuthorEditorService.findBookAuthorEditorById(bookId, authorId, editorId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new book author editor",
            description = "Create a new book-author-editor relationship with the provided book and author and editor IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "BookAuthorEditor successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookAuthorEditorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Invalid book or author or editor ID\" }"
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book or Author not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Book with id 1 not found\" }"
                            )
                    ))
    })
    public ResponseEntity<BookAuthorEditorResponseDto> createBook(
            @RequestBody @Valid BookAuthorEditorDto bookAuthorEditorDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookAuthorEditorService.createBookAuthorEditor(bookAuthorEditorDto));
    }

    @DeleteMapping("/{bookId}/{authorId}/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Delete a book author editor",
            description = "Delete a book-author-editor relationship by book ID and author ID and editor ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "BookAuthorEditor successfully deleted"
            )
    })
    public ResponseEntity<Void> deleteBook(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("authorId") Integer authorId,
            @PathVariable("editorId") Integer editorId
    ) {
        bookAuthorEditorService.deleteBookAuthorEditor(bookId, authorId, editorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
