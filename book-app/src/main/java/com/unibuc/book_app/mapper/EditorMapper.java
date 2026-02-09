package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.EditorCreateDto;
import com.unibuc.book_app.dto.EditorResponseDto;
import com.unibuc.book_app.dto.EditorSummaryDto;
import com.unibuc.book_app.dto.EditorUpdateDto;
import com.unibuc.book_app.model.Editor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EditorMapper {
    public Editor toEntity(EditorCreateDto dto) {
        return Editor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .debutDate(dto.getDebutDate())
                .build();
    }

    public void updateEntityFromDto(EditorUpdateDto dto, Editor editor) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(editor::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(editor::setLastName);
        Optional.ofNullable(dto.getDebutDate()).ifPresent(editor::setDebutDate);
    }

    public EditorSummaryDto toSummaryDto(Editor editor) {
        return EditorSummaryDto.builder()
                .id(editor.getId())
                .firstName(editor.getFirstName())
                .lastName(editor.getLastName())
                .build();
    }

    public EditorResponseDto toResponseDto(Editor editor) {
        return EditorResponseDto.builder()
                .id(editor.getId())
                .firstName(editor.getFirstName())
                .lastName(editor.getLastName())
                .debutDate(editor.getDebutDate())
                .build();
    }
}