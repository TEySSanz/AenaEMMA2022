package es.testadistica.www.aenaemma2022.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Locale;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.FormTrab;
import es.testadistica.www.aenaemma2022.utilidades.ModeloTrabajadores1;

public class CueTrabajadoresActivity extends AppCompatActivity {

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_TIME = "HH:mm";
    private Date fechaActual = null;
    private int maxPreg = 28;

    private CueTrabajadores cue;
    private FormTrab form;
    private int modeloCue;
    private int idAeropuerto;
    private int idCue;
    private int pregunta;
    private Button saveButton;
    private Button nextButton;
    private Button previousButton;
    DBHelper conn;

    TextView txt_encuestador;
    TextView txt_numEncuesta;
    TextView txt_airportHeader;
    EditText txt_fecha;
    EditText txt_hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Recoge los parámetros de la pantalla anterior
        Bundle datos = this.getIntent().getExtras();

        if (datos != null) {
            setAppLocale(datos.getString("idioma"));
        }

        setContentView(R.layout.activity_cuetrabajadores);

        //Añadir icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Asigna campos a componentes
        txt_encuestador = (TextView) findViewById(R.id.survey_text_encuestador);
        txt_numEncuesta = (TextView) findViewById(R.id.survey_text_numEncuesta);
        txt_fecha = (EditText) findViewById(R.id.survey_edit_fecha);
        txt_hora = (EditText) findViewById(R.id.survey_edit_hora);
        txt_airportHeader = (TextView) findViewById(R.id.survey_text_airportHeader);

        //BBDD
        conn = new DBHelper(this.getApplicationContext());

        //Recoge los parámetros de la pantalla anterior
        if (datos != null) {
            txt_encuestador.setText(datos.getString("encuestador"));
            txt_numEncuesta.setText(datos.getString("numEncuesta"));
            txt_airportHeader.setText("Encuesta Movilidad Trabajadores\n" + datos.getString("aeropuerto"));
            txt_fecha.setText(datos.getString("fecha"));
            txt_hora.setText(datos.getString("hora"));
            pregunta = 1;
            idCue = datos.getInt("idCue");
            modeloCue = datos.getInt("modeloCue");
            idAeropuerto = datos.getInt("idAeropuerto");
        }

        //Genera el cuestionario
        cue = new CueTrabajadores(idCue, idAeropuerto);
        switch (modeloCue) {
            case 1:
                form = new ModeloTrabajadores1(this, pregunta, conn);
                ((ModeloTrabajadores1) form).setCue(cue);

                break;
        }
        LinearLayout formContainer = (LinearLayout) findViewById(R.id.survey_form_container);
        View.inflate(this, form.getLayoutId(), formContainer);

        form.initFormView();

        //Progressbar
        final TextView tvQ = (TextView) findViewById(R.id.survey_text_question);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.survey_progressbar);

        pb.setProgress(pregunta);
        pb.setMax(maxPreg);
        tvQ.setText(String.valueOf(pregunta) + "/" + maxPreg);

        //Botón Guardar
        saveButton = (Button) findViewById(R.id.survey_button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (satisfyValidation()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CueTrabajadoresActivity.this);
                    alertDialogBuilder.setMessage("¿Está seguro de que desea guardar y salir?");

                    alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //form.onNextPressed(pregunta);
                            if (form.checkQuestion(999)) {
                                Toast.makeText(CueTrabajadoresActivity.this, "El cuestionario se ha guardado", Toast.LENGTH_LONG).show();
                                Intent visita = new Intent();
                                setResult(0, visita);
                                finish();
                            }
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        //Botón Anterior
        previousButton = (Button) findViewById(R.id.survey_button_previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etDummy = (EditText) findViewById(R.id.survey_edit_fecha);
                etDummy.requestFocus();

                int actual = pregunta;
                int anterior = ((ModeloTrabajadores1) form).getPreguntaAnterior();

                //form.onPreviousPressed(actual, anterior);
                pregunta = form.onPreviousPressed(actual, anterior);

                pb.setProgress(pregunta);
                pb.setMax(maxPreg);
                tvQ.setText(String.valueOf(pregunta) + "/" + maxPreg);
            }
        });

        //Botón Siguiente
        nextButton = (Button) findViewById(R.id.survey_button_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etDummy = (EditText) findViewById(R.id.survey_edit_fecha);
                etDummy.requestFocus();

                pb.setProgress(pregunta);

                if (form.onNextPressed(pregunta) != 0) {

                    pregunta = form.onNextPressed(pregunta);

                    pb.setProgress(pregunta);
                    pb.setMax(maxPreg);
                    tvQ.setText(String.valueOf(pregunta) + "/" + maxPreg);
                }
            }
        });
    }

    private boolean satisfyValidation(){

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){

        return true;
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    private void setAppLocale(String localeCode){

        switch (localeCode){
            case "Inglés":
                localeCode="en";
                break;
            default:
                localeCode="es";
                break;
        }

        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
