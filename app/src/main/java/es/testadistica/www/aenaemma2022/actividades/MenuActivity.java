package es.testadistica.www.aenaemma2022.actividades;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.testadistica.www.aenaemma2022.BuildConfig;
import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;
import es.testadistica.www.aenaemma2022.utilidades.Contracts;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.JSONWriter;
import es.testadistica.www.aenaemma2022.utilidades.LogcatHelper;
import es.testadistica.www.aenaemma2022.utilidades.PermissionCheck;
import es.testadistica.www.aenaemma2022.utilidades.UpdateHelper;

public class MenuActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, UpdateHelper.OnUpdateCheckListener, UpdateHelper.OnNoUpdateCheckListener {

    //private static final String WEBSERVICE = "http://192.168.7.18:8084/AenaEMMA2022/rest/";
    private static final String WEBSERVICE = "http://213.229.135.43:8081/AenaEMMA2022/rest/";

    DBHelper conn;
    Context context;
    TextView txt_usuario;
    ProgressDialog progreso;
    RequestQueue peticion;
    JsonObjectRequest resultado;
    private StorageReference mStorageRef;
    private String PROVIDER_PATH = ".provider";
    private static final String TAG = MenuActivity.class.toString();
    private ArrayList<CuePasajeros> listaPasajeros;
    private ArrayList<CueTrabajadores> listaTrabajadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Añadir icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Asigna campos
        txt_usuario = (TextView) findViewById(R.id.txt_usuario);

        //Recoge los parámetros de la pantalla anterior
        Bundle datos = this.getIntent().getExtras();

        if (datos != null) {
            txt_usuario.setText(datos.getString("usuario"));
        }

        //BBDD
        conn = new DBHelper(this.getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();

        //Inicia la instancia para el envío del backup
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Inicia la petición al webservice
        peticion = Volley.newRequestQueue(this.getApplicationContext());

        //Ocultar el cuestionario de trabajadores cuando el aeropuerto no es uno de los selecionados para hacer el estudio
        Button cuestionarioTrabajadores = (Button) findViewById(R.id.btn_cueTrabajadores);
        switch (obtenerAeropuerto(txt_usuario.getText().toString())){
            case 1:
                //Aeropuerto Madrid-Barajas
                cuestionarioTrabajadores.setVisibility(View.VISIBLE);
                break;
            default:
                cuestionarioTrabajadores.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "No se pudo consultar " + error.toString(), Toast.LENGTH_LONG).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        ejecutaUpdate(response);
    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Nueva versión disponible")
                .setMessage("Por favor, actualice a la nueva versión para continuar trabajando")
                .setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), ""+urlApp, Toast.LENGTH_LONG).show();
                        //String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                        String destination = MenuActivity.this.getExternalFilesDir("AenaStorage") + "/";
                        String fileName = "app-aena.apk";
                        destination += fileName;
                        final Uri uri = Uri.parse("file://" + destination);

                        //Delete update file if exists
                        final File file = new File(destination);
                        if (file.exists()) {
                            file.delete();
                        }

                        //get url of app on server
                        //String url = UpdateHelper.KEY_UPDATE_URL;
                        String url = "https://www.testadistica.es/img/app-aena.apk";

                        //set downloadmanager
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.downloading), Toast.LENGTH_LONG).show();
                        //request.setDescription(Main.this.getString(R.string.notification_description));
                        //request.setTitle(Main.this.getString(R.string.app_name));

                        //set destination
                        request.setDestinationUri(uri);

                        if (PermissionCheck.readAndWriteExternalStorage(MenuActivity.this)) {

                            //get download service and enqueue file
                            final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            final long downloadId = manager.enqueue(request);

                            //set BroadcastReceiver to install app when .apk is downloaded
                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context ctxt, Intent intent) {
                                    /*Intent install = new Intent(Intent.ACTION_VIEW);
                                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    install.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory() + fileName),
                                            manager.getMimeTypeForDownloadedFile(downloadId));
                                    startActivity(install);

                                    unregisterReceiver(this);
                                    finish();*/
                                    Uri contentUri = FileProvider.getUriForFile(
                                            getApplicationContext(),
                                            BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                                            file
                                    );
                                    Intent install = new Intent(Intent.ACTION_VIEW);
                                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                    install.setData(contentUri);
                                    startActivity(install);
                                    unregisterReceiver(this);
                                    // finish()
                                }
                            };

                            //register receiver for when .apk download is compete
                            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }

                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        alertDialog.show();
    }

    @Override
    public void onNoUpdateCheckListener() {
        Toast.makeText(getApplicationContext(), "Ya dispone de la última versión por lo que no es necesario actualizar", Toast.LENGTH_LONG).show();
    }

    public void accederEncuestas(View view){
        Intent encuestas = new Intent(this, ListadoPasajerosActivity.class);
        Bundle datos = new Bundle();

        //Obtenemos el nombre del aeropuerto
        String aeropuerto = null;
        String idAeropuerto = null;
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] usuarios = {txt_usuario.getText().toString()};

        Cursor cUsuarios = db.rawQuery("SELECT T2." + Contracts.COLUMN_AEROPUERTOS_NOMBRE +", T2."+Contracts.COLUMN_AEROPUERTOS_IDEN+
                " FROM " + Contracts.TABLE_USUARIOS + " AS T1 INNER JOIN " +
                           Contracts.TABLE_AEROPUERTOS + " AS T2 ON T1.idAeropuerto = T2.iden " +
                " WHERE T1." + Contracts.COLUMN_USUARIOS_NOMBRE + "=?", usuarios);

        while (cUsuarios.moveToNext()) {
            aeropuerto = cUsuarios.getString(0);
            idAeropuerto = cUsuarios.getString(1);
        }

        cUsuarios.close();

        datos.putString("usuario", txt_usuario.getText().toString());
        datos.putString("aeropuerto", aeropuerto);
        datos.putString("idAeropuerto", idAeropuerto);

        encuestas.putExtras(datos);
        startActivity(encuestas);
    }

    public void accederTrabajadores(View view){
        Intent encuestas = new Intent(this, ListadoTrabajadoresActivity.class);
        Bundle datos = new Bundle();

        //Obtenemos el nombre del aeropierto
        String aeropuerto = null;
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] usuarios = {txt_usuario.getText().toString()};

        Cursor cUsuarios = db.rawQuery("SELECT T2." + Contracts.COLUMN_AEROPUERTOS_NOMBRE +
                " FROM " + Contracts.TABLE_USUARIOS + " AS T1 INNER JOIN " +
                Contracts.TABLE_AEROPUERTOS + " AS T2 ON T1.idAeropuerto = T2.iden " +
                " WHERE T1." + Contracts.COLUMN_USUARIOS_NOMBRE + "=?", usuarios);

        while (cUsuarios.moveToNext()) {
            aeropuerto = cUsuarios.getString(0);
        }

        cUsuarios.close();

        datos.putString("usuario", txt_usuario.getText().toString());
        datos.putString("aeropuerto", aeropuerto);

        encuestas.putExtras(datos);
        startActivity(encuestas);
    }

    private void ejecutaUpdate(JSONObject resultado){
        SQLiteDatabase db = conn.getWritableDatabase();
        JSONArray total;
        JSONArray codigo;
        String conteo;
        String codigoTxt;

        try {
            total = resultado.getJSONArray("Total");
            conteo = total.getJSONObject(0).getString("conteo");

            if(conteo.equals("0")){
                Toast.makeText(getApplicationContext(), "No existen nuevas actualizaciones", Toast.LENGTH_LONG).show();
            } else {
                codigo = resultado.getJSONArray("Codigo");

                for (int i = 0; i < codigo.length(); i++) {
                    codigoTxt = codigo.getJSONObject(i).getString("codigo");
                    db.execSQL(codigoTxt);
                }

                Toast.makeText(getApplicationContext(), "Se ha completado la actualización correctamente", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Se ha producido un problema durante la actualización", Toast.LENGTH_LONG).show();
        }
    }

    public void transferir(View view) {
        final CharSequence[] opciones = {"Sí", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(MenuActivity.this);
        alertOpciones.setTitle("¿Quiere realizar un envío de la información pendiente?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Sí")) {
                    progreso = new ProgressDialog(MenuActivity.this);
                    progreso.setMessage("Consultando...");
                    progreso.show();

                    String ruta = WEBSERVICE + "/carga";
                    Gson gson = new Gson();

                    //Envío Pasajeros
                    listaPasajeros = cuePasajerosPendientes();

                    for(CuePasajeros cuestionario: listaPasajeros){
                        progreso.setMessage("Enviando cuestionarios de pasajeros...");
                        final int cuestionarioIden = cuestionario.getIden();
                        String jsonStringBody = gson.toJson(cuestionario);
                        JSONObject jsonBody = null;

                        //Genera el fichero
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                        ParsePosition pos = new ParsePosition(0);
                        Date fechaCues = sdf.parse(cuestionario.getFecha()+" "+cuestionario.getHoraInicio(), pos);
                        String fileName = "EMMA_"+ cuestionario.getNencdor() + "_" + sdf.format(fechaCues) + "_PAS_" + String.valueOf(cuestionario.getIden()) + ".json";
                        JSONWriter jsonWriter = new JSONWriter();
                        jsonWriter.ficheroCuePasajeros(MenuActivity.this, cuestionario, fileName);

                        try {
                            jsonBody = new JSONObject(jsonStringBody);
                        } catch (Exception e){
                            Log.e(TAG, "Unable to cast form gson to JSONObject");
                            continue;
                        }

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.POST, ruta+"/cuepasajeros", jsonBody, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        marcarEnviadoCuestionario(cuestionarioIden, Contracts.TABLE_CUEPASAJEROS);
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //addFailedQue(queIden);
                                        Log.i(TAG, "Error al enviar el cuestionario de pasajeros con iden: " + cuestionarioIden);
                                        error.printStackTrace();
                                    }
                                });

                        peticion.add(jsObjRequest);
                    }

                    //Envío Trabajadores
                    listaTrabajadores = cueTrabajadoresPendientes();

                    for(CueTrabajadores cuestionario: listaTrabajadores){
                        progreso.setMessage("Enviando cuestionarios de trabajadores...");
                        final int cuestionarioIden = cuestionario.getIden();
                        String jsonStringBody = gson.toJson(cuestionario);
                        JSONObject jsonBody = null;

                        //Genera el fichero
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                        ParsePosition pos = new ParsePosition(0);
                        Date fechaCues = sdf.parse(cuestionario.getFecha()+" "+cuestionario.getHoraInicio(), pos);
                        String fileName = "EMMA_"+ cuestionario.getNencdor() + "_" + sdf.format(fechaCues) + "_TRAB_" + String.valueOf(cuestionario.getIden()) + ".json";
                        JSONWriter jsonWriter = new JSONWriter();
                        jsonWriter.ficheroCueTrabajadores(MenuActivity.this, cuestionario, fileName);

                        try {
                            jsonBody = new JSONObject(jsonStringBody);
                        } catch (Exception e){
                            Log.e(TAG, "Unable to cast form gson to JSONObject");
                            continue;
                        }

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.POST, ruta+"/cuetrabajadores", jsonBody, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        marcarEnviadoCuestionario(cuestionarioIden, Contracts.TABLE_CUETRABAJADORES);
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //addFailedQue(queIden);
                                        Log.i(TAG, "Error al enviar el cuestionario de trabajadores con iden: " + cuestionarioIden);
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
    }

    private void marcarEnviadoCuestionario(int cueIden, String tabla){
        SQLiteDatabase db = conn.getWritableDatabase();

        db.execSQL("UPDATE " + tabla + " SET enviado = 1 WHERE iden = " + cueIden);
    }

    private ArrayList<CuePasajeros> cuePasajerosPendientes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        CuePasajeros cue = null;
        int a = 0;
        String[] parametros = {String.valueOf(a)};
        ArrayList<CuePasajeros> pendientes;

        pendientes = new ArrayList<CuePasajeros>();
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

    private ArrayList<CueTrabajadores> cueTrabajadoresPendientes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        CueTrabajadores cue = null;
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

    public void actualizaDatos(View view){
        final CharSequence[] opciones = {"Sí", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(MenuActivity.this);
        alertOpciones.setTitle("¿Quiere comprobar si existen actualizaciones?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Sí")) {
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    progreso = new ProgressDialog(MenuActivity.this);
                    progreso.setMessage("Consultando...");
                    progreso.show();

                    String ruta = WEBSERVICE + "/actualizacion/" + txt_usuario.getText().toString();
                    resultado = new JsonObjectRequest(Request.Method.GET, ruta, null, MenuActivity.this, MenuActivity.this);
                    peticion.add(resultado);
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    public void comprobarVersion (View view) {
        final CharSequence[] opciones = {"Sí", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(MenuActivity.this);
        alertOpciones.setTitle("¿Quiere comprobar si existe una nueva versión disponible?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Sí")) {
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    progreso = new ProgressDialog(MenuActivity.this);
                    progreso.setMessage("Consultando...");
                    progreso.show();

                    UpdateHelper.with(MenuActivity.this)
                            .onUpdateCheck(MenuActivity.this, MenuActivity.this)
                            .check();

                    progreso.dismiss();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    public void enviaBackup (View view) {
        final CharSequence[] opciones = {"Sí", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(MenuActivity.this);
        alertOpciones.setTitle("¿Quiere realizar el envío de la copia de seguridad?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Sí")) {
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    progreso = new ProgressDialog(MenuActivity.this);
                    progreso.setMessage("Consultando...");
                    progreso.show();

                    try {
                        File sd = MenuActivity.this.getExternalFilesDir("AenaStorage");
                        File data = Environment.getDataDirectory();

                        if (sd.canWrite()) {
                            String currentDBPath = "//data//"+getPackageName()+"//databases//"+ Contracts.DATABASE_NAME+"";
                            String backupDBPath = Contracts.DATABASE_NAME;
                            File currentDB = new File(data, currentDBPath);
                            File backupDB = new File(sd, backupDBPath);

                            String currentLogPath = sd.getAbsolutePath()+"/log/logcat.log";
                            File currentLog = new File(currentLogPath);

                            if (currentDB.exists()) {
                                FileChannel src = new FileInputStream(currentDB).getChannel();
                                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                dst.transferFrom(src, 0, src.size());
                                src.close();
                                dst.close();
                            }

                            if (backupDB.exists() && currentLog.exists()) {
                                Uri file = Uri.fromFile(backupDB);
                                Uri log = Uri.fromFile(currentLog);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                                Date fechaBackup = Calendar.getInstance().getTime();

                                StorageReference backupRef = mStorageRef.child("backups/"+txt_usuario.getText()+"_"+sdf.format(fechaBackup)+".db");
                                StorageReference logRef = mStorageRef.child("logs/"+txt_usuario.getText()+"_"+sdf.format(fechaBackup)+".log");

                                backupRef.putFile(file)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get a URL to the uploaded content
                                                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                Toast.makeText(getApplicationContext(), "Se ha completado el envío del backup", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                Toast.makeText(getApplicationContext(), "Se ha producido un problema durante el envío del backup", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                logRef.putFile(log)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                // Get a URL to the uploaded content
                                                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                Toast.makeText(getApplicationContext(), "Se ha completado el envío del log", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                Toast.makeText(getApplicationContext(), "Se ha producido un problema durante el envío del log", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(getApplicationContext(), "No existen backups para enviar", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Se ha producido un problema durante el envío", Toast.LENGTH_LONG).show();
                    }

                    progreso.dismiss();
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    public void cerrarSesion(View view) {
        Intent cerrar = new Intent(this, LoginActivity.class);

        LogcatHelper.getInstance((getApplicationContext())).stop();
        startActivity(cerrar);
        finish();
    }

    public void salir(View view) {
        finish();
        System.exit(0);
    }

    public int obtenerAeropuerto(String usuario){

        //Obtenemos el nombre del aeropuerto
        String aeropuerto = null;
        int idAeropuerto = 0;
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] usuarios = {usuario};

        Cursor cUsuarios = db.rawQuery("SELECT T2." + Contracts.COLUMN_AEROPUERTOS_NOMBRE +", T2."+Contracts.COLUMN_AEROPUERTOS_IDEN+
                " FROM " + Contracts.TABLE_USUARIOS + " AS T1 INNER JOIN " +
                Contracts.TABLE_AEROPUERTOS + " AS T2 ON T1.idAeropuerto = T2.iden " +
                " WHERE T1." + Contracts.COLUMN_USUARIOS_NOMBRE + "=?", usuarios);

        while (cUsuarios.moveToNext()) {
            aeropuerto = cUsuarios.getString(0);
            idAeropuerto = cUsuarios.getInt(1);
        }

        cUsuarios.close();

        return idAeropuerto;
    }
}
