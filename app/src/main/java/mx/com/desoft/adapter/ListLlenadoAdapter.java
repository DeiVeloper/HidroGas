package mx.com.desoft.adapter;

import android.content.Context;
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
        private List<LlenadoTO> lista;

        public ListLlenadoAdapter(Context context, int layout, List<LlenadoTO> lista) {
            this.context = context;
            this.layout = layout;
            this.lista = lista;
        }

        @Override
        public int getCount() {
            return this.lista.size();
        }

        @Override
        public Object getItem(int position) {
            return this.lista.get(position);
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
                holder.textViewFechaRegistro = (TextView) v.findViewById(R.id.txtReporteFechaRegistro);
                holder.textViewVariacion = (TextView) v.findViewById(R.id.txtReporteVariacion);
                holder.textViewPorcentajeVariacion = (TextView) v.findViewById(R.id.txtReportePorcentajeVariacion);
                holder.textViewClave = (TextView) v.findViewById(R.id.txtReporteClave);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            // Nos traemos el valor actual dependiente de la posición
            String noPipa = lista.get(position).getNoPipa().toString();
            String fechaRegistro = convertirFecha(lista.get(position).getFechaRegistro());
            String variacion = lista.get(position).getVariacion().toString();
            String porcentajeVariacion = lista.get(position).getPorcentajeVariacion().toString();
            String clave = lista.get(position).getClave() != null ? lista.get(position).getClave().toString() : "";

            // Referenciamos el elemento a modificar y lo rellenamos
            holder.textViewPipar.setText(noPipa);
            holder.textViewFechaRegistro.setText(fechaRegistro);
            holder.textViewVariacion.setText(variacion);
            holder.textViewPorcentajeVariacion.setText(porcentajeVariacion);
            holder.textViewClave.setText(clave);

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
            private TextView textViewFechaRegistro;
            private TextView textViewVariacion;
            private TextView textViewPorcentajeVariacion;
            private TextView textViewClave;
        }

}
