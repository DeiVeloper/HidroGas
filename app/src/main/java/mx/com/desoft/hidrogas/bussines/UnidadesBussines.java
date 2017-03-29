package mx.com.desoft.hidrogas.bussines;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.LoginActivity;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.PipasTO;

public class UnidadesBussines {

    private static final int CHOFER = 1;
    private static final int AYUDANTE = 2;

    @SuppressLint("Recycle")
    public List<PersonalTO> obtenerPersonal(Integer idUnidad) {
        List<PersonalTO> lista = new ArrayList<>();
        Cursor fila = LoginActivity.conexion.rawQuery("select idPipa, nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, tipoEmpleado from empleados where idPipa='" + idUnidad + "'", null);
        if (fila.moveToFirst()) {
            do {
                PersonalTO personalTO = new PersonalTO();
                personalTO.setNoPipa(fila.getInt(fila.getColumnIndex("idPipa")));
                personalTO.setNomina(fila.getInt(fila.getColumnIndex("nominaEmpleado")));
                personalTO.setNombre(fila.getString(fila.getColumnIndex("nombre")));
                personalTO.setApellidoPaterno(fila.getString(fila.getColumnIndex("apellidoPaterno")));
                personalTO.setApellidoMaterno(fila.getString(fila.getColumnIndex("apellidoMaterno")));
                personalTO.setTipoEmpleado(fila.getInt(fila.getColumnIndex("tipoEmpleado")));
                lista.add(personalTO);
            }while (fila.moveToNext());
        }
        return lista;
    }

    @SuppressLint("Recycle")
    public PipasTO getCapacidadPipa (Integer idPipa){
        PipasTO pipa = new PipasTO();
        String consulta = "   SELECT  capacidad, " +
                "           clave" +
                "   FROM    Pipas " +
                "   WHERE   noPipa = " + idPipa;
        Cursor cursor = LoginActivity.conexion.rawQuery(consulta, null);
        if (cursor.moveToFirst()){
            pipa.setCapacidad(cursor.getInt(cursor.getColumnIndex("capacidad")));
            pipa.setClavePipa(cursor.getString(cursor.getColumnIndex("clave")));
        }
        return pipa;
    }

    @SuppressLint("Recycle")
    public PersonalTO getChoferPipa(Integer noNomina){
        Cursor fila = LoginActivity.conexion.rawQuery("select idPipa, " +
                "nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, tipoEmpleado " +
                "from empleados e " +
                " where nominaEmpleado = " + noNomina +
                " and tipoEmpleado = " + CHOFER, null);
        PersonalTO personalTO = null;
        if (fila.moveToFirst()) {
            personalTO = new PersonalTO();
            personalTO.setNoPipa(fila.getInt(fila.getColumnIndex("idPipa")));
            personalTO.setNomina(fila.getInt(fila.getColumnIndex("nominaEmpleado")));
            personalTO.setNombre(fila.getString(fila.getColumnIndex("nombre")));
            personalTO.setApellidoPaterno(fila.getString(fila.getColumnIndex("apellidoPaterno")));
            personalTO.setApellidoMaterno(fila.getString(fila.getColumnIndex("apellidoMaterno")));
            personalTO.setTipoEmpleado(fila.getInt(fila.getColumnIndex("tipoEmpleado")));
        }
        return personalTO;
    }

    @SuppressLint("Recycle")
    public PersonalTO getAyudantePipa(ViewGroup viewGroup, String noNomina){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select idPipa, " +
                "nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, tipoEmpleado " +
                "from empleados e "+
                " where nominaEmpleado = " + noNomina+
                " and tipoEmpleado = " + AYUDANTE, null);
        PersonalTO personalTO = null;
        if (fila.moveToFirst()) {
            personalTO = new PersonalTO();
            personalTO.setNoPipa(fila.getInt(fila.getColumnIndex("idPipa")));
            personalTO.setNomina(fila.getInt(fila.getColumnIndex("nominaEmpleado")));
            personalTO.setNombre(fila.getString(fila.getColumnIndex("nombre")));
            personalTO.setApellidoPaterno(fila.getString(fila.getColumnIndex("apellidoPaterno")));
            personalTO.setApellidoMaterno(fila.getString(fila.getColumnIndex("apellidoMaterno")));
            personalTO.setTipoEmpleado(fila.getInt(fila.getColumnIndex("tipoEmpleado")));
        }
        return personalTO;
    }

}
