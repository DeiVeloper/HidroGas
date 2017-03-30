package mx.com.desoft.hidrogas.bussines;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.hidrogas.LoginActivity;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.LlenadoTO;

public class ReporteUnidadesBussines {

    @SuppressLint("Recycle")
    public List<LlenadoTO> getUnidadLlenadoByFecha(Long fechaBusqueda){
        List<LlenadoTO> lista = new ArrayList<>();
        String selectQuery = "    SELECT  liquidacion.idPipa, pipa.noPipa, liquidacion.fechaRegistro, liquidacion.variacion, liquidacion.porcentajeVariacion, pipa.clave " +
                "    FROM    Liquidacion liquidacion, " +
                "            Pipas pipa " +
                "    WHERE   liquidacion.idPipa = pipa.idPipa" +
                "    AND     liquidacion.alerta = 1 " +
                "    AND     liquidacion.fechaRegistro = " + fechaBusqueda;

        Cursor cursor = LoginActivity.conexion.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                LlenadoTO unidad = new LlenadoTO();
                unidad.setNoPipa(cursor.getInt(1));
                unidad.setFechaRegistro(cursor.getLong(2));
                unidad.setVariacion(cursor.getInt(3));
                unidad.setPorcentajeVariacion(cursor.getFloat(cursor.getColumnIndex("porcentajeVariacion")));
                unidad.setClave(cursor.getString(cursor.getColumnIndex("clave")));
                lista.add(unidad);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    @SuppressLint("Recycle")
    public ArrayList<LiquidacionesTO> getAllLiquidacionesByFecha(Long fechaBusqueda, int folioBusqueda){
        ArrayList<LiquidacionesTO> lista = new ArrayList<>();
        String selectQuery = "    SELECT  liquidacion.idLiquidacion, liquidacion.fechaRegistro, pipa.noPipa " +
                "    FROM    Liquidacion liquidacion, " +
                "            Pipas pipa " +
                "    WHERE   liquidacion.idPipa = pipa.idPipa" +
                (fechaBusqueda == 0 ? "" : " AND liquidacion.fechaRegistro = " + fechaBusqueda)+
                (folioBusqueda == 0 ? "" : " AND liquidacion.idLiquidacion = " + folioBusqueda);

        Cursor cursor = LoginActivity.conexion.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                lista.add(new LiquidacionesTO(cursor.getInt(0), cursor.getInt(2), cursor.getLong(1)));
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public void eliminarLiquidacion( LiquidacionesTO liquidacionTO) {
        LoginActivity.conexion.delete("Viajes", "idLiquidacion = " + liquidacionTO.getIdLiquidacion(), null);
        LoginActivity.conexion.delete("Liquidacion", "idLiquidacion = " + liquidacionTO.getIdLiquidacion(), null);
    }
}


