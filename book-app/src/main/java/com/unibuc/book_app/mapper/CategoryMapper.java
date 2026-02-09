package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.CategoryDto;
import com.unibuc.book_app.dto.CategorySummaryDto;
import com.unibuc.book_app.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    public Category toEntity(CategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public void updateEntityFromDto(CategoryDto dto, Category category) {
        Optional.ofNullable(dto.getName()).ifPresent(category::setName);
    }

    public CategorySummaryDto toResponseDto(Category category) {
        return CategorySummaryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}