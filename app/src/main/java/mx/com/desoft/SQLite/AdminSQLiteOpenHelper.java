package mx.com.desoft.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mx.com.desoft.hidrogas.model.Empleado;

/**
 * Created by erick.martinez on 14/12/2016.
 */

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hidrogas.db";

    private static final String SQL_CREAR_EMPLEADOS = "CREATE TABLE Empleados(nominaEmpleado TEXT PRIMARY KEY, nombre TEXT NOT NULL," +
            "apellidoPaterno TEXT NOT NULL, apellidoMaterno TEXT, password TEXT, noPipa INTEGER NOT NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL," +
            "nominaRegistro TEXT NOT NULL, tipoEmpleado INTEGER NOT NULL DEFAULT 1)";

    private static final String SQL_CREAR_LIQUIDACION = "CREATE TABLE Liquidacion(idLiquidacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nominaChofer TEXT NOT NULL, nominaAyudante TEXT NOT NULL, noPipa INTEGER NOT NULL DEFAULT 0," +
            "variacion INTEGER NOT NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL)";

    private static final String SQL_CREAR_LLENADO = "CREATE TABLE Llenado(idLlenado INTEGER PRIMARY KEY AUTOINCREMENT, noPipa INTEGER NOT NULL," +
            "porcentajeLlenado INTEGER NOT NULL, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL)";

    private static final String SQL_CREAR_PIPAS = "CREATE TABLE Pipas (noPipa INTEGER PRIMARY KEY AUTOINCREMENT, fechaRegistro INTEGER NOT NULL," +
            "nominaRegistro TEXT NOT NULL)";

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
        registro.put("noPipa", 1);
        registro.put("fechaRegistro", getFechaActual());
        registro.put("nominaRegistro", "130191");
        registro.put("tipoEmpleado", 0);

        // Inserting Row
        db.insert("Empleados", null, registro);
        db.close(); // Closing database connection
    }

    public List<Empleado> getContact() {
        List<Empleado> contactList = new ArrayList<Empleado>();
        // Select All Query
        String selectQuery = "SELECT nominaEmpleado, nombre, apellidoPaterno, apellidoMaterno, password, noPipa, fechaRegistro, nominaRegistro, tipoEmpleado FROM Empleados";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Empleado contact = new Empleado();
                contact.setNominaEmpleado(cursor.getString(0));
                contact.setNombre(cursor.getString(1));
                contact.setApellidoPaterno(cursor.getString(2));
                contact.setApellidoMaterno(cursor.getString(3));
                contact.setPassword(cursor.getString(4));
                //contact.setNoPipa(Integer.valueOf(cursor.getColumnName(5).toString()).intValue());
                //contact.setFechaRegistro();
                contact.setNominaRegistro(cursor.getColumnName(7));
                //contact.setTipoEmpleado(Integer.parseInt(cursor.getColumnName(8)));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private Integer getFechaActual(){
        Calendar fecha = Calendar.getInstance();
        int year = fecha.get(Calendar.YEAR);
        int month = fecha.get(Calendar.MONTH) + 1;
        int day = fecha.get(Calendar.DAY_OF_MONTH);
        return Integer.valueOf(day+""+month+""+year);
    }
}

