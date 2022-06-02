package com.example.proyectoapps.Model;

public class Usuario {

    private String IDE_USU;
    private String NOM_USU;
    private String APE_USU;
    private String COR_USU;
    private String CON_USU;
    private String URL_IMG;

    public Usuario() {
    }

    public String getURL_IMG() {
        return URL_IMG;
    }

    public void setURL_IMG(String URL_IMG) {
        this.URL_IMG = URL_IMG;
    }

    public Usuario(String IDE_USU, String NOM_USU, String APE_USU, String COR_USU, String CON_USU, String URL_IMG) {
        this.IDE_USU = IDE_USU;
        this.NOM_USU = NOM_USU;
        this.APE_USU = APE_USU;
        this.COR_USU = COR_USU;
        this.CON_USU = CON_USU;
        this.URL_IMG = URL_IMG;
    }

    public String getIDE_USU() {
        return IDE_USU;
    }

    public void setIDE_USU(String IDE_USU) {
        this.IDE_USU = IDE_USU;
    }

    public String getNOM_USU() {
        return NOM_USU;
    }

    public void setNOM_USU(String NOM_USU) {
        this.NOM_USU = NOM_USU;
    }

    public String getAPE_USU() {
        return APE_USU;
    }

    public void setAPE_USU(String APE_USU) {
        this.APE_USU = APE_USU;
    }

    public String getCOR_USU() {
        return COR_USU;
    }

    public void setCOR_USU(String COR_USU) {
        this.COR_USU = COR_USU;
    }

    public String getCON_USU() {
        return CON_USU;
    }

    public void setCON_USU(String CON_USU) {
        this.CON_USU = CON_USU;
    }

    @Override
    public String toString() {
        return NOM_USU;
    }
}
