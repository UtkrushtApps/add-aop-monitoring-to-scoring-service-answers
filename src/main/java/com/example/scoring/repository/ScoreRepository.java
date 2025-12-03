package com.example.scoring.repository;

import com.example.scoring.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for persisting {@link Score} entities.
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
}
