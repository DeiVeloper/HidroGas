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

import mx.com.desoft.adapter.AdapterPersonal;
import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by erick.martinez on 25/11/2016.
 */

public class ListaPersonal extends Fragment {
    Button btnAgregar, btnBuscar;
    EditText txtNombre, txtNoNomina;
    private ViewGroup viewGroup;
    private ListView lstPersonal;
    private PersonalTO personalTO;
    private PersonalBussines personalBussines;

    ListView listview;
    private AdapterPersonal adapterPersonal;
    ArrayList<PersonalTO> personalTOArray;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_litado_personal, container, false);
        personalTO = new PersonalTO();
        personalBussines = new PersonalBussines();
        personalTOArray = new ArrayList<PersonalTO>();
        btnAgregar = (Button)viewGroup.findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accion = new Intent (viewGroup.getContext(), AgregarEditarPersonal.class);
                chargePage(accion);
            }
        });
        txtNombre = (EditText)viewGroup.findViewById(R.id.txtNombre);
        txtNoNomina = (EditText)viewGroup.findViewById(R.id.txtNoNomina);
        btnBuscar = (Button)viewGroup.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscar();
            }
        });
        return viewGroup;
    }

    public void buscar() {
        //try {
            if (TextUtils.isEmpty(txtNoNomina.getText().toString())) {
                personalTO.setNomina(0);
            }else {
                personalTO.setNomina(Integer.parseInt(txtNoNomina.getText().toString()));
            }
            personalTO.setNombre(txtNombre.getText().toString().trim());
            Cursor registros = personalBussines.buscar(viewGroup.getContext(), personalTO);
            personalTOArray = new ArrayList<PersonalTO>();
            if (registros.moveToFirst()) {
                do {
                    personalTOArray.add(new PersonalTO(registros.getInt(0), registros.getString(1), registros.getString(2), registros.getString(3), registros.getInt(5), registros.getInt(8)));
                } while (registros.moveToNext());
            } else {
                Toast.makeText(viewGroup.getContext(), "Su búsqueda no tiene registros asociados.", Toast.LENGTH_SHORT).show();
            }
            this.adapterPersonal = new AdapterPersonal(viewGroup.getContext(), R.layout.list_items_personal, personalTOArray);
            listview= (ListView)viewGroup.findViewById(R.id.lstPersonal);
            listview.setItemsCanFocus(false);
            listview.setAdapter(adapterPersonal);
            registerForContextMenu(listview);
        /*} catch (Exception e) {
            Toast.makeText(viewGroup.getContext(), "Ha ocurrido un error al realizar la búsqueda, Intente nuevamente por favor.", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_personal, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.eliminar:
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(viewGroup.getContext());
                dialogo1.setTitle("Eliminar Personal");
                dialogo1.setMessage("¿Está seguro de eliminar este registro?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        personalBussines.eliminar(viewGroup.getContext(), personalTOArray.get(adapterContextMenuInfo.position));
                        Toast.makeText(viewGroup.getContext(), "El usuario con nómina: " + personalTOArray.get(adapterContextMenuInfo.position).getNomina() + " se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                        personalTOArray.remove(adapterContextMenuInfo.position);
                        adapterPersonal.notifyDataSetChanged();
                    }
                });
                dialogo1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
                return true;
            case R.id.editar:
                Intent accion = new Intent (viewGroup.getContext(), AgregarEditarPersonal.class);
                accion.putExtra("nomina", personalTOArray.get(adapterContextMenuInfo.position).getNomina());
                accion.putExtra("nombre", personalTOArray.get(adapterContextMenuInfo.position).getNombre());
                accion.putExtra("aPaterno", personalTOArray.get(adapterContextMenuInfo.position).getApellidoPaterno());
                accion.putExtra("aMaterno", personalTOArray.get(adapterContextMenuInfo.position).getApellidoMaterno());
                accion.putExtra("tipoEmpleado", personalTOArray.get(adapterContextMenuInfo.position).getTipoEmpleado());
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
