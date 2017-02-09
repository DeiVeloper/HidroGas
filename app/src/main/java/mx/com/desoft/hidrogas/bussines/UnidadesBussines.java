package mx.com.desoft.hidrogas.bussines;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.MainActivity;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by carlosdavid.castro on 29/12/2016.
 */

public class UnidadesBussines {
    public UnidadesBussines(){

    }

    private static AdminSQLiteOpenHelper baseDatos;
    private static final String ADMINISTRADOR = "Administrador";
    private static final int CHOFER = 2;
    private static final int AYUDANTE = 3;

    public List<PersonalTO> obtenerPersonal(ViewGroup viewGroup, Integer idUnidad) {
        List<PersonalTO> lista = new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select idPipa, nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, tipoEmpleado from empleados where idPipa='" + idUnidad + "'", null);

        if (fila.moveToFirst()) {
            do {
                PersonalTO personalTO = new PersonalTO();
                personalTO.setNoPipa(Integer.parseInt(fila.getString(0)));
                personalTO.setNomina(fila.getInt(1));
                personalTO.setNombre(fila.getString(2));
                personalTO.setApellidoPaterno(fila.getString(3));
                personalTO.setApellidoMaterno(fila.getString(4));
                personalTO.setTipoEmpleado(Integer.parseInt(fila.getString(5).toString()));
                lista.add(personalTO);
            }while (fila.moveToNext());

        }
        return lista;
    }

    public Integer getCapacidadPipa (ViewGroup viewGroup, Integer idPipa){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        Integer porcentaje = 0;
        StringBuilder consulta = new StringBuilder();
        consulta.append("   SELECT  capacidad ");
        consulta.append("   FROM    Pipas ");
        consulta.append("   WHERE   idPipa = " + idPipa);
        Cursor cursor = bd.rawQuery(consulta.toString(), null);
        if (cursor.moveToFirst()){
            porcentaje = cursor.getInt(0);
        }
        return porcentaje;
    }

    public PersonalTO getChoferPipa(ViewGroup viewGroup, Integer noNomina){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select idPipa, " +
                "nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, tipoEmpleado " +
                "from empleados e " +
                " where nominaEmpleado = " + noNomina +
                " and tipoEmpleado = " + CHOFER, null);
        PersonalTO personalTO = null;
        if (fila.moveToFirst()) {
            personalTO = new PersonalTO();
            personalTO.setNoPipa(Integer.parseInt(fila.getString(0)));
            personalTO.setNomina(fila.getInt(1));
            personalTO.setNombre(fila.getString(2));
            personalTO.setApellidoPaterno(fila.getString(3));
            personalTO.setApellidoMaterno(fila.getString(4));
            personalTO.setTipoEmpleado(Integer.parseInt(fila.getString(5).toString()));

        }
        return personalTO;
    }

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
            personalTO.setNoPipa(Integer.parseInt(fila.getString(0)));
            personalTO.setNomina(fila.getInt(1));
            personalTO.setNombre(fila.getString(2));
            personalTO.setApellidoPaterno(fila.getString(3));
            personalTO.setApellidoMaterno(fila.getString(4));
            personalTO.setTipoEmpleado(Integer.parseInt(fila.getString(5).toString()));

        }
        return personalTO;
    }

}
