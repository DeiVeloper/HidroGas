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
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.utils.Utils;


public class AdapterLiquidaciones extends ArrayAdapter<LiquidacionesTO> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<LiquidacionesTO> liquidacionesTOs = new ArrayList<>();

    public AdapterLiquidaciones(Context context, int layoutResourceId, ArrayList<LiquidacionesTO> liquidacionesTOs) {
        super(context, layoutResourceId, liquidacionesTOs);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.liquidacionesTOs = liquidacionesTOs;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        Utils utils = new Utils();
        View item = convertView;
        LiquidacionTOWrapper liquidacionTOWrapper;
        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);
            liquidacionTOWrapper = new LiquidacionTOWrapper();
            liquidacionTOWrapper.folio = (TextView) item.findViewById(R.id.txtFolioLiquidacion);
            liquidacionTOWrapper.fecha = (TextView) item.findViewById(R.id.txtFechaLiquidacion);
            liquidacionTOWrapper.pipa = (TextView) item.findViewById(R.id.txtPipaLiquidacion);
            item.setTag(liquidacionTOWrapper);
        } else {
            liquidacionTOWrapper = (LiquidacionTOWrapper) item.getTag();
        }
        LiquidacionesTO liquidacionesTO = liquidacionesTOs.get(position);
        liquidacionTOWrapper.folio.setText(liquidacionesTO.getIdLiquidacion().toString());
        liquidacionTOWrapper.fecha.setText(utils.convertirFecha(liquidacionesTO.getFechaRegistro()));
        liquidacionTOWrapper.pipa.setText(liquidacionesTO.getNoPipa().toString());
        return item;
    }

    private static class LiquidacionTOWrapper {
        TextView folio;
        TextView fecha;
        TextView pipa;
    }



}
