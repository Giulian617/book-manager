package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.AuthorSummaryDto;
import com.unibuc.book_app.dto.BookAuthorEditorResponseDto;
import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.EditorSummaryDto;
import com.unibuc.book_app.model.Author;
import com.unibuc.book_app.model.Book;
import com.unibuc.book_app.model.BookAuthorEditor;
import com.unibuc.book_app.model.BookAuthorEditor.BookAuthorEditorId;
import com.unibuc.book_app.model.Editor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookAuthorEditorMapper {
    public BookAuthorEditor toEntity(Book book, Author author, Editor editor) {
        BookAuthorEditorId id = BookAuthorEditorId.builder()
                .bookId(book.getId())
                .authorId(author.getId())
                .editorId(editor.getId())
                .build();

        return BookAuthorEditor.builder()
                .bookAuthorEditorId(id)
                .book(book)
                .author(author)
                .editor(editor)
                .build();
    }

    public BookAuthorEditorResponseDto toResponseDto(BookAuthorEditor bookAuthorEditor) {
        BookSummaryDto bookSummaryDto = BookSummaryDto.builder()
                .id(bookAuthorEditor.getBook().getId())
                .name(bookAuthorEditor.getBook().getName())
                .build();

        AuthorSummaryDto authorSummaryDto = AuthorSummaryDto.builder()
                .id(bookAuthorEditor.getAuthor().getId())
                .firstName(bookAuthorEditor.getAuthor().getFirstName())
                .lastName(bookAuthorEditor.getAuthor().getLastName())
                .build();

        EditorSummaryDto editorSummaryDto = EditorSummaryDto.builder()
                .id(bookAuthorEditor.getEditor().getId())
                .firstName(bookAuthorEditor.getEditor().getFirstName())
                .lastName(bookAuthorEditor.getEditor().getLastName())
                .build();

        return BookAuthorEditorResponseDto.builder()
                .book(bookSummaryDto)
                .author(authorSummaryDto)
                .editor(editorSummaryDto)
                .build();
    }
}