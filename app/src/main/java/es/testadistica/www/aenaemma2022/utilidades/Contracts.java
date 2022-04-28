package es.testadistica.www.aenaemma2022.utilidades;

public class Contracts {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Aena.db";

    //Tabla Usuarios
    public static final String TABLE_USUARIOS = "Usuarios";
    public static final String COLUMN_USUARIOS_IDEN = "iden";
    public static final String COLUMN_USUARIOS_NOMBRE = "nombre";
    public static final String COLUMN_USUARIOS_PASSWORD = "password";
    public static final String SQL_CREATE_USUARIOS = "CREATE TABLE "+ TABLE_USUARIOS + " ("+
            COLUMN_USUARIOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_USUARIOS_NOMBRE + " TEXT, "+
            COLUMN_USUARIOS_PASSWORD + " TEXT )";
    public static final String SQL_DROP_USUARIOS = "DROP TABLE IF EXISTS " + TABLE_USUARIOS;
}
