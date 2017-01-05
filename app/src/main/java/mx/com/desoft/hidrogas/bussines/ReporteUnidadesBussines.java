package mx.com.desoft.hidrogas.bussines;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.LlenadoTO;

/**
 * Created by David on 04/01/17.
 */

public class ReporteUnidadesBussines {

    private Activity activity = new Activity();
    private static AdminSQLiteOpenHelper baseDatos;
    private SQLiteDatabase sqLiteDatabase;

    public ReporteUnidadesBussines(){
        baseDatos = new AdminSQLiteOpenHelper(activity.getApplicationContext());
        sqLiteDatabase = baseDatos.getWritableDatabase();
    }

    public List<LlenadoTO> getUnidadLlenadoByFecha(Integer fechaBusqueda){
        List<LlenadoTO> lista = new ArrayList<>();
        String selectQuery = "SELECT  idLlenado, noPipa, porcentajeLlenado, fechaRegistro, nominaRegistro FROM Llenado";

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LlenadoTO unidad = new LlenadoTO();
                unidad.setIdLlenado(cursor.getInt(0));
                unidad.setNoPipa(cursor.getInt(1));
                unidad.setPorcentajeLlenado(cursor.getInt(2));
                //unidad.setFechaRegistro(cursor.getInt(3));
                unidad.setNominaRegistro(cursor.getColumnName(4));
                lista.add(unidad);
            } while (cursor.moveToNext());
        }

        return lista;
    }
}
