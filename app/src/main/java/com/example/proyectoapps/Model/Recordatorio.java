package com.example.proyectoapps.Model;

public class Recordatorio {

    private String NOT_REC;
    private String FEC_REC;
    private String HOR_REC;
    private String LUG_REC;
    private String URL_IMG;
    private String CON_USU;

    public Recordatorio() {
    }

    public String getNOT_REC() {
        return NOT_REC;
    }

    public void setNOT_REC(String NOT_REC) {
        this.NOT_REC = NOT_REC;
    }

    public String getFEC_REC() {
        return FEC_REC;
    }

    public void setFEC_REC(String FEC_REC) {
        this.FEC_REC = FEC_REC;
    }

    public String getHOR_REC() {
        return HOR_REC;
    }

    public void setHOR_REC(String HOR_REC) {
        this.HOR_REC = HOR_REC;
    }

    public String getLUG_REC() {
        return LUG_REC;
    }

    public void setLUG_REC(String LUG_REC) {
        this.LUG_REC = LUG_REC;
    }

    public String getURL_IMG() {
        return URL_IMG;
    }

    public void setURL_IMG(String URL_IMG) {
        this.URL_IMG = URL_IMG;
    }

    public String getCON_USU() {
        return CON_USU;
    }

    public void setCON_USU(String CON_USU) {
        this.CON_USU = CON_USU;
    }

    public Recordatorio(String NOT_REC, String FEC_REC, String HOR_REC, String LUG_REC, String URL_IMG, String CON_USU) {
        this.NOT_REC = NOT_REC;
        this.FEC_REC = FEC_REC;
        this.HOR_REC = HOR_REC;
        this.LUG_REC = LUG_REC;
        this.URL_IMG = URL_IMG;
        this.CON_USU = CON_USU;
    }


}
