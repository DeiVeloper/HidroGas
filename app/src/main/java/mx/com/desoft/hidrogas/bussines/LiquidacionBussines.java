package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.model.Empleado;
import mx.com.desoft.hidrogas.model.Liquidaciones;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.ViajesTO;

/**
 * Created by carlosdavid.castro on 29/12/2016.
 */

public class LiquidacionBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private static SQLiteDatabase bd;

    public LiquidacionBussines() {

    }

    private void iniciarDataBase(ViewGroup viewGroup){
        baseDatos = new AdminSQLiteOpenHelper(viewGroup.getContext());
        bd = baseDatos.getWritableDatabase();
    }

    public void guardarLiquidacion(ViewGroup viewGroup, LiquidacionesTO liquidacionesTO,List<ViajesTO> listaViajes) {
        iniciarDataBase(viewGroup);
        ContentValues registro = new ContentValues();
        registro.put("nominaChofer", liquidacionesTO.getNominaChofer());
        registro.put("nominaAyudante", liquidacionesTO.getNominaAyudante());
        registro.put("noPipa", liquidacionesTO.getNoPipa());
        registro.put("variacion", liquidacionesTO.getVariacion());
        registro.put("fechaRegistro", liquidacionesTO.getFechaRegistro().toString());
        registro.put("nominaRegistro", liquidacionesTO.getNominaRegistro());
        Long idLiquidacion = bd.insert("liquidacion", null, registro);

        ContentValues registroViajes = new ContentValues();
        for (ViajesTO viajes : listaViajes) {
            registroViajes.put("idLiquidacion", idLiquidacion.intValue());
            registroViajes.put("porcentajeInicial", viajes.getPorcentajeInicial());
            registroViajes.put("porcentajeFinal", viajes.getPorcentajeFinal());
            registroViajes.put("totalizadorInicial", viajes.getTotalizadorInicial());
            registroViajes.put("totalizadorFinal", viajes.getTotalizadorFinal());
            bd.insert("viajes", null, registroViajes);
        }

        bd.close();
    }

    public List<LiquidacionesTO> getAllLiquidaciones(ViewGroup viewGroup){
        iniciarDataBase(viewGroup);
            List<LiquidacionesTO> contactList = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT  idLiquidacion,noPipa, nominaChofer, nominaAyudante, variacion FROM Liquidacion";

            Cursor cursor = bd.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    LiquidacionesTO contact = new LiquidacionesTO();
                    contact.setIdLiquidacion(Integer.parseInt(cursor.getString(0)));
                    contact.setNoPipa(Integer.parseInt(cursor.getString(1)));
                    contact.setNominaChofer(cursor.getColumnName(2));
                    contact.setNominaAyudante(cursor.getColumnName(3));
                    //contact.setVariacion(Integer.parseInt(cursor.getColumnName(4)));
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

            // return contact list
            return contactList;

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
        selectQuery.append("AND     liquidacion.fechaRegistro = " );
        selectQuery.append("        liquidacion.noPipa = " + idPipa);

        Cursor cursor = bd.rawQuery(selectQuery.toString(), null);

        if (cursor.moveToFirst()) {
            do {
                ViajesTO viaje = new ViajesTO();
                viaje.setIdViaje(cursor.getInt(0));
                viaje.setIdLiquidacion(cursor.getInt(1));
                viaje.setPorcentajeInicial(cursor.getInt(2));
                viaje.setPorcentajeFinal(cursor.getInt(3));
                viaje.setTotalizadorInicial(cursor.getInt(4));
                viaje.setTotalizadorFinal(cursor.getInt(4));
                lista.add(viaje);
            } while (cursor.moveToNext());
        }
        return lista;
    }
}
