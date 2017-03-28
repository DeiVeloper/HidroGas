package mx.com.desoft.hidrogas.to;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class PipasTO {
    private Integer idPipa;
    private Integer noPipa;
    private Integer porcentajeLlenado;
    private Integer capacidad;
    private Long fechaRegistro;
    private Integer nominaRegistro;
    private String nombreChofer;
    private String nombreAyudante;
    private Integer clave;
    private String clavePipa;

    public PipasTO () {

    }

    public PipasTO (Integer idPipa ,Integer noPipa, Integer porcentajeLlenado, Integer capacidad, Long fechaRegistro, Integer nominaRegistro, String nombreChofer, String nombreAyudante) {
        this.idPipa = idPipa;
        this.noPipa = noPipa;
        this.porcentajeLlenado = porcentajeLlenado;
        this.capacidad = capacidad;
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

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Long getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Long fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getNominaRegistro() {
        return nominaRegistro;
    }

    public void setNominaRegistro(Integer nominaRegistro) {
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

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getClavePipa() {
        return clavePipa;
    }

    public void setClavePipa(String clavePipa) {
        this.clavePipa = clavePipa;
    }

}
