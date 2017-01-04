package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by erick.martinez on 22/12/2016.
 */

public class PersonalBussines {
    private static AdminSQLiteOpenHelper baseDatos;

    public PersonalBussines() {

    }

    public void guardar(ViewGroup viewGroup, PersonalTO personalTO) {
        baseDatos = new AdminSQLiteOpenHelper(viewGroup.getContext());
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
        bd.insert("empleados", null, registro);
        bd.close();
    }
}
