package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.desoft.adapter.ListLlenadoAdapter;
import mx.com.desoft.hidrogas.bussines.ReporteUnidadesBussines;
import mx.com.desoft.hidrogas.to.LlenadoTO;

/**
 * Created by David on 03/12/16.
 */

public class TapReportes extends Fragment{

    private TextView labelFechaBusqueda;
    private Button btnBuscar,btnExportarExcel, btnFechaBusqueda;
    private ListView listView;
    private View view;
    private ReporteUnidadesBussines reporteUnidadesBussines;
    private List<LlenadoTO> listaLlenado = new ArrayList<>();
    private Reportes reportes = new Reportes();
    private int year, month, day;
    private Long fechaBusqueda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_reportes, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        btnBuscar = (Button) view.findViewById(R.id.btnBuscar);
        btnExportarExcel = (Button) view.findViewById(R.id.btnExportarExcel);
        labelFechaBusqueda = (TextView) view.findViewById(R.id.labelFechaBusqueda);
        btnFechaBusqueda = (Button) view.findViewById(R.id.btnFechaBusqueda);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reporteUnidadesBussines = new ReporteUnidadesBussines(view.getContext());
                listaLlenado = reporteUnidadesBussines.getUnidadLlenadoByFecha(fechaBusqueda);
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

        btnFechaBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaActual();
                DatePickerDialog pik = new DatePickerDialog(view.getContext(), myDateListener, year, month, day);
                pik.show();

            }

        });
        return view;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
             fecha = formato.parse(day+"/"+month+"/"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fechaBusqueda = fecha.getTime();
        labelFechaBusqueda.setText(formato.format(fecha));
    }

    private void getFechaActual(){
        final Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);
    }
}
