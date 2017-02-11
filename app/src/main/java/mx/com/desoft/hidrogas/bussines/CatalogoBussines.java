package mx.com.desoft.hidrogas.bussines;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class CatalogoBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private Cursor registros;
    public CatalogoBussines() {

    }

    public Cursor getTipoEmpleado(Context context) {
        baseDatos = new AdminSQLiteOpenHelper(context/*, "hidroGas", null, 1*/);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        registros = bd.rawQuery("SELECT * FROM tipoEmpleados WHERE 1=1 ", null);
        return registros;
    }

    public Cursor getPipas(Context context) {
        baseDatos = new AdminSQLiteOpenHelper(context/*, "hidroGas", null, 1*/);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        registros = bd.rawQuery("SELECT * FROM Pipas WHERE 1=1 ", null);
        return registros;
    }
}
