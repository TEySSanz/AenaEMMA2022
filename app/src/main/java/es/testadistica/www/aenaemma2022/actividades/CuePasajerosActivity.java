package es.testadistica.www.aenaemma2022.actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.utilidades.Form;

public class CuePasajerosActivity extends AppCompatActivity {

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_TIME = "HH:mm";
    private Date fechaActual = null;
    private int maxPreg = 41;

    private CuePasajeros cue;
    private Form form;
    private int idCue;
    private int pregunta;
    private Button saveButton;
    private Button nextButton;
    private Button previousButton;

    TextView txt_encuestador;
    TextView txt_numEncuesta;
    EditText txt_fechaHora;
    EditText txt_aeropuerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuepasajeros);

        //Añadir icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Asigna campos a componentes
        txt_encuestador = (TextView) findViewById(R.id.survey_text_encuestador);
        txt_numEncuesta = (TextView) findViewById(R.id.survey_text_numEncuesta);
        txt_fechaHora = (EditText) findViewById(R.id.survey_edit_fecha);
        //txt_aeropuerto = (EditText) findViewById(R.id.survey_edit_linea);
    }
}
