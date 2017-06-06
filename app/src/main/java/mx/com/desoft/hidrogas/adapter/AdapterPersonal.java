package mx.com.desoft.hidrogas.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mx.com.desoft.hidrogas.R;
import mx.com.desoft.hidrogas.to.PersonalTO;

public class AdapterPersonal extends ArrayAdapter<PersonalTO> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<PersonalTO> personalTOs = new ArrayList<>();

    public AdapterPersonal(Context context, int layoutResourceId, ArrayList<PersonalTO> personalTOs) {
        super(context, layoutResourceId, personalTOs);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.personalTOs = personalTOs;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        PersonalTOWrapper personalTOWrapper;
        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            personalTOWrapper = new PersonalTOWrapper();
            personalTOWrapper.nomina = (TextView) item.findViewById(R.id.txtNomina);
            personalTOWrapper.nombre = (TextView) item.findViewById(R.id.txtNombre);
            personalTOWrapper.pipa = (TextView) item.findViewById(R.id.txtPipa);
            item.setTag(personalTOWrapper);
        } else {
            personalTOWrapper = (PersonalTOWrapper) item.getTag();
        }
        PersonalTO personalTO = personalTOs.get(position);
        personalTOWrapper.nomina.setText(personalTO.getNomina().toString());
        personalTOWrapper.nombre.setText(personalTO.getNombre() + " " + personalTO.getApellidoPaterno() + " " + personalTO.getApellidoMaterno());
        personalTOWrapper.pipa.setText(personalTO.getNoPipa().toString());
        return item;
    }

    private static class PersonalTOWrapper {
        TextView nomina;
        TextView nombre;
        TextView pipa;
    }

}
