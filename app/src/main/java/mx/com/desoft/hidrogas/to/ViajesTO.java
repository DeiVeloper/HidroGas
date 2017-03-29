package mx.com.desoft.hidrogas.to;

import mx.com.desoft.hidrogas.model.Liquidaciones;

/**
 * Created by carlosdavid.castro on 29/12/2016.
 */

public class ViajesTO {
    private Integer idViaje;
    private Integer idLiquidacion;
    private Integer porcentajeInicial;
    private Integer totalizadorInicial;
    private Integer porcentajeFinal;
    private Integer totalizadorFinal;

    public Integer getIdViaje() {
        return idViaje;
    }

    public void setIdViaje(Integer idViaje) {
        this.idViaje = idViaje;
    }

    public Integer getIdLiquidacion() {
        return idLiquidacion;
    }

    public void setIdLiquidacion(Integer idLiquidacion) {
        this.idLiquidacion = idLiquidacion;
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

}
