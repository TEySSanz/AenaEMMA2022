package es.testadistica.www.aenaemma2022.utilidades;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;
import es.testadistica.www.aenaemma2022.searchablespinner.SearchableSpinner;
import es.testadistica.www.aenaemma2022.searchablespinner.mListString;

public class ModeloPasajeros5 extends Form {

    private int preguntaAnterior = 1;
    private int idCue;
    private int idAeropuerto;
    private int finCue = 33;
    private boolean resultValue;

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_TIME = "HH:mm";
    private Date fechaActual = Calendar.getInstance().getTime();
    private SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_SHORT);
    private SimpleDateFormat sdfTime = new SimpleDateFormat(DATE_FORMAT_TIME);

    public ModeloPasajeros5(Activity surveyAct, int pregunta, DBHelper conn) {

        super(surveyAct, pregunta, conn);
    }

    @Override
    public int getLayoutId() {
        return R.layout.form_modelpasajeros5;
    }

    @Override
    public void initFormView() {

        idCue = cue.getIden();
        idAeropuerto = cue.getIdAeropuerto();
        System.out.println("idCue: "+idCue);
        System.out.println("idAeropuerto: "+idAeropuerto);
        showQuestion(pregunta);

        iniciarTextosAeropuertos();
        iniciarSpinners();
        //iniciarTimePickers();;
        condicionesSpinners();
        condicionesRadioButton();
        condicionesChecks();
        //condicionesEditText();
        condicionesRatingBar();
    }

    private void iniciarTextosAeropuertos(){
        //Por defecto se muestran las opciones del cuestionario de Madrid, si de algún aeropuerto se cambian los textos hay que incluirlo
        // en el switch
        switch (idAeropuerto){
            case 4:
                //Ibiza
                //P1
                activity.findViewById(R.id.survey_text_cdpaisna).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdpaisna_m4).setVisibility(VISIBLE);
                //P2
                activity.findViewById(R.id.survey_text_cdpaisre).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdpaisre_m4).setVisibility(VISIBLE);
                //P3 NO
                activity.findViewById(R.id.survey_text_cdlocaco_prov).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdlocaco_prov_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_viene_re).setVisibility(GONE);
                activity.findViewById(R.id.survey_m5_text_viene_re).setVisibility(VISIBLE);
                //P4 NO
                activity.findViewById(R.id.survey_radio_cdalojin_option10).setVisibility(GONE);
                //P5 NO
                //P6
                activity.findViewById(R.id.survey_text_sitiopark).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_sitiopark_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_edit_text_pqfuera).setVisibility(GONE);
                activity.findViewById(R.id.survey_edit_text_pqfuera_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_radio_sitiopark_option1).setVisibility(GONE);
                activity.findViewById(R.id.survey_radio_sitiopark_option1_m4).setVisibility(VISIBLE);
                //P7
                activity.findViewById(R.id.survey_text_acomptes).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_acomptes_m4).setVisibility(VISIBLE);
                //P8
                activity.findViewById(R.id.survey_text_hllega).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_hllega_m4).setVisibility(VISIBLE);
                //P9
                activity.findViewById(R.id.survey_text_cdiaptod).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdiaptod_m4).setVisibility(VISIBLE);
                //P10
                activity.findViewById(R.id.survey_text_numvuepa).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_numvuepa_m4).setVisibility(VISIBLE);
                //P11
                activity.findViewById(R.id.survey_text_cdterm).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdterm_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_cdociaar).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdociaar_m4).setVisibility(VISIBLE);
                //P12
                activity.findViewById(R.id.survey_text_cdiaptof).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdiaptof_m4).setVisibility(VISIBLE);
                //P13
                activity.findViewById(R.id.survey_text_cdmviaje).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdmviaje_m4).setVisibility(VISIBLE);
                //P14
                activity.findViewById(R.id.survey_text_cdidavue).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdidavue_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_cdidavue_a).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdidavue_a_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_cdidavue_b).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdidavue_b_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_edit_text_taus).setVisibility(GONE);
                activity.findViewById(R.id.survey_edit_text_taus_m4).setVisibility(VISIBLE);
                //P15
                activity.findViewById(R.id.survey_text_npers).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_npers_m4).setVisibility(VISIBLE);
                //P16
                activity.findViewById(R.id.survey_text_nniños).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_nniños_m4).setVisibility(VISIBLE);
                //P17
                activity.findViewById(R.id.survey_text_relacion).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_relacion_m4).setVisibility(VISIBLE);
                //P18
                activity.findViewById(R.id.survey_text_cdtreser).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdtreser_m4).setVisibility(VISIBLE);
                //P19
                activity.findViewById(R.id.survey_text_cdbillet).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdbillet_m4).setVisibility(VISIBLE);
                //P20
                activity.findViewById(R.id.survey_text_nviaje).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_nviaje_m4).setVisibility(VISIBLE);
                //P21
                activity.findViewById(R.id.survey_text_vol12mes).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_vol12mes_m4).setVisibility(VISIBLE);
                //P22
                activity.findViewById(R.id.survey_text_eleccovid).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_eleccovid_m5).setVisibility(VISIBLE);
                //P23
                activity.findViewById(R.id.survey_text_p44factu).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_p44factu_m5).setVisibility(VISIBLE);
                //P24
                activity.findViewById(R.id.survey_text_nperbul).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_nperbul_m5).setVisibility(VISIBLE);
                //P25
                activity.findViewById(R.id.survey_text_chekinb).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_chekinb_m5).setVisibility(VISIBLE);
                //P26
                activity.findViewById(R.id.survey_text_consume).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_consume_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_gas_cons).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_gas_cons_m5).setVisibility(VISIBLE);
                //P27
                activity.findViewById(R.id.survey_text_comprart).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_comprart_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_gas_com).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_gas_com_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_comprart_b).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_comprart_b_m5).setVisibility(VISIBLE);
                //P28
                activity.findViewById(R.id.survey_text_cdslab).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdslab_m5).setVisibility(VISIBLE);
                //P29
                activity.findViewById(R.id.survey_text_cdsprof).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdsprof_m5).setVisibility(VISIBLE);
                //P30
                activity.findViewById(R.id.survey_text_estudios).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_estudios_m5).setVisibility(VISIBLE);
                //P31
                activity.findViewById(R.id.survey_text_cdedad).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdedad_m5).setVisibility(VISIBLE);
                //P32
                activity.findViewById(R.id.survey_text_cdsexo).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdsexo_m5).setVisibility(VISIBLE);
                //P33
                activity.findViewById(R.id.survey_text_valorexp).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_valorexp_m5).setVisibility(VISIBLE);
                break;
            case 6:
                //Menorca
                //P1
                activity.findViewById(R.id.survey_text_cdpaisna).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdpaisna_m4).setVisibility(VISIBLE);
                //P2
                activity.findViewById(R.id.survey_text_cdpaisre).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdpaisre_m4).setVisibility(VISIBLE);
                //P3 NO
                activity.findViewById(R.id.survey_text_cdlocaco_prov).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdlocaco_prov_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_viene_re).setVisibility(GONE);
                activity.findViewById(R.id.survey_m5_MAH_text_viene_re).setVisibility(VISIBLE);
                //P4 NO
                activity.findViewById(R.id.survey_radio_cdalojin_option10).setVisibility(GONE);
                //P5 NO
                //P6
                activity.findViewById(R.id.survey_text_sitiopark).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_sitiopark_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_edit_text_pqfuera).setVisibility(GONE);
                activity.findViewById(R.id.survey_edit_text_pqfuera_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_radio_sitiopark_option1).setVisibility(GONE);
                activity.findViewById(R.id.survey_radio_sitiopark_option1_m4).setVisibility(VISIBLE);
                //P7
                activity.findViewById(R.id.survey_text_acomptes).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_acomptes_m4).setVisibility(VISIBLE);
                //P8
                activity.findViewById(R.id.survey_text_hllega).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_hllega_m4).setVisibility(VISIBLE);
                //P9
                activity.findViewById(R.id.survey_text_cdiaptod).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdiaptod_m4).setVisibility(VISIBLE);
                //P10
                activity.findViewById(R.id.survey_text_numvuepa).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_numvuepa_m4).setVisibility(VISIBLE);
                //P11
                activity.findViewById(R.id.survey_text_cdterm).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdterm_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_cdociaar).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdociaar_m4).setVisibility(VISIBLE);
                //P12
                activity.findViewById(R.id.survey_text_cdiaptof).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdiaptof_m4).setVisibility(VISIBLE);
                //P13
                activity.findViewById(R.id.survey_text_cdmviaje).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdmviaje_m4).setVisibility(VISIBLE);
                //P14
                activity.findViewById(R.id.survey_text_cdidavue).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdidavue_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_cdidavue_a).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdidavue_a_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_cdidavue_b).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdidavue_b_m4).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_edit_text_taus).setVisibility(GONE);
                activity.findViewById(R.id.survey_edit_text_taus_m4).setVisibility(VISIBLE);
                //P15
                activity.findViewById(R.id.survey_text_npers).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_npers_m4).setVisibility(VISIBLE);
                //P16
                activity.findViewById(R.id.survey_text_nniños).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_nniños_m4).setVisibility(VISIBLE);
                //P17
                activity.findViewById(R.id.survey_text_relacion).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_relacion_m4).setVisibility(VISIBLE);
                //P18
                activity.findViewById(R.id.survey_text_cdtreser).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdtreser_m4).setVisibility(VISIBLE);
                //P19
                activity.findViewById(R.id.survey_text_cdbillet).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdbillet_m4).setVisibility(VISIBLE);
                //P20
                activity.findViewById(R.id.survey_text_nviaje).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_nviaje_m4).setVisibility(VISIBLE);
                //P21
                activity.findViewById(R.id.survey_text_vol12mes).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_vol12mes_m4).setVisibility(VISIBLE);
                //P22
                activity.findViewById(R.id.survey_text_eleccovid).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_eleccovid_m5).setVisibility(VISIBLE);
                //P23
                activity.findViewById(R.id.survey_text_p44factu).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_p44factu_m5).setVisibility(VISIBLE);
                //P24
                activity.findViewById(R.id.survey_text_nperbul).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_nperbul_m5).setVisibility(VISIBLE);
                //P25
                activity.findViewById(R.id.survey_text_chekinb).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_chekinb_m5).setVisibility(VISIBLE);
                //P26
                activity.findViewById(R.id.survey_text_consume).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_consume_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_gas_cons).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_gas_cons_m5).setVisibility(VISIBLE);
                //P27
                activity.findViewById(R.id.survey_text_comprart).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_comprart_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_gas_com).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_gas_com_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_comprart_b).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_comprart_b_m5).setVisibility(VISIBLE);
                //P28
                activity.findViewById(R.id.survey_text_cdslab).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdslab_m5).setVisibility(VISIBLE);
                //P29
                activity.findViewById(R.id.survey_text_cdsprof).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdsprof_m5).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_radio_cdsprof_option9).setVisibility(VISIBLE);
                //P30
                activity.findViewById(R.id.survey_text_estudios).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_estudios_m5).setVisibility(VISIBLE);
                //P31
                activity.findViewById(R.id.survey_text_cdedad).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdedad_m5).setVisibility(VISIBLE);
                //P32
                activity.findViewById(R.id.survey_text_cdsexo).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdsexo_m5_MAH).setVisibility(VISIBLE);
                //P33
                activity.findViewById(R.id.survey_text_valorexp).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_valorexp_m5).setVisibility(VISIBLE);
                break;
        }
    }

    private void iniciarSpinners() {

        ArrayList<mListString> paisesAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES,"iden", "codigo","descripcion", "descripcion"));
        ArrayList<mListString> paises1y2Adapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES1Y2,"iden", "codigo","zonas || ', ' || descripcion", "zonas || ', ' || descripcion"));
        ArrayList<mListString> islasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOISLAS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> islasLocalidadAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOISLASLOCALIDAD,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> provinciasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> municipiosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> motivoViajeAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMOTIVOVIAJE,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> motivoViajefiltroAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMOTIVOVIAJEFILTRO,"iden", "codigo","motivo", "codigo"));

        String filtroAeropuerto = "iden IS NOT NULL"; //Para que salgan todos
        switch (idAeropuerto){
            case 4:
                //Ibiza
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_IBZOLEADA+"=1 ";
                break;
            case 6:
                //Menorca
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_MAHOLEADA+"=1 ";
                break;
        }
        ArrayList<mListString> companiasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto));

        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 4:
                //Ibiza
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_IBZAEREA+"=1 ";
                break;
            case 6:
                //Menorca
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_MAHAEREA+"=1 ";
                break;
        }
        ArrayList<mListString> companiasPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));
        ArrayList<mListString> productosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPRODUCTOS,"iden", "codigo","descripcion", "codigo"));

        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 4:
                //Ibiza
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_IBZOLEADA+" = 1";
                break;
            case 6:
                //Menorca
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_MAHOLEADA+" = 1";
                break;

        }
        ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));

        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 4:
                //Ibiza
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_IBZPRINCIPAL+" = 1";
                break;
            case 6:
                //Menorca
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_MAHPRINCIPAL+" = 1";
                break;
        }
        ArrayList<mListString> tipoAeropuertosPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));

        //P1
        //Asigna los valores del desplegable de paises
        SearchableSpinner sp_cdpasina;
        sp_cdpasina = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);
        sp_cdpasina.setAdapter(paisesAdapter, 1, 1, activity.getString(R.string.spinner_pais_title), activity.getString(R.string.spinner_close));

        sp_cdpasina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdpasina.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P2
        //Asigna los valores del desplegable de paises
        SearchableSpinner sp_cdpasire;
        sp_cdpasire = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);
        sp_cdpasire.setAdapter(paisesAdapter, 1, 1, activity.getString(R.string.spinner_pais_title), activity.getString(R.string.spinner_close));

        sp_cdpasire.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdpasire.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado_prov);
        sp_cdlocado_prov.setAdapter(provinciasAdapter, 1, 1, activity.getString(R.string.spinner_provincia_title), activity.getString(R.string.spinner_close));

        sp_cdlocado_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                sp_cdlocado_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
        sp_cdlocado.setAdapter(municipiosAdapter, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));

        sp_cdlocado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocado.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
        sp_distres_area.setAdapter(paises1y2Adapter, 1, 1, activity.getString(R.string.spinner_pais1y2_title), activity.getString(R.string.spinner_close));

        sp_distres_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_distres_area.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P3 Asigna los valores a los despeglables de aeropuertos y localidades

        SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);
        sp_cdlocaco_prov.setAdapter(islasAdapter, 1, 1, activity.getString(R.string.spinner_isla_title), activity.getString(R.string.spinner_close));

        sp_cdlocaco_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocaco_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
        sp_cdlocaco.setAdapter(islasLocalidadAdapter, 1, 1, activity.getString(R.string.spinner_localidad_title), activity.getString(R.string.spinner_close));

        sp_cdlocaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocaco.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_cdiaptoo;
        sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);
        sp_cdiaptoo.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

        sp_cdiaptoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P9
        //Asigna los valores del desplegable de paises
        SearchableSpinner sp_cdiaptod;
        sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);
        sp_cdiaptod.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

        sp_cdiaptod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdiaptod.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P10
        //Asigna los valores del desplegable de companias
        SearchableSpinner sp_numvuepa;
        sp_numvuepa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_numvuepa);
        sp_numvuepa.setAdapter(companiasAdapter, 1, 1, activity.getString(R.string.spinner_compania_title), activity.getString(R.string.spinner_close));

        sp_numvuepa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_numvuepa.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P11b
        //Asigna los valores del desplegable de companias
        SearchableSpinner sp_cdociaar;
        sp_cdociaar = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdociaar);
        sp_cdociaar.setAdapter(companiasPpalAdapter, 1, 1, activity.getString(R.string.spinner_compania_title), activity.getString(R.string.spinner_close));

        sp_cdociaar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdociaar.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P12
        //Asigna los valores del desplegable de paises
        SearchableSpinner sp_cdiaptof;
        sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);
        sp_cdiaptof.setAdapter(tipoAeropuertosPpalAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

        sp_cdiaptof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdiaptof.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P13
        //Asigna los valores del desplegable de motivo viaje
        SearchableSpinner sp_cdmviaje;
        sp_cdmviaje = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviaje);
        sp_cdmviaje.setAdapter(motivoViajeAdapter, 1, 1, activity.getString(R.string.spinner_motivo_title), activity.getString(R.string.spinner_close));

        sp_cdmviaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdmviaje.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_cdmviajefiltro;
        sp_cdmviajefiltro = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviajefiltro);
        sp_cdmviajefiltro.setAdapter(motivoViajefiltroAdapter, 1, 1, activity.getString(R.string.spinner_motivo_title), activity.getString(R.string.spinner_close));

        sp_cdmviajefiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdmviajefiltro.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P27
        //Asigna los valores del desplegable de productos
        SearchableSpinner sp_prod1;
        sp_prod1 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod1);
        sp_prod1.setAdapter(productosAdapter, 1, 1, activity.getString(R.string.spinner_producto_title), activity.getString(R.string.spinner_close));

        sp_prod1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_prod1.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_prod2;
        sp_prod2 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod2);
        sp_prod2.setAdapter(productosAdapter, 1, 1, activity.getString(R.string.spinner_producto_title), activity.getString(R.string.spinner_close));

        sp_prod2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_prod2.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_prod3;
        sp_prod3 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod3);
        sp_prod3.setAdapter(productosAdapter, 1, 1, activity.getString(R.string.spinner_producto_title), activity.getString(R.string.spinner_close));

        sp_prod3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_prod3.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_prod4;
        sp_prod4 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod4);
        sp_prod4.setAdapter(productosAdapter, 1, 1, activity.getString(R.string.spinner_producto_title), activity.getString(R.string.spinner_close));

        sp_prod4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_prod4.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_prod5;
        sp_prod5 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod5);
        sp_prod5.setAdapter(productosAdapter, 1, 1, activity.getString(R.string.spinner_producto_title), activity.getString(R.string.spinner_close));

        sp_prod5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_prod5.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void condicionesSpinners() {
        //P1
        final SearchableSpinner sp_cdpaisna = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);

        sp_cdpaisna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdpaisna.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_cdociaar.getSelectedItem().toString().substring(0,3);
                String texto = getValorDesplegable(sp_cdpaisna).substring(0,3);

                if (!texto.equals("999")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdpaisnaotro));
                    activity.findViewById(R.id.survey_layout_cdpaisnaotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdpaisnaotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //P2
        final SearchableSpinner sp_cdpaisre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);

        sp_cdpaisre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdpaisre.setBackgroundResource(android.R.drawable.btn_dropdown);
                //Blanquear todas las opciones cuando se cambia de pais
                SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado_prov);
                SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
                SearchableSpinner sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
                sp_cdlocado_prov.setSelection(0);
                sp_cdlocado_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
                sp_cdlocado.setSelection(0);
                sp_cdlocado.setBackgroundResource(android.R.drawable.btn_dropdown);
                sp_distres_area.setSelection(0);
                sp_distres_area.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto = getValorDesplegable(sp_cdpaisre).substring(0,3);
                if (texto.equals("724")){ //España
                    activity.findViewById(R.id.survey_layout_cdlocado_esp).setVisibility(VISIBLE);
                    activity.findViewById(R.id.survey_layout_cdlocado_no_esp).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdlocado_esp).setVisibility(GONE);
                    if (compruebaListaPaises1y2(texto) > 0){ //Si el pais es uno de la lista 1 y 2 se habilita para introducir el área / región
                        String filtro = " iden = 0 OR "+Contracts.COLUMN_TIPOPAISES1Y2_CODIGOPAIS+" = '"+texto+"'";
                        ArrayList<mListString> paises1y2Adapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES1Y2,"iden", "codigo","zonas || ', ' || descripcion", "zonas || ', ' || descripcion", filtro));
                        sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
                        sp_distres_area.setAdapter(paises1y2Adapter, 1, 1, activity.getString(R.string.spinner_pais1y2_title), activity.getString(R.string.spinner_close));
                        activity.findViewById(R.id.survey_layout_cdlocado_no_esp).setVisibility(VISIBLE);

                    } else {
                        activity.findViewById(R.id.survey_layout_cdlocado_no_esp).setVisibility(GONE);
                    }
                }
                if (!texto.equals("999")) {
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdpaisreotro));
                    activity.findViewById(R.id.survey_layout_cdpaisreotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdpaisreotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado_prov);

        //P2 Filtro municipios
        sp_cdlocado_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String filtro = " provincia IN (";

                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                } else if (id > 9 && id <=57){
                    filtro =  filtro +"'00','"+id+"','99',";
                }
                /*else if (id == 53){
                    filtro =  filtro +"'99',";
                } */ else {
                    filtro = " (iden > -1 ";
                }

                if (!filtro.contains(")")) {
                    filtro = filtro.substring(0, filtro.length()-1);
                    filtro = filtro + ")";
                }

                final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
                ArrayList<mListString> municipioAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                sp_cdlocado.setAdapter(municipioAdapter, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

        sp_cdlocado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdlocado.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_cdlocado).substring(0,5);


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P3 Filtro municipios
        final SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);

        sp_cdlocaco_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocaco_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_cdlocaco_prov).substring(0,2);

                String texto1 = " iden IS NOT NULL ";

                if (texto.equals("01") || texto.equals("02") || texto.equals("03")){
                    texto1 = " iden = 0 OR "+Contracts.COLUMN_TIPOISLASLOCALIDAD_CODIGOISLA+"= '"+texto+"'";
                }

                final SearchableSpinner sp_cdlocaco= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
                ArrayList<mListString> islasLocalidadAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOISLASLOCALIDAD,"iden", "codigo","descripcion", "codigo", texto1));
                sp_cdlocaco.setAdapter(islasLocalidadAdapter, 1, 1, activity.getString(R.string.spinner_localidad_title), activity.getString(R.string.spinner_close));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);

        sp_cdlocaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocaco.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_cdlocaco).substring(0,4);

                if (!texto.equals("9999")) {
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdlocacootro));
                    activity.findViewById(R.id.survey_layout_cdlocacootro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdlocacootro).setVisibility(VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);

        sp_cdiaptoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);
                final RadioButton rbCdiaptoo = (RadioButton) activity.findViewById(R.id.survey_radio_cdiaptoo);
                if (rbCdiaptoo.isChecked()) {

                    String texto = getValorDesplegable(sp_cdiaptoo).substring(0, 3);

                    String filtroAeropuerto1 = " iden IS NOT NULL "; //Para que salgan todos
                    switch (idAeropuerto) {
                        case 4:
                            //IBIZA
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_IBZPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_IBZPRINCIPAL + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                            break;
                        case 6:
                            //Menorca
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MAHPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MAHPRINCIPAL + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                            break;

                    }

                    final SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);
                    ArrayList<mListString> tipoAeropuertosPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS, "iden", "codigo", "descripcion", "descripcion", filtroAeropuerto1));
                    sp_cdiaptof.setAdapter(tipoAeropuertosPpalAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

                    String filtroAeropuerto2 = " iden IS NOT NULL "; //Para que salgan todos
                    switch (idAeropuerto) {
                        case 4:
                            //Ibiza
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_IBZPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_IBZOLEADA + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                            break;
                        case 6:
                            //Menorca
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MAHPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MAHOLEADA + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                            break;
                    }

                    final SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);
                    ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS, "iden", "codigo", "descripcion", "descripcion", filtroAeropuerto2));
                    sp_cdiaptod.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));
                }
                String texto = getValorDesplegable(sp_cdiaptoo).substring(0, 3);
                if (!texto.equals("ZZZ")) {
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdiaptoootro_m4));
                    activity.findViewById(R.id.survey_layout_cdiaptoootro_m4).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdiaptoootro_m4).setVisibility(VISIBLE);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P9
        final SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);

        sp_cdiaptod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdiaptod.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_cdiaptod).substring(0,3);

                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto1 = getValorDesplegable(sp_cdiaptoo).substring(0,3);
                final RadioButton rbCdiaptoo = (RadioButton) activity.findViewById(R.id.survey_radio_cdiaptoo);
                if (rbCdiaptoo.isChecked()) {

                    String filtroAeropuerto1 = " iden IS NOT NULL "; //Para que salgan todos
                    switch (idAeropuerto) {

                        case 4:
                            //Ibiza
                            if (texto1.equals("000")||(texto1.equals("ZZZ"))) {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_IBZPRINCIPAL+" = 1 ";
                            } else {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_IBZPRINCIPAL+" = 1 AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto+"') AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto1+"')";
                            }
                            break;
                        case 6:
                            //Menorca
                            if (texto1.equals("000")||(texto1.equals("ZZZ"))) {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_MAHPRINCIPAL+" = 1 ";
                            } else {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_MAHPRINCIPAL+" = 1 AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto+"') AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto1+"')";
                            }
                            break;
                    }

                    final SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);
                    ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS, "iden", "codigo", "descripcion", "descripcion", filtroAeropuerto1));
                    sp_cdiaptof.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));
                }

                if (!texto.equals("ZZZ")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdiaptodotro));
                    activity.findViewById(R.id.survey_layout_cdiaptodotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdiaptodotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P11
        final SearchableSpinner sp_cdociaar = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdociaar);

        sp_cdociaar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdociaar.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_cdociaar.getSelectedItem().toString().substring(0,3);
                String texto = getValorDesplegable(sp_cdociaar).substring(0,3);

                if (!texto.equals("999")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdociaarotro));
                    activity.findViewById(R.id.survey_layout_cdociaarotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdociaarotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P12
        final SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);

        sp_cdiaptof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdiaptof.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_cdiaptof.getSelectedItem().toString().substring(0,3);
                String texto = getValorDesplegable(sp_cdiaptof).substring(0,3);

                if (!texto.equals("ZZZ")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdiaptofotro));
                    activity.findViewById(R.id.survey_layout_cdiaptofotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdiaptofotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P13
        final SearchableSpinner sp_cdmviajefiltro = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviajefiltro);
        final SearchableSpinner sp_cdmviaje = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviaje);

        sp_cdmviajefiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdmviajefiltro.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String textoViajeFiltro = sp_cdmviajefiltro.getSelectedItem().toString().substring(0,2);
                String textoViajeFiltro = getValorDesplegable(sp_cdmviajefiltro).substring(0,2);
                int numero=209;

                String filtroViaje = " iden IS NOT NULL ";

                if (textoViajeFiltro.equals("10") || textoViajeFiltro.equals("20") || textoViajeFiltro.equals("29")){
                    activity.findViewById(R.id.survey_spinner_cdmviaje).setVisibility(VISIBLE);
                    filtroViaje = " "+ Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN (" + numero + ") AND " +Contracts.COLUMN_TIPOMOTIVOVIAJE_CODGRUPO +" LIKE '"+textoViajeFiltro+" %' OR iden = 0";
                }

                ArrayList<mListString> motivoViajeAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMOTIVOVIAJE,"iden", "codigo","descripcion", "codigo", filtroViaje));
                sp_cdmviaje.setAdapter(motivoViajeAdapter, 1, 1, activity.getString(R.string.spinner_motivo_title), activity.getString(R.string.spinner_close));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //P18
        final SearchableSpinner sp_numvuepa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_numvuepa);

        sp_numvuepa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_numvuepa.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_numvuepa.getSelectedItem().toString().substring(0,3);
                String texto = getValorDesplegable(sp_numvuepa).substring(0,3);
                TextView miTextView =(TextView) activity.findViewById(R.id.survey_text_numvuepa_companyCode);
                if (!texto.equals("000")){
                    miTextView.setText(texto+"-");
                } else {
                    miTextView.setText("XXX-");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void condicionesRadioButton() {
        //P3
        RadioGroup rgViene_re = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re_diaptoo);
        rgViene_re.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgViene_re.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_cdlocaco:
                        activity.findViewById(R.id.survey_layout_cdlocaco).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_layout_cdiaptoo).setVisibility(GONE);
                        activity.findViewById(R.id.survey_layout_cdiaptoootro_m4).setVisibility(GONE);
                        break;
                    case R.id.survey_radio_cdiaptoo:
                        activity.findViewById(R.id.survey_layout_cdiaptoo).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_layout_cdlocaco).setVisibility(GONE);
                        activity.findViewById(R.id.survey_layout_cdiaptoootro_m4).setVisibility(GONE);
                        break;
                    case R.id.survey_radio_viene_re:
                        SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado_prov);
                        //String textSpCdlocado = sp_cdlocado.getSelectedItem().toString().substring(0, 5);
                        String textSpCdlocado = getValorDesplegable(sp_cdlocado_prov).substring(0, 2);
                        if (textSpCdlocado.contains("07")) {
                            activity.findViewById(R.id.survey_text_viene_re_aviso_AGP).setVisibility(GONE);
                        } else {
                            activity.findViewById(R.id.survey_text_viene_re_aviso_AGP).setVisibility(VISIBLE);
                        }
                        activity.findViewById(R.id.survey_layout_cdiaptoo).setVisibility(GONE);
                        activity.findViewById(R.id.survey_layout_cdlocaco).setVisibility(GONE);
                        break;
                }
            }
        });
        //P4
        RadioGroup rgCdalojin= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojin_crucero);
        rgCdalojin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCdalojin.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_cdalojin_option9:
                        activity.findViewById(R.id.survey_layout_cdalojin_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_cdalojin_otros).setVisibility(GONE);
                        break;
                }
            }
        });
        //P5
        RadioGroup rgUmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_m4_ultimodo_1modo);
        rgUmodos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgUmodos.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
                switch(i)
                {
                    case R.id.survey_radio_m4_ultimodo_1modo_option8:
                        activity.findViewById(R.id.survey_edit_m4_ultimodo_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_edit_m4_ultimodo_otros).setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
        //P6
        RadioGroup rgSitiopark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);
        rgSitiopark.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgSitiopark.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_sitiopark_option4:
                        activity.findViewById(R.id.survey_layout_pqfuera).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_pqfuera).setVisibility(GONE);
                        break;
                }
            }
        });
        //P7
        RadioGroup rgAcomptes= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_acomptes);
        rgAcomptes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgAcomptes.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_acomptes_option3:
                        activity.findViewById(R.id.survey_layout_acomptes_especificar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_acomptes_especificar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P11
        RadioGroup rgCdterm = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdterm);
        rgCdterm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCdterm.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_cdterm_option2:
                        activity.findViewById(R.id.survey_layout_cdociaar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_cdociaar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P14
        RadioGroup rgCdidavue = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdidavue);
        rgCdidavue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCdidavue.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_cdidavue_option1:
                        activity.findViewById(R.id.survey_layout_taus).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_text_cdidavue_a_m4).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_text_cdidavue_b_m4).setVisibility(GONE);
                        break;
                    case R.id.survey_radio_cdidavue_option2:
                        activity.findViewById(R.id.survey_layout_taus).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_text_cdidavue_a_m4).setVisibility(GONE);
                        activity.findViewById(R.id.survey_text_cdidavue_b_m4).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_taus).setVisibility(GONE);
                        activity.findViewById(R.id.survey_text_cdidavue_a_m4).setVisibility(GONE);
                        activity.findViewById(R.id.survey_text_cdidavue_b_m4).setVisibility(GONE);
                        break;
                }
            }
        });
        //P15
        RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
        rgNpers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgNpers.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_npers_option3:
                        activity.findViewById(R.id.survey_layout_npers_especificar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_npers_especificar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P17
        final RadioGroup rgRelacion = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_relacion);
        rgRelacion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgRelacion.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P18
        RadioGroup rgCdtreser = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdtreser);
        rgCdtreser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCdtreser.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_cdtreser_dias:
                        activity.findViewById(R.id.survey_layout_cdtreser_especificar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_cdtreser_especificar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P19
        final RadioGroup rgCdbillet = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdbillet);
        rgCdbillet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdbillet.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P20
        RadioGroup rgNviaje = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nviaje);
        rgNviaje.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgNviaje.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_nviaje_numviajes:
                        activity.findViewById(R.id.survey_layout_nviaje_especificar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_nviaje_especificar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P21
        RadioGroup rgVol12mes = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_vol12mes);
        rgVol12mes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgVol12mes.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_vol12mes_numviajes:
                        activity.findViewById(R.id.survey_layout_vol12mes_especificar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_vol12mes_especificar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P22
        final RadioGroup rgEleccovid = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_eleccovid);
        rgEleccovid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgEleccovid.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P23
        RadioGroup rgP44factu = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_p44factu);
        rgP44factu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgP44factu.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_p44factu_option1:
                        activity.findViewById(R.id.survey_layout_bulgrupo).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_bulgrupo).setVisibility(GONE);
                        break;
                }
            }
        });
        //P24
        RadioGroup rgNperbul = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nperbul);
        rgNperbul.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgNperbul.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_nperbul_option3:
                        activity.findViewById(R.id.survey_layout_nperbul_especificar).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_nperbul_especificar).setVisibility(GONE);
                        break;
                }
            }
        });
        //P25
        final RadioGroup rgChekinb = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_chekinb);
        rgChekinb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgChekinb.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P26
        RadioGroup rgConsume = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_consume);
        rgConsume.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgConsume.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_consume_option1:
                        activity.findViewById(R.id.survey_layout_gas_cons).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_gas_cons).setVisibility(GONE);
                        break;
                }
            }
        });
        //P27
        RadioGroup rgCompart = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_comprart);
        rgCompart.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCompart.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_comprart_option1:
                        activity.findViewById(R.id.survey_layout_gas_com).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_gas_com).setVisibility(GONE);
                        break;
                }
            }
        });
        //P28
        final RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
        rgCdslab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P29
        final RadioGroup rgCdsprof = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsprof);
        rgCdsprof.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsprof.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P30
        final RadioGroup rgEstudios = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_estudios);
        rgEstudios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgEstudios.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P31
        final RadioGroup rgCdedad = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedad);
        rgCdedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdedad.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P32
        final RadioGroup rgCdsexo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsexo);
        rgCdsexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });



    }

    private void condicionesChecks(){
        CheckBox ckCdidavue = (CheckBox) activity.findViewById(R.id.survey_check_cdidavue_option0);

        ckCdidavue.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                EditText etTaus = (EditText) activity.findViewById(R.id.survey_edit_taus);
                if (compoundButton.isChecked()){
                    etTaus.setText("");
                    etTaus.setEnabled(false);
                    etTaus.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    compoundButton.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                } else {
                    etTaus.setEnabled(true);
                    etTaus.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    etTaus.setError(null);
                }
            }
        });

    }

    private void condicionesRatingBar(){
        RatingBar rabValorexp = (RatingBar) activity.findViewById(R.id.survey_rating_valorexp);
        rabValorexp.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rabValorexp.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
    }

    @Override
    public int onNextPressed(int p) {
        if (checkQuestion(p)) {
            pregunta = p;
            saveQuestion(p);
            hideQuestions();
            setPreguntaAnterior(p);
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return showNextQuestion(p);
        } else {
            return 0;
        }
    }

    @Override
    public int onPreviousPressed(int actual, int anterior) {

        Button siguiente = (Button) activity.findViewById(R.id.survey_button_next);
        siguiente.setVisibility(VISIBLE);

        checkDelete(actual);
        hideQuestions();
        setPreguntaAnterior(anterior - 1);

        return showQuestion(anterior);
    }

    public int showQuestion(int show) {
        Button next = (Button) activity.findViewById(R.id.survey_button_next);
        Button previo = (Button) activity.findViewById(R.id.survey_button_previous);
        Button save = (Button) activity.findViewById(R.id.survey_button_save);
        switch (show) {
            case 1:
                //B1
                LinearLayout p1 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisna);
                previo.setVisibility(GONE);
                save.setVisibility(GONE);
                p1.setVisibility(VISIBLE);
                break;
            case 2:
                //P2
                LinearLayout p2 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisre);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p2.setVisibility(VISIBLE);
                break;
            case 3:
                //P3
                LinearLayout p3 = (LinearLayout) activity.findViewById(R.id.survey_layout_viene_re_cdlocaco_diaptoo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p3.setVisibility(VISIBLE);
                break;
            case 4:
                //P4
                LinearLayout p4 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojin_crucero);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p4.setVisibility(VISIBLE);
                break;
            case 5:
                //P5
                LinearLayout p5 = (LinearLayout) activity.findViewById(R.id.survey_m4_layout_ultimod);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p5.setVisibility(VISIBLE);
                break;
            case 6:
                //P6
                LinearLayout p6 = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
                RadioButton rbSitiopark = (RadioButton) activity.findViewById(R.id.survey_radio_sitiopark_option1);
                RadioButton rbSitiopark_m4 = (RadioButton) activity.findViewById(R.id.survey_radio_sitiopark_option1_m4);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p6.setVisibility(VISIBLE);
                rbSitiopark.setVisibility(GONE);
                rbSitiopark_m4.setVisibility(VISIBLE);
                break;
            case 7:
                //P7
                LinearLayout p7 = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p7.setVisibility(VISIBLE);
                break;
            case 8:
                //P8
                LinearLayout p8 = (LinearLayout) activity.findViewById(R.id.survey_layout_hllega);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p8.setVisibility(VISIBLE);
                break;
            case 9:
                //P9
                LinearLayout p9 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptod);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p9.setVisibility(VISIBLE);
                break;
            case 10:
                //P10
                LinearLayout p10 = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p10.setVisibility(VISIBLE);
                break;
            case 11:
                //P11
                LinearLayout p11 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdterm);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p11.setVisibility(VISIBLE);
                break;
            case 12:
                //P12
                LinearLayout p12 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p12.setVisibility(VISIBLE);
                break;
            case 13:
                //P13
                LinearLayout p13 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p13.setVisibility(VISIBLE);
                break;
            case 14:
                //P14
                LinearLayout p14 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p14.setVisibility(VISIBLE);
                break;
            case 15:
                //P15
                LinearLayout p15 = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p15.setVisibility(VISIBLE);
                break;
            case 16:
                //P16
                LinearLayout p16 = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p16.setVisibility(VISIBLE);
                break;
            case 17:
                //P17
                LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_relacion);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p17.setVisibility(VISIBLE);
                break;
            case 18:
                //P18
                LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdtreser);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p18.setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdbillet);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p19.setVisibility(VISIBLE);
                break;
            case 20:
                //P20
                LinearLayout p20 = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p20.setVisibility(VISIBLE);
                break;
            case 21:
                //P21
                LinearLayout p21 = (LinearLayout) activity.findViewById(R.id.survey_layout_vol12mes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p21.setVisibility(VISIBLE);
                break;
            case 22:
                //P22
                LinearLayout p22 = (LinearLayout) activity.findViewById(R.id.survey_layout_eleccovid);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p22.setVisibility(VISIBLE);
                break;
            case 23:
                //P23
                LinearLayout p23  = (LinearLayout) activity.findViewById(R.id.survey_layout_p44factu);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p23.setVisibility(VISIBLE);
                break;
            case 24:
                //P24
                LinearLayout p24 = (LinearLayout) activity.findViewById(R.id.survey_layout_nperbul);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p24.setVisibility(VISIBLE);
                break;
            case 25:
                //P25
                LinearLayout p25 = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p25.setVisibility(VISIBLE);
                break;
            case 26:
                //P26
                LinearLayout p26 = (LinearLayout) activity.findViewById(R.id.survey_layout_consume);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p26.setVisibility(VISIBLE);
                break;
            case 27:
                //P27
                LinearLayout p27 = (LinearLayout) activity.findViewById(R.id.survey_layout_comprart);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p27.setVisibility(VISIBLE);
                break;
            case 28:
                //P28
                LinearLayout p28 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdslab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p28.setVisibility(VISIBLE);
                break;
            case 29:
                //P29
                LinearLayout p29 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p29.setVisibility(VISIBLE);
                break;
            case 30:
                //P30
                LinearLayout p30 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p30.setVisibility(VISIBLE);
                break;
            case 31:
                //P31
                LinearLayout p31 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p31.setVisibility(VISIBLE);
                break;
            case 32:
                //P32
                LinearLayout p32 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p32.setVisibility(VISIBLE);
                break;
            case 33:
                //P33
                LinearLayout p33 = (LinearLayout) activity.findViewById(R.id.survey_layout_valorexp);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(GONE);
                p33.setVisibility(VISIBLE);
                break;
        }

        return show;
    }

    @Override
    public boolean checkQuestion(int check) {
        boolean activated = true;
        cue = fillQuest(cue);

        if (activated) {
            switch (check) {
                case 1:
                    final SearchableSpinner sp_cdpaisna = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);

                    if (getValorDesplegable(sp_cdpaisna).substring(0,3).equals("000")) {
                        sp_cdpaisna.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    if (activity.findViewById(R.id.survey_layout_cdpaisnaotro).getVisibility() == VISIBLE) {
                        EditText etCdpaisna = (EditText) activity.findViewById(R.id.survey_edit_cdpaisnaotro);
                        if (etCdpaisna.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCdpaisna.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdpaisna.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdpaisna.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 2:
                    final SearchableSpinner sp_cdpaisre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);

                    if (getValorDesplegable(sp_cdpaisre).substring(0,3).equals("000")) {
                        sp_cdpaisre.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    if (activity.findViewById(R.id.survey_layout_cdpaisreotro).getVisibility() == VISIBLE) {
                        EditText etCdpaisre = (EditText) activity.findViewById(R.id.survey_edit_cdpaisreotro);
                        if (etCdpaisre.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCdpaisre.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdpaisre.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdpaisre.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    if(activity.findViewById(R.id.survey_layout_cdlocado_no_esp).getVisibility()==VISIBLE){
                        final SearchableSpinner sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);

                        if (getValorDesplegable(sp_distres_area).substring(0,3).equals("000")) {
                            sp_distres_area.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }
                    if(activity.findViewById(R.id.survey_layout_cdlocado_esp).getVisibility()==VISIBLE){
                        /*if(!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdlocado), "00000")) {
                            return false;
                        }*/
                        final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

                        if (getValorDesplegable(sp_cdlocado).substring(0,3).equals("000")) {
                            sp_cdlocado.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }

                    break;
                case 3:
                    //P3

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_viene_re_diaptoo))){
                        return false;
                    }

                    if(activity.findViewById(R.id.survey_layout_cdlocaco).getVisibility()==VISIBLE){
                        final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);

                        if (getValorDesplegable(sp_cdlocaco).substring(0,4).equals("0000")) {
                            sp_cdlocaco.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }
                    if (activity.findViewById(R.id.survey_layout_cdlocacootro).getVisibility() == VISIBLE) {
                        EditText etCdlocaco = (EditText) activity.findViewById(R.id.survey_edit_cdlocacootro);
                        if (etCdlocaco.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCdlocaco.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdlocaco.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdlocaco.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    if(activity.findViewById(R.id.survey_layout_cdiaptoo).getVisibility()==VISIBLE){
                        final SearchableSpinner sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);

                        if (getValorDesplegable(sp_cdiaptoo).substring(0,3).equals("000")) {
                            sp_cdiaptoo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }
                    if(activity.findViewById(R.id.survey_layout_cdiaptoootro_m4).getVisibility()==VISIBLE) {
                        EditText etCdiaptoo = (EditText) activity.findViewById(R.id.survey_edit_cdiaptoootro_m4);
                        if (etCdiaptoo.getText().toString().isEmpty()){
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCdiaptoo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdiaptoo.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdiaptoo.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 4:
                    //P4
                    RadioGroup rgCdalojin = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojin_crucero);
                    EditText etCdalojin = (EditText) activity.findViewById(R.id.survey_edit_cdalojin_otros);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdalojin_crucero))){
                        return false;
                    }

                    if (rgCdalojin.getCheckedRadioButtonId()==R.id.survey_radio_cdalojin_option9 &&
                            etCdalojin.getText().toString().isEmpty()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etCdalojin.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etCdalojin.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etCdalojin.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 5:
                    //P5
                    RadioGroup rgUltimodo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_m4_ultimodo_1modo);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_m4_ultimodo_1modo))){
                        return false;
                    }

                    if (rgUltimodo.getCheckedRadioButtonId() == R.id.survey_radio_m4_ultimodo_1modo_option8){
                        EditText etUltimodo = (EditText) activity.findViewById(R.id.survey_edit_m4_ultimodo_otros);
                        if (etUltimodo.getText().toString().isEmpty()){
                            String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                            etUltimodo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etUltimodo.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etUltimodo.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 6:
                    //P6
                    RadioGroup rgSitiopark= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);

                    if(!requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_sitiopark))){
                        return false;
                    }
                    if (rgSitiopark.getCheckedRadioButtonId() == R.id.survey_radio_sitiopark_option4){
                        EditText etPqfuera = (EditText) activity.findViewById(R.id.survey_edit_pqfuera);
                        if (etPqfuera.getText().toString().isEmpty()){
                            String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                            etPqfuera.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etPqfuera.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etPqfuera.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 7:
                    //P7
                    RadioGroup rgAcomptes = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_acomptes);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_acomptes))){
                        return false;
                    }

                    if (rgAcomptes.getCheckedRadioButtonId() == R.id.survey_radio_acomptes_option3){
                        EditText etAcomptes_especificar = (EditText) activity.findViewById(R.id.survey_edit_acomptes_especificar);
                        if (stringToInt(etAcomptes_especificar.getText().toString())<3){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_mas2);
                            etAcomptes_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etAcomptes_especificar.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etAcomptes_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 8:
                    //P8
                    EditText etHora = (EditText) activity.findViewById(R.id.survey_edit_hllega_hora);
                    EditText etMinuto = (EditText) activity.findViewById(R.id.survey_edit_hllega_minutos);
                    EditText etHoraEncuesta = (EditText) activity.findViewById(R.id.survey_edit_hora);
                    String hora=etHora.getText().toString();
                    String minuto=etMinuto.getText().toString();

                    if (hora.isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etHora.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etHora.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etHora.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (minuto.isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etMinuto.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etMinuto.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etMinuto.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if(!validarFecha(hora,minuto)){
                        String textoError = activity.getResources().getString(R.string.survey_text_error_date);
                        etHora.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etHora.setError(textoError);
                        etMinuto.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etMinuto.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etHora.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        etMinuto.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    try {
                        //Formato de hora (hora/minuto)
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm");
                        //Comprobación de la HORA
                        formatoFecha.setLenient(false);
                        Date dateHoraEncuesta = formatoFecha.parse(etHoraEncuesta.getText().toString());
                        Date dateHllega = formatoFecha.parse(hora + ":" + minuto);

                        if(dateHllega.after(dateHoraEncuesta)){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_hllega);
                            etHora.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etHora.setError(textoError);
                            etMinuto.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etMinuto.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etHora.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            etMinuto.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            etHora.setError(null);
                            etMinuto.setError(null);
                        }

                    } catch (ParseException e) {
                        //En el caso de que no se haya recogido por algún motivo la hora de inicio de la encuesta no se comprueba que sea posterior
                        //a la hora de llegada
                        Toast.makeText(null, activity.getResources().getString(R.string.survey_text_error_hora_hllega), Toast.LENGTH_LONG).show();
                    }
                    break;
                case 9:
                    //P9
                    final SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);

                    if (getValorDesplegable(sp_cdiaptod).substring(0,3).equals("000")) {
                        sp_cdiaptod.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    if(activity.findViewById(R.id.survey_layout_cdiaptodotro).getVisibility()==VISIBLE) {
                        EditText etCdiaptod = (EditText) activity.findViewById(R.id.survey_edit_cdiaptodotro);
                        if (etCdiaptod.getText().toString().isEmpty()){
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCdiaptod.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdiaptod.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdiaptod.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 10:
                    //P10
                    final SearchableSpinner sp_numvuepa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_numvuepa);

                    if (getValorDesplegable(sp_numvuepa).substring(0,3).equals("000")) {
                        sp_numvuepa.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    EditText etNumvuepa = (EditText) activity.findViewById(R.id.survey_edit_numvuepa);
                    if (etNumvuepa.getText().toString().isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etNumvuepa.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNumvuepa.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNumvuepa.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 11:
                    //P11
                    RadioGroup rgCdTerm = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdterm);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdterm))){
                        return false;
                    }

                    if (rgCdTerm.getCheckedRadioButtonId() == R.id.survey_radio_cdterm_option2){
                        final SearchableSpinner sp_cdociaar = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdociaar);
                        if(getValorDesplegable(sp_cdociaar).substring(0,3).equals("000")) {
                            sp_cdociaar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                        if(activity.findViewById(R.id.survey_layout_cdociaarotro).getVisibility()==VISIBLE) {
                            EditText etCdociaar = (EditText) activity.findViewById(R.id.survey_edit_cdociaarotro);
                            if (etCdociaar.getText().toString().isEmpty()){
                                String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                                etCdociaar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                                etCdociaar.setError(textoError);
                                return getDialogValueBackError(activity,
                                        activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                        textoError,
                                        activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                            } else {
                                etCdociaar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            }
                        }
                    }

                    break;
                case 12:
                    //P12
                    final SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);

                    if (getValorDesplegable(sp_cdiaptof).substring(0,3).equals("000")) {
                        sp_cdiaptof.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    if (activity.findViewById(R.id.survey_layout_cdiaptofotro).getVisibility() == VISIBLE) {
                        EditText etCdiaptof = (EditText) activity.findViewById(R.id.survey_edit_cdiaptofotro);
                        if (etCdiaptof.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCdiaptof.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdiaptof.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdiaptof.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    break;
                case 13:
                    //P13
                    final SearchableSpinner sp_cdmviajefiltro = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviajefiltro);

                    if (getValorDesplegable(sp_cdmviajefiltro).substring(0,2).equals("00")) {
                        sp_cdmviajefiltro.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    final SearchableSpinner sp_cdmviaje = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviaje);

                    if (getValorDesplegable(sp_cdmviaje).substring(0,2).equals("00")) {
                        sp_cdmviaje.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    break;
                case 14:
                    //P14
                    RadioGroup rgCdidavue = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdidavue);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdidavue))){
                        return false;
                    }

                    if (rgCdidavue.getCheckedRadioButtonId() == R.id.survey_radio_cdidavue_option1 ||
                            rgCdidavue.getCheckedRadioButtonId() == R.id.survey_radio_cdidavue_option2){
                        CheckBox ckCdidavue = (CheckBox) activity.findViewById(R.id.survey_check_cdidavue_option0);
                        EditText etTaus = (EditText) activity.findViewById(R.id.survey_edit_taus);
                        if (!ckCdidavue.isChecked() && stringToInt(etTaus.getText().toString())<1){
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etTaus.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etTaus.setError(textoError);
                            ckCdidavue.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etTaus.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            ckCdidavue.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        }
                    }
                    //P13, hemos indicado vacaciones >7 días (293), P22 “duración del viaje tiene que ser superior a 7;
                    //si en P13 se indica opción 291 o 292, en P22 igual o inferior a 7.
                    SearchableSpinner sp_cdmviaje1 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviaje);
                    //String st_cdmviaje = sp_cdmviaje1.getSelectedItem().toString().substring(0, 3);
                    String st_cdmviaje = getValorDesplegable(sp_cdmviaje1).substring(0,3);
                    EditText etTaus = (EditText) activity.findViewById(R.id.survey_edit_taus);
                    String st_Taus = etTaus.getText().toString();

                    if (st_cdmviaje.equals("293") && stringToInt(st_Taus) <= 7) {
                        String textoError = activity.getResources().getString(R.string.survey_text_m4_error_taus_293);
                        etTaus.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etTaus.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else if ((st_cdmviaje.equals("291") || st_cdmviaje.equals("292")) && stringToInt(st_Taus)>7){
                        String textoError = activity.getResources().getString(R.string.survey_text_m4_error_taus_291_292);
                        etTaus.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etTaus.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }else {
                        etTaus.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 15:
                    //P15
                    RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_npers))){
                        return false;
                    }

                    if (rgNpers.getCheckedRadioButtonId() == R.id.survey_radio_npers_option3){
                        EditText etNpers_especificar = (EditText) activity.findViewById(R.id.survey_edit_npers_especificar);
                        if (stringToInt(etNpers_especificar.getText().toString())<3){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_mas2);
                            etNpers_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etNpers_especificar.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etNpers_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }


                    break;
                case 16:
                    rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
                    EditText etNpers_especificar = (EditText) activity.findViewById(R.id.survey_edit_npers_especificar);
                    EditText etNninos = (EditText) activity.findViewById(R.id.survey_edit_nniños);
                    if (etNninos.getText().toString().isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etNninos.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNninos.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNninos.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    int intEtNninos = stringToInt(etNninos.getText().toString());
                    int checkedId = rgNpers.getCheckedRadioButtonId();
                    int selectedCode = 0;
                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_npers_option1:
                                selectedCode = 1;
                                break;
                            case R.id.survey_radio_npers_option2:
                                selectedCode = 2;
                                break;
                            case R.id.survey_radio_npers_option3:
                                selectedCode = stringToInt(etNpers_especificar.getText().toString());
                                break;
                        }
                    }

                    if (intEtNninos > (selectedCode)){
                        String textoError = activity.getResources().getString(R.string.survey_text_error_nninos);
                        etNninos.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNninos.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNninos.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 17:
                    //P17
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_relacion))){
                        return false;
                    }
                    break;
                case 18:
                    //P18
                    RadioGroup rgCdtreser = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdtreser);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdtreser))){
                        return false;
                    }

                    if (rgCdtreser.getCheckedRadioButtonId() == R.id.survey_radio_cdtreser_dias){
                        EditText etCdtreser_especificar = (EditText) activity.findViewById(R.id.survey_edit_cdtreser_especificar);
                        if (stringToInt(etCdtreser_especificar.getText().toString())<2){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_mas1);
                            etCdtreser_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCdtreser_especificar.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCdtreser_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    break;
                case 19:
                    //P19
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdbillet))){
                        return false;
                    }
                    break;
                case 20:
                    //P20
                    RadioGroup rgNviaje = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nviaje);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_nviaje))){
                        return false;
                    }

                    if (rgNviaje.getCheckedRadioButtonId() == R.id.survey_radio_nviaje_numviajes){
                        EditText etNviaje_especificar = (EditText) activity.findViewById(R.id.survey_edit_nviaje_especificar);
                        if (stringToInt(etNviaje_especificar.getText().toString())<1){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_nviaje_num);
                            etNviaje_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etNviaje_especificar.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etNviaje_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    break;
                case 21:
                    //P21
                    RadioGroup rgVol12mes = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_vol12mes);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_vol12mes))){
                        return false;
                    }

                    if (rgVol12mes.getCheckedRadioButtonId() == R.id.survey_radio_vol12mes_numviajes){
                        EditText etVol12mes_especificar = (EditText) activity.findViewById(R.id.survey_edit_vol12mes_especificar);
                        if (stringToInt(etVol12mes_especificar.getText().toString())<1){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_nviaje_num);
                            etVol12mes_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etVol12mes_especificar.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etVol12mes_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    RadioGroup rgNviaje3 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nviaje);
                    EditText etNviaje = (EditText) activity.findViewById(R.id.survey_edit_nviaje_especificar);
                    int intEtNviaje = stringToInt(etNviaje.getText().toString());
                    int checkedId3 = rgNviaje3.getCheckedRadioButtonId();
                    int selectedCode3 = 0;
                    if (checkedId3 > 0) {
                        switch (checkedId3) {
                            case R.id.survey_radio_nviaje_option0:
                                selectedCode3 = 0;
                                break;
                            case R.id.survey_radio_nviaje_numviajes:
                                selectedCode3 = stringToInt(etNviaje.getText().toString());
                                break;
                        }
                    }

                    int checkedId4 = rgVol12mes.getCheckedRadioButtonId();
                    int selectedCode4 = 0;
                    EditText etVol12mes = (EditText) activity.findViewById(R.id.survey_edit_vol12mes_especificar);
                    if (checkedId4 > 0) {
                        switch (checkedId4) {
                            case R.id.survey_radio_vol12mes_option0:
                                selectedCode4 = 0;
                                break;
                            case R.id.survey_radio_vol12mes_numviajes:
                                selectedCode4 = stringToInt(etVol12mes.getText().toString());
                                break;
                        }
                    }

                    if ((selectedCode4) > (selectedCode3)){
                        String textoError = activity.getResources().getString(R.string.survey_text_error_vol12mes);
                        etVol12mes.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etVol12mes.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etVol12mes.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 22:
                    //P22
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_eleccovid))){
                        return false;
                    }
                    break;
                case 23:
                    //P23
                    RadioGroup rgP44factu = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_p44factu);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_p44factu))){
                        return false;
                    }

                    if (rgP44factu.getCheckedRadioButtonId() == R.id.survey_radio_p44factu_option1){
                        EditText etBulgrupo = (EditText) activity.findViewById(R.id.survey_edit_bulgrupo);
                        if (stringToInt(etBulgrupo.getText().toString())<1){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_bulgrupo);
                            etBulgrupo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etBulgrupo.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etBulgrupo.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    break;
                case 24:
                    //P24
                    RadioGroup rgNperbul = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nperbul);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_nperbul))){
                        return false;
                    }

                    if (rgNperbul.getCheckedRadioButtonId() == R.id.survey_radio_nperbul_option3){
                        EditText etNperbul_especificar = (EditText) activity.findViewById(R.id.survey_edit_nperbul_especificar);
                        if (stringToInt(etNperbul_especificar.getText().toString())<3){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_mas2);
                            etNperbul_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etNperbul_especificar.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etNperbul_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    EditText etNpers= (EditText) activity.findViewById(R.id.survey_edit_npers_especificar);
                    rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
                    EditText etNperbul= (EditText) activity.findViewById(R.id.survey_edit_nperbul_especificar);

                    int checkedId2 = rgNperbul.getCheckedRadioButtonId();
                    int selectedCode2 = 0;
                    int checkedId1 = rgNpers.getCheckedRadioButtonId();
                    int selectedCode1 = 0;
                    if (checkedId1 > 0) {
                        switch (checkedId1) {
                            case R.id.survey_radio_npers_option1:
                                selectedCode1 = 1;
                                break;
                            case R.id.survey_radio_npers_option2:
                                selectedCode1 = 2;
                                break;
                            case R.id.survey_radio_npers_option3:
                                selectedCode1 = stringToInt(etNpers.getText().toString());
                                break;
                        }
                    }

                    if (checkedId2 > 0) {
                        switch (checkedId2) {
                            case R.id.survey_radio_nperbul_option1:
                                selectedCode2 = 1;
                                break;
                            case R.id.survey_radio_nperbul_option2:
                                selectedCode2 = 2;
                                break;
                            case R.id.survey_radio_nperbul_option3:
                                selectedCode2 = stringToInt(etNperbul.getText().toString());
                                break;
                        }
                    }

                    if ((selectedCode2) > (selectedCode1)){
                        String textoError = activity.getResources().getString(R.string.survey_text_error_nperbul);
                        etNperbul.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNperbul.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNperbul.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }


                    break;
                case 25:
                    //P25
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_chekinb))){
                        return false;
                    }
                    break;
                case 26:
                    //P26
                    RadioGroup rgConsume = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_consume);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_consume))){
                        return false;
                    }

                    if (rgConsume.getCheckedRadioButtonId() == R.id.survey_radio_consume_option1){
                        EditText etGas_cons = (EditText) activity.findViewById(R.id.survey_edit_gas_cons);
                        if (stringToInt(etGas_cons.getText().toString())<1){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_Nmodos_otros);
                            etGas_cons.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etGas_cons.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etGas_cons.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 27:
                    //P27
                    RadioGroup rgComprart = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_comprart);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_comprart))){
                        return false;
                    }

                    if (rgComprart.getCheckedRadioButtonId() == R.id.survey_radio_comprart_option1){
                        EditText etGas_com = (EditText) activity.findViewById(R.id.survey_edit_gas_com);
                        if (stringToInt(etGas_com.getText().toString())<1){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_Nmodos_otros);
                            etGas_com.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etGas_com.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etGas_com.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }

                        if(!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_prod1), "0")){
                            return false;
                        }
                    }
                    break;
                case 28:
                    //P28
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdslab))){
                        return false;
                    }
                    break;
                case 29:
                    //P29
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsprof))){
                        return false;
                    }
                    break;
                case 30:
                    //P30
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_estudios))){
                        return false;
                    }
                    break;
                case 31:
                    //P31
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdedad))){
                        return false;
                    }
                    break;
                case 32:
                    //P32
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsexo))){
                        return false;
                    }
                    break;
                case 33:
                    RatingBar rabValorexp = (RatingBar) activity.findViewById(R.id.survey_rating_valorexp);
                    int intValorexp = Math.round(rabValorexp.getRating());
                    if (intValorexp<1 || intValorexp>10) {
                        String textoError = activity.getResources().getString(R.string.survey_text_error_1a10);
                        rabValorexp.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        rabValorexp.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }
                    break;
                case 999:
                    //Siempre que se guarde se comprueba que tenga relleno el numero de vuelo y la puerta de embarque
                    EditText etCodCompVuelo = (EditText) activity.findViewById(R.id.survey_edit_codCompVuelo);
                    EditText etNnumVuelo = (EditText) activity.findViewById(R.id.survey_edit_numVuelo);
                    EditText etPpuertaEmbarque = (EditText) activity.findViewById(R.id.survey_edit_puertaEmbarque);
                    String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);

                    if (etCodCompVuelo.getText().toString().isEmpty()) {
                        etCodCompVuelo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etCodCompVuelo.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etCodCompVuelo.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (etNnumVuelo.getText().toString().isEmpty()) {
                        etNnumVuelo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNnumVuelo.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNnumVuelo.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (etPpuertaEmbarque.getText().toString().isEmpty()) {
                        etPpuertaEmbarque.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etPpuertaEmbarque.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etPpuertaEmbarque.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUECA, String.valueOf(cue.getNumvueca()));
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PUERTA, String.valueOf(cue.getPuerta()));
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HORAFIN, String.valueOf(cue.getHoraFin()));
                    break;
            }
        }
        return true;
    }

    public boolean saveQuestion(int check) {
        boolean activated = true;
        Calendar currentTime = Calendar.getInstance();
        fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        //Asigna la fecha a visualizar
        String hora = sdfTime.format(currentTime.getTime());

        if (activated) {
            switch (check) {
                case 1:
                    //P1
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISNA, cue.getCdpaisna());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISNAOTRO, cue.getCdpaisnaotro());
                    break;
                case 2:
                    //P2
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISRE, cue.getCdpaisre());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISREOTRO, cue.getCdpaisreotro());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCADO, cue.getCdlocado());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRES, cue.getDistres());
                    break;
                case 3:
                    //P3
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VIEN_RE, cue.getVien_re());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACO, cue.getCdlocaco());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACOOTRO, cue.getCdlocacootro());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO, cue.getCdiaptoo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO, cue.getCdiaptoootro());
                    break;
                case 4:
                    //P4
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN, cue.getCdalojin());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN_OTROS, cue.getCdalojin_otros());
                    break;
                case 5:
                    //P5
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODO, cue.getUltimodo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODOOTRO, cue.getUltimodootro());
                    break;
                case 6:
                    //P6
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK, cue.getSitiopark());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PQFUERA, cue.getPqfuera());
                    break;
                case 7:
                    //P7
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES, String.valueOf(cue.getAcomptes()));
                    break;
                case 8:
                    //P8
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGA, cue.getHllega());
                    break;
                case 9:
                    //P9
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD, cue.getCdiaptod());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTODOTRO, cue.getCdiaptodotro());
                    break;
                case 10:
                    //P10
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA, cue.getNumvuepa());
                    break;
                case 11:
                    //P11
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDTERM, cue.getCdterm());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR, cue.getCdociaar());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAROTRO, cue.getCdociaarotro());
                    break;
                case 12:
                    //P12
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF, cue.getCdiaptof());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOFOTRO, cue.getCdiaptofotro());
                    break;
                case 13:
                    //P13
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE, cue.getCdmviaje());
                    break;
                case 14:
                    //P14
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE, cue.getCdidavue());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_TAUS, String.valueOf(cue.getTaus()));
                    break;
                case 15:
                    //P15
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERS, String.valueOf(cue.getNpers()));
                    break;
                case 16:
                    //P16
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS, String.valueOf(cue.getNniños()));
                    break;
                case 17:
                    //P17
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_RELACION, cue.getRelacion());
                    break;
                case 18:
                    //P18
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDTRESER, cue.getCdtreser());
                    break;
                case 19:
                    //P19
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDBILLET, cue.getCdbillet());
                    break;
                case 20:
                    //P20
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE, cue.getNviaje());
                    break;
                case 21:
                    //P21
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VOL12MES, cue.getVol12mes());
                    break;
                case 22:
                    //P22
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ELECCOVID, cue.getEleccovid());
                    break;
                case 23:
                    //P23
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_P44FACTU, cue.getP44factu());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_BULGRUPO, cue.getBulgrupo());
                    break;
                case 24:
                    //P24
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERBUL, cue.getNperbul());
                    break;
                case 25:
                    //P25
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB, String.valueOf(cue.getChekinb()));
                    break;
                case 26:
                    //P26
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CONSUME, cue.getConsume());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_GAS_CONS, String.valueOf(cue.getGas_cons()));
                    break;
                case 27:
                    //P27
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_COMPRART, cue.getComprart());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_GAS_COM, String.valueOf(cue.getGas_com()));
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD1, cue.getProd1());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD2, cue.getProd2());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD3, cue.getProd3());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD4, cue.getProd4());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD5, cue.getProd5());
                    break;
                case 28:
                    //P28
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSLAB, cue.getCdslab());
                    break;
                case 29:
                    //P29
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF, cue.getCdsprof());
                    break;
                case 30:
                    //P30
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS, cue.getEstudios());
                    break;
                case 31:
                    //P31
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD, cue.getCdedad());
                    break;
                case 32:
                    //P32
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO, String.valueOf(cue.getCdsexo()));
                    break;
                case 33:
                    //P33
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VALOREXP, String.valueOf(cue.getValorexp()));
                    break;
            }
        }

        return true;
    }

    private boolean checkDelete(int check) {
        boolean activated = true;

        if (activated) {
            switch (check) {
                case 2:
                    //P2
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISRE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISREOTRO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCADO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRES);
                    break;
                case 3:
                    //P3
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VIEN_RE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACOOTRO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO);
                    break;
                case 4:
                    //P4
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN_OTROS);
                    break;
                case 5:
                    //P5
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODOOTRO);
                    break;
                case 6:
                    //P6
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PQFUERA);
                    break;
                case 7:
                    //P7
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES);
                    break;
                case 8:
                    //P8
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGA);
                    break;
                case 9:
                    //P9
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTODOTRO);
                    break;
                case 10:
                    //P10
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA);
                    break;
                case 11:
                    //P11
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDTERM);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAROTRO);
                    break;
                case 12:
                    //P12
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOFOTRO);
                    break;
                case 13:
                    //P13
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE);
                    break;
                case 14:
                    //P14
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_TAUS);
                    break;
                case 15:
                    //P15
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NPERS);
                    break;
                case 16:
                    //P16
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS);
                    break;
                case 17:
                    //P17
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_RELACION);
                    break;
                case 18:
                    //P18
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDTRESER);
                    break;
                case 19:
                    //P19
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDBILLET);
                    break;
                case 20:
                    //P20
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE);
                    break;
                case 21:
                    //P21
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VOL12MES);
                    break;
                case 22:
                    //P22
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ELECCOVID);
                    break;
                case 23:
                    //P23
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_P44FACTU);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_BULGRUPO);
                    break;
                case 24:
                    //P24
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NPERBUL);
                    break;
                case 25:
                    //P25
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB);
                    break;
                case 26:
                    //P26
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CONSUME);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_GAS_CONS);
                    break;
                case 27:
                    //P27
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_COMPRART);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_GAS_COM);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD1);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD2);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD3);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD4);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD5);
                    break;
                case 28:
                    //P28
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSLAB);
                    break;
                case 29:
                    //P29
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF);
                    break;
                case 30:
                    //P30
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS);
                    break;
                case 31:
                    //P31
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD);
                    break;
                case 32:
                    //P32
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO);
                    break;
                case 33:
                    //P33
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VALOREXP);
                    break;
            }
        }
        return true;
    }

    public void hideQuestions() {

        //P1
        LinearLayout cdpaisna = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisna);
        cdpaisna.setVisibility(GONE);

        //P2
        LinearLayout cdpaisre = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisre);
        cdpaisre.setVisibility(GONE);

        //P3
        LinearLayout cdcambio = (LinearLayout) activity.findViewById(R.id.survey_layout_viene_re_cdlocaco_diaptoo);
        cdcambio.setVisibility(GONE);

        //P4
        LinearLayout cdalojin = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojin_crucero);
        cdalojin.setVisibility(GONE);

        //P5
        LinearLayout ultimomodo = (LinearLayout) activity.findViewById(R.id.survey_m4_layout_ultimod);
        ultimomodo.setVisibility(GONE);

        //P6
        LinearLayout sitiopark = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
        sitiopark.setVisibility(GONE);

        //P7
        LinearLayout acomptes = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
        acomptes.setVisibility(GONE);

        //P8
        LinearLayout hllega = (LinearLayout) activity.findViewById(R.id.survey_layout_hllega);
        hllega.setVisibility(GONE);

        //P9
        LinearLayout diaptod = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptod);
        diaptod.setVisibility(GONE);

        //P10
        LinearLayout numvuepa = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
        numvuepa.setVisibility(GONE);

        //P11
        LinearLayout cdterm_cdociaar = (LinearLayout) activity.findViewById(R.id.survey_layout_cdterm);
        cdterm_cdociaar.setVisibility(GONE);

        //P12
        LinearLayout cdiaptof = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptof);
        cdiaptof.setVisibility(GONE);

        //P13
        LinearLayout cdmviaje = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
        cdmviaje.setVisibility(GONE);

        //P14
        LinearLayout cdidavue_taus = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
        cdidavue_taus.setVisibility(GONE);

        //P15
        LinearLayout npers = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
        npers.setVisibility(GONE);

        //P16
        LinearLayout nniños = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
        nniños.setVisibility(GONE);

        //P17
        LinearLayout relacion = (LinearLayout) activity.findViewById(R.id.survey_layout_relacion);
        relacion.setVisibility(GONE);

        //P18
        LinearLayout cdtreser = (LinearLayout) activity.findViewById(R.id.survey_layout_cdtreser);
        cdtreser.setVisibility(GONE);

        //P19
        LinearLayout cdbillet = (LinearLayout) activity.findViewById(R.id.survey_layout_cdbillet);
        cdbillet.setVisibility(GONE);

        //P20
        LinearLayout nviaje = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
        nviaje.setVisibility(GONE);

        //P21
        LinearLayout vol12mes = (LinearLayout) activity.findViewById(R.id.survey_layout_vol12mes);
        vol12mes.setVisibility(GONE);

        //P22
        LinearLayout eleccovid = (LinearLayout) activity.findViewById(R.id.survey_layout_eleccovid);
        eleccovid.setVisibility(GONE);

        //P23
        LinearLayout p44factu_bulgrupo = (LinearLayout) activity.findViewById(R.id.survey_layout_p44factu);
        p44factu_bulgrupo.setVisibility(GONE);

        //P24
        LinearLayout nperbul = (LinearLayout) activity.findViewById(R.id.survey_layout_nperbul);
        nperbul.setVisibility(GONE);

        //P25
        LinearLayout checkinb = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
        checkinb.setVisibility(GONE);

        //P26
        LinearLayout consume_gans_cons = (LinearLayout) activity.findViewById(R.id.survey_layout_consume);
        consume_gans_cons.setVisibility(GONE);

        //P27
        LinearLayout comprart_gas_com_prod = (LinearLayout) activity.findViewById(R.id.survey_layout_comprart);
        comprart_gas_com_prod.setVisibility(GONE);

        //P28
        LinearLayout cdslab = (LinearLayout) activity.findViewById(R.id.survey_layout_cdslab);
        cdslab.setVisibility(GONE);

        //P29
        LinearLayout cdsprof = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
        cdsprof.setVisibility(GONE);

        //P30
        LinearLayout estudios = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
        estudios.setVisibility(GONE);

        //P31
        LinearLayout cdedad = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
        cdedad.setVisibility(GONE);

        //P32
        LinearLayout cdsexo = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
        cdsexo.setVisibility(GONE);

        //P33
        LinearLayout valorexp = (LinearLayout) activity.findViewById(R.id.survey_layout_valorexp);
        valorexp.setVisibility(GONE);

    }

    public int showNextQuestion(int show) {

        Button previous = (Button) activity.findViewById(R.id.survey_button_previous);
        Button save = (Button) activity.findViewById(R.id.survey_button_save);
        Button next = (Button) activity.findViewById(R.id.survey_button_next);

        boolean activated = true;

        int checkedId;
        int checkedId2;

        switch (show) {
            case 1:
                //B1
                show = showQuestion(2);
                break;
            case 2:
                //P2
                show = showQuestion(3);
                break;
            case 3:
                //P3
                generarTituloCdalojin();
                if (activated) {
                    RadioGroup rgVienere = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re_diaptoo);
                    checkedId = rgVienere.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_viene_re:
                                // Si el pasajero viene de su residencia habitual tiene que ser viaje de IDA: si P9=1 P22=1.
                                activity.findViewById(R.id.survey_radio_cdidavue_option2).setVisibility(GONE);
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_cdlocaco:
                                // Si el pasajero viene de su residencia habitual tiene que ser viaje de IDA: si P9=1 P22=1.
                                activity.findViewById(R.id.survey_radio_cdidavue_option2).setVisibility(VISIBLE);
                                show = showQuestion(4); //>P4
                                break;
                            case R.id.survey_radio_cdiaptoo:
                                show = showQuestion(8); //>P8
                                break;
                        }
                    } else {
                        show = showQuestion(4); //>P4
                    }
                } else {
                    show = showQuestion(4);
                }
                break;
            case 4:
                //P4
                generarTituloCdalojin();
                show = showQuestion(5);
                break;
            case 5:
                //P5
                if (activated) {
                    RadioGroup rgUltimodo_umodo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_m4_ultimodo_1modo);
                    checkedId = rgUltimodo_umodo.getCheckedRadioButtonId();
                    if ((checkedId == R.id.survey_radio_m4_ultimodo_1modo_option4) || (checkedId == R.id.survey_radio_m4_ultimodo_1modo_option5)) {
                        show = showQuestion(6);//>P6
                    } else{
                        show = showQuestion(7);//>P7
                    }

                } else {
                    show = showQuestion(6);//>P6
                }
                break;
            case 6:
                //P6
                show = showQuestion(7);
                break;
            case 7:
                //P7
                show = showQuestion(8);
                break;
            case 8:
                //P8
                show = showQuestion(9);
                break;
            case 9:
                //P9
                generarTituloCdterm();
                show = showQuestion(10);
                break;
            case 10:
                //P10
                show = showQuestion(11);
                break;
            case 11:
                //P11
                generarTituloCdterm();
                if (activated) {
                    RadioGroup rgCdterm = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdterm);
                    checkedId = rgCdterm.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdterm_option1:
                                show = showQuestion(13); //>P13
                                break;
                            case R.id.survey_radio_cdterm_option2:
                                show = showQuestion(12); //>P12
                                break;
                        }
                    } else {
                        show = showQuestion(12); //>P12
                    }
                } else {
                    show = showQuestion(12); //>P12
                }
                break;
            case 12:
                //P12
                show = showQuestion(13);
                break;
            case 13:
                //P13
                show = showQuestion(14);
                break;
            case 14:
                //P14
                show = showQuestion(15);
                break;
            case 15:
                //P15
                if (activated) {
                    RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
                    checkedId = rgNpers.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_npers_option1:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_npers_option2:
                                show = showQuestion(16); //>P16
                                break;
                            case R.id.survey_radio_npers_option3:
                                show = showQuestion(16); //>P16
                                break;
                        }
                    } else {
                        show = showQuestion(16); //>P16
                    }
                } else {
                    show = showQuestion(16);
                }
                break;
            case 16:
                //P16
                show = showQuestion(17);
                break;
            case 17:
                //P17
                show = showQuestion(18);
                break;
            case 18:
                //P18
                show = showQuestion(19);
                break;
            case 19:
                //P19
                show = showQuestion(20);
                break;
            case 20:
                //P20
                if (activated) {
                    RadioGroup rgNviaje = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nviaje);
                    checkedId = rgNviaje.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_nviaje_option0:
                                show = showQuestion(22); //>P22
                                break;
                            case R.id.survey_radio_nviaje_numviajes:
                                show = showQuestion(21); //>P21
                                break;
                        }
                    } else {
                        show = showQuestion(21); //>P21
                    }
                } else {
                    show = showQuestion(21);
                }
                break;
            case 21:
                //P21
                show = showQuestion(22);
                break;
            case 22:
                //P22
                show = showQuestion(23);
                break;
            case 23:
                //P23
                if (activated) {
                    RadioGroup rgP44factu = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_p44factu);
                    checkedId = rgP44factu.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_p44factu_option1:
                                show = showQuestion(24); //>P24
                                break;
                            case R.id.survey_radio_p44factu_option2:
                                show = showQuestion(25); //>P25
                                break;
                        }
                    } else {
                        show = showQuestion(24); //>P24
                    }
                } else {
                    show = showQuestion(24); //>P24
                }
                break;
            case 24:
                //P24
                show = showQuestion(25);
                break;
            case 25:
                //P25
                show = showQuestion(26);
                break;
            case 26:
                //P26
                show = showQuestion(27);
                break;
            case 27:
                //P27
                show = showQuestion(28);
                break;
            case 28:
                //P28
                if (activated) {
                    RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
                    checkedId = rgCdslab.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdslab_option1:
                                show = showQuestion(29); //>P29
                                break;
                            default:
                                show = showQuestion(30); //>P30
                                break;
                        }
                    } else {
                        show = showQuestion(29); //>P29
                        break;
                    }
                } else {
                    show = showQuestion(29); //>P29
                    break;
                }
                break;
            case 29:
                //P29
                show = showQuestion(30);
                break;
            case 30:
                //P30
                show = showQuestion(31);
                break;
            case 31:
                //P31
                show = showQuestion(32);
                break;
            case 32:
                //P32
                show = showQuestion(33);
                break;
            case 33:
                //P33
                //FIN
                break;

        }

        return show;
    }

    public CuePasajeros fillQuest(CuePasajeros quest) {
        int selectedCode = -1;
        int checkedId = -1;

        //CABECERA
        EditText etCodCompVuelo = (EditText) activity.findViewById(R.id.survey_edit_codCompVuelo);
        EditText etNnumVuelo = (EditText) activity.findViewById(R.id.survey_edit_numVuelo);
        EditText etPuertaEmbarque = (EditText) activity.findViewById(R.id.survey_edit_puertaEmbarque);
        String stPuertaEmbarque = etPuertaEmbarque.getText().toString();
        String stNumvueca = etCodCompVuelo.getText().toString()+"-"+etNnumVuelo.getText().toString();

        if (!stPuertaEmbarque.isEmpty()) {
            quest.setPuerta(stPuertaEmbarque);
        } else {
            quest.setPuerta("-1");
        }

        if (!etCodCompVuelo.getText().toString().isEmpty() || !etNnumVuelo.getText().toString().isEmpty()){
            quest.setNumvueca(stNumvueca);
        }

        //Establece la fecha actual
        Calendar currentTime = Calendar.getInstance();
        Date fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_TIME);
        //Asigna la fecha a visualizar
        quest.setHoraFin(sdfDate.format(fechaActual));



        //P1
        SearchableSpinner sp_cdpaisna = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);
        //String textSpCdpaisna = sp_cdpaisna.getSelectedItem().toString().substring(0, 3);
        String textSpCdpaisna = getValorDesplegable(sp_cdpaisna).substring(0,3);
        if (!textSpCdpaisna.contains("000")) {
            quest.setCdpaisna(textSpCdpaisna);
            if (textSpCdpaisna.contains("999")){
                EditText et_cdpaisnaotro = (EditText) activity.findViewById(R.id.survey_edit_cdpaisnaotro);
                quest.setCdpaisnaotro(et_cdpaisnaotro.getText().toString());
            } else {
                quest.setCdpaisnaotro("-1");
            }
        } else {
            quest.setCdpaisna("-1");
        }
        //P2
        SearchableSpinner sp_cdpaisre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);
        //String textSpCdpaisre = sp_cdpaisre.getSelectedItem().toString().substring(0, 3);
        String textSpCdpaisre = getValorDesplegable(sp_cdpaisre).substring(0,3);
        if (!textSpCdpaisre.contains("000")) {
            quest.setCdpaisre(textSpCdpaisre);
            if (textSpCdpaisre.contains("999")){
                EditText et_cdpaisreotro = (EditText) activity.findViewById(R.id.survey_edit_cdpaisreotro);
                quest.setCdpaisreotro(et_cdpaisreotro.getText().toString());
            } else {
                quest.setCdpaisreotro("-1");
            }
        } else {
            quest.setCdpaisre("-1");
        }

        SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
        //String textSpCdlocado = sp_cdlocado.getSelectedItem().toString().substring(0, 5);
        String textSpCdlocado = getValorDesplegable(sp_cdlocado).substring(0,5);
        if (!textSpCdlocado.contains("00000")) {
            quest.setCdlocado(textSpCdlocado);
        } else {
            quest.setCdlocado("-1");
        }
        SearchableSpinner sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
        String textSpDistres_area = getValorDesplegable(sp_distres_area).substring(0,3);
        if (compruebaListaPaises1y2(textSpCdpaisre)>0) {
            quest.setDistres(textSpDistres_area);
        } else {
            quest.setDistres("-1");
        }
        //P3
        RadioGroup rgViene_re = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re_diaptoo);

        SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
        String textSpCdlocaco = getValorDesplegable(sp_cdlocaco).substring(0,4);


        selectedCode = -1;
        checkedId = rgViene_re.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_viene_re:
                    selectedCode = 1;
                    quest.setCdlocaco("-1");
                    quest.setDistracce("-1");
                    quest.setDistracceotro("-1");
                    break;
                case R.id.survey_radio_cdlocaco:
                    if(!textSpCdlocaco.contains("0000")){
                        quest.setCdlocaco(textSpCdlocaco);
                        if (textSpCdlocaco.contains("9999")){
                            EditText et_cdlocacootro = (EditText) activity.findViewById(R.id.survey_edit_cdlocacootro);
                            quest.setCdlocacootro(et_cdlocacootro.getText().toString());
                        } else {
                            quest.setCdlocacootro("-1");
                        }
                    } else {
                        quest.setCdlocaco("-1");
                    }
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdiaptoo:
                    SearchableSpinner sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);
                    String textSpCdiaptoo = getValorDesplegable(sp_cdiaptoo).substring(0,3);
                    if(!textSpCdiaptoo.contains("000")){
                        quest.setCdiaptoo(textSpCdiaptoo);
                        if (textSpCdiaptoo.contains("ZZZ")){
                            EditText et_cdiaptoootro = (EditText) activity.findViewById(R.id.survey_edit_cdiaptoootro_m4);
                            quest.setCdiaptoootro(et_cdiaptoootro.getText().toString());
                        } else {
                            quest.setCdiaptoootro("-1");
                        }
                    } else {
                        quest.setCdiaptoo("-1");
                    }
                    selectedCode = 99000;
                    quest.setCdlocaco("99000");
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setVien_re(String.valueOf(selectedCode));

        //P4
        RadioGroup rgCdalojin = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojin_crucero);
        EditText etCdalojin_otros = (EditText) activity.findViewById(R.id.survey_edit_cdalojin_otros);

        selectedCode = -1;
        quest.setCdalojin_otros("-1");
        checkedId = rgCdalojin.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdalojin_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdalojin_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdalojin_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_cdalojin_option7:
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_cdalojin_option8:
                    selectedCode = 8;
                    break;
                case R.id.survey_radio_cdalojin_option10:
                    selectedCode = 10;
                    break;
                case R.id.survey_radio_cdalojin_option9:
                    selectedCode = 9;
                    quest.setCdalojin_otros(etCdalojin_otros.getText().toString());
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdalojin(String.valueOf(selectedCode));

        //P5
        RadioGroup rgUltimodo_umodo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_m4_ultimodo_1modo);

        selectedCode = -1;
        checkedId = rgUltimodo_umodo.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_m4_ultimodo_1modo_option1:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option2:
                    selectedCode = 25;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option3:
                    selectedCode = 24;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option4:
                    selectedCode = 22;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option5:
                    selectedCode = 23;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option6:
                    selectedCode = 32;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option7:
                    selectedCode = 31;
                    break;
                case R.id.survey_radio_m4_ultimodo_1modo_option8:
                    EditText etUltimodo_otros= (EditText) activity.findViewById(R.id.survey_edit_m4_ultimodo_otros);
                    quest.setUltimodootro(etUltimodo_otros.getText().toString());
                    selectedCode = 91;
                    break;
                default:
                    selectedCode = 0;
                    break;
            }
        }

        quest.setUltimodo(String.valueOf(selectedCode));

        //P6
        RadioGroup rgSitioPark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);

        selectedCode = -1;
        checkedId = rgSitioPark.getCheckedRadioButtonId();
        quest.setPqfuera("-1");

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_sitiopark_option1_m4:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_sitiopark_option4:
                    EditText etPqfuera = (EditText) activity.findViewById(R.id.survey_edit_pqfuera);
                    quest.setPqfuera(etPqfuera.getText().toString());
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_sitiopark_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_sitiopark_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_sitiopark_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setSitiopark(String.valueOf(selectedCode));

        //P7
        RadioGroup rgAcomptes = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_acomptes);

        selectedCode = -1;
        checkedId = rgAcomptes.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_acomptes_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_acomptes_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_acomptes_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_acomptes_option3:
                    EditText acomptes_especificar = (EditText) activity.findViewById(R.id.survey_edit_acomptes_especificar);
                    selectedCode = Integer.valueOf(stringToInt(acomptes_especificar.getText().toString()));
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setAcomptes(selectedCode);

        //P8
        EditText hllega_hora = (EditText) activity.findViewById(R.id.survey_edit_hllega_hora);
        EditText hllega_minutos = (EditText) activity.findViewById(R.id.survey_edit_hllega_minutos);
        quest.setHllega(hllega_hora.getText().toString()+":"+hllega_minutos.getText().toString());

        //P9
        SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);
        //String textSpCdiaptod = sp_cdiaptod.getSelectedItem().toString().substring(0,3);
        String textSpCdiaptod = getValorDesplegable(sp_cdiaptod).substring(0,3);
        if(!textSpCdiaptod.contains("000")){
            quest.setCdiaptod(textSpCdiaptod);
            if (textSpCdiaptod.contains("ZZZ")){
                EditText et_cdiaptodotro = (EditText) activity.findViewById(R.id.survey_edit_cdiaptodotro);
                quest.setCdiaptodotro(et_cdiaptodotro.getText().toString());
            } else {
                quest.setCdiaptodotro("-1");
            }
        } else {
            quest.setCdiaptod("-1");
        }

        //P10
        SearchableSpinner sp_numvuepa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_numvuepa);
        //String textSpNumvuepa = sp_numvuepa.getSelectedItem().toString().substring(0,3);
        String textSpNumvuepa = getValorDesplegable(sp_numvuepa).substring(0,3);
        EditText numvuepa = (EditText) activity.findViewById(R.id.survey_edit_numvuepa);
        if(!textSpNumvuepa.contains("000")){
            quest.setNumvuepa(textSpNumvuepa+"-"+numvuepa.getText().toString());
        } else {
            quest.setNumvuepa("-1");
        }

        //P11b
        RadioGroup rgCdterm = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdterm);

        selectedCode = -1;
        checkedId = rgCdterm.getCheckedRadioButtonId();
        quest.setCdociaar("-1");

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdterm_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdterm_option2:
                    SearchableSpinner sp_cdociaar = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdociaar);
                    //String textSpCdociaar = sp_cdociaar.getSelectedItem().toString().substring(0,3);
                    String textSpCdociaar = getValorDesplegable(sp_cdociaar).substring(0,3);
                    if(!textSpCdociaar.contains("000")){
                        quest.setCdociaar(textSpCdociaar);
                        if (textSpCdociaar.contains("999")){
                            EditText et_cdociaarotro = (EditText) activity.findViewById(R.id.survey_edit_cdociaarotro);
                            quest.setCdociaarotro(et_cdociaarotro.getText().toString());
                        } else {
                            quest.setCdociaarotro("-1");
                        }
                    }
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }
        quest.setCdterm(String.valueOf(selectedCode));

        //P12
        SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);
        //String textSpCdiaptof = sp_cdiaptof.getSelectedItem().toString().substring(0,3);
        String textSpCdiaptof = getValorDesplegable(sp_cdiaptof).substring(0,3);
        if(!textSpCdiaptof.contains("000")){
            quest.setCdiaptof(textSpCdiaptof);
            if (textSpCdiaptof.contains("ZZZ")){
                EditText et_cdiaptofotro = (EditText) activity.findViewById(R.id.survey_edit_cdiaptofotro);
                quest.setCdiaptofotro(et_cdiaptofotro.getText().toString());
            } else {
                quest.setCdiaptofotro("-1");
            }
        } else {
            quest.setCdiaptof("-1");
        }

        //P13
        SearchableSpinner sp_cdmviaje = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviaje);
        String textSpCdmviaje = getValorDesplegable(sp_cdmviaje).substring(0,3);
        //String textSpCdmviaje = sp_cdmviaje.getSelectedItem().toString().substring(0,3);
        if(!textSpCdmviaje.contains("000")){
            quest.setCdmviaje(textSpCdmviaje);
        } else {
            quest.setCdmviaje("-1");
        }

        //P14
        RadioGroup rgCdidavue = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdidavue);
        CheckBox ckCdidavue = (CheckBox) activity.findViewById(R.id.survey_check_cdidavue_option0);

        selectedCode = -1;
        quest.setTaus(stringToInt("-1"));
        checkedId = rgCdidavue.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdidavue_option1:
                    if (ckCdidavue.isChecked()){
                        quest.setTaus(stringToInt("0"));
                    } else {
                        EditText etTaus = (EditText) activity.findViewById(R.id.survey_edit_taus);
                        quest.setTaus(stringToInt(etTaus.getText().toString()));
                    }
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdidavue_option2:
                    if (ckCdidavue.isChecked()){
                        quest.setTaus(stringToInt("0"));
                    } else {
                        EditText etTaus = (EditText) activity.findViewById(R.id.survey_edit_taus);
                        quest.setTaus(stringToInt(etTaus.getText().toString()));
                    }
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }
        quest.setCdidavue(String.valueOf(selectedCode));

        //P15
        RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
        EditText etNpers_especificar = (EditText) activity.findViewById(R.id.survey_edit_npers_especificar);

        selectedCode = -1;
        checkedId = rgNpers.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_npers_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_npers_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_npers_option3:
                    selectedCode = stringToInt(etNpers_especificar.getText().toString());
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setNpers(selectedCode);

        //P16
        EditText etNniños= (EditText) activity.findViewById(R.id.survey_edit_nniños);
        quest.setNniños(stringToInt(etNniños.getText().toString()));

        //P17
        RadioGroup rgRelacion = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_relacion);
        selectedCode = -1;

        checkedId = rgRelacion.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_relacion_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_relacion_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_relacion_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_relacion_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_relacion_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setRelacion(String.valueOf(selectedCode));

        //P18
        RadioGroup rgCdtreser = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdtreser);
        EditText etCdtreser_especificar = (EditText) activity.findViewById(R.id.survey_edit_cdtreser_especificar);

        selectedCode = -1;
        checkedId = rgCdtreser.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdtreser_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_cdtreser_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdtreser_dias:
                    selectedCode = stringToInt(etCdtreser_especificar.getText().toString());
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setCdtreser(String.valueOf(selectedCode));

        //P19
        RadioGroup rgCdbillet = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdbillet);

        selectedCode = -1;
        checkedId = rgCdbillet.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdbillet_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdbillet_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_cdbillet_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_cdbillet_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_cdbillet_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdbillet(String.valueOf(selectedCode));

        //P20
        RadioGroup rgNviaje = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nviaje);
        EditText etNviaje_especificar = (EditText) activity.findViewById(R.id.survey_edit_nviaje_especificar);

        selectedCode = -1;
        checkedId = rgNviaje.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_nviaje_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_nviaje_numviajes:
                    selectedCode = stringToInt(etNviaje_especificar.getText().toString());
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setNviaje(String.valueOf(selectedCode));

        //P21
        RadioGroup rgVol12mes = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_vol12mes);
        EditText etVol12mes_especificar = (EditText) activity.findViewById(R.id.survey_edit_vol12mes_especificar);

        selectedCode = -1;
        checkedId = rgVol12mes.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_vol12mes_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_vol12mes_numviajes:
                    selectedCode = stringToInt(etVol12mes_especificar.getText().toString());
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setVol12mes(String.valueOf(selectedCode));

        //P22
        RadioGroup rgEleccovid = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_eleccovid);

        selectedCode = -1;
        quest.setEleccovid("-1");
        checkedId = rgEleccovid.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_eleccovid_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_eleccovid_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_eleccovid_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_eleccovid_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_eleccovid_option5:
                    selectedCode = 5;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setEleccovid(String.valueOf(selectedCode));

        //P23
        RadioGroup rgP44factu = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_p44factu);
        EditText etBulgrupo = (EditText) activity.findViewById(R.id.survey_edit_bulgrupo);

        selectedCode = -1;
        quest.setBulgrupo("-1");
        checkedId = rgP44factu.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_p44factu_option1:
                    quest.setBulgrupo(etBulgrupo.getText().toString());
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_p44factu_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setP44factu(String.valueOf(selectedCode));

        //P24
        RadioGroup rgNperbul = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nperbul);
        EditText etNperbul_especificar = (EditText) activity.findViewById(R.id.survey_edit_nperbul_especificar);

        selectedCode = -1;
        checkedId = rgNperbul.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_nperbul_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_nperbul_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_nperbul_option3:
                    selectedCode = stringToInt(etNperbul_especificar.getText().toString());
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setNperbul(String.valueOf(selectedCode));

        //P25
        RadioGroup rgChekinb = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_chekinb);
        selectedCode = -1;
        checkedId = rgChekinb.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_chekinb_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_chekinb_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_chekinb_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setChekinb(selectedCode);

        //P26
        RadioGroup rgConsume = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_consume);
        EditText etGas_cons = (EditText) activity.findViewById(R.id.survey_edit_gas_cons);

        selectedCode = -1;
        quest.setGas_cons(stringToInt("-1"));
        checkedId = rgConsume.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_consume_option1:
                    quest.setGas_cons(stringToInt(etGas_cons.getText().toString()));
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_consume_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setConsume(String.valueOf(selectedCode));

        //P30
        RadioGroup rgComprart = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_comprart);
        EditText etGas_com = (EditText) activity.findViewById(R.id.survey_edit_gas_com);

        selectedCode = -1;
        quest.setGas_com(stringToInt("-1"));
        quest.setProd1("-1");
        quest.setProd2("-1");
        quest.setProd3("-1");
        quest.setProd4("-1");
        quest.setProd5("-1");
        checkedId = rgComprart.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_comprart_option1:
                    quest.setGas_com(stringToInt(etGas_com.getText().toString()));
                    SearchableSpinner sp_prod1= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod1);
                    //String textSpProd1 = sp_prod1.getSelectedItem().toString().substring(0,2);
                    String textSpProd1 = getValorDesplegable(sp_prod1).substring(0,2);
                    if(!textSpProd1.contains("00")){
                        quest.setProd1(textSpProd1);
                    } else {
                        quest.setProd1("-1");
                    }

                    SearchableSpinner sp_prod2= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod2);
                    //String textSpProd2 = sp_prod2.getSelectedItem().toString().substring(0,2);
                    String textSpProd2 = getValorDesplegable(sp_prod2).substring(0,2);
                    if(!textSpProd2.contains("00")){
                        quest.setProd2(textSpProd2);
                    } else {
                        quest.setProd2("-1");
                    }

                    SearchableSpinner sp_prod3= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod3);
                    //String textSpProd3 = sp_prod3.getSelectedItem().toString().substring(0,2);
                    String textSpProd3 = getValorDesplegable(sp_prod3).substring(0,2);
                    if(!textSpProd3.contains("00")){
                        quest.setProd3(textSpProd3);
                    } else {
                        quest.setProd3("-1");
                    }

                    SearchableSpinner sp_prod4= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod4);
                    //String textSpProd4 = sp_prod4.getSelectedItem().toString().substring(0,2);
                    String textSpProd4 = getValorDesplegable(sp_prod4).substring(0,2);
                    if(!textSpProd4.contains("00")){
                        quest.setProd4(textSpProd4);
                    } else {
                        quest.setProd4("-1");
                    }

                    SearchableSpinner sp_prod5= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod5);
                    //String textSpProd5 = sp_prod5.getSelectedItem().toString().substring(0,2);
                    String textSpProd5 = getValorDesplegable(sp_prod5).substring(0,2);
                    if(!textSpProd5.contains("00")){
                        quest.setProd5(textSpProd5);
                    } else {
                        quest.setProd5("-1");
                    }
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_comprart_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setComprart(String.valueOf(selectedCode));

        //P28
        RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
        selectedCode = -1;
        checkedId = rgCdslab.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdslab_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdslab_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdslab_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_cdslab_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_cdslab_option6:
                    selectedCode = 6;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdslab(String.valueOf(selectedCode));

        //P29
        RadioGroup rgCdsprof = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsprof);
        selectedCode = -1;
        checkedId = rgCdsprof.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdsprof_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_cdsprof_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdsprof_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdsprof_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_cdsprof_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_cdsprof_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdsprof(String.valueOf(selectedCode));

        //P30
        RadioGroup rgEstudios = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_estudios);
        selectedCode = -1;
        checkedId = rgEstudios.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_estudios_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_estudios_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_estudios_option5:
                    selectedCode = 5;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setEstudios(String.valueOf(selectedCode));

        //P31
        RadioGroup rgCdedad = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedad);

        selectedCode = -1;
        checkedId = rgCdedad.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdedad_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdedad_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdedad_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_cdedad_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_cdedad_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_cdedad_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_cdedad_option7:
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_cdedad_option8:
                    selectedCode = 8;
                    break;
                case R.id.survey_radio_cdedad_option9:
                    selectedCode = 9;
                    break;
                case R.id.survey_radio_cdedad_option10:
                    selectedCode = 10;
                    break;
                case R.id.survey_radio_cdedad_option11:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_cdedad_option12:
                    selectedCode = 12;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdedad(String.valueOf(selectedCode));

        //P32
        RadioGroup rgCdsexo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsexo);
        selectedCode = -1;
        checkedId = rgCdsexo.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdsexo_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdsexo_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }
        quest.setCdsexo(selectedCode);

        //P33
        RatingBar rabValorexp = (RatingBar) activity.findViewById(R.id.survey_rating_valorexp);
        quest.setValorexp(Math.round(rabValorexp.getRating()));

        return quest;
    }

    private boolean guardaDB(String nombreBD, String valor) {
        DBHelper conn = new DBHelper(activity.getApplicationContext());
        SQLiteDatabase db = conn.getWritableDatabase();

        System.out.println("UPDATE " + Contracts.TABLE_CUEPASAJEROS  + " SET " + nombreBD + " = '" + valor + "', " + Contracts.COLUMN_CUEPASAJEROS_PREGUNTA + " = " + pregunta + ", " + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + " = 0 WHERE " + Contracts.COLUMN_CUEPASAJEROS_IDEN + " = " + idCue);
        db.execSQL("UPDATE " + Contracts.TABLE_CUEPASAJEROS + " SET " + nombreBD + " = '" + valor + "', " + Contracts.COLUMN_CUEPASAJEROS_PREGUNTA + " = " + pregunta + ", " + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + " = 0 WHERE " + Contracts.COLUMN_CUEPASAJEROS_IDEN + " = " + idCue);

        return true;
    }


    private boolean borraDB(String nombreBD) {
        DBHelper conn = new DBHelper(activity.getApplicationContext());
        SQLiteDatabase db = conn.getWritableDatabase();

        db.execSQL("UPDATE " + Contracts.TABLE_CUEPASAJEROS + " SET " + nombreBD + " = NULL, " + Contracts.COLUMN_CUEPASAJEROS_PREGUNTA + " = " + pregunta + ", " + Contracts.COLUMN_CUEPASAJEROS_ENVIADO + " = 0 WHERE " + Contracts.COLUMN_CUEPASAJEROS_IDEN + " = " + idCue);

        return true;
    }

    public void setCue(CuePasajeros cue){

        this.cue = cue;
    }

    private void setPreguntaAnterior(int pregunta){

        preguntaAnterior = pregunta;
    }

    public int getPreguntaAnterior(){

        return preguntaAnterior;
    }

    private List<mListString> getDiccionario(String tabla, String campoIden, String campoCod, String campoValor, String campoOrden) {
        List<mListString> getDiccionario = new ArrayList<mListString>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + campoIden + ", " + campoCod + "||'. '|| " + campoValor +
                " FROM " + tabla + " AS T1 ORDER BY " + campoOrden , parametros);

        while (cursor.moveToNext()) {
            getDiccionario.add(new mListString(cursor.getInt(0), cursor.getString(1)));
        }

        cursor.close();

        return getDiccionario;
    }

    private List<mListString> getDiccionario(String tabla, String campoIden, String campoCod, String campoValor, String campoOrden, String filtro) {
        List<mListString> getDiccionario = new ArrayList<mListString>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + campoIden + ", " + campoCod + "||'. '|| " + campoValor +
                " FROM " + tabla + " AS T1" +
                " WHERE " + filtro + " ORDER BY " + campoOrden , parametros);

        while (cursor.moveToNext()) {
            getDiccionario.add(new mListString(cursor.getInt(0), cursor.getString(1)));
        }

        cursor.close();

        return getDiccionario;
    }

    public boolean getDialogValueBackAlert(Context context, String txtTitle, String txtMessage, String txtBtnNext, String txtBtnReview )
    {
        final Handler handler = new Handler()
        {

            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(txtTitle);
        alert.setMessage(txtMessage);
        alert.setPositiveButton(txtBtnNext, new
                DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        resultValue = true;
                        handler.sendMessage(handler.obtainMessage());
                    }
                });
        alert.setNegativeButton(txtBtnReview, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;
    }

    public boolean getDialogValueBackError(Context context, String txtTitle, String txtMessage, String txtBtnReview )
    {
        final Handler handler = new Handler()
        {

            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(txtTitle);
        alert.setMessage(txtMessage);
        alert.setNegativeButton(txtBtnReview, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;
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

    private void generarTituloCdalojin() {
        SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
        //String textSpCdlocaco = sp_cdlocaco.getSelectedItem().toString();
        String textSpCdlocaco = getValorDesplegable(sp_cdlocaco);
        String textoCdalojin = activity.getString(R.string.survey_m4_text_cdalojin);
        TextView tvCdalojin = (TextView) activity.findViewById(R.id.survey_text_cdalojin_crucero);
        if (textSpCdlocaco.length()>6)
            textSpCdlocaco = textSpCdlocaco.substring(6, textSpCdlocaco.length());
        tvCdalojin.setText(textoCdalojin.replace("%1$s", textSpCdlocaco));
    }

    private void generarTituloCdterm() {
        SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);
        //String textSpCdiaptod = sp_cdiaptod.getSelectedItem().toString();
        String textSpCdiaptod = getValorDesplegable(sp_cdiaptod);
        String textoCdterm = activity.getString(R.string.survey_m4_text_cdterm);
        if (textSpCdiaptod.length()>4)
            textSpCdiaptod = textSpCdiaptod.substring(4, textSpCdiaptod.length());
        TextView tvCdterm= (TextView) activity.findViewById(R.id.survey_text_cdterm_m4);
        tvCdterm.setText(textoCdterm.replace("%1$s", textSpCdiaptod));
    }

    private int compruebaListaPaises1y2(String codigoPais){
        int conteo = 0;
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS Conteo "+
                " FROM " + Contracts.TABLE_TIPOPAISES1Y2 + " AS T1 " +
                " WHERE iden <> 0 AND "+Contracts.COLUMN_TIPOPAISES1Y2_CODIGOPAIS + " = '"+codigoPais+"'" , parametros);

        while (cursor.moveToNext()) {
            conteo = cursor.getInt(0);
        }

        cursor.close();

        return conteo;
    }

    private String replicate (String origen, String relleno, int max){
        String texto;

        texto = origen;

        while(texto.length() < max){
            texto = relleno + texto;
        };

        return texto;
    }

    public String replicateTime (String origen, String relleno, int max, int tipo){
        String texto;

        if(tipo == 2){
            switch(origen) {
                case "0":
                    origen = "00";
                    break;
                case "1":
                    origen = "15";
                    break;
                case "2":
                    origen = "30";
                    break;
                case "3":
                    origen = "45";
                    break;
            }
        }

        texto = origen;

        while(texto.length() < max){
            texto = relleno + texto;
        };

        return texto;
    }

    private void blanquearEditText (View miView){
        EditText miEditText = (EditText) miView;
        miEditText.setText("");
    }

    private boolean requeridoSearchableSpinner(View miView, String codigoNoResponde) {
        SearchableSpinner miSearchableSp = (SearchableSpinner) miView;

        if (miSearchableSp.getSelectedItem().toString().substring(0,codigoNoResponde.length()).equals(codigoNoResponde)) {
            miSearchableSp.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
            return getDialogValueBackError(activity,
                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                    activity.getResources().getString(R.string.survey_text_selectOption),
                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
        }
        return true;
    }

    private boolean requeridoRadioGroup(View miView) {
        RadioGroup miRadioGroup = (RadioGroup) miView;

        if (miRadioGroup.getCheckedRadioButtonId()==-1) {
            miRadioGroup.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
            return getDialogValueBackError(activity,
                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                    activity.getResources().getString(R.string.survey_text_selectOption),
                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
        } else {
            miRadioGroup.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
        }
        return true;
    }

    private boolean requeridoRadioGroupNModos(View miView) {
        RadioGroup miRadioGroup = (RadioGroup) miView;

        if (miRadioGroup.getCheckedRadioButtonId()==-1) {
            miRadioGroup.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
            return getDialogValueBackError(activity,
                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                    activity.getResources().getString(R.string.survey_text_selectOption),
                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
        } else {
            miRadioGroup.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
        }
        return true;
    }

    public boolean validarFecha(String hora, String minuto) {
        boolean correcto = false;

        try {
            //Formato de hora (hora/minuto)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm");
            //Comprobación de la HORA
            formatoFecha.setLenient(false);
            formatoFecha.parse(hora + ":" + minuto);
            correcto = true;
        } catch (ParseException e) {
            //Si la hora no es correcta, pasará por aquí
            correcto = false;
        }

        return correcto;
    }

    public String getValorDesplegable(SearchableSpinner miSpinner) {
        String miValor = null;

        mListString mlsEmpresa = (mListString) miSpinner.getSelectedItem();
        miValor = mlsEmpresa.getNilai1();

        return  miValor;
    }
}
