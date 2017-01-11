package mx.com.desoft.hidrogas.to;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class PipasTO {
    private Integer noPipa;
    private Integer porcentajeLlenado;
    private Long fechaRegistro;
    private String nominaRegistro;

    public PipasTO () {

    }

    public PipasTO (Integer noPipa, Integer porcentajeLlenado, Long fechaRegistro, String nominaRegistro) {
        this.noPipa = noPipa;
        this.porcentajeLlenado = porcentajeLlenado;
        this.fechaRegistro = fechaRegistro;
        this.nominaRegistro = nominaRegistro;
    }

    public Integer getNoPipa() {
        return noPipa;
    }

    public void setNoPipa(Integer noPipa) {
        this.noPipa = noPipa;
    }

    public Integer getPorcentajeLlenado() {
        return porcentajeLlenado;
    }

    public void setPorcentajeLlenado(Integer porcentajeLlenado) {
        this.porcentajeLlenado = porcentajeLlenado;
    }

    public Long getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Long fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNominaRegistro() {
        return nominaRegistro;
    }

    public void setNominaRegistro(String nominaRegistro) {
        this.nominaRegistro = nominaRegistro;
    }

}
