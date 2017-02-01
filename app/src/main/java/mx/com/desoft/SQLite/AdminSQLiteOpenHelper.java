package mx.com.desoft.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.desoft.hidrogas.model.Empleado;

/**
 * Created by erick.martinez on 14/12/2016.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hidrogas.db";

    private Calendar calendar = Calendar.getInstance();

    private static final String SQL_CREAR_EMPLEADOS = "CREATE TABLE Empleados(nominaEmpleado INTEGER PRIMARY KEY, nombre TEXT NOT NULL," +
            "apellidoPaterno TEXT NOT NULL, apellidoMaterno TEXT, password TEXT, idPipa INTEGER NOT NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL," +
            "nominaRegistro TEXT NOT NULL, tipoEmpleado INTEGER NOT NULL DEFAULT 1)";

    private static final String SQL_CREAR_LIQUIDACION = "CREATE TABLE Liquidacion(idLiquidacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nominaChofer TEXT NOT NULL, nominaAyudante TEXT NOT NULL, idPipa INTEGER NOT NULL DEFAULT 0, alerta INTEGER NULL DEFAULT 0, " +
            "variacion INTEGER NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL)";

    private static final String SQL_CREAR_LLENADO = "CREATE TABLE Llenado(idLlenado INTEGER PRIMARY KEY AUTOINCREMENT, idPipa INTEGER NOT NULL," +
            "porcentajeLlenado INTEGER NOT NULL, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL)";

    private static final String SQL_CREAR_PIPAS = "CREATE TABLE Pipas (idPipa INTEGER PRIMARY KEY AUTOINCREMENT, noPipa INTEGER NOT NULL, fechaRegistro INTEGER NOT NULL," +
            "nominaRegistro TEXT NOT NULL, capacidad INTEGER NULL DEFAULT 0)";

    private static final String SQL_CREAR_TIPOEMPLEADOS = "CREATE TABLE TipoEmpleados (idEmpleado INTEGER PRIMARY KEY, descripcion TEXT)";

    private static final String SQL_CREAR_VIAJES = "CREATE TABLE Viajes (idViaje INTEGER PRIMARY KEY AUTOINCREMENT, idLiquidacion INTEGER NOT NULL," +
            "porcentajeInicial INTEGER NOT NULL DEFAULT 0, totalizadorInicial INTEGER NOT NULL DEFAULT 0, porcentajeFinal INTEGER NOT NULL DEFAULT 0," +
            "totalizadorFinal INTEGER NOT NULL DEFAULT 0)";

    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_EMPLEADOS);
        db.execSQL(SQL_CREAR_LIQUIDACION);
        db.execSQL(SQL_CREAR_LLENADO);
        db.execSQL(SQL_CREAR_PIPAS);
        db.execSQL(SQL_CREAR_VIAJES);
        db.execSQL(SQL_CREAR_TIPOEMPLEADOS);

        ContentValues registro = new ContentValues();
        registro.put("idEmpleado", 0);
        registro.put("descripcion", "Administrador");
        db.insert("tipoEmpleados", null, registro);
        registro = new ContentValues();
        registro.put("idEmpleado", 1);
        registro.put("descripcion", "Chofer");
        db.insert("tipoEmpleados", null, registro);
        registro = new ContentValues();
        registro.put("idEmpleado", 2);
        registro.put("descripcion", "Ayudante");
        db.insert("tipoEmpleados", null, registro);
    }

    public void addContact(String usuario, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", usuario);
        registro.put("nombre", "Carlos David");
        registro.put("apellidoPaterno", "Castro");
        registro.put("apellidoMaterno", "Aguilar");
        registro.put("password", password);
        registro.put("idPipa", 1);
        registro.put("fechaRegistro", this.getFechaActual());
        registro.put("nominaRegistro", "130191");
        registro.put("tipoEmpleado", 1);

        // Inserting Row
        db.insert("Empleados", null, registro);

        ContentValues llenado = new ContentValues();
        llenado.put("noPipa", 701);
        llenado.put("fechaRegistro", this.getFechaActual());
        llenado.put("nominaRegistro", usuario);
        llenado.put("capacidad", 5800);
        db.insert("Pipas", null, llenado);

        db.close(); // Closing database connection
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private Long getFechaActual(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day-1);
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = formato.parse(formato.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha.getTime();
    }
}

