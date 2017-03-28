package mx.com.desoft.hidrogas;

/**
 * Created by David on 30/11/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.com.desoft.SQLite.AdminSQLiteOpenHelper;
import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.to.PersonalTO;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences preferences;
    AdminSQLiteOpenHelper dataBase;

    private EditText editTextUsuario;
    private EditText editTextPassword;
    private Button btnLogin;
    private PersonalBussines  personalBussines = new PersonalBussines();
    public static PersonalTO personalTO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dataBase  =new AdminSQLiteOpenHelper(this.getApplicationContext());
        dataBase.addContact("130191","Dc13#");
        bindUI();
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExist();

        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Integer usuario = Integer.valueOf(editTextUsuario.getText().toString());
                String password = editTextPassword.getText().toString();
                if(isFormValid(usuario.toString(),password)){
                    goToMain();
                    saveOnPreferences(usuario.toString(),password);
                    Toast.makeText(getApplication(), "Bienvenido "+personalTO.getNombre()+" "+personalTO.getApellidoPaterno()+" "+personalTO.getApellidoMaterno(), Toast.LENGTH_SHORT).show();
                    }   else    {
                        Toast.makeText(getApplication(), "Usuario y/o contraseña incorrectos, favor de validar", Toast.LENGTH_LONG).show();
                    }
            }
        });

    }

    private void saveOnPreferences(String usuario, String password){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario);// Usuario que se enceuntra en la base de datos y es administrador
        editor.putString("password", password); // contraseña del usuario logueado
        editor.apply();
    }

    private void setCredentialsIfExist(){
        String usuario = preferences.getString("usuario", "");
        String password = preferences.getString("password", "");
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(password)) {
            editTextUsuario.setText(usuario);
            //editTextPassword.setText(password);
        }
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
            Toast.makeText(this, "Favor de capturar un nombre de Usuario", Toast.LENGTH_LONG).show();
            return false;

        }   else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Favor de capturar su Contraseña", Toast.LENGTH_LONG).show();
            return false;
        }

        return getUsarioLogin(Integer.valueOf(usuario), password);
    };

    private boolean getUsarioLogin(Integer usuario, String password){
        personalTO = personalBussines.getUserDataBase(getApplication(), usuario, password);
        return personalTO != null ?  true : false;
    }
}
