package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.ViajesTO;

/**
 * Created by carlosdavid.castro on 29/12/2016.
 */

public class LiquidacionBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private static SQLiteDatabase bd;
    private Calendar calendar;

    public LiquidacionBussines() {

    }

    private void iniciarDataBase(ViewGroup viewGroup){
        baseDatos = new AdminSQLiteOpenHelper(viewGroup.getContext());
        bd = baseDatos.getWritableDatabase();
    }

    public Long guardarLiquidacion(ViewGroup viewGroup, LiquidacionesTO liquidacionesTO,List<ViajesTO> listaViajes) {
        iniciarDataBase(viewGroup);
        ContentValues registro = new ContentValues();
        registro.put("nominaChofer", liquidacionesTO.getNominaChofer());
        registro.put("nominaAyudante", liquidacionesTO.getNominaAyudante());
        registro.put("idPipa", liquidacionesTO.getNoPipa());
        registro.put("variacion", liquidacionesTO.getVariacion());
        registro.put("alerta", liquidacionesTO.getAlerta());
        registro.put("fechaRegistro", liquidacionesTO.getFechaRegistro().toString());
        registro.put("nominaRegistro", liquidacionesTO.getNominaRegistro());
        registro.put("porcentajeVariacion", liquidacionesTO.getPorcentajeVariacion());
        registro.put("economico", liquidacionesTO.getEconomico());
        Long idLiquidacion = bd.insert("liquidacion", null, registro);


        for (ViajesTO viajes : listaViajes) {
            ContentValues registroViajes = new ContentValues();
            registroViajes.put("idLiquidacion", idLiquidacion.intValue());
            registroViajes.put("porcentajeInicial", viajes.getPorcentajeInicial());
            registroViajes.put("porcentajeFinal", viajes.getPorcentajeFinal());
            registroViajes.put("totalizadorInicial", viajes.getTotalizadorInicial());
            registroViajes.put("totalizadorFinal", viajes.getTotalizadorFinal());
            bd.insert("viajes", null, registroViajes);
        }

        //bd.close();
        return idLiquidacion;
    }

    public LiquidacionesTO getLiquidacionByIdLiquidacion(ViewGroup viewGroup, Long idLiquidacion){
        iniciarDataBase(viewGroup);
        LiquidacionesTO liquidacion = null;
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("    SELECT  liquidacion.*, ");
        selectQuery.append("            (SELECT e.nombre||' '||e.apellidoPaterno||' '|| e.apellidoMaterno FROM Empleados e WHERE e.nominaEmpleado = liquidacion.nominaChofer) as chofer,");
        selectQuery.append("            (SELECT e.nombre||' '||e.apellidoPaterno||' '|| e.apellidoMaterno FROM Empleados e WHERE e.nominaEmpleado = liquidacion.nominaAyudante) as ayudante");
        selectQuery.append("    FROM    Liquidacion liquidacion ");
        selectQuery.append("    WHERE   liquidacion.idLiquidacion = " + idLiquidacion.intValue());
        Cursor cursor = bd.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                liquidacion = new LiquidacionesTO();
                liquidacion.setIdLiquidacion(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idLiquidacion"))));
                liquidacion.setNoPipa(cursor.getInt(cursor.getColumnIndex("idPipa")));
                liquidacion.setEconomico(cursor.getString(cursor.getColumnIndex("economico")));
                liquidacion.setNominaChofer(cursor.getString(cursor.getColumnIndex("nominaChofer")));
                liquidacion.setNominaAyudante(cursor.getString(cursor.getColumnIndex("nominaAyudante")));
                liquidacion.setVariacion(cursor.getInt(cursor.getColumnIndex("variacion")));
                liquidacion.setAlerta(cursor.getInt(cursor.getColumnIndex("alerta")));
                liquidacion.setFechaRegistro(cursor.getLong(cursor.getColumnIndex("fechaRegistro")));
                liquidacion.setPorcentajeVariacion(cursor.getFloat(cursor.getColumnIndex("porcentajeVariacion")));
                liquidacion.setChofer(cursor.getString(cursor.getColumnIndex("chofer")));
                liquidacion.setAyudante(cursor.getString(cursor.getColumnIndex("ayudante")));
            } while (cursor.moveToNext());
        }
        return liquidacion;
    }

    public List<ViajesTO> getPorcentajeInicialAnterior(ViewGroup viewGroup, Integer idPipa){
        iniciarDataBase(viewGroup);
        List<ViajesTO> lista = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT  min(idViaje), ");
        selectQuery.append("        viajes.idLiquidacion, ");
        selectQuery.append("        porcentajeInicial, ");
        selectQuery.append("        porcentajeFinal, ");
        selectQuery.append("        totalizadorInicial, ");
        selectQuery.append("        totalizadorFinal ");
        selectQuery.append("FROM    Viajes viajes,  ");
        selectQuery.append("        Liquidacion liquidacion ");
        selectQuery.append("WHERE   viajes.idLiquidacion = liquidacion.idLiquidacion ");
        selectQuery.append("AND     liquidacion.fechaRegistro = " + this.getFechaAnterior());
        selectQuery.append(" AND     liquidacion.idPipa = " + idPipa);
        Cursor cursor = bd.rawQuery(selectQuery.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                ViajesTO viaje = new ViajesTO();
                viaje.setIdViaje(cursor.getInt(0));
                viaje.setIdLiquidacion(cursor.getInt(1));
                viaje.setPorcentajeInicial(cursor.getInt(2));
                viaje.setPorcentajeFinal(cursor.getInt(3));
                viaje.setTotalizadorInicial(cursor.getInt(4));
                viaje.setTotalizadorFinal(cursor.getInt(5));
                lista.add(viaje);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public List<ViajesTO> getViajesByIdLiquidacion(ViewGroup viewGroup, int idLiquidacion){
        iniciarDataBase(viewGroup);
        List<ViajesTO> lista = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT  * ");
        selectQuery.append("FROM    Viajes viajes  ");
        selectQuery.append("WHERE   viajes.idLiquidacion = " + idLiquidacion);
        Cursor cursor = bd.rawQuery(selectQuery.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                ViajesTO viaje = new ViajesTO();
                viaje.setIdViaje(cursor.getInt(cursor.getColumnIndex("idViaje")));
                viaje.setIdLiquidacion(cursor.getInt(cursor.getColumnIndex("idLiquidacion")));
                viaje.setPorcentajeInicial(cursor.getInt(cursor.getColumnIndex("porcentajeInicial")));
                viaje.setPorcentajeFinal(cursor.getInt(cursor.getColumnIndex("porcentajeFinal")));
                viaje.setTotalizadorInicial(cursor.getInt(cursor.getColumnIndex("totalizadorInicial")));
                viaje.setTotalizadorFinal(cursor.getInt(cursor.getColumnIndex("totalizadorFinal")));
                lista.add(viaje);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    private Long getFechaAnterior(){
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
        return fecha.getTime();
    }
}
