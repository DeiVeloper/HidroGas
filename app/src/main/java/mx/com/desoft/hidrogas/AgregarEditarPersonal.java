package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.desoft.hidrogas.bussines.CatalogoBussines;
import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.utils.Utils;

public class AgregarEditarPersonal extends Activity {
    private EditText txtNomina, txtNombre, txtAPaterno, txtAMaterno, txtPass;
    private PersonalTO personalTO;
    private PersonalBussines personalBussines;
    private boolean flgEditar;
    private Spinner sprTipoEmpleado, sprPipa;
    private CatalogoBussines catalogoBussines;
    private PipasBussines pipasBussines;
    private Long fecha;
    private KeyListener keyListenerPass;
    private Bundle bundle;
    private Integer tipoEmpleadoInicial;
    private Utils utils;
    private Integer idPipa;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        utils = new Utils();
        personalTO = new PersonalTO();
        catalogoBussines = new CatalogoBussines();
        flgEditar = false;
        personalBussines = new PersonalBussines();
        pipasBussines = new PipasBussines();
        tipoEmpleadoInicial = 0;
        bundle = getIntent().getExtras();
        inicializarComponentes();
        cargarEventos();
    }

    private void inicializarComponentes() {
        txtNomina = (EditText)findViewById(R.id.txtNoNomina);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtAPaterno = (EditText)findViewById(R.id.txtApellidoPaterno);
        txtAMaterno = (EditText)findViewById(R.id.txtApellidoMaterno);
        txtPass = (EditText)findViewById(R.id.txtPass);
        keyListenerPass = txtPass.getKeyListener();
        sprPipa = (Spinner) findViewById(R.id.sprPipa);
        sprTipoEmpleado = (Spinner) findViewById(R.id.sprPuesto);

        ArrayAdapter<String> spinnerAdapterPipas;
        List<String> listSpinner = pipasBussines.getAllPipas();
        spinnerAdapterPipas = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listSpinner);
        spinnerAdapterPipas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprPipa.setAdapter(spinnerAdapterPipas);
        ArrayAdapter<String> spinnerAdapter;
        List<String> tipoEmpleados = new ArrayList<>();
        Cursor registros = catalogoBussines.getTipoEmpleado();
        if (registros.moveToFirst()) {
            do {
                tipoEmpleados.add(registros.getString(1));
            } while (registros.moveToNext());
            spinnerAdapter = new ArrayAdapter<>(AgregarEditarPersonal.this, android.R.layout.simple_list_item_1, tipoEmpleados);
            sprTipoEmpleado.setAdapter(spinnerAdapter);
        }

        if(bundle != null) {
            flgEditar = true;
            if (bundle.containsKey("nomina")) {
                txtNomina.setText(((Integer)bundle.getInt("nomina")).toString());
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
                tipoEmpleadoInicial = bundle.getInt("tipoEmpleado");
            }
        } else {
            fecha = utils.consultaFechaLong(new Date(), "dd/MM/yyyy");
        }
    }

    private void cargarEventos() {
        sprPipa.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,View v, int position, long id) {
                        idPipa = PipasBussines.spinnerMap.get(parent.getSelectedItemPosition());
                    }
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

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
            }
        });

        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean guardado = guardar();
                if (guardado){
                    returnTab(1);
                }

            }
        });

        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnTab(1);
            }
        });
    }

    private boolean guardar() {
        try {
            boolean resultadoGuardar = true;
            if (TextUtils.isEmpty(txtNomina.getText().toString()) || TextUtils.isEmpty(txtNombre.getText().toString()) || TextUtils.isEmpty(txtAPaterno.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Los datos: Nómina, Nombre y Apellido paterno son obligatorios.", Toast.LENGTH_SHORT).show();
            } else if (sprTipoEmpleado.getSelectedItemId() == 0 && TextUtils.isEmpty(txtPass.getText().toString())){
                Toast.makeText(getApplicationContext(), "La contraseña es obligatoria.", Toast.LENGTH_SHORT).show();
            } else {
                if (sprTipoEmpleado.getSelectedItemId() != 0) {
                    if (idPipa != null){
                        resultadoGuardar = verificarPipa(idPipa, (int) sprTipoEmpleado.getSelectedItemId());
                    }   else    {
                        Toast.makeText(getApplicationContext(), "No se ha seleccionado una pipa, favor de validar.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                if (resultadoGuardar) {
                    personalTO.setNomina(Integer.parseInt(txtNomina.getText().toString()));
                    personalTO.setNombre(txtNombre.getText().toString());
                    personalTO.setApellidoPaterno(txtAPaterno.getText().toString());
                    personalTO.setApellidoMaterno(txtAMaterno.getText().toString());
                    personalTO.setPassword(txtPass.getText().toString());

                    personalTO.setNoPipa(idPipa);
                    personalTO.setFechaRegistro(fecha);
                    personalTO.setNominaRegistro(LoginActivity.personalTO.getNomina());
                    personalTO.setTipoEmpleado((int) sprTipoEmpleado.getSelectedItemId());
                    resultadoGuardar = personalBussines.guardar(personalTO, flgEditar);
                    if (resultadoGuardar) {
                        Toast.makeText(getApplicationContext(), "El usuario con nómina: " + personalTO.getNomina() + " se ha guardado correctamente.", Toast.LENGTH_SHORT).show();
                        onCleanForm();
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), "El usuario con nómina: " + personalTO.getNomina() + " ya existe.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "La pipa: " + sprPipa.getSelectedItem() + " ya tiene un " + sprTipoEmpleado.getSelectedItem() + " asignado.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al guardar el trabajador: " + txtNombre.getText() + ".", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean verificarPipa(Integer pipa, Integer tipoEmpleado){
        Cursor resgistroChoferAyudante = pipasBussines.buscarChoferAyudanteByNoPipa(pipa);
        if (resgistroChoferAyudante.moveToFirst()) {
            do {
                if (tipoEmpleado == resgistroChoferAyudante.getInt(8)) {
                    return flgEditar && tipoEmpleadoInicial == resgistroChoferAyudante.getInt(8);
                }
            } while (resgistroChoferAyudante.moveToNext());
        }
        return true;
    }

    public void onCleanForm() {
        txtNomina.setText("");
        txtNombre.setText("");
        txtAPaterno.setText("");
        txtAMaterno.setText("");
        txtPass.setText("");
    }

    private void returnTab(int posicion){
        Intent go = new Intent(getApplicationContext(), MainActivity.class);
        go.putExtra("viewpager_position", posicion);
        startActivity(go);
    }
}
