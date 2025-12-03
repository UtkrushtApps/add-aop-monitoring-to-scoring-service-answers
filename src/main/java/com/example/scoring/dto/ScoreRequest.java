package com.example.scoring.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for triggering a score calculation.
 */
public class ScoreRequest {

    @NotBlank
    private String candidateId;

    @NotNull
    @Min(1)
    private Integer totalQuestions;

    @NotNull
    @Min(0)
    private Integer correctAnswers;

    public ScoreRequest() {
    }

    public ScoreRequest(String candidateId, Integer totalQuestions, Integer correctAnswers) {
        this.candidateId = candidateId;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
}
