package es.testadistica.www.aenaemma2022.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import es.testadistica.www.aenaemma2022.R;

public abstract class BaseSearchableSpinnerAdapter<T extends Object> extends ArrayAdapter<String> {

    protected Context mContext;
    protected List<String> mLabels;
    protected List<T> mItems;

    public BaseSearchableSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        mItems = new ArrayList<>();
        mLabels = new ArrayList<>();
    }

    /** Abstract Methods **/
    public abstract String getLabel(int pos); //used to get the label to view

    /** Override ArrayAdapter Methods **/
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.selection_spinner_item_small, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.text1);
        if(tv != null){
            tv.setText(getLabel(position));
        }
        return convertView;
    }

    @Override
    public int getCount(){
        return mLabels.size();
    }

    @Override
    public String getItem(int position){
        return mLabels.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    /** Public Methods **/
    public void clear(){
        mLabels.clear();
        mItems.clear();
    }

    public void addAll(List<T> objs){
        mLabels.clear();
        mItems.clear();
        if(objs != null && objs.size() > 0x0){
            mItems.addAll(objs);
            for(int i=0x0; i<objs.size(); i++){
                mLabels.add(getLabel(i));
            }
        }
        notifyDataSetChanged();
    }

    public T getMyItem(int pos){
        return mItems.get(pos);
    }

}
