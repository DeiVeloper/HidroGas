package mx.com.desoft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mx.com.desoft.hidrogas.R;
import mx.com.desoft.hidrogas.to.LlenadoTO;

/**
 * Created by David on 17/01/17.
 */

public class ListLiquidacionAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<LlenadoTO> names;

    public ListLiquidacionAdapter(Context context, int layout, List<LlenadoTO> names) {
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

        // View Holder Pattern
        ViewHolder holder;

        if (convertView == null) {
            // Inflamos la vista que nos ha llegado con nuestro layout personalizado
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(this.layout, null);

            holder = new ViewHolder();
            // Referenciamos el elemento a modificar y lo rellenamos
            holder.nameTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Nos traemos el valor actual dependiente de la posición
        String currentName = names.get(position).getNominaRegistro();
        //currentName = (String) getItem(position);

        // Referenciamos el elemento a modificar y lo rellenamos
        holder.nameTextView.setText(currentName);

        // devolvemos la vista inflada y modificada con nuestros datos
        return convertView;
    }

    static class ViewHolder {
        private TextView nameTextView;
    }

}
