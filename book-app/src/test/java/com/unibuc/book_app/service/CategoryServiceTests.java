package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.CategoryDto;
import com.unibuc.book_app.dto.CategorySummaryDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.CategoryMapper;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.Category;
import com.unibuc.book_app.model.Translator;
import com.unibuc.book_app.repository.BookCategoryRepository;
import com.unibuc.book_app.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllCategories_Valid() {
        Category category1 = Category.builder()
                .name("Fantasy")
                .build();
        Category category2 = Category.builder()
                .name("Horror")
                .build();
        CategorySummaryDto dto1 = new CategorySummaryDto(1, "Fantasy");
        CategorySummaryDto dto2 = new CategorySummaryDto(2, "Horror");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryMapper.toResponseDto(category1)).thenReturn(dto1);
        when(categoryMapper.toResponseDto(category2)).thenReturn(dto2);

        List<CategorySummaryDto> result = categoryService.findAllCategories();

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindAllBooksByCategoryId_Valid() {
        Book book1 = Book.builder()
                .name("Intre iadul deznadejdii si iadul smereniei")
                .isbn("973-9344-56-9")
                .noPages(302)
                .price(27)
                .language("romanian")
                .publishDate(LocalDate.of(2024, 3, 10))
                .translator(
                        Translator.builder()
                                .id(1)
                                .firstName("John")
                                .lastName("Doe")
                                .build()
                )
                .build();
        Book book2 = Book.builder()
                .name("The Shadow of the Wind")
                .isbn("978-0143126393")
                .noPages(450)
                .price(15)
                .language("english")
                .publishDate(LocalDate.of(2023, 11, 5))
                .translator(
                        Translator.builder()
                                .id(2)
                                .firstName("Leo")
                                .lastName("Tolstoy")
                                .build()
                )
                .build();
        BookSummaryDto dto1 = new BookSummaryDto(1, "1984");
        BookSummaryDto dto2 = new BookSummaryDto(2, "Animal Farm");

        when(bookCategoryRepository.findAllBooksByCategoryId(1)).thenReturn(List.of(book1, book2));
        when(bookMapper.toSummaryDto(book1)).thenReturn(dto1);
        when(bookMapper.toSummaryDto(book2)).thenReturn(dto2);

        List<BookSummaryDto> result = categoryService.findAllBooksByCategoryId(1);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void testFindCategoryEntityById_Valid() {
        Category category = Category.builder()
                .name("Fantasy")
                .build();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.findCategoryEntityById(1);

        assertEquals(category, result);
    }

    @Test
    void testFindCategoryEntityById_Invalid() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> categoryService.findCategoryEntityById(1));

        assertEquals("Category with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindCategoryById_Valid() {
        Category category = Category.builder()
                .name("Fantasy")
                .build();
        CategorySummaryDto dto = new CategorySummaryDto(1, "Fantasy");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(dto);

        CategorySummaryDto result = categoryService.findCategoryById(1);

        assertEquals(dto, result);
    }

    @Test
    void testCreateCategory_Valid() {
        CategoryDto createDto = new CategoryDto("Fantasy");
        Category category = Category.builder()
                .name("Fantasy")
                .build();
        Category savedCategory = Category.builder()
                .id(1)
                .name("Fantasy")
                .build();
        CategorySummaryDto summaryDto = new CategorySummaryDto(1, "Fantasy");

        when(categoryMapper.toEntity(createDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toResponseDto(savedCategory)).thenReturn(summaryDto);

        CategorySummaryDto result = categoryService.createCategory(createDto);

        assertEquals(summaryDto, result);
    }

    @Test
    void testUpdateCategory_Valid() {
        CategoryDto updateDto = new CategoryDto("Fantasy");
        Category category = Category.builder()
                .name("Fantasy")
                .build();
        Category updatedCategory = Category.builder()
                .id(1)
                .name("Fantasy")
                .build();
        CategorySummaryDto summaryDto = new CategorySummaryDto(1, "Fantasy");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateEntityFromDto(updateDto, category);
        when(categoryRepository.save(category)).thenReturn(updatedCategory);
        when(categoryMapper.toResponseDto(updatedCategory)).thenReturn(summaryDto);

        CategorySummaryDto result = categoryService.updateCategory(1, updateDto);

        assertEquals(summaryDto, result);
    }

    @Test
    void testUpdateCategory_Invalid() {
        CategoryDto updateDto = new CategoryDto("Fantasy");

        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> categoryService.updateCategory(1, updateDto));

        assertEquals("Category with id 1 not found", exception.getMessage());
    }

    @Test
    void testDeleteCategory_Valid() {
        doNothing().when(categoryRepository).deleteById(1);

        categoryService.deleteCategory(1);

        verify(categoryRepository, times(1)).deleteById(1);
    }
}
