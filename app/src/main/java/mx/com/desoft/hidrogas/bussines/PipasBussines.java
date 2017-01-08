package mx.com.desoft.hidrogas.bussines;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class PipasBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private Cursor registros;
    public PipasBussines() {

    }

    public Cursor buscarByNoPipa(Context context, Integer pipa) {
        String condicion = "";
        baseDatos = new AdminSQLiteOpenHelper(context, "hidroGas", null, 1);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        if (pipa != 0) {
            condicion += " AND noPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);
        return registros;
    }
}
