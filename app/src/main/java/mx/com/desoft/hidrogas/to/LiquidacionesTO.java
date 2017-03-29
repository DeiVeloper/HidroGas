package mx.com.desoft.hidrogas.to;


import java.util.Date;
import java.util.List;

/**
 * Created by carlosdavid.castro on 26/12/2016.
 */

public class LiquidacionesTO  {
    private Integer idLiquidacion;
    private String  nominaChofer;
    private String  nominaAyudante;
    private String  chofer;
    private String  ayudante;
    private Integer noPipa;
    private Integer variacion;
    private Long    fechaRegistro;
    private String  nominaRegistro;
    private Integer alerta;
    private Integer porcentajeInicial;
    private Integer totalizadorInicial;
    private Integer porcentajeFinal;
    private Integer totalizadorFinal;
    private Integer autoconsumo ;
    private Integer medidores;
    private Integer traspasosRecibidos;
    private Integer traspasosRealizados;
    private Float   porcentajeVariacion;
    private String  clave;
    private String  economico;
    private List<ViajesTO> viajes;

    public LiquidacionesTO(Integer idLiquidacion, Integer noPipa, Long fechaRegistro) {
        this.idLiquidacion = idLiquidacion;
        this.noPipa = noPipa;
        this.fechaRegistro = fechaRegistro;
    }

    public  LiquidacionesTO(){}

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

    public Integer getAlerta() {
        return alerta;
    }

    public void setAlerta(Integer alerta) {
        this.alerta = alerta;
    }

    public Integer getPorcentajeInicial() {
        return porcentajeInicial;
    }

    public void setPorcentajeInicial(Integer porcentajeInicial) {
        this.porcentajeInicial = porcentajeInicial;
    }

    public Integer getTotalizadorInicial() {
        return totalizadorInicial;
    }

    public void setTotalizadorInicial(Integer totalizadorInicial) {
        this.totalizadorInicial = totalizadorInicial;
    }

    public Integer getPorcentajeFinal() {
        return porcentajeFinal;
    }

    public void setPorcentajeFinal(Integer porcentajeFinal) {
        this.porcentajeFinal = porcentajeFinal;
    }

    public Integer getTotalizadorFinal() {
        return totalizadorFinal;
    }

    public void setTotalizadorFinal(Integer totalizadorFinal) {
        this.totalizadorFinal = totalizadorFinal;
    }

    public Integer getAutoconsumo() {
        return autoconsumo;
    }

    public void setAutoconsumo(Integer autoconsumo) {
        this.autoconsumo = autoconsumo;
    }

    public Integer getMedidores() {
        return medidores;
    }

    public void setMedidores(Integer medidores) {
        this.medidores = medidores;
    }

    public Integer getTraspasosRecibidos() {
        return traspasosRecibidos;
    }

    public void setTraspasosRecibidos(Integer traspasosRecibidos) {
        this.traspasosRecibidos = traspasosRecibidos;
    }

    public Integer getTraspasosRealizados() {
        return traspasosRealizados;
    }

    public void setTraspasosRealizados(Integer traspasosRealizados) {
        this.traspasosRealizados = traspasosRealizados;
    }

    public Float getPorcentajeVariacion() {
        return porcentajeVariacion;
    }

    public void setPorcentajeVariacion(Float porcentajeVariacion) {
        this.porcentajeVariacion = porcentajeVariacion;
    }

    public String getChofer() {
        return chofer;
    }

    public void setChofer(String chofer) {
        this.chofer = chofer;
    }

    public String getAyudante() {
        return ayudante;
    }

    public void setAyudante(String ayudante) {
        this.ayudante = ayudante;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEconomico() {
        return economico;
    }

    public void setEconomico(String economico) {
        this.economico = economico;
    }

    public List<ViajesTO> getViajes() {
        return viajes;
    }

    public void setViajes(List<ViajesTO> viajes) {
        this.viajes = viajes;
    }
}
