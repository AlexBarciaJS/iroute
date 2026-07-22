package com.iroute.commerce.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProcessResponse {

    private LocalDate processDate;
    private int quarantineCount;
    private String message;
    private List<QuarantineResponse> records = new ArrayList<>();

    public ProcessResponse() {
    }

    public ProcessResponse(
            LocalDate processDate,
            int quarantineCount,
            String message,
            List<QuarantineResponse> records) {
        this.processDate = processDate;
        this.quarantineCount = quarantineCount;
        this.message = message;
        this.records = records != null ? records : new ArrayList<>();
    }

    public LocalDate getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }

    public int getQuarantineCount() {
        return quarantineCount;
    }

    public void setQuarantineCount(int quarantineCount) {
        this.quarantineCount = quarantineCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<QuarantineResponse> getRecords() {
        return records;
    }

    public void setRecords(List<QuarantineResponse> records) {
        this.records = records;
    }
}
