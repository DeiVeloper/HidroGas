package mx.com.desoft.hidrogas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.com.desoft.adapter.AdapterPipas;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PipasTO;

public class ListaPipas extends Fragment {
    private ViewGroup viewGroup;
    private PipasTO pipasTO;
    private PipasBussines pipasBussines;
    private AdapterPipas adapterPipas;
    EditText txtPipa;
    Button btnAgregar, btnBuscar;
    ArrayList<PipasTO> pipasTOArray;
    ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pipasTO = new PipasTO();
        pipasBussines = new PipasBussines();
        pipasTOArray = new ArrayList<PipasTO>();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_administrador_pipas, container, false);
        btnAgregar = (Button)viewGroup.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accion = new Intent (viewGroup.getContext(), AgregarPipas.class);
                chargePage(accion);
            }
        });
        txtPipa = (EditText) viewGroup.findViewById(R.id.txtPipa);
        btnBuscar = (Button)viewGroup.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });
        return viewGroup;
    }

    private void buscar() {
        Integer pipa = 0;
        if (!TextUtils.isEmpty(txtPipa.getText().toString())){
            pipa = Integer.parseInt (txtPipa.getText().toString());
        }
        Cursor registros = pipasBussines.buscarByNoPipa(viewGroup.getContext(), pipa);
        if (registros.moveToFirst()) {
            do {
                pipasTOArray.add(new PipasTO(registros.getInt(0), registros.getInt(1), registros.getString(2)));
            } while (registros.moveToNext());
            this.adapterPipas = new AdapterPipas(viewGroup.getContext(), R.layout.list_items_pipas, pipasTOArray);
            listView = (ListView)viewGroup.findViewById(R.id.lstPipas);
            listView.setItemsCanFocus(false);
            listView.setAdapter(adapterPipas);
        } else {
            Toast.makeText(viewGroup.getContext(), "Su búsqueda no tiene registros asociados.", Toast.LENGTH_SHORT).show();
        }
    }

    public void chargePage(Intent accion){
        startActivity(accion);
    }
}
