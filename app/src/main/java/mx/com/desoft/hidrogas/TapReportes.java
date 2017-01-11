package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    private DatePicker datePicker;
    private Calendar calendar;
    private View view;
    private Activity activity;
    private ReporteUnidadesBussines reporteUnidadesBussines;
    private List<LlenadoTO> listaLlenado = new ArrayList<>();
    private Reportes reportes = new Reportes();
    private int year, month, day;
    static final int DATE_DIALOG_ID = 999;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_reportes, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        btnBuscar = (Button) view.findViewById(R.id.btnBuscar);
        btnExportarExcel = (Button) view.findViewById(R.id.btnExportarExcel);
        labelFechaBusqueda = (TextView) view.findViewById(R.id.labelFechaBusqueda);
        btnFechaBusqueda = (Button) view.findViewById(R.id.btnFechaBusqueda);
        //datePicker = (DatePicker) view.findViewById(R.id.dpResult);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //setCurrentDateOnView();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reporteUnidadesBussines = new ReporteUnidadesBussines(view.getContext());
                listaLlenado = reporteUnidadesBussines.getUnidadLlenadoByFecha(Integer.valueOf(labelFechaBusqueda.getText().toString()));
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
                //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setPositiveButton("Fecha", new DialogInterface.OnClickListener() {
                   // @Override
                    //public void onClick(DialogInterface dialog, int which) {
                        DatePickerDialog pik = new DatePickerDialog(view.getContext(), myDateListener, year, month, day);
                        pik.show();
                    //}

                    //});
                //builder.show();

                //dialogo  d = new dialogo(year,month,day,labelFechaBusqueda, view.getContext());
            }

        });

        return view;
    }


    private void getFechaActual(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Long fecha = new Date().getTime();
        labelFechaBusqueda.setText(format.format(fecha).toString());
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        labelFechaBusqueda.setText(new StringBuilder().append(day).append("/")
                .append(month).append("///").append(year));
    }
    public void setCurrentDateOnView() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        labelFechaBusqueda.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(month + 1).append("-").append(day).append("-")
                .append(year).append(" "));

        // set current date into datepicker
        datePicker.init(year, month, day, null);

    }




    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview
            labelFechaBusqueda.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

            // set selected date into datepicker also
            datePicker.init(year, month, day, null);

        }
    };
}

class dialogo extends Activity{
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private Context context;


    public dialogo(int year, int month, int day, TextView fecha, Context context){
        this.dateView = fecha;
        this.context = context;
        showDate(year, month+1, day);
        setDate(this.getCurrentFocus());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(0);
        Toast.makeText(context, "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int i) {
        // TODO Auto-generated method stub
      //  if (id == 999) {
            return new DatePickerDialog(context,
                    myDateListener, year, month, day);
        //}
        //return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("///").append(year));
    }
}
