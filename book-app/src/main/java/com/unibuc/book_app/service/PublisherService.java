package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.PublisherCreateDto;
import com.unibuc.book_app.dto.PublisherResponseDto;
import com.unibuc.book_app.dto.PublisherUpdateDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.PublisherMapper;
import com.unibuc.book_app.model.Publisher;
import com.unibuc.book_app.repository.BookPublisherRepository;
import com.unibuc.book_app.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    private final BookPublisherRepository bookPublisherRepository;
    private final BookMapper bookMapper;

    public List<PublisherResponseDto> findAllPublishers() {
        return publisherRepository
                .findAll()
                .stream()
                .map(publisherMapper::toResponseDto)
                .toList();
    }

    public List<BookSummaryDto> findAllBooksByPublisherId(Integer publisherId) {
        return bookPublisherRepository.findAllBooksByPublisherId(publisherId)
                .stream()
                .map(bookMapper::toSummaryDto)
                .toList();
    }

    public Publisher findPublisherEntityById(Integer publisherId) {
        return publisherRepository.findById(publisherId).orElseThrow(
                () -> new NotFoundException(String.format("Publisher with id %d not found", publisherId))
        );
    }

    public PublisherResponseDto findPublisherById(Integer publisherId) {
        return publisherMapper.toResponseDto(publisherRepository.findById(publisherId).orElseThrow(
                () -> new NotFoundException(String.format("Publisher with id %d not found", publisherId))
        ));
    }

    public PublisherResponseDto createPublisher(PublisherCreateDto publisherCreateDto) {
        Publisher publisher = publisherMapper.toEntity(publisherCreateDto);
        return publisherMapper.toResponseDto(publisherRepository.save(publisher));
    }

    public PublisherResponseDto updatePublisher(Integer publisherId, PublisherUpdateDto publisherUpdateDto) {
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(
                () -> new NotFoundException(String.format("Publisher with id %d not found", publisherId))
        );
        publisherMapper.updateEntityFromDto(publisherUpdateDto, publisher);
        return publisherMapper.toResponseDto(publisherRepository.save(publisher));
    }

    public void deletePublisher(Integer publisherId) {
        publisherRepository.deleteById(publisherId);
    }
}
