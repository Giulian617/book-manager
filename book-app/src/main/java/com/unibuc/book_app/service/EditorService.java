package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.*;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.AuthorMapper;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.EditorMapper;
import com.unibuc.book_app.model.Editor;
import com.unibuc.book_app.repository.BookAuthorEditorRepository;
import com.unibuc.book_app.repository.EditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepository editorRepository;
    private final EditorMapper editorMapper;
    private final BookAuthorEditorRepository bookAuthorEditorRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    public List<EditorResponseDto> findAllEditors() {
        return editorRepository
                .findAll()
                .stream()
                .map(editorMapper::toResponseDto)
                .toList();
    }

    public List<BookSummaryDto> findAllBooksByEditorId(Integer editorId) {
        return bookAuthorEditorRepository.findAllBooksByEditorId(editorId)
                .stream()
                .map(bookMapper::toSummaryDto)
                .toList();
    }

    public List<AuthorSummaryDto> findAllAuthorsByEditorId(Integer editorId) {
        return bookAuthorEditorRepository.findAllAuthorsByEditorId(editorId)
                .stream()
                .map(authorMapper::toSummaryDto)
                .toList();
    }

    public Editor findEditorEntityById(Integer editorId) {
        return editorRepository.findById(editorId).orElseThrow(
                () -> new NotFoundException(String.format("Editor with id %d not found", editorId))
        );
    }

    public EditorResponseDto findEditorById(Integer editorId) {
        return editorMapper.toResponseDto(editorRepository.findById(editorId).orElseThrow(
                () -> new NotFoundException(String.format("Editor with id %d not found", editorId))
        ));
    }

    public EditorResponseDto createEditor(EditorCreateDto editorCreateDto) {
        Editor editor = editorMapper.toEntity(editorCreateDto);
        return editorMapper.toResponseDto(editorRepository.save(editor));
    }

    public EditorResponseDto updateEditor(Integer editorId, EditorUpdateDto editorUpdateDto) {
        Editor editor = editorRepository.findById(editorId).orElseThrow(
                () -> new NotFoundException(String.format("Editor with id %d not found", editorId))
        );
        editorMapper.updateEntityFromDto(editorUpdateDto, editor);
        return editorMapper.toResponseDto(editorRepository.save(editor));
    }

    public void deleteEditor(Integer editorId) {
        editorRepository.deleteById(editorId);
    }
}
