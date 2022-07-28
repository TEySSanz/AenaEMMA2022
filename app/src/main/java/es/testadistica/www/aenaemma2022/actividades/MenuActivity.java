package es.testadistica.www.aenaemma2022.actividades;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;
import es.testadistica.www.aenaemma2022.utilidades.Contracts;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.JSONWriter;
import es.testadistica.www.aenaemma2022.utilidades.LogcatHelper;
import es.testadistica.www.aenaemma2022.utilidades.UpdateHelper;

public class MenuActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, UpdateHelper.OnUpdateCheckListener, UpdateHelper.OnNoUpdateCheckListener {

    //private static final String WEBSERVICE = "http://192.168.7.18:8084/AenaEMMA2022/rest";
    //private static final String WEBSERVICE = "http://213.229.135.43:8081/AenaEMMA2022/rest";
    private static final String WEBSERVICE = "http://194.224.27.206:8081/AenaEMMA2022/rest";
    
    DBHelper conn;
    Context context;
    TextView txt_usuario;
    ProgressDialog progreso;
    RequestQueue peticion;
    JsonObjectRequest resultado;
    private StorageReference mStorageRef;
    private String PROVIDER_PATH = ".provider";
    private static final int PERMISSION_REQUEST_CODE = 200;
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
                        if (checkPermission()) {
                            UpdateApp actualizaApp = new UpdateApp();
                            actualizaApp.setContext(MenuActivity.this);
                            actualizaApp.execute("https://www.testadistica.es/img/app-aena.apk");
                        } else {
                            requestPermission();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (locationAccepted && cameraAccepted) {
                    UpdateApp updateApp = new UpdateApp();
                    updateApp.setContext(MenuActivity.this);
                    updateApp.execute("https://www.testadistica.es/img/app-aena.apk");
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public class UpdateApp extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;

        void setContext(Activity context) {
            mContext = context;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mPDialog = new ProgressDialog(mContext);
                    mPDialog.setMessage("Espere...");
                    mPDialog.setIndeterminate(true);
                    mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mPDialog.setCancelable(false);
                    mPDialog.show();
                }
            });
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(arg0[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                int lenghtOfFile = c.getContentLength();

                String PATH = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(PATH);
                boolean isCreate = file.mkdirs();
                File outputFile = new File(file, "my_apk.apk");
                if (outputFile.exists()) {
                    boolean isDelete = outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1;
                long total = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    fos.write(buffer, 0, len1);
                    publishProgress((int) ((total * 100) / lenghtOfFile));
                }
                fos.close();
                is.close();
                if (mPDialog != null)
                    mPDialog.dismiss();
                installApk();
            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPDialog != null)
                mPDialog.show();

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (mPDialog != null) {
                mPDialog.setIndeterminate(false);
                mPDialog.setMax(100);
                mPDialog.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (mPDialog != null)
                mPDialog.dismiss();
            if (result != null)
                Toast.makeText(mContext, "Error en la descarga: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(mContext, "Archivo descargado", Toast.LENGTH_SHORT).show();
        }


        private void installApk() {
            try {
                String PATH = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(PATH + "/my_apk.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri downloaded_apk = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                    List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        mContext.grantUriPermission(mContext.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

                    String ruta = WEBSERVICE + "/envio";
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
                        String fileName = "EMMA_"+ cuestionario.getNencdor() +"_PAS_" + String.valueOf(cuestionario.getClave()) + ".json";
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
                        String fileName = "EMMA_"+ cuestionario.getNencdor() + "_TRAB_" + String.valueOf(cuestionario.getClave()) + ".json";
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

        Cursor cursor = db.rawQuery("SELECT " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_IDEN + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_IDUSUARIO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PREGUNTA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CLAVE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_FECHA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_HORAINICIO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_HORAFIN + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_IDAEROPUERTO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_IDIDIOMA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_MODULO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDSLAB + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_MOTIVOAVION2 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PQFUERA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PREFIERE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_USOAVE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDENTREV + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_FENTREV + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_HENTREV + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NUMVUECA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NENCDOR + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDSEXO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_IDIOMA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDPAISNA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDPAISRE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDLOCADO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_DISTRES + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_DISTRESOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDCAMBIO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CIAANTES + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CONEXFAC + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDSINOPE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDALOJEN + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_VIEN_RE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDLOCACO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_DISTRACCE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_DISTRACCEOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDALOJIN + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDALOJIN_OTROS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NMODOS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_MODO1 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_MODO2 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_ULTIMODO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_ULTIMODOOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_SITIOPARK + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_BUSTERMI + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_ACOMPTES + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_HLLEGA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDTERM + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTOE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_TAUS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NPERS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NNIÑOS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_RELACION + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDTRESER + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDBILLET + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NVIAJE + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_VOL12MES + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_ELECCOVID + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_P44FACTU + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_BULGRUPO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_NPERBUL + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_DROPOFF + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CHEKINB + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CONSUME + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_GAS_CONS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_COMPRART + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_GAS_COM + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PROD1 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PROD2 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PROD3 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PROD4 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PROD5 + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDSPROF + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDEDAD + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_HINI + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_HFIN + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_PUERTA + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_VALOREXP + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CIAANTESOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTODOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDOCIAAROTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDLOCACOOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDIAPTOFOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDPAISNAOTRO + ", " +
                "T1." + Contracts.COLUMN_CUEPASAJEROS_CDPAISREOTRO +
                " FROM " + Contracts.TABLE_CUEPASAJEROS + " AS T1 " +
                        " WHERE T1." + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + "=?" +
                        " ORDER BY T1." + Contracts.COLUMN_CUEPASAJEROS_IDEN, parametros);

        while (cursor.moveToNext()) {
            cue = new CuePasajeros();

            cue.setIden(cursor.getInt(0));
            cue.setIdUsuario(cursor.getInt(1));
            cue.setEnviado(cursor.getInt(2));
            cue.setPregunta(cursor.getInt(3));
            cue.setClave(cursor.getString(4));
            cue.setFecha(cursor.getString(5));
            cue.setHoraInicio(cursor.getString(6));
            cue.setHoraFin(cursor.getString(7));
            cue.setIdAeropuerto(cursor.getInt(8));
            cue.setIdIdioma(cursor.getInt(9));
            cue.setModulo(cursor.getString(10));
            cue.setCdociaar(cursor.getString(11));
            cue.setCdslab(cursor.getString(12));
            cue.setMotivoavion2(cursor.getString(13));
            cue.setPqfuera(cursor.getString(14));
            cue.setPrefiere(cursor.getString(15));
            cue.setUsoave(cursor.getString(16));
            cue.setCdentrev(cursor.getString(17));
            cue.setFentrev(cursor.getString(18));
            cue.setHentrev(cursor.getString(19));
            cue.setNumvueca(cursor.getString(20));
            cue.setNumvuepa(cursor.getString(21));
            cue.setNencdor(cursor.getString(22));
            cue.setCdsexo(cursor.getInt(23));
            cue.setIdioma(cursor.getString(24));
            cue.setCdpaisna(cursor.getString(25));
            cue.setCdpaisre(cursor.getString(26));
            cue.setCdlocado(cursor.getString(27));
            cue.setDistres(cursor.getString(28));
            cue.setDistresotro(cursor.getString(29));
            cue.setCdcambio(cursor.getString(30));
            cue.setCdiaptoo(cursor.getString(31));
            cue.setCiaantes(cursor.getString(32));
            cue.setConexfac(cursor.getString(33));
            cue.setCdsinope(cursor.getString(34));
            cue.setCdalojen(cursor.getString(35));
            cue.setVien_re(cursor.getString(36));
            cue.setCdlocaco(cursor.getString(37));
            cue.setDistracce(cursor.getString(38));
            cue.setDistracceotro(cursor.getString(39));
            cue.setCdalojin(cursor.getString(40));
            cue.setCdalojin_otros(cursor.getString(41));
            cue.setNmodos(cursor.getString(42));
            cue.setModo1(cursor.getString(43));
            cue.setModo2(cursor.getString(44));
            cue.setUltimodo(cursor.getString(45));
            cue.setUltimodootro(cursor.getString(46));
            cue.setSitiopark(cursor.getString(47));
            cue.setBustermi(cursor.getInt(48));
            cue.setAcomptes(cursor.getInt(49));
            cue.setHllega(cursor.getString(50));
            cue.setCdiaptod(cursor.getString(51));
            cue.setCdterm(cursor.getString(52));
            cue.setCdiaptoe(cursor.getString(53));
            cue.setCdiaptof(cursor.getString(54));
            cue.setCdmviaje(cursor.getString(55));
            cue.setCdidavue(cursor.getString(56));
            cue.setTaus(cursor.getInt(57));
            cue.setNpers(cursor.getInt(58));
            cue.setNniños(cursor.getInt(59));
            cue.setRelacion(cursor.getString(60));
            cue.setCdtreser(cursor.getString(61));
            cue.setCdbillet(cursor.getString(62));
            cue.setNviaje(cursor.getString(63));
            cue.setVol12mes(cursor.getString(64));
            cue.setEleccovid(cursor.getString(65));
            cue.setP44factu(cursor.getString(66));
            cue.setBulgrupo(cursor.getString(67));
            cue.setNperbul(cursor.getString(68));
            cue.setDropoff(cursor.getString(69));
            cue.setChekinb(cursor.getInt(70));
            cue.setConsume(cursor.getString(71));
            cue.setGas_cons(cursor.getInt(72));
            cue.setComprart(cursor.getString(73));
            cue.setGas_com(cursor.getInt(74));
            cue.setProd1(cursor.getString(75));
            cue.setProd2(cursor.getString(76));
            cue.setProd3(cursor.getString(77));
            cue.setProd4(cursor.getString(78));
            cue.setProd5(cursor.getString(79));
            cue.setCdsprof(cursor.getString(80));
            cue.setEstudios(cursor.getString(81));
            cue.setCdedad(cursor.getString(82));
            cue.setHini(cursor.getString(83));
            cue.setHfin(cursor.getString(84));
            cue.setPuerta(cursor.getString(85));
            cue.setValorexp(cursor.getInt(86));
            cue.setCdiaptoootro(cursor.getString(87));
            cue.setCiaantesotro(cursor.getString(88));
            cue.setCdiaptodotro(cursor.getString(89));
            cue.setCdociaarotro(cursor.getString(90));
            cue.setCdlocacootro(cursor.getString(91));
            cue.setCdiaptofotro(cursor.getString(92));
            cue.setCdpaisnaotro(cursor.getString(93));
            cue.setCdpaisreotro(cursor.getString(94));

            pendientes.add(cue);
        }

        return pendientes;
    }

    private ArrayList<CueTrabajadores> cueTrabajadoresPendientes() {
        SQLiteDatabase db = conn.getReadableDatabase();
        CueTrabajadores cue = null;
        int a = 0;
        String[] parametros = {String.valueOf(a)};
        ArrayList<CueTrabajadores> pendientes;

        pendientes = new ArrayList<CueTrabajadores>();

        Cursor cursor = db.rawQuery("SELECT " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_IDEN + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_IDUSUARIO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_PREGUNTA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_CLAVE + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_FECHA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORAINICIO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORAFIN + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_IDAEROPUERTO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_IDIDIOMA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NENCDOR + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_CDSEXO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_IDIOMA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_EMPRESA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ACTEMPRE + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ACTEMPREOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_CDLOCADO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISTRES + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISTRESOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_JORNADA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_JORNADAOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NDIASTRAB + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ZONATRAB1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ZONATRAB2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ZONATRAB3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ZONATRAB4 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ZONATRAB5 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ZONATRAB6 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORAENT1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORASAL1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORAENT2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORASAL2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORAENT3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_HORASAL3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NMODOS + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MODO1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MODO2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_ULTIMODO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NOCUCOCHE + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_SATISTRANSPUBLI + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLIOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLIOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DESPLAZATRAB + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLI + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_APARCTRAB + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHEOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISPBICI1 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISPBICI2 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISPBICI3 + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_DISPBICIOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MODOSALIDA + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_MODOSALIDAOTRO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_CDEDADTRAB + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_CDSLAB + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_PUESTO + ", " +
                "T1." + Contracts.COLUMN_CUETRABAJADORES_SUGERENCIAS +
                " FROM " + Contracts.TABLE_CUETRABAJADORES + " AS T1 " +
                        " WHERE T1." + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + "=?" +
                        " ORDER BY T1." + Contracts.COLUMN_CUETRABAJADORES_IDEN, parametros);

        while (cursor.moveToNext()) {
            cue = new CueTrabajadores();

            cue.setIden(cursor.getInt(0));
            cue.setIdUsuario(cursor.getInt(1));
            cue.setEnviado(cursor.getInt(2));
            cue.setPregunta(cursor.getInt(3));
            cue.setClave(cursor.getString(4));
            cue.setFecha(cursor.getString(5));
            cue.setHoraInicio(cursor.getString(6));
            cue.setHoraFin(cursor.getString(7));
            cue.setIdAeropuerto(cursor.getInt(8));
            cue.setIdIdioma(cursor.getInt(9));
            cue.setNencdor(cursor.getString(10));
            cue.setCdsexo(cursor.getInt(11));
            cue.setIdioma(cursor.getString(12));
            cue.setEmpresa(cursor.getString(13));
            cue.setActempre(cursor.getString(14));
            cue.setActempreotro(cursor.getString(15));
            cue.setCdlocado(cursor.getString(16));
            cue.setDistres(cursor.getString(17));
            cue.setDistresotro(cursor.getString(18));
            cue.setJornada(cursor.getString(19));
            cue.setJornadaotro(cursor.getString(20));
            cue.setNdiastrab(cursor.getString(21));
            cue.setZonatrab1(cursor.getInt(22));
            cue.setZonatrab2(cursor.getInt(23));
            cue.setZonatrab3(cursor.getInt(24));
            cue.setZonatrab4(cursor.getInt(25));
            cue.setZonatrab5(cursor.getInt(26));
            cue.setZonatrab6(cursor.getInt(27));
            cue.setHoraent1(cursor.getString(28));
            cue.setHorasal1(cursor.getString(29));
            cue.setHoraent2(cursor.getString(30));
            cue.setHorasal2(cursor.getString(31));
            cue.setHoraent3(cursor.getString(32));
            cue.setHorasal3(cursor.getString(33));
            cue.setNmodos(cursor.getString(34));
            cue.setModo1(cursor.getString(35));
            cue.setModo2(cursor.getString(36));
            cue.setUltimodo(cursor.getString(37));
            cue.setNocucoche(cursor.getString(38));
            cue.setSatistranspubli(cursor.getString(39));
            cue.setValtranspubli1(cursor.getString(40));
            cue.setValtranspubli2(cursor.getString(41));
            cue.setValtranspubli3(cursor.getString(42));
            cue.setValtranspubliotro(cursor.getString(43));
            cue.setMejtranspubli1(cursor.getString(44));
            cue.setMejtranspubli2(cursor.getString(45));
            cue.setMejtranspubli3(cursor.getString(46));
            cue.setMejtranspubliotro(cursor.getString(47));
            cue.setDesplazatrab(cursor.getString(48));
            cue.setNotranspubli1(cursor.getString(49));
            cue.setNotranspubli2(cursor.getString(50));
            cue.setNotranspubli3(cursor.getString(51));
            cue.setNotranspubliotro(cursor.getString(52));
            cue.setDisptranspubli(cursor.getString(53));
            cue.setDisptranspubliotro(cursor.getString(54));
            cue.setImportranspubli(cursor.getString(55));
            cue.setMedtranspubli1(cursor.getString(56));
            cue.setMedtranspubli2(cursor.getString(57));
            cue.setMedtranspubli3(cursor.getString(58));
            cue.setMedtranspubliotro(cursor.getString(59));
            cue.setTiempotranspubli(cursor.getString(60));
            cue.setAparctrab(cursor.getString(61));
            cue.setCompartcoche1(cursor.getString(62));
            cue.setCompartcoche2(cursor.getString(63));
            cue.setCompartcoche3(cursor.getString(64));
            cue.setCompartcocheotro(cursor.getString(65));
            cue.setDispbici1(cursor.getString(66));
            cue.setDispbici2(cursor.getString(67));
            cue.setDispbici3(cursor.getString(68));
            cue.setDispbiciotro(cursor.getString(69));
            cue.setModosalida(cursor.getString(70));
            cue.setModosalidaotro(cursor.getString(71));
            cue.setCdedadtrab(cursor.getString(72));
            cue.setCdslab(cursor.getString(73));
            cue.setPuesto(cursor.getString(74));
            cue.setSugerencias(cursor.getString(75));

            pendientes.add(cue);
        }

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
