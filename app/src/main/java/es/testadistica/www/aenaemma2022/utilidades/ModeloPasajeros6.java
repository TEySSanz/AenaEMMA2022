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

public class ModeloPasajeros6 extends Form {

    private int idCue;
    private int preguntaAnterior = 1;
    private int idAeropuerto;
    private int finCue = 60;
    private boolean resultValue;

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_TIME = "HH:mm";
    private Date fechaActual = Calendar.getInstance().getTime();
    private SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_SHORT);
    private SimpleDateFormat sdfTime = new SimpleDateFormat(DATE_FORMAT_TIME);

    public ModeloPasajeros6(Activity surveyAct, int pregunta, DBHelper conn) {

        super(surveyAct, pregunta, conn);
    }

    @Override
    public int getLayoutId() {

        return R.layout.form_modelpasajeros6;
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
        condicionesSpinners();
        condicionesRadioButton();
        condicionesChecks();
        condicionesRatingBar();
    }

    private void iniciarTextosAeropuertos(){
        activity.findViewById(R.id.survey_rl_numBus).setVisibility(VISIBLE);
        activity.findViewById(R.id.survey_rl_numComp).setVisibility(VISIBLE);
        activity.findViewById(R.id.survey_rl_numDarsena).setVisibility(VISIBLE);
        activity.findViewById(R.id.survey_rl_numVuelo).setVisibility(GONE);
        activity.findViewById(R.id.survey_rl_puertaEmbarque).setVisibility(GONE);
        //Por defecto se muestran las opciones del cuestionario de Madrid, si de algún aeropuerto se cambian los textos hay que incluirlo
        // en el switch

    }

    private void iniciarSpinners(){
        ArrayList<mListString> autobusAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAUTOBUS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> paisesAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES,"iden", "codigo","descripcion", "descripcion"));
        ArrayList<mListString> paises1y2Adapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPAISES1Y2,"iden", "codigo","zonas || ', ' || descripcion", "zonas || ', ' || descripcion"));
        ArrayList<mListString> provinciasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> provinciasAdapterEntautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> provinciasAdapterDesautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> municipiosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> municipiosAdapterEntautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        ArrayList<mListString> municipiosAdapterDesautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        String filtroAeropuerto1 = " ciudad like '%MAD%' ";
        ArrayList<mListString> distritosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPODISTRITOS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto1));
        String filtroAeropuerto2 = " ciudad like '%MAD%' ";
        ArrayList<mListString> distritosAdapterEntautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPODISTRITOS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto2));
        String filtroAeropuerto3 = " ciudad like '%MAD%' ";
        ArrayList<mListString> distritosAdapterDesautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPODISTRITOS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto3));
        String filtroAeropuerto = "iden IS NOT NULL"; //Para que salgan todos
        switch (idAeropuerto){
            case 14:
                //Autobuses
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_BUSOLEADA+"=1 ";
                break;
        }
        ArrayList<mListString> companiasAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "codigo",  filtroAeropuerto));

        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 14:
                //Autobuses
                filtroAeropuerto = " iden = 0 OR "+Contracts.COLUMN_TIPOCOMPANIAS_BUSAEREA+"=1 ";
                break;
        }
        ArrayList<mListString> companiasPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));
        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 14:
                //Autobuses
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_BUSOLEADA+" = 1";
                break;
        }
        ArrayList<mListString> tipoAeropuertosAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));
        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 14:
                //Autobuses
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_BUSOLEADA+" = 1";
                break;
        }
        ArrayList<mListString> tipoAeropuertosAdapter1 = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));

        filtroAeropuerto = " iden IS NOT NULL "; //Para que salgan todos
        switch (idAeropuerto){
            case 14:
                //Gran Canaria
                filtroAeropuerto = " "+Contracts.COLUMN_TIPOAEROPUERTOS_BUSPRINCIPAL+" = 1";
                break;
        }
        ArrayList<mListString> tipoAeropuertosPpalAdapter = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOAEROPUERTOS,"iden", "codigo","descripcion", "descripcion",  filtroAeropuerto));


        //P2
        //Asigna los valores del desplegable de companias
        SearchableSpinner sp_empresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);
        sp_empresa.setAdapter(autobusAdapter, 1, 1, activity.getString(R.string.spinner_autobus_title), activity.getString(R.string.spinner_close));

        sp_empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_empresa.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P3
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

        //P4
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

        //P5
        SearchableSpinner sp_destino;
        sp_destino = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_destino);
        sp_destino.setAdapter(tipoAeropuertosAdapter, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

        sp_destino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_destino.setBackgroundResource(android.R.drawable.btn_dropdown);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P6
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
        //P9
        SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);
        sp_cdlocaco_prov.setAdapter(provinciasAdapter, 1, 1, activity.getString(R.string.spinner_provincia_title), activity.getString(R.string.spinner_close));
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

        //P23
        SearchableSpinner sp_cdiaptoo;
        sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);
        sp_cdiaptoo.setAdapter(tipoAeropuertosAdapter1, 1, 1, activity.getString(R.string.spinner_tipoAeropuerto_title), activity.getString(R.string.spinner_close));

        sp_cdiaptoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P24
        //Asigna los valores del desplegable de companias
        SearchableSpinner sp_ciaantes;
        sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);
        sp_ciaantes.setAdapter(companiasAdapter, 1, 1, activity.getString(R.string.spinner_compania_title), activity.getString(R.string.spinner_close));

        sp_ciaantes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_ciaantes.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P39
        SearchableSpinner sp_entautobus_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus_prov);
        sp_entautobus_prov.setAdapter(provinciasAdapterEntautobus, 1, 1, activity.getString(R.string.spinner_provincia_title), activity.getString(R.string.spinner_close));
        /*sp_cdlocaco_prov.setAdapter(provinciasAdapter);
        sp_cdlocaco_prov.setTitle(activity.getString(R.string.spinner_provincia_title));
        sp_cdlocaco_prov.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_entautobus_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_entautobus_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_entautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus);
        sp_entautobus.setAdapter(municipiosAdapterEntautobus, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
        /*sp_cdlocaco.setAdapter(municipiosAdapter);
        sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
        sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_entautobus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_entautobus.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //P40
        SearchableSpinner sp_desautobus_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus_prov);
        sp_desautobus_prov.setAdapter(provinciasAdapterDesautobus, 1, 1, activity.getString(R.string.spinner_provincia_title), activity.getString(R.string.spinner_close));
        /*sp_cdlocaco_prov.setAdapter(provinciasAdapter);
        sp_cdlocaco_prov.setTitle(activity.getString(R.string.spinner_provincia_title));
        sp_cdlocaco_prov.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_desautobus_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_desautobus_prov.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SearchableSpinner sp_desautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus);
        sp_desautobus.setAdapter(municipiosAdapterDesautobus, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
        /*sp_cdlocaco.setAdapter(municipiosAdapter);
        sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
        sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));*/

        sp_desautobus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_desautobus.setBackgroundResource(android.R.drawable.btn_dropdown);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void condicionesSpinners() {
        //P2
        final SearchableSpinner sp_empresa_s1 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);

        sp_empresa_s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_empresa_s1.setBackgroundResource(android.R.drawable.btn_dropdown);
                String texto = getValorDesplegable(sp_empresa_s1).substring(0,3);

                if (!texto.equals("999")){
                    blanquearEditText(activity.findViewById(R.id.survey_edit_empresaotro_s1));
                    activity.findViewById(R.id.survey_layout_empresaotro_s1).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_empresaotro_s1).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P3
        final SearchableSpinner sp_cdpaisna = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);

        sp_cdpaisna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdpaisna.setBackgroundResource(android.R.drawable.btn_dropdown);
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
        //P4
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

                if ((!texto.equals("724"))){
                    activity.findViewById(R.id.survey_radio_viene_re_option1).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_radio_viene_re_option1).setVisibility(VISIBLE);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado_prov);

        //P4 Filtro municipios
        sp_cdlocado_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String filtro = " provincia IN (";

                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                }  else if (id > 9 && id <=57){
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
                sp_cdlocado_prov.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto = getValorDesplegable(sp_cdlocado_prov).substring(0,2);
                if ((texto.equals("07"))||(texto.equals("52"))||(texto.equals("94"))||(texto.equals("38"))||(texto.equals("35"))){
                    activity.findViewById(R.id.survey_radio_viene_re_option1).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_radio_viene_re_option1).setVisibility(VISIBLE);
                }
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

        //P5
        final SearchableSpinner sp_destino = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_destino);

        sp_destino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_destino.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto4 = getValorDesplegable(sp_destino).substring(0, 3);

                if(!texto4.equals("ZZZ"))

                {
                    blanquearEditText(activity.findViewById(R.id.survey_edit_destinoootro));
                    activity.findViewById(R.id.survey_layout_destinoootro).setVisibility(GONE);
                } else

                {
                    activity.findViewById(R.id.survey_layout_destinoootro).setVisibility(VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P6
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

        //P9 Filtro municipios
        final SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);

        sp_cdlocaco_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String filtro = " provincia IN (";

                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                }  else if (id > 9 && id <=57){
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

        //P23
        final SearchableSpinner sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);

        sp_cdiaptoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_cdiaptoo.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto4 = getValorDesplegable(sp_cdiaptoo).substring(0, 3);

                if(!texto4.equals("ZZZ"))

                {
                    blanquearEditText(activity.findViewById(R.id.survey_edit_cdiaptooootro));
                    activity.findViewById(R.id.survey_layout_cdiaptooootro).setVisibility(GONE);
                } else

                {
                    activity.findViewById(R.id.survey_layout_cdiaptooootro).setVisibility(VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P24
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

        //P39 Filtro municipios
        final SearchableSpinner sp_entautobus_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus_prov);

        sp_entautobus_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String filtro = " provincia IN (";

                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                }  else if (id > 9 && id <=57){
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

                /*final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
                ArrayAdapter<String> municipioAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                municipioAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
                sp_cdlocaco.setAdapter(municipioAdapter);
                sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
                sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));*/

                final SearchableSpinner sp_entautobus= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus);
                ArrayList<mListString> municipioAdapterEntautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                sp_entautobus.setAdapter(municipioAdapterEntautobus, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_entautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus);

        sp_entautobus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_entautobus.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_cdlocaco.getSelectedItem().toString().substring(0,5);
                String texto = getValorDesplegable(sp_entautobus).substring(0,5);
                /*String filtroAeropuerto = "28079";


                if (!texto.equals(filtroAeropuerto)){
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(GONE);
                    activity.findViewById(R.id.survey_layout_distracceotro).setVisibility(GONE);
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distracceotro));
                } else {
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(VISIBLE);
                }*/

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //P40 Filtro municipios
        final SearchableSpinner sp_desautobus_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus_prov);

        sp_desautobus_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String filtro = " provincia IN (";

                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                }  else if (id > 9 && id <=57){
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

                /*final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
                ArrayAdapter<String> municipioAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                municipioAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
                sp_cdlocaco.setAdapter(municipioAdapter);
                sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
                sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));*/

                final SearchableSpinner sp_desautobus= (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus);
                ArrayList<mListString> municipioAdapterDesautobus = new ArrayList<mListString>(getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                sp_desautobus.setAdapter(municipioAdapterDesautobus, 1, 1, activity.getString(R.string.spinner_municipio_title), activity.getString(R.string.spinner_close));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_desautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus);

        sp_desautobus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_desautobus.setBackgroundResource(android.R.drawable.btn_dropdown);
                //String texto = sp_cdlocaco.getSelectedItem().toString().substring(0,5);
                String texto = getValorDesplegable(sp_desautobus).substring(0,5);
                /*String filtroAeropuerto = "28079";


                if (!texto.equals(filtroAeropuerto)){
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(GONE);
                    activity.findViewById(R.id.survey_layout_distracceotro).setVisibility(GONE);
                    blanquearEditText(activity.findViewById(R.id.survey_edit_distracceotro));
                } else {
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(VISIBLE);
                }*/

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void condicionesRadioButton() {
        //P1
        final RadioGroup rgAutobus = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_autobus);
        rgAutobus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgAutobus.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
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
        final RadioGroup rgBustransfer = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_bustransfer);
        rgBustransfer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgBustransfer.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P11
        final RadioGroup rgCdmviaje = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdmviaje);
        rgCdmviaje.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdmviaje.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P12
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
        //P13
        final RadioGroup rgChekinb = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_chekinb);
        rgChekinb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgChekinb.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P14
        final RadioGroup rgCdsprof = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsprof);
        rgCdsprof.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsprof.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P15
        final RadioGroup rgEstudios = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_estudios);
        rgEstudios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgEstudios.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P16
        final RadioGroup rgCdedad = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedad);
        rgCdedad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdedad.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P17
        final RadioGroup rgCdsexo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsexo);
        rgCdsexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });
        //P29
        RadioGroup rgCdidavue = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdidavue);
        rgCdidavue.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rgCdidavue.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch (i) {
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
        //P33
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
        //P41
        final RadioGroup rgModo_s3 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s3);
        rgModo_s3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgModo_s3.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_modo_s3_option9:
                        activity.findViewById(R.id.survey_layout_modo_s3_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_modo_s3_otros).setVisibility(GONE);
                        break;
                }
            }
        });
        //P52
        final RadioGroup rgModo_s4 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s4);
        rgModo_s4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgModo_s4.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                switch(i)
                {
                    case R.id.survey_radio_modo_s4_option91:
                        activity.findViewById(R.id.survey_layout_modo_otros).setVisibility(VISIBLE);
                        break;
                    default:
                        activity.findViewById(R.id.survey_layout_modo_otros).setVisibility(GONE);
                        break;
                }
            }
        });
        //P1
        final RadioGroup rgSitiopark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);
        rgSitiopark.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup radioGroup, int i){
                rgSitiopark.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
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
            View view = activity.getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            pregunta = p;
            saveQuestion(p);
            hideQuestions();
            setPreguntaAnterior(p);
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
                //P1
                LinearLayout p1 = (LinearLayout) activity.findViewById(R.id.survey_layout_autobus);
                previo.setVisibility(GONE);
                save.setVisibility(GONE);
                p1.setVisibility(VISIBLE);
                break;
            case 2:
                //P2
                LinearLayout p2 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p2.setVisibility(VISIBLE);
                break;
            case 3:
                //P3
                LinearLayout p3 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisna);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p3.setVisibility(VISIBLE);
                break;
            case 4:
                //P4
                LinearLayout p4 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisre);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p4.setVisibility(VISIBLE);
                break;
            case 5:
                //P5
                LinearLayout p5 = (LinearLayout) activity.findViewById(R.id.survey_layout_destino);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p5.setVisibility(VISIBLE);
                break;
            case 6:
                //P6
                LinearLayout p6 = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p6.setVisibility(VISIBLE);
                break;
            case 7:
                //P7
                LinearLayout p7 = (LinearLayout) activity.findViewById(R.id.survey_layout_hllegabus);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p7.setVisibility(VISIBLE);
                break;
            case 8:
                //P8
                LinearLayout p8 = (LinearLayout) activity.findViewById(R.id.survey_layout_hsaleavion);
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
                LinearLayout p10 = (LinearLayout) activity.findViewById(R.id.survey_layout_bustransfer);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p10.setVisibility(VISIBLE);
                break;
            case 11:
                //P11
                LinearLayout p11 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p11.setVisibility(VISIBLE);
                break;
            case 12:
                //P12
                LinearLayout p12 = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p12.setVisibility(VISIBLE);
                break;
            case 13:
                //P13
                LinearLayout p13 = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p13.setVisibility(VISIBLE);
                break;
            case 14:
                //P14
                LinearLayout p14 = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p14.setVisibility(VISIBLE);
                break;
            case 15:
                //P15
                LinearLayout p15 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p15.setVisibility(VISIBLE);
                break;
            case 16:
                //P16
                LinearLayout p16 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p16.setVisibility(VISIBLE);
                break;
            case 17:
                //P17
                LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p17.setVisibility(VISIBLE);
                break;
            case 18:
                //P18
                LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p18.setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p19.setVisibility(VISIBLE);
                break;
            case 20:
                //P20
                LinearLayout p20 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisna);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p20.setVisibility(VISIBLE);
                break;
            case 21:
                //P21
                LinearLayout p21 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisre);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p21.setVisibility(VISIBLE);
                break;
            case 22:
                //P22
                LinearLayout p22 = (LinearLayout) activity.findViewById(R.id.survey_layout_desautobus_tot);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p22.setVisibility(VISIBLE);
                break;
            case 23:
                //P23
                LinearLayout p23 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptoo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p23.setVisibility(VISIBLE);
                break;
            case 24:
                //P24
                LinearLayout p24 = (LinearLayout) activity.findViewById(R.id.survey_layout_ciaantes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p24.setVisibility(VISIBLE);
                break;
            case 25:
                //P25
                LinearLayout p25 = (LinearLayout) activity.findViewById(R.id.survey_layout_hllegabus);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p25.setVisibility(VISIBLE);
                break;
            case 26:
                //P26
                LinearLayout p26 = (LinearLayout) activity.findViewById(R.id.survey_layout_hsalebus);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p26.setVisibility(VISIBLE);
                break;
            case 27:
                //P27
                LinearLayout p27 = (LinearLayout) activity.findViewById(R.id.survey_layout_bustransfer);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p27.setVisibility(VISIBLE);
                break;
            case 28:
                //P28
                LinearLayout p28 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p28.setVisibility(VISIBLE);
                break;
            case 29:
                //P29
                LinearLayout p29 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p29.setVisibility(VISIBLE);
                break;
            case 30:
                //P30
                LinearLayout p30 = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p30.setVisibility(VISIBLE);
                break;
            case 31:
                //P31
                LinearLayout p31 = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p31.setVisibility(VISIBLE);
                break;
            case 32:
                //P32
                LinearLayout p32 = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p32.setVisibility(VISIBLE);
                break;
            case 33:
                //P33
                LinearLayout p33 = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p33.setVisibility(VISIBLE);
                break;
            case 34:
                //P34
                LinearLayout p34 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p34.setVisibility(VISIBLE);
                break;

            case 35:
                //P35
                LinearLayout p35 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p35.setVisibility(VISIBLE);
                break;

            case 36:
                //P36
                LinearLayout p36 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p36.setVisibility(VISIBLE);
                break;

            case 37:
                //P37
                LinearLayout p37 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p37.setVisibility(VISIBLE);
                break;

            case 38:
                //P38
                LinearLayout p38 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p38.setVisibility(VISIBLE);
                break;

            case 39:
                //P39
                LinearLayout p39 = (LinearLayout) activity.findViewById(R.id.survey_layout_entautobus_tot);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p39.setVisibility(VISIBLE);
                break;

            case 40:
                //P40
                LinearLayout p40 = (LinearLayout) activity.findViewById(R.id.survey_layout_desautobus_tot);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p40.setVisibility(VISIBLE);
                break;

            case 41:
                //P41
                LinearLayout p41 = (LinearLayout) activity.findViewById(R.id.survey_layout_modo_s3);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p41.setVisibility(VISIBLE);
                break;

            case 42:
                //P42
                LinearLayout p42 = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p42.setVisibility(VISIBLE);
                break;

            case 43:
                //P43
                LinearLayout p43 = (LinearLayout) activity.findViewById(R.id.survey_layout_bustransfer);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p43.setVisibility(VISIBLE);
                break;

            case 44:
                //P44
                LinearLayout p44 = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p44.setVisibility(VISIBLE);
                break;

            case 45:
                //P45
                LinearLayout p45 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p45.setVisibility(VISIBLE);
                break;

            case 46:
                //P46
                LinearLayout p46 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p46.setVisibility(VISIBLE);
                break;

            case 47:
                //P47
                LinearLayout p47 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p47.setVisibility(VISIBLE);
                break;

            case 48:
                //P48
                LinearLayout p48 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p48.setVisibility(VISIBLE);
                break;

            case 49:
                //P49
                LinearLayout p49 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p49.setVisibility(VISIBLE);
                break;

            case 50:
                //P50
                LinearLayout p50 = (LinearLayout) activity.findViewById(R.id.survey_layout_entautobus_tot);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p50.setVisibility(VISIBLE);
                break;

            case 51:
                //P51
                LinearLayout p51 = (LinearLayout) activity.findViewById(R.id.survey_layout_desautobus_tot);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p51.setVisibility(VISIBLE);
                break;

            case 52:
                //P52
                LinearLayout p52 = (LinearLayout) activity.findViewById(R.id.survey_layout_modo_s4);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p52.setVisibility(VISIBLE);
                break;

            case 53:
                //P53
                LinearLayout p53 = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p53.setVisibility(VISIBLE);
                break;

            case 54:
                //P54
                LinearLayout p54 = (LinearLayout) activity.findViewById(R.id.survey_layout_bustransfer);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p54.setVisibility(VISIBLE);
                break;

            case 55:
                //P55
                LinearLayout p55 = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p55.setVisibility(VISIBLE);
                break;

            case 56:
                //P56
                LinearLayout p56 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p56.setVisibility(VISIBLE);
                break;

            case 57:
                //P57
                LinearLayout p57 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p57.setVisibility(VISIBLE);
                break;

            case 58:
                //P58
                LinearLayout p58 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p58.setVisibility(VISIBLE);
                break;

            case 59:
                //P59
                LinearLayout p59 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p59.setVisibility(VISIBLE);
                break;

            case 60:
                //P60
                LinearLayout p60 = (LinearLayout) activity.findViewById(R.id.survey_layout_valorexp);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(GONE);
                p60.setVisibility(VISIBLE);
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
                    //P1
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_autobus))) {
                        return false;
                    }
                    break;
                case 2: case 19: case 38: case 49:
                    //P2
                    final SearchableSpinner empresa_s1 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);

                    if (getValorDesplegable(empresa_s1).substring(0,3).equals("000")) {
                        empresa_s1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }
                    if (activity.findViewById(R.id.survey_layout_empresaotro_s1).getVisibility() == VISIBLE) {
                        EditText etEmpresaotro_s1 = (EditText) activity.findViewById(R.id.survey_edit_empresaotro_s1);
                        if (etEmpresaotro_s1.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etEmpresaotro_s1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etEmpresaotro_s1.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etEmpresaotro_s1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 3: case 20:
                    //P3
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
                case 4: case 21:
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
                    break;
                case 5:
                    final SearchableSpinner destino = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_destino);

                    if (getValorDesplegable(destino).substring(0,3).equals("000")) {
                        destino.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    if (activity.findViewById(R.id.survey_layout_destinoootro).getVisibility() == VISIBLE) {
                        EditText etDestino = (EditText) activity.findViewById(R.id.survey_edit_destinoootro);
                        if (etDestino.getText().toString().isEmpty()) {
                            String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                            etDestino.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etDestino.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etDestino.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        }
                    }
                    break;
                case 6:
                    //P6
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
                case 7: case 25:
                    //P7
                    EditText etHora = (EditText) activity.findViewById(R.id.survey_edit_hllegabus_hora);
                    EditText etMinuto = (EditText) activity.findViewById(R.id.survey_edit_hllegabus_minutos);
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
                        Date dateHllegabus = formatoFecha.parse(hora + ":" + minuto);

                        if(dateHllegabus.after(dateHoraEncuesta)){
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
                case 8:
                    //P8
                    EditText etHora1 = (EditText) activity.findViewById(R.id.survey_edit_hsaleavion_hora);
                    EditText etMinuto1 = (EditText) activity.findViewById(R.id.survey_edit_hsaleavion_minutos);
                    EditText etHoraEncuesta1 = (EditText) activity.findViewById(R.id.survey_edit_hora);
                    String hora1=etHora1.getText().toString();
                    String minuto1=etMinuto1.getText().toString();

                    if (hora1.isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etHora1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etHora1.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etHora1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (minuto1.isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etMinuto1.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if(!validarFecha(hora1,minuto1)){
                        String textoError = activity.getResources().getString(R.string.survey_text_error_date);
                        etHora1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etHora1.setError(textoError);
                        etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etMinuto1.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etHora1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    /*try {
                        //Formato de hora (hora/minuto)
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm");
                        //Comprobación de la HORA
                        formatoFecha.setLenient(false);
                        Date dateHoraEncuesta = formatoFecha.parse(etHoraEncuesta1.getText().toString());
                        Date dateHllegabus = formatoFecha.parse(hora1 + ":" + minuto1);

                        if(dateHllegabus.after(dateHoraEncuesta)){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_hllega);
                            etHora1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etHora1.setError(textoError);
                            etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etMinuto1.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etHora1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            etHora1.setError(null);
                            etMinuto1.setError(null);
                        }

                    } catch (ParseException e) {
                        //En el caso de que no se haya recogido por algún motivo la hora de inicio de la encuesta no se comprueba que sea posterior
                        //a la hora de llegada
                        Toast.makeText(null, activity.getResources().getString(R.string.survey_text_error_hora_hllega), Toast.LENGTH_LONG).show();
                    }*/
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

                    break;
                case 10: case 27: case 43: case 54:
                    //P10
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_bustransfer))) {
                        return false;
                    }
                    break;
                case 11: case 28:
                    //P11
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdmviaje))) {
                        return false;
                    }
                    break;
                case 12: case 30:
                    //P12
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
                case 13: case 31:
                    //P20
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
                case 14: case 32:
                    //P14
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_chekinb))) {
                        return false;
                    }
                    break;
                case 15: case 34: case 45: case 56:
                    //P15
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsprof))) {
                        return false;
                    }
                    break;
                case 16: case 35: case 46: case 57:
                    //P16
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_estudios))) {
                        return false;
                    }
                    break;
                case 17: case 36: case 47: case 58:
                    //P17
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdedad))) {
                        return false;
                    }
                    break;
                case 18: case 37: case 48: case 59:
                    //P18
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_cdsexo))) {
                        return false;
                    }
                    break;
                case 23:
                    //P23
                    final SearchableSpinner cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);

                    if (getValorDesplegable(cdiaptoo).substring(0,3).equals("000")) {
                        cdiaptoo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    if (activity.findViewById(R.id.survey_layout_cdiaptooootro).getVisibility() == VISIBLE) {
                        EditText etCdiaptoo = (EditText) activity.findViewById(R.id.survey_edit_cdiaptooootro);
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
                case 24:
                    //P24
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
                case 26:
                    //P26
                    EditText etHora2 = (EditText) activity.findViewById(R.id.survey_edit_hsalebus_hora);
                    EditText etMinuto2 = (EditText) activity.findViewById(R.id.survey_edit_hsalebus_minutos);
                    EditText etHoraEncuesta2 = (EditText) activity.findViewById(R.id.survey_edit_hora);
                    String hora2=etHora2.getText().toString();
                    String minuto2=etMinuto2.getText().toString();

                    if (hora2.isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etHora2.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etHora2.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etHora2.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (minuto2.isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etMinuto2.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etMinuto2.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etMinuto2.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if(!validarFecha(hora2,minuto2)){
                        String textoError = activity.getResources().getString(R.string.survey_text_error_date);
                        etHora2.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etHora2.setError(textoError);
                        etMinuto2.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etMinuto2.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etHora2.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                        etMinuto2.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    /*try {
                        //Formato de hora (hora/minuto)
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("HH:mm");
                        //Comprobación de la HORA
                        formatoFecha.setLenient(false);
                        Date dateHoraEncuesta = formatoFecha.parse(etHoraEncuesta1.getText().toString());
                        Date dateHllegabus = formatoFecha.parse(hora1 + ":" + minuto1);

                        if(dateHllegabus.after(dateHoraEncuesta)){
                            String textoError = activity.getResources().getString(R.string.survey_text_error_hllega);
                            etHora1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etHora1.setError(textoError);
                            etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                            etMinuto1.setError(textoError);
                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    textoError,
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        } else {
                            etHora1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            etMinuto1.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                            etHora1.setError(null);
                            etMinuto1.setError(null);
                        }

                    } catch (ParseException e) {
                        //En el caso de que no se haya recogido por algún motivo la hora de inicio de la encuesta no se comprueba que sea posterior
                        //a la hora de llegada
                        Toast.makeText(null, activity.getResources().getString(R.string.survey_text_error_hora_hllega), Toast.LENGTH_LONG).show();
                    }*/
                    break;
                case 29:
                    //P29
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
                /*//P18, hemos indicado vacaciones >7 días (293), P22 “duración del viaje tiene que ser superior a 7;
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
                }*/

                    break;
                case 33:
                    //P33
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
                case 39: case 50:
                    //P39
                    if (activity.findViewById(R.id.survey_layout_entautobus_tot).getVisibility() == VISIBLE) {
                        /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdlocaco), "00000")) {
                            return false;
                        }*/
                        final SearchableSpinner entautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus);

                        if (getValorDesplegable(entautobus).substring(0,5).equals("00000")) {
                            entautobus.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }

                    break;
                case 40: case 51: case 22:
                    //P40
                    if (activity.findViewById(R.id.survey_layout_desautobus_tot).getVisibility() == VISIBLE) {
                        /*if (!requeridoSearchableSpinner(activity.findViewById(R.id.survey_spinner_cdlocaco), "00000")) {
                            return false;
                        }*/
                        final SearchableSpinner desautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus);

                        if (getValorDesplegable(desautobus).substring(0,5).equals("00000")) {
                            desautobus.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                            return getDialogValueBackError(activity,
                                    activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                    activity.getResources().getString(R.string.survey_text_selectOption),
                                    activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                        }
                    }

                    break;
                case 41:
                    //P41
                    RadioGroup rgModo_s3 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s3);
                    EditText etModo_s3 = (EditText) activity.findViewById(R.id.survey_edit_modo_s3_otros);
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_modo_s3))) {
                        return false;
                    }
                    if (rgModo_s3.getCheckedRadioButtonId() == R.id.survey_radio_modo_s3_option9 &&
                            etModo_s3.getText().toString().isEmpty()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etModo_s3.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etModo_s3.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etModo_s3.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 42: case 53:
                    //P42
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_autobus))) {
                        return false;
                    }
                    break;
                case 44: case 55:
                    EditText etAcomptes = (EditText) activity.findViewById(R.id.survey_edit_acomptes);
                    if (etAcomptes.getText().toString().isEmpty()){
                        String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);
                        etAcomptes.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etAcomptes.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etAcomptes.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 52:
                    //P52
                    RadioGroup rgModo_s4 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s4);
                    EditText etModo_s4 = (EditText) activity.findViewById(R.id.survey_edit_modo_otros);
                    if (!requeridoRadioGroup(activity.findViewById(R.id.survey_radiogroup_modo_s4))) {
                        return false;
                    }
                    if (rgModo_s4.getCheckedRadioButtonId() == R.id.survey_radio_modo_s4_option91 &&
                            etModo_s4.getText().toString().isEmpty()) {
                        String textoError = activity.getResources().getString(R.string.survey_text_selectOption);
                        etModo_s4.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etModo_s4.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etModo_s4.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }
                    break;
                case 60:
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
                    EditText etNumComp = (EditText) activity.findViewById(R.id.survey_edit_numComp);
                    EditText etNumBus = (EditText) activity.findViewById(R.id.survey_edit_numBus);
                    EditText etNumDarsena = (EditText) activity.findViewById(R.id.survey_edit_numDarsena);
                    String textoError = activity.getResources().getString(R.string.survey_text_specifyAnswer);

                    if (etNumComp.getText().toString().isEmpty()) {
                        etNumComp.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNumComp.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNumComp.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (etNumBus.getText().toString().isEmpty()) {
                        etNumBus.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNumBus.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNumBus.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    if (etNumDarsena.getText().toString().isEmpty()) {
                        etNumDarsena.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));
                        etNumDarsena.setError(textoError);
                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                textoError,
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etNumDarsena.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMCOMP, String.valueOf(cue.getNumcomp()));
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMBUS, String.valueOf(cue.getNumbus()));
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMDARSENA, String.valueOf(cue.getNumdarsena()));
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
                    ///P1
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_SECCION, cue.getSeccion());
                    break;
                case 2: case 19: case 38: case 49:
                    //P2
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_EMPRESA, cue.getEmpresa());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_EMPRESAOTRO, cue.getEmpresaotro());
                    break;
                case 3: case 20:
                    //P3
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISNA, cue.getCdpaisna());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISNAOTRO, cue.getCdpaisnaotro());
                    break;
                case 4: case 21:
                    //P4
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISRE, cue.getCdpaisre());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISREOTRO, cue.getCdpaisreotro());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCADO, cue.getCdlocado());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRES, cue.getDistres());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRESOTRO, cue.getDistresotro());
                    break;
                case 5:
                    //P5
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DESTINO, cue.getDestino());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DESTINOOTRO, cue.getDestinootro());
                    break;
                case 6:
                    //P6
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA, cue.getNumvuepa());
                    break;
                case 7: case 25:
                    //P7
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGABUS, cue.getHllegabus());
                    break;
                case 8:
                    //P8
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HSALEAVION, cue.getHsaleavion());
                    break;
                case 9:
                    //P9
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VIEN_RE, cue.getVien_re());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACO, cue.getCdlocaco());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCE, cue.getDistracce());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCEOTRO, cue.getDistracceotro());
                    break;
                case 10: case 27: case 43: case 54:
                    //P10
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_BUSTRANSFER, cue.getBustransfer());
                    break;
                case 11: case 28:
                    //P11
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE, cue.getCdmviaje());
                    break;
                case 12: case 30:
                    //P12
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERS, String.valueOf(cue.getNpers()));
                    break;
                case 13: case 31:
                    //P13
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS, String.valueOf(cue.getNniños()));
                    break;
                case 14: case 32:
                    //P14
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB, String.valueOf(cue.getChekinb()));
                    break;
                case 15: case 34: case 45: case 56:
                    //P15
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF, cue.getCdsprof());
                    break;
                case 16: case 35: case 46: case 57:
                    //P16
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS, cue.getEstudios());
                    break;
                case 17: case 36: case 47: case 58:
                    //P17
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD, cue.getCdedad());
                    break;
                case 18: case 37: case 48: case 59:
                    //P18
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO, String.valueOf(cue.getCdsexo()));
                    break;
                case 23:
                    //P23
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO, cue.getCdiaptoo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO, cue.getCdiaptoootro());
                    break;
                case 24:
                    //P24
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CIA, cue.getCia());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CIAOTRO, cue.getCiaotro());
                    break;
                case 26:
                    //P26
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HSALEBUS, cue.getHsalebus());
                    break;
                case 29:
                    //P29
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE, cue.getCdidavue());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_TAUS, String.valueOf(cue.getTaus()));
                    break;
                case 33:
                    //P33
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE, cue.getNviaje());
                    break;
                case 39: case 50:
                    //P39
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ENTAUTOBUS, cue.getEntautobus());
                    break;
                case 40: case 51: case 22:
                    //P40
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DESAUTOBUS, cue.getDesautobus());
                    break;
                case 41:
                    //P41
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODO, cue.getModo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODOOTRO, cue.getModootro());
                    break;
                case 42: case 53:
                    //P42
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK, cue.getSitiopark());
                    break;
                case 44: case 55:
                    //P13
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES, String.valueOf(cue.getAcomptes()));
                    break;
                case 52:
                    //P52
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODO, cue.getModo());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODOOTRO, cue.getModootro());
                    break;
                case 60:
                    //P60
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
                case 1:
                    //P1
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_SECCION);
                    break;
                case 2: case 19: case 38: case 49:
                    //P2
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_EMPRESA);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_EMPRESAOTRO);
                    break;
                case 3: case 20:
                    //P3
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISNA);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISNAOTRO);
                    break;
                case 4: case 21:
                    //P4
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISRE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCADO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRES);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRESOTRO);
                    break;
                case 5:
                    //P5
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DESTINO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DESTINOOTRO);
                    break;
                case 6:
                    //P6
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA);
                    break;
                case 7: case 25:
                    //P7
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGABUS);
                    break;
                case 8:
                    //P8
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_HSALEAVION);
                    break;
                case 9:
                    //P9
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VIEN_RE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCACO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DISTRACCEOTRO);
                    break;
                case 10: case 27: case 43: case 54:
                    //P10
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_BUSTRANSFER);
                    break;
                case 11: case 28:
                    //P11
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE);
                    break;
                case 12: case 30:
                    //P12
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NPERS);
                    break;
                case 13: case 31:
                    //P13
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS);
                    break;
                case 14: case 32:
                    //P14
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB);
                    break;
                case 15: case 34: case 45: case 56:
                    //P15
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF);
                    break;
                case 16: case 35: case 46: case 57:
                    //P16
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS);
                    break;
                case 17: case 36: case 47: case 58:
                    //P17
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD);
                    break;
                case 18: case 37: case 48: case 59:
                    //P18
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO);
                    break;
                case 23:
                    //P23
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOOOTRO);
                    break;
                case 24:
                    //P24
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CIA);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CIAOTRO);
                    break;
                case 26:
                    //P26
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_HSALEBUS);
                    break;
                case 29:
                    //P29
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_TAUS);
                    break;
                case 33:
                    //P33
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE);
                    break;
                case 39: case 50:
                    //P39
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ENTAUTOBUS);
                    break;
                case 40: case 51: case 22:
                    //P40
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_DESAUTOBUS);
                    break;
                case 41:
                    //P41
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_MODO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_MODOOTRO);
                    break;
                case 42: case 53:
                    //P42
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK);
                    break;
                case 44: case 55:
                    //P13
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES);
                    break;
                case 52:
                    //P52
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_MODO);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_MODOOTRO);
                    break;
                case 60:
                    //P60
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_VALOREXP);
                    break;
            }
        }
        return true;
    }

    public void hideQuestions() {

        //P1
        LinearLayout autobus_s0 = (LinearLayout) activity.findViewById(R.id.survey_layout_autobus);
        autobus_s0.setVisibility(GONE);
        //P2
        LinearLayout empresa_s1 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
        empresa_s1.setVisibility(GONE);
        //P3
        LinearLayout cdpaisna = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisna);
        cdpaisna.setVisibility(GONE);
        //P4
        LinearLayout cdpaisre = (LinearLayout) activity.findViewById(R.id.survey_layout_cdpaisre);
        cdpaisre.setVisibility(GONE);
        //P5
        LinearLayout destino = (LinearLayout) activity.findViewById(R.id.survey_layout_destino);
        destino.setVisibility(GONE);
        //P6
        LinearLayout numvuepa = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
        numvuepa.setVisibility(GONE);
        //P7
        LinearLayout hllegabus = (LinearLayout) activity.findViewById(R.id.survey_layout_hllegabus);
        hllegabus.setVisibility(GONE);
        //P8
        LinearLayout hsaleavion = (LinearLayout) activity.findViewById(R.id.survey_layout_hsaleavion);
        hsaleavion.setVisibility(GONE);
        //P9
        LinearLayout viene_re = (LinearLayout) activity.findViewById(R.id.survey_layout_viene_re);
        viene_re.setVisibility(GONE);
        //P10
        LinearLayout bustransfer = (LinearLayout) activity.findViewById(R.id.survey_layout_bustransfer);
        bustransfer.setVisibility(GONE);
        //P11
        LinearLayout cdmviaje = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
        cdmviaje.setVisibility(GONE);
        //P12
        LinearLayout npers = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
        npers.setVisibility(GONE);
        //P13
        LinearLayout nniños = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
        nniños.setVisibility(GONE);
        //P14
        LinearLayout chekinb = (LinearLayout) activity.findViewById(R.id.survey_layout_chekinb);
        chekinb.setVisibility(GONE);
        //P15
        LinearLayout cdsprof = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
        cdsprof.setVisibility(GONE);
        //P16
        LinearLayout estudios = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
        estudios.setVisibility(GONE);
        //P17
        LinearLayout cdedad = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
        cdedad.setVisibility(GONE);
        //P18
        LinearLayout cdsexo = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsexo);
        cdsexo.setVisibility(GONE);
        //P23
        LinearLayout cdiaptoo = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptoo);
        cdiaptoo.setVisibility(GONE);
        //P24
        LinearLayout ciaantes = (LinearLayout) activity.findViewById(R.id.survey_layout_ciaantes);
        ciaantes.setVisibility(GONE);
        //P26
        LinearLayout hsalebus = (LinearLayout) activity.findViewById(R.id.survey_layout_hsalebus);
        hsalebus.setVisibility(GONE);
        //P29
        LinearLayout cdidavue_taus = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
        cdidavue_taus.setVisibility(GONE);
        //P33
        LinearLayout nviaje = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
        nviaje.setVisibility(GONE);
        //P39
        LinearLayout entautobus = (LinearLayout) activity.findViewById(R.id.survey_layout_entautobus_tot);
        entautobus.setVisibility(GONE);
        //P40
        LinearLayout desautobus = (LinearLayout) activity.findViewById(R.id.survey_layout_desautobus_tot);
        desautobus.setVisibility(GONE);
        //P40
        LinearLayout modo_s3 = (LinearLayout) activity.findViewById(R.id.survey_layout_modo_s3);
        modo_s3.setVisibility(GONE);
        //P40
        LinearLayout sitiopark = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
        sitiopark.setVisibility(GONE);
        //P40
        LinearLayout acomptes = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
        acomptes.setVisibility(GONE);
        //P52
        LinearLayout ultimomodo = (LinearLayout) activity.findViewById(R.id.survey_layout_modo_s4);
        ultimomodo.setVisibility(GONE);
        //P60
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
                //P1
                if (activated) {
                    RadioGroup rgBus = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_autobus);
                    checkedId = rgBus.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_autobus_option1:
                                show = showQuestion(2); //>P2
                                break;
                            case R.id.survey_radio_autobus_option2:

                                activity.findViewById(R.id.survey_text_empresa_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_empresa_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdpaisna_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdpaisna_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdpaisre_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdpaisre_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_hllegabus_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_hllegabus_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_bustrasnfer_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_bustrasnfer_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdmviaje_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdmviaje_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_npers_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_npers_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_nniños_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_nniños_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_chekinb_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_chekinb_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_radio_chekinb_option0).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdsprof_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdsprof_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_estudios_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_estudios_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdedad_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdedad_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdsexo_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdsexo_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_valorexp_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_valorexp_s2).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_desautobus_s3).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_desautobus_s2).setVisibility(VISIBLE);
                                show = showQuestion(19); //>P19
                                break;
                            case R.id.survey_radio_autobus_option3:
                                activity.findViewById(R.id.survey_text_empresa_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_empresa_s3).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_bustrasnfer_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_bustrasnfer_s3).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdsprof_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdsprof_s3).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_estudios_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_estudios_s3).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdedad_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdedad_s3).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdsexo_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdsexo_s3).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_valorexp_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_valorexp_s3).setVisibility(VISIBLE);
                                show = showQuestion(38); //>P38
                                break;
                            case R.id.survey_radio_autobus_option4:
                                activity.findViewById(R.id.survey_text_empresa_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_empresa_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_entautobus_s3).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_entautobus_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_desautobus_s3).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_desautobus_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_modo_s3).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_modo_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_sitiopark_s3).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_sitiopark_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_bustrasnfer_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_bustrasnfer_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_acomptes_s3).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_acomptes_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdsprof_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdsprof_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_estudios_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_estudios_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdedad_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdedad_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_cdsexo_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_cdsexo_s4).setVisibility(VISIBLE);
                                activity.findViewById(R.id.survey_text_valorexp_s1).setVisibility(GONE);
                                activity.findViewById(R.id.survey_text_valorexp_s4).setVisibility(VISIBLE);
                                show = showQuestion(49); //>P49
                                break;
                        }
                    } else {
                        show = showQuestion(2); //>P2
                    }
                } else {
                    show = showQuestion(2);
                }
                break;
            case 2:
                //P2
                show = showQuestion(3);
                break;
            case 3:
                //P3
                show = showQuestion(4);
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
                show = showQuestion(8);
                break;
            case 8:
                //P8
                show = showQuestion(9);
                break;
            case 9:
                //P9
                if (activated) {
                    RadioGroup rgVienere = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re);
                    checkedId = rgVienere.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_viene_re_option1:
                                show = showQuestion(10); //>P10
                                break;
                            case R.id.survey_radio_viene_re_option2:
                                show = showQuestion(10); //>P10
                                break;
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
                show = showQuestion(12);
                break;
            case 12:
                //P12
                if (activated) {
                    RadioGroup rgNpers = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_npers);
                    checkedId = rgNpers.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_npers_option1:
                                show = showQuestion(14); //>P14
                                break;
                            default:
                                show = showQuestion(13); //>P13
                                break;
                        }
                    } else {
                        show = showQuestion(13); //>P13
                    }
                } else {
                    show = showQuestion(13);
                }
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
                show = showQuestion(60);
                activity.findViewById(R.id.survey_text_valorexp_s1).setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                show = showQuestion(20);
                break;
            case 20:
                //P20
                show = showQuestion(21);
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
                    checkedId = rgNpers.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_npers_option1:
                                show = showQuestion(32); //>P32
                                break;
                            default:
                                show = showQuestion(31); //>P31
                                break;
                        }
                    } else {
                        show = showQuestion(31); //>P31
                    }
                } else {
                    show = showQuestion(31);
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
                show = showQuestion(37);
                break;
            case 37:
                //P37
                show = showQuestion(60);
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
                if (activated) {
                    RadioGroup rgModo_s3 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s3);
                    checkedId = rgModo_s3.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_modo_s3_option22:
                                show = showQuestion(42); //>P42
                                break;
                            default:
                                show = showQuestion(43); //>P43
                                break;
                        }
                    } else {
                        show = showQuestion(43); //>P43
                    }
                } else {
                    show = showQuestion(43);
                }
                break;
            case 42:
                //P42
                show = showQuestion(43);
                break;
            case 43:
                //P43
                show = showQuestion(44);
                break;
            case 44:
                //P44
                show = showQuestion(45);
                break;
            case 45:
                //P45
                show = showQuestion(46);
                break;
            case 46:
                //P46
                show = showQuestion(47);
                break;
            case 47:
                //P47
                show = showQuestion(48);
                break;
            case 48:
                //P48
                show = showQuestion(60);
                break;
            case 49:
                //P49
                show = showQuestion(50);
                break;
            case 50:
                //P50
                show = showQuestion(51);
                break;
            case 51:
                //P51
                show = showQuestion(52);
                break;
            case 52:
                //P52
                if (activated) {
                    RadioGroup rgModo_s4 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s4);
                    checkedId = rgModo_s4.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_modo_s4_option22:
                                show = showQuestion(53); //>P53
                                break;
                            case R.id.survey_radio_modo_s4_option23:
                                show = showQuestion(53); //>P53
                                break;
                            default:
                                show = showQuestion(54); //>P54
                                break;
                        }
                    } else {
                        show = showQuestion(54); //>P54
                    }
                } else {
                    show = showQuestion(54);
                }
                break;
            case 53:
                //P53
                show = showQuestion(54);
                break;
            case 54:
                //P54
                show = showQuestion(55);
                break;
            case 55:
                //P55
                show = showQuestion(56);
                break;
            case 56:
                //P56
                show = showQuestion(57);
                break;
            case 57:
                //P57
                show = showQuestion(58);
                break;
            case 58:
                //P58
                show = showQuestion(59);
                break;
            case 59:
                //P59
                show = showQuestion(60);
                break;
            case 60:
                //P60
                //FIN
                break;

        }
        return show;
    }

    public CuePasajeros fillQuest(CuePasajeros quest)  {
        int selectedCode = -1;
        int checkedId = -1;

        //CABECERA
        EditText etNumComp = (EditText) activity.findViewById(R.id.survey_edit_numComp);
        EditText etNumBus = (EditText) activity.findViewById(R.id.survey_edit_numBus);
        EditText etNumDarsena = (EditText) activity.findViewById(R.id.survey_edit_numDarsena);
        String stNumComp = etNumComp.getText().toString();
        String stNumBus = etNumBus.getText().toString();
        String stNumDarsena = etNumDarsena.getText().toString();
        //String stNumvueca = etCodCompVuelo.getText().toString()+"-"+etNnumVuelo.getText().toString();

        if (!etNumComp.getText().toString().isEmpty()) {
            quest.setNumcomp(stNumComp);
        }
        if (!etNumBus.getText().toString().isEmpty()) {
            quest.setNumbus(stNumBus);
        }
        if (!etNumDarsena.getText().toString().isEmpty()) {
            quest.setNumdarsena(stNumDarsena);
        }


        //Establece la fecha actual
        Calendar currentTime = Calendar.getInstance();
        Date fechaActual = currentTime.getTime();
        //Aplica el formato a la fecha
        SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_TIME);
        //Asigna la fecha a visualizar
        quest.setHoraFin(sdfDate.format(fechaActual));

        //P1
        RadioGroup rgAutobus = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_autobus);
        selectedCode = -1;

        checkedId = rgAutobus.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_autobus_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_autobus_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_autobus_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_autobus_option4:
                    selectedCode = 4;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setSeccion(String.valueOf(selectedCode));
        //P2
        SearchableSpinner sp_empresa_s1 = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);
        String textSpEmpresa_s1 = getValorDesplegable(sp_empresa_s1).substring(0,3);
        if(!textSpEmpresa_s1.contains("000")){
            quest.setEmpresa(textSpEmpresa_s1);
            if (textSpEmpresa_s1.contains("999")){
                EditText et_empresaotro_s1 = (EditText) activity.findViewById(R.id.survey_edit_empresaotro_s1);
                quest.setEmpresaotro(et_empresaotro_s1.getText().toString());
            } else {
                quest.setEmpresaotro("-1");
            }
        } else {
            quest.setEmpresa("-1");
        }
        //P3
        SearchableSpinner sp_cdpaisna = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);
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
        //P4
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

        //P5
        SearchableSpinner sp_destino = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_destino);
        String textSpDestino = getValorDesplegable(sp_destino).substring(0,3);
        if(!textSpDestino.contains("000")){
            quest.setDestino(textSpDestino);
            if (textSpDestino.contains("ZZZ")){
                EditText et_destinootro = (EditText) activity.findViewById(R.id.survey_edit_destinoootro);
                quest.setDestinootro(et_destinootro.getText().toString());
            } else {
                quest.setDestinootro("-1");
            }
        } else {
            quest.setDestino("-1");
        }
        //P6
        SearchableSpinner sp_numvuepa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_numvuepa);
        //String textSpNumvuepa = sp_numvuepa.getSelectedItem().toString().substring(0,3);
        String textSpNumvuepa = getValorDesplegable(sp_numvuepa).substring(0,3);
        EditText numvuepa = (EditText) activity.findViewById(R.id.survey_edit_numvuepa);
        if(!textSpNumvuepa.contains("000")){
            quest.setNumvuepa(textSpNumvuepa+"-"+numvuepa.getText().toString());
        } else {
            quest.setNumvuepa("-1");
        }
        //P7
        EditText hllegabus_hora = (EditText) activity.findViewById(R.id.survey_edit_hllegabus_hora);
        EditText hllegabus_minutos = (EditText) activity.findViewById(R.id.survey_edit_hllegabus_minutos);
        quest.setHllegabus(hllegabus_hora.getText().toString()+":"+hllegabus_minutos.getText().toString());
        //P8
        EditText hsaleavion_hora = (EditText) activity.findViewById(R.id.survey_edit_hsaleavion_hora);
        EditText hsaleavion_minutos = (EditText) activity.findViewById(R.id.survey_edit_hsaleavion_minutos);
        quest.setHsaleavion(hsaleavion_hora.getText().toString()+":"+hsaleavion_minutos.getText().toString());

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
                    if(!textSpDistracce.contains("00") && textSpCdlocaco.contains("28079")){
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
        RadioGroup rgBustransfer = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_bustransfer);
        selectedCode = -1;

        checkedId = rgBustransfer.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_bustransfer_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_bustransfer_option2:
                    selectedCode = 2;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setBustransfer(String.valueOf(selectedCode));
        //P11
        RadioGroup rgCdmviaje = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdmviaje);
        selectedCode = -1;

        checkedId = rgCdmviaje.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdmviaje_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdmviaje_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdmviaje_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_cdmviaje_option4:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdmviaje(String.valueOf(selectedCode));

        //P12
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

        //P13
        EditText etNniños= (EditText) activity.findViewById(R.id.survey_edit_nniños);
        quest.setNniños(stringToInt(etNniños.getText().toString()));

        //P14
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

        //P15
        RadioGroup rgCdsprof = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsprof);
        selectedCode = -1;

        checkedId = rgCdsprof.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdsprof_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdsprof_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdsprof_option3:
                    selectedCode = 3;
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

        //P16
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

        //P16
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
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdedad(String.valueOf(selectedCode));

        //P16
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
                    selectedCode = 99;
                    break;
            }
        }
        quest.setCdsexo(selectedCode);

        //P23
        SearchableSpinner sp_diaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);
        String textSpDiaptoo = getValorDesplegable(sp_diaptoo).substring(0,3);
        if(!textSpDiaptoo.contains("000")){
            quest.setCdiaptoo(textSpDiaptoo);
            if (textSpDiaptoo.contains("ZZZ")){
                EditText et_cdiaptoootro = (EditText) activity.findViewById(R.id.survey_edit_cdiaptooootro);
                quest.setCdiaptoootro(et_cdiaptoootro.getText().toString());
            } else {
                quest.setCdiaptoootro("-1");
            }
        } else {
            quest.setCdiaptoo("-1");
        }

        //P24
        SearchableSpinner sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);
        String textSpCiaantes = getValorDesplegable(sp_ciaantes).substring(0,3);
        if(!textSpCiaantes.contains("000")){
            quest.setCia(textSpCiaantes);
            if (textSpCiaantes.contains("999")){
                EditText et_ciaantesotro = (EditText) activity.findViewById(R.id.survey_edit_ciaantesotro);
                quest.setCiaotro(et_ciaantesotro.getText().toString());
            } else {
                quest.setCiaotro("-1");
            }
        } else {
            quest.setCia("-1");
        }

        //P26
        EditText hsalebus_hora = (EditText) activity.findViewById(R.id.survey_edit_hsalebus_hora);
        EditText hsalebus_minutos = (EditText) activity.findViewById(R.id.survey_edit_hsalebus_minutos);
        quest.setHsalebus(hsalebus_hora.getText().toString()+":"+hsalebus_minutos.getText().toString());

        //P29
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

        //P33
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

        //P39

        SearchableSpinner sp_entautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_entautobus);
        //String textSpCdlocaco = sp_cdlocaco.getSelectedItem().toString().substring(0,5);
        String textEntautobus = getValorDesplegable(sp_entautobus).substring(0,5);

        if(!textEntautobus.contains("00000")){
            quest.setEntautobus(textEntautobus);
        } else {
            quest.setEntautobus("-1");
        }

        //P40

        SearchableSpinner sp_desautobus = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_desautobus);
        //String textSpCdlocaco = sp_cdlocaco.getSelectedItem().toString().substring(0,5);
        String textDesautobus = getValorDesplegable(sp_desautobus).substring(0,5);

        if(!textDesautobus.contains("00000")){
            quest.setDesautobus(textDesautobus);
        } else {
            quest.setDesautobus("-1");
        }

        //P41
        RadioGroup rgModo_s3 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s3);
        EditText etModo_s3_otro = (EditText) activity.findViewById(R.id.survey_edit_modo_s3_otros);

        selectedCode = -1;
        quest.setModootro("-1");
        checkedId = rgModo_s3.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_modo_s3_option11:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_modo_s3_option24:
                    selectedCode = 24;
                    break;
                case R.id.survey_radio_modo_s3_option22:
                    selectedCode = 22;
                    break;
                case R.id.survey_radio_modo_s3_option37:
                    selectedCode = 37;
                    break;
                case R.id.survey_radio_modo_s3_option38:
                    selectedCode = 38;
                    break;
                case R.id.survey_radio_modo_s3_option42:
                    selectedCode = 42;
                    break;
                case R.id.survey_radio_modo_s3_option43:
                    selectedCode = 43;
                    break;
                case R.id.survey_radio_modo_s3_option9:
                    selectedCode = 9;
                    quest.setModootro(etModo_s3_otro.getText().toString());
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setModo(String.valueOf(selectedCode));

        //P42
        RadioGroup rgSitiopark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);
        selectedCode = -1;

        checkedId = rgSitiopark.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_sitiopark_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_sitiopark_option2:
                    selectedCode = 2;
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

        //P44
        EditText etAcomptes= (EditText) activity.findViewById(R.id.survey_edit_acomptes);
        quest.setAcomptes(stringToInt(etAcomptes.getText().toString()));

        //P52
        RadioGroup rgModo_s4 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modo_s4);
        EditText etModo_s4_otro = (EditText) activity.findViewById(R.id.survey_edit_modo_otros);

        selectedCode = -1;
        quest.setModootro("-1");
        checkedId = rgModo_s4.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_modo_s4_option11:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_modo_s4_option24:
                    selectedCode = 24;
                    break;
                case R.id.survey_radio_modo_s4_option25:
                    selectedCode = 25;
                    break;
                case R.id.survey_radio_modo_s4_option22:
                    selectedCode = 22;
                    break;
                case R.id.survey_radio_modo_s4_option23:
                    selectedCode = 23;
                    break;
                case R.id.survey_radio_modo_s4_option35:
                    selectedCode = 35;
                    break;
                case R.id.survey_radio_modo_s4_option37:
                    selectedCode = 37;
                    break;
                case R.id.survey_radio_modo_s4_option39:
                    selectedCode = 39;
                    break;
                case R.id.survey_radio_modo_s4_option38:
                    selectedCode = 38;
                    break;
                case R.id.survey_radio_modo_s4_option42:
                    selectedCode = 42;
                    break;
                case R.id.survey_radio_modo_s4_option43:
                    selectedCode = 43;
                    break;
                case R.id.survey_radio_modo_s4_option91:
                    selectedCode = 91;
                    quest.setModootro(etModo_s4_otro.getText().toString());
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }
        quest.setModo(String.valueOf(selectedCode));

        //P60
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
