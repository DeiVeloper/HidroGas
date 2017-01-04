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
    private Liquidaciones liquidaciones;

    public LiquidacionBussines() {

    }

    public void guardarLiquidacion(ViewGroup viewGroup, LiquidacionesTO liquidacionesTO,List<ViajesTO> viajesTO) {
        baseDatos = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nominaChofer", liquidacionesTO.getNominaChofer());
        registro.put("nominaAyudante", liquidacionesTO.getNominaAyudante());
        registro.put("noPipa", liquidacionesTO.getNoPipa());
        registro.put("variacion", liquidacionesTO.getVariacion());
        registro.put("fechaRegistro", liquidacionesTO.getFechaRegistro().toString());
        registro.put("nominaRegistro", liquidacionesTO.getNominaRegistro());
        bd.insert("liquidacion", null, registro);
        bd.close();
    }

    public List<LiquidacionesTO> getAllLiquidaciones(ViewGroup viewGroup){
        baseDatos = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
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
}
