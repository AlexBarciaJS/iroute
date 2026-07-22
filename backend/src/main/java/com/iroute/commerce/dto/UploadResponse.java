package com.iroute.commerce.dto;

public class UploadResponse {

    private int insertedRecords;
    private String message;

    public UploadResponse() {
    }

    public UploadResponse(int insertedRecords, String message) {
        this.insertedRecords = insertedRecords;
        this.message = message;
    }

    public int getInsertedRecords() {
        return insertedRecords;
    }

    public void setInsertedRecords(int insertedRecords) {
        this.insertedRecords = insertedRecords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
