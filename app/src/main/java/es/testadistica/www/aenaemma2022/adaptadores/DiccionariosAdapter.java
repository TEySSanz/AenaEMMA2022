package es.testadistica.www.aenaemma2022.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.Diccionarios;

public class DiccionariosAdapter extends ArrayAdapter<Diccionarios> {

    List<Diccionarios> list;

    public DiccionariosAdapter(Activity activity, int layout, List<Diccionarios> list) {
        super(activity, layout, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // Ordinary


        return initView(position, convertView, parent);
    }

    @Nullable
    @Override
    public Diccionarios getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) { // This view starts when we click the

        View view = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        view = layoutInflater.inflate(R.layout.selection_spinner_item_small, parent, false);

        TextView tv= (TextView) view.findViewById(R.id.text1);
        tv.setText(list.get(position).getTexto());
        return view;

    }

    public View initView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        view = layoutInflater.inflate(R.layout.selection_spinner_item_small, parent, false);

        TextView tv= (TextView) view.findViewById(R.id.text1);
        tv.setText(list.get(position).getTexto());
        return view;
    }
}
