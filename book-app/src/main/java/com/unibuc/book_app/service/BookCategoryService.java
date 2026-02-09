package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookCategoryDto;
import com.unibuc.book_app.dto.BookCategoryResponseDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookCategoryMapper;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookCategory;
import com.unibuc.book_app.model.BookCategory.BookCategoryId;
import com.unibuc.book_app.model.Category;
import com.unibuc.book_app.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookCategoryMapper bookCategoryMapper;
    private final BookService bookService;
    private final CategoryService categoryService;

    public List<BookCategoryResponseDto> findAllBookCategories() {
        return bookCategoryRepository
                .findAll()
                .stream()
                .map(bookCategoryMapper::toResponseDto)
                .toList();
    }

    public BookCategoryResponseDto findBookCategoryById(Integer bookId, Integer categoryId) {
        BookCategoryId id = new BookCategoryId(bookId, categoryId);
        BookCategory bookCategory = bookCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("BookCategory with bookId=%d and categoryId=%d not found", bookId, categoryId)
                ));
        return bookCategoryMapper.toResponseDto(bookCategory);
    }

    public BookCategoryResponseDto createBookCategory(BookCategoryDto dto) {
        Book book = bookService.findBookEntityById(dto.getBookId());
        Category category = categoryService.findCategoryEntityById(dto.getCategoryId());
        BookCategory bookCategory = bookCategoryMapper.toEntity(book, category);

        return bookCategoryMapper.toResponseDto(bookCategoryRepository.save(bookCategory));
    }

    public void deleteBookCategory(Integer bookId, Integer categoryId) {
        bookCategoryRepository.deleteById(new BookCategoryId(bookId, categoryId));
    }
}
