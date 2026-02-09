package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.AuthorCreateDto;
import com.unibuc.book_app.dto.AuthorResponseDto;
import com.unibuc.book_app.dto.AuthorSummaryDto;
import com.unibuc.book_app.dto.AuthorUpdateDto;
import com.unibuc.book_app.model.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorMapper {
    public Author toEntity(AuthorCreateDto dto) {
        return Author.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .debutDate(dto.getDebutDate())
                .build();
    }

    public void updateEntityFromDto(AuthorUpdateDto dto, Author author) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(author::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(author::setLastName);
        Optional.ofNullable(dto.getDebutDate()).ifPresent(author::setDebutDate);
    }

    public AuthorSummaryDto toSummaryDto(Author author) {
        return AuthorSummaryDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();
    }

    public AuthorResponseDto toResponseDto(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .debutDate(author.getDebutDate())
                .build();
    }
}