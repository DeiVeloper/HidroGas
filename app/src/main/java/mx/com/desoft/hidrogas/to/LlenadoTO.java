package mx.com.desoft.hidrogas.to;

/**
 * Created by David on 04/01/17.
 */

public class LlenadoTO {

    private Integer idLlenado;
    private Integer noPipa;
    private Integer porcentajeLlenado;
    private Integer fechaRegistro;
    private String  nominaRegistro;


    public LlenadoTO() {
    }

    public LlenadoTO(Integer idLlenado, Integer noPipa, Integer porcentajeLlenado, Integer fechaRegistro, String nominaRegistro) {
        this.idLlenado = idLlenado;
        this.noPipa = noPipa;
        this.porcentajeLlenado = porcentajeLlenado;
        this.fechaRegistro = fechaRegistro;
        this.nominaRegistro = nominaRegistro;
    }

    public Integer getIdLlenado() {
        return idLlenado;
    }

    public void setIdLlenado(Integer idLlenado) {
        this.idLlenado = idLlenado;
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
