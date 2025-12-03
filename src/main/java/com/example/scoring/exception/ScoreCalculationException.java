package com.example.scoring.exception;

/**
 * Domain-specific exception thrown when score calculation fails.
 */
public class ScoreCalculationException extends RuntimeException {

    public ScoreCalculationException(String message) {
        super(message);
    }

    public ScoreCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
