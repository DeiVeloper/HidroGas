package mx.com.desoft.hidrogas.bussines;

import android.database.Cursor;

import mx.com.desoft.hidrogas.LoginActivity;

public class CatalogoBussines {
    public CatalogoBussines() {

    }
    public Cursor getTipoEmpleado() {
        return LoginActivity.conexion.rawQuery("SELECT * FROM tipoEmpleados WHERE 1=1 ", null);
    }
}
