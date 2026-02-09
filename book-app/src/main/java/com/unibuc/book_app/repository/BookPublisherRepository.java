package com.unibuc.book_app.repository;

import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookPublisher;
import com.unibuc.book_app.model.BookPublisher.BookPublisherId;
import com.unibuc.book_app.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookPublisherRepository extends JpaRepository<BookPublisher, BookPublisherId> {
    @Query("""
                SELECT DISTINCT bp.book
                FROM BookPublisher bp
                WHERE bp.publisher.id = :publisherId
            """)
    List<Book> findAllBooksByPublisherId(@Param("publisherId") Integer publisherId);

    @Query("""
                SELECT DISTINCT bp.publisher
                FROM BookPublisher bp
                WHERE bp.book.id = :bookId
            """)
    List<Publisher> findAllPublishersByBookId(@Param("bookId") Integer bookId);
}
