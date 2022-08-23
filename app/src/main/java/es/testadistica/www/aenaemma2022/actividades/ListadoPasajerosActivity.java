package es.testadistica.www.aenaemma2022.actividades;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.adaptadores.ListadoPasajerosItemAdapter;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.entidades.CuePasajerosListado;
import es.testadistica.www.aenaemma2022.utilidades.Contracts;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.SearchableSpinnerOLD;

public class ListadoPasajerosActivity extends AppCompatActivity {

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_COMPLETE ="dd/MM/yyyy HH:mm";
    private Date fechaActual = null;
    private ArrayList<CuePasajerosListado> listaCue;
    private int modeloCue;
    private int maxPreg = 1;

    TextView txt_usuario;
    TextView txt_fechaActual;
    TextView txt_aeropuerto;
    int idAeropuerto;
    SearchableSpinnerOLD sp_idioma;
    ListView list_pasajeros;
    DBHelper conn;
    RequestQueue peticion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_pasajeros);

        //Añadir icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Asigna campos a componentes
        txt_usuario = (TextView) findViewById(R.id.txt_usuario);
        txt_fechaActual = (TextView) findViewById(R.id.txt_fechaActual);
        txt_aeropuerto = (TextView) findViewById(R.id.txt_aeropuerto);
        sp_idioma = (SearchableSpinnerOLD) findViewById(R.id.spinner_idioma);
        list_pasajeros = (ListView) findViewById(R.id.list_pasajeros);

        //Recoge los parámetros de la pantalla anterior
        Bundle datos = this.getIntent().getExtras();

        if (datos != null) {
            txt_usuario.setText(datos.getString("usuario"));
            txt_aeropuerto.setText(datos.getString("aeropuerto"));
            idAeropuerto=stringToInt(datos.getString("idAeropuerto"));
        }

        switch (idAeropuerto) {
            case 1:
                maxPreg = 42;
                break;
            case 2:
                maxPreg = 42;
                break;
            case 3:
                maxPreg = 36;
                break;
            case 4:
                maxPreg = 33;
                break;
            case 5:
                maxPreg = 37;
                break;
            case 6:
                maxPreg = 33;
                break;
            case 7:
                maxPreg = 36;
                break;
            case 8:
                maxPreg = 36;
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
        }

        //BBDD
        conn = new DBHelper(this.getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();

        //Establece la fecha actual
        Calendar currentTime = Calendar.getInstance();
        fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_COMPLETE);
        //Asigna la fecha a visualizar
        txt_fechaActual.setText(sdfDate.format(currentTime.getTime()));

        //Asigna los valores del desplegable de idiomas
        ArrayAdapter<String> idiomaAdapter = new ArrayAdapter<String>(this, R.layout.selection_spinner_item_small, getIdiomas(Contracts.COLUMN_IDIOMAS_CLAVE+" NOT IN ('EU')"));
        idiomaAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
        sp_idioma.setAdapter(idiomaAdapter);
        sp_idioma.setTitle(this.getString(R.string.spinner_idioma_title));
        sp_idioma.setPositiveButton(this.getString(R.string.spinner_close));

        //Actualiza el listado
        refrescar();

        //Crea backup de la base de datos
        exportDatabase(Contracts.DATABASE_NAME);

        //Inicia la petición al webservice
        peticion = Volley.newRequestQueue(this.getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        refrescar();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void refrescar(){
        listaCue = todasEncuestas();
        ListadoPasajerosItemAdapter adaptador = new ListadoPasajerosItemAdapter(this, listaCue);
        list_pasajeros.setAdapter(adaptador);

        //Establece la fecha actual
        Calendar currentTime = Calendar.getInstance();
        fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_COMPLETE);
        //Asigna la fecha a visualizar
        txt_fechaActual.setText(sdfDate.format(currentTime.getTime()));
    }

    private ArrayList<CuePasajerosListado> todasEncuestas(){
        SQLiteDatabase db = conn.getReadableDatabase();
        CuePasajerosListado cue = null;
        String[] parametros = {txt_usuario.getText().toString()};

        listaCue = new ArrayList<CuePasajerosListado>();

        Cursor cursor = db.rawQuery("SELECT T1." + Contracts.COLUMN_CUEPASAJEROS_IDEN + ", " +
                        "T3." + Contracts.COLUMN_USUARIOS_NOMBRE + ", " +
                        "T1." + Contracts.COLUMN_CUEPASAJEROS_FECHA + "||' '||" + "T1." + Contracts.COLUMN_CUEPASAJEROS_HORAINICIO + ", " +
                        "COALESCE(T4." + Contracts.COLUMN_IDIOMAS_CLAVE + ",' '), " +
                        "COALESCE(T1." + Contracts.COLUMN_CUEPASAJEROS_NUMVUECA + ",' '), " +
                        "COALESCE(T1." + Contracts.COLUMN_CUEPASAJEROS_PUERTA + ",' '), " +
                        "COALESCE(T1." + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + ",' ') " +
                        " FROM " + Contracts.TABLE_CUEPASAJEROS + " AS T1 LEFT JOIN " +
                                   Contracts.TABLE_AEROPUERTOS + " AS T2 ON T1." + Contracts.COLUMN_CUEPASAJEROS_IDAEROPUERTO + " = T2." + Contracts.COLUMN_AEROPUERTOS_IDEN + " LEFT JOIN " +
                                   Contracts.TABLE_USUARIOS + " AS T3 ON T1." + Contracts.COLUMN_CUEPASAJEROS_IDUSUARIO + " = T3." + Contracts.COLUMN_USUARIOS_IDEN + " LEFT JOIN " +
                                   Contracts.TABLE_IDIOMAS + " AS T4 ON T1." + Contracts.COLUMN_CUEPASAJEROS_IDIDIOMA + " = T4." + Contracts.COLUMN_IDIOMAS_IDEN +
                        " WHERE T3." + Contracts.COLUMN_USUARIOS_NOMBRE + "=? AND T1." + Contracts.COLUMN_CUEPASAJEROS_PREGUNTA + " = " + maxPreg +
                        " ORDER BY T1." + Contracts.COLUMN_CUEPASAJEROS_IDEN, parametros);

        while (cursor.moveToNext()) {
            cue = new CuePasajerosListado(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            listaCue.add(cue);
        }

        cursor.close();

        return listaCue;
    }

    private List<String> getIdiomas(String filtro) {
        List<String> getIdiomas = new ArrayList<String>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + Contracts.COLUMN_IDIOMAS_IDEN + ", " + Contracts.COLUMN_IDIOMAS_IDIOMA +
                " FROM " + Contracts.TABLE_IDIOMAS + " AS T1" +
                " WHERE " + filtro +
                " ORDER BY " + Contracts.COLUMN_IDIOMAS_IDEN , parametros);

        while (cursor.moveToNext()) {
            getIdiomas.add(cursor.getString(1));
        }

        cursor.close();

        return getIdiomas;
    }

    private CuePasajeros acceso(){
        SQLiteDatabase db = conn.getWritableDatabase();
        CuePasajeros cue = null;

        //Establece la fecha actual
        Calendar currentTime = Calendar.getInstance();
        fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_COMPLETE);
        //Asigna la fecha a visualizar
        txt_fechaActual.setText(sdfDate.format(currentTime.getTime()));
        String fecha = txt_fechaActual.getText().toString().substring(0,10);
        String hora = txt_fechaActual.getText().toString().substring(11);

        //Usuario
        String[] usuarios = {txt_usuario.getText().toString()};
        String idUsuario = null;

        Cursor cUsuarios = db.rawQuery("SELECT " + Contracts.COLUMN_USUARIOS_IDEN +
                " FROM " + Contracts.TABLE_USUARIOS + " AS T1" +
                " WHERE " + Contracts.COLUMN_USUARIOS_NOMBRE + "=?", usuarios);

        while (cUsuarios.moveToNext()) {
            idUsuario = cUsuarios.getString(0);
        }

        cUsuarios.close();

        //Nº Encuestador
        String nencdor = txt_usuario.getText().toString().substring(3,3);

        //Aeropuerto
        String[] aeropuerto = {txt_aeropuerto.getText().toString()};
        String idAeropuerto = null;

        Cursor cAeropuerto = db.rawQuery("SELECT " + Contracts.COLUMN_AEROPUERTOS_IDEN + ", " + Contracts.COLUMN_AEROPUERTOS_MODELO +
                " FROM " + Contracts.TABLE_AEROPUERTOS + " AS T1" +
                " WHERE " + Contracts.COLUMN_AEROPUERTOS_NOMBRE + "=?", aeropuerto);

        while (cAeropuerto.moveToNext()) {
            idAeropuerto = cAeropuerto.getString(0);
            modeloCue = cAeropuerto.getInt(1);
        }

        cAeropuerto.close();

        //Idioma
        String[] idioma = {sp_idioma.getSelectedItem().toString()};
        String idIdioma = null;

        Cursor cIdioma = db.rawQuery("SELECT " + Contracts.COLUMN_IDIOMAS_IDEN + ", " + Contracts.COLUMN_IDIOMAS_IDIOMA +
                " FROM " + Contracts.TABLE_IDIOMAS + " AS T1" +
                " WHERE " + Contracts.COLUMN_IDIOMAS_IDIOMA + "=?", idioma);

        while (cIdioma.moveToNext()) {
            idIdioma = cIdioma.getString(0);
        }

        cIdioma.close();

        //Clave única
        String clave = getUniqueKey();

        //Crea el nuevo cuestionario
        db.execSQL("INSERT INTO " + Contracts.TABLE_CUEPASAJEROS + " (" + Contracts.COLUMN_CUEPASAJEROS_IDUSUARIO + ", " + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + ", " + Contracts.COLUMN_CUEPASAJEROS_FECHA + ", " + Contracts.COLUMN_CUEPASAJEROS_HORAINICIO + ", " + Contracts.COLUMN_CUEPASAJEROS_IDAEROPUERTO + ", " + Contracts.COLUMN_CUEPASAJEROS_CLAVE + ", " + Contracts.COLUMN_CUEPASAJEROS_IDIDIOMA + ", " + Contracts.COLUMN_CUEPASAJEROS_NENCDOR + ") VALUES (" + idUsuario + ", 0, '" + fecha + "', '" + hora + "', " + idAeropuerto + ", '" + clave + "', " + idIdioma + ", '" + nencdor + "')");

        //Iden de cuestionario
        String[] iden = {Contracts.TABLE_CUEPASAJEROS};
        int idCue = 0;

        Cursor cIdenCue = db.rawQuery("SELECT seq" +
                " FROM sqlite_sequence" +
                " WHERE name =?" , iden);

        while (cIdenCue.moveToNext()) {
            idCue = cIdenCue.getInt(0);
        }

        cIdenCue.close();

        cue = new CuePasajeros(idCue);
        cue.setIdAeropuerto(stringToInt(idAeropuerto));
        cue.setIdioma(idIdioma);

        return cue;
    }

    public void iniciarCue(View view){
        Intent survey = new Intent(getApplicationContext(), CuePasajerosActivity.class);
        Bundle datosSurvey = new Bundle();

        datosSurvey.putString("encuestador", txt_usuario.getText().toString());
        datosSurvey.putString("aeropuerto", txt_aeropuerto.getText().toString());

        CuePasajeros cue = acceso();

        String fecha = txt_fechaActual.getText().toString().substring(0,10);
        String hora = txt_fechaActual.getText().toString().substring(11);
        String idioma = sp_idioma.getSelectedItem().toString();

        datosSurvey.putString("fecha", fecha);
        datosSurvey.putString("hora", hora);
        datosSurvey.putString("numEncuesta", String.valueOf(cue.getIden()));
        datosSurvey.putInt("idCue", cue.getIden());
        datosSurvey.putInt("idAeropuerto", cue.getIdAeropuerto());
        datosSurvey.putInt("modeloCue", modeloCue);
        datosSurvey.putString("idioma", idioma);

        survey.putExtras(datosSurvey);
        startActivityForResult(survey, 0);
    }

    public void exportDatabase(String databaseName) {
        try {
            File sd = this.getExternalFilesDir("AenaStorage");
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
                String backupDBPath = Contracts.DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public static synchronized String getUniqueKey(){

        Random random = new Random();
        Integer number = random.nextInt(900000)+100000;

        return System.currentTimeMillis()+ String.valueOf(number);
    }

    public int stringToInt (String numeroStr){
        if (numeroStr != null && numeroStr.matches("[0-9.]+")){
            int number = 0;
            try{
                number = Integer.parseInt(numeroStr);
                return number;
            }
            catch (NumberFormatException ex){
                return 0;
            }
        }
        return 0;
    }
}
