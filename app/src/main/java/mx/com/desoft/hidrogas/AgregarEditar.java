package mx.com.desoft.hidrogas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by David on 03/12/16.
 */

public class AgregarEditar extends Fragment {
    private EditText txtNomina, txtNombre, txtAPaterno, txtAMaterno, txtPass;
    private Button btnGuardar;
    private ViewGroup viewGroup;
    private PersonalTO personalTO;
    private PersonalBussines personalBussines;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_personal, container, false);
        personalTO = new PersonalTO();
        personalBussines = new PersonalBussines();
        txtNomina = (EditText)viewGroup.findViewById(R.id.txtNoNomina);
        txtNombre = (EditText)viewGroup.findViewById(R.id.txtNombre);
        txtAPaterno = (EditText)viewGroup.findViewById(R.id.txtApellidoPaterno);
        txtAMaterno = (EditText)viewGroup.findViewById(R.id.txtApellidoMaterno);
        txtPass = (EditText)viewGroup.findViewById(R.id.txtPass);
        btnGuardar = (Button)viewGroup.findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar(view);
            }
        });
        return viewGroup;
    }
    private void guardar(View view) {
        personalTO.setNomina(txtNomina.getText().toString());
        personalTO.setNombre(txtNombre.getText().toString());
        personalTO.setApellidoPaterno(txtAPaterno.getText().toString());
        personalTO.setApellidoMaterno(txtAMaterno.getText().toString());
        personalTO.setPassword(txtPass.getText().toString());
        personalTO.setNoPipa(1);
        personalTO.setFechaRegistro(1L);
        personalTO.setNominaRegistro("203040");
        personalTO.setTipoEmpleado(1);
        personalBussines.guardar(view.getContext(), personalTO,false);
        Toast.makeText(viewGroup.getContext(), "El usuario con nómina: " + personalTO.getNomina() + " se ha registrado correctamente.", Toast.LENGTH_SHORT).show();
    }
}
