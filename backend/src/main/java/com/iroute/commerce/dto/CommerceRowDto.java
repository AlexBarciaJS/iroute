package com.iroute.commerce.dto;

import java.time.LocalDate;

public class CommerceRowDto {

    private String pcCodcom;
    private String pcNomcomred;
    private String pcTipdoc;
    private String pcNumdoc;
    private LocalDate pcProcessdate;

    public CommerceRowDto() {
    }

    public CommerceRowDto(
            String pcCodcom,
            String pcNomcomred,
            String pcTipdoc,
            String pcNumdoc,
            LocalDate pcProcessdate) {
        this.pcCodcom = pcCodcom;
        this.pcNomcomred = pcNomcomred;
        this.pcTipdoc = pcTipdoc;
        this.pcNumdoc = pcNumdoc;
        this.pcProcessdate = pcProcessdate;
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
}
