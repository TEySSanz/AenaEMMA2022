package es.testadistica.www.aenaemma2022.adaptadores;

import android.content.Context;
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
                    .inflate(R.layout.listpasajeros_item, parent, false);
            holder = new ViewHolder();
            holder.txt_numEncuesta = ((TextView) convertView.findViewById(R.id.surveylist_item_text_iden));
            holder.txt_encuestador = ((TextView) convertView.findViewById(R.id.surveylist_item_text_codEnc));
            holder.txt_fecha = ((TextView) convertView.findViewById(R.id.surveylist_item_text_fecha));
            holder.txt_idioma = ((TextView) convertView.findViewById(R.id.surveylist_item_text_codIdioma));
            holder.txt_vuelo = ((TextView) convertView.findViewById(R.id.surveylist_item_text_vuelo));
            holder.txt_puerta = ((TextView) convertView.findViewById(R.id.surveylist_item_text_puerta));

            convertView.setTag(holder);
        } else {
            holder = new ViewHolder();
            holder.txt_numEncuesta = ((TextView) convertView.findViewById(R.id.surveylist_item_text_iden));
            holder.txt_encuestador = ((TextView) convertView.findViewById(R.id.surveylist_item_text_codEnc));
            holder.txt_fecha = ((TextView) convertView.findViewById(R.id.surveylist_item_text_fecha));
            holder.txt_idioma = ((TextView) convertView.findViewById(R.id.surveylist_item_text_codIdioma));
            holder.txt_vuelo = ((TextView) convertView.findViewById(R.id.surveylist_item_text_vuelo));
            holder.txt_puerta = ((TextView) convertView.findViewById(R.id.surveylist_item_text_puerta));

            holder = (ViewHolder) convertView.getTag();
        }

        //Asigna los valores
        holder.txt_numEncuesta.setText(String.valueOf(cue.getIden()));
        holder.txt_encuestador.setText(String.valueOf(cue.getEncuestador()));
        holder.txt_fecha.setText(String.valueOf(cue.getFecha()));
        holder.txt_idioma.setText(String.valueOf(cue.getIdioma()));
        holder.txt_vuelo.setText(String.valueOf(cue.getVuelo()));
        holder.txt_puerta.setText(String.valueOf(cue.getPuerta()));

        return convertView;
    }

    static class ViewHolder {
        public TextView txt_numEncuesta;
        public TextView txt_encuestador;
        public TextView txt_fecha;
        public TextView txt_idioma;
        public TextView txt_vuelo;
        public TextView txt_puerta;
    }
}
