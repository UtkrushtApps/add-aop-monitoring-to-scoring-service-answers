package com.example.scoring.service;

import com.example.scoring.domain.Score;
import com.example.scoring.dto.ScoreRequest;

import java.util.concurrent.CompletableFuture;

/**
 * Service interface exposing scoring operations.
 */
public interface ScoreService {

    /**
     * Synchronously calculates a score.
     *
     * @param request input parameters for the score calculation
     * @return calculated and persisted score
     */
    Score calculateScoreSync(ScoreRequest request);

    /**
     * Asynchronously calculates a score. The returned {@link CompletableFuture}
     * can be used by callers that wish to await completion, but typical REST
     * endpoints will simply fire-and-forget.
     *
     * @param request input parameters for the score calculation
     * @return future representing the eventual calculated score
     */
    CompletableFuture<Score> calculateScoreAsync(ScoreRequest request);
}
