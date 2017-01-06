package mx.com.desoft.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mx.com.desoft.hidrogas.R;
import mx.com.desoft.hidrogas.to.PersonalTO;

/**
 * Created by erick.martinez on 30/12/2016.
 */

public class AdapterPersonal extends ArrayAdapter<PersonalTO> {
    Context context;
    private int layoutResourceId;
    ArrayList<PersonalTO> personalTOs = new ArrayList<PersonalTO>();
    private PersonalTO personalTO;
    public AdapterPersonal(Context context, int layoutResourceId, ArrayList<PersonalTO> personalTOs) {
        super(context, layoutResourceId, personalTOs);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.personalTOs = personalTOs;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        PersonalTOWrapper personalTOWrapper = null;
        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            personalTOWrapper = new PersonalTOWrapper();
            personalTOWrapper.nomina = (TextView) item.findViewById(R.id.txtNoNomina);
            personalTOWrapper.nombre = (TextView) item.findViewById(R.id.txtNombre);
            personalTOWrapper.pipa = (TextView) item.findViewById(R.id.txtPipa);
            item.setTag(personalTOWrapper);
        } else {
            personalTOWrapper = (PersonalTOWrapper) item.getTag();
        }
        personalTO = personalTOs.get(position);
        personalTOWrapper.nomina.setText(personalTO.getNomina());
        personalTOWrapper.nombre.setText(personalTO.getNombre() + personalTO.getApellidoPaterno() + personalTO.getApellidoMaterno());
        personalTOWrapper.pipa.setText(personalTO.getNoPipa().toString());
        return item;
    }
    static class PersonalTOWrapper {
        TextView nomina;
        TextView nombre;
        TextView pipa;
    }



}
