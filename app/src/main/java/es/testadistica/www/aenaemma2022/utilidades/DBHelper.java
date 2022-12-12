package es.testadistica.www.aenaemma2022.utilidades;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;

public class DBHelper extends SQLiteOpenHelper {

    private int idAeropuerto;
    private CuePasajeros cue;

    public DBHelper(Context context) {
        super(context, Contracts.DATABASE_NAME, null, Contracts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creación de tablas y vistas
        actualizarTablasDiccionario(db);

        //CuePasajeros
        db.execSQL(Contracts.SQL_DROP_CUEPASAJEROS);
        db.execSQL(Contracts.SQL_CREATE_CUEPASAJEROS);

        //CueTrabajadores
        db.execSQL(Contracts.SQL_DROP_CUETRABAJADORES);
        db.execSQL(Contracts.SQL_CREATE_CUETRABAJADORES);

        //Version
        db.execSQL(Contracts.SQL_DROP_VERSION);
        db.execSQL(Contracts.SQL_CREATE_VERSION);
        DBInsert.insertsVersion(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        actualizarTablasDiccionario(db);
        String version = "";
        Cursor cursor = db.rawQuery("SELECT " + Contracts.COLUMN_VERSION_VERSION + " FROM " + Contracts.TABLE_VERSION, null);
        while(cursor.moveToNext()){
            version = cursor.getString(0);
        }

        if (version.startsWith("v1.")) {
            int punto = version.indexOf(".");
            if (punto>0){
                String st_subVersion = version.substring(punto+1, version.length());
                int subVersion = stringToInt(st_subVersion);
                if (subVersion <= 6){ //Si la versión que tiene instalada la tablet es menor o igual a la v1.6 se intentan añadir las columnas.
                    addColumn(db, Contracts.TABLE_CUEPASAJEROS, Contracts.COLUMN_CUEPASAJEROS_MOTIVOAVION2OTRO);
                    addColumn(db, Contracts.TABLE_CUEPASAJEROS, Contracts.COLUMN_CUEPASAJEROS_CDIAPTOFOTRO);
                }
                if (subVersion <= 9){ //Si la versión que tiene instalada la tablet es menor o igual a la v1.9 se intentan añadir las columnas.
                    addColumn(db, Contracts.TABLE_CUEPASAJEROS, Contracts.COLUMN_CUEPASAJEROS_CDPAISNAOTRO);
                    addColumn(db, Contracts.TABLE_CUEPASAJEROS, Contracts.COLUMN_CUEPASAJEROS_CDPAISREOTRO);
                }
                if (subVersion <= 11){ //Si la versión que tiene instalada la tablet es menor o igual a la v1.11 se intentan añadir las columnas.
                    addColumn(db, Contracts.TABLE_CUEPASAJEROS, Contracts.COLUMN_CUEPASAJEROS_CDLOCACOOTRO);

                }
                if (subVersion <= 24){ //Si la versión que tiene instalada la tablet es menor o igual a la v1.24 se intentan añadir las columnas.
                    addColumn(db, Contracts.TABLE_CUETRABAJADORES, Contracts.COLUMN_CUETRABAJADORES_VELECAEROP);

                }
            }
        }

        //Version
        db.execSQL(Contracts.SQL_DROP_VERSION);
        db.execSQL(Contracts.SQL_CREATE_VERSION);
        DBInsert.insertsVersion(db);

    }

    public void actualizarTablasDiccionario (SQLiteDatabase db){
        //Usuarios
        db.execSQL(Contracts.SQL_DROP_USUARIOS);
        db.execSQL(Contracts.SQL_CREATE_USUARIOS);
        DBInsert.insertsUsuarios(db);

        //Aeropuertos
        db.execSQL(Contracts.SQL_DROP_AEROPUERTOS);
        db.execSQL(Contracts.SQL_CREATE_AEROPUERTOS);
        DBInsert.insertsAeropuertos(db);

        //Idiomas
        db.execSQL(Contracts.SQL_DROP_IDIOMAS);
        db.execSQL(Contracts.SQL_CREATE_IDIOMAS);
        DBInsert.insertsIdiomas(db);

        //TipoAeropuertos
        db.execSQL(Contracts.SQL_DROP_TIPOAEROPUERTOS);
        db.execSQL(Contracts.SQL_CREATE_TIPOAEROPUERTOS);
        DBInsert.insertsTipoAeropuertos_Parte1(db);
        DBInsert.insertsTipoAeropuertos_Parte2(db);

        //TipoCompanias
        db.execSQL(Contracts.SQL_DROP_TIPOCOMPANIAS);
        db.execSQL(Contracts.SQL_CREATE_TIPOCOMPANIAS);
        DBInsert.insertsTipoCompanias(db);

        //TipoDistritos
        db.execSQL(Contracts.SQL_DROP_TIPODISTRITOS);
        db.execSQL(Contracts.SQL_CREATE_TIPODISTRITOS);
        DBInsert.insertsTipoDistritos(db);

        //TipoMotivoViaje
        db.execSQL(Contracts.SQL_DROP_TIPOMOTIVOVIAJE);
        db.execSQL(Contracts.SQL_CREATE_TIPOMOTIVOVIAJE);
        DBInsert.insertsTipoMotivoViaje(db);

        //TipoMunicipios
        db.execSQL(Contracts.SQL_DROP_TIPOMUNICIPIOS);
        db.execSQL(Contracts.SQL_CREATE_TIPOMUNICIPIOS);
        DBInsert.insertsTipoMunicipios(db);

        //TipoPaises
        db.execSQL(Contracts.SQL_DROP_TIPOPAISES);
        db.execSQL(Contracts.SQL_CREATE_TIPOPAISES);
        DBInsert.insertsTipoPaises(db);

        //TipoPaises1y2
        db.execSQL(Contracts.SQL_DROP_TIPOPAISES1Y2);
        db.execSQL(Contracts.SQL_CREATE_TIPOPAISES1Y2);
        DBInsert.insertsTipoPaises1y2(db);

        //TipoProductos
        db.execSQL(Contracts.SQL_DROP_TIPOPRODUCTOS);
        db.execSQL(Contracts.SQL_CREATE_TIPOPRODUCTOS);
        DBInsert.insertsTipoProductos(db);

        //TipoProvincias
        db.execSQL(Contracts.SQL_DROP_TIPOPROVINCIAS);
        db.execSQL(Contracts.SQL_CREATE_TIPOPROVINCIAS);
        DBInsert.insertsTipoProvincias(db);

        //TipoActEmpTrab
        db.execSQL(Contracts.SQL_DROP_TIPOACTEMPTRAB);
        db.execSQL(Contracts.SQL_CREATE_TIPOACTEMPTRAB);
        DBInsert.insertsTipoActEmpTrab(db);

        //TipoBarrios
        db.execSQL(Contracts.SQL_DROP_TIPOBARRIOS);
        db.execSQL(Contracts.SQL_CREATE_TIPOBARRIOS);
        DBInsert.insertsTipoBarrios(db);

        //TipoEmpresaTrab
        db.execSQL(Contracts.SQL_DROP_TIPOEMPRESATRAB);
        db.execSQL(Contracts.SQL_CREATE_TIPOEMPRESATRAB);
        DBInsert.insertsTipoEmpresaTrab(db);

        //TipoMotivoViajeFiltro
        db.execSQL(Contracts.SQL_DROP_TIPOMOTIVOVIAJEFILTRO);
        db.execSQL(Contracts.SQL_CREATE_TIPOMOTIVOVIAJEFILTRO);
        DBInsert.insertsTipoMotivoViajeFiltro(db);

        //TipoIslas
        db.execSQL(Contracts.SQL_DROP_TIPOISLAS);
        db.execSQL(Contracts.SQL_CREATE_TIPOISLAS);
        DBInsert.insertsTipoIslas(db);

        //TipoIslasLocalidad
        db.execSQL(Contracts.SQL_DROP_TIPOISLASLOCALIDAD);
        db.execSQL(Contracts.SQL_CREATE_TIPOISLASLOCALIDAD);
        DBInsert.insertsTipoIslasLocalidad(db);

        //TipoGranCanaria
        db.execSQL(Contracts.SQL_DROP_TIPOGRANCANARIA);
        db.execSQL(Contracts.SQL_CREATE_TIPOGRANCANARIA);
        DBInsert.insertsTipoGranCanaria(db);

        //TipoGranCanariaLocalidad
        db.execSQL(Contracts.SQL_DROP_TIPOGRANCANARIALOCALIDAD);
        db.execSQL(Contracts.SQL_CREATE_TIPOGRANCANARIALOCALIDAD);
        DBInsert.insertsTipoGranCanariaLocalidad(db);

        //TipoGranCanariaPlaya
        db.execSQL(Contracts.SQL_DROP_TIPOGRANCANARIAPLAYA);
        db.execSQL(Contracts.SQL_CREATE_TIPOGRANCANARIAPLAYA);
        DBInsert.insertsTipoGranCanariaPlaya(db);

        //TipoAutobus
        db.execSQL(Contracts.SQL_DROP_TIPOAUTOBUS);
        db.execSQL(Contracts.SQL_CREATE_TIPOAUTOBUS);
        DBInsert.insertsTipoAutobus(db);

    }

    public void addColumn (SQLiteDatabase db, String tabla, String columna){
        try {
            db.execSQL("ALTER TABLE " + tabla + " ADD " + columna + " TEXT;");
        } catch (Exception e) {
            String TAG = DBHelper.class.toString();

            Log.e(TAG, "Ya existe el campo "+columna+" que se quiere insertar en "+tabla);
        }
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
