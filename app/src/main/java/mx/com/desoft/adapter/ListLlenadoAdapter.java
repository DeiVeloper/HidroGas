package mx.com.desoft.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.com.desoft.hidrogas.R;
import mx.com.desoft.hidrogas.to.LlenadoTO;

/**
 * Created by David on 04/01/17.
 */

public class ListLlenadoAdapter extends BaseAdapter{

        private Context context;
        private int layout;
        private List<LlenadoTO> names;

        public ListLlenadoAdapter(Context context, int layout, List<LlenadoTO> names) {
            this.context = context;
            this.layout = layout;
            this.names = names;
        }

        @Override
        public int getCount() {
            return this.names.size();
        }

        @Override
        public Object getItem(int position) {
            return this.names.get(position);
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View v = convertView;
            // View Holder Pattern
            ViewHolder holder = null;

            if (v == null) {
                // Inflamos la vista que nos ha llegado con nuestro layout personalizado
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                v = layoutInflater.inflate(layout, null);

                holder = new ViewHolder();
                // Referenciamos el elemento a modificar y lo rellenamos
                holder.textViewPipar = (TextView) v.findViewById(R.id.txtReportePipa);
                holder.textViewPorcentaje = (TextView) v.findViewById(R.id.txtReportePorcentaje);
                holder.textViewFechaRegistro = (TextView) v.findViewById(R.id.txtReporteFechaRegistro);
                holder.textViewVariacion = (TextView) v.findViewById(R.id.txtReporteVariacion);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            // Nos traemos el valor actual dependiente de la posición
            String noPipa = names.get(position).getNoPipa().toString();
            String porcentaje = names.get(position).getPorcentajeLlenado().toString();
            String fechaRegistro = convertirFecha(names.get(position).getFechaRegistro());
            String variacion = names.get(position).getVariacion().toString();

            // Referenciamos el elemento a modificar y lo rellenamos
            holder.textViewPipar.setText(noPipa);
            holder.textViewPorcentaje.setText(porcentaje);
            holder.textViewFechaRegistro.setText(fechaRegistro);
            holder.textViewVariacion.setText(variacion);


            // devolvemos la vista inflada y modificada con nuestros datos
            return v;
        }

        private String convertirFecha(Long fechaLong){
            Date date=new Date(fechaLong);
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            return formato.format(date);
        }

        static class ViewHolder {
            private TextView textViewPipar;
            private TextView textViewPorcentaje;
            private TextView textViewFechaRegistro;
            private TextView textViewVariacion;
        }

}
