package com.iroute.commerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "commerce_quarantine")
public class CommerceQuarantine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pc_codcom", nullable = false, length = 20)
    private String pcCodcom;

    @Column(name = "pc_nomcomred", length = 100)
    private String pcNomcomred;

    @Column(name = "pc_tipdoc", length = 5)
    private String pcTipdoc;

    @Column(name = "pc_numdoc", length = 20)
    private String pcNumdoc;

    @Column(name = "pc_processdate", nullable = false)
    private LocalDate pcProcessdate;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Column(name = "created_at", insertable = false, updatable = false)
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
