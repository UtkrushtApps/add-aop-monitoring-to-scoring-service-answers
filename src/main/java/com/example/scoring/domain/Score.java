package com.example.scoring.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * JPA entity representing a calculated score for a candidate.
 */
@Entity
@Table(name = "scores")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false, length = 64)
    private String candidateId;

    @Column(name = "score_value", nullable = false)
    private double scoreValue;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @Column(name = "correct_answers", nullable = false)
    private int correctAnswers;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    @Column(name = "async_calculation", nullable = false)
    private boolean asyncCalculation;

    protected Score() {
        // For JPA
    }

    public Score(String candidateId,
                 double scoreValue,
                 int totalQuestions,
                 int correctAnswers,
                 LocalDateTime calculatedAt,
                 boolean asyncCalculation) {
        this.candidateId = candidateId;
        this.scoreValue = scoreValue;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.calculatedAt = calculatedAt;
        this.asyncCalculation = asyncCalculation;
    }

    public Long getId() {
        return id;
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
