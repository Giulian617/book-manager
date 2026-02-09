package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.BookCategoryResponseDto;
import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.CategorySummaryDto;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookCategory;
import com.unibuc.book_app.model.BookCategory.BookCategoryId;
import com.unibuc.book_app.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookCategoryMapper {
    public BookCategory toEntity(Book book, Category category) {
        BookCategoryId id = BookCategoryId.builder()
                .bookId(book.getId())
                .categoryId(category.getId())
                .build();

        return BookCategory.builder()
                .bookCategoryId(id)
                .book(book)
                .category(category)
                .build();
    }

    public BookCategoryResponseDto toResponseDto(BookCategory bookCategory) {
        BookSummaryDto bookSummaryDto = BookSummaryDto.builder()
                .id(bookCategory.getBook().getId())
                .name(bookCategory.getBook().getName())
                .build();

        CategorySummaryDto categorySummaryDto = CategorySummaryDto.builder()
                .id(bookCategory.getCategory().getId())
                .name(bookCategory.getCategory().getName())
                .build();

        return BookCategoryResponseDto.builder()
                .book(bookSummaryDto)
                .category(categorySummaryDto)
                .build();
    }
}