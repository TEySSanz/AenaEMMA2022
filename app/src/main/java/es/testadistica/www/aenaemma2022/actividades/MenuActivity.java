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
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.testadistica.www.aenaemma2022.BuildConfig;
import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.utilidades.Contracts;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.LogcatHelper;
import es.testadistica.www.aenaemma2022.utilidades.PermissionCheck;
import es.testadistica.www.aenaemma2022.utilidades.UpdateHelper;

public class MenuActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, UpdateHelper.OnUpdateCheckListener, UpdateHelper.OnNoUpdateCheckListener {

    private static final String DESCARGA_URL = "http://192.168.7.18:8084/AENA/rest/actualizacion";
    //private static final String DESCARGA_URL = "http://213.229.135.43:8081/AENA/rest/actualizacion";

    DBHelper conn;
    Context context;
    TextView txt_usuario;
    ProgressDialog progreso;
    RequestQueue peticion;
    JsonObjectRequest resultado;
    private StorageReference mStorageRef;
    private String PROVIDER_PATH = ".provider";

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

        datos.putString("usuario", txt_usuario.getText().toString());
        datos.putString("aeropuerto", aeropuerto);

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

                    String ruta = DESCARGA_URL + "/" + txt_usuario.getText().toString();
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
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Se ha producido un problema durante el envío", Toast.LENGTH_LONG).show();
                    }

                    Toast.makeText(getApplicationContext(), "No existen backups para enviar", Toast.LENGTH_LONG).show();
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
}
