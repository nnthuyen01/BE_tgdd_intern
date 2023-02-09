package com.group04.tgdd.repository;

import com.group04.tgdd.model.Technical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicalRepo extends JpaRepository<Technical, Long> {
}
