package com.unibuc.book_app.repository;

import com.unibuc.book_app.model.Author;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookAuthorEditor;
import com.unibuc.book_app.model.BookAuthorEditor.BookAuthorEditorId;
import com.unibuc.book_app.model.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorEditorRepository extends JpaRepository<BookAuthorEditor, BookAuthorEditorId> {
    @Query("""
                SELECT DISTINCT bae.author
                FROM BookAuthorEditor bae
                WHERE bae.book.id = :bookId
            """)
    List<Author> findAllAuthorsByBookId(@Param("bookId") Integer bookId);

    @Query("""
                SELECT DISTINCT bae.editor
                FROM BookAuthorEditor bae
                WHERE bae.book.id = :bookId
            """)
    List<Editor> findAllEditorsByBookId(@Param("bookId") Integer bookId);

    @Query("""
                SELECT DISTINCT bae.book
                FROM BookAuthorEditor bae
                WHERE bae.author.id = :authorId
            """)
    List<Book> findAllBooksByAuthorId(@Param("authorId") Integer authorId);

    @Query("""
                SELECT DISTINCT bae.editor
                FROM BookAuthorEditor bae
                WHERE bae.author.id = :authorId
            """)
    List<Editor> findAllEditorsByAuthorId(@Param("authorId") Integer authorId);

    @Query("""
                SELECT DISTINCT bae.book
                FROM BookAuthorEditor bae
                WHERE bae.editor.id = :editorId
            """)
    List<Book> findAllBooksByEditorId(@Param("editorId") Integer editorId);

    @Query("""
                SELECT DISTINCT bae.author
                FROM BookAuthorEditor bae
                WHERE bae.editor.id = :editorId
            """)
    List<Author> findAllAuthorsByEditorId(@Param("editorId") Integer editorId);
}
