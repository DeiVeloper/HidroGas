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
import mx.com.desoft.hidrogas.to.LiquidacionesTO;

/**
 * Created by erick.martinez on 27/03/2017.
 */

public class AdapterLiquidaciones extends ArrayAdapter<LiquidacionesTO> {
    Context context;
    private int layoutResourceId;
    ArrayList<LiquidacionesTO> liquidacionesTOs = new ArrayList<LiquidacionesTO>();
    private LiquidacionesTO liquidacionesTO;
    public AdapterLiquidaciones(Context context, int layoutResourceId, ArrayList<LiquidacionesTO> liquidacionesTOs) {
        super(context, layoutResourceId, liquidacionesTOs);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.liquidacionesTOs = liquidacionesTOs;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        LiquidacionTOWrapper liquidacionTOWrapper = null;
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
        liquidacionesTO = liquidacionesTOs.get(position);
        liquidacionTOWrapper.folio.setText(liquidacionesTO.getIdLiquidacion().toString());
        liquidacionTOWrapper.fecha.setText(liquidacionesTO.getFechaRegistro().toString());
        liquidacionTOWrapper.pipa.setText(liquidacionesTO.getNoPipa().toString());
        return item;
    }
    static class LiquidacionTOWrapper {
        TextView folio;
        TextView fecha;
        TextView pipa;
    }



}
