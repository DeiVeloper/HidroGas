package mx.com.desoft.hidrogas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mx.com.desoft.adapter.AdapterLiquidaciones;
import mx.com.desoft.hidrogas.bussines.ReporteUnidadesBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;

/**
 * Created by erick.martinez on 27/03/2017.
 */

public class ListaLiquidaciones extends Fragment {
    private Button btnBuscar;
    private ImageButton btnFechaBusqueda;
    private EditText txtFolioLiquidacion, txtNoNomina;
    private TextView labelFechaBusqueda;
    private ViewGroup viewGroup;
    private int year, month, day;
    private final Calendar calendar = Calendar.getInstance();
    private Long fechaBusqueda;
    private ReporteUnidadesBussines reporteUnidadesBussines;
    private ArrayList<LiquidacionesTO> listaLiquidaciones = new ArrayList<>();
    private ListView listViewLiquidaciones;
    private AdapterLiquidaciones listAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_listado_liquidaciones, container, false);
        inicializarComponentes();
        cargarEventos();
        return viewGroup;
    }

    private void inicializarComponentes() {
        labelFechaBusqueda = (TextView) viewGroup.findViewById(R.id.labelFechaBusqueda);
        txtFolioLiquidacion = (EditText)viewGroup.findViewById(R.id.txtFolioLiquidacionBusqueda);
        btnBuscar = (Button)viewGroup.findViewById(R.id.btnBuscar);
        btnFechaBusqueda = (ImageButton) viewGroup.findViewById(R.id.btnFechaBusqueda2);
    }

    private void cargarEventos() {
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });
        btnFechaBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaActual();
                DatePickerDialog pik = new DatePickerDialog(viewGroup.getContext(), myDateListener, year, month, day);
                pik.show();
            }

        });
    }

    public void buscar() {
        //try {
        if (!TextUtils.isEmpty(labelFechaBusqueda.getText().toString())){
            reporteUnidadesBussines = new ReporteUnidadesBussines();
            listaLiquidaciones = reporteUnidadesBussines.getAllLiquidacionesByFecha(fechaBusqueda);
            if (!listaLiquidaciones.isEmpty()){
                listAdapter = new AdapterLiquidaciones(viewGroup.getContext(),R.layout.list_items_liquidaciones, listaLiquidaciones);
                listViewLiquidaciones = (ListView) viewGroup.findViewById(R.id.lstLiquidaciones);
                listViewLiquidaciones.setItemsCanFocus(false);
                listViewLiquidaciones.setAdapter(listAdapter);
                registerForContextMenu(listViewLiquidaciones);
            }   else    {
                Toast.makeText(viewGroup.getContext(), "No existen registros asociados a su busqueda.", Toast.LENGTH_LONG).show();
            }
        }   else    {
            Toast.makeText(viewGroup.getContext(), "El campo de fecha es requerido , favor de seleccionar una fecha.", Toast.LENGTH_LONG).show();
        }
        /*} catch (Exception e) {
            Toast.makeText(viewGroup.getContext(), "Ha ocurrido un error al realizar la búsqueda, Intente nuevamente por favor.", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_liquidaciones, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.eliminarLiquidacion:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(viewGroup.getContext());
                dialogo1.setTitle("Eliminar Liquidación");
                dialogo1.setMessage("¿Está seguro de eliminar este registro?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        reporteUnidadesBussines.eliminarLiquidacion( listaLiquidaciones.get(adapterContextMenuInfo.position));
                        Toast.makeText(viewGroup.getContext(), "La liquidación con Folio: " + listaLiquidaciones.get(adapterContextMenuInfo.position).getIdLiquidacion() + " se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                        listaLiquidaciones.remove(adapterContextMenuInfo.position);
                        listAdapter.notifyDataSetChanged();
                    }
                });
                dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
                return true;
            case R.id.editarLiquidacion:
                Intent accion = new Intent (viewGroup.getContext(), MainActivity.class);
                accion.putExtra("viewpager_position", 0);
                accion.putExtra("folio", listaLiquidaciones.get(adapterContextMenuInfo.position).getIdLiquidacion());
                chargePage(accion);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void chargePage(Intent accion){
        startActivity(accion);
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