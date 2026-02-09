package com.unibuc.book_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDto {
    private String name;
    private String isbn;
    private Integer noPages;
    private Integer price;
    private String language;
    private LocalDate publishDate;
}