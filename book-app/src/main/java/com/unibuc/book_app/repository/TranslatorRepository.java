package com.unibuc.book_app.repository;

import com.unibuc.book_app.model.Translator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslatorRepository extends JpaRepository<Translator, Integer> {
}