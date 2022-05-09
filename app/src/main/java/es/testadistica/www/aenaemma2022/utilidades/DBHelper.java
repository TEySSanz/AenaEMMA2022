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
        db.execSQL(Contracts.SQL_CREATE_USUARIOS);
        DBInsert.insertsUsuarios(db);

        //Aeropuertos
        db.execSQL(Contracts.SQL_CREATE_AEROPUERTOS);
        DBInsert.insertsAeropuertos(db);

        //Idiomas
        db.execSQL(Contracts.SQL_CREATE_IDIOMAS);
        DBInsert.insertsIdiomas(db);

        //CuePasajeros
        db.execSQL(Contracts.SQL_CREATE_CUEPASAJEROS);

        //CueTrabajadores
        db.execSQL(Contracts.SQL_CREATE_CUETRABAJADORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contracts.SQL_DROP_CUEPASAJEROS);
        onCreate(db);

    }

}
