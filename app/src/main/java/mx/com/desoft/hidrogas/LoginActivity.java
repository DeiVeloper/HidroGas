package mx.com.desoft.hidrogas;

/**
 * Created by David on 30/11/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.model.Empleado;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences preferences;
    AdminSQLiteOpenHelper dataBase;

    private EditText editTextUsuario;
    private EditText editTextPassword;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        bindUI();
        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String usuario = editTextUsuario.getText().toString();
                String password = editTextPassword.getText().toString();
                if(isFormValid(usuario,password)){
                    goToMain();
                    saveOnPreferences(usuario,password);

                }
            }
        });

        /*btnAgregarRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                bd.addContact(usuario.getText().toString(),password.getText().toString());

                Toast toast = Toast.makeText(activity, "Usuario:" + usuario.getText() + " Password: " + password.getText(), Toast.LENGTH_LONG);
                toast.show();
            }
        });*/
    }

    private void saveOnPreferences(String usuario, String password){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario);// Usuario que se enceuntra en la base de datos y es administrador
        editor.putString("password", password); // contraseña del usuario logueado
        editor.apply();
    }

    private void conectarBaseDeDatos(){
        dataBase = new AdminSQLiteOpenHelper(this);
    }

    private void bindUI(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        editTextUsuario = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
    }

    private void goToMain(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean isFormValid(String usuario, String password){
        if (TextUtils.isEmpty(usuario)){
            Toast.makeText(this, "El Usuario no puede ir vacio, favor de validar", Toast.LENGTH_LONG).show();
            return false;

        }   else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "La contraseña no puede ir vacia, favor de validar", Toast.LENGTH_LONG).show();
            return false;
        }

        return getUsarioLogin(usuario,password);
    };

    private boolean getUsarioLogin(String usuario, String password){
        // consulta para traer el usuario de la base de datos
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.liquidacionUnidades:
                setContentView(R.layout.activity_liquidacion);
                break;
            case R.id.llenadoPipas:
                setContentView(R.layout.activity_llenado_pipas);
                break;
            case R.id.personal:
                setContentView(R.layout.activity_personal);
                break;
            case R.id.administradorPipas:
                setContentView(R.layout.activity_administrador_pipas);
                break;
            case R.id.reportes:
                setContentView(R.layout.activity_reportes);
                break;
        }
        return true;
    }
}
