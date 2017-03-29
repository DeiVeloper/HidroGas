package mx.com.desoft.adapter;

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
import mx.com.desoft.hidrogas.to.PipasTO;

public class AdapterPipas extends ArrayAdapter<PipasTO> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<PipasTO> pipasTOs = new ArrayList<>();

    public AdapterPipas(Context context, int layoutResourceId, ArrayList<PipasTO> pipasTOs) {
        super(context, layoutResourceId, pipasTOs);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.pipasTOs = pipasTOs;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        AdapterPipas.PipasTOWrapper pipasTOWrapper;
        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            pipasTOWrapper = new AdapterPipas.PipasTOWrapper();
            pipasTOWrapper.pipa = (TextView) item.findViewById(R.id.txtPipa);
            pipasTOWrapper.porcentaje = (TextView) item.findViewById(R.id.txtPorcentaje);
            pipasTOWrapper.capacidad = (TextView) item.findViewById(R.id.txtCapacidad);
            pipasTOWrapper.chofer = (TextView) item.findViewById(R.id.txtChofer);
            pipasTOWrapper.ayudante = (TextView) item.findViewById(R.id.txtAyudante);
            item.setTag(pipasTOWrapper);
        } else {
            pipasTOWrapper = (AdapterPipas.PipasTOWrapper) item.getTag();
        }
        PipasTO pipasTO = pipasTOs.get(position);
        pipasTOWrapper.pipa.setText(pipasTO.getNoPipa().toString());
        pipasTOWrapper.porcentaje.setText(pipasTO.getPorcentajeLlenado().toString());
        pipasTOWrapper.capacidad.setText(pipasTO.getCapacidad().toString());
        pipasTOWrapper.chofer.setText(pipasTO.getNombreChofer());
        pipasTOWrapper.ayudante.setText(pipasTO.getNombreAyudante());
        return item;
    }
    private static class PipasTOWrapper {
        TextView pipa, porcentaje, capacidad, chofer, ayudante;
    }
}
