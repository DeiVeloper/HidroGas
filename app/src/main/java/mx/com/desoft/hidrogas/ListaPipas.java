package mx.com.desoft.hidrogas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.com.desoft.hidrogas.adapter.AdapterPipas;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PipasTO;

public class ListaPipas extends Fragment {
    private ViewGroup viewGroup;
    private PipasBussines pipasBussines;
    private AdapterPipas adapterPipas;
    EditText txtPipa;
    Button btnAgregar, btnBuscar, btnExportarPipas;
    ArrayList<PipasTO> pipasTOArray;
    ListView listView;
    private Reportes reportes;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pipasBussines = new PipasBussines();
        pipasTOArray = new ArrayList<>();
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_administrador_pipas, container, false);
        inicializarComponentes();
        cargarEventos();
        return viewGroup;
    }

    private void inicializarComponentes() {
        txtPipa = (EditText) viewGroup.findViewById(R.id.txtPipa);
        btnBuscar = (Button)viewGroup.findViewById(R.id.btnBuscar);
        btnAgregar = (Button)viewGroup.findViewById(R.id.btnAgregar);
        btnExportarPipas = (Button) viewGroup.findViewById(R.id.btnExportarPipas);
    }

    private void cargarEventos() {
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accion = new Intent (viewGroup.getContext(), AgregarPipas.class);
                chargePage(accion);
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });

        btnExportarPipas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0)
            {
                try {
                    if (!pipasTOArray.isEmpty()){
                        reportes = new Reportes();
                        reportes.reporteExcelPipas(viewGroup, pipasTOArray);
                        Toast.makeText(viewGroup.getContext(), "Se genero el reporte con éxito"  , Toast.LENGTH_LONG).show();
                    }else   {
                        Toast.makeText(viewGroup.getContext(), "No existen registros para exportar, favor de validar"  , Toast.LENGTH_LONG).show();
                    }

                }catch (Exception  e)   {
                    e.printStackTrace();
                    Toast.makeText(viewGroup.getContext(), "No se pudo crear al excel, favor de contactar al Administrador", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void buscar() {
        Integer pipa = 0, porcentajeLlenado;
        String  chofer, ayudante;
        if (!TextUtils.isEmpty(txtPipa.getText().toString())){
            pipa = Integer.parseInt (txtPipa.getText().toString());
        }
        Cursor registros = pipasBussines.buscarByNoPipa2(pipa);
        pipasTOArray = new ArrayList<>();
        if (registros.moveToFirst()) {
            do {
                Cursor resgistroLlenado = pipasBussines.buscarLlenadoByNoPipa(registros.getInt(0));
                porcentajeLlenado = 0;
                if (resgistroLlenado.moveToFirst()) {
                    porcentajeLlenado = resgistroLlenado.getInt(2);
                }
                Cursor resgistroChoferAyudante = pipasBussines.buscarChoferAyudanteByNoPipa(registros.getInt(0));
                chofer = "";
                ayudante = "";
                if (resgistroChoferAyudante.moveToFirst()) {
                    do {
                        if (resgistroChoferAyudante.getInt(8) == 1) {
                            chofer = resgistroChoferAyudante.getString(1) + " " + resgistroChoferAyudante.getString(2) + " " + resgistroChoferAyudante.getString(3);
                        } else if (resgistroChoferAyudante.getInt(8) == 2){
                            ayudante = resgistroChoferAyudante.getString(1) + " " + resgistroChoferAyudante.getString(2) + " " + resgistroChoferAyudante.getString(3);
                        }
                    } while (resgistroChoferAyudante.moveToNext());
                }
                pipasTOArray.add(new PipasTO(registros.getInt(0),registros.getInt(1), porcentajeLlenado, registros.getInt(2), registros.getLong(4), registros.getInt(3), chofer, ayudante));
            } while (registros.moveToNext());
        } else {
            Toast.makeText(viewGroup.getContext(), "Su búsqueda no tiene registros asociados.", Toast.LENGTH_SHORT).show();
        }
        this.adapterPipas = new AdapterPipas(viewGroup.getContext(), R.layout.list_items_pipas, pipasTOArray);
        listView = (ListView)viewGroup.findViewById(R.id.lstPipas);
        listView.setItemsCanFocus(false);
        listView.setAdapter(adapterPipas);
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_pipas, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.eliminarPipa:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewGroup.getContext());
                alertDialog.setTitle("Eliminar Pipa");
                alertDialog.setMessage("¿Está seguro de eliminar este registro?");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final boolean eliminada = pipasBussines.eliminar(pipasTOArray.get(adapterContextMenuInfo.position).getIdPipa());
                        if (eliminada) {
                            Toast.makeText(viewGroup.getContext(), "La Pipa número: " + pipasTOArray.get(adapterContextMenuInfo.position).getNoPipa() + " se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                            pipasTOArray.remove(adapterContextMenuInfo.position);
                            adapterPipas.notifyDataSetChanged();
                        } else {
                            Toast.makeText(viewGroup.getContext(), "La Pipa " + pipasTOArray.get(adapterContextMenuInfo.position).getNoPipa() + " no se puede eliminar. Tiene chofer y/o ayudante asignados.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(viewGroup.getContext(), "Favor de asignar otra pipa al chofer y/o ayudante o, eliminarlos antes de eliminar la pipa.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alertDialog.show();
                return true;
            case R.id.llenarPipa:
                Intent accion = new Intent(viewGroup.getContext(), LlenarPipa.class);
                accion.putExtra("idPipa", pipasTOArray.get(adapterContextMenuInfo.position).getIdPipa().toString());
                accion.putExtra("noPipa",pipasTOArray.get(adapterContextMenuInfo.position).getNoPipa().toString());
                accion.putExtra("porcentajeLlenado", pipasTOArray.get(adapterContextMenuInfo.position).getPorcentajeLlenado().toString());
                chargePage(accion);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void chargePage(Intent accion){
        startActivity(accion);
    }
}
