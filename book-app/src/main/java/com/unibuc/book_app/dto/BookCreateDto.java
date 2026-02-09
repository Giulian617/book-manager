package com.unibuc.book_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateDto {
    @NotBlank(message = "name is required and cannot be blank")
    private String name;

    @NotBlank(message = "isbn is required and cannot be blank")
    private String isbn;

    @Min(value = 0, message = "noPages cannot be less than 0")
    private Integer noPages;

    @Min(value = 0, message = "price cannot be less than 0")
    private Integer price;

    @NotBlank(message = "language is required and cannot be blank")
    private String language;

    @NotNull(message = "publishDate is required")
    @PastOrPresent(message = "publishDate must be in the past or present")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    private Integer translatorId;
}