package com.unibuc.book_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private Integer id;
    private String name;
    private String isbn;
    private Integer noPages;
    private Integer price;
    private String language;
    private LocalDate publishDate;
    private TranslatorSummaryDto translator;
}