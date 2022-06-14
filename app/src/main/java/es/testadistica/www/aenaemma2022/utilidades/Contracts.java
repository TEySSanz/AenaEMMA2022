package es.testadistica.www.aenaemma2022.utilidades;

public class Contracts {

    public static final int DATABASE_VERSION = 6;
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
    public static final String COLUMN_AEROPUERTOS_MODELO = "modelo";
    public static final String SQL_CREATE_AEROPUERTOS = "CREATE TABLE "+ TABLE_AEROPUERTOS + " ("+
            COLUMN_AEROPUERTOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_AEROPUERTOS_NOMBRE + " TEXT, " +
            COLUMN_AEROPUERTOS_CLAVE + " TEXT, " +
            COLUMN_AEROPUERTOS_MODELO + " INTEGER )";
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

    //Tabla TipoAeropuertos
    public static final String TABLE_TIPOAEROPUERTOS = "TipoAeropuertos";
    public static final String COLUMN_TIPOAEROPUERTOS_IDEN = "iden";
    public static final String COLUMN_TIPOAEROPUERTOS_CODIGO = "codigo";
    public static final String COLUMN_TIPOAEROPUERTOS_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOAEROPUERTOS_DESCRIPCIONPRINCIPAL = "descripcionPrincipal";
    public static final String COLUMN_TIPOAEROPUERTOS_CODPAIS = "codPais";
    public static final String COLUMN_TIPOAEROPUERTOS_PAIS = "Pais";
    public static final String COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL = "MADprincipal";
    public static final String COLUMN_TIPOAEROPUERTOS_MADOLEADA = "MADoleada";
    public static final String COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL = "BCNprincipal";
    public static final String COLUMN_TIPOAEROPUERTOS_BCNOLEADA = "BCNoleada";
    public static final String SQL_CREATE_TIPOAEROPUERTOS = "CREATE TABLE "+ TABLE_TIPOAEROPUERTOS + " ("+
            COLUMN_TIPOAEROPUERTOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOAEROPUERTOS_CODIGO + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_DESCRIPCIONPRINCIPAL + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_CODPAIS + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_PAIS + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_MADOLEADA + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " TEXT, " +
            COLUMN_TIPOAEROPUERTOS_BCNOLEADA + " TEXT )";
    public static final String SQL_DROP_TIPOAEROPUERTOS = "DROP TABLE IF EXISTS " + TABLE_TIPOAEROPUERTOS;

    //Tabla TipoCompanias
    public static final String TABLE_TIPOCOMPANIAS = "TipoCompanias";
    public static final String COLUMN_TIPOCOMPANIAS_IDEN = "iden";
    public static final String COLUMN_TIPOCOMPANIAS_CODIGO = "codigo";
    public static final String COLUMN_TIPOCOMPANIAS_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOCOMPANIAS_MADAEREA = "MADaerea";
    public static final String COLUMN_TIPOCOMPANIAS_MADOLEADA = "MADoleada";
    public static final String COLUMN_TIPOCOMPANIAS_BCNAEREA = "BCNaerea";
    public static final String COLUMN_TIPOCOMPANIAS_BCNOLEADA = "BCNoleada";
    public static final String SQL_CREATE_TIPOCOMPANIAS = "CREATE TABLE "+ TABLE_TIPOCOMPANIAS + " ("+
            COLUMN_TIPOCOMPANIAS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOCOMPANIAS_CODIGO + " TEXT, " +
            COLUMN_TIPOCOMPANIAS_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOCOMPANIAS_MADAEREA + " TEXT, " +
            COLUMN_TIPOCOMPANIAS_MADOLEADA + " TEXT, " +
            COLUMN_TIPOCOMPANIAS_BCNAEREA + " TEXT, " +
            COLUMN_TIPOCOMPANIAS_BCNOLEADA + " TEXT )";
    public static final String SQL_DROP_TIPOCOMPANIAS = "DROP TABLE IF EXISTS " + TABLE_TIPOCOMPANIAS;

    //Tabla Distritos
    public static final String TABLE_TIPODISTRITOS = "TipoDistritos";
    public static final String COLUMN_TIPODISTRITOS_IDEN = "iden";
    public static final String COLUMN_TIPODISTRITOS_CODIGO = "codigo";
    public static final String COLUMN_TIPODISTRITOS_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPODISTRITOS_CIUDAD = "ciudad";
    public static final String SQL_CREATE_TIPODISTRITOS = "CREATE TABLE "+ TABLE_TIPODISTRITOS + " ("+
            COLUMN_TIPODISTRITOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPODISTRITOS_CODIGO + " TEXT, " +
            COLUMN_TIPODISTRITOS_DESCRIPCION + " TEXT, " +
            COLUMN_TIPODISTRITOS_CIUDAD + " TEXT )";
    public static final String SQL_DROP_TIPODISTRITOS = "DROP TABLE IF EXISTS " + TABLE_TIPODISTRITOS;

    //Tabla MotivoViaje
    public static final String TABLE_TIPOMOTIVOVIAJE = "TipoMotivoViaje";
    public static final String COLUMN_TIPOMOTIVOVIAJE_IDEN = "iden";
    public static final String COLUMN_TIPOMOTIVOVIAJE_CODIGO = "codigo";
    public static final String COLUMN_TIPOMOTIVOVIAJE_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOMOTIVOVIAJE_CODGRUPO = "codGrupo";
    public static final String SQL_CREATE_TIPOMOTIVOVIAJE = "CREATE TABLE "+ TABLE_TIPOMOTIVOVIAJE + " ("+
            COLUMN_TIPOMOTIVOVIAJE_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOMOTIVOVIAJE_CODIGO + " TEXT, " +
            COLUMN_TIPOMOTIVOVIAJE_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOMOTIVOVIAJE_CODGRUPO + " TEXT )";
    public static final String SQL_DROP_TIPOMOTIVOVIAJE = "DROP TABLE IF EXISTS " + TABLE_TIPOMOTIVOVIAJE;

    //Tabla TipoMunicipios
    public static final String TABLE_TIPOMUNICIPIOS = "TipoMunicipios";
    public static final String COLUMN_TIPOMUNICIPIOS_IDEN = "iden";
    public static final String COLUMN_TIPOMUNICIPIOS_CODIGO = "codigo";
    public static final String COLUMN_TIPOMUNICIPIOS_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOMUNICIPIOS_PROVINCIA = "provincia";
    public static final String SQL_CREATE_TIPOMUNICIPIOS = "CREATE TABLE "+ TABLE_TIPOMUNICIPIOS + " ("+
            COLUMN_TIPOMUNICIPIOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOMUNICIPIOS_CODIGO + " TEXT, " +
            COLUMN_TIPOMUNICIPIOS_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOMUNICIPIOS_PROVINCIA + " TEXT )";
    public static final String SQL_DROP_TIPOMUNICIPIOS = "DROP TABLE IF EXISTS " + TABLE_TIPOMUNICIPIOS;

    //Tabla TipoPaises
    public static final String TABLE_TIPOPAISES = "TipoPaises";
    public static final String COLUMN_TIPOPAISES_IDEN = "iden";
    public static final String COLUMN_TIPOPAISES_CODIGO = "codigo";
    public static final String COLUMN_TIPOPAISES_DESCRIPCION= "descripcion";
    public static final String COLUMN_TIPOPAISES_CODGRUPOPAIS= "codGrupoPais";
    public static final String COLUMN_TIPOPAISES_CODZONAPAIS1= "codZonaPais1";
    public static final String COLUMN_TIPOPAISES_CODZONAPAIS2= "codZonaPais2";
    public static final String COLUMN_TIPOPAISES_CODZONAPAIS3= "codZonaPais3";
    public static final String SQL_CREATE_TIPOPAISES = "CREATE TABLE "+ TABLE_TIPOPAISES + " ("+
            COLUMN_TIPOPAISES_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOPAISES_CODIGO + " TEXT, " +
            COLUMN_TIPOPAISES_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOPAISES_CODGRUPOPAIS + " TEXT, " +
            COLUMN_TIPOPAISES_CODZONAPAIS1 + " TEXT, " +
            COLUMN_TIPOPAISES_CODZONAPAIS2 + " TEXT, " +
            COLUMN_TIPOPAISES_CODZONAPAIS3 + " TEXT )";
    public static final String SQL_DROP_TIPOPAISES = "DROP TABLE IF EXISTS " + TABLE_TIPOPAISES;

    //Tabla TipoPaises1y2
    public static final String TABLE_TIPOPAISES1Y2 = "TipoPaises1y2";
    public static final String COLUMN_TIPOPAISES1Y2_IDEN = "iden";
    public static final String COLUMN_TIPOPAISES1Y2_CODIGO = "codigo";
    public static final String COLUMN_TIPOPAISES1Y2_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOPAISES1Y2_CODIGOPAIS = "codigoPais";
    public static final String COLUMN_TIPOPAISES1Y2_ZONAS = "zonas";
    public static final String SQL_CREATE_TIPOPAISES1Y2 = "CREATE TABLE "+ TABLE_TIPOPAISES1Y2 + " ("+
            COLUMN_TIPOPAISES1Y2_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOPAISES1Y2_CODIGO + " TEXT, " +
            COLUMN_TIPOPAISES1Y2_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOPAISES1Y2_CODIGOPAIS + " TEXT, " +
            COLUMN_TIPOPAISES1Y2_ZONAS + " TEXT )";
    public static final String SQL_DROP_TIPOPAISES1Y2 = "DROP TABLE IF EXISTS " + TABLE_TIPOPAISES1Y2;

    //Tabla TipoProductos
    public static final String TABLE_TIPOPRODUCTOS = "TipoProductos";
    public static final String COLUMN_TIPOPRODUCTOS_IDEN = "iden";
    public static final String COLUMN_TIPOPRODUCTOS_CODIGO = "codigo";
    public static final String COLUMN_TIPOPRODUCTOS_DESCRIPCION = "descripcion";
    public static final String SQL_CREATE_TIPOPRODUCTOS = "CREATE TABLE "+ TABLE_TIPOPRODUCTOS + " ("+
            COLUMN_TIPOPRODUCTOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOPRODUCTOS_CODIGO + " TEXT, " +
            COLUMN_TIPOPRODUCTOS_DESCRIPCION + " TEXT )";
    public static final String SQL_DROP_TIPOPRODUCTOS = "DROP TABLE IF EXISTS " + TABLE_TIPOPRODUCTOS;

    //Tabla TipoProvincias
    public static final String TABLE_TIPOPROVINCIAS = "TipoProvincias";
    public static final String COLUMN_TIPOPROVINCIAS_IDEN = "iden";
    public static final String COLUMN_TIPOPROVINCIAS_CODIGO = "codigo";
    public static final String COLUMN_TIPOPROVINCIAS_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOPROVINCIAS_idCA = "idCA";
    public static final String COLUMN_TIPOPROVINCIAS_DESCRIPCIONCA = "descripcionCA";
    public static final String SQL_CREATE_TIPOPROVINCIAS = "CREATE TABLE "+ TABLE_TIPOPROVINCIAS + " ("+
            COLUMN_TIPOPROVINCIAS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOPROVINCIAS_CODIGO + " TEXT, " +
            COLUMN_TIPOPROVINCIAS_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOPROVINCIAS_idCA + " TEXT, " +
            COLUMN_TIPOPROVINCIAS_DESCRIPCIONCA + " TEXT )";
    public static final String SQL_DROP_TIPOPROVINCIAS = "DROP TABLE IF EXISTS " + TABLE_TIPOPROVINCIAS;

    //Tabla TipoActEmpTrab
    public static final String TABLE_TIPOACTEMPTRAB = "TipoActEmpTrab";
    public static final String COLUMN_TIPOACTEMPTRAB_IDEN = "iden";
    public static final String COLUMN_TIPOACTEMPTRAB_CODIGO = "codigo";
    public static final String COLUMN_TIPOACTEMPTRAB_DESCRIPCION = "descripcion";
    public static final String SQL_CREATE_TIPOACTEMPTRAB = "CREATE TABLE "+ TABLE_TIPOACTEMPTRAB + " ("+
            COLUMN_TIPOACTEMPTRAB_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOACTEMPTRAB_CODIGO + " TEXT, " +
            COLUMN_TIPOACTEMPTRAB_DESCRIPCION + " TEXT )";
    public static final String SQL_DROP_TIPOACTEMPTRAB = "DROP TABLE IF EXISTS " + TABLE_TIPOACTEMPTRAB;

    //Tabla TipoBarrios
    public static final String TABLE_TIPOBARRIOS = "TipoBarrios";
    public static final String COLUMN_TIPOBARRIOS_IDEN = "iden";
    public static final String COLUMN_TIPOBARRIOS_CODIGO = "codigo";
    public static final String COLUMN_TIPOBARRIOS_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOBARRIOS_DISTRITO = "distrito";
    public static final String COLUMN_TIPOBARRIOS_IDAEROPUERTO = "idAeropuerto";
    public static final String SQL_CREATE_TIPOBARRIOS = "CREATE TABLE "+ TABLE_TIPOBARRIOS + " ("+
            COLUMN_TIPOBARRIOS_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOBARRIOS_CODIGO + " TEXT, " +
            COLUMN_TIPOBARRIOS_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOBARRIOS_DISTRITO + " TEXT, " +
            COLUMN_TIPOBARRIOS_IDAEROPUERTO + " TEXT )";
    public static final String SQL_DROP_TIPOBARRIOS = "DROP TABLE IF EXISTS " + TABLE_TIPOBARRIOS;

    //Tabla TipoEmpresaTrab
    public static final String TABLE_TIPOEMPRESATRAB = "TipoEmpresaTrab";
    public static final String COLUMN_TIPOEMPRESATRAB_IDEN = "iden";
    public static final String COLUMN_TIPOEMPRESATRAB_CODIGO = "codigo";
    public static final String COLUMN_TIPOEMPRESATRAB_DESCRIPCION = "descripcion";
    public static final String COLUMN_TIPOEMPRESATRAB_IDAEROPUERTO = "idAeropuerto";
    public static final String SQL_CREATE_TIPOEMPRESATRAB = "CREATE TABLE "+ TABLE_TIPOEMPRESATRAB + " ("+
            COLUMN_TIPOEMPRESATRAB_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOEMPRESATRAB_CODIGO + " TEXT, " +
            COLUMN_TIPOEMPRESATRAB_DESCRIPCION + " TEXT, " +
            COLUMN_TIPOEMPRESATRAB_IDAEROPUERTO + " TEXT )";
    public static final String SQL_DROP_TIPOEMPRESATRAB = "DROP TABLE IF EXISTS " + TABLE_TIPOEMPRESATRAB;

    //Tabla TipoMotivoViajeFiltro
    public static final String TABLE_TIPOMOTIVOVIAJEFILTRO = "TipoMotivoViajeFiltro";
    public static final String COLUMN_TIPOMOTIVOVIAJEFILTRO_IDEN = "iden";
    public static final String COLUMN_TIPOMOTIVOVIAJEFILTRO_MOTIVO = "motivo";
    public static final String COLUMN_TIPOMOTIVOVIAJEFILTRO_CODIGO = "codigo";
    public static final String SQL_CREATE_TIPOMOTIVOVIAJEFILTRO = "CREATE TABLE "+ TABLE_TIPOMOTIVOVIAJEFILTRO + " ("+
            COLUMN_TIPOMOTIVOVIAJEFILTRO_IDEN + " INTEGER PRIMARY KEY, " +
            COLUMN_TIPOMOTIVOVIAJEFILTRO_MOTIVO + " TEXT, " +
            COLUMN_TIPOMOTIVOVIAJEFILTRO_CODIGO + " TEXT )";
    public static final String SQL_DROP_TIPOMOTIVOVIAJEFILTRO = "DROP TABLE IF EXISTS " + TABLE_TIPOMOTIVOVIAJEFILTRO;

    //Tabla CuePasajeros
    public static final String TABLE_CUEPASAJEROS = "CuePasajeros";
    public static final String COLUMN_CUEPASAJEROS_IDEN = "iden";
    public static final String COLUMN_CUEPASAJEROS_IDUSUARIO = "idUsuario";
    public static final String COLUMN_CUEPASAJEROS_ENVIADO = "enviado";
    public static final String COLUMN_CUEPASAJEROS_PREGUNTA = "pregunta";
    public static final String COLUMN_CUEPASAJEROS_CLAVE = "clave";
    public static final String COLUMN_CUEPASAJEROS_FECHA = "fecha";
    public static final String COLUMN_CUEPASAJEROS_HORAINICIO = "horaInicio";
    public static final String COLUMN_CUEPASAJEROS_HORAFIN = "horaFin";
    public static final String COLUMN_CUEPASAJEROS_IDAEROPUERTO = "idAeropuerto";
    public static final String COLUMN_CUEPASAJEROS_IDIDIOMA = "idIdioma";
    public static final String COLUMN_CUEPASAJEROS_MODULO = "modulo";
    public static final String COLUMN_CUEPASAJEROS_CDOCIAAR = "cdociaar";
    public static final String COLUMN_CUEPASAJEROS_CDOCIAAROTRO = "cdociaarotro";
    public static final String COLUMN_CUEPASAJEROS_CDSLAB = "cdslab";
    public static final String COLUMN_CUEPASAJEROS_MOTIVOAVION2 = "motivoavion2";
    public static final String COLUMN_CUEPASAJEROS_MOTIVOAVION2OTRO = "motivoavion2otro";
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
    public static final String COLUMN_CUEPASAJEROS_DISTRESOTRO = "distresotro";
    public static final String COLUMN_CUEPASAJEROS_CDCAMBIO = "cdcambio";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOO = "cdiaptoo";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOOOTRO = "cdiaptoootro";
    public static final String COLUMN_CUEPASAJEROS_CIAANTES = "ciaantes";
    public static final String COLUMN_CUEPASAJEROS_CIAANTESOTRO = "ciaantesotro";
    public static final String COLUMN_CUEPASAJEROS_CONEXFAC = "conexfac";
    public static final String COLUMN_CUEPASAJEROS_CDSINOPE = "cdsinope";
    public static final String COLUMN_CUEPASAJEROS_CDALOJEN = "cdalojen";
    public static final String COLUMN_CUEPASAJEROS_VIEN_RE = "vien_re";
    public static final String COLUMN_CUEPASAJEROS_CDLOCACO = "cdlocaco";
    public static final String COLUMN_CUEPASAJEROS_DISTRACCE = "distracce";
    public static final String COLUMN_CUEPASAJEROS_DISTRACCEOTRO = "distracceotro";
    public static final String COLUMN_CUEPASAJEROS_CDALOJIN = "cdalojin";
    public static final String COLUMN_CUEPASAJEROS_CDALOJIN_OTROS = "cdalojin_otros";
    public static final String COLUMN_CUEPASAJEROS_NMODOS = "nmodos";
    public static final String COLUMN_CUEPASAJEROS_MODO1 = "modo1";
    public static final String COLUMN_CUEPASAJEROS_MODO2 = "modo2";
    public static final String COLUMN_CUEPASAJEROS_ULTIMODO = "ultimodo";
    public static final String COLUMN_CUEPASAJEROS_ULTIMODOOTRO = "ultimodootro";
    public static final String COLUMN_CUEPASAJEROS_SITIOPARK = "sitiopark";
    public static final String COLUMN_CUEPASAJEROS_BUSTERMI = "bustermi";
    public static final String COLUMN_CUEPASAJEROS_ACOMPTES = "acomptes";
    public static final String COLUMN_CUEPASAJEROS_HLLEGA = "hllega";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOD = "cdiaptod";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTODOTRO = "cdiaptodotro";
    public static final String COLUMN_CUEPASAJEROS_CDTERM = "cdterm";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOE = "cdiaptoe";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOF = "cdiaptof";
    public static final String COLUMN_CUEPASAJEROS_CDIAPTOFOTRO = "cdiaptofotro";
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
    public static final String COLUMN_CUEPASAJEROS_VALOREXP = "valorexp";
    public static final String SQL_CREATE_CUEPASAJEROS = "CREATE TABLE "+ TABLE_CUEPASAJEROS + " ("+
            COLUMN_CUEPASAJEROS_IDEN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CUEPASAJEROS_IDUSUARIO + " INTEGER, " +
            COLUMN_CUEPASAJEROS_ENVIADO + " INTEGER, " +
            COLUMN_CUEPASAJEROS_PREGUNTA + " INTEGER, " +
            COLUMN_CUEPASAJEROS_CLAVE + " TEXT, " +
            COLUMN_CUEPASAJEROS_FECHA + " TEXT, " +
            COLUMN_CUEPASAJEROS_HORAINICIO + " TEXT, " +
            COLUMN_CUEPASAJEROS_HORAFIN + " TEXT, " +
            COLUMN_CUEPASAJEROS_IDAEROPUERTO + " INTEGER, " +
            COLUMN_CUEPASAJEROS_IDIDIOMA + " INTEGER, " +
            COLUMN_CUEPASAJEROS_ACOMPTES + " INTEGER," +
            COLUMN_CUEPASAJEROS_BULGRUPO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDALOJIN + " TEXT," +
            COLUMN_CUEPASAJEROS_CDALOJIN_OTROS + " TEXT," +
            COLUMN_CUEPASAJEROS_CDBILLET + " TEXT," +
            COLUMN_CUEPASAJEROS_CDCAMBIO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDEDAD + " TEXT," +
            COLUMN_CUEPASAJEROS_CDENTREV + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOD + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTODOTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOF + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOFOTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIAPTOOOTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDIDAVUE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDLOCACO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDLOCADO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDMVIAJE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDOCIAAR + " TEXT," +
            COLUMN_CUEPASAJEROS_CDOCIAAROTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDPAISNA + " TEXT," +
            COLUMN_CUEPASAJEROS_CDPAISRE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDSEXO + " INTEGER," +
            COLUMN_CUEPASAJEROS_CDSLAB + " TEXT," +
            COLUMN_CUEPASAJEROS_CDSPROF + " TEXT," +
            COLUMN_CUEPASAJEROS_CDTERM + " TEXT," +
            COLUMN_CUEPASAJEROS_CDTRESER + " TEXT," +
            COLUMN_CUEPASAJEROS_CHEKINB + " INTEGER," +
            COLUMN_CUEPASAJEROS_CIAANTES + " TEXT," +
            COLUMN_CUEPASAJEROS_CIAANTESOTRO + " TEXT," +
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
            COLUMN_CUEPASAJEROS_MOTIVOAVION2OTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_NENCDOR + " TEXT," +
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
            COLUMN_CUEPASAJEROS_ULTIMODOOTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_USOAVE + " TEXT," +
            COLUMN_CUEPASAJEROS_VIEN_RE + " TEXT," +
            COLUMN_CUEPASAJEROS_VOL12MES + " TEXT," +
            COLUMN_CUEPASAJEROS_DISTRES + " TEXT," +
            COLUMN_CUEPASAJEROS_DISTRESOTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_CDSINOPE + " TEXT," +
            COLUMN_CUEPASAJEROS_CDALOJEN + " TEXT," +
            COLUMN_CUEPASAJEROS_DISTRACCE + " TEXT," +
            COLUMN_CUEPASAJEROS_DISTRACCEOTRO + " TEXT," +
            COLUMN_CUEPASAJEROS_NMODOS + " TEXT," +
            COLUMN_CUEPASAJEROS_MODO1 + " TEXT," +
            COLUMN_CUEPASAJEROS_MODO2 + " TEXT," +
            COLUMN_CUEPASAJEROS_BUSTERMI + " INTEGER," +
            COLUMN_CUEPASAJEROS_DROPOFF + " TEXT," +
            COLUMN_CUEPASAJEROS_ELECCOVID + " TEXT, " +
            COLUMN_CUEPASAJEROS_VALOREXP + " INTEGER )";
    public static final String SQL_DROP_CUEPASAJEROS = "DROP TABLE IF EXISTS " + TABLE_CUEPASAJEROS;

    //Tabla CueTrabajadores
    public static final String TABLE_CUETRABAJADORES = "CueTrabajadores";
    public static final String COLUMN_CUETRABAJADORES_IDEN = "iden";
    public static final String COLUMN_CUETRABAJADORES_IDUSUARIO = "idUsuario";
    public static final String COLUMN_CUETRABAJADORES_ENVIADO = "enviado";
    public static final String COLUMN_CUETRABAJADORES_PREGUNTA = "pregunta";
    public static final String COLUMN_CUETRABAJADORES_CLAVE = "clave";
    public static final String COLUMN_CUETRABAJADORES_FECHA = "fecha";
    public static final String COLUMN_CUETRABAJADORES_HORAINICIO = "horaInicio";
    public static final String COLUMN_CUETRABAJADORES_HORAFIN = "horaFin";
    public static final String COLUMN_CUETRABAJADORES_IDAEROPUERTO = "idAeropuerto";
    public static final String COLUMN_CUETRABAJADORES_IDIDIOMA = "idIdioma";
    public static final String COLUMN_CUETRABAJADORES_NENCDOR = "nencdor";
    public static final String COLUMN_CUETRABAJADORES_CDSEXO = "cdsexo";
    public static final String COLUMN_CUETRABAJADORES_IDIOMA = "idioma";
    public static final String COLUMN_CUETRABAJADORES_EMPRESA = "empresa";
    public static final String COLUMN_CUETRABAJADORES_ACTEMPRE = "actempre";
    public static final String COLUMN_CUETRABAJADORES_ACTEMPREOTRO = "actempreotro";
    public static final String COLUMN_CUETRABAJADORES_CDLOCADO = "cdlocado";
    public static final String COLUMN_CUETRABAJADORES_DISTRES = "distres";
    public static final String COLUMN_CUETRABAJADORES_DISTRESOTRO = "distresotro";
    public static final String COLUMN_CUETRABAJADORES_JORNADA = "jornada";
    public static final String COLUMN_CUETRABAJADORES_JORNADAOTRO = "jornadaotro";
    public static final String COLUMN_CUETRABAJADORES_NDIASTRAB = "ndiastrab";
    public static final String COLUMN_CUETRABAJADORES_ZONATRAB1 = "zonatrab1";
    public static final String COLUMN_CUETRABAJADORES_ZONATRAB2 = "zonatrab2";
    public static final String COLUMN_CUETRABAJADORES_ZONATRAB3 = "zonatrab3";
    public static final String COLUMN_CUETRABAJADORES_ZONATRAB4 = "zonatrab4";
    public static final String COLUMN_CUETRABAJADORES_ZONATRAB5 = "zonatrab5";
    public static final String COLUMN_CUETRABAJADORES_ZONATRAB6 = "zonatrab6";
    public static final String COLUMN_CUETRABAJADORES_HORAENT1 = "horaent1";
    public static final String COLUMN_CUETRABAJADORES_HORASAL1 = "horasal1";
    public static final String COLUMN_CUETRABAJADORES_HORAENT2 = "horaent2";
    public static final String COLUMN_CUETRABAJADORES_HORASAL2 = "horasal2";
    public static final String COLUMN_CUETRABAJADORES_HORAENT3 = "horaent3";
    public static final String COLUMN_CUETRABAJADORES_HORASAL3 = "horasal3";
    public static final String COLUMN_CUETRABAJADORES_NMODOS = "nmodos";
    public static final String COLUMN_CUETRABAJADORES_MODO1 = "modo1";
    public static final String COLUMN_CUETRABAJADORES_MODO2 = "modo2";
    public static final String COLUMN_CUETRABAJADORES_ULTIMODO = "ultimodo";
    public static final String COLUMN_CUETRABAJADORES_NOCUCOCHE = "nocucoche";
    public static final String COLUMN_CUETRABAJADORES_SATISTRANSPUBLI = "satistranspubli";
    public static final String COLUMN_CUETRABAJADORES_VALTRANSPUBLI1 = "valtranspubli1";
    public static final String COLUMN_CUETRABAJADORES_VALTRANSPUBLI2 = "valtranspubli2";
    public static final String COLUMN_CUETRABAJADORES_VALTRANSPUBLI3 = "valtranspubli3";
    public static final String COLUMN_CUETRABAJADORES_VALTRANSPUBLIOTRO = "valtranspubliotro";
    public static final String COLUMN_CUETRABAJADORES_MEJTRANSPUBLI1 = "mejtranspubli1";
    public static final String COLUMN_CUETRABAJADORES_MEJTRANSPUBLI2 = "mejtranspubli2";
    public static final String COLUMN_CUETRABAJADORES_MEJTRANSPUBLI3 = "mejtranspubli3";
    public static final String COLUMN_CUETRABAJADORES_MEJTRANSPUBLIOTRO = "mejtranspubliotro";
    public static final String COLUMN_CUETRABAJADORES_DESPLAZATRAB = "desplazatrab";
    public static final String COLUMN_CUETRABAJADORES_NOTRANSPUBLI1 = "notranspubli1";
    public static final String COLUMN_CUETRABAJADORES_NOTRANSPUBLI2 = "notranspubli2";
    public static final String COLUMN_CUETRABAJADORES_NOTRANSPUBLI3 = "notranspubli3";
    public static final String COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO = "notranspubliotro";
    public static final String COLUMN_CUETRABAJADORES_DISPTRANSPUBLI = "disptranspubli";
    public static final String COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO = "disptranspubliotro";
    public static final String COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI = "importranspubli";
    public static final String COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1 = "medtranspubli1";
    public static final String COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2 = "medtranspubli2";
    public static final String COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3 = "medtranspubli3";
    public static final String COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO = "medtranspubliotro";
    public static final String COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI = "tiempotranspubli";
    public static final String COLUMN_CUETRABAJADORES_APARCTRAB = "aparctrab";
    public static final String COLUMN_CUETRABAJADORES_COMPARTCOCHE1 = "compartcoche1";
    public static final String COLUMN_CUETRABAJADORES_COMPARTCOCHE2 = "compartcoche2";
    public static final String COLUMN_CUETRABAJADORES_COMPARTCOCHE3 = "compartcoche3";
    public static final String COLUMN_CUETRABAJADORES_COMPARTCOCHEOTRO = "compartcocheotro";
    public static final String COLUMN_CUETRABAJADORES_DISPBICI1 = "dispbici1";
    public static final String COLUMN_CUETRABAJADORES_DISPBICI2 = "dispbici2";
    public static final String COLUMN_CUETRABAJADORES_DISPBICI3 = "dispbici3";
    public static final String COLUMN_CUETRABAJADORES_DISPBICIOTRO = "dispbiciotro";
    public static final String COLUMN_CUETRABAJADORES_MODOSALIDA = "modosalida";
    public static final String COLUMN_CUETRABAJADORES_MODOSALIDAOTRO = "modosalidaotro";
    public static final String COLUMN_CUETRABAJADORES_CDEDADTRAB = "cdedadtrab";
    public static final String COLUMN_CUETRABAJADORES_CDSLAB = "cdslab";
    public static final String COLUMN_CUETRABAJADORES_PUESTO = "puesto";
    public static final String COLUMN_CUETRABAJADORES_SUGERENCIAS = "sugerencias";
    public static final String SQL_CREATE_CUETRABAJADORES = "CREATE TABLE "+ TABLE_CUETRABAJADORES + " ("+
            COLUMN_CUETRABAJADORES_IDEN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CUETRABAJADORES_IDUSUARIO + " INTEGER, " +
            COLUMN_CUETRABAJADORES_ENVIADO + " INTEGER, " +
            COLUMN_CUETRABAJADORES_PREGUNTA + " INTEGER, " +
            COLUMN_CUETRABAJADORES_CLAVE + " TEXT, " +
            COLUMN_CUETRABAJADORES_FECHA + " TEXT, " +
            COLUMN_CUETRABAJADORES_HORAINICIO + " TEXT, " +
            COLUMN_CUETRABAJADORES_HORAFIN + " TEXT, " +
            COLUMN_CUETRABAJADORES_IDAEROPUERTO + " INTEGER, " +
            COLUMN_CUETRABAJADORES_IDIDIOMA + " INTEGER, " +
            COLUMN_CUETRABAJADORES_NENCDOR + " TEXT," +
            COLUMN_CUETRABAJADORES_CDSEXO + " INTEGER," +
            COLUMN_CUETRABAJADORES_IDIOMA + " TEXT," +
            COLUMN_CUETRABAJADORES_EMPRESA + " TEXT," +
            COLUMN_CUETRABAJADORES_ACTEMPRE + " TEXT," +
            COLUMN_CUETRABAJADORES_ACTEMPREOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_CDLOCADO + " TEXT," +
            COLUMN_CUETRABAJADORES_DISTRES + " TEXT," +
            COLUMN_CUETRABAJADORES_DISTRESOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_JORNADA + " TEXT," +
            COLUMN_CUETRABAJADORES_JORNADAOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_NDIASTRAB + " TEXT," +
            COLUMN_CUETRABAJADORES_ZONATRAB1 + " INTEGER," +
            COLUMN_CUETRABAJADORES_ZONATRAB2 + " INTEGER," +
            COLUMN_CUETRABAJADORES_ZONATRAB3 + " INTEGER," +
            COLUMN_CUETRABAJADORES_ZONATRAB4 + " INTEGER," +
            COLUMN_CUETRABAJADORES_ZONATRAB5 + " INTEGER," +
            COLUMN_CUETRABAJADORES_ZONATRAB6 + " INTEGER," +
            COLUMN_CUETRABAJADORES_HORAENT1 + " TEXT," +
            COLUMN_CUETRABAJADORES_HORASAL1 + " TEXT," +
            COLUMN_CUETRABAJADORES_HORAENT2 + " TEXT," +
            COLUMN_CUETRABAJADORES_HORASAL2 + " TEXT," +
            COLUMN_CUETRABAJADORES_HORAENT3 + " TEXT," +
            COLUMN_CUETRABAJADORES_HORASAL3 + " TEXT," +
            COLUMN_CUETRABAJADORES_NMODOS + " TEXT," +
            COLUMN_CUETRABAJADORES_MODO1 + " TEXT," +
            COLUMN_CUETRABAJADORES_MODO2 + " TEXT," +
            COLUMN_CUETRABAJADORES_ULTIMODO + " TEXT," +
            COLUMN_CUETRABAJADORES_NOCUCOCHE + " TEXT," +
            COLUMN_CUETRABAJADORES_SATISTRANSPUBLI + " TEXT," +
            COLUMN_CUETRABAJADORES_VALTRANSPUBLI1 + " TEXT," +
            COLUMN_CUETRABAJADORES_VALTRANSPUBLI2 + " TEXT," +
            COLUMN_CUETRABAJADORES_VALTRANSPUBLI3 + " TEXT," +
            COLUMN_CUETRABAJADORES_VALTRANSPUBLIOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_MEJTRANSPUBLI1 + " TEXT," +
            COLUMN_CUETRABAJADORES_MEJTRANSPUBLI2 + " TEXT," +
            COLUMN_CUETRABAJADORES_MEJTRANSPUBLI3 + " TEXT," +
            COLUMN_CUETRABAJADORES_MEJTRANSPUBLIOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_DESPLAZATRAB + " TEXT," +
            COLUMN_CUETRABAJADORES_NOTRANSPUBLI1 + " TEXT," +
            COLUMN_CUETRABAJADORES_NOTRANSPUBLI2 + " TEXT," +
            COLUMN_CUETRABAJADORES_NOTRANSPUBLI3 + " TEXT," +
            COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_DISPTRANSPUBLI + " TEXT," +
            COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI + " TEXT," +
            COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1 + " TEXT," +
            COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2 + " TEXT," +
            COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3 + " TEXT," +
            COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI + " TEXT," +
            COLUMN_CUETRABAJADORES_APARCTRAB + " TEXT," +
            COLUMN_CUETRABAJADORES_COMPARTCOCHE1 + " TEXT," +
            COLUMN_CUETRABAJADORES_COMPARTCOCHE2 + " TEXT," +
            COLUMN_CUETRABAJADORES_COMPARTCOCHE3 + " TEXT," +
            COLUMN_CUETRABAJADORES_COMPARTCOCHEOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_DISPBICI1 + " TEXT," +
            COLUMN_CUETRABAJADORES_DISPBICI2 + " TEXT," +
            COLUMN_CUETRABAJADORES_DISPBICI3 + " TEXT," +
            COLUMN_CUETRABAJADORES_DISPBICIOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_MODOSALIDA + " TEXT," +
            COLUMN_CUETRABAJADORES_MODOSALIDAOTRO + " TEXT," +
            COLUMN_CUETRABAJADORES_CDEDADTRAB + " TEXT," +
            COLUMN_CUETRABAJADORES_CDSLAB + " TEXT," +
            COLUMN_CUETRABAJADORES_PUESTO + " TEXT," +
            COLUMN_CUETRABAJADORES_SUGERENCIAS + " TEXT )";
    public static final String SQL_DROP_CUETRABAJADORES = "DROP TABLE IF EXISTS " + TABLE_CUETRABAJADORES;

    //Tabla Versión
    public static final String TABLE_VERSION = "Version";
    public static final String COLUMN_VERSION_VERSION = "version";
    public static final String SQL_CREATE_VERSION = "CREATE TABLE "+ TABLE_VERSION + " ("+
            COLUMN_VERSION_VERSION + " TEXT )";
    public static final String SQL_DROP_VERSION = "DROP TABLE IF EXISTS " + TABLE_VERSION;
}
