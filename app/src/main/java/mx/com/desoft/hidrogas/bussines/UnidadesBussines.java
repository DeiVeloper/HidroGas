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

    public List<PersonalTO> obtenerPersonal(ViewGroup viewGroup, Integer idUnidad) {
        List<PersonalTO> lista = new ArrayList<>();
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select noPipa, nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, tipoEmpleado from empleados where noPipa='" + idUnidad + "'", null);

        if (fila.moveToFirst()) {
            PersonalTO personalTO = new PersonalTO();
            personalTO.setNoPipa(Integer.parseInt(fila.getString(0)));
            personalTO.setNomina(fila.getString(1));
            personalTO.setNombre(fila.getString(2));
            personalTO.setApellidoPaterno(fila.getString(3));
            personalTO.setApellidoMaterno(fila.getString(4));
            personalTO.setTipoEmpleado(Integer.parseInt(fila.getString(5).toString()));
            lista.add(personalTO);
        }
        return lista;
    }

    public Integer getPorcentajePipa (ViewGroup viewGroup, Integer noPipa){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(viewGroup.getContext());
        SQLiteDatabase bd = admin.getWritableDatabase();
        Integer porcentaje = 0;
        StringBuilder consulta = new StringBuilder();
        consulta.append("   SELECT  porcentajeLlenado ");
        consulta.append("   FROM    Llenado ");
        consulta.append("   WHERE   noPipa = " + noPipa);
        Cursor cursor = bd.rawQuery(consulta.toString(), null);
        if (cursor.moveToFirst()){
            porcentaje = cursor.getInt(0);
        }
        return porcentaje;
    }

}
