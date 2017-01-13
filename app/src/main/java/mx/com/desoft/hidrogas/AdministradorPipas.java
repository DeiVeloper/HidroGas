package mx.com.desoft.hidrogas;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by erick.martinez on 25/11/2016.
 */

public class AdministradorPipas extends Activity {
    Button /*btnEditar,*/ btnEliminar, btnAgregar, btnRegresar, btnBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador_pipas);
        //btnEditar = (Button)findViewById(R.id.btnEditar);
        //btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnAgregar = (Button)findViewById(R.id.btnAgregar);
        btnRegresar = (Button)findViewById(R.id.btnRegresar);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);

        /*btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(LiquidacionUnidades.class);
            }
        });*/
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(AdministradorPipas.class);
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(AgregarPipas.class);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(AdministradorPipas.class);
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(AdministradorPipas.class);
            }
        });
    }

    public void chargePage(Class clase){
        Intent btnAccion = new Intent (this, clase);
        startActivity(btnAccion);
    }
}

