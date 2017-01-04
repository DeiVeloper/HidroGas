package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by erick.martinez on 25/11/2016.
 */

public class ListaPersonal extends Activity {
    Button btnEditar, btnEliminar, btnRegresar, btnBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litado_personal);
        btnEditar = (Button)findViewById(R.id.btnEditar);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnRegresar = (Button)findViewById(R.id.btnRegresar);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(AgregarEditarPersonal.class);
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(ListaPersonal.class);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(ListaPersonal.class);
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(ListaPersonal.class);
            }
        });
    }

    public void chargePage(Class clase){
        Intent btnAccion = new Intent (this, clase);
        startActivity(btnAccion);
    }
}
