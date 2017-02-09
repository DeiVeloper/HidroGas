package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.model.Empleado;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by erick.martinez on 22/12/2016.
 */

public class PersonalBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private Cursor registros;
    private PipasBussines pipasBussines;
    public PersonalBussines() {

    }


    public boolean guardar(Context context, PersonalTO personalTO, boolean flgEditar) {
        baseDatos = new AdminSQLiteOpenHelper(context);
        pipasBussines = new PipasBussines();
        SQLiteDatabase bd = baseDatos.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", personalTO.getNomina());
        registro.put("nombre", personalTO.getNombre());
        registro.put("apellidoPaterno", personalTO.getApellidoPaterno());
        registro.put("apellidoMaterno", personalTO.getApellidoMaterno());
        registro.put("password", personalTO.getPassword());
        registros = pipasBussines.buscarByNoPipa(context, personalTO.getNoPipa());
        if (registros.moveToFirst()){
            registro.put("noPipa", registros.getInt(1));
        } else {
            registro.put("noPipa", 0);
        }
        registro.put("nominaRegistro", personalTO.getNominaRegistro());
        registro.put("tipoEmpleado", personalTO.getTipoEmpleado());
        if (flgEditar) {
            bd.update("empleados", registro, "nominaEmpleado = " + personalTO.getNomina(), null);
        } else {
            registros = buscarByNomina(context, personalTO.getNomina());
            if (registros.moveToFirst()) {
                flgEditar = false;
            } else {
                registro.put("fechaRegistro", personalTO.getFechaRegistro());
                bd.insert("empleados", null, registro);
                flgEditar = true;
            }
        }
        bd.close();
        return flgEditar;
    }

    public Cursor buscar(Context context, PersonalTO personalTO) {
        String condicion = "";
        baseDatos = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        if (personalTO.getNomina() != 0) {
            condicion += " AND nominaEmpleado = " + personalTO.getNomina();
        }
        if (!personalTO.getNombre().equals("")) {
            condicion += " AND nombre = '" + personalTO.getNombre() + "' ";
        }
        registros = bd.rawQuery("SELECT * FROM empleados WHERE 1=1 " + condicion, null);
        return registros;
    }

    private Cursor buscarByNomina(Context context, Integer nomina) {
        if (nomina.equals("")) {
            return null;
        } else {
            baseDatos = new AdminSQLiteOpenHelper(context);
            SQLiteDatabase bd = baseDatos.getWritableDatabase();
            return bd.rawQuery("SELECT * FROM empleados WHERE nominaEmpleado = " + nomina, null);
        }
    }

    public void eliminar(Context context, PersonalTO personalTO) {
        baseDatos = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();
        bd.delete("Empleados", "nominaEmpleado = " + personalTO.getNomina(), null);
    }

    public PersonalTO getUserDataBase(Context context, String user, String pass){
        baseDatos = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase bd = baseDatos.getWritableDatabase();

        String selectQuery = "SELECT nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, password, idPipa, fechaRegistro, nominaRegistro, tipoEmpleado FROM Empleados" +
                " WHERE nominaEmpleado = " + user + " AND password = " +pass+ " AND tipoEmpleado = 0 ";
        PersonalTO usuario = null;

        Cursor cursor = bd.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                usuario = new PersonalTO();
                usuario.setNomina(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setApellidoPaterno(cursor.getString(2));
                usuario.setApellidoMaterno(cursor.getString(3));
                usuario.setPassword(cursor.getString(4));
                usuario.setNoPipa(cursor.getInt(5));
                //usuario.setFechaRegistro(cursor.getInt(6));
                usuario.setNominaRegistro(cursor.getInt(7));
                usuario.setTipoEmpleado(cursor.getInt(8));
            } while (cursor.moveToNext());
        }
        return usuario;
    }

    public void guardarEmpleadosExcel(Context context, PersonalTO personalTO){
        baseDatos = new AdminSQLiteOpenHelper(context);

        SQLiteDatabase bd = baseDatos.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", personalTO.getNomina());
        registro.put("nombre", personalTO.getNombre());
        registro.put("apellidoPaterno", personalTO.getApellidoPaterno());
        registro.put("apellidoMaterno", personalTO.getApellidoMaterno());
        registro.put("idPipa", personalTO.getNoPipa());
        registro.put("nominaRegistro", personalTO.getNominaRegistro());
        registro.put("tipoEmpleado", personalTO.getTipoEmpleado());
        registro.put("fechaRegistro", personalTO.getFechaRegistro());
        bd.insert("empleados", null, registro);
        bd.close();
    }
}
