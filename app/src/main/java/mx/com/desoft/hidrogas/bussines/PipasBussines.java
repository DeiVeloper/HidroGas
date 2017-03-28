package mx.com.desoft.hidrogas.bussines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
    public static HashMap<Integer,Integer> spinnerMap;
    private Calendar calendar;

    public PipasBussines() {

    }

    public boolean guardar(Context context, PipasTO pipasTO) {
        bd = getBase(context);
        registro = new ContentValues();
        registro.put("noPipa", pipasTO.getNoPipa());
        registro.put("capacidad", pipasTO.getCapacidad());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        registros = buscarByNoPipa(context, pipasTO.getNoPipa(), bd);
        if (registros.moveToFirst()) {
            return false;
        } else {
            bd.insert("Pipas", null, registro);
            return true;
        }
    }

    public Long guardar2(Context context, PipasTO pipasTO, SQLiteDatabase data) {
        Long idSaveOrUpdate;
        registro = new ContentValues();
        registro.put("noPipa", pipasTO.getNoPipa());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        registro.put("capacidad", pipasTO.getCapacidad());
        registros = buscarByNoPipa(context, pipasTO.getNoPipa(), data);
        if (registros.moveToFirst()) {
            idSaveOrUpdate = ((long)data.update("Pipas",registro, "noPipa = " + pipasTO.getNoPipa(), null));
        } else {
            idSaveOrUpdate = data.insert("Pipas", null, registro);
        }
        return idSaveOrUpdate;

    }

    public void llenar(Context context, PipasTO pipasTO, SQLiteDatabase data) {

        registro = new ContentValues();
        registro.put("idPipa", pipasTO.getIdPipa());
        registro.put("porcentajeLlenado", pipasTO.getPorcentajeLlenado());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        data.insert("Llenado", null, registro);

    }

    public void setClavePipa(Context context, PipasTO pipasTO, SQLiteDatabase data) {
        //bd = getBase(context);
        registro = new ContentValues();
        registro.put("clave", pipasTO.getClavePipa());
        data.update("Pipas", registro, "noPipa = " + pipasTO.getNoPipa(), null);

    }

    public Cursor buscarLlenadoByNoPipa(Context context, Integer pipa) {
        String condicion = "";
        bd = getBase(context);
        if (pipa != 0) {
            condicion += " AND p.idPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT l.* FROM Llenado l, pipas p WHERE 1=1 AND p.idPipa = l.idPipa " + condicion + " ORDER BY idLlenado DESC", null);
        return registros;
    }

    public Cursor buscarChoferAyudanteByNoPipa(Context context, Integer pipa) {
        String condicion = "";
        bd = getBase(context);
        if (pipa != 0) {
            condicion += " AND p.idPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT e.* FROM Empleados e, pipas p WHERE 1=1 AND e.idPipa = p.idPipa" + condicion, null);
        return registros;
    }

    public Cursor buscarByIdPipa(Context context, Integer pipa) {
        String condicion = "";
        bd = getBase(context);
        if (pipa != 0) {
            condicion += " AND idPipa = " + pipa;
        }
        registros = bd.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);
        return registros;
    }

    public Cursor buscarByNoPipa(Context context, Integer pipa, SQLiteDatabase data) {
        String condicion = "";
        if (pipa != 0) {
            condicion += " AND noPipa = " + pipa;
        }
        registros = data.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);

        return registros;
    }

    public Cursor buscarByNoPipa2(Context context, Integer noPipa) {
        String condicion = "";
        bd = getBase(context);
        if (noPipa != 0) {
            condicion += " AND noPipa = " + noPipa;
        }
        registros = bd.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);
        return registros;
    }

    public Integer getNoPipaByIdPipa(Context context, Integer pipa) {
        bd = getBase(context);
        registros = bd.rawQuery("SELECT * FROM pipas WHERE idPipa = " + pipa, null);
        if (registros.moveToFirst()){
            return registros.getInt(1);
        }
        return 0;
    }

    public boolean eliminar (Context context, Integer pipa) {
        bd = getBase(context);
        Cursor resgistroChoferAyudante = buscarChoferAyudanteByNoPipa(context, pipa);
        if (resgistroChoferAyudante.moveToFirst()) {
            return false;
        }
        bd.delete("Pipas", "idPipa = " + pipa, null);
        //bd.close();
        return true;
    }

    public List<String> getAllPipas(Context context){
        bd = getBase(context);
        List<String> lista = new ArrayList<>();
        StringBuilder consulta =  new StringBuilder();
        consulta.append("   SELECT  idPipa AS _id, ");
        consulta.append("           noPipa ");
        consulta.append("   FROM    Pipas ");
        lista.add(0,"Seleccione");
        Cursor cursor = bd.rawQuery(consulta.toString(), null);
        spinnerMap = new HashMap<Integer, Integer>();
        int contador=1;
        if (cursor.moveToFirst()){
            do {
                String pipa =  new String();
                pipa = "Pipa - " + cursor.getInt(1);
                spinnerMap.put(contador,cursor.getInt(0));
                lista.add(pipa);
                contador++;
            }while (cursor.moveToNext());

        }

        return lista;
    }

    public Integer getCapacidadDiaAnteriorPipa(Context context, Integer idPipa){
        bd = getBase(context);
        StringBuilder consulta =  new StringBuilder();
        consulta.append("   SELECT  porcentajeLlenado ");
        consulta.append("   FROM    Llenado ");
        consulta.append("   WHERE   fechaRegistro = " + this.getFechaAnterior());
        consulta.append("   AND     idPipa = " + idPipa);
        Cursor cursor = bd.rawQuery(consulta.toString(), null);
        Integer porcentajeLlenado = 0;
        if (cursor.moveToFirst()) {
            do {
               porcentajeLlenado = cursor.getInt(cursor.getColumnIndex("porcentajeLlenado"));
            } while (cursor.moveToNext());
        }
        return porcentajeLlenado;
    }

    private SQLiteDatabase getBase(Context context) {
        baseDatos = new AdminSQLiteOpenHelper(context);
        bd = baseDatos.getWritableDatabase();
        return bd;
    }

    private Long getFechaAnterior(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day);
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        calendar.add(Calendar.DATE,-1);
        try {
            fecha = formato.parse(formato.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha.getTime();
    }
}