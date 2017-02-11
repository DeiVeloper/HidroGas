package mx.com.desoft.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by erick.martinez on 30/01/2017.
 */

public class Utils {

    private Date fecha;
    private Long fechaLong;
    private String formato;
    private String fechaString;

    public Utils() {
    }

    public Utils(String formato, Date fecha) {
        this.formato = formato;
        this.fecha = fecha;
    }

    public Long consultaFechaLong (Date fecha, String formato) {
        SimpleDateFormat formatoTemp = new SimpleDateFormat(formato);
        Date soloFecha = null;
        fechaLong = 0L;
        try {
            soloFecha = formatoTemp.parse(formatoTemp.format(fecha));
            fechaLong = soloFecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaLong;
    }

    public String consultaFechaString (Date fecha, String formato) {
        SimpleDateFormat formatoTemp = new SimpleDateFormat(formato);
        fechaString = formatoTemp.format(fecha);
        return fechaString;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getFechaLong() {
        return fechaLong;
    }

    public void setFechaLong(Long fechaLong) {
        this.fechaLong = fechaLong;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getFechaString() {
        return fechaString;
    }

    public void setFechaString(String fechaString) {
        this.fechaString = fechaString;
    }
}
