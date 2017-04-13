package mx.com.desoft.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mx.com.desoft.utils.Utils;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private Utils utils = new Utils();
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "hidrogas.db";


    private static final String SQL_CREAR_EMPLEADOS = "CREATE TABLE Empleados(nominaEmpleado INTEGER PRIMARY KEY, nombre TEXT NOT NULL," +
            "apellidoPaterno TEXT NOT NULL, apellidoMaterno TEXT, password TEXT, idPipa INTEGER NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL," +
            "nominaRegistro TEXT NOT NULL, tipoEmpleado INTEGER NOT NULL DEFAULT 1)";

    private static final String SQL_CREAR_LIQUIDACION = "CREATE TABLE Liquidacion(idLiquidacion INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nominaChofer TEXT NOT NULL, nominaAyudante TEXT NOT NULL, idPipa INTEGER NOT NULL DEFAULT 0, alerta INTEGER NULL DEFAULT 0, " +
            "variacion INTEGER NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL, porcentajeVariacion REAL NOT NULL, " +
            "economico TEXT NOT NULL, autoconsumo INTEGER NULL, medidores INTEGER NULL, traspasosRecibidos INTEGER NULL, traspasosRealizados INTEGER NULL)";

    private static final String SQL_CREAR_LLENADO = "CREATE TABLE Llenado(idLlenado INTEGER PRIMARY KEY AUTOINCREMENT, idPipa INTEGER NOT NULL," +
            "porcentajeLlenado INTEGER NOT NULL, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL)";

    private static final String SQL_CREAR_PIPAS = "CREATE TABLE Pipas (idPipa INTEGER PRIMARY KEY AUTOINCREMENT, noPipa INTEGER NOT NULL, capacidad INTEGER NOT NULL, " +
            "totalizador INTEGER NULL DEFAULT 0, fechaRegistro INTEGER NOT NULL, nominaRegistro TEXT NOT NULL, clave TEXT NULL)";

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
        this.addTipoEmpleados(db);
        this.addContact(db);
    }

    private void addContact(SQLiteDatabase db) throws SQLiteConstraintException {
        ContentValues registro = new ContentValues();
        registro.put("nominaEmpleado", 2018);
        registro.put("nombre", "Roberto");
        registro.put("apellidoPaterno", "Nuñez");
        registro.put("apellidoMaterno", "Luna");
        registro.put("password", "1698");
        registro.put("fechaRegistro", utils.getFechaActual());
        registro.put("nominaRegistro", "1698");
        registro.put("tipoEmpleado", 0);
        db.insert("Empleados", null, registro);

        registro = new ContentValues();
        registro.put("nominaEmpleado", 8888);
        registro.put("nombre", "Rodolfo");
        registro.put("apellidoPaterno", "Rodriguez");
        registro.put("apellidoMaterno", "");
        registro.put("password", "1965");
        registro.put("fechaRegistro", utils.getFechaActual());
        registro.put("nominaRegistro", "1698");
        registro.put("tipoEmpleado", 0);
        db.insert("Empleados", null, registro);

        registro = new ContentValues();
        registro.put("nominaEmpleado", 9999);
        registro.put("nombre", "Ricardo");
        registro.put("apellidoPaterno", "Nava");
        registro.put("apellidoMaterno", "");
        registro.put("password", "2000");
        registro.put("fechaRegistro", utils.getFechaActual());
        registro.put("nominaRegistro", "1698");
        registro.put("tipoEmpleado", 0);
        db.insert("Empleados", null, registro);

        registro = new ContentValues();
        registro.put("nominaEmpleado", 1234);
        registro.put("nombre", "Antonio");
        registro.put("apellidoPaterno", "Lopez");
        registro.put("apellidoMaterno", "");
        registro.put("password", "1234");
        registro.put("fechaRegistro", utils.getFechaActual());
        registro.put("nominaRegistro", "1698");
        registro.put("tipoEmpleado", 0);
        db.insert("Empleados", null, registro);

    }

    private void addTipoEmpleados(SQLiteDatabase db){
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}

