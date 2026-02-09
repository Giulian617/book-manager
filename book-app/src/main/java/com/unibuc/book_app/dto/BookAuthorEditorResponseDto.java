package com.unibuc.book_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookAuthorEditorResponseDto {
    private BookSummaryDto book;
    private AuthorSummaryDto author;
    private EditorSummaryDto editor;
}
