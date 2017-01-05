package mx.com.desoft.hidrogas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.adapter.ListLlenadoAdapter;
import mx.com.desoft.hidrogas.bussines.ReporteUnidadesBussines;
import mx.com.desoft.hidrogas.to.LlenadoTO;

/**
 * Created by David on 03/12/16.
 */

public class TapReportes extends Fragment{

    private EditText editTextFechaBusqueda;
    private Button btnBuscar,btnExportarExcel;
    private ListView listView;
    private ReporteUnidadesBussines reporteUnidadesBussines;
    private List<LlenadoTO> listaLlenado = new ArrayList<>();
    private Reportes reportes = new Reportes();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_reportes, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        btnBuscar = (Button) view.findViewById(R.id.btnBuscar);
        btnExportarExcel = (Button) view.findViewById(R.id.btnExportarExcel);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFechaBusqueda = (EditText) view.findViewById(R.id.editFechaBusqueda);
                reporteUnidadesBussines = new ReporteUnidadesBussines(view.getContext());
                listaLlenado = reporteUnidadesBussines.getUnidadLlenadoByFecha(Integer.valueOf(editTextFechaBusqueda.getText().toString()));
                Toast.makeText(view.getContext(), "Fui a buscar las unidades", Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "Clicked: "+names.get(position), Toast.LENGTH_LONG).show();
            }
        });

        // Enlazamos con nuestro adaptador personalizado
        ListLlenadoAdapter listAdapter = new ListLlenadoAdapter(view.getContext(),R.id.list_item, listaLlenado);
        listView.setAdapter(listAdapter);

        btnExportarExcel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                try {
                    String ruta = reportes.excel(view);
                    Toast.makeText(view.getContext(), "El reporte se creo en la siguiente ruta: " + ruta, Toast.LENGTH_LONG).show();
                }catch (Exception  e)   {
                    Log.d("Error " + e.getStackTrace()," , Mensaje "+ e.getMessage());
                    Toast.makeText(view.getContext(), "No se pudo crear al excel, favor de contactar al Administrador", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}
