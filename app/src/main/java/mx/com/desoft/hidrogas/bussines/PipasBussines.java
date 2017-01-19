package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.to.PipasTO;

/**
 * Created by erick.martinez on 08/01/2017.
 */

public class PipasBussines {
    private static AdminSQLiteOpenHelper baseDatos;
    private Cursor registros;
    private ContentValues registro;
    private SQLiteDatabase bd;
    public PipasBussines() {

    }

    public boolean guardar(Context context, PipasTO pipasTO) {
        bd = getBase(context);
        registro = new ContentValues();
        registro.put("noPipa", pipasTO.getNoPipa());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        registros = buscarByNoPipa(context, pipasTO.getNoPipa());
        if (registros.moveToFirst()) {
            return false;
        } else {
            bd.insert("Pipas", null, registro);
            return true;
        }
    }

    public void llenar(Context context, PipasTO pipasTO) {
        bd = getBase(context);
        registro = new ContentValues();
        registro.put("noPipa", pipasTO.getNoPipa());
        registro.put("porcentajeLlenado", pipasTO.getPorcentajeLlenado());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        bd.insert("Llenado", null, registro);
    }

    public Cursor buscarLlenadoByNoPipa(Context context, Integer pipa) {
        String condicion = "";
        bd = getBase(context);
        if (pipa != 0) {
            condicion += " AND noPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT * FROM Llenado WHERE 1=1 " + condicion + " ORDER BY idLlenado DESC", null);
        return registros;
    }

    public Cursor buscarChoferAyudanteByNoPipa(Context context, Integer pipa) {
        String condicion = "";
        bd = getBase(context);
        if (pipa != 0) {
            condicion += " AND noPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT * FROM Empleados WHERE 1=1 " + condicion, null);
        return registros;
    }

    public Cursor buscarByNoPipa(Context context, Integer pipa) {
        String condicion = "";
        bd = getBase(context);
        if (pipa != 0) {
            condicion += " AND noPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);
        return registros;
    }

    public void eliminar (Context context, Integer pipa) {
        bd = getBase(context);
        bd.delete("Pipas", "noPipa = " + pipa, null);
        bd.close();
    }

    public List<String> getAllPipas(Context context){
        bd = getBase(context);
        List<String> lista = new ArrayList<>();
        StringBuilder consulta =  new StringBuilder();
        consulta.append("   SELECT  idPipa AS _id, ");
        consulta.append("           noPipa ");
        consulta.append("   FROM    Pipas ");
        lista.add("Seleccione");
        Cursor cursor = bd.rawQuery(consulta.toString(), null);
        if (cursor.moveToFirst()){
            String pipa =  new String();
            pipa = "No. Pipa - " + cursor.getInt(1);
            lista.add(pipa);
        }
        return lista;
    }

    private SQLiteDatabase getBase(Context context) {
        baseDatos = new AdminSQLiteOpenHelper(context/*, "hidroGas", null, 1*/);
        bd = baseDatos.getWritableDatabase();
        return bd;
    }
}
