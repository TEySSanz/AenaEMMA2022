package es.testadistica.www.aenaemma2022.utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Contracts.DATABASE_NAME, null, Contracts.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creación de tablas y vistas
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
        onCreate(db);
    }

}
