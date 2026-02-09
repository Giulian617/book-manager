package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.TranslatorCreateDto;
import com.unibuc.book_app.dto.TranslatorResponseDto;
import com.unibuc.book_app.dto.TranslatorUpdateDto;
import com.unibuc.book_app.model.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TranslatorMapper {
    private final BookMapper bookMapper;

    public Translator toEntity(TranslatorCreateDto dto) {
        return Translator.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
    }

    public void updateEntityFromDto(TranslatorUpdateDto dto, Translator translator) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(translator::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(translator::setLastName);
    }

    public TranslatorResponseDto toResponseDto(Translator translator) {
        return TranslatorResponseDto.builder()
                .id(translator.getId())
                .firstName(translator.getFirstName())
                .lastName(translator.getLastName())
                .build();
    }
}