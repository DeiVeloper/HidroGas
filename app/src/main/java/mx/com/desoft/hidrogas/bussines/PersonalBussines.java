package mx.com.desoft.hidrogas.bussines;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import mx.com.desoft.hidrogas.LoginActivity;
import mx.com.desoft.hidrogas.to.PersonalTO;

public class PersonalBussines {
    private Cursor registros;
    private static final int ADMINISTRADOR = 0;

    public boolean guardar(PersonalTO personalTO, boolean flgEditar) {
        PipasBussines pipasBussines = new PipasBussines();
        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", personalTO.getNomina());
        registro.put("nombre", personalTO.getNombre());
        registro.put("apellidoPaterno", personalTO.getApellidoPaterno());
        registro.put("apellidoMaterno", personalTO.getApellidoMaterno());
        registro.put("password", personalTO.getPassword());
        if (personalTO.getTipoEmpleado() != ADMINISTRADOR) {
            registros = pipasBussines.buscarByIdPipa(personalTO.getNoPipa());
            if (registros.moveToFirst()) {
                registro.put("idPipa", registros.getInt(0));
            } else {
                registro.put("idPipa", 0);
            }
        }
        registro.put("nominaRegistro", personalTO.getNominaRegistro());
        registro.put("tipoEmpleado", personalTO.getTipoEmpleado());
        if (flgEditar) {
            LoginActivity.conexion.update("empleados", registro, "nominaEmpleado = " + personalTO.getNomina(), null);
        } else {
            registros = buscarByNomina(personalTO.getNomina());
            if (registros != null && registros.moveToFirst()) {
                flgEditar = false;
            } else {
                registro.put("fechaRegistro", personalTO.getFechaRegistro());
                LoginActivity.conexion.insert("empleados", null, registro);
                flgEditar = true;
            }
        }
        return flgEditar;
    }

    public Cursor buscar(PersonalTO personalTO) {
        String condicion = "";
        if (personalTO.getNomina() != 0) {
            condicion += " AND nominaEmpleado = " + personalTO.getNomina();
        }
        if (!personalTO.getNombre().equals("")) {
            condicion += " AND nombre = '" + personalTO.getNombre() + "' ";
        }
        registros = LoginActivity.conexion.rawQuery("SELECT *,p.noPipa,p.idPipa FROM empleados e  LEFT JOIN  pipas p ON (p.idPipa = e.idPipa) WHERE 1=1  " + condicion, null);
        return registros;
    }

    private Cursor buscarByNomina( Integer nomina) {
        if (nomina.equals("")) {
            return null;
        } else {
            return LoginActivity.conexion.rawQuery("SELECT * FROM empleados WHERE nominaEmpleado = " + nomina, null);
        }
    }

    public void eliminar(PersonalTO personalTO) {
        LoginActivity.conexion.delete("Empleados", "nominaEmpleado = " + personalTO.getNomina(), null);
    }

    @SuppressLint("Recycle")
    public PersonalTO getUserDataBase(Integer user, String pass){
        PersonalTO usuario = null;
        Cursor cursor;
        String  selectQuery = "SELECT nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, nominaRegistro FROM Empleados" +
                    " WHERE nominaEmpleado = " + user + " AND password = '" + pass + "' AND tipoEmpleado = 0 ";
        try {
            cursor = LoginActivity.conexion.rawQuery(selectQuery, null);
        }catch (SQLiteException e){
            return null;
        }
        if (cursor.moveToFirst()) {
            do {
                usuario = new PersonalTO();
                usuario.setNomina(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setApellidoPaterno(cursor.getString(2));
                usuario.setApellidoMaterno(cursor.getString(3));
                usuario.setNominaRegistro(Integer.valueOf(cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        return usuario;
    }

    public void guardarEmpleadosExcel(PersonalTO personalTO){
        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", personalTO.getNomina());
        registro.put("nombre", personalTO.getNombre());
        registro.put("apellidoPaterno", personalTO.getApellidoPaterno());
        registro.put("apellidoMaterno", personalTO.getApellidoMaterno());
        registro.put("idPipa", personalTO.getNoPipa());
        registro.put("nominaRegistro", personalTO.getNominaRegistro());
        registro.put("tipoEmpleado", personalTO.getTipoEmpleado());
        registro.put("fechaRegistro", personalTO.getFechaRegistro());
        registros = getEmpleadoByNoNomina(personalTO.getNomina());
        if (registros.moveToFirst()) {
            LoginActivity.conexion.update("Empleados", registro,"nominaEmpleado"+ "=" + personalTO.getNomina() ,null);
        } else {
            LoginActivity.conexion.insert("Empleados", null, registro);
        }
    }

    private Cursor getEmpleadoByNoNomina(Integer noNomina){
        String condicion = "";
        if (noNomina != 0) {
            condicion += " AND nominaEmpleado = " + noNomina;
        }
        return LoginActivity.conexion.rawQuery("SELECT * FROM Empleados WHERE 1=1 " + condicion, null);
    }
}
