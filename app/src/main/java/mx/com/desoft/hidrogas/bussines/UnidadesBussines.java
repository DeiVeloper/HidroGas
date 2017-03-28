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
import mx.com.desoft.hidrogas.to.PipasTO;

/**
 * Created by carlosdavid.castro on 29/12/2016.
 */

public class UnidadesBussines {
    public UnidadesBussines(){

    }

    private static AdminSQLiteOpenHelper baseDatos;
    private static final String ADMINISTRADOR = "Administrador";
    private static final int CHOFER = 1;
    private static final int AYUDANTE = 2;

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

    public PipasTO getCapacidadPipa (ViewGroup viewGroup, Integer idPipa){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        PipasTO pipa = new PipasTO();
        StringBuilder consulta = new StringBuilder();
        consulta.append("   SELECT  capacidad, ");
        consulta.append("           clave");
        consulta.append("   FROM    Pipas ");
        consulta.append("   WHERE   noPipa = " + idPipa);
        Cursor cursor = bd.rawQuery(consulta.toString(), null);
        if (cursor.moveToFirst()){
            pipa.setCapacidad(cursor.getInt(cursor.getColumnIndex("capacidad")));
            pipa.setClavePipa(cursor.getString(cursor.getColumnIndex("clave")));
        }
        return pipa;
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
