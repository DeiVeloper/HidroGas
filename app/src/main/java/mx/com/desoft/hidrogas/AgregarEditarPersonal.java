package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.desoft.hidrogas.bussines.CatalogoBussines;
import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by David on 03/12/16.
 */

public class AgregarEditarPersonal extends Activity {
    private EditText txtNomina, txtNombre, txtAPaterno, txtAMaterno, txtPass;
    private Button btnGuardar, btnCancelar;
    private PersonalTO personalTO;
    private PersonalBussines personalBussines;
    private boolean flgEditar;
    private Spinner sprTipoEmpleado, sprPipa;
    private CatalogoBussines catalogoBussines;
    private Long fecha;
    private KeyListener keyListenerPass;
    private Bundle bundle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        personalTO = new PersonalTO();
        catalogoBussines = new CatalogoBussines();
        flgEditar = false;
        personalBussines = new PersonalBussines();
        bundle = getIntent().getExtras();
        inicializarComponentes();
        cargarEventos();
    }

    private void inicializarComponentes() {
        //acceder a los item de la vista
        txtNomina = (EditText)findViewById(R.id.txtNoNomina);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtAPaterno = (EditText)findViewById(R.id.txtApellidoPaterno);
        txtAMaterno = (EditText)findViewById(R.id.txtApellidoMaterno);
        txtPass = (EditText)findViewById(R.id.txtPass);
        keyListenerPass = txtPass.getKeyListener();
        sprPipa = (Spinner) findViewById(R.id.sprPipa);
        sprTipoEmpleado = (Spinner) findViewById(R.id.sprPuesto);

        ArrayAdapter<Integer> spinnerAdapterPipas;
        List<Integer> pipas = new ArrayList<>();
        Cursor registroPipas = catalogoBussines.getPipas(getApplicationContext());
        if (registroPipas.moveToFirst()) {
            do {
                pipas.add(registroPipas.getInt(0));
            } while (registroPipas.moveToNext());
            spinnerAdapterPipas = new ArrayAdapter<Integer>(AgregarEditarPersonal.this, android.R.layout.simple_list_item_1, pipas);
            sprPipa.setAdapter(spinnerAdapterPipas);
        }

        ArrayAdapter<String> spinnerAdapter;
        List<String> tipoEmpleados = new ArrayList<>();
        Cursor registros = catalogoBussines.getTipoEmpleado(getApplicationContext());
        if (registros.moveToFirst()) {
            do {
                tipoEmpleados.add(registros.getString(1));
            } while (registros.moveToNext());
            spinnerAdapter = new ArrayAdapter<String>(AgregarEditarPersonal.this, android.R.layout.simple_list_item_1, tipoEmpleados);
            sprTipoEmpleado.setAdapter(spinnerAdapter);
        }

        //obtener datos que se pasaron en el Intent para editar
        if(bundle != null) {
            flgEditar = true;
            if (bundle.containsKey("nomina")) {
                txtNomina.setText(bundle.getString("nomina"));
                txtNomina.setKeyListener(null);
            }
            if (bundle.containsKey("nombre")) {
                txtNombre.setText(bundle.getString("nombre"));
            }
            if (bundle.containsKey("aPaterno")) {
                txtAPaterno.setText(bundle.getString("aPaterno"));
            }
            if (bundle.containsKey("aMaterno")) {
                txtAMaterno.setText(bundle.getString("aMaterno"));
            }
            if (bundle.containsKey("tipoEmpleado")) {
                sprTipoEmpleado.setSelection(bundle.getInt("tipoEmpleado"));
            }
        } else {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date soloFecha = formato.parse(formato.format(new Date()));
                fecha = soloFecha.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    private void cargarEventos() {
        sprTipoEmpleado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    sprPipa.setEnabled(true);
                    sprPipa.setClickable(true);
                    txtPass.setText("");
                    txtPass.setKeyListener(null);
                } else {
                    sprPipa.setEnabled(false);
                    sprPipa.setClickable(false);
                    txtPass.setKeyListener(keyListenerPass);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar(view);
            }
        });
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void guardar(View view) {
        try {
            boolean resultadoGuardar;
            if (TextUtils.isEmpty(txtNomina.getText().toString()) || TextUtils.isEmpty(txtNombre.getText().toString()) || TextUtils.isEmpty(txtAPaterno.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Los datos: Nómina, Nombre y Apellido paterno son obligatorios.", Toast.LENGTH_SHORT).show();
            } else if (sprTipoEmpleado.getSelectedItemId() == 0 && TextUtils.isEmpty(txtPass.getText().toString())){
                Toast.makeText(getApplicationContext(), "La contraseña es obligatoria.", Toast.LENGTH_SHORT).show();
            } else {
                personalTO.setNomina(txtNomina.getText().toString());
                personalTO.setNombre(txtNombre.getText().toString());
                personalTO.setApellidoPaterno(txtAPaterno.getText().toString());
                personalTO.setApellidoMaterno(txtAMaterno.getText().toString());
                personalTO.setPassword(txtPass.getText().toString());
                personalTO.setNoPipa((int) sprPipa.getSelectedItemId());
                personalTO.setFechaRegistro(fecha);
                personalTO.setNominaRegistro("203040");
                personalTO.setTipoEmpleado((int) sprTipoEmpleado.getSelectedItemId());
                resultadoGuardar = personalBussines.guardar(getApplicationContext(), personalTO, flgEditar);
                if (resultadoGuardar) {
                    Toast.makeText(getApplicationContext(), "El usuario con nómina: " + personalTO.getNomina() + " se ha guardado correctamente.", Toast.LENGTH_SHORT).show();
                    onCleanForm();
                } else {
                    Toast.makeText(getApplicationContext(), "El usuario con nómina: " + personalTO.getNomina() + " ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al guardar el trabajador: " + personalTO.getNombre() + ".", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCleanForm() {
        txtNomina.setText("");
        txtNombre.setText("");
        txtAPaterno.setText("");
        txtAMaterno.setText("");
        txtPass.setText("");
    }
}
