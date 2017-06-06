package mx.com.desoft.hidrogas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mx.com.desoft.hidrogas.R;
import mx.com.desoft.hidrogas.to.LlenadoTO;
import mx.com.desoft.hidrogas.utils.Utils;

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
            Utils utils = new Utils();
            View v = convertView;
            ViewHolder holder;

            if (v == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                v = layoutInflater.inflate(layout, null);

                holder = new ViewHolder();
                holder.textViewPipar = (TextView) v.findViewById(R.id.txtReportePipa);
                holder.textViewFechaRegistro = (TextView) v.findViewById(R.id.txtReporteFechaRegistro);
                holder.textViewVariacion = (TextView) v.findViewById(R.id.txtReporteVariacion);
                holder.textViewPorcentajeVariacion = (TextView) v.findViewById(R.id.txtReportePorcentajeVariacion);
                holder.textViewClave = (TextView) v.findViewById(R.id.txtReporteClave);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            String noPipa = lista.get(position).getNoPipa().toString();
            String fechaRegistro = utils.convertirFecha(lista.get(position).getFechaRegistro());
            String variacion = lista.get(position).getVariacion().toString();
            String porcentajeVariacion = lista.get(position).getPorcentajeVariacion().toString();
            String clave = lista.get(position).getClave() != null ? lista.get(position).getClave() : "";

            holder.textViewPipar.setText(noPipa);
            holder.textViewFechaRegistro.setText(fechaRegistro);
            holder.textViewVariacion.setText(variacion);
            holder.textViewPorcentajeVariacion.setText(porcentajeVariacion);
            holder.textViewClave.setText(clave);

            return v;
        }

        private static class ViewHolder {
            private TextView textViewPipar;
            private TextView textViewFechaRegistro;
            private TextView textViewVariacion;
            private TextView textViewPorcentajeVariacion;
            private TextView textViewClave;
        }

}
