package es.testadistica.www.aenaemma2022.utilidades;

public class Contracts {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Aena.db";

    //Tabla Usuarios
    public static final String TABLE_USUARIOS = "Usuarios";
    public static final String COLUMN_USUARIOS_IDEN = "iden";
    public static final String COLUMN_USUARIOS_NOMBRE = "nombre";
    public static final String COLUMN_USUARIOS_PASSWORD = "password";
    public static final String COLUMN_USUARIOS_IDAEROPUERTO = "idAeropuerto";
    public static final String SQL_CREATE_USUARIOS = "CREATE TABLE "+ TABLE_USUARIOS + " ("+
            COLUMN_USUARIOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_USUARIOS_NOMBRE + " TEXT, " +
            COLUMN_USUARIOS_PASSWORD + " TEXT, " +
            COLUMN_USUARIOS_IDAEROPUERTO + " INTEGER )";
    public static final String SQL_DROP_USUARIOS = "DROP TABLE IF EXISTS " + TABLE_USUARIOS;

    //Tabla Aeropuertos
    public static final String TABLE_AEROPUERTOS = "Aeropuertos";
    public static final String COLUMN_AEROPUERTOS_IDEN = "iden";
    public static final String COLUMN_AEROPUERTOS_NOMBRE = "nombre";
    public static final String COLUMN_AEROPUERTOS_CLAVE = "clave";
    public static final String SQL_CREATE_AEROPUERTOS = "CREATE TABLE "+ TABLE_AEROPUERTOS + " ("+
            COLUMN_AEROPUERTOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_AEROPUERTOS_NOMBRE + " TEXT, " +
            COLUMN_AEROPUERTOS_CLAVE + " TEXT )";
    public static final String SQL_DROP_AEROPUERTOS = "DROP TABLE IF EXISTS " + TABLE_AEROPUERTOS;

    //Tabla Idiomas
    public static final String TABLE_IDIOMAS = "Idiomas";
    public static final String COLUMN_IDIOMAS_IDEN = "iden";
    public static final String COLUMN_IDIOMAS_IDIOMA = "idioma";
    public static final String COLUMN_IDIOMAS_CLAVE = "clave";
    public static final String SQL_CREATE_IDIOMAS = "CREATE TABLE "+ TABLE_IDIOMAS + " ("+
            COLUMN_IDIOMAS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_IDIOMAS_IDIOMA + " TEXT, " +
            COLUMN_IDIOMAS_CLAVE + " TEXT )";
    public static final String SQL_DROP_IDIOMAS = "DROP TABLE IF EXISTS " + TABLE_IDIOMAS;

    //Tabla CuePasajeros
    public static final String TABLE_CUEPASAJEROS = "CuePasajeros";
    public static final String COLUMN_CUEPASAJEROS_IDEN = "iden";
    public static final String COLUMN_CUEPASAJEROS_IDUSUARIO = "idUsuario";
    public static final String COLUMN_CUEPASAJEROS_ENVIADO = "enviado";
    public static final String COLUMN_CUEPASAJEROS_CLAVE = "clave";
    public static final String COLUMN_CUEPASAJEROS_FECHA = "fecha";
    public static final String COLUMN_CUEPASAJEROS_HORAINICIO = "horaInicio";
    public static final String COLUMN_CUEPASAJEROS_HORAFIN = "horaFin";
    public static final String COLUMN_CUEPASAJEROS_IDAEROPUERTO = "idAeropuerto";
    public static final String COLUMN_CUEPASAJEROS_MODULO = "modulo";
    public static final String COLUMN_CUEPASAJEROS_CDOCIAAR = "cdociaar";
    public static final String COLUMN_CUEPASAJEROS_CDSLAB = "cdslab";
    public static final String COLUMN_CUEPASAJEROS_MOTIVOAVION2 = "motivoavion2";
    public static final String COLUMN_CUEPASAJEROS_PQFUERA = "pqfuera";
    public static final String COLUMN_CUEPASAJEROS_PREFIERE = "prefiere";
    public static final String COLUMN_CUEPASAJEROS_USOAVE = "usoave";
    public static final String COLUMN_CUEPASAJEROS_CDENTREV = "cdentrev";
    public static final String COLUMN_CUEPASAJEROS_FENTREV = "fentrev";
    public static final String COLUMN_CUEPASAJEROS_HENTREV = "hentrev";
    public static final String COLUMN_CUEPASAJEROS_NUMVUECA = "numvueca";
    public static final String COLUMN_CUEPASAJEROS_NUMVUEPA = "numvuepa";
    public static final String COLUMN_CUEPASAJEROS_NENCDOR = "nencdor";
    public static final String COLUMN_CUEPASAJEROS_CDSEXO = "cdsexo";
    public static final String COLUMN_CUEPASAJEROS_IDIOMA = "idioma";
    public static final String COLUMN_CUEPASAJEROS_CDPAISNA = "cdpaisna";
    public static final String COLUMN_CUEPASAJEROS_CDPAISRE = "cdpaisre";
    public static final String COLUMN_CUEPASAJEROS_CDLOCADO = "cdlocado";
    public static final String COLUMN_CUEPASAJEROS_DISTRES = "distres";
    public static final String COLUMN_CUEPASAJEROS_CDCAMBIO = "cdcambio";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOO = "cdiaptoo";
    public static final String COLUMN_CUEPASAJEROS_CIAANTES = "ciaantes";
    public static final String COLUMN_CUEPASAJEROS_CONEXFAC = "conexfac";
    public static final String COLUMN_CUEPASAJEROS_CDSINOPE = "cdsinope";
    public static final String COLUMN_CUEPASAJEROS_CDALOJEN = "cdalojen";
    public static final String COLUMN_CUEPASAJEROS_VIEN_RE = "vien_re";
    public static final String COLUMN_CUEPASAJEROS_CDLOCACO = "cdlocaco";
    public static final String COLUMN_CUEPASAJEROS_DISTRACCE = "distracce";
    public static final String COLUMN_CUEPASAJEROS_CDALOJIN = "cdalojin";
    public static final String COLUMN_CUEPASAJEROS_NMODOS = "nmodos";
    public static final String COLUMN_CUEPASAJEROS_MODO1 = "modo1";
    public static final String COLUMN_CUEPASAJEROS_MODO2 = "modo2";
    public static final String COLUMN_CUEPASAJEROS_ULTIMODO = "ultimodo";
    public static final String COLUMN_CUEPASAJEROS_SITIOPARK = "sitiopark";
    public static final String COLUMN_CUEPASAJEROS_BUSTERMI = "bustermi";
    public static final String COLUMN_CUEPASAJEROS_ACOMPTES = "acomptes";
    public static final String COLUMN_CUEPASAJEROS_HLLEGA = "hllega";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOD = "cdiaptod";
    public static final String COLUMN_CUEPASAJEROS_CDTERM = "cdterm";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOE = "cdiaptoe";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOF = "cdiaptof";
    public static final String COLUMN_CUEPASAJEROS_CDMVIAJE = "cdmviaje";
    public static final String COLUMN_CUEPASAJEROS_CDIDAVUE = "cdidavue";
    public static final String COLUMN_CUEPASAJEROS_TAUS = "taus";
    public static final String COLUMN_CUEPASAJEROS_NPERS = "npers";
    public static final String COLUMN_CUEPASAJEROS_NNIÑOS = "nniños";
    public static final String COLUMN_CUEPASAJEROS_RELACION = "relacion";
    public static final String COLUMN_CUEPASAJEROS_CDTRESER = "cdtreser";
    public static final String COLUMN_CUEPASAJEROS_CDBILLET = "cdbillet";
    public static final String COLUMN_CUEPASAJEROS_NVIAJE = "nviaje";
    public static final String COLUMN_CUEPASAJEROS_VOL12MES = "vol12mes";
    public static final String COLUMN_CUEPASAJEROS_ELECCOVID = "eleccovid";
    public static final String COLUMN_CUEPASAJEROS_P44FACTU = "p44factu";
    public static final String COLUMN_CUEPASAJEROS_BULGRUPO = "bulgrupo";
    public static final String COLUMN_CUEPASAJEROS_NPERBUL = "nperbul";
    public static final String COLUMN_CUEPASAJEROS_DROPOFF = "dropoff";
    public static final String COLUMN_CUEPASAJEROS_CHEKINB = "chekinb";
    public static final String COLUMN_CUEPASAJEROS_CONSUME = "consume";
    public static final String COLUMN_CUEPASAJEROS_GAS_CONS = "gas_cons";
    public static final String COLUMN_CUEPASAJEROS_COMPRART = "comprart";
    public static final String COLUMN_CUEPASAJEROS_GAS_COM = "gas_com";
    public static final String COLUMN_CUEPASAJEROS_PROD1 = "prod1";
    public static final String COLUMN_CUEPASAJEROS_PROD2 = "prod2";
    public static final String COLUMN_CUEPASAJEROS_PROD3 = "prod3";
    public static final String COLUMN_CUEPASAJEROS_PROD4 = "prod4";
    public static final String COLUMN_CUEPASAJEROS_PROD5 = "prod5";
    public static final String COLUMN_CUEPASAJEROS_CDSPROF = "cdsprof";
    public static final String COLUMN_CUEPASAJEROS_ESTUDIOS = "estudios";
    public static final String COLUMN_CUEPASAJEROS_CDEDAD = "cdedad";
    public static final String COLUMN_CUEPASAJEROS_HINI = "hini";
    public static final String COLUMN_CUEPASAJEROS_HFIN = "hfin";
    public static final String COLUMN_CUEPASAJEROS_PUERTA = "puerta";
    public static final String SQL_CREATE_CUEPASAJEROS = "CREATE TABLE "+ TABLE_CUEPASAJEROS + " ("+
            COLUMN_CUEPASAJEROS_IDEN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CUEPASAJEROS_IDUSUARIO + " INTEGER, " +
            COLUMN_CUEPASAJEROS_ENVIADO + " INTEGER, " +
            COLUMN_CUEPASAJEROS_CLAVE + " TEXT, " +
            COLUMN_CUEPASAJEROS_FECHA + " TEXT, " +
            COLUMN_CUEPASAJEROS_HORAINICIO + " TEXT, " +
            COLUMN_CUEPASAJEROS_HORAFIN + " TEXT, " +
            COLUMN_CUEPASAJEROS_IDAEROPUERTO + " INTEGER, " +
            COLUMN_CUEPASAJEROS_ACOMPTES + " INTEGER," +
            COLUMN_CUEPASAJEROS_BULGRUPO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDALOJIN + " TEXT," +
            COLUMN_CUEPASAJEROS_CDBILLET + " TEXT," +
            COLUMN_CUEPASAJEROS_CDCAMBIO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDEDAD + " TEXT," +
            COLUMN_CUEPASAJEROS_CDENTREV + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOD + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOF + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIDAVUE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDLOCACO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDLOCADO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDMVIAJE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDOCIAAR + " TEXT," +
            COLUMN_CUEPASAJEROS_CDPAISNA + " TEXT," +
            COLUMN_CUEPASAJEROS_CDPAISRE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDSEXO + " INTEGER," +
            COLUMN_CUEPASAJEROS_CDSLAB + " TEXT," +
            COLUMN_CUEPASAJEROS_CDSPROF + " TEXT," +
            COLUMN_CUEPASAJEROS_CDTERM + " TEXT," +
            COLUMN_CUEPASAJEROS_CDTRESER + " TEXT," +
            COLUMN_CUEPASAJEROS_CHEKINB + " INTEGER," +
            COLUMN_CUEPASAJEROS_CIAANTES + " TEXT," +
            COLUMN_CUEPASAJEROS_COMPRART + " TEXT," +
            COLUMN_CUEPASAJEROS_CONEXFAC + " TEXT," +
            COLUMN_CUEPASAJEROS_CONSUME + " TEXT," +
            COLUMN_CUEPASAJEROS_ESTUDIOS + " TEXT," +
            COLUMN_CUEPASAJEROS_FENTREV + " INTEGER," +
            COLUMN_CUEPASAJEROS_GAS_COM + " INTEGER," +
            COLUMN_CUEPASAJEROS_GAS_CONS + " INTEGER," +
            COLUMN_CUEPASAJEROS_HENTREV + " TEXT," +
            COLUMN_CUEPASAJEROS_HFIN + " TEXT," +
            COLUMN_CUEPASAJEROS_HINI + " TEXT," +
            COLUMN_CUEPASAJEROS_HLLEGA + " TEXT," +
            COLUMN_CUEPASAJEROS_IDIOMA + " TEXT," +
            COLUMN_CUEPASAJEROS_MODULO + " TEXT," +
            COLUMN_CUEPASAJEROS_MOTIVOAVION2 + " TEXT," +
            COLUMN_CUEPASAJEROS_NENCDOR + " INTEGER," +
            COLUMN_CUEPASAJEROS_NNIÑOS + " INTEGER," +
            COLUMN_CUEPASAJEROS_NPERBUL + " TEXT," +
            COLUMN_CUEPASAJEROS_NPERS + " INTEGER," +
            COLUMN_CUEPASAJEROS_NUMVUECA + " TEXT," +
            COLUMN_CUEPASAJEROS_NUMVUEPA + " TEXT," +
            COLUMN_CUEPASAJEROS_NVIAJE + " TEXT," +
            COLUMN_CUEPASAJEROS_P44FACTU + " TEXT," +
            COLUMN_CUEPASAJEROS_PQFUERA + " TEXT," +
            COLUMN_CUEPASAJEROS_PREFIERE + " TEXT," +
            COLUMN_CUEPASAJEROS_PROD1 + " TEXT," +
            COLUMN_CUEPASAJEROS_PROD2 + " TEXT," +
            COLUMN_CUEPASAJEROS_PROD3 + " TEXT," +
            COLUMN_CUEPASAJEROS_PROD4 + " TEXT," +
            COLUMN_CUEPASAJEROS_PROD5 + " TEXT," +
            COLUMN_CUEPASAJEROS_PUERTA + " TEXT," +
            COLUMN_CUEPASAJEROS_RELACION + " TEXT," +
            COLUMN_CUEPASAJEROS_SITIOPARK + " TEXT," +
            COLUMN_CUEPASAJEROS_TAUS + " INTEGER," +
            COLUMN_CUEPASAJEROS_ULTIMODO + " TEXT," +
            COLUMN_CUEPASAJEROS_USOAVE + " TEXT," +
            COLUMN_CUEPASAJEROS_VIEN_RE + " TEXT," +
            COLUMN_CUEPASAJEROS_VOL12MES + " TEXT," +
            COLUMN_CUEPASAJEROS_DISTRES + " INTEGER," +
            COLUMN_CUEPASAJEROS_CDSINOPE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDALOJEN + " TEXT," +
            COLUMN_CUEPASAJEROS_DISTRACCE + " INTEGER," +
            COLUMN_CUEPASAJEROS_NMODOS + " INTEGER," +
            COLUMN_CUEPASAJEROS_MODO1 + " INTEGER," +
            COLUMN_CUEPASAJEROS_MODO2 + " INTEGER," +
            COLUMN_CUEPASAJEROS_BUSTERMI + " INTEGER," +
            COLUMN_CUEPASAJEROS_DROPOFF + " TEXT," +
            COLUMN_CUEPASAJEROS_ELECCOVID + " TEXT )";
    public static final String SQL_DROP_CUEPASAJEROS = "DROP TABLE IF EXISTS " + TABLE_CUEPASAJEROS;

}
