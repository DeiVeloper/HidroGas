package mx.com.desoft.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private Date fecha;
    private Calendar calendar = Calendar.getInstance();

    public Utils() {
    }

    public Utils(Date fecha) {
        this.fecha = fecha;
    }

    @SuppressLint("SimpleDateFormat")
    public Long consultaFechaLong (Date fecha, String formato) {
        SimpleDateFormat formatoTemp = new SimpleDateFormat(formato);
        Date soloFecha;
        Long fechaLong = 0L;
        try {
            soloFecha = formatoTemp.parse(formatoTemp.format(fecha));
            fechaLong = soloFecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaLong;
    }

    @SuppressLint("SimpleDateFormat")
    public String consultaFechaString (Date fecha, String formato) {
        SimpleDateFormat formatoTemp = new SimpleDateFormat(formato);
        return formatoTemp.format(fecha);
    }

    @SuppressLint("SimpleDateFormat")
    public Long getFechaActual(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day);
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = formato.parse(formato.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha != null ? fecha.getTime() : 0;
    }

    @SuppressLint("SimpleDateFormat")
    public Long getFechaAnterior(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day);
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        calendar.add(Calendar.DATE,-1);
        try {
            fecha = formato.parse(formato.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha != null ? fecha.getTime() : 0;
    }

    @SuppressLint("SimpleDateFormat")
    public String convertirFecha(Long fechaLong){
        Date date=new Date(fechaLong);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(date);
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean esNumero (String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
