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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CuePasajeros;

public class ModeloPasajeros1 extends Form {

    private int preguntaAnterior = 1;
    private int idCue;
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
        showQuestion(pregunta);

        iniciarSpinners();
        condicionesSpinners();
        condicionesRadioButton();
    }

    private void iniciarSpinners() {

        ArrayAdapter<String> paisesAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOPAISES,"iden", "codigo","descripcion", "codigo"));
        paisesAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
        ArrayAdapter<String> provinciasAdapter = new ArrayAdapter<String>(this.activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOPROVINCIAS,"iden", "codigo","descripcion", "codigo"));
        provinciasAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
        ArrayAdapter<String> municipiosAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        municipiosAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
        ArrayAdapter<String> distritosAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPODISTRITOS,"iden", "codigo","descripcion", "codigo", " ciudad like '%MAD%' "));
        distritosAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
        ArrayAdapter<String> companiasAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOCOMPANIAS,"iden", "codigo","descripcion", "codigo"));
        companiasAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        //P1
        //Asigna los valores del desplegable de idiomas
        SearchableSpinner sp_cdpasina;
        sp_cdpasina = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);
        sp_cdpasina.setAdapter(paisesAdapter);
        sp_cdpasina.setTitle(activity.getString(R.string.spinner_pais_title));
        sp_cdpasina.setPositiveButton(activity.getString(R.string.spinner_close));

        //P2
        //Asigna los valores del desplegable de paises
        SearchableSpinner sp_cdpasire;
        sp_cdpasire = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);
        sp_cdpasire.setAdapter(paisesAdapter);
        sp_cdpasire.setTitle(activity.getString(R.string.spinner_pais_title));
        sp_cdpasire.setPositiveButton(activity.getString(R.string.spinner_close));

        SearchableSpinner sp_cdlocado_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado_prov);
        sp_cdlocado_prov.setAdapter(provinciasAdapter);
        sp_cdlocado_prov.setTitle(activity.getString(R.string.spinner_provincia_title));
        sp_cdlocado_prov.setPositiveButton(activity.getString(R.string.spinner_close));

        SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

        sp_cdlocado.setAdapter(municipiosAdapter);
        sp_cdlocado.setTitle(activity.getString(R.string.spinner_municipio_title));
        sp_cdlocado.setPositiveButton(activity.getString(R.string.spinner_close));

        SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        sp_distres.setAdapter(distritosAdapter);
        sp_distres.setTitle(activity.getString(R.string.spinner_distrito_title));
        sp_distres.setPositiveButton(activity.getString(R.string.spinner_close));

        //P4
        //Asigna los valores del desplegable de paises
        SearchableSpinner sp_cdiaptoo;
        sp_cdiaptoo = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdiaptoo);
        sp_cdiaptoo.setAdapter(paisesAdapter);
        sp_cdiaptoo.setTitle(activity.getString(R.string.spinner_pais_title));
        sp_cdiaptoo.setPositiveButton(activity.getString(R.string.spinner_close));

        //P5
        //Asigna los valores del desplegable de companias
        SearchableSpinner sp_ciaantes;
        sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);
        sp_ciaantes.setAdapter(companiasAdapter);
        sp_ciaantes.setTitle(activity.getString(R.string.spinner_compania_title));
        sp_ciaantes.setPositiveButton(activity.getString(R.string.spinner_close));

        //P9
        SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);
        sp_cdlocaco_prov.setAdapter(provinciasAdapter);
        sp_cdlocaco_prov.setTitle(activity.getString(R.string.spinner_provincia_title));
        sp_cdlocaco_prov.setPositiveButton(activity.getString(R.string.spinner_close));

        SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
        sp_cdlocaco.setAdapter(municipiosAdapter);
        sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
        sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));

        SearchableSpinner sp_distracce = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distracce);
        sp_distracce.setAdapter(distritosAdapter);
        sp_distracce.setTitle(activity.getString(R.string.spinner_distrito_title));
        sp_distracce.setPositiveButton(activity.getString(R.string.spinner_close));
    }

    private void condicionesSpinners() {
        //P2
        final SearchableSpinner sp_cdpaisre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);

        sp_cdpaisre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String texto = sp_cdpaisre.getSelectedItem().toString().substring(0,3);

                if (!texto.equals("724")){
                    activity.findViewById(R.id.survey_layout_cdlocado_esp).setVisibility(GONE);
                    activity.findViewById(R.id.survey_layout_distres).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_cdlocado_esp).setVisibility(VISIBLE);
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
                } else if (id > 9 && id <52){
                    filtro =  filtro +"'00','"+id+"','99',";
                }
                else if (id == 53){
                    filtro =  filtro +"'99',";
                } else {
                    filtro = " (iden > -1 ";
                }

                if (!filtro.contains(")")) {
                    filtro = filtro.substring(0, filtro.length()-1);
                    filtro = filtro + ")";
                }

                //System.out.println(filtro);

                final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
                ArrayAdapter<String> municipioAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                municipioAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
                sp_cdlocado.setAdapter(municipioAdapter);
                sp_cdlocado.setTitle(activity.getString(R.string.spinner_municipio_title));
                sp_cdlocado.setPositiveButton(activity.getString(R.string.spinner_close));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

        sp_cdlocado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String texto = sp_cdlocado.getSelectedItem().toString().substring(0,5);

                if (!texto.equals("28079")){
                    activity.findViewById(R.id.survey_layout_distres).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_distres).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocaco_prov = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco_prov);

        //P9 Filtro municipios
        sp_cdlocaco_prov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String filtro = " provincia IN (";

                if (id > 0 && id <10){
                    filtro = filtro + "'00','0"+id+"','99',";
                } else if (id > 9 && id <52){
                    filtro =  filtro +"'00','"+id+"','99',";
                }
                else if (id == 53){
                    filtro =  filtro +"'99',";
                } else {
                    filtro = " (iden > -1 ";
                }

                if (!filtro.contains(")")) {
                    filtro = filtro.substring(0, filtro.length()-1);
                    filtro = filtro + ")";
                }               

                final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);
                ArrayAdapter<String> municipioAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "descripcion", filtro));
                municipioAdapter.setDropDownViewResource(R.layout.selection_spinner_item);
                sp_cdlocaco.setAdapter(municipioAdapter);
                sp_cdlocaco.setTitle(activity.getString(R.string.spinner_municipio_title));
                sp_cdlocaco.setPositiveButton(activity.getString(R.string.spinner_close));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final SearchableSpinner sp_cdlocaco = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocaco);

        sp_cdlocaco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String texto = sp_cdlocaco.getSelectedItem().toString().substring(0,5);

                if (!texto.equals("28079")){
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_layout_distracce).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void condicionesRadioButton() {
        //P9
        RadioGroup rgViene_re = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_viene_re);
        rgViene_re.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        //System.out.println("Entra "+i);
                        //System.out.println(R.id.survey_radio_viene_re_option2);
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
        RadioGroup rgCdalojin= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojin);
        rgCdalojin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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

        //P13
        RadioGroup rgSitiopark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);
        rgSitiopark.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
        //P15
        RadioGroup rgAcomptes= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_acomptes);
        rgAcomptes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
                //P1
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
                LinearLayout p10 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojin);
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
                LinearLayout p13 = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p13.setVisibility(VISIBLE);
                break;
            case 14:
                //P14
                LinearLayout p14 = (LinearLayout) activity.findViewById(R.id.survey_layout_bustermi);
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
                LinearLayout p16 = (LinearLayout) activity.findViewById(R.id.survey_layout_hllega);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p16.setVisibility(VISIBLE);
                break;
            /*case 17:
                //P17
                LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptod);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p17.setVisibility(VISIBLE);
                break;
            case 18:
                //P18
                LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_numvuepa);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p18.setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdterm);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p19.setVisibility(VISIBLE);
                break;
            case 20:
                //P20
                LinearLayout p20 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdiaptof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p20.setVisibility(VISIBLE);
                break;
            case 21:
                //P21
                LinearLayout p21 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdmviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p21.setVisibility(VISIBLE);
                break;
            case 22:
                //P22
                LinearLayout p22 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdidavue);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p22.setVisibility(VISIBLE);
                break;
            case 23:
                //P23
                LinearLayout p23 = (LinearLayout) activity.findViewById(R.id.survey_layout_npers);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p23.setVisibility(VISIBLE);
                break;
            case 24:
                //P24
                LinearLayout p24 = (LinearLayout) activity.findViewById(R.id.survey_layout_nniños);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p24.setVisibility(VISIBLE);
                break;
            case 25:
                //P25
                LinearLayout p25 = (LinearLayout) activity.findViewById(R.id.survey_layout_relacion);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p25.setVisibility(VISIBLE);
                break;.
            case 26:
                //P26
                LinearLayout p26 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdtreser);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p26.setVisibility(VISIBLE);
                break;
            case 27:
                //P27
                LinearLayout p27 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdbillet);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p27.setVisibility(VISIBLE);
                break;
            case 28:
                //P28
                LinearLayout p28 = (LinearLayout) activity.findViewById(R.id.survey_layout_nviaje);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p28.setVisibility(VISIBLE);
                break;
            case 29:
                //P29
                LinearLayout p29 = (LinearLayout) activity.findViewById(R.id.survey_layout_vol12mes);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p29.setVisibility(VISIBLE);
                break;
            case 30:
                //P30
                LinearLayout p30 = (LinearLayout) activity.findViewById(R.id.survey_layout_eleccovid);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p30.setVisibility(VISIBLE);
                break;
            case 31:
                //P31
                LinearLayout p31 = (LinearLayout) activity.findViewById(R.id.survey_layout_p44factu);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p31.setVisibility(VISIBLE);
                break;
            case 32:
                //P32
                LinearLayout p32 = (LinearLayout) activity.findViewById(R.id.survey_layout_nperbul);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p32.setVisibility(VISIBLE);
                break;
            case 33:
                //P33
                LinearLayout p33 = (LinearLayout) activity.findViewById(R.id.survey_layout_dropoff);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p33.setVisibility(VISIBLE);
                break;
            case 34:
                //P34
                LinearLayout p34 = (LinearLayout) activity.findViewById(R.id.survey_layout_checkinb);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p34.setVisibility(VISIBLE);
                break;
            case 35:
                //P35
                LinearLayout p35 = (LinearLayout) activity.findViewById(R.id.survey_layout_consume);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p35.setVisibility(VISIBLE);
                break;
            case 36:
                //P36
                LinearLayout p36 = (LinearLayout) activity.findViewById(R.id.survey_layout_comprart);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p36.setVisibility(VISIBLE);
                break;
            case 37:
                //P37
                LinearLayout p37 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdslab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p37.setVisibility(VISIBLE);
                break;
            case 38:
                //P38
                LinearLayout p38 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdsprof);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p38.setVisibility(VISIBLE);
                break;
            case 39:
                //P39
                LinearLayout p39 = (LinearLayout) activity.findViewById(R.id.survey_layout_estudios);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p39.setVisibility(VISIBLE);
                break;
            case 40:
                //P40
                LinearLayout p40 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdedad);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p40.setVisibility(VISIBLE);
                break;  */
            case 41:
                //FIN
                next.setVisibility(GONE);
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
                    /*RadioButton rbB1_1 = (RadioButton) activity.findViewById(R.id.survey_model_radio_B1_option1);
                    RadioButton rbB1_6 = (RadioButton) activity.findViewById(R.id.survey_model_radio_B1_option6);

                    if (!rbB1_1.isChecked() && !rbB1_6.isChecked()) {
                        Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.survey_text_selectOption), Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }*/
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
                    break;
                case 2:
                    //P2
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDPAISRE, cue.getCdpaisre());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDLOCADO, cue.getCdlocado());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DISTRES, cue.getDistres());
                    break;
                case 3:
                    //P3
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDCAMBIO, cue.getCdcambio());
                    break;
                case 4:
                    //P4
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOO, cue.getCdiaptoo());
                    break;
                case 5:
                    //P5
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTES, cue.getCiaantes());
                    break;
                case 6:
                    //P6
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTES, cue.getConexfac());
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
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODO1, cue.getModo1());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_MODO2, cue.getModo2());
                    break;
                case 13:
                    //P13
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK, cue.getSitiopark());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PQFUERA, cue.getPqfuera());
                    break;
                case 14:
                    //P14
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_BUSTERMI, String.valueOf(cue.getBustermi()));
                    break;
                case 15:
                    //P15
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES, String.valueOf(cue.getAcomptes()));
                    break;
                case 16:
                    //P16
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGA, cue.getHllega());
                    break;
                case 17:
                    //P17
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD, cue.getCdiaptod());
                    break;
                case 18:
                    //P18
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA, cue.getNumvuepa());
                    break;
                case 19:
                    //P19
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDTERM, cue.getCdterm());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR, cue.getCdociaar());
                    break;
                case 20:
                    //P20
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF, cue.getCdiaptof());
                    break;
                case 21:
                    //P21
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE, cue.getCdmviaje());
                    break;
                case 22:
                    //P22
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE, cue.getCdidavue());
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_TAUS, cue.getTaus());
                    break;
                case 23:
                    //P23
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERS, cue.getNpers());
                    break;
                case 24:
                    //P24
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS, cue.getNniños());
                    break;
                case 25:
                    //P25
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_RELACION, cue.getRelacion());
                    break;
                case 26:
                    //P26
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDTRESER, cue.getCdtreser());
                    break;
                case 27:
                    //P27
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDBILLET, cue.getCdbillet());
                    break;
                case 28:
                    //P28
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NVIAJE, cue.getNviaje());
                    break;
                case 29:
                    //P29
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_VOL12MES, cue.getVol12mes());
                    break;
                case 30:
                    //P30
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ELECCOVID, cue.getEleccovid());
                    break;
                case 31:
                    //P31
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_P44FACTU, cue.getP44factu());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_BULGRUPO, cue.getBulgrupo());
                    break;
                case 32:
                    //P32
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_NPERBUL, cue.getNperbul());
                    break;
                case 33:
                    //P33
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_DROPOFF, cue.getDropoff());
                    break;
                case 34:
                    //P34
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_CHEKINB, cue.getChekinb());
                    break;
                case 35:
                    //P35
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CONSUME, cue.getConsume());
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_GAS_CONS, cue.getGas_cons());
                    break;
                case 36:
                    //P36
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_COMPRART, cue.getComprart());
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_GAS_COM, cue.getGas_com());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD1, cue.getProd1());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD2, cue.getProd2());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD3, cue.getProd3());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD4, cue.getProd4());
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_PROD5, cue.getProd5());
                    break;
                case 37:
                    //P37
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSLAB, cue.getCdslab());
                    break;
                case 38:
                    //P38
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSPROF, cue.getCdsprof());
                    break;
                case 39:
                    //P39
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_ESTUDIOS, cue.getEstudios());
                    break;
                case 40:
                    //P40
                    guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDEDAD, cue.getCdedad());
                    break;
                case 41:
                    //P41
                    //guardaDB(Contracts.COLUMN_CUEPASAJEROS_CDSEXO, cue.getCdsexo());
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
                    break;
                case 5:
                    //P5
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CIAANTES);
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
                    break;
                case 13:
                    //P13
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_SITIOPARK);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_PQFUERA);
                    break;
                case 14:
                    //P14
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_BUSTERMI);
                    break;
                case 15:
                    //P15
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_ACOMPTES);
                    break;
                case 16:
                    //P16
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_HLLEGA);
                    break;
                case 17:
                    //P17
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOD);
                    break;
                case 18:
                    //P18
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NUMVUEPA);
                    break;
                case 19:
                    //P19
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDTERM);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDOCIAAR);
                    break;
                case 20:
                    //P20
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIAPTOF);
                    break;
                case 21:
                    //P21
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDMVIAJE);
                    break;
                case 22:
                    //P22
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_CDIDAVUE);
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_TAUS);
                    break;
                case 23:
                    //P23
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NPERS);
                    break;
                case 24:
                    //P24
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_NNIÑOS);
                    break;
                case 25:
                    //P25
                    borraDB(Contracts.COLUMN_CUEPASAJEROS_RELACION);
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
        LinearLayout cdalojin = (LinearLayout) activity.findViewById(R.id.survey_layout_cdalojin);
        cdalojin.setVisibility(GONE);

        //P11
        LinearLayout nmodos = (LinearLayout) activity.findViewById(R.id.survey_layout_nmodos);
        nmodos.setVisibility(GONE);

        //P12
        LinearLayout ultimomodo = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo);
        ultimomodo.setVisibility(GONE);

        //P13
        LinearLayout sitiopark = (LinearLayout) activity.findViewById(R.id.survey_layout_sitiopark);
        sitiopark.setVisibility(GONE);

        //P14
        LinearLayout bustermi = (LinearLayout) activity.findViewById(R.id.survey_layout_bustermi);
        bustermi.setVisibility(GONE);

        //P15
        LinearLayout acomptes = (LinearLayout) activity.findViewById(R.id.survey_layout_acomptes);
        acomptes.setVisibility(GONE);

        //P16
        LinearLayout hllega = (LinearLayout) activity.findViewById(R.id.survey_layout_hllega);
        hllega.setVisibility(GONE);

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
                //P1
                show = showQuestion(2);
                break;
            case 2:
                //P2
                show = showQuestion(3);
                break;
            case 3:
                //P3
                show = showQuestion(4); //>P4
                //show = showQuestion(X); //>P9
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
                show = showQuestion(8); //>P8
                //show = showQuestion(16); //>P16
                break;
            case 8:
                //P8
                show = showQuestion(9); //>P9
                //show = showQuestion(16); //>P16
                break;
            case 9:
                //P9
                show = showQuestion(10); //>P10
                //show = showQuestion(11); //>P11
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
                            break;
                        case R.id.survey_radio_nmodos_option2:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(GONE);
                            break;
                        case R.id.survey_radio_nmodos_option3:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(VISIBLE);
                            break;
                    }
                }

                show = showQuestion(12);
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
                show = showQuestion(19);
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

        }

        return show;
    }

    public CuePasajeros fillQuest(CuePasajeros quest) {
        int selectedCode = -1;
        int checkedId = -1;

        //P1
        SearchableSpinner sp_cdpaisna = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisna);
        String textSpCdpaisna = sp_cdpaisna.getSelectedItem().toString().substring(0,3);
        if(!textSpCdpaisna.contains("000")){
            quest.setCdpaisna(textSpCdpaisna);
        } else {
            quest.setCdpaisna("-1");
        }

        //P2
        SearchableSpinner sp_cdpaisre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdpaisre);
        String textSpCdpaisre = sp_cdpaisre.getSelectedItem().toString().substring(0,3);
        if(!textSpCdpaisre.contains("000")){
            quest.setCdpaisre(textSpCdpaisre);
        } else {
            quest.setCdpaisre("-1");
        }

        SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
        String textSpCdlocado = sp_cdlocado.getSelectedItem().toString().substring(0,5);
        if(!textSpCdlocado.contains("00000")){
            quest.setCdlocado(textSpCdlocado);
        } else {
            quest.setCdlocado("-1");
        }

        SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        String textSpDistres = sp_distres.getSelectedItem().toString().substring(0,2);
        if(!textSpDistres.contains("00") && textSpCdlocado.contains("28079")){
            quest.setDistres(textSpDistres);
        } else {
            quest.setDistres("-1");
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
        String textSpCdiaptoo = sp_cdiaptoo.getSelectedItem().toString().substring(0,3);
        if(!textSpCdiaptoo.contains("000")){
            quest.setCdiaptoo(textSpCdiaptoo);
        } else {
            quest.setCdiaptoo("-1");
        }

        //P5
        SearchableSpinner sp_ciaantes = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_ciaantes);
        String textSpCiaantes = sp_ciaantes.getSelectedItem().toString().substring(0,3);
        if(!textSpCiaantes.contains("000")){
            quest.setCiaantes(textSpCiaantes);
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
        String textSpCdlocaco = sp_cdlocaco.getSelectedItem().toString().substring(0,5);

        SearchableSpinner sp_distracce = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distracce);
        String textSpDistracce = sp_distracce.getSelectedItem().toString().substring(0,2);

        selectedCode = -1;
        checkedId = rgViene_re.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_viene_re_option1:
                    selectedCode = 1;
                    quest.setCdlocaco("-1");
                    quest.setDistracce("-1");
                    break;
                case R.id.survey_radio_viene_re_option2:
                    if(!textSpCdlocaco.contains("00000")){
                        quest.setCdlocaco(textSpCdlocaco);
                    } else {
                        quest.setCdlocaco("-1");
                    }
                    if(!textSpDistracce.contains("00") && textSpCdpaisre.contains("28079")){
                        quest.setDistracce(textSpDistracce);
                    } else {
                        quest.setDistracce("-1");
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
        RadioGroup rgCdalojin = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdalojin);
        TextInputEditText etCdalojin_otros = (TextInputEditText) activity.findViewById(R.id.survey_edit_cdalojin_otros);

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
                    selectedCode = Integer.valueOf(etNmodos_otros.getText().toString());
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

        selectedCode = -1;
        checkedId = rgUltimodo_umodo.getCheckedRadioButtonId();

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
                    selectedCode = 91;
                    break;
                default:
                    selectedCode = 0;
                    break;
            }
        }
        quest.setUltimodo(String.valueOf(selectedCode));

        selectedCode = -1;
        if (quest.getNmodos().equals("2") || quest.getNmodos().equals("3")) {
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
        if (quest.getNmodos().equals("3")) {
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
        RadioGroup rgSitioPark = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_sitiopark);

        selectedCode = -1;
        checkedId = rgSitioPark.getCheckedRadioButtonId();
        quest.setPqfuera("-1");

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_sitiopark_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_sitiopark_option4:
                    TextInputEditText etPqfuera = (TextInputEditText) activity.findViewById(R.id.survey_edit_pqfuera);
                    System.out.println(etPqfuera.getText().toString());
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


        //P14
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
                    selectedCode = Integer.valueOf(acomptes_especificar.getText().toString());
                    break;
                default:
                    selectedCode = 9999;
                    break;
            }
        }
        quest.setAcomptes(selectedCode);

        //P16
        EditText hllega_hora = (EditText) activity.findViewById(R.id.survey_edit_hllega_hora);
        EditText hllega_minutos = (EditText) activity.findViewById(R.id.survey_edit_hllega_minutos);

        quest.setHllega(hllega_hora.getText().toString()+":"+hllega_minutos.getText().toString());



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

    private List<String> getDiccionario(String tabla, String campoIden, String campoCod, String campoValor, String campoOrden) {
        List<String> getDiccionario = new ArrayList<String>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + campoIden + ", " + campoCod + "||'. '|| " + campoValor +
                " FROM " + tabla + " AS T1 ORDER BY " + campoOrden , parametros);

        while (cursor.moveToNext()) {
            getDiccionario.add(cursor.getString(1));
        }

        return getDiccionario;
    }

    private List<String> getDiccionario(String tabla, String campoIden, String campoCod, String campoValor, String campoOrden, String filtro) {
        List<String> getDiccionario = new ArrayList<String>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + campoIden + ", " + campoCod + "||'. '|| " + campoValor +
                " FROM " + tabla + " AS T1" +
                " WHERE " + filtro + " ORDER BY " + campoOrden , parametros);

        while (cursor.moveToNext()) {
            getDiccionario.add(cursor.getString(1));
        }

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


}
