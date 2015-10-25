package br.com.puc.sqlite;

/**
 * Created by Felipe on 19/10/2015.
 */
public class ciclista {

    private int NCICLI;
    private String CNOME;
    private String CEMAIL;
    private int CCELULAR;
    private boolean CSTATUS;

    public ciclista(){}

    public ciclista(String CNOME, String CEMAIL, int CCELULAR, boolean CSTATUS) {
        super();
        this.CNOME = CNOME;
        this.CEMAIL = CEMAIL;
        this.CCELULAR = CCELULAR;
        this.CSTATUS = CSTATUS;
    }

    //getters & setters
    public int getNCICLI() {
        return NCICLI;
    }

    public void setNCICLI(int NCICLI) {
        this.NCICLI = NCICLI;
    }

    public String getCNOME() {
        return CNOME;
    }

    public void setCNOME(String CNOME) {
        this.CNOME = CNOME;
    }

    public String getCEMAIL() {
        return CEMAIL;
    }

    public void setCEMAIL(String CEMAIL) {
        this.CEMAIL = CEMAIL;
    }

    public int getCCELULAR() {
        return CCELULAR;
    }

    public void setCCELULAR(int CCELULAR) {
        this.CCELULAR = CCELULAR;
    }

    public boolean isCSTATUS() {
        return CSTATUS;
    }

    public void setCSTATUS(boolean CSTATUS) {
        this.CSTATUS = CSTATUS;
    }

    @Override
    public String toString() {
        return "ciclista [NCICLI=" + NCICLI + ", CNOME=" + CNOME + ", CEMAIL=" + CEMAIL  + ", CCELULAR=" + CCELULAR + ", CSTATUS=" + CSTATUS
                + "]";
    }
}