package mx.com.desoft.hidrogas;

/**
 * Created by David on 30/11/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    AdminSQLiteOpenHelper bd;
    Activity activity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        bd = new AdminSQLiteOpenHelper(this);
        //TextView registerScreen = (TextView) findViewById(R.id.btnLinkToRegisterScreen);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnAgregarRegistro = (Button) findViewById(R.id.btnAgregarRegistro);


        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                List<Empleado> empleado = bd.getContact();
                for (Empleado row: empleado){

                    Log.d("Datos: ", row.toString());
                    //Toast toast = Toast.makeText(activity,"DAtos : " + row.toString(), Toast.LENGTH_LONG);
                    //toast.show();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        btnAgregarRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                EditText usuario = (EditText) findViewById(R.id.email);
                EditText password = (EditText) findViewById(R.id.password);
                bd.addContact(usuario.getText().toString(),password.getText().toString());

                Toast toast = Toast.makeText(activity, "Usuario:" + usuario.getText() + " Password: " + password.getText(), Toast.LENGTH_LONG);
                toast.show();
            }
        });


        // Listening to register new account link
        /*registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });*/
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
