package com.unibuc.book_app.mapper;

import com.unibuc.book_app.dto.PublisherCreateDto;
import com.unibuc.book_app.dto.PublisherResponseDto;
import com.unibuc.book_app.dto.PublisherSummaryDto;
import com.unibuc.book_app.dto.PublisherUpdateDto;
import com.unibuc.book_app.model.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PublisherMapper {
    public Publisher toEntity(PublisherCreateDto dto) {
        return Publisher.builder()
                .name(dto.getName())
                .foundedDate(dto.getFoundedDate())
                .build();
    }

    public void updateEntityFromDto(PublisherUpdateDto dto, Publisher publisher) {
        Optional.ofNullable(dto.getName()).ifPresent(publisher::setName);
        Optional.ofNullable(dto.getFoundedDate()).ifPresent(publisher::setFoundedDate);
    }

    public PublisherSummaryDto toSummaryDto(Publisher publisher) {
        return PublisherSummaryDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

    public PublisherResponseDto toResponseDto(Publisher publisher) {
        return PublisherResponseDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .foundedDate(publisher.getFoundedDate())
                .build();
    }
}