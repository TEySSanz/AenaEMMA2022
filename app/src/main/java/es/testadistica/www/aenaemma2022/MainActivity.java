package es.testadistica.www.aenaemma2022;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import es.testadistica.www.aenaemma2022.utilidades.SearchableSpinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SearchableSpinner spB3_LIT = (SearchableSpinner) this.findViewById(R.id.survey_spinner_B3_LIT);
        List<String> adapter = new ArrayList<>();

        adapter.add(0,"Casa");
        adapter.add(1,"Perro");
        adapter.add(2,"Hospital");
        ArrayAdapter<String> b3Adapter = new ArrayAdapter<String>(this, R.layout.selection_spinner_item_small, adapter);
        spB3_LIT.setAdapter(b3Adapter);
        spB3_LIT.setTitle("HOLA");
        spB3_LIT.setPositiveButton("CLARO QUE SI");
    }
}