package mx.com.desoft.hidrogas.bussines;

import android.app.Activity;
import android.content.Context;
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

    public ReporteUnidadesBussines(Context context){
        baseDatos = new AdminSQLiteOpenHelper(context);
        sqLiteDatabase = baseDatos.getWritableDatabase();
    }

    public List<LlenadoTO> getUnidadLlenadoByFecha(Long fechaBusqueda){
        List<LlenadoTO> lista = new ArrayList<>();
        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("    SELECT  liquidacion.idPipa, pipa.noPipa, liquidacion.fechaRegistro, liquidacion.variacion, liquidacion.porcentajeVariacion, pipa.clave " );
        selectQuery.append("    FROM    Liquidacion liquidacion, ");
        selectQuery.append("            Pipas pipa ");
        selectQuery.append("    WHERE   liquidacion.idPipa = pipa.idPipa" );
        selectQuery.append("    AND     liquidacion.alerta = 1 ");
        selectQuery.append("    AND     liquidacion.fechaRegistro = " + fechaBusqueda);

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery.toString(), null);
        if (cursor.moveToFirst()) {
            do {
                LlenadoTO unidad = new LlenadoTO();
                unidad.setNoPipa(cursor.getInt(1));
                unidad.setFechaRegistro(cursor.getLong(2));
                unidad.setVariacion(cursor.getInt(3));
                unidad.setPorcentajeVariacion(cursor.getFloat(cursor.getColumnIndex("porcentajeVariacion")));
                unidad.setClave(cursor.getString(cursor.getColumnIndex("clave")));
                lista.add(unidad);
            } while (cursor.moveToNext());
        }

        return lista;
    }
}
