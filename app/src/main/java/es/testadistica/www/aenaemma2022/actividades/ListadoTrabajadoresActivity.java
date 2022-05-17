package es.testadistica.www.aenaemma2022.actividades;

import android.app.Activity;
import android.app.ProgressDialog;
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
import es.testadistica.www.aenaemma2022.adaptadores.ListadoTrabajadoresItemAdapter;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadoresListado;
import es.testadistica.www.aenaemma2022.utilidades.Contracts;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.SearchableSpinner;

public class ListadoTrabajadoresActivity extends AppCompatActivity {

    private static final String CARGA_URL = "http://192.168.7.18:8084/AENA/rest/envio";
    //private static final String CARGA_URL = "http://213.229.135.43:8081/AENA/rest/envio";
    private static final String TAG = ListadoTrabajadoresActivity.class.toString();
    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_COMPLETE ="dd/MM/yyyy HH:mm";
    private Date fechaActual = null;
    private ArrayList<CueTrabajadores> listaEncuestas;
    private ArrayList<CueTrabajadoresListado> listaCue;
    private Activity listado = this;
    private int modeloCue = 1;
    private int idAeropuerto = 1;

    TextView txt_usuario;
    TextView txt_fechaActual;
    TextView txt_aeropuerto;
    SearchableSpinner sp_idioma;
    ListView list_trabajadores;
    DBHelper conn;
    ProgressDialog progreso;
    RequestQueue peticion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_trabajadores);

        //Añadir icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Asigna campos a componentes
        txt_usuario = (TextView) findViewById(R.id.txt_usuario);
        txt_fechaActual = (TextView) findViewById(R.id.txt_fechaActual);
        txt_aeropuerto = (TextView) findViewById(R.id.txt_aeropuerto);
        sp_idioma = (SearchableSpinner) findViewById(R.id.spinner_idioma);
        list_trabajadores = (ListView) findViewById(R.id.list_trabajadores);

        //Recoge los parámetros de la pantalla anterior
        Bundle datos = this.getIntent().getExtras();

        if (datos != null) {
            txt_usuario.setText(datos.getString("usuario"));
            txt_aeropuerto.setText(datos.getString("aeropuerto"));
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
        ArrayAdapter<String> idiomaAdapter = new ArrayAdapter<String>(this, R.layout.selection_spinner_item_small, getIdiomas());
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
        ListadoTrabajadoresItemAdapter adaptador = new ListadoTrabajadoresItemAdapter(this, listaCue);
        list_trabajadores.setAdapter(adaptador);

        //Establece la fecha actual
        Calendar currentTime = Calendar.getInstance();
        fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_COMPLETE);
        //Asigna la fecha a visualizar
        txt_fechaActual.setText(sdfDate.format(currentTime.getTime()));
    }

    private ArrayList<CueTrabajadoresListado> todasEncuestas(){
        SQLiteDatabase db = conn.getReadableDatabase();
        CueTrabajadoresListado cue = null;
        String[] parametros = {txt_usuario.getText().toString()};

        listaCue = new ArrayList<CueTrabajadoresListado>();

        Cursor cursor = db.rawQuery("SELECT T1." + Contracts.COLUMN_CUETRABAJADORES_IDEN + ", " +
                        "T3." + Contracts.COLUMN_USUARIOS_NOMBRE + ", " +
                        "T1." + Contracts.COLUMN_CUETRABAJADORES_FECHA + "||' '||" + "T1." + Contracts.COLUMN_CUETRABAJADORES_HORAINICIO + ", " +
                        "COALESCE(T4." + Contracts.COLUMN_IDIOMAS_CLAVE + ",' ') " +
                        " FROM " + Contracts.TABLE_CUETRABAJADORES + " AS T1 LEFT JOIN " +
                                   Contracts.TABLE_AEROPUERTOS + " AS T2 ON T1." + Contracts.COLUMN_CUETRABAJADORES_IDAEROPUERTO + " = T2." + Contracts.COLUMN_AEROPUERTOS_IDEN + " LEFT JOIN " +
                                   Contracts.TABLE_USUARIOS + " AS T3 ON T1." + Contracts.COLUMN_CUETRABAJADORES_IDUSUARIO + " = T3." + Contracts.COLUMN_USUARIOS_IDEN + " LEFT JOIN " +
                                   Contracts.TABLE_IDIOMAS + " AS T4 ON T1." + Contracts.COLUMN_CUETRABAJADORES_IDIDIOMA + " = T4." + Contracts.COLUMN_IDIOMAS_IDEN +
                        " WHERE T3." + Contracts.COLUMN_USUARIOS_NOMBRE + "=? " +
                        " ORDER BY T1." + Contracts.COLUMN_CUETRABAJADORES_IDEN, parametros);

        while (cursor.moveToNext()) {
            cue = new CueTrabajadoresListado(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            listaCue.add(cue);
        }

        return listaCue;
    }

    private List<String> getIdiomas() {
        List<String> getIdiomas = new ArrayList<String>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + Contracts.COLUMN_IDIOMAS_IDEN + ", " + Contracts.COLUMN_IDIOMAS_IDIOMA +
                " FROM " + Contracts.TABLE_IDIOMAS + " AS T1" +
                " ORDER BY " + Contracts.COLUMN_IDIOMAS_IDEN , parametros);

        while (cursor.moveToNext()) {
            getIdiomas.add(cursor.getString(1));
        }

        return getIdiomas;
    }

    private CueTrabajadores acceso(){
        SQLiteDatabase db = conn.getWritableDatabase();
        CueTrabajadores cue = null;

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

        //Aeropuerto
        String[] aeropuerto = {txt_aeropuerto.getText().toString()};
        String idAeropuerto = null;

        Cursor cAeropuerto = db.rawQuery("SELECT " + Contracts.COLUMN_AEROPUERTOS_IDEN + ", " + Contracts.COLUMN_AEROPUERTOS_MODELO +
                " FROM " + Contracts.TABLE_AEROPUERTOS + " AS T1" +
                " WHERE " + Contracts.COLUMN_AEROPUERTOS_NOMBRE + "=?", aeropuerto);

        while (cAeropuerto.moveToNext()) {
            idAeropuerto = cAeropuerto.getString(0);
            this.idAeropuerto = cAeropuerto.getInt(0);
        }

        //Idioma
        String[] idioma = {sp_idioma.getSelectedItem().toString()};
        String idIdioma = null;

        Cursor cIdioma = db.rawQuery("SELECT " + Contracts.COLUMN_IDIOMAS_IDEN + ", " + Contracts.COLUMN_IDIOMAS_IDIOMA +
                " FROM " + Contracts.TABLE_IDIOMAS + " AS T1" +
                " WHERE " + Contracts.COLUMN_IDIOMAS_IDIOMA + "=?", idioma);

        while (cIdioma.moveToNext()) {
            idIdioma = cIdioma.getString(0);
        }

        //Clave única
        String clave = getUniqueKey();

        //Crea el nuevo cuestionario
        db.execSQL("INSERT INTO " + Contracts.TABLE_CUETRABAJADORES + " (" + Contracts.COLUMN_CUETRABAJADORES_IDUSUARIO + ", " + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + ", " + Contracts.COLUMN_CUETRABAJADORES_FECHA + ", " + Contracts.COLUMN_CUETRABAJADORES_HORAINICIO + ", " + Contracts.COLUMN_CUETRABAJADORES_IDAEROPUERTO + ", " + Contracts.COLUMN_CUETRABAJADORES_CLAVE + ", " + Contracts.COLUMN_CUETRABAJADORES_IDIDIOMA + ") VALUES (" + idUsuario + ", 0, '" + fecha + "', '" + hora + "', " + idAeropuerto + ", '" + clave + "', " + idIdioma + ")");

        //Iden de cuestionario
        String[] iden = {Contracts.TABLE_CUETRABAJADORES};
        int idCue = 0;

        Cursor cIdenCue = db.rawQuery("SELECT seq" +
                " FROM sqlite_sequence" +
                " WHERE name =?" , iden);

        while (cIdenCue.moveToNext()) {
            idCue = cIdenCue.getInt(0);
        }

        cue = new CueTrabajadores(idCue, this.idAeropuerto);

        return cue;
    }

    public void iniciarCue(View view){
        Intent survey = new Intent(getApplicationContext(), CueTrabajadoresActivity.class);
        Bundle datosSurvey = new Bundle();

        datosSurvey.putString("encuestador", txt_usuario.getText().toString());
        datosSurvey.putString("aeropuerto", txt_aeropuerto.getText().toString());

        CueTrabajadores cue = acceso();

        String fecha = txt_fechaActual.getText().toString().substring(0,10);
        String hora = txt_fechaActual.getText().toString().substring(11);

        datosSurvey.putString("fecha", fecha);
        datosSurvey.putString("hora", hora);
        datosSurvey.putString("numEncuesta", String.valueOf(cue.getIden()));
        datosSurvey.putInt("idCue", cue.getIden());
        datosSurvey.putInt("modeloCue", modeloCue);
        datosSurvey.putInt("idAeropuerto", idAeropuerto);

        survey.putExtras(datosSurvey);
        startActivityForResult(survey, 0);
    }

    public void enviar(View view){
        /*
        final CharSequence[] opciones = {"Sí", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(ListadoPasajerosActivity.this);
        alertOpciones.setTitle("¿Quiere realizar un envío de la información pendiente?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Sí")) {
                    progreso = new ProgressDialog(ListadoPasajerosActivity.this);
                    progreso.setMessage("Consultando...");
                    progreso.show();

                    String ruta = CARGA_URL;
                    Gson gson = new Gson();

                    listaEncuestas = cuestionariosPendientes();

                    for(Cuestionarios cuestionario: listaEncuestas){
                        progreso.setMessage("Enviando cuestionarios...");
                        final int cuestionarioIden = cuestionario.getIden();
                        String jsonStringBody = gson.toJson(cuestionario);
                        JSONObject jsonBody = null;

                        //Genera el fichero
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                        ParsePosition pos = new ParsePosition(0);
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy HH:mm");
                        //Date fechaCues = formato.parse(cuestionario.getFechaInicio_HC()+" "+cuestionario.getHoraInicio_HC(), pos);
                        Date fechaCues = Calendar.getInstance().getTime();
                        String fileName = "Metro_"+ cuestionario.getIdUsuario() + "_" + cuestionario.getIden() + "_C_"+ sdf.format(fechaCues)+".json";
                        JSONWriter jsonWriter = new JSONWriter();
                        jsonWriter.ficheroCuestionario(listado, cuestionario, fileName);

                        try {
                            jsonBody = new JSONObject(jsonStringBody);
                        } catch (Exception e){
                            Log.e(TAG, "Unable to cast form gson to JSONObject");
                            continue;
                        }

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.POST, ruta+"/cuestionarios", jsonBody, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        marcarEnviadoCuestionario(cuestionarioIden);
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //addFailedQue(queIden);
                                        Log.i(TAG, "Failure when sending questionnaire with iden: " + cuestionarioIden);
                                        error.printStackTrace();
                                    }
                                });

                        peticion.add(jsObjRequest);
                    }

                    progreso.hide();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Se ha completado el proceso de envío", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();

         */
    }

    private void marcarEnviadoCuestionario(int cueIden){
        SQLiteDatabase db = conn.getWritableDatabase();

        db.execSQL("UPDATE " + Contracts.TABLE_CUETRABAJADORES + " SET " + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + " = 1 WHERE " + Contracts.COLUMN_CUETRABAJADORES_IDEN + " = " + cueIden);
    }

    private ArrayList<CueTrabajadores> cuestionariosPendientes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        CuePasajeros cue = null;
        int a = 0;
        String[] parametros = {String.valueOf(a)};
        ArrayList<CueTrabajadores> pendientes;

        pendientes = new ArrayList<CueTrabajadores>();
/*
        Cursor cursor = db.rawQuery("SELECT " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDEN + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDUSUARIO + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_ENVIADO + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_FECHA + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_HORAINICIO + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_HORAFIN + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDLINEA + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDESTACION + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDTRAMO + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F0 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F5 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_F6 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P3_1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P3_2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P3_3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P6_1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P6_2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P6_3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDASPECTO1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDASPECTO2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDASPECTO3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDASPECTO4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDASPECTO5 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_IDASPECTO6 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5A_1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5A_2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5A_3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5A_4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5A_5 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5A_6 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5B_1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5B_2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5B_3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5B_4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5B_5 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P5B_6 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P15 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P16A + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P16B + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P17_1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P17_2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P17_3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P17_4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P17_5 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P17_6 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_1 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_2 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_3 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_4 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_5 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_6 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_7 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_8 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_9 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_10 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_11 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_12 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_13 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_14 + ", " +
                        "T1." + Contracts.COLUMN_CUESTIONARIOS_P14_15 +
                        " FROM " + Contracts.TABLE_CUESTIONARIOS + " AS T1 " +
                        " WHERE T1." + Contracts.COLUMN_CUESTIONARIOS_ENVIADO + "=?" +
                        " ORDER BY T1." + Contracts.COLUMN_CUESTIONARIOS_IDEN, parametros);

        while (cursor.moveToNext()) {
            cue = new CuePasajeros();

            cue.setIden(cursor.getInt(0));
            cue.setIdUsuario(cursor.getInt(1));
            cue.setEnviado(cursor.getInt(2));
            cue.setFecha(cursor.getString(3));
            cue.setHoraInicio(cursor.getString(4));
            cue.setHoraFin(cursor.getString(5));
            cue.setIdLinea(cursor.getInt(6));
            cue.setIdEstacion(cursor.getInt(7));
            cue.setIdTramo(cursor.getInt(8));
            cue.setF0(cursor.getInt(9));
            cue.setF1(cursor.getInt(10));
            cue.setF2(cursor.getInt(11));
            cue.setF3(cursor.getInt(12));
            cue.setF4(cursor.getInt(13));
            cue.setF5(cursor.getInt(14));
            cue.setF6(cursor.getInt(15));
            cue.setP1(cursor.getInt(16));
            cue.setP3_1(cursor.getString(17));
            cue.setP3_2(cursor.getString(18));
            cue.setP3_3(cursor.getString(19));
            cue.setP6_1(cursor.getString(20));
            cue.setP6_2(cursor.getString(21));
            cue.setP6_3(cursor.getString(22));
            cue.setP4(cursor.getInt(23));
            cue.setIdAspecto1(cursor.getInt(24));
            cue.setIdAspecto2(cursor.getInt(25));
            cue.setIdAspecto3(cursor.getInt(26));
            cue.setIdAspecto4(cursor.getInt(27));
            cue.setIdAspecto5(cursor.getInt(28));
            cue.setIdAspecto6(cursor.getInt(29));
            cue.setP5A_1(cursor.getInt(30));
            cue.setP5A_2(cursor.getInt(31));
            cue.setP5A_3(cursor.getInt(32));
            cue.setP5A_4(cursor.getInt(33));
            cue.setP5A_5(cursor.getInt(34));
            cue.setP5A_6(cursor.getInt(35));
            cue.setP5B_1(cursor.getInt(36));
            cue.setP5B_2(cursor.getInt(37));
            cue.setP5B_3(cursor.getInt(38));
            cue.setP5B_4(cursor.getInt(39));
            cue.setP5B_5(cursor.getInt(40));
            cue.setP5B_6(cursor.getInt(41));
            cue.setP15(cursor.getInt(42));
            cue.setP16A(cursor.getString(43));
            cue.setP16B(cursor.getString(44));
            cue.setP17_1(cursor.getInt(45));
            cue.setP17_2(cursor.getInt(46));
            cue.setP17_3(cursor.getInt(47));
            cue.setP17_4(cursor.getInt(48));
            cue.setP17_5(cursor.getInt(49));
            cue.setP17_6(cursor.getInt(50));
            cue.setP14_1(cursor.getInt(51));
            cue.setP14_2(cursor.getInt(52));
            cue.setP14_3(cursor.getInt(53));
            cue.setP14_4(cursor.getInt(54));
            cue.setP14_5(cursor.getInt(55));
            cue.setP14_6(cursor.getInt(56));
            cue.setP14_7(cursor.getInt(57));
            cue.setP14_8(cursor.getInt(58));
            cue.setP14_9(cursor.getInt(59));
            cue.setP14_10(cursor.getInt(60));
            cue.setP14_11(cursor.getInt(61));
            cue.setP14_12(cursor.getInt(62));
            cue.setP14_13(cursor.getInt(63));
            cue.setP14_14(cursor.getInt(64));
            cue.setP14_15(cursor.getInt(65));

            pendientes.add(cue);
        }*/

        return pendientes;
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
}
