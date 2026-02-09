package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.TranslatorCreateDto;
import com.unibuc.book_app.dto.TranslatorResponseDto;
import com.unibuc.book_app.dto.TranslatorUpdateDto;
import com.unibuc.book_app.service.TranslatorService;
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
@RequestMapping("translators")
@RequiredArgsConstructor
public class TranslatorController {
    private final TranslatorService translatorService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all translators",
            description = "Retrieve a list of all translators in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of translators",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TranslatorResponseDto.class)
                    ))
    })
    public ResponseEntity<List<TranslatorResponseDto>> getAllTranslators() {
        return ResponseEntity.ok(translatorService.findAllTranslators());
    }

    @GetMapping("/books/{translatorId}")
    @ResponseBody
    @Operation(
            summary = "Get all books by translator",
            description = "Retrieve a list of all books for a specific translator by ID"
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
    public ResponseEntity<List<BookSummaryDto>> getAllBooksByTranslatorId(
            @PathVariable Integer translatorId
    ) {
        return ResponseEntity.ok(translatorService.findAllBooksByTranslatorId(translatorId));
    }

    @GetMapping("/{translatorId}")
    @ResponseBody
    @Operation(
            summary = "Get translator by ID",
            description = "Retrieve a single translator by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved translator",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TranslatorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Translator not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Translator with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<TranslatorResponseDto> getTranslatorById(
            @PathVariable("translatorId") Integer translatorId
    ) {
        return ResponseEntity.ok(translatorService.findTranslatorById(translatorId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new translator",
            description = "Create a new translator with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Translator successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TranslatorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid translator data\" }")
                    ))
    })
    public ResponseEntity<TranslatorResponseDto> createTranslator(
            @RequestBody @Valid TranslatorCreateDto translatorCreateDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(translatorService.createTranslator(translatorCreateDto));
    }

    @PutMapping("/{translatorId}")
    @ResponseBody
    @Operation(
            summary = "Update a translator",
            description = "Update an existing translator by their ID with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Translator successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TranslatorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid translator data\" }")
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Translator not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Translator with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<TranslatorResponseDto> updateTranslator(
            @PathVariable("translatorId") Integer translatorId,
            @RequestBody @Valid TranslatorUpdateDto translatorUpdateDto
    ) {
        return ResponseEntity.ok(translatorService.updateTranslator(translatorId, translatorUpdateDto));
    }

    @DeleteMapping("/{translatorId}")
    @ResponseBody
    @Operation(
            summary = "Delete a translator",
            description = "Delete a translator by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Translator successfully deleted"
            ),
    })
    public ResponseEntity<Void> deleteTranslator(
            @PathVariable("translatorId") Integer translatorId
    ) {
        translatorService.deleteTranslator(translatorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
