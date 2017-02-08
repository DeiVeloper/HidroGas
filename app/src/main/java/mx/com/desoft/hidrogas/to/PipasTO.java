package mx.com.desoft.hidrogas.to;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class PipasTO {
    private Integer idPipa;
    private Integer noPipa;
    private Integer porcentajeLlenado;
    private Long fechaRegistro;
    private String nominaRegistro;
    private String nombreChofer;
    private String nombreAyudante;

    public PipasTO () {

    }

    public PipasTO (Integer idPipa, Integer noPipa, Integer porcentajeLlenado, Long fechaRegistro, String nominaRegistro, String nombreChofer, String nombreAyudante) {
        this.idPipa = idPipa;
        this.noPipa = noPipa;
        this.porcentajeLlenado = porcentajeLlenado;
        this.fechaRegistro = fechaRegistro;
        this.nominaRegistro = nominaRegistro;
        this.nombreChofer = nombreChofer;
        this.nombreAyudante = nombreAyudante;
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

    public String getNombreChofer() {
        return nombreChofer;
    }

    public void setNombreChofer(String nombreChofer) {
        this.nombreChofer = nombreChofer;
    }

    public String getNombreAyudante() {
        return nombreAyudante;
    }

    public void setNombreAyudante(String nombreAyudante) {
        this.nombreAyudante = nombreAyudante;
    }

    public Integer getIdPipa() {
        return idPipa;
    }

    public void setIdPipa(Integer idPipa) {
        this.idPipa = idPipa;
    }
}
