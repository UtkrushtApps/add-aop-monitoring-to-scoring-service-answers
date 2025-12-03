package com.example.scoring.dto;

import com.example.scoring.domain.Score;

import java.time.LocalDateTime;

/**
 * Response payload representing a calculated score.
 */
public class ScoreResponse {

    private Long id;
    private String candidateId;
    private double scoreValue;
    private int totalQuestions;
    private int correctAnswers;
    private LocalDateTime calculatedAt;
    private boolean asyncCalculation;

    public ScoreResponse() {
    }

    public ScoreResponse(Long id,
                         String candidateId,
                         double scoreValue,
                         int totalQuestions,
                         int correctAnswers,
                         LocalDateTime calculatedAt,
                         boolean asyncCalculation) {
        this.id = id;
        this.candidateId = candidateId;
        this.scoreValue = scoreValue;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.calculatedAt = calculatedAt;
        this.asyncCalculation = asyncCalculation;
    }

    public static ScoreResponse fromEntity(Score score) {
        if (score == null) {
            return null;
        }
        return new ScoreResponse(
                score.getId(),
                score.getCandidateId(),
                score.getScoreValue(),
                score.getTotalQuestions(),
                score.getCorrectAnswers(),
                score.getCalculatedAt(),
                score.isAsyncCalculation()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public double getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(double scoreValue) {
        this.scoreValue = scoreValue;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public boolean isAsyncCalculation() {
        return asyncCalculation;
    }

    public void setAsyncCalculation(boolean asyncCalculation) {
        this.asyncCalculation = asyncCalculation;
    }
}
