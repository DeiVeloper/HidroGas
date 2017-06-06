package mx.com.desoft.hidrogas.bussines;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import mx.com.desoft.hidrogas.LoginActivity;
import mx.com.desoft.hidrogas.to.PipasTO;
import mx.com.desoft.hidrogas.utils.Utils;

public class PipasBussines {

    private Cursor registros;
    private ContentValues registro;
    private Utils utils = new Utils();
    public static HashMap<Integer,Integer> spinnerMap;

    public boolean guardar(PipasTO pipasTO) {
        registro = new ContentValues();
        registro.put("noPipa", pipasTO.getNoPipa());
        registro.put("capacidad", pipasTO.getCapacidad());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        registros = buscarByNoPipa(pipasTO.getNoPipa());
        if (registros.moveToFirst()) {
            return false;
        } else {
            LoginActivity.conexion.insert("Pipas", null, registro);
            return true;
        }
    }

    public Long guardar2(PipasTO pipasTO) {
        Long idSaveOrUpdate;
        registro = new ContentValues();
        registro.put("noPipa", pipasTO.getNoPipa());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        registro.put("capacidad", pipasTO.getCapacidad());
        registros = buscarByNoPipa(pipasTO.getNoPipa());
        if (registros.moveToFirst()) {
            idSaveOrUpdate = ((long)LoginActivity.conexion.update("Pipas",registro, "noPipa = " + pipasTO.getNoPipa(), null));
        } else {
            idSaveOrUpdate = LoginActivity.conexion.insert("Pipas", null, registro);
        }
        return idSaveOrUpdate;

    }

    public void llenar(PipasTO pipasTO) {
        registro = new ContentValues();
        registro.put("idPipa", pipasTO.getIdPipa());
        registro.put("porcentajeLlenado", pipasTO.getPorcentajeLlenado());
        registro.put("fechaRegistro", pipasTO.getFechaRegistro());
        registro.put("nominaRegistro", pipasTO.getNominaRegistro());
        LoginActivity.conexion.insert("Llenado", null, registro);

    }

    public void setClavePipa(PipasTO pipasTO) {
        registro = new ContentValues();
        registro.put("clave", pipasTO.getClavePipa());
        LoginActivity.conexion.update("Pipas", registro, "noPipa = " + pipasTO.getNoPipa(), null);
    }

    public Cursor buscarLlenadoByNoPipa(Integer pipa) {
        String condicion = "";
        if (pipa != 0) {
            condicion += " AND p.idPipa = " + pipa;
        }
        registros = LoginActivity.conexion.rawQuery("SELECT l.* FROM Llenado l, pipas p WHERE 1=1 AND p.idPipa = l.idPipa " + condicion + " ORDER BY idLlenado DESC", null);
        return registros;
    }

    public Cursor buscarChoferAyudanteByNoPipa(Integer pipa) {
        String condicion = "";
        if (pipa != 0) {
            condicion += " AND p.idPipa = " + pipa;
        }
        registros = LoginActivity.conexion.rawQuery("SELECT e.* FROM Empleados e, pipas p WHERE 1=1 AND e.idPipa = p.idPipa" + condicion, null);
        return registros;
    }

    Cursor buscarByIdPipa(Integer pipa) {
        String condicion = "";
        if (pipa != 0) {
            condicion += " AND idPipa = " + pipa;
        }
        registros = LoginActivity.conexion.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);
        return registros;
    }

    public Cursor buscarByNoPipa(Integer pipa) {
        String condicion = "";
        if (pipa != 0) {
            condicion += " AND noPipa = " + pipa;
        }
        registros = LoginActivity.conexion.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);

        return registros;
    }

    public Cursor buscarByNoPipa2(Integer noPipa) {
        String condicion = "";
        if (noPipa != 0) {
            condicion += " AND noPipa = " + noPipa;
        }
        registros = LoginActivity.conexion.rawQuery("SELECT * FROM pipas WHERE 1=1 " + condicion, null);
        return registros;
    }

    public Integer getNoPipaByIdPipa(Integer pipa) {
        registros = LoginActivity.conexion.rawQuery("SELECT * FROM pipas WHERE idPipa = " + pipa, null);
        if (registros.moveToFirst()){
            return registros.getInt(1);
        }
        return 0;
    }

    public boolean eliminar (Integer pipa) {
        Cursor resgistroChoferAyudante = buscarChoferAyudanteByNoPipa(pipa);
        if (resgistroChoferAyudante.moveToFirst()) {
            return false;
        }
        LoginActivity.conexion.delete("Pipas", "idPipa = " + pipa, null);
        return true;
    }

    @SuppressLint({"Recycle", "UseSparseArrays"})
    public List<String> getAllPipas(){
        List<String> lista = new ArrayList<>();
        String consulta = "   SELECT  idPipa AS _id, " +
                "           noPipa " +
                "   FROM    Pipas ";
        lista.add(0,"Seleccione");
        Cursor cursor = LoginActivity.conexion.rawQuery(consulta, null);
        spinnerMap = new HashMap<>();
        int contador=1;
        if (cursor.moveToFirst()){
            do {
                String pipa;
                pipa = "Pipa - " + cursor.getInt(1);
                spinnerMap.put(contador,cursor.getInt(0));
                lista.add(pipa);
                contador++;
            }while (cursor.moveToNext());

        }
        return lista;
    }

    @SuppressLint("Recycle")
    public Integer getCapacidadDiaAnteriorPipa(Integer idPipa){
        String consulta = "   SELECT  porcentajeLlenado " +
                "   FROM    Llenado ll, Pipas p " +
                "   WHERE   ll.fechaRegistro = " + utils.getFechaAnterior() +
                "   AND     ll.idPipa = p.idPipa" +
                "   AND     p.noPipa = " +idPipa;
        Cursor cursor = LoginActivity.conexion.rawQuery(consulta, null);
        Integer porcentajeLlenado = 0;
        if (cursor.moveToFirst()) {
            do {
               porcentajeLlenado = cursor.getInt(cursor.getColumnIndex("porcentajeLlenado"));
            } while (cursor.moveToNext());
        }
        return porcentajeLlenado;
    }
}