package es.testadistica.www.aenaemma2022.utilidades;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static es.testadistica.www.aenaemma2022.utilidades.Contracts.TABLE_TIPOAEROPUERTOS;

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

public class ModeloPasajeros1 extends Form {

    private int preguntaAnterior = 1;
    private int idCue;
    private int idAeropuerto;
    private int finCue = 41;
    private boolean resultValue;

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_TIME = "HH:mm";
    private Date fechaActual = Calendar.getInstance().getTime();
    private SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_SHORT);
    private SimpleDateFormat sdfTime = new SimpleDateFormat(DATE_FORMAT_TIME);

    public ModeloPasajeros1(Activity surveyAct, int pregunta, DBHelper conn) {

        super(surveyAct, pregunta, conn);
    }

    @Override
    public int getLayoutId() {
        return R.layout.form_modelpasajeros1;
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
        activity.findViewById(R.id.survey_rl_numVuelo).setVisibility(VISIBLE);
        activity.findViewById(R.id.survey_rl_puertaEmbarque).setVisibility(VISIBLE);
        activity.findViewById(R.id.survey_radio_cdsprof_option6).setVisibility(GONE);
        activity.findViewById(R.id.survey_radio_cdsprof_option0).setVisibility(GONE);
        activity.findViewById(R.id.survey_radio_cdsprof_option9).setVisibility(VISIBLE);
        switch (idAeropuerto) {
            case 1:
                //Madrid
                //P10
                activity.findViewById(R.id.survey_radio_cdalojin_option10).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdalojin_crucero).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdalojin_crucero_m1).setVisibility(VISIBLE);
                break;
            case 2:
                //Barcelona
                //P2
                activity.findViewById(R.id.survey_text_distres).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_distres_bcn).setVisibility(VISIBLE);
                //P7
                activity.findViewById(R.id.survey_text_cdsinope).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdsinope_bcn).setVisibility(VISIBLE);
                //P9
                activity.findViewById(R.id.survey_text_distracce).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_distracce_bcn).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_text_viene_re).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_viene_re_bcn).setVisibility(VISIBLE);
                //P10
                activity.findViewById(R.id.survey_text_cdalojin_crucero).setVisibility(GONE);
                activity.findViewById(R.id.survey_text_cdalojin_crucero_m1).setVisibility(VISIBLE);
                break;
        }
    }

    private void iniciarSpinners() {
        ArrayList<mListString> paisesAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES,"iden", "codigo","descripcion", "descripcion"));
        ArrayList<mListString> paises1y2Adapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES1Y2,"iden", "codigo","zonas || ', ' || descripcion", "zonas || ', ' || descripcion"));
        ArrayList<mListString> provinciasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> provinciasP9Adapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo", "codigo NOT IN ('07', '35', '38', '51', '52')"));
        ArrayList<mListString> municipiosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> motivoViajeAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMOTIVOVIAJE,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> productosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPRODUCTOS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> motivoViajefiltroAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMOTIVOVIAJEFILTRO,"iden", "codigo","motivo", "codigo"));
        String filtroAeropuerto = " ciudad like '%MAD%' ";
        switch (idAeropuerto){
            case 2:
                //Barcelona
                filtroAeropuerto = " ciudad like '%BCN%' ";
                break;
        }
        ArrayList<mListString> distritosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPODISTRITOS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto));
        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 1:
                //Madrid
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_MADOLEADA+" = 1";
                break;
            case 2:
                //Barcelona
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_BCNOLEADA+" = 1";
                break;
        }
        ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));
        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 1:
                //Madrid
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL+" = 1";
                break;
            case 2:
                //Barcelona
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL+" = 1";
                break;
        }
        ArrayList<mListString> tipoAeropuertosPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));
        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 1:
                //Madrid
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_MADOLEADA+"=1 ";

                break;
            case 2:
                //Barcelona
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_BCNOLEADA+"=1 ";

                break;

        }
        ArrayList<mListString> companiasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto));
        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 1:
                //Madrid
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_MADAEREA+"=1 ";
                break;
            case 2:
                //Barcelona
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_BCNAEREA+"=1 ";
                break;
        }
        ArrayList<mListString> companiasPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));

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

        SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        sp_distres.setAdapter(distritosAdapter, 1, 1, activity.getString(R.string.spinner_distrito_title), activity.getString(R.string.spinner_close));

        sp_distres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_distres.setBackgroundResource(android.R.drawable.btn_dropdown);
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

        //P4
        //Asigna los valores del desplegable de paises
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

        //P5
        //Asigna los valores del desplegable de companias
        SearchableSpinner sp_ciaantes;
        sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);
        sp_ciaantes.setAdapter(companiasAdapter, 1, 1, activity.getString(R.string.spinner_compania_title), activity.getString(R.string.spinner_close));
        /*sp_ciaantes.setAdapter(companiasAdapter);
        sp_ciaantes.setTitle(activity.getString(R.string.spinner_compania_title));
        sp_ciaantes.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_ciaantes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_ciaantes.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //P9
        SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);
        sp_cdlocaco_prov.setAdapter(provinciasP9Adapter, 1, 1, activity.getString(R.string.spinner_provincia_title), activity.getString(R.string.spinner_close));
        /*sp_cdlocaco_prov.setAdapter(provinciasAdapter);
        sp_cdlocaco_prov.setTitle(activity.getString(R.string.spinner_provincia_title));
        sp_cdlocaco_prov.setPositiveButton(activity.getString(R.string.spinner_close));*/

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
        sp_cdlocaco.setAdapter(municipiosAdapter, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
        /*sp_cdlocaco.setAdapter(municipiosAdapter);
        sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
        sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_cdlocaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocaco.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_distracce = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distracce);
        sp_distracce.setAdapter(distritosAdapter, 1, 1, activity.getString(R.string.spinner_distrito_title), activity.getString(R.string.spinner_close));
        /*sp_distracce.setAdapter(distritosAdapter);
        sp_distracce.setTitle(activity.getString(R.string.spinner_distrito_title));
        sp_distracce.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_distracce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_distracce.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P18
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

        //P19
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

        //P20
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

        //P21
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

        //P22
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
        //P35
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
                SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
                EditText et_distresotro = (EditText) activity.findViewById(R.id.survey_edit_distresotro);
                SearchableSpinner sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
                sp_cdlocado_prov.setSelection(0);
                sp_cdlocado_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
                sp_cdlocado.setSelection(0);
                sp_cdlocado.setBackgroundResource(android.R.drawable.btn_dropdown);
                sp_distres.setSelection(0);
                sp_distres.setBackgroundResource(android.R.drawable.btn_dropdown);
                blanquearEditText(et_distresotro);
                sp_distres_area.setSelection(0);
                sp_distres_area.setBackgroundResource(android.R.drawable.btn_dropdown);

                //String texto = sp_cdpaisre.getSelectedItem().toString().substring(0,3);
                String texto = getValorDesplegable(sp_cdpaisre).substring(0,3);
                if (texto.equals("724")){ //España
                    activity.findViewById(R.id.survey_layout_cdlocado_esp).setVisibility(VISIBLE);
                    activity.findViewById(R.id.survey_layout_cdlocado_no_esp).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdlocado_esp).setVisibility(GONE);
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distresotro));
                    if (compruebaListaPaises1y2(texto) > 0){ //Si el pais es uno de la lista 1 y 2 se habilita para introducir el área / región
                        String filtro = " iden = 0 OR "+Contracts.COLUMN_TIPOPAISES1Y2_CODIGOPAIS+" = '"+texto+"'";
                        /*ArrayAdapter<String> paises1y2Adapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOPAISES1Y2,"iden", "codigo","zonas || ', ' || descripcion", "zonas || ', ' || descripcion", filtro));
                        paises1y2Adapter.setDropDownViewResource(R.layout.selection_spinner_item);
                        sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
                        sp_distres_area.setAdapter(paises1y2Adapter);
                        sp_distres_area.setTitle(activity.getString(R.string.spinner_pais1y2_title));
                        sp_distres_area.setPositiveButton(activity.getString(R.string.spinner_close));*/
                        ArrayList<mListString> paises1y2Adapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES1Y2,"iden", "codigo","zonas || ', ' || descripcion", "zonas || ', ' || descripcion", filtro));
                        //paises1y2Adapter.setDropDownViewResource(R.layout.selection_spinner_item);
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
                } */else {
                    filtro = " (iden > -1 ";
                }

                if (!filtro.contains(")")) {
                    filtro = filtro.substring(0, filtro.length()-1);
                    filtro = filtro + ")";
                }



                final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
                ArrayList<mListString> municipioAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                /*ArrayAdapter<String> municipioAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                municipioAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
                sp_cdlocado.setAdapter(municipioAdapter);
                sp_cdlocado.setTitle(activity.getString(R.string.spinner_municipio_title));
                sp_cdlocado.setPositiveButton(activity.getString(R.string.spinner_close));*/
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
                //String texto = sp_cdlocado.getSelectedItem().toString().substring(0,5);
                String texto = getValorDesplegable(sp_cdlocado).substring(0,5);
                String filtroAeropuerto = "28079";

                switch (idAeropuerto) {
                    case 2:
                        //Barcelona
                        filtroAeropuerto = "08019";
                        break;
                }

                if (!texto.equals(filtroAeropuerto)){
                    activity.findViewById(R.id.survey_layout_distres).setVisibility(GONE);
                    activity.findViewById(R.id.survey_layout_distresotro).setVisibility(GONE);
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distresotro));
                } else {
                    activity.findViewById(R.id.survey_layout_distres).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);

        sp_distres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_distres.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_distres.getSelectedItem().toString().substring(0,2);
                String texto = getValorDesplegable(sp_distres).substring(0,2);

                if (!texto.equals("OT")){
                    activity.findViewById(R.id.survey_layout_distresotro).setVisibility(GONE);
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distresotro));
                } else {
                    activity.findViewById(R.id.survey_layout_distresotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //P4
        final SearchableSpinner sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);

        sp_cdiaptoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_cdiaptoo).substring(0,3);
                String textoCiudad = compCiudad(texto);

                String filtroAeropuerto1 = " iden IS NOT NULL "; //Para que salgan todos
                switch (idAeropuerto){
                    case 1:
                        //Madrid
                        if(textoCiudad.equals("")){
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                        }else{
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')";
                            }
                        }

                        break;
                    case 2:
                        //Barcelona
                        if(textoCiudad.equals("")){
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                        }else{
                            if (texto.equals("000")||(texto.equals("ZZZ"))) {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')";
                            }
                        }

                        break;

                }


                final SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);
                ArrayList<mListString> tipoAeropuertosPpalAdapter = new ArrayList<mListString>(getDiccionario(TABLE_TIPOAEROPUERTOS, "iden", "codigo", "descripcion", "descripcion", filtroAeropuerto1));
                sp_cdiaptof.setAdapter(tipoAeropuertosPpalAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));
                String filtroAeropuerto2 = " iden IS NOT NULL "; //Para que salgan todos
                switch (idAeropuerto){
                    case 1:
                        //Madrid
                        if(textoCiudad.equals("")) {
                            if (texto.equals("000") || (texto.equals("ZZZ"))) {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADOLEADA + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                        }else{
                            if (texto.equals("000") || (texto.equals("ZZZ"))) {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADOLEADA + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')";
                            }
                        }
                        break;
                    case 2:
                        //Barcelona
                        if(textoCiudad.equals("")) {
                            if (texto.equals("000") || (texto.equals("ZZZ"))) {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNOLEADA + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "')";
                            }
                        }else{
                            if (texto.equals("000") || (texto.equals("ZZZ"))) {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 ";
                            } else {
                                filtroAeropuerto2 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNOLEADA + " = 1 AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')";
                            }
                        }
                        break;
                }

                final SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);
                ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(TABLE_TIPOAEROPUERTOS, "iden", "codigo", "descripcion", "descripcion", filtroAeropuerto2));
                sp_cdiaptod.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

                if (!texto.equals("ZZZ")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdiaptoootro));
                    activity.findViewById(R.id.survey_layout_cdiaptoootro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdiaptoootro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P5
        final SearchableSpinner sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);

        sp_ciaantes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_ciaantes.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_ciaantes.getSelectedItem().toString().substring(0,3);
                String texto = getValorDesplegable(sp_ciaantes).substring(0,3);

                if (!texto.equals("999")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_ciaantesotro));
                    activity.findViewById(R.id.survey_layout_ciaantesotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_ciaantesotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P9 Filtro municipios
        final SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);

        sp_cdlocaco_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String texto = getValorDesplegable(sp_cdlocaco_prov).substring(0,2);
                String filtro = " provincia IN (";
                /*
                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                }  else if (id > 9 && id <=57){
                    filtro =  filtro +"'00','"+id+"','99',";
                }*/
                /*else if (id == 53){
                    filtro =  filtro +"'99',";
                } */
                if (!texto.equals("00")) {
                    filtro =  filtro +"'00','"+texto+"','99',";
                } else {
                    filtro = " (iden > -1 ";
                }

                if (!filtro.contains(")")) {
                    filtro = filtro.substring(0, filtro.length()-1);
                    filtro = filtro + ")";
                }

                /*final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
                ArrayAdapter<String> municipioAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                municipioAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
                sp_cdlocaco.setAdapter(municipioAdapter);
                sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
                sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));*/

                final SearchableSpinner sp_cdlocaco= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
                ArrayList<mListString> municipioAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                sp_cdlocaco.setAdapter(municipioAdapter, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
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
                //String texto = sp_cdlocaco.getSelectedItem().toString().substring(0,5);
                String texto = getValorDesplegable(sp_cdlocaco).substring(0,5);
                String filtroAeropuerto = "28079";

                switch (idAeropuerto) {
                    case 2:
                        //Barcelona
                        filtroAeropuerto = "08019";
                        break;
                }

                if (!texto.equals(filtroAeropuerto)){
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(GONE);
                    activity.findViewById(R.id.survey_layout_distracceotro).setVisibility(GONE);
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distracceotro));
                } else {
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_distracce = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distracce);

        sp_distracce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_distracce.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_distracce.getSelectedItem().toString().substring(0,2);
                String texto = getValorDesplegable(sp_distracce).substring(0,2);

                if (!texto.equals("OT")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distracceotro));
                    activity.findViewById(R.id.survey_layout_distracceotro).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_distracceotro).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //P18
        final SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);

        sp_cdiaptod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdiaptod.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto1 = getValorDesplegable(sp_cdiaptod).substring(0, 3);

                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_cdiaptoo).substring(0, 3);
                String textoCP = comprobarCP(texto1);
                String textoCiudad = compCiudad(texto1);
                String textoCiudad2 = compCiudad(texto);

                String filtroAeropuerto1 = " iden IS NOT NULL "; //Para que salgan todos
                switch (idAeropuerto) {
                    case 1:
                        //Madrid
                            /*if (texto1.equals("000")||(texto1.equals("ZZZ"))) {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_FUEPRINCIPAL+" = 1 ";
                            } else {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_FUEPRINCIPAL+" = 1 AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto+"') AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto1+"')";
                            }*/
                        filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_MADPRINCIPAL + " = 1 ";
                        if(textoCiudad2.equals("")) {
                            if (textoCiudad.equals("")) {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') ";
                                    }
                                }
                            } else {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";

                                    }
                                }
                            }
                        }else{
                            if (textoCiudad.equals("")) {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                }
                            } else {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                }
                            }
                        }

                        break;
                    case 2:
                        //Barcelona
                            /*if (texto1.equals("000")||(texto1.equals("ZZZ"))) {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_FUEPRINCIPAL+" = 1 ";
                            } else {
                                filtroAeropuerto1 = " "+Contracts.COLUMN_TIPOAEROPUERTOS_FUEPRINCIPAL+" = 1 AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto+"') AND "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO+" NOT IN ('" + texto1+"')";
                            }*/
                        filtroAeropuerto1 = " " + Contracts.COLUMN_TIPOAEROPUERTOS_BCNPRINCIPAL + " = 1 ";
                        if(textoCiudad2.equals("")) {
                            if (textoCiudad.equals("")) {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') ";
                                    }
                                }
                            } else {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "')  ";

                                    }
                                }
                            }
                        }else{
                            if (textoCiudad.equals("")) {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                }
                            } else {
                                if (textoCP.equals("724")) {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                } else {
                                    if (!(texto.equals("000") || (texto.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                    if (!(texto1.equals("000") || (texto1.equals("ZZZ")))) {
                                        filtroAeropuerto1 = filtroAeropuerto1 + " AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " NOT IN ('" + texto1 + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " NOT IN ('724') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad + "') AND " + Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD + " NOT IN ('" + textoCiudad2 + "')  ";
                                    }
                                }
                            }
                        }

                        break;
                }

                final SearchableSpinner sp_cdiaptof = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptof);
                ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(TABLE_TIPOAEROPUERTOS, "iden", "codigo", "descripcion", "descripcion", filtroAeropuerto1));
                sp_cdiaptof.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));


                if (!texto1.equals("ZZZ")) {
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

        //P19
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

        //P20
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

        //P21
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

        //P22
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



    }

    private void condicionesRadioButton() {
        //P3
        final RadioGroup rgCdcambio = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdcambio);
        rgCdcambio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdcambio.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P6
        final RadioGroup rgConexfac = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_conexfac);
        rgConexfac.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgConexfac.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P7
        final RadioGroup rgCdsinope = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsinope);
        rgCdsinope.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsinope.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P8
        final RadioGroup rgCdalojen = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojen);
        rgCdalojen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdalojen.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P9
        RadioGroup rgViene_re = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re);
        rgViene_re.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgViene_re.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_viene_re_option2:
                        activity.findViewById(R.id.survey_layout_no_viene_re).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_no_viene_re).setVisibility(GONE);
                        break;
                }
            }
        });
        //P10
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

        //P11
        RadioGroup rgNmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
        rgNmodos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgNmodos.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_nmodos_option3:
                        activity.findViewById(R.id.survey_layout_nmodos_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_nmodos_otros).setVisibility(GONE);
                        break;
                }
            }
        });

        //P12
        RadioGroup rgUmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo);
        RadioGroup rg1modos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_1modo);
        RadioGroup rg2modos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_2modo);
        rgUmodos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgUmodos.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
                switch(i)
                {
                    case R.id.survey_radio_ultimodo_umodo_option13:
                        activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        if (rg1modos.getCheckedRadioButtonId()==R.id.survey_radio_ultimodo_1modo_option13 ||
                                rg2modos.getCheckedRadioButtonId()==R.id.survey_radio_ultimodo_2modo_option13 ){
                            activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(VISIBLE);
                        } else {
                            activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        });

        rg1modos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rg1modos.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
                switch(i)
                {
                    case R.id.survey_radio_ultimodo_1modo_option13:
                        activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        if (rgUmodos.getCheckedRadioButtonId()==R.id.survey_radio_ultimodo_umodo_option13 ||
                                rg2modos.getCheckedRadioButtonId()==R.id.survey_radio_ultimodo_2modo_option13 ){
                            activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(VISIBLE);
                        } else {
                            activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        });

        rg2modos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rg2modos.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
                switch(i)
                {
                    case R.id.survey_radio_ultimodo_2modo_option13:
                        activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        if (rg1modos.getCheckedRadioButtonId()==R.id.survey_radio_ultimodo_1modo_option13 ||
                                rgUmodos.getCheckedRadioButtonId()==R.id.survey_radio_ultimodo_umodo_option13 ){
                            activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(VISIBLE);
                        } else {
                            activity.findViewById(R.id.survey_edit_ultimodo_otros).setVisibility(View.INVISIBLE);
                        }
                        break;
                }
            }
        });

        //P13
        RadioGroup rgVtc= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_vtc);
        rgVtc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgVtc.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_vtc_option9:
                        activity.findViewById(R.id.survey_layout_vtc_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_vtc_otros).setVisibility(GONE);
                        break;
                }
            }
        });
        //P14
        RadioGroup rgSitiopark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);
        rgSitiopark.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgSitiopark.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_sitiopark_option4:
                        activity.findViewById(R.id.survey_layout_pqfuera).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_layout_sitioparkotro).setVisibility(GONE);
                        break;
                    case R.id.survey_radio_sitiopark_option9:
                        activity.findViewById(R.id.survey_layout_sitioparkotro).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_layout_pqfuera).setVisibility(GONE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_pqfuera).setVisibility(GONE);
                        activity.findViewById(R.id.survey_layout_sitioparkotro).setVisibility(GONE);
                        break;
                }
            }
        });
        //P15
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

        //P16
        final RadioGroup rgBustermi = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_bustermi);
        rgBustermi.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgBustermi.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P20
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
        //P23
        RadioGroup rgCdidavue = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdidavue);
        rgCdidavue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCdidavue.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_cdidavue_option1:
                        activity.findViewById(R.id.survey_layout_taus).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_text_cdidavue_a).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_text_cdidavue_b).setVisibility(GONE);
                        break;
                    case R.id.survey_radio_cdidavue_option2:
                        activity.findViewById(R.id.survey_layout_taus).setVisibility(VISIBLE);
                        activity.findViewById(R.id.survey_text_cdidavue_a).setVisibility(GONE);
                        activity.findViewById(R.id.survey_text_cdidavue_b).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_taus).setVisibility(GONE);
                        activity.findViewById(R.id.survey_text_cdidavue_a).setVisibility(GONE);
                        activity.findViewById(R.id.survey_text_cdidavue_b).setVisibility(GONE);
                        break;
                }
            }
        });
        //P24
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
        //P26
        final RadioGroup rgRelacion = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_relacion);
        rgRelacion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgRelacion.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P27
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
        //P28
        final RadioGroup rgCdbillet = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdbillet);
        rgCdbillet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdbillet.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P29
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
        //P30
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
        //P31
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
        //P32
        final RadioGroup rgDropoff = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_dropoff);
        rgDropoff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgDropoff.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P33
        final RadioGroup rgChekinb = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_chekinb);
        rgChekinb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgChekinb.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P34
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
        //P35
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
        //P36
        final RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
        rgCdslab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P37
        final RadioGroup rgCdsprof = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsprof);
        rgCdsprof.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsprof.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P38
        final RadioGroup rgEstudios = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_estudios);
        rgEstudios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgEstudios.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P39
        final RadioGroup rgCdedad = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedad);
        rgCdedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdedad.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P40
        final RadioGroup rgCdsexo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsexo);
        rgCdsexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
    }

    private void condicionesChecks() {
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
                LinearLayout p3 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdcambio);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p3.setVisibility(VISIBLE);
                break;
            case 4:
                //P4
                LinearLayout p4 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptoo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p4.setVisibility(VISIBLE);
                break;
            case 5:
                //P5
                LinearLayout p5 = (LinearLayout) activity.findViewById(R.id.survey_layout_ciaantes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p5.setVisibility(VISIBLE);
                break;
            case 6:
                //P6
                LinearLayout p6 = (LinearLayout) activity.findViewById(R.id.survey_layout_conexfac);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p6.setVisibility(VISIBLE);
                break;
            case 7:
                //P7
                LinearLayout p7 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsinope);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p7.setVisibility(VISIBLE);
                break;
            case 8:
                //P8
                LinearLayout p8 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojen);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p8.setVisibility(VISIBLE);
                break;
            case 9:
                //P9
                LinearLayout p9 = (LinearLayout) activity.findViewById(R.id.survey_layout_viene_re);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p9.setVisibility(VISIBLE);
                break;
            case 10:
                //P10
                LinearLayout p10 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojin_crucero);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p10.setVisibility(VISIBLE);
                break;
            case 11:
                //P11
                LinearLayout p11 = (LinearLayout) activity.findViewById(R.id.survey_layout_nmodos);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p11.setVisibility(VISIBLE);
                break;
            case 12:
                //P12
                LinearLayout p12 = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p12.setVisibility(VISIBLE);
                break;
            case 13:
                //P13
                LinearLayout p13 = (LinearLayout) activity.findViewById(R.id.survey_layout_vtc);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p13.setVisibility(VISIBLE);
                break;
            case 14:
                //P14
                LinearLayout p14 = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p14.setVisibility(VISIBLE);
                break;
            case 15:
                //P15
                LinearLayout p15 = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p15.setVisibility(VISIBLE);
                break;
            case 16:
                //P16
                LinearLayout p16 = (LinearLayout) activity.findViewById(R.id.survey_layout_bustermi);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p16.setVisibility(VISIBLE);
                break;
            case 17:
                //P17
                LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_hllega);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p17.setVisibility(VISIBLE);
                break;
            case 18:
                //P18
                LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptod);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p18.setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p19.setVisibility(VISIBLE);
                break;
            case 20:
                //P20
                LinearLayout p20 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdterm);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p20.setVisibility(VISIBLE);
                break;
            case 21:
                //P21
                LinearLayout p21 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p21.setVisibility(VISIBLE);
                break;
            case 22:
                //P22
                LinearLayout p22 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p22.setVisibility(VISIBLE);
                break;
            case 23:
                //P23
                LinearLayout p23 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p23.setVisibility(VISIBLE);
                break;
            case 24:
                //P24
                LinearLayout p24 = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p24.setVisibility(VISIBLE);
                break;
            case 25:
                //P25
                LinearLayout p25 = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p25.setVisibility(VISIBLE);
                break;
            case 26:
                //P26
                LinearLayout p26 = (LinearLayout) activity.findViewById(R.id.survey_layout_relacion);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p26.setVisibility(VISIBLE);
                break;
            case 27:
                //P27
                LinearLayout p27 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdtreser);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p27.setVisibility(VISIBLE);
                break;
            case 28:
                //P28
                LinearLayout p28 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdbillet);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p28.setVisibility(VISIBLE);
                break;
            case 29:
                //P29
                LinearLayout p29 = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p29.setVisibility(VISIBLE);
                break;
            case 30:
                //P30
                LinearLayout p30  = (LinearLayout) activity.findViewById(R.id.survey_layout_p44factu);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p30.setVisibility(VISIBLE);
                break;
            case 31:
                //P31
                LinearLayout p31 = (LinearLayout) activity.findViewById(R.id.survey_layout_nperbul);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p31.setVisibility(VISIBLE);
                break;
            case 32:
                //P32
                LinearLayout p32 = (LinearLayout) activity.findViewById(R.id.survey_layout_dropoff);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p32.setVisibility(VISIBLE);
                break;
            case 33:
                //P33
                LinearLayout p33 = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p33.setVisibility(VISIBLE);
                break;
            case 34:
                //P34
                LinearLayout p34 = (LinearLayout) activity.findViewById(R.id.survey_layout_consume);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p34.setVisibility(VISIBLE);
                break;
            case 35:
                //P35
                LinearLayout p35 = (LinearLayout) activity.findViewById(R.id.survey_layout_comprart);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p35.setVisibility(VISIBLE);
                break;
            case 36:
                //P36
                LinearLayout p36 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdslab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p36.setVisibility(VISIBLE);
                break;
            case 37:
                //P37
                LinearLayout p37 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p37.setVisibility(VISIBLE);
                break;
            case 38:
                //P38
                LinearLayout p38 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p38.setVisibility(VISIBLE);
                break;
            case 39:
                //P39
                LinearLayout p39 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p39.setVisibility(VISIBLE);
                break;
            case 40:
                //P40
                LinearLayout p40 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p40.setVisibility(VISIBLE);
                break;
            case 41:
                //P41
                LinearLayout p41 = (LinearLayout) activity.findViewById(R.id.survey_layout_valorexp);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(GONE);
                p41.setVisibility(VISIBLE);
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
                    //P2
                    /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdpaisre), "000")) {
                        return false;
                    }*/

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

                    if (activity.findViewById(R.id.survey_layout_cdlocado_esp).getVisibility() == VISIBLE) {
                        /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdlocado), "00000")) {
                            return false;
                        }*/
                        final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

                        if (getValorDesplegable(sp_cdlocado).substring(0,5).equals("00000")) {
                            sp_cdlocado.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                        if (activity.findViewById(R.id.survey_layout_distres).getVisibility() == VISIBLE) {
                            /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_distres), "00")) {
                                return false;
                            }*/
                            final SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);

                            if (getValorDesplegable(sp_distres).substring(0,2).equals("00")) {
                                sp_distres.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                                return getDialogValueBackError(activity,
                                        activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                        activity.getResources().getString(R.string.survey_text_selectOption),
                                        activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                            }
                        }
                        if (activity.findViewById(R.id.survey_layout_distresotro).getVisibility() == VISIBLE) {
                            EditText etDistresotro = (EditText) activity.findViewById(R.id.survey_edit_distresotro);
                            if (etDistresotro.getText().toString().isEmpty()) {
                                String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                                etDistresotro.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                                etDistresotro.setError(textoError);
                                return getDialogValueBackError(activity,
                                        activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                        textoError,
                                        activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                            } else {
                                etDistresotro.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            }
                        }
                    }

                    if (activity.findViewById(R.id.survey_layout_cdlocado_no_esp).getVisibility() == VISIBLE) {
                        /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_distres_area), "000")) {
                            return false;
                        }*/
                        final SearchableSpinner distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);

                        if (getValorDesplegable(distres_area).substring(0,3).equals("000")) {
                            distres_area.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }
                    break;
                case 3:
                    //P3
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdcambio))) {
                        return false;
                    }
                    break;
                case 4:
                    //P4
                    /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdiaptoo), "000")) {
                        return false;
                    }*/
                    final SearchableSpinner cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);

                    if (getValorDesplegable(cdiaptoo).substring(0,3).equals("000")) {
                        cdiaptoo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    if (activity.findViewById(R.id.survey_layout_cdiaptoootro).getVisibility() == VISIBLE) {
                        EditText etCdiaptoo = (EditText) activity.findViewById(R.id.survey_edit_cdiaptoootro);
                        if (etCdiaptoo.getText().toString().isEmpty()) {
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
                case 5:
                    //P5
                    /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_ciaantes), "000")) {
                        return false;
                    }*/
                    final SearchableSpinner ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);

                    if (getValorDesplegable(ciaantes).substring(0,3).equals("000")) {
                        ciaantes.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    if (activity.findViewById(R.id.survey_layout_ciaantesotro).getVisibility() == VISIBLE) {
                        EditText etCiaantes = (EditText) activity.findViewById(R.id.survey_edit_ciaantesotro);
                        if (etCiaantes.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etCiaantes.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etCiaantes.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etCiaantes.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 6:
                    //P6
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_conexfac))) {
                        return false;
                    }

                    break;
                case 7:
                    //P7
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsinope))) {
                        return false;
                    }

                    break;
                case 8:
                    //P8
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdalojen))) {
                        return false;
                    }

                    break;
                case 9:
                    //P9
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_viene_re))) {
                        return false;
                    }
                    if (activity.findViewById(R.id.survey_layout_no_viene_re).getVisibility() == VISIBLE) {
                        /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdlocaco), "00000")) {
                            return false;
                        }*/
                        final SearchableSpinner cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);

                        if (getValorDesplegable(cdlocaco).substring(0,5).equals("00000")) {
                            cdlocaco.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                        if (activity.findViewById(R.id.survey_layout_distracce).getVisibility() == VISIBLE) {
                            /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_distracce), "00")) {
                                return false;
                            }*/
                            final SearchableSpinner distracce = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distracce);

                            if (getValorDesplegable(distracce).substring(0,2).equals("00")) {
                                cdlocaco.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                                return getDialogValueBackError(activity,
                                        activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                        activity.getResources().getString(R.string.survey_text_selectOption),
                                        activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                            }
                        }
                        if (activity.findViewById(R.id.survey_layout_distracceotro).getVisibility() == VISIBLE) {
                            EditText etDistracceotro = (EditText) activity.findViewById(R.id.survey_edit_distracceotro);
                            if (etDistracceotro.getText().toString().isEmpty()) {
                                String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                                etDistracceotro.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                                etDistracceotro.setError(textoError);
                                return getDialogValueBackError(activity,
                                        activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                        textoError,
                                        activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                            } else {
                                etDistracceotro.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            }
                        }

                    }
                    final SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
                    sp_cdlocado_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
                    String texto_prov = getValorDesplegable(sp_cdlocado_prov).substring(0,2);
                    final SearchableSpinner sp_cdpaisre_pais = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);
                    sp_cdpaisre_pais.setBackgroundResource(android.R.drawable.btn_dropdown);
                    String texto_pais = getValorDesplegable(sp_cdpaisre_pais).substring(0,3);
                    RadioGroup rgVienere = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re);
                    if (rgVienere.getCheckedRadioButtonId()==R.id.survey_radio_viene_re_option1) {
                        if (!(texto_pais.equals("724"))) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption1);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            if (((texto_prov.equals("07")) || (texto_prov.equals("35")) || (texto_prov.equals("38")) || (texto_prov.equals("51")) || (texto_prov.equals("52")))) {
                                String textoError = activity.getResources().getString(R.string.survey_text_selectOption1);
                                return getDialogValueBackError(activity,
                                        activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                        textoError,
                                        activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                            } else {

                            }
                        }
                    }

                    break;
                case 10:
                    //P10
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
                case 11:
                    //P11
                    RadioGroup rgNmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);

                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_nmodos))) {
                        return false;
                    }

                    if (rgNmodos.getCheckedRadioButtonId() == R.id.survey_radio_nmodos_option3) {
                        EditText etNmodos_otros = (EditText) activity.findViewById(R.id.survey_edit_nmodos_otros);
                        if (stringToInt(etNmodos_otros.getText().toString()) < 3) {
                            String textoError = activity.getResources().getString(R.string.survey_text_error_mas2);
                            etNmodos_otros.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etNmodos_otros.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etNmodos_otros.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }

                    break;
                case 12:
                    //P12
                    if (checkUModo()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_error_umodo);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    RadioButton rbUmodo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option13);
                    RadioButton rb1modo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option13);
                    RadioButton rb2modo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option13);
                    EditText etUltimodo_otros_especificar = (EditText) activity.findViewById(R.id.survey_edit_ultimodo_otros);

                    if ((rbUmodo_13.isChecked() || rb1modo_13.isChecked() || rb2modo_13.isChecked()) &&
                            etUltimodo_otros_especificar.getText().toString().isEmpty()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etUltimodo_otros_especificar.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etUltimodo_otros_especificar.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etUltimodo_otros_especificar.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    rgNmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
                    RadioGroup rgUmodo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo);
                    RadioGroup rg1modo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_1modo);
                    RadioGroup rg2modo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_2modo);
                    switch (rgNmodos.getCheckedRadioButtonId()) {
                        case R.id.survey_radio_nmodos_option1:
                            if (!requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo))) {
                                return false;
                            }
                            break;
                        case R.id.survey_radio_nmodos_option2:
                            if (!requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo)) ||
                                    !requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_ultimodo_1modo))) {
                                return false;
                            }
                            break;
                        case R.id.survey_radio_nmodos_option3:
                            if (!requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo)) ||
                                    !requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_ultimodo_1modo)) ||
                                    !requeridoRadioGroupNModos(activity.findViewById(R.id.survey_radiogroup_ultimodo_2modo))) {
                                return false;
                            }
                            break;
                    }

                    break;
                case 13:
                    //P13
                    RadioGroup rgVtc = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_vtc);
                    EditText etVtc = (EditText) activity.findViewById(R.id.survey_edit_vtc_otros);

                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_vtc))){
                        return false;
                    }

                    if (rgVtc.getCheckedRadioButtonId()==R.id.survey_radio_vtc_option9 &&
                            etVtc.getText().toString().isEmpty()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etVtc.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etVtc.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etVtc.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 14:
                    //P14
                    RadioGroup rgSitiopark= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);
                    EditText etSitioparkotro = (EditText) activity.findViewById(R.id.survey_edit_sitioparkotro);

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

                    if (rgSitiopark.getCheckedRadioButtonId()==R.id.survey_radio_sitiopark_option9 &&
                            etSitioparkotro.getText().toString().isEmpty()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etSitioparkotro.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etSitioparkotro.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etSitioparkotro.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 15:
                    //P15
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
                case 16:
                    //P16
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_bustermi))) {
                        return false;
                    }
                    break;
                case 17:
                    //P17
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
                case 18:
                    //P18
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
                case 19:
                    //P19
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
                case 20:
                    //P20
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
                case 21:
                    //P21
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
                case 22:
                    //P22
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
                case 23:
                    //P23
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
                case 24:
                    //P24
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
                case 25:
                    //P25
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

                    if (intEtNninos >= (selectedCode)){
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
                case 26:
                    //P26
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_relacion))){
                        return false;
                    }
                    break;
                case 27:
                    //P27
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
                case 28:
                    //P28
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdbillet))){
                        return false;
                    }
                    break;
                case 29:
                    //P29
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
                case 30:
                    //P30
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
                case 31:
                    //P31
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
                case 32:
                    //P32
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_dropoff))){
                        return false;
                    }
                    break;
                case 33:
                    //P33
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_chekinb))){
                        return false;
                    }
                    break;
                case 34:
                    //P34
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
                case 35:
                    //P35
                    RadioGroup rgComprart = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_comprart);
                    SearchableSpinner sp_prod1 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_prod1);
                    //String st_cdmviaje = sp_cdmviaje1.getSelectedItem().toString().substring(0, 3);
                    //String prod1 = getValorDesplegable(sp_prod1).substring(0,1);

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

                        /*if(!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_prod1), "0")){
                            return false;
                        }*/
                        if (getValorDesplegable(sp_prod1).substring(0,1).equals("0")) {
                            sp_prod1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }
                    break;
                case 36:
                    //P36
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdslab))){
                        return false;
                    }
                    break;
                case 37:
                    //P37
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsprof))){
                        return false;
                    }
                    break;
                case 38:
                    //P38
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_estudios))){
                        return false;
                    }
                    break;
                case 39:
                    //P39
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdedad))){
                        return false;
                    }
                    break;
                case 40:
                    //P40
                    if(!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsexo))){
                        return false;
                    }
                    break;
                case 41:
                    //P41
                    RatingBar rabValorexp = (RatingBar) activity.findViewById(R.id.survey_rating_valorexp);
                    int intValorexp = Math.round(rabValorexp.getRating());
                    if (intValorexp<1 || intValorexp>11) {
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
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRESOTRO, cue.getDistresotro());
                    break;
                case 3:
                    //P3
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDCAMBIO, cue.getCdcambio());
                    break;
                case 4:
                    //P4
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO, cue.getCdiaptoo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO, cue.getCdiaptoootro());
                    break;
                case 5:
                    //P5
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTES, cue.getCiaantes());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTESOTRO, cue.getCiaantesotro());
                    break;
                case 6:
                    //P6
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CONEXFAC, cue.getConexfac());
                    break;
                case 7:
                    //P7
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSINOPE, cue.getCdsinope());
                    break;
                case 8:
                    //P8
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJEN, cue.getCdalojen());
                    break;
                case 9:
                    //P9
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VIEN_RE, cue.getVien_re());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACO, cue.getCdlocaco());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCE, cue.getDistracce());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCEOTRO, cue.getDistracceotro());
                    break;
                case 10:
                    //P10
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN, cue.getCdalojin());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN_OTROS, cue.getCdalojin_otros());
                    break;
                case 11:
                    //P11
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NMODOS, cue.getNmodos());
                    break;
                case 12:
                    //P12
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODO, cue.getUltimodo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODOOTRO, cue.getUltimodootro());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODO1, cue.getModo1());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODO2, cue.getModo2());
                    break;
                case 13:
                    //P13
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VTC, cue.getVtc());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VTCOTRO, cue.getVtcotro());
                    break;
                case 14:
                    //P14
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK, cue.getSitiopark());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARKOTRO, cue.getSitioparkotro());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PQFUERA, cue.getPqfuera());
                    break;
                case 15:
                    //P15
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES, String.valueOf(cue.getAcomptes()));
                    break;
                case 16:
                    //P16
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_BUSTERMI, String.valueOf(cue.getBustermi()));
                    break;
                case 17:
                    //P17
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGA, cue.getHllega());
                    break;
                case 18:
                    //P18
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD, cue.getCdiaptod());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTODOTRO, cue.getCdiaptodotro());
                    break;
                case 19:
                    //P19
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA, cue.getNumvuepa());
                    break;
                case 20:
                    //P20
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDTERM, cue.getCdterm());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR, cue.getCdociaar());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAROTRO, cue.getCdociaarotro());
                    break;
                case 21:
                    //P21
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF, cue.getCdiaptof());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOFOTRO, cue.getCdiaptofotro());
                    break;
                case 22:
                    //P22
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE, cue.getCdmviaje());
                    break;
                case 23:
                    //P23
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE, cue.getCdidavue());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_TAUS, String.valueOf(cue.getTaus()));
                    break;
                case 24:
                    //P24
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERS, String.valueOf(cue.getNpers()));
                    break;
                case 25:
                    //P25
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS, String.valueOf(cue.getNniños()));
                    break;
                case 26:
                    //P26
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_RELACION, cue.getRelacion());
                    break;
                case 27:
                    //P27
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDTRESER, cue.getCdtreser());
                    break;
                case 28:
                    //P28
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDBILLET, cue.getCdbillet());
                    break;
                case 29:
                    //P29
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE, cue.getNviaje());
                    break;
                case 30:
                    //P30
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_P44FACTU, cue.getP44factu());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_BULGRUPO, cue.getBulgrupo());
                    break;
                case 31:
                    //P31
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERBUL, cue.getNperbul());
                    break;
                case 32:
                    //P32
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DROPOFF, cue.getDropoff());
                    break;
                case 33:
                    //P33
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB, String.valueOf(cue.getChekinb()));
                    break;
                case 34:
                    //P34
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CONSUME, cue.getConsume());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_GAS_CONS, String.valueOf(cue.getGas_cons()));
                    break;
                case 35:
                    //P35
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_COMPRART, cue.getComprart());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_GAS_COM, String.valueOf(cue.getGas_com()));
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD1, cue.getProd1());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD2, cue.getProd2());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD3, cue.getProd3());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD4, cue.getProd4());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD5, cue.getProd5());
                    break;
                case 36:
                    //P36
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSLAB, cue.getCdslab());
                    break;
                case 37:
                    //P37
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF, cue.getCdsprof());
                    break;
                case 38:
                    //P38
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS, cue.getEstudios());
                    break;
                case 39:
                    //P39
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD, cue.getCdedad());
                    break;
                case 40:
                    //P40
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO, String.valueOf(cue.getCdsexo()));
                    break;
                case 41:
                    //P41
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
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDCAMBIO);
                    break;
                case 4:
                    //P4
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO);
                    break;
                case 5:
                    //P5
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTES);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTESOTRO);
                    break;
                case 6:
                    //P6
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CONEXFAC);
                    break;
                case 7:
                    //P7
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSINOPE);
                    break;
                case 8:
                    //P8
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJEN);
                    break;
                case 9:
                    //P9
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VIEN_RE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCEOTRO);
                    break;
                case 10:
                    //P10
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDALOJIN_OTROS);
                    break;
                case 11:
                    //P11
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NMODOS);
                    break;
                case 12:
                    //P12
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ULTIMODOOTRO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_MODO1);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_MODO2);
                    break;
                case 13:
                    //P13
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VTC);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VTCOTRO);
                    break;
                case 14:
                    //P14
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARKOTRO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PQFUERA);
                    break;
                case 15:
                    //P15
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES);
                    break;
                case 16:
                    //P16
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_BUSTERMI);
                    break;
                case 17:
                    //P17
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGA);
                    break;
                case 18:
                    //P18
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTODOTRO);
                    break;
                case 19:
                    //P19
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA);
                    break;
                case 20:
                    //P21
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDTERM);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAROTRO);
                    break;
                case 21:
                    //P21
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOFOTRO);
                    break;
                case 22:
                    //P22
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE);
                    break;
                case 23:
                    //P23
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_TAUS);
                    break;
                case 24:
                    //P24
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NPERS);
                    break;
                case 25:
                    //P25
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS);
                    break;
                case 26:
                    //P26
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_RELACION);
                    break;
                case 27:
                    //P27
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDTRESER);
                    break;
                case 28:
                    //P28
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDBILLET);
                    break;
                case 29:
                    //P29
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE);
                    break;
                case 30:
                    //P30
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_P44FACTU);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_BULGRUPO);
                    break;
                case 31:
                    //P31
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NPERBUL);
                    break;
                case 32:
                    //P32
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DROPOFF);
                    break;
                case 33:
                    //P33
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB);
                    break;
                case 34:
                    //P34
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CONSUME);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_GAS_CONS);
                    break;
                case 35:
                    //P35
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_COMPRART);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_GAS_COM);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD1);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD2);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD3);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD4);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PROD5);
                    break;
                case 36:
                    //P36
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSLAB);
                    break;
                case 37:
                    //P37
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF);
                    break;
                case 38:
                    //P38
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS);
                    break;
                case 39:
                    //P39
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD);
                    break;
                case 40:
                    //P40
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO);
                    break;
                case 41:
                    //P41
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
        LinearLayout cdcambio = (LinearLayout) activity.findViewById(R.id.survey_layout_cdcambio);
        cdcambio.setVisibility(GONE);

        //P4
        LinearLayout cdiaptoo = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptoo);
        cdiaptoo.setVisibility(GONE);

        //P5
        LinearLayout ciaantes = (LinearLayout) activity.findViewById(R.id.survey_layout_ciaantes);
        ciaantes.setVisibility(GONE);

        //P6
        LinearLayout conexfac = (LinearLayout) activity.findViewById(R.id.survey_layout_conexfac);
        conexfac.setVisibility(GONE);

        //P7
        LinearLayout cdsinope = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsinope);
        cdsinope.setVisibility(GONE);

        //P8
        LinearLayout cdalojen = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojen);
        cdalojen.setVisibility(GONE);

        //P9
        LinearLayout viene_re = (LinearLayout) activity.findViewById(R.id.survey_layout_viene_re);
        viene_re.setVisibility(GONE);

        //P10
        LinearLayout cdalojin = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojin_crucero);
        cdalojin.setVisibility(GONE);

        //P11
        LinearLayout nmodos = (LinearLayout) activity.findViewById(R.id.survey_layout_nmodos);
        nmodos.setVisibility(GONE);

        //P12
        LinearLayout ultimomodo = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo);
        ultimomodo.setVisibility(GONE);

        //P13
        LinearLayout vtc = (LinearLayout) activity.findViewById(R.id.survey_layout_vtc);
        vtc.setVisibility(GONE);

        //P14
        LinearLayout sitiopark = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
        sitiopark.setVisibility(GONE);

        //P15
        LinearLayout acomptes = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
        acomptes.setVisibility(GONE);

        //P16
        LinearLayout bustermi = (LinearLayout) activity.findViewById(R.id.survey_layout_bustermi);
        bustermi.setVisibility(GONE);

        //P17
        LinearLayout hllega = (LinearLayout) activity.findViewById(R.id.survey_layout_hllega);
        hllega.setVisibility(GONE);

        //P18
        LinearLayout diaptod = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptod);
        diaptod.setVisibility(GONE);

        //P19
        LinearLayout numvuepa = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
        numvuepa.setVisibility(GONE);

        //P20
        LinearLayout cdterm_cdociaar = (LinearLayout) activity.findViewById(R.id.survey_layout_cdterm);
        cdterm_cdociaar.setVisibility(GONE);

        //P21
        LinearLayout cdiaptof = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptof);
        cdiaptof.setVisibility(GONE);

        //P22
        LinearLayout cdmviaje = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
        cdmviaje.setVisibility(GONE);

        //P23
        LinearLayout cdidavue_taus = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
        cdidavue_taus.setVisibility(GONE);

        //P24
        LinearLayout npers = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
        npers.setVisibility(GONE);

        //P25
        LinearLayout nniños = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
        nniños.setVisibility(GONE);

        //P26
        LinearLayout relacion = (LinearLayout) activity.findViewById(R.id.survey_layout_relacion);
        relacion.setVisibility(GONE);

        //P27
        LinearLayout cdtreser = (LinearLayout) activity.findViewById(R.id.survey_layout_cdtreser);
        cdtreser.setVisibility(GONE);

        //P28
        LinearLayout cdbillet = (LinearLayout) activity.findViewById(R.id.survey_layout_cdbillet);
        cdbillet.setVisibility(GONE);

        //P29
        LinearLayout nviaje = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
        nviaje.setVisibility(GONE);

        //P30
        LinearLayout p44factu_bulgrupo = (LinearLayout) activity.findViewById(R.id.survey_layout_p44factu);
        p44factu_bulgrupo.setVisibility(GONE);

        //P31
        LinearLayout nperbul = (LinearLayout) activity.findViewById(R.id.survey_layout_nperbul);
        nperbul.setVisibility(GONE);

        //P32
        LinearLayout dropoff = (LinearLayout) activity.findViewById(R.id.survey_layout_dropoff);
        dropoff.setVisibility(GONE);

        //P33
        LinearLayout checkinb = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
        checkinb.setVisibility(GONE);

        //P34
        LinearLayout consume_gans_cons = (LinearLayout) activity.findViewById(R.id.survey_layout_consume);
        consume_gans_cons.setVisibility(GONE);

        //P35
        LinearLayout comprart_gas_com_prod = (LinearLayout) activity.findViewById(R.id.survey_layout_comprart);
        comprart_gas_com_prod.setVisibility(GONE);

        //P36
        LinearLayout cdslab = (LinearLayout) activity.findViewById(R.id.survey_layout_cdslab);
        cdslab.setVisibility(GONE);

        //P37
        LinearLayout cdsprof = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
        cdsprof.setVisibility(GONE);

        //P38
        LinearLayout estudios = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
        estudios.setVisibility(GONE);

        //P39
        LinearLayout cdedad = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
        cdedad.setVisibility(GONE);

        //P40
        LinearLayout cdsexo = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
        cdsexo.setVisibility(GONE);

        //P41
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
        int checkedId3;

        switch (show) {
            case 1:
                //B1
                show = showQuestion(2);
                break;

            case 2:
                //B2
                show = showQuestion(3);
                break;

            case 3:
                //P3
                if (activated) {
                    RadioGroup rgCdcambio = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdcambio);
                    checkedId = rgCdcambio.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdcambio_option1:
                                show = showQuestion(4); //>P4
                                break;
                            case R.id.survey_radio_cdcambio_option2:
                                show = showQuestion(9); //>P9
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
                show = showQuestion(5);
                break;
            case 5:
                //P5
                show = showQuestion(6);
                break;
            case 6:
                //P6
                show = showQuestion(7);
                break;
            case 7:
                //P7
                if (activated) {
                    RadioGroup rgCdsinope = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsinope);
                    checkedId = rgCdsinope.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdsinope_option1:
                                show = showQuestion(8); //>P8
                                break;
                            case R.id.survey_radio_cdsinope_option2:
                                show = showQuestion(16); //>P16
                                break;
                        }
                    } else {
                        show = showQuestion(8); //>P8
                    }
                } else {
                    show = showQuestion(8);
                }
                break;
            case 8:
                //P8
                if (activated) {
                    RadioGroup rgCdalojen = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojen);
                    checkedId = rgCdalojen.getCheckedRadioButtonId();
                    RadioButton rbViene_re1 = (RadioButton) activity.findViewById(R.id.survey_radio_viene_re_option1);
                    rbViene_re1.setVisibility(VISIBLE);
                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdalojen_option0:
                                show = showQuestion(16); //>P16
                                break;
                            case R.id.survey_radio_cdalojen_option1:
                                show = showQuestion(9); //>P9
                                rbViene_re1.setVisibility(GONE);
                                break;
                            case R.id.survey_radio_cdalojen_option7:
                                show = showQuestion(9);
                                rbViene_re1.setVisibility(GONE);
                                break;
                            case R.id.survey_radio_cdalojen_option9:
                                if (activated) {
                                    RadioGroup rgCdcambio = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdcambio);
                                    checkedId = rgCdcambio.getCheckedRadioButtonId();

                                    if (checkedId > 0) {
                                        switch (checkedId) {
                                            case R.id.survey_radio_cdcambio_option1:
                                                show = showQuestion(9); //>P9
                                                rbViene_re1.setVisibility(GONE);
                                                break;
                                        }
                                    } else {
                                        show = showQuestion(9); //>P9
                                    }
                                } else {
                                    show = showQuestion(9);
                                }
                                break;
                        }
                    } else {
                        show = showQuestion(9); //>P9
                    }
                } else {
                    show = showQuestion(9);
                }
                break;
            case 9:
                //P9
                generarTituloCdalojin();
                if (activated) {
                    RadioGroup rgViene_re = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re);
                    checkedId = rgViene_re.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_viene_re_option1:
                                // Si el pasajero viene de su residencia habitual tiene que ser viaje de IDA: si P9=1 P22=1.
                                activity.findViewById(R.id.survey_radio_cdidavue_option2).setVisibility(GONE);
                                show = showQuestion(11); //>P11
                                break;
                            case R.id.survey_radio_viene_re_option2:
                                // Si el pasajero viene de su residencia habitual tiene que ser viaje de IDA: si P9=1 P22=1.
                                activity.findViewById(R.id.survey_radio_cdidavue_option2).setVisibility(VISIBLE);
                                //Si en P8 anota alojamiento en conexión (Hotel/Vivienda familiares amigos), NO preguntar P10
                                RadioGroup rgCdalojen = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojen);
                                switch (rgCdalojen.getCheckedRadioButtonId()) {
                                    case R.id.survey_radio_cdalojen_option1:
                                        show = showQuestion(11); //>P11
                                        break;
                                    case R.id.survey_radio_cdalojen_option7:
                                        show = showQuestion(11); //>P11
                                        break;
                                    default:
                                        show = showQuestion(10); //>P10
                                        break;
                                }
                        }
                    } else {
                        show = showQuestion(10); //>P10
                    }
                } else {
                    show = showQuestion(10);
                }
                break;
            case 10:
                //P10
                show = showQuestion(11);
                break;
            case 11:
                //P11
                RadioGroup rgNmodos= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
                checkedId = rgNmodos.getCheckedRadioButtonId();

                if (checkedId > 0) {
                    switch (checkedId) {
                        case R.id.survey_radio_nmodos_option1:
                            //P12
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_text_ultimodo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_text_ultimodo_b).setVisibility(GONE);
                            break;
                        case R.id.survey_radio_nmodos_option2:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_text_ultimodo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_text_ultimodo_b).setVisibility(VISIBLE);
                            break;
                        case R.id.survey_radio_nmodos_option3:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_text_ultimodo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_text_ultimodo_b).setVisibility(VISIBLE);
                            break;
                    }
                }

                show = showQuestion(12);
                break;
            case 12:
                //P12
                if (activated) {
                    RadioGroup rgUltimodo_umodo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo);
                    RadioGroup rgUltimodo_1modo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_1modo);
                    RadioGroup rgUltimodo_2modo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_2modo);
                    checkedId = rgUltimodo_umodo.getCheckedRadioButtonId();
                    checkedId2 = rgUltimodo_1modo.getCheckedRadioButtonId();
                    checkedId3 = rgUltimodo_2modo.getCheckedRadioButtonId();
                    RadioButton rbSitiopark1 = (RadioButton) activity.findViewById(R.id.survey_radio_sitiopark_option1);
                    rbSitiopark1.setVisibility(VISIBLE);
                    RadioButton rbSitiopark5 = (RadioButton) activity.findViewById(R.id.survey_radio_sitiopark_option5);
                    rbSitiopark5.setVisibility(VISIBLE);
                    RadioButton rbSitiopark6 = (RadioButton) activity.findViewById(R.id.survey_radio_sitiopark_option6);
                    rbSitiopark6.setVisibility(VISIBLE);

                    if ((checkedId == R.id.survey_radio_ultimodo_umodo_option2)) {
                        show = showQuestion(13);//>P13
                    } else if ((checkedId == R.id.survey_radio_ultimodo_umodo_option4) || (checkedId == R.id.survey_radio_ultimodo_umodo_option5)) {
                        if((checkedId == R.id.survey_radio_ultimodo_umodo_option4)){
                                rbSitiopark1.setVisibility(GONE);
                                rbSitiopark6.setVisibility(GONE);
                                rbSitiopark5.setVisibility(VISIBLE);
                        }else if((checkedId == R.id.survey_radio_ultimodo_umodo_option5)){
                                rbSitiopark5.setVisibility(GONE);
                                rbSitiopark1.setVisibility(VISIBLE);
                                rbSitiopark6.setVisibility(VISIBLE);
                        }
                        show = showQuestion(14);//>P14
                    } else {
                        show = showQuestion(15);//>P14
                        rbSitiopark5.setVisibility(VISIBLE);
                        rbSitiopark1.setVisibility(VISIBLE);
                        rbSitiopark6.setVisibility(VISIBLE);
                    }

                } else {
                    show = showQuestion(15);//>P13
                }
                break;
            case 13:
                //P13
                show = showQuestion(15);//>P13
                break;
            case 14:
                //P14
                show = showQuestion(15);
                break;
            case 15:
                //P15
                show = showQuestion(16);
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
                generarTituloCdterm();
                show = showQuestion(19);
                break;
            case 19:
                //P19
                show = showQuestion(20);
                break;
            case 20:
                //P20
                generarTituloCdterm();
                if (activated) {
                    RadioGroup rgCdterm = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdterm);
                    checkedId = rgCdterm.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdterm_option1:
                                show = showQuestion(22); //>P22
                                break;
                            case R.id.survey_radio_cdterm_option2:
                                show = showQuestion(21); //>P21
                                break;
                        }
                    } else {
                        show = showQuestion(21); //>P21
                    }
                } else {
                    show = showQuestion(21); //>P21
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
                show = showQuestion(24);
                break;
            case 24:
                //P24
                if (activated) {
                    RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
                    checkedId = rgNpers.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_npers_option1:
                                show = showQuestion(27); //>P27
                                break;
                            case R.id.survey_radio_npers_option2:
                                show = showQuestion(25); //>P25
                                break;
                            case R.id.survey_radio_npers_option3:
                                show = showQuestion(25); //>P25
                                break;
                        }
                    } else {
                        show = showQuestion(25); //>P25
                    }
                } else {
                    show = showQuestion(25);
                }
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
                show = showQuestion(29);
                break;
            case 29:
                //P29
                show = showQuestion(30);
                break;
            case 30:
                //P30
                if (activated) {
                    RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
                    RadioGroup rgP44factu = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_p44factu);
                    checkedId = rgNpers.getCheckedRadioButtonId();
                    checkedId2 = rgP44factu.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_npers_option1:
                                if (checkedId2 > 0) {
                                    switch (checkedId2) {
                                        case R.id.survey_radio_p44factu_option1:
                                            show = showQuestion(32); //>P32
                                            break;
                                        case R.id.survey_radio_p44factu_option2:
                                            show = showQuestion(33); //>P33
                                            break;
                                    }
                                } else {
                                    show = showQuestion(32); //>P32
                                }
                                break;
                            default:
                                if (checkedId2 > 0) {
                                    switch (checkedId2) {
                                        case R.id.survey_radio_p44factu_option1:
                                            show = showQuestion(31); //>P31
                                            break;
                                        case R.id.survey_radio_p44factu_option2:
                                            show = showQuestion(33); //>P33
                                            break;
                                    }
                                } else {
                                    show = showQuestion(31); //>P31
                                }
                                break;
                        }
                    } else {
                        show = showQuestion(31); //>P31
                    }
                }
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
                show = showQuestion(34);
                break;
            case 34:
                //P34
                show = showQuestion(35);
                break;
            case 35:
                //P35
                show = showQuestion(36);
                break;
            case 36:
                //P36
                if (activated) {
                    RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
                    checkedId = rgCdslab.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_cdslab_option1:
                                show = showQuestion(37); //>P37
                                break;
                            default:
                                show = showQuestion(38); //>P38
                                break;
                        }
                    } else {
                        show = showQuestion(38); //>P38
                        break;
                    }
                } else {
                    show = showQuestion(38); //>P38
                    break;
                }
                break;
            case 37:
                //P37
                show = showQuestion(38);
                break;
            case 38:
                //P38
                show = showQuestion(39);
                break;
            case 39:
                //P39
                show = showQuestion(40);
                break;
            case 40:
                //P40
                show = showQuestion(41);
                break;
            case 41:
                //P41
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

        SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        //String textSpDistres = sp_distres.getSelectedItem().toString().substring(0, 2);
        String textSpDistres = getValorDesplegable(sp_distres).substring(0,2);
        SearchableSpinner sp_distres_area = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres_area);
        //String textSpDistres_area = sp_distres_area.getSelectedItem().toString().substring(0, 3);
        String textSpDistres_area = getValorDesplegable(sp_distres_area).substring(0,3);
        String filtroAeropuerto = "28079";

        switch (idAeropuerto) {
            case 2:
                //Barcelona
                filtroAeropuerto = "08019";
                break;
        }
        if (!textSpDistres.contains("00") && textSpCdlocado.contains(filtroAeropuerto)) {
            if (textSpDistres.contains("OT")){
                EditText et_distresotro = (EditText) activity.findViewById(R.id.survey_edit_distresotro);
                quest.setDistresotro(et_distresotro.getText().toString());
            } else {
                quest.setDistresotro("-1");
            }
            quest.setDistres(textSpDistres);
        } else if (!textSpDistres.contains("000") && compruebaListaPaises1y2(textSpCdpaisre)>0) {
            quest.setDistres(textSpDistres_area);
        } else {
            quest.setDistres("-1");
            quest.setDistresotro("-1");
        }

        //P3
        RadioGroup rgCdcambio = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdcambio);

        selectedCode = -1;
        checkedId = rgCdcambio.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdcambio_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdcambio_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdcambio(String.valueOf(selectedCode));

        //P4
        SearchableSpinner sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);
        //String textSpCdiaptoo = sp_cdiaptoo.getSelectedItem().toString().substring(0,3);
        String textSpCdiaptoo = getValorDesplegable(sp_cdiaptoo).substring(0,3);
        if(!textSpCdiaptoo.contains("000")){
            quest.setCdiaptoo(textSpCdiaptoo);
            if (textSpCdiaptoo.contains("ZZZ")){
                EditText et_cdiaptoootro = (EditText) activity.findViewById(R.id.survey_edit_cdiaptoootro);
                quest.setCdiaptoootro(et_cdiaptoootro.getText().toString());
            } else {
                quest.setCdiaptoootro("-1");
            }
        } else {
            quest.setCdiaptoo("-1");
        }

        //P5
        SearchableSpinner sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);
        //String textSpCiaantes = sp_ciaantes.getSelectedItem().toString().substring(0,3);
        String textSpCiaantes = getValorDesplegable(sp_ciaantes).substring(0,3);
        if(!textSpCiaantes.contains("000")){
            quest.setCiaantes(textSpCiaantes);
            if (textSpCiaantes.contains("999")){
                EditText et_ciaantesotro = (EditText) activity.findViewById(R.id.survey_edit_ciaantesotro);
                quest.setCiaantesotro(et_ciaantesotro.getText().toString());
            } else {
                quest.setCiaantesotro("-1");
            }
        } else {
            quest.setCiaantes("-1");
        }

        //P6
        RadioGroup rgConexfac = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_conexfac);

        selectedCode = -1;
        checkedId = rgConexfac.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_conexfac_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_conexfac_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setConexfac(String.valueOf(selectedCode));

        //P7
        RadioGroup rgCdsinope = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsinope);

        selectedCode = -1;
        checkedId = rgCdsinope.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdsinope_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdsinope_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdsinope(String.valueOf(selectedCode));

        //P8
        RadioGroup rgCdalojen = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojen);

        selectedCode = -1;
        checkedId = rgCdalojen.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdalojen_option0:
                    selectedCode = 0;
                    break;
                case R.id.survey_radio_cdalojen_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdalojen_option7:
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_cdalojen_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdalojen(String.valueOf(selectedCode));

        //P9
        RadioGroup rgViene_re = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re);

        SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
        //String textSpCdlocaco = sp_cdlocaco.getSelectedItem().toString().substring(0,5);
        String textSpCdlocaco = getValorDesplegable(sp_cdlocaco).substring(0,5);

        SearchableSpinner sp_distracce = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distracce);
        //String textSpDistracce = sp_distracce.getSelectedItem().toString().substring(0,2);
        String textSpDistracce = getValorDesplegable(sp_distracce).substring(0,2);

        selectedCode = -1;
        checkedId = rgViene_re.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_viene_re_option1:
                    selectedCode = 1;
                    quest.setCdlocaco("-1");
                    quest.setDistracce("-1");
                    quest.setDistracceotro("-1");
                    break;
                case R.id.survey_radio_viene_re_option2:
                    if(!textSpCdlocaco.contains("00000")){
                        quest.setCdlocaco(textSpCdlocaco);
                    } else {
                        quest.setCdlocaco("-1");
                    }
                    filtroAeropuerto = "28079";

                    switch (idAeropuerto) {
                        case 2:
                            //Barcelona
                            filtroAeropuerto = "08019";
                            break;
                    }
                    if(!textSpDistracce.contains("00") && textSpCdlocaco.contains(filtroAeropuerto)){
                        if (textSpDistracce.contains("OT")){
                            EditText et_distraccesotro = (EditText) activity.findViewById(R.id.survey_edit_distracceotro);
                            quest.setDistracceotro(et_distraccesotro.getText().toString());
                        } else {
                            quest.setDistracceotro("-1");
                        }
                        quest.setDistracce(textSpDistracce);
                    } else {
                        quest.setDistracce("-1");
                        quest.setDistresotro("-1");
                    }
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setVien_re(String.valueOf(selectedCode));

        //P10
        RadioGroup rgCdalojin = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojin_crucero);
        EditText etCdalojin_otros = (EditText) activity.findViewById(R.id.survey_edit_cdalojin_otros);

        selectedCode = -1;
        quest.setCdalojin_otros("-1");
        checkedId = rgCdalojin.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdalojin_option1:
                    quest.setCdalojin_otros("-1");
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdalojin_option2:
                    quest.setCdalojin_otros("-1");
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdalojin_option4:
                    quest.setCdalojin_otros("-1");
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_cdalojin_option7:
                    quest.setCdalojin_otros("-1");
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_cdalojin_option8:
                    quest.setCdalojin_otros("-1");
                    selectedCode = 8;
                    break;
                case R.id.survey_radio_cdalojin_option10:
                    quest.setCdalojin_otros("-1");
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

        //P11
        RadioGroup rgNmodos= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
        EditText etNmodos_otros = (EditText) activity.findViewById(R.id.survey_edit_nmodos_otros);

        selectedCode = -1;
        checkedId = rgNmodos.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_nmodos_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_nmodos_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_nmodos_option3:
                    selectedCode = Integer.valueOf(stringToInt(etNmodos_otros.getText().toString()));
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setNmodos(String.valueOf(selectedCode));

        //P12
        RadioGroup rgUltimodo_umodo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_umodo);
        RadioGroup rgUltimodo_1modo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_1modo);
        RadioGroup rgUltimodo_2modo= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ultimodo_2modo);
        EditText etUltimodo_otros= (EditText) activity.findViewById(R.id.survey_edit_ultimodo_otros);

        selectedCode = -1;
        checkedId = rgUltimodo_umodo.getCheckedRadioButtonId();
        quest.setUltimodootro("-1");

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_ultimodo_umodo_option1:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option2:
                    selectedCode = 24;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option3:
                    selectedCode = 25;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option4:
                    selectedCode = 22;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option5:
                    selectedCode = 23;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option6:
                    selectedCode = 35;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option7:
                    selectedCode = 37;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option8:
                    selectedCode = 39;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option9:
                    selectedCode = 38;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option10:
                    selectedCode = 42;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option11:
                    selectedCode = 42;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option12:
                    selectedCode = 43;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option13:
                    quest.setUltimodootro(etUltimodo_otros.getText().toString());
                    selectedCode = 91;
                    break;
                default:
                    selectedCode = 0;
                    break;
            }
        }
        quest.setUltimodo(String.valueOf(selectedCode));

        selectedCode = -1;
        if (stringToInt(quest.getNmodos())>1) {
            checkedId = rgUltimodo_1modo.getCheckedRadioButtonId();

            if (checkedId > 0) {
                switch (checkedId) {
                    case R.id.survey_radio_ultimodo_1modo_option1:
                        selectedCode = 11;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option2:
                        selectedCode = 24;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option3:
                        selectedCode = 25;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option4:
                        selectedCode = 22;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option5:
                        selectedCode = 23;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option6:
                        selectedCode = 35;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option7:
                        selectedCode = 37;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option8:
                        selectedCode = 39;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option9:
                        selectedCode = 38;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option10:
                        selectedCode = 42;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option11:
                        selectedCode = 42;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option12:
                        selectedCode = 43;
                        break;
                    case R.id.survey_radio_ultimodo_1modo_option13:
                        quest.setUltimodootro(etUltimodo_otros.getText().toString());
                        selectedCode = 91;
                        break;
                    default:
                        selectedCode = 0;
                        break;
                }
            }
        }
        quest.setModo1(String.valueOf(selectedCode));

        selectedCode = -1;
        if (stringToInt(quest.getNmodos())>=3) {
            checkedId = rgUltimodo_2modo.getCheckedRadioButtonId();
            if (checkedId > 0) {
                switch (checkedId) {
                    case R.id.survey_radio_ultimodo_2modo_option1:
                        selectedCode = 11;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option2:
                        selectedCode = 24;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option3:
                        selectedCode = 25;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option4:
                        selectedCode = 22;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option5:
                        selectedCode = 23;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option6:
                        selectedCode = 35;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option7:
                        selectedCode = 37;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option8:
                        selectedCode = 39;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option9:
                        selectedCode = 38;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option10:
                        selectedCode = 42;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option11:
                        selectedCode = 42;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option12:
                        selectedCode = 43;
                        break;
                    case R.id.survey_radio_ultimodo_2modo_option13:
                        quest.setUltimodootro(etUltimodo_otros.getText().toString());
                        selectedCode = 91;
                        break;
                    default:
                        selectedCode = 0;
                        break;
                }
            }
        }

        quest.setModo2(String.valueOf(selectedCode));

        //P13
        RadioGroup rgVtc = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_vtc);
        EditText etVtc_otros = (EditText) activity.findViewById(R.id.survey_edit_vtc_otros);

        selectedCode = -1;
        quest.setVtcotro("-1");
        checkedId = rgVtc.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_vtc_option1:
                    quest.setVtcotro("-1");
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_vtc_option2:
                    quest.setVtcotro("-1");
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_vtc_option3:
                    quest.setVtcotro("-1");
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_vtc_option9:
                    selectedCode = 9;
                    quest.setVtcotro(etVtc_otros.getText().toString());
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setVtc(String.valueOf(selectedCode));

        //P14
        RadioGroup rgSitioPark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);

        selectedCode = -1;
        checkedId = rgSitioPark.getCheckedRadioButtonId();
        quest.setPqfuera("-1");

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_sitiopark_option1_m4:
                    quest.setPqfuera("-1");
                    quest.setSitioparkotro("-1");
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_sitiopark_option4:
                    EditText etPqfuera = (EditText) activity.findViewById(R.id.survey_edit_pqfuera);
                    quest.setPqfuera(etPqfuera.getText().toString());
                    quest.setSitioparkotro("-1");
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_sitiopark_option5:
                    quest.setPqfuera("-1");
                    quest.setSitioparkotro("-1");
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_sitiopark_option6:
                    quest.setPqfuera("-1");
                    quest.setSitioparkotro("-1");
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_sitiopark_option9:
                    quest.setPqfuera("-1");
                    EditText etSitioparkotro= (EditText) activity.findViewById(R.id.survey_edit_sitioparkotro);
                    quest.setSitioparkotro(etSitioparkotro.getText().toString());
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setSitiopark(String.valueOf(selectedCode));

        //P15
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

        //P16
        RadioGroup rgBustermi = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_bustermi);

        selectedCode = -1;
        checkedId = rgBustermi.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_bustermi_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_bustermi_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }
        quest.setBustermi(selectedCode);

        //P17
        EditText hllega_hora = (EditText) activity.findViewById(R.id.survey_edit_hllega_hora);
        EditText hllega_minutos = (EditText) activity.findViewById(R.id.survey_edit_hllega_minutos);
        quest.setHllega(hllega_hora.getText().toString()+":"+hllega_minutos.getText().toString());

        //P18
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

        //P19
        SearchableSpinner sp_numvuepa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_numvuepa);
        //String textSpNumvuepa = sp_numvuepa.getSelectedItem().toString().substring(0,3);
        String textSpNumvuepa = getValorDesplegable(sp_numvuepa).substring(0,3);
        EditText numvuepa = (EditText) activity.findViewById(R.id.survey_edit_numvuepa);
        if(!textSpNumvuepa.contains("000")){
            quest.setNumvuepa(textSpNumvuepa+"-"+numvuepa.getText().toString());
        } else {
            quest.setNumvuepa("-1");
        }

        //P20
        RadioGroup rgCdterm = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdterm);

        selectedCode = -1;
        checkedId = rgCdterm.getCheckedRadioButtonId();
        quest.setCdociaar("-1");

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdterm_option1:
                    quest.setCdociaarotro("-1");
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

        //P21
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

        //P22
        SearchableSpinner sp_cdmviaje = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdmviaje);
        String textSpCdmviaje = getValorDesplegable(sp_cdmviaje).substring(0,3);
        //String textSpCdmviaje = sp_cdmviaje.getSelectedItem().toString().substring(0,3);
        if(!textSpCdmviaje.contains("000")){
            quest.setCdmviaje(textSpCdmviaje);
        } else {
            quest.setCdmviaje("-1");
        }

        //P23
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
                        activity.findViewById(R.id.survey_edit_text_taus_m4).setVisibility(GONE);
                    } else {
                        EditText etTaus = (EditText) activity.findViewById(R.id.survey_edit_taus);
                        quest.setTaus(stringToInt(etTaus.getText().toString()));
                    }
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdidavue_option2:
                    if (ckCdidavue.isChecked()){
                        quest.setTaus(stringToInt("0"));
                        activity.findViewById(R.id.survey_edit_text_taus_m4).setVisibility(GONE);
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

        //P24
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

        //P25
        EditText etNniños= (EditText) activity.findViewById(R.id.survey_edit_nniños);
        quest.setNniños(stringToInt(etNniños.getText().toString()));

        //P26
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

        //P27
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

        //P28
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

        //P29
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

        //P30
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

        //P31
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

        //P32
        RadioGroup rgDropoff = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_dropoff);
        selectedCode = -1;

        checkedId = rgDropoff.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_dropoff_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_dropoff_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setDropoff(String.valueOf(selectedCode));

        //P33
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

        //P34
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

        //P35
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

        //P36
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

        //P37
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

        //P38
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

        //P39
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

        //P40
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

        //P41
        RatingBar rabValorexp = (RatingBar) activity.findViewById(R.id.survey_rating_valorexp);
        quest.setValorexp(Math.round(rabValorexp.getRating())-1);

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
        //Lanzarote , Tenerife Sur, Santander y Almeria
        SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
        //String textSpCdlocaco = sp_cdlocaco.getSelectedItem().toString();
        String textSpCdlocaco = getValorDesplegable(sp_cdlocaco);
        String textoCdalojin = activity.getString(R.string.survey_m1_text_cdalojin);
        TextView tvCdalojin = (TextView) activity.findViewById(R.id.survey_text_cdalojin_crucero_m1);
        if (textSpCdlocaco.length()>6)
            textSpCdlocaco = textSpCdlocaco.substring(6, textSpCdlocaco.length());
        tvCdalojin.setText(textoCdalojin.replace("%1$s", textSpCdlocaco));

    }

    private void generarTituloCdterm() {
        SearchableSpinner sp_cdiaptod = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptod);
        //String textSpCdiaptod = sp_cdiaptod.getSelectedItem().toString();
        String textSpCdiaptod = getValorDesplegable(sp_cdiaptod);
        String textoCdterm = activity.getString(R.string.survey_m1_text_cdterm);
        if (textSpCdiaptod.length()>4)
            textSpCdiaptod = textSpCdiaptod.substring(4, textSpCdiaptod.length());
        TextView tvCdterm= (TextView) activity.findViewById(R.id.survey_text_cdterm);
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

    private String comprobarCP(String codigo) {
        String codPais = "0";
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;
        //String textoCP = "SELECT " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " FROM " +TABLE_TIPOAEROPUERTOS+" WHERE " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " = '" +texto+ "'";
        Cursor cursor = db.rawQuery("SELECT "+ Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS +
                " FROM " + Contracts.TABLE_TIPOAEROPUERTOS +
                " WHERE "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " = '"+codigo+"'", parametros);

        while (cursor.moveToNext()) {
            codPais = cursor.getString(0);
        }

        cursor.close();

        return codPais;
    }

    private String compCiudad(String codigo) {
        String ciudad = "0";
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;
        //String textoCP = "SELECT " + Contracts.COLUMN_TIPOAEROPUERTOS_CODPAIS + " FROM " +TABLE_TIPOAEROPUERTOS+" WHERE " + Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " = '" +texto+ "'";
        Cursor cursor = db.rawQuery("SELECT "+ Contracts.COLUMN_TIPOAEROPUERTOS_CIUDAD +
                " FROM " + Contracts.TABLE_TIPOAEROPUERTOS +
                " WHERE "+Contracts.COLUMN_TIPOAEROPUERTOS_CODIGO + " = '"+codigo+"'", parametros);

        while (cursor.moveToNext()) {
            ciudad = cursor.getString(0);
        }

        cursor.close();

        return ciudad;
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

    private boolean checkUModo(){
        RadioButton rbUmodo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option1);
        RadioButton rbUmodo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option2);
        RadioButton rbUmodo_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option3);
        RadioButton rbUmodo_4 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option4);
        RadioButton rbUmodo_5 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option5);
        RadioButton rbUmodo_6 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option6);
        RadioButton rbUmodo_7 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option7);
        RadioButton rbUmodo_8 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option8);
        RadioButton rbUmodo_9 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option9);
        RadioButton rbUmodo_10 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option10);
        RadioButton rbUmodo_11 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option11);
        RadioButton rbUmodo_12 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option12);
        RadioButton rbUmodo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option13);

        RadioButton rb1modo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option1);
        RadioButton rb1modo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option2);
        RadioButton rb1modo_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option3);
        RadioButton rb1modo_4 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option4);
        RadioButton rb1modo_5 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option5);
        RadioButton rb1modo_6 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option6);
        RadioButton rb1modo_7 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option7);
        RadioButton rb1modo_8 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option8);
        RadioButton rb1modo_9 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option9);
        RadioButton rb1modo_10 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option10);
        RadioButton rb1modo_11 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option11);
        RadioButton rb1modo_12 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option12);
        RadioButton rb1modo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_1modo_option13);

        RadioButton rb2modo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option1);
        RadioButton rb2modo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option2);
        RadioButton rb2modo_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option3);
        RadioButton rb2modo_4 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option4);
        RadioButton rb2modo_5 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option5);
        RadioButton rb2modo_6 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option6);
        RadioButton rb2modo_7 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option7);
        RadioButton rb2modo_8 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option8);
        RadioButton rb2modo_9 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option9);
        RadioButton rb2modo_10 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option10);
        RadioButton rb2modo_11 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option11);
        RadioButton rb2modo_12 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option12);
        RadioButton rb2modo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_2modo_option13);

        LinearLayout rgUmodo_1 = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo_1modo);
        LinearLayout rgUmodo_2 = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo_2modo);

        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_1.isChecked() && (rbUmodo_1.isChecked() || rb1modo_1.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_1.isChecked() && rbUmodo_1.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_2.isChecked() && (rbUmodo_2.isChecked() || rb1modo_2.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_2.isChecked() && rbUmodo_2.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_3.isChecked() && (rbUmodo_3.isChecked() || rb1modo_3.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_3.isChecked() && rbUmodo_3.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_4.isChecked() && (rbUmodo_4.isChecked() || rb1modo_4.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_4.isChecked() && rbUmodo_4.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_5.isChecked() && (rbUmodo_5.isChecked() || rb1modo_5.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_5.isChecked() && rbUmodo_5.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_6.isChecked() && (rbUmodo_6.isChecked() || rb1modo_6.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_6.isChecked() && rbUmodo_6.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_7.isChecked() && (rbUmodo_7.isChecked() || rb1modo_7.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_7.isChecked() && rbUmodo_7.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_8.isChecked() && (rbUmodo_8.isChecked() || rb1modo_8.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_8.isChecked() && rbUmodo_8.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_9.isChecked() && (rbUmodo_9.isChecked() || rb1modo_9.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_9.isChecked() && rbUmodo_9.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_10.isChecked() && (rbUmodo_10.isChecked() || rb1modo_10.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_10.isChecked() && rbUmodo_10.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_11.isChecked() && (rbUmodo_11.isChecked() || rb1modo_11.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_11.isChecked() && rbUmodo_11.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_12.isChecked() && (rbUmodo_12.isChecked() || rb1modo_12.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_12.isChecked() && rbUmodo_12.isChecked()))
            return true;
        if((rgUmodo_2.getVisibility()==VISIBLE && rb2modo_13.isChecked() && (rbUmodo_13.isChecked() || rb1modo_13.isChecked())) ||
                (rgUmodo_2.getVisibility()==GONE && rgUmodo_1.getVisibility()==VISIBLE && rb1modo_13.isChecked() && rbUmodo_13.isChecked()))
            return true;

        return false;
    }

}
