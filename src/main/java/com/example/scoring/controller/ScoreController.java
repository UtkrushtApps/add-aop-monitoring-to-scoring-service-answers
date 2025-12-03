package com.example.scoring.controller;

import com.example.scoring.dto.ScoreAsyncResponse;
import com.example.scoring.dto.ScoreRequest;
import com.example.scoring.dto.ScoreResponse;
import com.example.scoring.domain.Score;
import com.example.scoring.service.ScoreService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing APIs for synchronous and asynchronous score calculation.
 */
@RestController
@RequestMapping("/api/scores")
@Validated
public class ScoreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreController.class);

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * Synchronously calculates a score and returns the result when ready.
     */
    @PostMapping("/sync")
    public ResponseEntity<ScoreResponse> calculateScoreSync(@Valid @RequestBody ScoreRequest request) {
        LOGGER.debug("Received synchronous scoring request for candidateId={} totalQuestions={} correctAnswers={}",
                request.getCandidateId(), request.getTotalQuestions(), request.getCorrectAnswers());

        Score score = scoreService.calculateScoreSync(request);
        return ResponseEntity.ok(ScoreResponse.fromEntity(score));
    }

    /**
     * Triggers an asynchronous score calculation. The calculation is executed on a separate thread
     * and this endpoint returns immediately with an accepted response.
     */
    @PostMapping("/async")
    public ResponseEntity<ScoreAsyncResponse> calculateScoreAsync(@Valid @RequestBody ScoreRequest request) {
        LOGGER.debug("Received asynchronous scoring request for candidateId={} totalQuestions={} correctAnswers={}",
                request.getCandidateId(), request.getTotalQuestions(), request.getCorrectAnswers());

        scoreService.calculateScoreAsync(request);

        ScoreAsyncResponse response = new ScoreAsyncResponse(
                request.getCandidateId(),
                "ACCEPTED",
                "Score calculation has been scheduled for asynchronous processing."
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
