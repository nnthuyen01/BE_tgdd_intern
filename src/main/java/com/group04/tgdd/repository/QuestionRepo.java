package com.group04.tgdd.repository;

import com.group04.tgdd.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepo extends JpaRepository<Question, Long> {
}
