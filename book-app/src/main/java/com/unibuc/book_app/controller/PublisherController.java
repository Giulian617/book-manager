package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.PublisherCreateDto;
import com.unibuc.book_app.dto.PublisherResponseDto;
import com.unibuc.book_app.dto.PublisherUpdateDto;
import com.unibuc.book_app.service.PublisherService;
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
@RequestMapping("publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all publishers",
            description = "Retrieve a list of all publishers in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of publishers",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PublisherResponseDto.class)
                    ))
    })
    public ResponseEntity<List<PublisherResponseDto>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.findAllPublishers());
    }

    @GetMapping("/books/{publisherId}")
    @ResponseBody
    @Operation(
            summary = "Get all books by publisher",
            description = "Retrieve a list of all books for a specific publisher by ID"
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
    public ResponseEntity<List<BookSummaryDto>> findAllBooksByPublisherId(
            @PathVariable Integer publisherId
    ) {
        return ResponseEntity.ok(publisherService.findAllBooksByPublisherId(publisherId));
    }

    @GetMapping("/{publisherId}")
    @ResponseBody
    @Operation(
            summary = "Get publisher by ID",
            description = "Retrieve a single publisher by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved publisher",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PublisherResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Publisher not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Publisher with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<PublisherResponseDto> getPublisherById(
            @PathVariable("publisherId") Integer publisherId
    ) {
        return ResponseEntity.ok(publisherService.findPublisherById(publisherId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new publisher",
            description = "Create a new publisher with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Publisher successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PublisherResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid publisher data\" }")
                    ))
    })
    public ResponseEntity<PublisherResponseDto> createPublisher(
            @RequestBody @Valid PublisherCreateDto publisherCreateDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publisherService.createPublisher(publisherCreateDto));
    }

    @PutMapping("/{publisherId}")
    @ResponseBody
    @Operation(
            summary = "Update a publisher",
            description = "Update an existing publisher by their ID with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Publisher successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PublisherResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid publisher data\" }")
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Publisher not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Publisher with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<PublisherResponseDto> updatePublisher(
            @PathVariable("publisherId") Integer publisherId,
            @RequestBody @Valid PublisherUpdateDto publisherUpdateDto
    ) {
        return ResponseEntity.ok(publisherService.updatePublisher(publisherId, publisherUpdateDto));
    }

    @DeleteMapping("/{publisherId}")
    @ResponseBody
    @Operation(
            summary = "Delete a publisher",
            description = "Delete a publisher by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Publisher successfully deleted"
            ),
    })
    public ResponseEntity<Void> deletePublisher(
            @PathVariable("publisherId") Integer publisherId
    ) {
        publisherService.deletePublisher(publisherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
