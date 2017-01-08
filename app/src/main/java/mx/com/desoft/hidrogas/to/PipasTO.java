package mx.com.desoft.hidrogas.to;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class PipasTO {
    private Integer noPipa;
    private Integer fechaRegistro;
    private String nominaRegistro;

    public PipasTO () {

    }

    public PipasTO (Integer noPipa, Integer fechaRegistro, String nominaRegistro) {
        this.noPipa = noPipa;
        this.fechaRegistro = fechaRegistro;
        this.nominaRegistro = nominaRegistro;
    }

    public Integer getNoPipa() {
        return noPipa;
    }

    public void setNoPipa(Integer noPipa) {
        this.noPipa = noPipa;
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
