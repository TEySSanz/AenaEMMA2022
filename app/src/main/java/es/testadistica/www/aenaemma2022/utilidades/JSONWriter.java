package es.testadistica.www.aenaemma2022.utilidades;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;

public class JSONWriter {
    private static String appFilePath ="MetroStorage";

    public void ficheroCuePasajeros(Activity act, CuePasajeros cuestionario, String fileName){
        Gson gson = new Gson();
        //Object to JSON in file
        String s = gson.toJson(cuestionario);
        File file;

        file = new File (act.getExternalFilesDir(appFilePath), fileName);
        if(file.getParentFile().mkdirs()) {
            String debug = "Esto existe";
        }

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ficheroCueTrabajadores(Activity act, CueTrabajadores cuestionario, String fileName){
        Gson gson = new Gson();
        //Object to JSON in file
        String s = gson.toJson(cuestionario);
        File file;

        file = new File (act.getExternalFilesDir(appFilePath), fileName);
        if(file.getParentFile().mkdirs()) {
            String debug = "Esto existe";
        }

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getStorageDir(String fileName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(null), fileName);
        if (!file.mkdirs()) {
            Log.e("LOG_TAG", "Directory not created");
        }

        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }
}
