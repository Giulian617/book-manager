package com.unibuc.book_app.controller;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.service.EditorService;
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
@RequestMapping("editors")
@RequiredArgsConstructor
public class EditorController {
    private final EditorService editorService;

    @GetMapping("")
    @ResponseBody
    @Operation(
            summary = "Get all editors",
            description = "Retrieve a list of all editors in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of editors",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EditorResponseDto.class)
                    ))
    })
    public ResponseEntity<List<EditorResponseDto>> findAllEditors() {
        return ResponseEntity.ok(editorService.findAllEditors());
    }

    @GetMapping("/books/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Get all books by editor",
            description = "Retrieve a list of all books for a specific editor by ID"
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
    public ResponseEntity<List<BookSummaryDto>> findAllBooksByEditorId(
            @PathVariable Integer editorId
    ) {
        return ResponseEntity.ok(editorService.findAllBooksByEditorId(editorId));
    }

    @GetMapping("/authors/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Get all authors by editor",
            description = "Retrieve a list of all authors for a specific editor by ID"
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
    public ResponseEntity<List<AuthorSummaryDto>> findAllAuthorsByEditorId(
            @PathVariable Integer editorId
    ) {
        return ResponseEntity.ok(editorService.findAllAuthorsByEditorId(editorId));
    }

    @GetMapping("/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Get editor by ID",
            description = "Retrieve a single editor by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved editor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EditorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Editor not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Editor with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<EditorResponseDto> findEditorById(
            @PathVariable("editorId") Integer editorId
    ) {
        return ResponseEntity.ok(editorService.findEditorById(editorId));
    }

    @PostMapping("")
    @ResponseBody
    @Operation(
            summary = "Create a new editor",
            description = "Create a new editor with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Editor successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EditorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid editor data\" }")
                    ))
    })
    public ResponseEntity<EditorResponseDto> createEditor(
            @RequestBody @Valid EditorCreateDto editorCreateDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(editorService.createEditor(editorCreateDto));
    }

    @PutMapping("/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Update an editor",
            description = "Update an existing editor by their ID with new data"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Editor successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EditorResponseDto.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Invalid editor data\" }")
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Editor not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"error\": \"Editor with id 1 not found\" }")
                    ))
    })
    public ResponseEntity<EditorResponseDto> updateEditor(
            @PathVariable("editorId") Integer editorId,
            @RequestBody EditorUpdateDto editorUpdateDto
    ) {
        return ResponseEntity.ok(editorService.updateEditor(editorId, editorUpdateDto));
    }

    @DeleteMapping("/{editorId}")
    @ResponseBody
    @Operation(
            summary = "Delete an editor",
            description = "Delete an editor by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Editor successfully deleted"
            ),
    })
    public ResponseEntity<Void> deleteEditor(
            @PathVariable("editorId") Integer editorId
    ) {
        editorService.deleteEditor(editorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
