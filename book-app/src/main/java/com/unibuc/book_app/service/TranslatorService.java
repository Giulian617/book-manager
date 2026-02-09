package com.unibuc.book_app.service;

import com.unibuc.book_app.dto.BookSummaryDto;
import com.unibuc.book_app.dto.TranslatorCreateDto;
import com.unibuc.book_app.dto.TranslatorResponseDto;
import com.unibuc.book_app.dto.TranslatorUpdateDto;
import com.unibuc.book_app.exception.NotFoundException;
import com.unibuc.book_app.mapper.BookMapper;
import com.unibuc.book_app.mapper.TranslatorMapper;
import com.unibuc.book_app.model.Translator;
import com.unibuc.book_app.repository.TranslatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslatorService {
    private final TranslatorRepository translatorRepository;
    private final TranslatorMapper translatorMapper;
    private final BookMapper bookMapper;

    public List<TranslatorResponseDto> findAllTranslators() {
        return translatorRepository
                .findAll()
                .stream()
                .map(translatorMapper::toResponseDto)
                .toList();
    }

    public List<BookSummaryDto> findAllBooksByTranslatorId(Integer translatorId) {
        return this.findTranslatorEntityById(translatorId)
                .getBooks()
                .stream()
                .map(bookMapper::toSummaryDto)
                .toList();
    }

    public Translator findTranslatorEntityById(Integer translatorId) {
        return translatorRepository.findById(translatorId).orElseThrow(
                () -> new NotFoundException(String.format("Translator with id %d not found", translatorId))
        );
    }

    public TranslatorResponseDto findTranslatorById(Integer translatorId) {
        return translatorMapper.toResponseDto(translatorRepository.findById(translatorId).orElseThrow(
                () -> new NotFoundException(String.format("Translator with id %d not found", translatorId))
        ));
    }

    public TranslatorResponseDto createTranslator(TranslatorCreateDto translatorCreateDto) {
        Translator translator = translatorMapper.toEntity(translatorCreateDto);
        return translatorMapper.toResponseDto(translatorRepository.save(translator));
    }

    public TranslatorResponseDto updateTranslator(Integer translatorId, TranslatorUpdateDto translatorUpdateDto) {
        Translator translator = translatorRepository.findById(translatorId).orElseThrow(
                () -> new NotFoundException(String.format("Translator with id %d not found", translatorId))
        );
        translatorMapper.updateEntityFromDto(translatorUpdateDto, translator);
        return translatorMapper.toResponseDto(translatorRepository.save(translator));
    }

    public void deleteTranslator(Integer translatorId) {
        translatorRepository.deleteById(translatorId);
    }
}
