package com.iroute.commerce.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProcessRequest {

    @NotNull(message = "La fecha de proceso es obligatoria")
    private LocalDate processDate;

    public LocalDate getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }
}
