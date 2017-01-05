package mx.com.desoft.hidrogas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import mx.com.desoft.adapter.ListLlenadoAdapter;
import mx.com.desoft.hidrogas.bussines.ReporteUnidadesBussines;
import mx.com.desoft.hidrogas.to.LlenadoTO;

/**
 * Created by David on 03/12/16.
 */

public class TapReportes extends Fragment{

    private EditText editTextFechaBusqueda;
    private ListView listView;
    private ReporteUnidadesBussines reporteUnidadesBussines;
    private List<LlenadoTO> listaLlenado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reportes, container, false);

        editTextFechaBusqueda = (EditText) view.findViewById(R.id.editFechaBusqueda);
        listView = (ListView) view.findViewById(R.id.listView);
        reporteUnidadesBussines = new ReporteUnidadesBussines();
        listaLlenado = reporteUnidadesBussines.getUnidadLlenadoByFecha(Integer.valueOf(editTextFechaBusqueda.getText().toString()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "Clicked: "+names.get(position), Toast.LENGTH_LONG).show();
            }
        });

        // Enlazamos con nuestro adaptador personalizado
        ListLlenadoAdapter listAdapter = new ListLlenadoAdapter(view.getContext(),R.id.list_item, listaLlenado);
        listView.setAdapter(listAdapter);
        return view;
    }
}
