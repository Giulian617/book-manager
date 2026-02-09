package com.unibuc.book_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryResponseDto {
    private BookSummaryDto book;
    private CategorySummaryDto category;
}
