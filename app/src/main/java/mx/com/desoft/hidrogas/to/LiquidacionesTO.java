package mx.com.desoft.hidrogas.to;


import java.util.Date;

/**
 * Created by carlosdavid.castro on 26/12/2016.
 */

public class LiquidacionesTO  {
    private Integer idLiquidacion;
    private String  nominaChofer;
    private String  nominaAyudante;
    private Integer noPipa;
    private Integer variacion;
    private Integer fechaRegistro;
    private String  nominaRegistro;

    public Integer getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Integer idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
    }

    public String getNominaChofer() {
        return nominaChofer;
    }

    public void setNominaChofer(String nominaChofer) {
        this.nominaChofer = nominaChofer;
    }

    public String getNominaAyudante() {
        return nominaAyudante;
    }

    public void setNominaAyudante(String nominaAyudante) {
        this.nominaAyudante = nominaAyudante;
    }

    public Integer getNoPipa() {
        return noPipa;
    }

    public void setNoPipa(Integer noPipa) {
        this.noPipa = noPipa;
    }

    public Integer getVariacion() {
        return variacion;
    }

    public void setVariacion(Integer variacion) {
        this.variacion = variacion;
    }

    public Integer getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Integer fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNominaRegistro() {
        return nominaRegistro;
    }

    public void setNominaRegistro(String nominaRegistro) {
        this.nominaRegistro = nominaRegistro;
    }
}
