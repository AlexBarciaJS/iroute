package com.iroute.commerce.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QuarantineResponse {

    private Long id;
    private String pcCodcom;
    private String pcNomcomred;
    private String pcTipdoc;
    private String pcNumdoc;
    private LocalDate pcProcessdate;
    private String motivo;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPcCodcom() {
        return pcCodcom;
    }

    public void setPcCodcom(String pcCodcom) {
        this.pcCodcom = pcCodcom;
    }

    public String getPcNomcomred() {
        return pcNomcomred;
    }

    public void setPcNomcomred(String pcNomcomred) {
        this.pcNomcomred = pcNomcomred;
    }

    public String getPcTipdoc() {
        return pcTipdoc;
    }

    public void setPcTipdoc(String pcTipdoc) {
        this.pcTipdoc = pcTipdoc;
    }

    public String getPcNumdoc() {
        return pcNumdoc;
    }

    public void setPcNumdoc(String pcNumdoc) {
        this.pcNumdoc = pcNumdoc;
    }

    public LocalDate getPcProcessdate() {
        return pcProcessdate;
    }

    public void setPcProcessdate(LocalDate pcProcessdate) {
        this.pcProcessdate = pcProcessdate;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
