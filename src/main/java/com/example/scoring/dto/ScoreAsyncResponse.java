package com.example.scoring.dto;

/**
 * Response payload returned when an asynchronous score calculation is triggered.
 */
public class ScoreAsyncResponse {

    private String candidateId;
    private String status;
    private String message;

    public ScoreAsyncResponse() {
    }

    public ScoreAsyncResponse(String candidateId, String status, String message) {
        this.candidateId = candidateId;
        this.status = status;
        this.message = message;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
