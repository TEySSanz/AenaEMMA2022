package es.testadistica.www.aenaemma2022.adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajerosListado;

public class ListadoPasajerosItemAdapter extends ArrayAdapter<CuePasajerosListado> {

    public ListadoPasajerosItemAdapter(Context context, List<CuePasajerosListado> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CuePasajerosListado cue = getItem(position);
        ViewHolder holder;

        //Inflate view
        if(convertView==null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.txt_numEncuesta = ((TextView) convertView.findViewById(R.id.list_item_text_numEncuesta));
            holder.txt_fecha = ((TextView) convertView.findViewById(R.id.list_item_text_fecha));
            holder.txt_hora = ((TextView) convertView.findViewById(R.id.list_item_text_hora));
            holder.txt_aeropuerto = ((TextView) convertView.findViewById(R.id.list_item_text_aeropuerto));
            holder.txt_puerta = ((TextView) convertView.findViewById(R.id.list_item_text_puerta));

            convertView.setTag(holder);
        } else {
            holder = new ViewHolder();
            holder.txt_numEncuesta = ((TextView) convertView.findViewById(R.id.list_item_text_numEncuesta));
            holder.txt_fecha = ((TextView) convertView.findViewById(R.id.list_item_text_fecha));
            holder.txt_hora = ((TextView) convertView.findViewById(R.id.list_item_text_hora));
            holder.txt_aeropuerto = ((TextView) convertView.findViewById(R.id.list_item_text_aeropuerto));
            holder.txt_puerta = ((TextView) convertView.findViewById(R.id.list_item_text_puerta));

            holder = (ViewHolder) convertView.getTag();
        }

        //Asigna los valores
        holder.txt_numEncuesta.setText(String.valueOf(cue.getIden()));
        holder.txt_fecha.setText(String.valueOf(cue.getFecha()));
        holder.txt_hora.setText(String.valueOf(cue.getHoraInicio()));
        holder.txt_aeropuerto.setText(String.valueOf(cue.getAeropuerto()));
        holder.txt_puerta.setText(String.valueOf(cue.getPuerta()));

        return convertView;
    }

    static class ViewHolder {
        public TextView txt_numEncuesta;
        public TextView txt_fecha;
        public TextView txt_hora;
        public TextView txt_aeropuerto;
        public TextView txt_puerta;
    }
}
