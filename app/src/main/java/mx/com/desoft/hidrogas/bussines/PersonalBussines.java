package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by erick.martinez on 22/12/2016.
 */

public class PersonalBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private Cursor registros;
    public PersonalBussines() {

    }

    public void guardar(Context context, PersonalTO personalTO, boolean flgEditar) {
        baseDatos = new AdminSQLiteOpenHelper(context, "hidroGas", null, 1);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", personalTO.getNomina());
        registro.put("nombre", personalTO.getNombre());
        registro.put("apellidoPaterno", personalTO.getApellidoPaterno());
        registro.put("apellidoMaterno", personalTO.getApellidoMaterno());
        registro.put("password", personalTO.getPassword());
        registro.put("noPipa", personalTO.getNoPipa());
        registro.put("fechaRegistro", personalTO.getFechaRegistro());
        registro.put("nominaRegistro", personalTO.getNominaRegistro());
        registro.put("tipoEmpleado", personalTO.getTipoEmpleado());
        if (flgEditar) {
            bd.update("empleados", registro, "nominaEmpleado = " + personalTO.getNomina(), null);
        } else {
            bd.insert("empleados", null, registro);
        }
        bd.close();
    }

    public Cursor buscar(Context context, PersonalTO personalTO) {
        String condicion = "";
        baseDatos = new AdminSQLiteOpenHelper(context, "hidroGas", null, 1);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        if (!personalTO.getNomina().equals("")) {
            condicion += " AND nominaEmpleado = '" + personalTO.getNomina() + "' ";
        }
        if (!personalTO.getNombre().equals("")) {
            condicion += " AND nombre = '" + personalTO.getNombre() + "' ";
        }
        registros = bd.rawQuery("SELECT * FROM empleados WHERE 1=1 " + condicion, null);
        Toast.makeText(context, condicion, Toast.LENGTH_LONG).show();
        return registros;
    }

    public void eliminar(Context context, PersonalTO personalTO) {
        baseDatos = new AdminSQLiteOpenHelper(context, "hidroGas", null, 1);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        bd.delete("Empleados", "nominaEmpleado = " + personalTO.getNomina(), null);
    }
}
