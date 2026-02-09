package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.CategoryDto;
import com.unibuc.book_app.dto.CategorySummaryDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.CategoryMapper;
import com.unibuc.book_app.model.Category;
import com.unibuc.book_app.repository.BookCategoryRepository;
import com.unibuc.book_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookMapper bookMapper;

    public List<CategorySummaryDto> findAllCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }

    public List<BookSummaryDto> findAllBooksByCategoryId(Integer categoryId) {
        return bookCategoryRepository.findAllBooksByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toSummaryDto)
                .toList();
    }

    public Category findCategoryEntityById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %d not found", categoryId))
        );
    }

    public CategorySummaryDto findCategoryById(Integer categoryId) {
        return categoryMapper.toResponseDto(categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %d not found", categoryId))
        ));
    }

    public CategorySummaryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    public CategorySummaryDto updateCategory(Integer categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %d not found", categoryId))
        );
        categoryMapper.updateEntityFromDto(categoryDto, category);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    public void deleteCategory(Integer categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
