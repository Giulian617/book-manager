package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookPublisherDto;
import com.unibuc.book_app.dto.BookPublisherResponseDto;
import com.unibuc.book_app.service.BookPublisherService;
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
@RequestMapping("book_publishers")
@RequiredArgsConstructor
public class BookPublisherController {
    private final BookPublisherService bookPublisherService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all book publishers",
            description = "Retrieve a list of all book-publisher relationships in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of book publishers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookPublisherResponseDto.class)
                    ))
    })
    public ResponseEntity<List<BookPublisherResponseDto>> findAllBookPublishers() {
        return ResponseEntity.ok(bookPublisherService.findAllBookPublishers());
    }

    @GetMapping("/{bookId}/{publisherId}")
    @ResponseBody
    @Operation(
            summary = "Get a book publisher by IDs",
            description = "Retrieve a single book-publisher relationship by book ID and publisher ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved book publisher",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookPublisherResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "BookPublisher not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"BookPublisher with bookId=1 and publisherId=1 not found\" }"
                            )
                    ))
    })
    public ResponseEntity<BookPublisherResponseDto> findBookPublisherById(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("publisherId") Integer publisherId
    ) {
        return ResponseEntity.ok(bookPublisherService.findBookPublisherById(bookId, publisherId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new book publisher",
            description = "Create a new book-publisher relationship with the provided book and publisher IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "BookPublisher successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookPublisherResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Invalid book or publisher ID\" }"
                            )
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Book or Publisher not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"error\": \"Book with id 1 not found\" }"
                            )
                    ))
    })
    public ResponseEntity<BookPublisherResponseDto> createBook(
            @RequestBody @Valid BookPublisherDto bookPublisherDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookPublisherService.createBookPublisher(bookPublisherDto));
    }

    @DeleteMapping("/{bookId}/{publisherId}")
    @ResponseBody
    @Operation(
            summary = "Delete a book publisher",
            description = "Delete a book-publisher relationship by book ID and publisher ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "BookPublisher successfully deleted"
            )
    })
    public ResponseEntity<Void> deleteBook(
            @PathVariable("bookId") Integer bookId,
            @PathVariable("publisherId") Integer publisherId
    ) {
        bookPublisherService.deleteBookPublisher(bookId, publisherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
