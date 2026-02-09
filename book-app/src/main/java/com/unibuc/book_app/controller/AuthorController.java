package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.service.AuthorService;
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
@RequestMapping("authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all authors",
            description = "Retrieve a list of all authors in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of authors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponseDto.class)
                    ))
    })
    public ResponseEntity<List<AuthorResponseDto>> findAllAuthors() {
        return ResponseEntity.ok(authorService.findAllAuthors());
    }

    @GetMapping("/books/{authorId}")
    @ResponseBody
    @Operation(
            summary = "Get all books by author",
            description = "Retrieve a list of all books for a specific author by ID"
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
    public ResponseEntity<List<BookSummaryDto>> findAllBooksByAuthorId(
            @PathVariable Integer authorId
    ) {
        return ResponseEntity.ok(authorService.findAllBooksByAuthorId(authorId));
    }

    @GetMapping("/editors/{authorId}")
    @ResponseBody
    @Operation(
            summary = "Get all editors by author",
            description = "Retrieve a list of all editors for a specific author by ID"
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
    public ResponseEntity<List<EditorSummaryDto>> findAllEditorsByAuthorId(
            @PathVariable Integer authorId
    ) {
        return ResponseEntity.ok(authorService.findAllEditorsByAuthorId(authorId));
    }

    @GetMapping("/{authorId}")
    @ResponseBody
    @Operation(
            summary = "Get author by ID",
            description = "Retrieve a single author by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved author",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Author with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<AuthorResponseDto> findAuthorById(
            @PathVariable("authorId") Integer authorId
    ) {
        return ResponseEntity.ok(authorService.findAuthorById(authorId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new author",
            description = "Create a new author with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Author successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid author data\" }")
                    ))
    })
    public ResponseEntity<AuthorResponseDto> createAuthor(
            @RequestBody @Valid AuthorCreateDto authorCreateDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorCreateDto));
    }

    @PutMapping("/{authorId}")
    @ResponseBody
    @Operation(
            summary = "Update an author",
            description = "Update an existing author by their ID with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Author successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid author data\" }")
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Author with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<AuthorResponseDto> updateAuthor(
            @PathVariable("authorId") Integer authorId,
            @RequestBody @Valid AuthorUpdateDto authorUpdateDto
    ) {
        return ResponseEntity.ok(authorService.updateAuthor(authorId, authorUpdateDto));
    }

    @DeleteMapping("/{authorId}")
    @ResponseBody
    @Operation(
            summary = "Delete an author",
            description = "Delete an author by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Author successfully deleted"
            ),
    })
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable("authorId") Integer authorId
    ) {
        authorService.deleteAuthor(authorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
