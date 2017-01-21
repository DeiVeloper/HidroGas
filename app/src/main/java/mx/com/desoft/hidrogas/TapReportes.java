package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.bussines.ReporteUnidadesBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.LlenadoTO;

import static mx.com.desoft.hidrogas.R.id.fecha;

/**
 * Created by David on 03/12/16.
 */

public class TapReportes extends Fragment{

    private TextView labelFechaBusqueda;
    private Button btnBuscar,btnExportarExcel, btnFechaBusqueda;
    private ListView listView;
    private ViewGroup view;
    private ReporteUnidadesBussines reporteUnidadesBussines;
    private List<LlenadoTO> listaLlenado = new ArrayList<>();
    private Reportes reportes = new Reportes();
    private int year, month, day;
    private Long fechaBusqueda;
    private LiquidacionBussines liquidacionBussines = new LiquidacionBussines();
    private final Calendar calendar = Calendar.getInstance();
    private ListLlenadoAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.activity_reportes, container, false);

        btnBuscar = (Button) view.findViewById(R.id.btnBuscar);
        btnExportarExcel = (Button) view.findViewById(R.id.btnExportarExcel);
        labelFechaBusqueda = (TextView) view.findViewById(R.id.labelFechaBusqueda);
        btnFechaBusqueda = (Button) view.findViewById(R.id.btnFechaBusqueda);


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!TextUtils.isEmpty(labelFechaBusqueda.getText().toString())){
                reporteUnidadesBussines = new ReporteUnidadesBussines(view.getContext());
                listaLlenado = reporteUnidadesBussines.getUnidadLlenadoByFecha(fechaBusqueda);
                listAdapter = new ListLlenadoAdapter(view.getContext(),R.layout.list_item_variacion, listaLlenado);
                listView = (ListView) view.findViewById(R.id.listViewBusquedaVariacion);
                listView.setItemsCanFocus(false);
                listView.setAdapter(listAdapter);
            }   else    {
                Toast.makeText(view.getContext(), "El campo de fecha es requerido , favor de seleccionar una fecha.", Toast.LENGTH_LONG).show();
            }

                //List<LiquidacionesTO> lista = liquidacionBussines.getAllLiquidaciones(view);
                //Date fechahoy= null;

                /*
                for (LlenadoTO l: listaLlenado) {
                    Log.d("Error " +l.toString(), "LLENADO  " +l.getIdLlenado() + " " + l.getNoPipa() + " " +l.getPorcentajeLlenado()+" " + l.getVariacion()
                            +" " + l.getNominaRegistro()+ " " + l.getFechaRegistro());
                    Date date=new Date(l.getFechaRegistro());
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                    String dateText = df2.format(date);
                    try {
                        fechahoy = df2.parse(dateText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d("FEcha"," fecha completada" + dateText);
                }*/
                //Toast.makeText(view.getContext(), "Fui a buscar las unidades "+fechahoy, Toast.LENGTH_LONG).show();
            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "Clicked: "+names.get(position), Toast.LENGTH_LONG).show();
            }
        });*/

        // Enlazamos con nuestro adaptador personalizado


        btnExportarExcel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                try {
                    if (listaLlenado.size() > 0){
                        String ruta = reportes.excel(view,listaLlenado);
                        Toast.makeText(view.getContext(), "El reporte se creo en la siguiente ruta: " + ruta, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(view.getContext(), "No existe información para exportar, favor de validar", Toast.LENGTH_LONG).show();
                    }

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
                    showDate(arg1, arg2, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        calendar.set(year, month, day);
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = formato.parse(formato.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fechaBusqueda = fecha.getTime();
        labelFechaBusqueda.setText(formato.format(fecha));
    }

    private void getFechaActual(){
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);

    }
}
