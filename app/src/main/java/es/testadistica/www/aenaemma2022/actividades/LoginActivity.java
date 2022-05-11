package es.testadistica.www.aenaemma2022.actividades;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.FileProvider;

import java.io.File;

import es.testadistica.www.aenaemma2022.BuildConfig;
import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.utilidades.Contracts;
import es.testadistica.www.aenaemma2022.utilidades.DBHelper;
import es.testadistica.www.aenaemma2022.utilidades.LogcatHelper;
import es.testadistica.www.aenaemma2022.utilidades.PermissionCheck;
import es.testadistica.www.aenaemma2022.utilidades.UpdateHelper;

public class LoginActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener, UpdateHelper.OnNoUpdateCheckListener {

    EditText input_usuario, input_contrasena;
    DBHelper conn;
    Context context;

    private String PROVIDER_PATH = ".provider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Añadir icono en el ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Permisos
        //PermissionCheck.location(this);
        PermissionCheck.readAndWriteExternalStorage(this);

        //BBDD
        conn = new DBHelper(this.getApplicationContext());
        SQLiteDatabase db = conn.getReadableDatabase();

        //Asigna campos
        input_usuario = (EditText) findViewById(R.id.input_usuario);
        input_contrasena = (EditText) findViewById(R.id.input_contrasena);

        //Creación Log
        if (PermissionCheck.readAndWriteExternalStorage(LoginActivity.this)) {
            LogcatHelper.getInstance((getApplicationContext())).start();
        }

        UpdateHelper.with(this)
                .onUpdateCheck(this, this)
                .check();
    }

    public void confirmLogin(View view){
        if (input_usuario.getText().toString().isEmpty()==true || input_contrasena.getText().toString().isEmpty()==true) {
            Toast.makeText(getApplicationContext(), "Faltan datos por completar", Toast.LENGTH_LONG).show();
        } else {

            SQLiteDatabase db = conn.getReadableDatabase();
            String[] parametros = {input_usuario.getText().toString()};
            String contrasena = "";

            try {
                Cursor cursor = db.rawQuery("SELECT " + Contracts.COLUMN_USUARIOS_NOMBRE + ", " + Contracts.COLUMN_USUARIOS_PASSWORD +
                        " FROM " + Contracts.TABLE_USUARIOS +
                        " WHERE " + Contracts.COLUMN_USUARIOS_NOMBRE + "=? ", parametros);

                cursor.moveToFirst();
                contrasena = cursor.getString(1);
                cursor.close();

                if (contrasena.equals(input_contrasena.getText().toString())) {
                    Intent acceder = new Intent(this, MenuActivity.class);
                    Bundle datos = new Bundle();

                    datos.putString("usuario", input_usuario.getText().toString());

                    acceder.putExtras(datos);
                    startActivity(acceder);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "La contraseña no es correcta", Toast.LENGTH_LONG).show();
                    input_contrasena.setText(null);
                }
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_LONG).show();
                input_usuario.setText(null);
            }

            db.close();
        }
    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Nueva versión disponible")
                .setMessage("Por favor, actualice a la nueva versión para continuar trabajando")
                .setPositiveButton("ACTUALIZAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), ""+urlApp, Toast.LENGTH_LONG).show();
                        //String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
                        String destination = LoginActivity.this.getExternalFilesDir("AenaStorage") + "/";
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

                        if (PermissionCheck.readAndWriteExternalStorage(LoginActivity.this)) {

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

    }
}
