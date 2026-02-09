package com.unibuc.book_app.repository;

import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookCategory;
import com.unibuc.book_app.model.BookCategory.BookCategoryId;
import com.unibuc.book_app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId> {
    @Query("""
                SELECT DISTINCT bc.book
                FROM BookCategory bc
                WHERE bc.category.id = :categoryId
            """)
    List<Book> findAllBooksByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("""
                SELECT DISTINCT bc.category
                FROM BookCategory bc
                WHERE bc.book.id = :bookId
            """)
    List<Category> findAllCategoriesByBookId(@Param("bookId") Integer bookId);
}
