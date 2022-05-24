package es.testadistica.www.aenaemma2022.utilidades;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;

public class ModeloTrabajadores1 extends FormTrab {

    private static final String TAG = "ModeloTrabajadores1";
    private int preguntaAnterior = 1;
    private int idCue;
    private int finCue = 28;
    private boolean resultValue;

    private static String DATE_FORMAT_SHORT = "dd/MM/yyyy";
    private static String DATE_FORMAT_TIME = "HH:mm";
    private Date fechaActual = Calendar.getInstance().getTime();
    private SimpleDateFormat sdfDate = new SimpleDateFormat(DATE_FORMAT_SHORT);
    private SimpleDateFormat sdfTime = new SimpleDateFormat(DATE_FORMAT_TIME);

    public ModeloTrabajadores1(Activity surveyAct, int pregunta, DBHelper conn) {

        super(surveyAct, pregunta, conn);
    }

    @Override
    public int getLayoutId() {
        return R.layout.form_modeltrabajadores1;
    }

    @Override
    public void initFormView() {

        idCue = cue.getIden();
        showQuestion(pregunta);

        iniciarSpinners();
        condicionesSpinners();
        condicionesRadioButton();
        iniciarTimePickers();
        iniciarCheckBox();
    }

    private void iniciarSpinners() {

        //P1
        ArrayAdapter<String> empresasAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionarioNoCod(Contracts.TABLE_TIPOEMPRESATRAB,"iden", "codigo","descripcion", "descripcion", " idAeropuerto IN (0," + cue.getIdAeropuerto() + ")"));
        empresasAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_empresa;
        survey_spinner_empresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);
        survey_spinner_empresa.setAdapter(empresasAdapter);
        survey_spinner_empresa.setTitle(activity.getString(R.string.survey_text_empresa));
        survey_spinner_empresa.setPositiveButton(activity.getString(R.string.spinner_close));

        //P2
        ArrayAdapter<String> actempreAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOACTEMPTRAB,"iden", "codigo","descripcion", "codigo"));
        actempreAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_actempre;
        survey_spinner_actempre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_actempre);
        survey_spinner_actempre.setAdapter(actempreAdapter);
        survey_spinner_actempre.setTitle(activity.getString(R.string.survey_text_actempre));
        survey_spinner_actempre.setPositiveButton(activity.getString(R.string.spinner_close));

        //P3
        //ArrayAdapter<String> cdlocadoAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        ArrayAdapter<String> cdlocadoAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario("28"));
        cdlocadoAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_cdlocado;
        survey_spinner_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
        survey_spinner_cdlocado.setAdapter(cdlocadoAdapter);
        survey_spinner_cdlocado.setTitle(activity.getString(R.string.survey_text_cdlocado_loc));
        survey_spinner_cdlocado.setPositiveButton(activity.getString(R.string.spinner_close));

        ArrayAdapter<String> distresAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOBARRIOS,"iden", "distrito||codigo","descripcion", "distrito||codigo", " idAeropuerto IN (0," + cue.getIdAeropuerto() + ")"));
        distresAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_distres;
        survey_spinner_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        survey_spinner_distres.setAdapter(distresAdapter);
        survey_spinner_distres.setTitle(activity.getString(R.string.survey_text_distres));
        survey_spinner_distres.setPositiveButton(activity.getString(R.string.spinner_close));
    }

    private void condicionesSpinners() {

        //P1
        final SearchableSpinner sp_empresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);

        sp_empresa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_empresa.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto = sp_empresa.getSelectedItem().toString();

                if (!texto.equals("OTROS")){
                    activity.findViewById(R.id.survey_edit_empresa).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_edit_empresa).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P2
        final SearchableSpinner sp_actempre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_actempre);

        sp_actempre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_actempre.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto = sp_actempre.getSelectedItem().toString().substring(0,3);

                if (!texto.equals("024")){
                    activity.findViewById(R.id.survey_edit_actempre).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_edit_actempre).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //P3
        final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

        sp_cdlocado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sp_cdlocado.setBackgroundResource(android.R.drawable.btn_dropdown);

                String texto = sp_cdlocado.getSelectedItem().toString().substring(0,5);

                if (!texto.equals("28079")){
                    activity.findViewById(R.id.survey_model_layout_distres).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_model_layout_distres).setVisibility(VISIBLE);
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

                String texto = sp_distres.getSelectedItem().toString().substring(0,5);

                if (!texto.equals("99999")){
                    activity.findViewById(R.id.survey_distres_otros).setVisibility(GONE);
                    activity.findViewById(R.id.survey_edit_distres_otros).setVisibility(GONE);
                } else {
                    activity.findViewById(R.id.survey_distres_otros).setVisibility(VISIBLE);
                    activity.findViewById(R.id.survey_edit_distres_otros).setVisibility(VISIBLE);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void condicionesRadioButton() {

        //P4
        final RadioGroup rgJornada = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_jornada);
        final RadioButton rbJornada_1 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option1);
        final RadioButton rbJornada_2 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option2);
        final RadioButton rbJornada_3 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option3);
        final RadioButton rbJornada_4 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option4);
        final RadioButton rbJornada_5 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option5);
        final RadioButton rbJornada_6 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option6);
        final RadioButton rbJornada_9 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option9);

        rbJornada_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(GONE);
            }
        });

        rbJornada_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(GONE);
            }
        });

        rbJornada_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(VISIBLE);
            }
        });

        rbJornada_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(GONE);
            }
        });

        rbJornada_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(VISIBLE);
            }
        });

        rbJornada_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(VISIBLE);
            }
        });

        rbJornada_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_layout_horaent_option2).setVisibility(VISIBLE);
                activity.findViewById(R.id.survey_layout_horaent_option3).setVisibility(VISIBLE);
            }
        });

        //P5
        final RadioGroup rgNdiastrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ndiastrab);
        final RadioButton rbNdiastrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ndiastrab_option1);
        final RadioButton rbNdiastrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ndiastrab_option2);
        final RadioButton rbNdiastrab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ndiastrab_option3);

        rbNdiastrab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNdiastrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbNdiastrab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNdiastrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbNdiastrab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNdiastrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P6
        final RadioGroup rgZonatrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_zonatrab);
        final RadioButton rbZonatrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option1);
        final RadioButton rbZonatrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option2);
        final RadioButton rbZonatrab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option3);
        final RadioButton rbZonatrab_4 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option4);
        final RadioButton rbZonatrab_5 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option5);
        final RadioButton rbZonatrab_6 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option6);

        rbZonatrab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbZonatrab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbZonatrab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbZonatrab_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbZonatrab_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbZonatrab_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P8
        final RadioGroup rgNmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
        final RadioButton rbNmodos_1 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_option1);
        final RadioButton rbNmodos_2 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_option2);
        final RadioButton rbNmodos_3 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_nmodos);

        rbNmodos_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNmodos.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_nmodos_otros).setVisibility(GONE);
            }
        });

        rbNmodos_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNmodos.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_nmodos_otros).setVisibility(GONE);
            }
        });

        rbNmodos_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNmodos.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_nmodos_otros).setVisibility(VISIBLE);
            }
        });

        //P9
        final RadioGroup rgUmodo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_umodo);
        final RadioButton rbUmodo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option1);
        final RadioButton rbUmodo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option2);
        final RadioButton rbUmodo_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option3);
        final RadioButton rbUmodo_4 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option4);
        final RadioButton rbUmodo_5 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option5);
        final RadioButton rbUmodo_6 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option6);
        final RadioButton rbUmodo_7 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option7);
        final RadioButton rbUmodo_8 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option8);
        final RadioButton rbUmodo_9 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option9);
        final RadioButton rbUmodo_10 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option10);
        final RadioButton rbUmodo_11 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option11);
        final RadioButton rbUmodo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option13);

        rbUmodo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        rbUmodo_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.md_blue_grey_200));
            }
        });

        //P10
        final RadioGroup rgNocucoche = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nocucoche);
        final RadioButton rbNocucoche_1 = (RadioButton) activity.findViewById(R.id.survey_radio_nocucoche_option1);
        final RadioButton rbNocucoche_2 = (RadioButton) activity.findViewById(R.id.survey_radio_nocucoche_option2);
        final RadioButton rbNocucoche_3 = (RadioButton) activity.findViewById(R.id.survey_radio_nocucoche_option3);

        rbNocucoche_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNocucoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbNocucoche_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNocucoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbNocucoche_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgNocucoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P11
        final RadioGroup rgSatistranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_satistranspubli);
        final RadioButton rbSatistranspubli_1 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option1);
        final RadioButton rbSatistranspubli_2 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option2);
        final RadioButton rbSatistranspubli_3 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option3);
        final RadioButton rbSatistranspubli_4 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option4);
        final RadioButton rbSatistranspubli_5 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option5);
        final RadioButton rbSatistranspubli_9 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option9);

        rbSatistranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbSatistranspubli_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbSatistranspubli_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbSatistranspubli_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbSatistranspubli_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbSatistranspubli_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P15
        final RadioGroup rgAparctrab= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_aparctrab);
        final RadioButton rbAparctrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option1);
        final RadioButton rbAparctrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option2);
        final RadioButton rbAparctrab_3= (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option3);
        final RadioButton rbAparctrab_4 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option4);
        final RadioButton rbAparctrab_9 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option9);

        rbAparctrab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbAparctrab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbAparctrab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbAparctrab_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbAparctrab_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P17
        final RadioGroup rgDisptranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_disptranspubli);
        final RadioButton rbDisptranspubli_1 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option1);
        final RadioButton rbDisptranspubli_2 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option2);
        final RadioButton rbDisptranspubli_3 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option3);
        final RadioButton rbDisptranspubli_4 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option4);
        final RadioButton rbDisptranspubli_5 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option5);
        final RadioButton rbDisptranspubli_6 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option6);
        final RadioButton rbDisptranspubli_7 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option7);

        rbDisptranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(GONE);
            }
        });

        rbDisptranspubli_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(GONE);
            }
        });

        rbDisptranspubli_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(GONE);
            }
        });

        rbDisptranspubli_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(GONE);
            }
        });

        rbDisptranspubli_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(GONE);
            }
        });

        rbDisptranspubli_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(GONE);
            }
        });

        rbDisptranspubli_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_edit_disptranspubli_otros).setVisibility(VISIBLE);
            }
        });

        //P23
        final RadioGroup rgModosalida = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modosalida);
        final RadioButton rbModosalida_1 = (RadioButton) activity.findViewById(R.id.survey_radio_modosalida_option1);
        final RadioButton rbModosalida_2 = (RadioButton) activity.findViewById(R.id.survey_radio_modosalida_option2);

        rbModosalida_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgModosalida.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_layout_modosalida_indique).setVisibility(GONE);
            }
        });

        rbModosalida_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgModosalida.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                activity.findViewById(R.id.survey_layout_modosalida_indique).setVisibility(VISIBLE);
            }
        });

        //P24
        final RadioGroup rgCdedadtrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedadtrab);
        final RadioButton rbCdedadtrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option1);
        final RadioButton rbCdedadtrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option2);
        final RadioButton rbCdedadtrab_3= (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option3);
        final RadioButton rbCdedadtrab_4 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option4);

        rbCdedadtrab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdedadtrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbCdedadtrab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdedadtrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbCdedadtrab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdedadtrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbCdedadtrab_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdedadtrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P25
        final RadioGroup rgCdsexo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsexo);
        final RadioButton rbCdsexo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_cdsexo_option1);
        final RadioButton rbCdsexo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_cdsexo_option2);

        rbCdsexo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbCdsexo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P26
        final RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
        final RadioButton rbCdslab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_cdslab_option1);
        final RadioButton rbCdslab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_cdslab_option2);
        final RadioButton rbCdslab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_cdslab_option3);

        rbCdslab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbCdslab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbCdslab_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        //P27
        final RadioGroup rgPuesto = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_puesto);
        final RadioButton rbPuesto_1 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option1);
        final RadioButton rbPuesto_2 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option2);
        final RadioButton rbPuesto_3 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option3);
        final RadioButton rbPuesto_4 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option4);

        rbPuesto_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPuesto.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbPuesto_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPuesto.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbPuesto_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPuesto.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

        rbPuesto_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPuesto.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
            }
        });

    }

    private void iniciarTimePickers(){
        //P7
        TimePicker tpHoraent1 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent1);
        TimePicker tpHorasal1 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal1);
        TimePicker tpHoraent2 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent2);
        TimePicker tpHorasal2 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal2);
        TimePicker tpHoraent3 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent3);
        TimePicker tpHorasal3 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal3);

        tpHoraent1.setIs24HourView(true);
        tpHorasal1.setIs24HourView(true);
        tpHoraent2.setIs24HourView(true);
        tpHorasal2.setIs24HourView(true);
        tpHoraent3.setIs24HourView(true);
        tpHorasal3.setIs24HourView(true);

        setTimePickerInterval(tpHoraent1);
        setTimePickerInterval(tpHorasal1);
        setTimePickerInterval(tpHoraent2);
        setTimePickerInterval(tpHorasal2);
        setTimePickerInterval(tpHoraent3);
        setTimePickerInterval(tpHorasal3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tpHoraent1.setHour(0);
            tpHoraent1.setMinute(0);
            tpHorasal1.setHour(0);
            tpHorasal1.setMinute(0);
            tpHoraent2.setHour(0);
            tpHoraent2.setMinute(0);
            tpHorasal2.setHour(0);
            tpHorasal2.setMinute(0);
            tpHoraent3.setHour(0);
            tpHoraent3.setMinute(0);
            tpHorasal3.setHour(0);
            tpHorasal3.setMinute(0);
        } else {
            tpHoraent1.setCurrentHour(0);
            tpHoraent1.setCurrentMinute(0);
            tpHorasal1.setCurrentHour(0);
            tpHorasal1.setCurrentMinute(0);
            tpHoraent2.setCurrentHour(0);
            tpHoraent2.setCurrentMinute(0);
            tpHorasal2.setCurrentHour(0);
            tpHorasal2.setCurrentMinute(0);
            tpHoraent3.setCurrentHour(0);
            tpHoraent3.setCurrentMinute(0);
            tpHorasal3.setCurrentHour(0);
            tpHorasal3.setCurrentMinute(0);
        }
    }

    private void iniciarCheckBox(){

        //P12
        final LinearLayout llValtranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_valtranspubli);
        final CheckBox cbValtranspubli_1 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option1);
        final CheckBox cbValtranspubli_2 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option2);
        final CheckBox cbValtranspubli_3 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option3);
        final CheckBox cbValtranspubli_4 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option4);
        final CheckBox cbValtranspubli_5 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option5);
        final CheckBox cbValtranspubli_6 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option6);
        final CheckBox cbValtranspubli_7 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option7);
        final CheckBox cbValtranspubli_8 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option8);
        final CheckBox cbValtranspubli_9 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option9);
        final EditText etValtranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli);

        final ArrayList<String> listValtranspubli = new ArrayList<String>();

        final EditText etValtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli1);
        final EditText etValtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli2);
        final EditText etValtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli3);

        cbValtranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_1.setChecked(false);
                }

                if(cbValtranspubli_1.isChecked()) {
                    listValtranspubli.add("1");
                } else {
                    listValtranspubli.remove("1");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_2.setChecked(false);
                }

                if(cbValtranspubli_2.isChecked()) {
                    listValtranspubli.add("2");
                } else {
                    listValtranspubli.remove("2");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_3.setChecked(false);
                }

                if(cbValtranspubli_3.isChecked()) {
                    listValtranspubli.add("3");
                } else {
                    listValtranspubli.remove("3");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_4.setChecked(false);
                }

                if(cbValtranspubli_4.isChecked()) {
                    listValtranspubli.add("4");
                } else {
                    listValtranspubli.remove("4");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_5.setChecked(false);
                }

                if(cbValtranspubli_5.isChecked()) {
                    listValtranspubli.add("5");
                } else {
                    listValtranspubli.remove("5");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_6.setChecked(false);
                }

                if(cbValtranspubli_6.isChecked()) {
                    listValtranspubli.add("6");
                } else {
                    listValtranspubli.remove("6");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_7.setChecked(false);
                }

                if(cbValtranspubli_7.isChecked()) {
                    listValtranspubli.add("7");
                } else {
                    listValtranspubli.remove("7");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_8.setChecked(false);
                }

                if(cbValtranspubli_8.isChecked()) {
                    listValtranspubli.add("8");
                } else {
                    listValtranspubli.remove("8");
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        cbValtranspubli_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_9.setChecked(false);
                }

                if(cbValtranspubli_9.isChecked()) {
                    listValtranspubli.add("9");
                    etValtranspubli_otros.setVisibility(VISIBLE);
                } else {
                    listValtranspubli.remove("9");
                    etValtranspubli_otros.setVisibility(GONE);
                }

                if (listValtranspubli.size() == 1) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                } else if (listValtranspubli.size() == 2) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText("");
                } else if (listValtranspubli.size() == 3) {
                    etValtranspubli_1.setText(listValtranspubli.get(0));
                    etValtranspubli_2.setText(listValtranspubli.get(1));
                    etValtranspubli_2.setText(listValtranspubli.get(2));
                } else {
                    etValtranspubli_1.setText("");
                    etValtranspubli_2.setText("");
                    etValtranspubli_3.setText("");
                }
            }
        });

        //P13
        final LinearLayout llMejtranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_mejtranspubli);
        final CheckBox cbMejtranspubli_1 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option1);
        final CheckBox cbMejtranspubli_2 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option2);
        final CheckBox cbMejtranspubli_3 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option3);
        final CheckBox cbMejtranspubli_4 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option4);
        final CheckBox cbMejtranspubli_5 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option5);
        final CheckBox cbMejtranspubli_6 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option6);
        final CheckBox cbMejtranspubli_9 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option9);
        final EditText etMejtranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli);

        final ArrayList<String> listMejtranspubli = new ArrayList<String>();

        final EditText etMejtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli1);
        final EditText etMejtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli2);
        final EditText etMejtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli3);

        cbMejtranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_1.setChecked(false);
                }

                if(cbMejtranspubli_1.isChecked()) {
                    listMejtranspubli.add("1");
                } else {
                    listMejtranspubli.remove("1");
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        cbMejtranspubli_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_2.setChecked(false);
                }

                if(cbMejtranspubli_2.isChecked()) {
                    listMejtranspubli.add("2");
                } else {
                    listMejtranspubli.remove("2");
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        cbMejtranspubli_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_3.setChecked(false);
                }

                if(cbMejtranspubli_3.isChecked()) {
                    listMejtranspubli.add("3");
                } else {
                    listMejtranspubli.remove("3");
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        cbMejtranspubli_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_4.setChecked(false);
                }

                if(cbMejtranspubli_4.isChecked()) {
                    listMejtranspubli.add("4");
                } else {
                    listMejtranspubli.remove("4");
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        cbMejtranspubli_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_5.setChecked(false);
                }

                if(cbMejtranspubli_5.isChecked()) {
                    listMejtranspubli.add("5");
                } else {
                    listMejtranspubli.remove("5");
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        cbMejtranspubli_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_6.setChecked(false);
                }

                if(cbMejtranspubli_6.isChecked()) {
                    listMejtranspubli.add("6");
                } else {
                    listMejtranspubli.remove("6");
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        cbMejtranspubli_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_9.setChecked(false);
                }

                if(cbMejtranspubli_9.isChecked()) {
                    listMejtranspubli.add("9");
                    etMejtranspubli_otros.setVisibility(VISIBLE);
                } else {
                    listMejtranspubli.remove("9");
                    etMejtranspubli_otros.setVisibility(GONE);
                }

                if (listMejtranspubli.size() == 1) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 2) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText("");
                } else if (listMejtranspubli.size() == 3) {
                    etMejtranspubli_1.setText(listMejtranspubli.get(0));
                    etMejtranspubli_2.setText(listMejtranspubli.get(1));
                    etMejtranspubli_3.setText(listMejtranspubli.get(2));
                } else {
                    etMejtranspubli_1.setText("");
                    etMejtranspubli_2.setText("");
                    etMejtranspubli_3.setText("");
                }
            }
        });

        //P16
        final LinearLayout llNotranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_notranspubli);
        final CheckBox cbNotranspubli_1 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option1);
        final CheckBox cbNotranspubli_2 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option2);
        final CheckBox cbNotranspubli_3 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option3);
        final CheckBox cbNotranspubli_4 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option4);
        final CheckBox cbNotranspubli_5 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option5);
        final CheckBox cbNotranspubli_6 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option6);
        final CheckBox cbNotranspubli_7 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option7);
        final CheckBox cbNotranspubli_8 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option8);
        final CheckBox cbNotranspubli_9 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option9);
        final CheckBox cbNotranspubli_10 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option10);
        final EditText etNotranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_notranspubli);

        final ArrayList<String> listNotranspubli = new ArrayList<String>();

        final EditText etNotranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli1);
        final EditText etNotranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli2);
        final EditText etNotranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli3);

        cbNotranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_1.setChecked(false);
                }

                if(cbNotranspubli_1.isChecked()) {
                    listNotranspubli.add("1");
                } else {
                    listNotranspubli.remove("1");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_2.setChecked(false);
                }

                if(cbNotranspubli_2.isChecked()) {
                    listNotranspubli.add("2");
                } else {
                    listNotranspubli.remove("2");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_3.setChecked(false);
                }

                if(cbNotranspubli_3.isChecked()) {
                    listNotranspubli.add("3");
                } else {
                    listNotranspubli.remove("3");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_4.setChecked(false);
                }

                if(cbNotranspubli_4.isChecked()) {
                    listNotranspubli.add("4");
                } else {
                    listNotranspubli.remove("4");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_5.setChecked(false);
                }

                if(cbNotranspubli_5.isChecked()) {
                    listNotranspubli.add("5");
                } else {
                    listNotranspubli.remove("5");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_6.setChecked(false);
                }

                if(cbNotranspubli_6.isChecked()) {
                    listNotranspubli.add("6");
                } else {
                    listNotranspubli.remove("6");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_7.setChecked(false);
                }

                if(cbNotranspubli_7.isChecked()) {
                    listNotranspubli.add("7");
                } else {
                    listNotranspubli.remove("7");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_8.setChecked(false);
                }

                if(cbNotranspubli_8.isChecked()) {
                    listNotranspubli.add("8");
                } else {
                    listNotranspubli.remove("8");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_9.setChecked(false);
                }

                if(cbNotranspubli_9.isChecked()) {
                    listNotranspubli.add("9");
                } else {
                    listNotranspubli.remove("9");
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        cbNotranspubli_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_10.setChecked(false);
                }

                if(cbNotranspubli_10.isChecked()) {
                    listNotranspubli.add("10");
                    etNotranspubli_otros.setVisibility(VISIBLE);
                } else {
                    listNotranspubli.remove("10");
                    etNotranspubli_otros.setVisibility(GONE);
                }

                if (listNotranspubli.size() == 1) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 2) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText("");
                } else if (listNotranspubli.size() == 3) {
                    etNotranspubli_1.setText(listNotranspubli.get(0));
                    etNotranspubli_2.setText(listNotranspubli.get(1));
                    etNotranspubli_3.setText(listNotranspubli.get(2));
                } else {
                    etNotranspubli_1.setText("");
                    etNotranspubli_2.setText("");
                    etNotranspubli_3.setText("");
                }
            }
        });

        //P19
        final LinearLayout lMedtranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_medtranspubli);
        final CheckBox cbMedtranspubli_7 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option7);
        final CheckBox cbMedtranspubli_8 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option8);
        final CheckBox cbMedtranspubli_10 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option10);
        final CheckBox cbMedtranspubli_11 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option11);
        final CheckBox cbMedtranspubli_99 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option99);
        final EditText etMedtranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli);

        final ArrayList<String> listMedtranspubli = new ArrayList<String>();

        final EditText etMedtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli1);
        final EditText etMedtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli2);
        final EditText etMedtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli3);

        cbMedtranspubli_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMedtranspubli_7.isChecked()? 1:0) + (cbMedtranspubli_8.isChecked()? 1:0) + (cbMedtranspubli_10.isChecked()? 1:0) + (cbMedtranspubli_11.isChecked()? 1:0) +
                        (cbMedtranspubli_99.isChecked()? 1:0);

                if (count > 3){
                    cbMedtranspubli_7.setChecked(false);
                }

                if(cbMedtranspubli_7.isChecked()) {
                    listMedtranspubli.add("7");
                } else {
                    listMedtranspubli.remove("7");
                }

                if (listMedtranspubli.size() == 1) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 2) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 3) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText(listMedtranspubli.get(2));
                } else {
                    etMedtranspubli_1.setText("");
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                }
            }
        });

        cbMedtranspubli_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMedtranspubli_7.isChecked()? 1:0) + (cbMedtranspubli_8.isChecked()? 1:0) + (cbMedtranspubli_10.isChecked()? 1:0) + (cbMedtranspubli_11.isChecked()? 1:0) +
                        (cbMedtranspubli_99.isChecked()? 1:0);

                if (count > 3){
                    cbMedtranspubli_8.setChecked(false);
                }

                if(cbMedtranspubli_8.isChecked()) {
                    listMedtranspubli.add("8");
                } else {
                    listMedtranspubli.remove("8");
                }

                if (listMedtranspubli.size() == 1) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 2) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 3) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText(listMedtranspubli.get(2));
                } else {
                    etMedtranspubli_1.setText("");
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                }
            }
        });

        cbMedtranspubli_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMedtranspubli_7.isChecked()? 1:0) + (cbMedtranspubli_8.isChecked()? 1:0) + (cbMedtranspubli_10.isChecked()? 1:0) + (cbMedtranspubli_11.isChecked()? 1:0) +
                        (cbMedtranspubli_99.isChecked()? 1:0);

                if (count > 3){
                    cbMedtranspubli_10.setChecked(false);
                }

                if(cbMedtranspubli_10.isChecked()) {
                    listMedtranspubli.add("10");
                } else {
                    listMedtranspubli.remove("10");
                }

                if (listMedtranspubli.size() == 1) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 2) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 3) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText(listMedtranspubli.get(2));
                } else {
                    etMedtranspubli_1.setText("");
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                }
            }
        });

        cbMedtranspubli_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMedtranspubli_7.isChecked()? 1:0) + (cbMedtranspubli_8.isChecked()? 1:0) + (cbMedtranspubli_10.isChecked()? 1:0) + (cbMedtranspubli_11.isChecked()? 1:0) +
                        (cbMedtranspubli_99.isChecked()? 1:0);

                if (count > 3){
                    cbMedtranspubli_11.setChecked(false);
                }

                if(cbMedtranspubli_11.isChecked()) {
                    listMedtranspubli.add("11");
                } else {
                    listMedtranspubli.remove("11");
                }

                if (listMedtranspubli.size() == 1) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 2) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 3) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText(listMedtranspubli.get(2));
                } else {
                    etMedtranspubli_1.setText("");
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                }
            }
        });

        cbMedtranspubli_99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbMedtranspubli_7.isChecked()? 1:0) + (cbMedtranspubli_8.isChecked()? 1:0) + (cbMedtranspubli_10.isChecked()? 1:0) + (cbMedtranspubli_11.isChecked()? 1:0) +
                        (cbMedtranspubli_99.isChecked()? 1:0);

                if (count > 3){
                    cbMedtranspubli_99.setChecked(false);
                }

                if(cbMedtranspubli_99.isChecked()) {
                    listMedtranspubli.add("99");
                    etMedtranspubli_otros.setVisibility(VISIBLE);
                } else {
                    listMedtranspubli.remove("99");
                    etMedtranspubli_otros.setVisibility(GONE);
                }

                if (listMedtranspubli.size() == 1) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 2) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText("");
                } else if (listMedtranspubli.size() == 3) {
                    etMedtranspubli_1.setText(listMedtranspubli.get(0));
                    etMedtranspubli_2.setText(listMedtranspubli.get(1));
                    etMedtranspubli_3.setText(listMedtranspubli.get(2));
                } else {
                    etMedtranspubli_1.setText("");
                    etMedtranspubli_2.setText("");
                    etMedtranspubli_3.setText("");
                }
            }
        });

        //P21
        final LinearLayout lCompartcoche = (LinearLayout) activity.findViewById(R.id.survey_layout_check_compartcoche);
        final CheckBox cbCompartcoche_0 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option0);
        final CheckBox cbCompartcoche_1 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option1);
        final CheckBox cbCompartcoche_2 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option2);
        final CheckBox cbCompartcoche_3 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option3);
        final CheckBox cbCompartcoche_4 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option4);
        final CheckBox cbCompartcoche_5 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option5);
        final EditText etCompartcoche = (EditText) activity.findViewById(R.id.survey_edit_compartcoche);

        final ArrayList<String> listCompartcoche = new ArrayList<String>();

        final EditText etCompartcoche_1 = (EditText) activity.findViewById(R.id.survey_edit_compartcoche1);
        final EditText etCompartcoche_2 = (EditText) activity.findViewById(R.id.survey_edit_compartcoche2);
        final EditText etCompartcoche_3 = (EditText) activity.findViewById(R.id.survey_edit_compartcoche3);

        cbCompartcoche_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbCompartcoche_0.isChecked()? 1:0) + (cbCompartcoche_1.isChecked()? 1:0) + (cbCompartcoche_2.isChecked()? 1:0) + (cbCompartcoche_3.isChecked()? 1:0) +
                        (cbCompartcoche_4.isChecked()? 1:0) + (cbCompartcoche_5.isChecked()? 1:0);

                if (count > 3){
                    cbCompartcoche_0.setChecked(false);
                }

                if(cbCompartcoche_0.isChecked()) {
                    listCompartcoche.add("0");
                } else {
                    listCompartcoche.remove("0");
                }

                if (listCompartcoche.size() == 1) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 2) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 3) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText(listCompartcoche.get(2));
                } else {
                    etCompartcoche_1.setText("");
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                }
            }
        });

        cbCompartcoche_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbCompartcoche_0.isChecked()? 1:0) + (cbCompartcoche_1.isChecked()? 1:0) + (cbCompartcoche_2.isChecked()? 1:0) + (cbCompartcoche_3.isChecked()? 1:0) +
                        (cbCompartcoche_4.isChecked()? 1:0) + (cbCompartcoche_5.isChecked()? 1:0);

                if (count > 3){
                    cbCompartcoche_1.setChecked(false);
                }

                if(cbCompartcoche_1.isChecked()) {
                    listCompartcoche.add("1");
                } else {
                    listCompartcoche.remove("1");
                }

                if (listCompartcoche.size() == 1) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 2) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 3) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText(listCompartcoche.get(2));
                } else {
                    etCompartcoche_1.setText("");
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                }
            }
        });

        cbCompartcoche_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbCompartcoche_0.isChecked()? 1:0) + (cbCompartcoche_1.isChecked()? 1:0) + (cbCompartcoche_2.isChecked()? 1:0) + (cbCompartcoche_3.isChecked()? 1:0) +
                        (cbCompartcoche_4.isChecked()? 1:0) + (cbCompartcoche_5.isChecked()? 1:0);

                if (count > 3){
                    cbCompartcoche_2.setChecked(false);
                }

                if(cbCompartcoche_2.isChecked()) {
                    listCompartcoche.add("2");
                } else {
                    listCompartcoche.remove("2");
                }

                if (listCompartcoche.size() == 1) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 2) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 3) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText(listCompartcoche.get(2));
                } else {
                    etCompartcoche_1.setText("");
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                }
            }
        });

        cbCompartcoche_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbCompartcoche_0.isChecked()? 1:0) + (cbCompartcoche_1.isChecked()? 1:0) + (cbCompartcoche_2.isChecked()? 1:0) + (cbCompartcoche_3.isChecked()? 1:0) +
                        (cbCompartcoche_4.isChecked()? 1:0) + (cbCompartcoche_5.isChecked()? 1:0);

                if (count > 3){
                    cbCompartcoche_3.setChecked(false);
                }

                if(cbCompartcoche_3.isChecked()) {
                    listCompartcoche.add("3");
                } else {
                    listCompartcoche.remove("3");
                }

                if (listCompartcoche.size() == 1) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 2) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 3) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText(listCompartcoche.get(2));
                } else {
                    etCompartcoche_1.setText("");
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                }
            }
        });

        cbCompartcoche_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbCompartcoche_0.isChecked()? 1:0) + (cbCompartcoche_1.isChecked()? 1:0) + (cbCompartcoche_2.isChecked()? 1:0) + (cbCompartcoche_3.isChecked()? 1:0) +
                        (cbCompartcoche_4.isChecked()? 1:0) + (cbCompartcoche_5.isChecked()? 1:0);

                if (count > 3){
                    cbCompartcoche_4.setChecked(false);
                }

                if(cbCompartcoche_4.isChecked()) {
                    listCompartcoche.add("4");
                } else {
                    listCompartcoche.remove("4");
                }

                if (listCompartcoche.size() == 1) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 2) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 3) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText(listCompartcoche.get(2));
                } else {
                    etCompartcoche_1.setText("");
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                }
            }
        });

        cbCompartcoche_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbCompartcoche_0.isChecked()? 1:0) + (cbCompartcoche_1.isChecked()? 1:0) + (cbCompartcoche_2.isChecked()? 1:0) + (cbCompartcoche_3.isChecked()? 1:0) +
                        (cbCompartcoche_4.isChecked()? 1:0) + (cbCompartcoche_5.isChecked()? 1:0);

                if (count > 3){
                    cbCompartcoche_5.setChecked(false);
                }

                if(cbCompartcoche_5.isChecked()) {
                    listCompartcoche.add("5");
                    etCompartcoche.setVisibility(VISIBLE);
                } else {
                    listCompartcoche.remove("5");
                    etCompartcoche.setVisibility(GONE);
                }

                if (listCompartcoche.size() == 1) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 2) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText("");
                } else if (listCompartcoche.size() == 3) {
                    etCompartcoche_1.setText(listCompartcoche.get(0));
                    etCompartcoche_2.setText(listCompartcoche.get(1));
                    etCompartcoche_3.setText(listCompartcoche.get(2));
                } else {
                    etCompartcoche_1.setText("");
                    etCompartcoche_2.setText("");
                    etCompartcoche_3.setText("");
                }
            }
        });

        //P22
        final LinearLayout lDisbici = (LinearLayout) activity.findViewById(R.id.survey_layout_check_dispbici);
        final CheckBox cbDispbici_0 = (CheckBox) activity.findViewById(R.id.check_dispbici_option0);
        final CheckBox cbDispbici_1 = (CheckBox) activity.findViewById(R.id.check_dispbici_option1);
        final CheckBox cbDispbici_2 = (CheckBox) activity.findViewById(R.id.check_dispbici_option2);
        final CheckBox cbDispbici_3 = (CheckBox) activity.findViewById(R.id.check_dispbici_option3);
        final CheckBox cbDispbici_4 = (CheckBox) activity.findViewById(R.id.check_dispbici_option4);
        final CheckBox cbDispbici_5 = (CheckBox) activity.findViewById(R.id.check_dispbici_option5);
        final CheckBox cbDispbici_6 = (CheckBox) activity.findViewById(R.id.check_dispbici_option6);
        final EditText etDisbici = (EditText) activity.findViewById(R.id.survey_edit_dispbici);

        final ArrayList<String> listDispbici = new ArrayList<String>();

        final EditText etDispbici_1 = (EditText) activity.findViewById(R.id.survey_edit_dispbici1);
        final EditText etDispbici_2 = (EditText) activity.findViewById(R.id.survey_edit_dispbici2);
        final EditText etDispbici_3 = (EditText) activity.findViewById(R.id.survey_edit_dispbici3);

        cbDispbici_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_0.setChecked(false);
                }

                if(cbDispbici_0.isChecked()) {
                    listDispbici.add("0");
                } else {
                    listDispbici.remove("0");
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
            }
        });

        cbDispbici_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_1.setChecked(false);
                }

                if(cbDispbici_1.isChecked()) {
                    listDispbici.add("1");
                } else {
                    listDispbici.remove("1");
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
            }
        });

        cbDispbici_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_2.setChecked(false);
                }

                if(cbDispbici_2.isChecked()) {
                    listDispbici.add("2");
                } else {
                    listDispbici.remove("2");
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
            }
        });

        cbDispbici_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_3.setChecked(false);
                }

                if(cbDispbici_3.isChecked()) {
                    listDispbici.add("3");
                } else {
                    listDispbici.remove("3");
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
            }
        });

        cbDispbici_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_4.setChecked(false);
                }

                if(cbDispbici_4.isChecked()) {
                    listDispbici.add("4");
                } else {
                    listDispbici.remove("4");
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
            }
        });

        cbDispbici_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_5.setChecked(false);
                }

                if(cbDispbici_5.isChecked()) {
                    listDispbici.add("5");
                } else {
                    listDispbici.remove("5");
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
            }
        });

        cbDispbici_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));

                int count = (cbDispbici_0.isChecked()? 1:0) + (cbDispbici_1.isChecked()? 1:0) + (cbDispbici_2.isChecked()? 1:0) + (cbDispbici_3.isChecked()? 1:0) +
                        (cbDispbici_4.isChecked()? 1:0) + (cbDispbici_5.isChecked()? 1:0) + (cbDispbici_6.isChecked()? 1:0);

                if (count > 3){
                    cbDispbici_6.setChecked(false);
                }

                if(cbDispbici_6.isChecked()) {
                    listDispbici.add("6");
                    etDisbici.setVisibility(VISIBLE);
                } else {
                    listDispbici.remove("6");
                    etDisbici.setVisibility(GONE);
                }

                if (listDispbici.size() == 1) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 2) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText("");
                } else if (listDispbici.size() == 3) {
                    etDispbici_1.setText(listDispbici.get(0));
                    etDispbici_2.setText(listDispbici.get(1));
                    etDispbici_3.setText(listDispbici.get(2));
                } else {
                    etDispbici_1.setText("");
                    etDispbici_2.setText("");
                    etDispbici_3.setText("");
                }
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
                LinearLayout p1 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
                previo.setVisibility(GONE);
                save.setVisibility(GONE);
                next.setVisibility(VISIBLE);
                p1.setVisibility(VISIBLE);
                break;
            case 2:
                //P2
                LinearLayout p2 = (LinearLayout) activity.findViewById(R.id.survey_layout_actempre);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p2.setVisibility(VISIBLE);
                break;
            case 3:
                //P3
                LinearLayout p3 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdlocado);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p3.setVisibility(VISIBLE);
                break;
            case 4:
                //P4
                RelativeLayout p4 = (RelativeLayout) activity.findViewById(R.id.survey_layout_jornada);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p4.setVisibility(VISIBLE);
                break;
            case 5:
                //P5
                RelativeLayout p5 = (RelativeLayout) activity.findViewById(R.id.survey_layout_ndiastrab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p5.setVisibility(VISIBLE);
                break;
            case 6:
                //P6
                RelativeLayout p6 = (RelativeLayout) activity.findViewById(R.id.survey_layout_zonatrab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p6.setVisibility(VISIBLE);
                break;
            case 7:
                //P7
                LinearLayout p7 = (LinearLayout) activity.findViewById(R.id.survey_layout_horaent_pregunta);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p7.setVisibility(VISIBLE);
                break;
            case 8:
                //P8
                RelativeLayout p8 = (RelativeLayout) activity.findViewById(R.id.survey_layout_nmodos);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p8.setVisibility(VISIBLE);
                break;
            case 9:
                //P9
                LinearLayout p9 = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo_pregunta);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p9.setVisibility(VISIBLE);
                break;
            case 10:
                //P10
                RelativeLayout p10 = (RelativeLayout) activity.findViewById(R.id.survey_layout_nocucoche);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p10.setVisibility(VISIBLE);
                break;
            case 11:
                //P11
                RelativeLayout p11 = (RelativeLayout) activity.findViewById(R.id.survey_layout_satistranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p11.setVisibility(VISIBLE);
                break;
            case 12:
                //P12
                LinearLayout p12 = (LinearLayout) activity.findViewById(R.id.survey_layout_valtranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p12.setVisibility(VISIBLE);
                break;
            case 13:
                //P13
                LinearLayout p13 = (LinearLayout) activity.findViewById(R.id.survey_layout_mejtranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p13.setVisibility(VISIBLE);
                break;
            case 14:
                //P14
                LinearLayout p14 = (LinearLayout) activity.findViewById(R.id.survey_layout_desplazatrab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p14.setVisibility(VISIBLE);
                break;
            case 15:
                //P15
                RelativeLayout p15 = (RelativeLayout) activity.findViewById(R.id.survey_layout_aparctrab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p15.setVisibility(VISIBLE);
                break;
            case 16:
                //P16
                LinearLayout p16 = (LinearLayout) activity.findViewById(R.id.survey_layout_notranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p16.setVisibility(VISIBLE);
                break;
            case 17:
                //P17
                LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_disptranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p17.setVisibility(VISIBLE);
                break;
            case 18:
                //P18
                LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_importranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p18.setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_medtranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p19.setVisibility(VISIBLE);
                break;
            case 20:
                //P20
                LinearLayout p20 = (LinearLayout) activity.findViewById(R.id.survey_layout_tiempotranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p20.setVisibility(VISIBLE);
                break;
            case 21:
                //P21
                LinearLayout p21 = (LinearLayout) activity.findViewById(R.id.survey_layout_compartcoche);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p21.setVisibility(VISIBLE);
                break;
            case 22:
                //P22
                LinearLayout p22 = (LinearLayout) activity.findViewById(R.id.survey_layout_dispbici);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p22.setVisibility(VISIBLE);
                break;
            case 23:
                //P23
                LinearLayout p23 = (LinearLayout) activity.findViewById(R.id.survey_layout_modosalida);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p23.setVisibility(VISIBLE);
                break;
            case 24:
                //P24
                RelativeLayout p24 = (RelativeLayout) activity.findViewById(R.id.survey_layout_cdedadtrab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p24.setVisibility(VISIBLE);
                break;
            case 25:
                //P25
                RelativeLayout p25 = (RelativeLayout) activity.findViewById(R.id.survey_layout_cdsexo);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p25.setVisibility(VISIBLE);
                break;
            case 26:
                //P26
                RelativeLayout p26 = (RelativeLayout) activity.findViewById(R.id.survey_layout_cdslab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p26.setVisibility(VISIBLE);
                break;
            case 27:
                //P27
                RelativeLayout p27 = (RelativeLayout) activity.findViewById(R.id.survey_layout_puesto);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p27.setVisibility(VISIBLE);
                break;
            case 28:
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
                    final SearchableSpinner sp_empresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);

                    if (sp_empresa.getSelectedItem().toString().equals("...")) {
                        sp_empresa.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    break;
                case 2:
                    //P2
                    final SearchableSpinner sp_actempre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_actempre);
                    final EditText etActempre_otro = (EditText) activity.findViewById(R.id.survey_edit_actempre);

                    if (sp_actempre.getSelectedItem().toString().substring(0,3).equals("000")) {
                        sp_actempre.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    if (sp_actempre.getSelectedItem().toString().substring(0,3).equals("024") && etActempre_otro.getText().toString().equals("")) {
                        etActempre_otro.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    break;
                case 3:
                    //P3
                    final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
                    final SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
                    final EditText etDistres_otro = (EditText) activity.findViewById(R.id.survey_edit_distres_otros);

                    if (sp_cdlocado.getSelectedItem().toString().substring(0,5).equals("00000")) {
                        sp_cdlocado.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    if (sp_cdlocado.getSelectedItem().toString().substring(0,5).equals("28079") && sp_distres.getSelectedItem().toString().substring(0,5).equals("00000")) {
                        sp_distres.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    }

                    if (sp_distres.getSelectedItem().toString().substring(0,5).equals("99999") && etDistres_otro.getText().toString().equals("")) {
                        etDistres_otro.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etDistres_otro.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 4:
                    //P4
                    final RadioGroup rgJornada = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_jornada);
                    final RadioButton rbJornada_1 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option1);
                    final RadioButton rbJornada_2 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option2);
                    final RadioButton rbJornada_3 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option3);
                    final RadioButton rbJornada_4 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option4);
                    final RadioButton rbJornada_5 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option5);
                    final RadioButton rbJornada_6 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option6);
                    final RadioButton rbJornada_9 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option9);
                    final EditText etJornada_otros = (EditText) activity.findViewById(R.id.survey_edit_jornada_otros);

                    if (!rbJornada_1.isChecked() && !rbJornada_2.isChecked() && !rbJornada_3.isChecked() && !rbJornada_4.isChecked() && !rbJornada_5.isChecked()
                            && !rbJornada_6.isChecked() && !rbJornada_9.isChecked()) {
                        rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (rbJornada_9.isChecked() && etJornada_otros.getText().toString().equals("")){
                        etJornada_otros.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        rgJornada.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etJornada_otros.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 5:
                    //P5
                    final RadioGroup rgNdiastrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ndiastrab);
                    final RadioButton rbNdiastrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ndiastrab_option1);
                    final RadioButton rbNdiastrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ndiastrab_option2);
                    final RadioButton rbNdiastrab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ndiastrab_option3);

                    if (!rbNdiastrab_1.isChecked() && !rbNdiastrab_2.isChecked() && !rbNdiastrab_3.isChecked()) {
                        rgNdiastrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgNdiastrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 6:
                    //P6
                    final RadioGroup rgZonatrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_zonatrab);
                    final RadioButton rbZonatrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option1);
                    final RadioButton rbZonatrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option2);
                    final RadioButton rbZonatrab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option3);
                    final RadioButton rbZonatrab_4 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option4);
                    final RadioButton rbZonatrab_5 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option5);
                    final RadioButton rbZonatrab_6 = (RadioButton) activity.findViewById(R.id.survey_radio_zonatrab_option6);

                    if (!rbZonatrab_1.isChecked() && !rbZonatrab_2.isChecked() && !rbZonatrab_3.isChecked() && !rbZonatrab_4.isChecked() && !rbZonatrab_5.isChecked() && !rbZonatrab_6.isChecked()) {
                        rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgZonatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 7:
                    //P7
                    final LinearLayout llHoraent1 = (LinearLayout) activity.findViewById(R.id.survey_layout_horaent_option1);
                    final TimePicker tpHoraent1 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent1);
                    final TimePicker tpHorasal1 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal1);
                    final LinearLayout llHoraent2 = (LinearLayout) activity.findViewById(R.id.survey_layout_horaent_option2);
                    final TimePicker tpHoraent2 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent2);
                    final TimePicker tpHorasal2 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal2);
                    final LinearLayout llHoraent3 = (LinearLayout) activity.findViewById(R.id.survey_layout_horaent_option3);
                    final TimePicker tpHoraent3 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent3);
                    final TimePicker tpHorasal3 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal3);

                    String stHoraent1;
                    String stHorasal1;

                    stHoraent1 = replicate(String.valueOf(tpHoraent1.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(tpHoraent1.getCurrentMinute()), "0", 2, 2);
                    stHorasal1 = replicate(String.valueOf(tpHorasal1.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(tpHorasal1.getCurrentMinute()), "0", 2, 2);

                    String stHoraent2;
                    String stHorasal2;

                    stHoraent2 = replicate(String.valueOf(tpHoraent2.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(tpHoraent2.getCurrentMinute()), "0", 2, 2);
                    stHorasal2 = replicate(String.valueOf(tpHorasal2.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(tpHorasal2.getCurrentMinute()), "0", 2, 2);

                    String stHoraent3;
                    String stHorasal3;

                    stHoraent3 = replicate(String.valueOf(tpHoraent3.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(tpHoraent3.getCurrentMinute()), "0", 2, 2);
                    stHorasal3 = replicate(String.valueOf(tpHorasal3.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(tpHorasal3.getCurrentMinute()), "0", 2, 2);

                    if(stHoraent1.equals("00:00") && stHorasal1.equals("00:00")){
                        llHoraent1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else if (!stHoraent1.equals("00:00") && stHoraent1.equals(stHorasal1)){
                        llHoraent1.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_sameTime),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else if (!stHoraent2.equals("00:00") && llHoraent2.getVisibility() == VISIBLE && stHoraent2.equals(stHorasal2)){
                        llHoraent2.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_sameTime),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else if (!stHoraent3.equals("00:00") && llHoraent3.getVisibility() == VISIBLE && stHoraent3.equals(stHorasal3)){
                        llHoraent3.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_sameTime),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        llHoraent1.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        llHoraent2.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        llHoraent3.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 8:
                    //P8
                    final RadioGroup rgNmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
                    final RadioButton rbNmodos_1 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_option1);
                    final RadioButton rbNmodos_2 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_option2);
                    final RadioButton rbNmodos_Nmodos = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_nmodos);
                    final EditText etNmodos_otros = (EditText) activity.findViewById(R.id.survey_edit_nmodos_otros);

                    if (!rbNmodos_1.isChecked() && !rbNmodos_2.isChecked() && !rbNmodos_Nmodos.isChecked()) {
                        rgNmodos.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (rbNmodos_Nmodos.isChecked() && stringToInt(etNmodos_otros.getText().toString()) < 3) {
                        etNmodos_otros.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_setNumberModes),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        etNmodos_otros.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 9:
                    //P9
                    final RadioGroup rgUmodo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_umodo);
                    final RadioButton rbUmodo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option1);
                    final RadioButton rbUmodo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option2);
                    final RadioButton rbUmodo_3 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option3);
                    final RadioButton rbUmodo_4 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option4);
                    final RadioButton rbUmodo_5 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option5);
                    final RadioButton rbUmodo_6 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option6);
                    final RadioButton rbUmodo_7 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option7);
                    final RadioButton rbUmodo_8 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option8);
                    final RadioButton rbUmodo_9 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option9);
                    final RadioButton rbUmodo_10 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option10);
                    final RadioButton rbUmodo_11 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option11);
                    final RadioButton rbUmodo_13 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option13);

                    if (!rbUmodo_1.isChecked() && !rbUmodo_2.isChecked() && !rbUmodo_3.isChecked() && !rbUmodo_4.isChecked() && !rbUmodo_5.isChecked()
                            && !rbUmodo_6.isChecked() && !rbUmodo_7.isChecked() && !rbUmodo_8.isChecked() && !rbUmodo_9.isChecked() && !rbUmodo_10.isChecked()
                            && !rbUmodo_11.isChecked() && !rbUmodo_13.isChecked()) {

                        rgUmodo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    }
                    break;
                case 10:
                    //P10
                    final RadioGroup rgNocucoche = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nocucoche);
                    final RadioButton rbNocucoche_1 = (RadioButton) activity.findViewById(R.id.survey_radio_nocucoche_option1);
                    final RadioButton rbNocucoche_2 = (RadioButton) activity.findViewById(R.id.survey_radio_nocucoche_option2);
                    final RadioButton rbNocucoche_3 = (RadioButton) activity.findViewById(R.id.survey_radio_nocucoche_option3);

                    if (!rbNocucoche_1.isChecked() && !rbNocucoche_2.isChecked() && !rbNocucoche_3.isChecked()) {
                        rgNocucoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgNocucoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 11:
                    //P11
                    final RadioGroup rgSatistranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_satistranspubli);
                    final RadioButton rbSatistranspubli_1 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option1);
                    final RadioButton rbSatistranspubli_2 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option2);
                    final RadioButton rbSatistranspubli_3 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option3);
                    final RadioButton rbSatistranspubli_4 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option4);
                    final RadioButton rbSatistranspubli_5 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option5);
                    final RadioButton rbSatistranspubli_9 = (RadioButton) activity.findViewById(R.id.survey_radio_satistranspubli_option9);

                    if (!rbSatistranspubli_1.isChecked() && !rbSatistranspubli_2.isChecked() && !rbSatistranspubli_3.isChecked() && !rbSatistranspubli_4.isChecked()
                            && !rbSatistranspubli_5.isChecked() && !rbSatistranspubli_9.isChecked()) {
                        rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgSatistranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 12:
                    //P12
                    final LinearLayout llValtranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_valtranspubli);
                    final CheckBox cbValtranspubli_1 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option1);
                    final CheckBox cbValtranspubli_2 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option2);
                    final CheckBox cbValtranspubli_3 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option3);
                    final CheckBox cbValtranspubli_4 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option4);
                    final CheckBox cbValtranspubli_5 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option5);
                    final CheckBox cbValtranspubli_6 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option6);
                    final CheckBox cbValtranspubli_7 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option7);
                    final CheckBox cbValtranspubli_8 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option8);
                    final CheckBox cbValtranspubli_9 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option9);
                    final EditText etValtranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli);

                    if (!cbValtranspubli_1.isChecked() && !cbValtranspubli_2.isChecked() && !cbValtranspubli_3.isChecked()
                            && !cbValtranspubli_4.isChecked() && !cbValtranspubli_5.isChecked() && !cbValtranspubli_6.isChecked()
                            && !cbValtranspubli_7.isChecked() && !cbValtranspubli_8.isChecked() && !cbValtranspubli_9.isChecked()){

                        llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (cbValtranspubli_9.isChecked() && etValtranspubli_otros.getText().toString().equals("")){
                        etValtranspubli_otros.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        llValtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 13:
                    //P13
                    final LinearLayout llMejtranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_mejtranspubli);
                    final CheckBox cbMejtranspubli_1 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option1);
                    final CheckBox cbMejtranspubli_2 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option2);
                    final CheckBox cbMejtranspubli_3 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option3);
                    final CheckBox cbMejtranspubli_4 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option4);
                    final CheckBox cbMejtranspubli_5 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option5);
                    final CheckBox cbMejtranspubli_6 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option6);
                    final CheckBox cbMejtranspubli_9 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option9);
                    final EditText etMejtranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli);

                    if (!cbMejtranspubli_1.isChecked() && !cbMejtranspubli_2.isChecked() && !cbMejtranspubli_3.isChecked()
                            && !cbMejtranspubli_4.isChecked() && !cbMejtranspubli_5.isChecked() && !cbMejtranspubli_6.isChecked() && !cbMejtranspubli_9.isChecked()){

                        llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (cbMejtranspubli_9.isChecked() && etMejtranspubli_otros.getText().toString().equals("")){
                        etMejtranspubli_otros.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        llMejtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etMejtranspubli_otros.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 14:
                    //P14
                    final EditText etDesplazatrab = (EditText) activity.findViewById(R.id.survey_edit_desplazatrab);

                    if (etDesplazatrab.getText().toString().equals("")) {
                        etDesplazatrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        etDesplazatrab.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 15:
                    //P15
                    final RadioGroup rgAparctrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_aparctrab);
                    final RadioButton rbAparctrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option1);
                    final RadioButton rbAparctrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option2);
                    final RadioButton rbAparctrab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option3);
                    final RadioButton rbAparctrab_4 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option4);
                    final RadioButton rbAparctrab_9 = (RadioButton) activity.findViewById(R.id.survey_radio_aparctrab_option9);

                    if (!rbAparctrab_1.isChecked() && !rbAparctrab_2.isChecked() && !rbAparctrab_3.isChecked() && !rbAparctrab_4.isChecked() && !rbAparctrab_9.isChecked()) {
                        rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgAparctrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 16:
                    //P16
                    final LinearLayout llNotranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_notranspubli);
                    final CheckBox cbNotranspubli_1 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option1);
                    final CheckBox cbNotranspubli_2 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option2);
                    final CheckBox cbNotranspubli_3 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option3);
                    final CheckBox cbNotranspubli_4 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option4);
                    final CheckBox cbNotranspubli_5 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option5);
                    final CheckBox cbNotranspubli_6 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option6);
                    final CheckBox cbNotranspubli_7 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option7);
                    final CheckBox cbNotranspubli_8 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option8);
                    final CheckBox cbNotranspubli_9 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option9);
                    final CheckBox cbNotranspubli_10 = (CheckBox) activity.findViewById(R.id.check_notranspubli_option10);
                    final EditText etNotranspubli_otros = (EditText) activity.findViewById(R.id.survey_edit_notranspubli);

                    if (!cbNotranspubli_1.isChecked() && !cbNotranspubli_2.isChecked() && !cbNotranspubli_3.isChecked()
                            && !cbNotranspubli_4.isChecked() && !cbNotranspubli_5.isChecked() && !cbNotranspubli_6.isChecked() && !cbNotranspubli_7.isChecked()
                            && !cbNotranspubli_8.isChecked() && !cbNotranspubli_9.isChecked() && !cbNotranspubli_10.isChecked()){

                        llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (cbNotranspubli_10.isChecked() && etNotranspubli_otros.getText().toString().equals("")){
                        etNotranspubli_otros.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        llNotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etNotranspubli_otros.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 17:
                    //P17
                    final RadioGroup rgDisptranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_disptranspubli);
                    final RadioButton rbDisptranspubli_1 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option1);
                    final RadioButton rbDisptranspubli_2 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option2);
                    final RadioButton rbDisptranspubli_3 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option3);
                    final RadioButton rbDisptranspubli_4 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option4);
                    final RadioButton rbDisptranspubli_5 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option5);
                    final RadioButton rbDisptranspubli_6 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option6);
                    final RadioButton rbDisptranspubli_7 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option7);
                    final EditText etDisptranspubli= (EditText) activity.findViewById(R.id.survey_edit_disptranspubli_otros);

                    if (!rbDisptranspubli_1.isChecked() && !rbDisptranspubli_2.isChecked() && !rbDisptranspubli_3.isChecked() && !rbDisptranspubli_4.isChecked() &&
                            !rbDisptranspubli_5.isChecked() && !rbDisptranspubli_6.isChecked() && !rbDisptranspubli_7.isChecked()) {
                        rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (rbDisptranspubli_7.isChecked() && etDisptranspubli.getText().toString().equals("")){

                        etDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        rgDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etDisptranspubli.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 18:
                    //P18

                    break;
                case 19:
                    //P19
                    final LinearLayout lMedtranspubli = (LinearLayout) activity.findViewById(R.id.survey_layout_check_medtranspubli);
                    final CheckBox cbMedtranspubli_7 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option7);
                    final CheckBox cbMedtranspubli_8 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option8);
                    final CheckBox cbMedtranspubli_10 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option10);
                    final CheckBox cbMedtranspubli_11 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option11);
                    final CheckBox cbMedtranspubli_99 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option99);
                    final EditText etMedtranspubli = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli);

                    if(!cbMedtranspubli_7.isChecked() && !cbMedtranspubli_8.isChecked() && !cbMedtranspubli_10.isChecked() && !cbMedtranspubli_11.isChecked()
                            && !cbMedtranspubli_99.isChecked()) {

                        lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (cbMedtranspubli_99.isChecked() && etMedtranspubli.getText().toString().equals("")){

                        etMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {

                        lMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etMedtranspubli.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 20:
                    //P20
                    final EditText etTiempotranspubli = (EditText) activity.findViewById(R.id.survey_edit_tiempotranspubli);
                    if (etTiempotranspubli.getText().toString().equals("")){

                        etTiempotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {

                        etTiempotranspubli.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 21:
                    //P21
                    final LinearLayout lCompartcoche = (LinearLayout) activity.findViewById(R.id.survey_layout_check_compartcoche);
                    final CheckBox cbCompartcoche_0 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option0);
                    final CheckBox cbCompartcoche_1 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option1);
                    final CheckBox cbCompartcoche_2 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option2);
                    final CheckBox cbCompartcoche_3 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option3);
                    final CheckBox cbCompartcoche_4 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option4);
                    final CheckBox cbCompartcoche_5 = (CheckBox) activity.findViewById(R.id.check_compartcoche_option5);
                    final EditText etCompartcoche = (EditText) activity.findViewById(R.id.survey_edit_compartcoche);

                    if(!cbCompartcoche_0.isChecked() && !cbCompartcoche_1.isChecked() && !cbCompartcoche_2.isChecked() && !cbCompartcoche_3.isChecked()
                            && !cbCompartcoche_4.isChecked() && !cbCompartcoche_5.isChecked()) {

                        lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (cbCompartcoche_5.isChecked() && etCompartcoche.getText().toString().equals("")){

                        etCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {

                        lCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etCompartcoche.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 22:
                    //P22
                    final LinearLayout lDisbici = (LinearLayout) activity.findViewById(R.id.survey_layout_check_dispbici);
                    final CheckBox cbDisbici_0 = (CheckBox) activity.findViewById(R.id.check_dispbici_option0);
                    final CheckBox cbDisbici_1 = (CheckBox) activity.findViewById(R.id.check_dispbici_option1);
                    final CheckBox cbDisbici_2 = (CheckBox) activity.findViewById(R.id.check_dispbici_option2);
                    final CheckBox cbDisbici_3 = (CheckBox) activity.findViewById(R.id.check_dispbici_option3);
                    final CheckBox cbDisbici_4 = (CheckBox) activity.findViewById(R.id.check_dispbici_option4);
                    final CheckBox cbDisbici_5 = (CheckBox) activity.findViewById(R.id.check_dispbici_option5);
                    final CheckBox cbDisbici_6 = (CheckBox) activity.findViewById(R.id.check_dispbici_option6);
                    final EditText etDisbici = (EditText) activity.findViewById(R.id.survey_edit_dispbici);

                    if(!cbDisbici_0.isChecked() && !cbDisbici_1.isChecked() && !cbDisbici_2.isChecked() && !cbDisbici_3.isChecked()
                            && !cbDisbici_4.isChecked() && !cbDisbici_5.isChecked() && !cbDisbici_6.isChecked()) {

                        lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (cbDisbici_6.isChecked() && etDisbici.getText().toString().equals("")){

                        etDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {

                        lDisbici.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etDisbici.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 23:
                    //P23
                    final RadioGroup rgModosalida = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modosalida);
                    final RadioButton rbModosalida_1 = (RadioButton) activity.findViewById(R.id.survey_radio_modosalida_option1);
                    final RadioButton rbModosalida_2 = (RadioButton) activity.findViewById(R.id.survey_radio_modosalida_option2);
                    final EditText etModosalida_i = (EditText) activity.findViewById(R.id.survey_edit_modosalida_indique);

                    if (!rbModosalida_1.isChecked() && !rbModosalida_2.isChecked()) {
                        rgModosalida.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else if (rbModosalida_2.isChecked() && etModosalida_i.getText().toString().equals("")){

                        etModosalida_i.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_specifyAnswer),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));
                    } else {
                        rgModosalida.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                        etModosalida_i.setBackgroundColor(activity.getResources().getColor(R.color.md_white_1000));
                    }

                    break;
                case 24:
                    //P24
                    final RadioGroup rgCdedadtrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedadtrab);
                    final RadioButton rbCdedadtrab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option1);
                    final RadioButton rbCdedadtrab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option2);
                    final RadioButton rbCdedadtrab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option3);
                    final RadioButton rbCdedadtrab_4 = (RadioButton) activity.findViewById(R.id.survey_radio_cdedadtrab_option4);

                    if (!rbCdedadtrab_1.isChecked() && !rbCdedadtrab_2.isChecked() && !rbCdedadtrab_3.isChecked() && !rbCdedadtrab_4.isChecked()) {
                        rgCdedadtrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgCdedadtrab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 25:
                    //P25
                    final RadioGroup rgCdsexo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdsexo);
                    final RadioButton rbCdsexo_1 = (RadioButton) activity.findViewById(R.id.survey_radio_cdsexo_option1);
                    final RadioButton rbCdsexo_2 = (RadioButton) activity.findViewById(R.id.survey_radio_cdsexo_option2);

                    if (!rbCdsexo_1.isChecked() && !rbCdsexo_2.isChecked()) {
                        rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgCdsexo.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 26:
                    //P26
                    final RadioGroup rgCdslab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdslab);
                    final RadioButton rbCdslab_1 = (RadioButton) activity.findViewById(R.id.survey_radio_cdslab_option1);
                    final RadioButton rbCdslab_2 = (RadioButton) activity.findViewById(R.id.survey_radio_cdslab_option2);
                    final RadioButton rbCdslab_3 = (RadioButton) activity.findViewById(R.id.survey_radio_cdslab_option3);

                    if (!rbCdslab_1.isChecked() && !rbCdslab_2.isChecked() && !rbCdslab_3.isChecked()) {
                        rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgCdslab.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

                    break;
                case 27:
                    //P27
                    final RadioGroup rgPuesto = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_puesto);
                    final RadioButton rbPuesto_1 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option1);
                    final RadioButton rbPuesto_2 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option2);
                    final RadioButton rbPuesto_3 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option3);
                    final RadioButton rbPuesto_4 = (RadioButton) activity.findViewById(R.id.survey_radio_puesto_option4);

                    if (!rbPuesto_1.isChecked() && !rbPuesto_2.isChecked() && !rbPuesto_3.isChecked() && !rbPuesto_4.isChecked()) {
                        rgPuesto.setBackgroundColor(activity.getResources().getColor(R.color.aenaRed));

                        return getDialogValueBackError(activity,
                                activity.getResources().getString(R.string.survey_model_text_errorTitle),
                                activity.getResources().getString(R.string.survey_text_selectOption),
                                activity.getResources().getString(R.string.survey_model_text_errorBtnReview));

                    } else {
                        rgPuesto.setBackgroundColor(activity.getResources().getColor(R.color.aenaDarkGrey));
                    }

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
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_EMPRESA, cue.getEmpresa());

                    break;
                case 2:
                    //P2
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_ACTEMPRE, cue.getActempre());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_ACTEMPREOTRO, cue.getActempreotro());

                    break;
                case 3:
                    //P3
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_CDLOCADO, cue.getCdlocado());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISTRES, cue.getDistres());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISTRESOTRO, cue.getDistresotro());

                    break;
                case 4:
                    //P4
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_JORNADA, cue.getJornada());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_JORNADAOTRO, cue.getJornadaotro());

                    break;
                case 5:
                    //P5
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NDIASTRAB, cue.getNdiastrab());

                    break;
                case 6:
                    //P6
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_ZONATRAB, cue.getZonatrab());

                    break;
                case 7:
                    //P7
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_HORAENT1, cue.getHoraent1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_HORASAL1, cue.getHorasal1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_HORAENT2, cue.getHoraent2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_HORASAL2, cue.getHorasal2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_HORAENT3, cue.getHoraent3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_HORASAL3, cue.getHorasal3());

                    break;
                case 8:
                    //P8
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NMODOS, cue.getNmodos());

                    break;
                case 9:
                    //P9
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MODO1, cue.getModo1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MODO2, cue.getModo2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_ULTIMODO, cue.getUltimodo());

                    break;
                case 10:
                    //P10
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOCUCOCHE, cue.getNocucoche());

                    break;
                case 11:
                    //P11
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_SATISTRANSPUBLI, cue.getSatistranspubli());

                    break;
                case 12:
                    //P12
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI1, cue.getValtranspubli1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI2, cue.getValtranspubli2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI3, cue.getValtranspubli3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLIOTRO, cue.getValtranspubliotro());

                    break;
                case 13:
                    //P13
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI1, cue.getMejtranspubli1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI2, cue.getMejtranspubli2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI3, cue.getMejtranspubli3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLIOTRO, cue.getMejtranspubliotro());

                    break;
                case 14:
                    //P14
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DESPLAZATRAB, cue.getDesplazatrab());

                    break;
                case 15:
                    //P15
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_APARCTRAB, cue.getAparctrab());

                    break;
                case 16:
                    //P16
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI1, cue.getNotranspubli1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI2, cue.getNotranspubli2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI3, cue.getNotranspubli3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO, cue.getNotranspubliotro());

                    break;
                case 17:
                    //P17
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLI, cue.getDisptranspubli());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO, cue.getDisptranspubliotro());

                    break;
                case 18:
                    //P18
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI, cue.getImportranspubli());

                    break;
                case 19:
                    //P19
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1, cue.getMedtranspubli1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2, cue.getMedtranspubli2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3, cue.getMedtranspubli3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO, cue.getMedtranspubliotro());

                    break;
                case 20:
                    //P20
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI, cue.getTiempotranspubli());

                    break;
                case 21:
                    //P21
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE1, cue.getCompartcoche1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE2, cue.getCompartcoche2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE3, cue.getCompartcoche3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHEOTRO, cue.getCompartcocheotro());

                    break;
                case 22:
                    //P22
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICI1, cue.getDispbici1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICI2, cue.getDispbici2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICI3, cue.getDispbici3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICIOTRO, cue.getDispbiciotro());

                    break;
                case 23:
                    //P23
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MODOSALIDA, cue.getModosalida());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MODOSALIDAOTRO, cue.getModosalidaotro());

                    break;
                case 24:
                    //P24
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_CDEDADTRAB, cue.getCdedadtrab());

                    break;
                case 25:
                    //P25
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_CDSEXO, String.valueOf(cue.getCdsexo()));

                    break;
                case 26:
                    //P26
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_CDSLAB, cue.getCdslab());

                    break;
                case 27:
                    //P27
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_PUESTO, cue.getPuesto());

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
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_ACTEMPRE);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_ACTEMPREOTRO);

                    break;
                case 3:
                    //P3
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_CDLOCADO);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISTRES);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISTRESOTRO);

                    break;
                case 4:
                    //P4
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_JORNADA);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_JORNADAOTRO);

                    break;
                case 5:
                    //P5
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NDIASTRAB);

                    break;
                case 6:
                    //P6
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_ZONATRAB);

                    break;
                case 7:
                    //P7
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_HORAENT1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_HORASAL1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_HORAENT2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_HORASAL2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_HORAENT3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_HORASAL3);

                    break;
                case 8:
                    //P8
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NMODOS);

                    break;
                case 9:
                    //P9
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MODO1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MODO2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_ULTIMODO);

                    break;
                case 10:
                    //P10
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOCUCOCHE);

                    break;
                case 11:
                    //P11
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_SATISTRANSPUBLI);

                    break;
                case 12:
                    //P12
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_VALTRANSPUBLIOTRO);

                    break;
                case 13:
                    //P13
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEJTRANSPUBLIOTRO);

                    break;
                case 14:
                    //P14
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DESPLAZATRAB);

                    break;
                case 15:
                    //P15
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_APARCTRAB);

                    break;
                case 16:
                    //P16
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO);

                    break;
                case 17:
                    //P17
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLI);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO);

                    break;
                case 18:
                    //P18
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI);

                    break;
                case 19:
                    //P19
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO);

                    break;
                case 20:
                    //P20
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI);

                    break;
                case 21:
                    //P21
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHE3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_COMPARTCOCHEOTRO);

                    break;
                case 22:
                    //P22
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPBICIOTRO);

                    break;
                case 23:
                    //P23
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MODOSALIDA);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MODOSALIDAOTRO);

                    break;
                case 24:
                    //P24
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_CDEDADTRAB);

                    break;
                case 25:
                    //P25
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_CDSEXO);

                    break;
                case 26:
                    //P26
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_CDSLAB);

                    break;
                case 27:
                    //P27
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_PUESTO);

                    break;
            }
        }
        return true;
    }

    public void hideQuestions() {

        //P1
        LinearLayout p1 = (LinearLayout) activity.findViewById(R.id.survey_layout_empresa);
        p1.setVisibility(GONE);

        //P2
        LinearLayout p2 = (LinearLayout) activity.findViewById(R.id.survey_layout_actempre);
        p2.setVisibility(GONE);

        //P3
        LinearLayout p3 = (LinearLayout) activity.findViewById(R.id.survey_layout_cdlocado);
        p3.setVisibility(GONE);

        //P4
        RelativeLayout p4 = (RelativeLayout) activity.findViewById(R.id.survey_layout_jornada);
        p4.setVisibility(GONE);

        //P5
        RelativeLayout p5 = (RelativeLayout) activity.findViewById(R.id.survey_layout_ndiastrab);
        p5.setVisibility(GONE);

        //P6
        RelativeLayout p6 = (RelativeLayout) activity.findViewById(R.id.survey_layout_zonatrab);
        p6.setVisibility(GONE);

        //P7
        LinearLayout p7 = (LinearLayout) activity.findViewById(R.id.survey_layout_horaent_pregunta);
        p7.setVisibility(GONE);

        //P8
        RelativeLayout p8 = (RelativeLayout) activity.findViewById(R.id.survey_layout_nmodos);
        p8.setVisibility(GONE);

        //P9
        LinearLayout p9 = (LinearLayout) activity.findViewById(R.id.survey_layout_ultimodo_pregunta);
        p9.setVisibility(GONE);

        //P10
        RelativeLayout p10 = (RelativeLayout) activity.findViewById(R.id.survey_layout_nocucoche);
        p10.setVisibility(GONE);

        //P11
        RelativeLayout p11 = (RelativeLayout) activity.findViewById(R.id.survey_layout_satistranspubli);
        p11.setVisibility(GONE);

        //P12
        LinearLayout p12 = (LinearLayout) activity.findViewById(R.id.survey_layout_valtranspubli);
        p12.setVisibility(GONE);

        //P13
        LinearLayout p13 = (LinearLayout) activity.findViewById(R.id.survey_layout_mejtranspubli);
        p13.setVisibility(GONE);

        //P14
        LinearLayout p14 = (LinearLayout) activity.findViewById(R.id.survey_layout_desplazatrab);
        p14.setVisibility(GONE);

        //P15
        RelativeLayout p15 = (RelativeLayout) activity.findViewById(R.id.survey_layout_aparctrab);
        p15.setVisibility(GONE);

        //P16
        LinearLayout p16 = (LinearLayout) activity.findViewById(R.id.survey_layout_notranspubli);
        p16.setVisibility(GONE);

        //P17
        LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_disptranspubli);
        p17.setVisibility(GONE);

        //P18
        LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_importranspubli);
        p18.setVisibility(GONE);

        //P19
        LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_medtranspubli);
        p19.setVisibility(GONE);

        //P20
        LinearLayout p20 = (LinearLayout) activity.findViewById(R.id.survey_layout_tiempotranspubli);
        p20.setVisibility(GONE);

        //P21
        LinearLayout p21 = (LinearLayout) activity.findViewById(R.id.survey_layout_compartcoche);
        p21.setVisibility(GONE);

        //P22
        LinearLayout p22 = (LinearLayout) activity.findViewById(R.id.survey_layout_dispbici);
        p22.setVisibility(GONE);

        //P23
        LinearLayout p23 = (LinearLayout) activity.findViewById(R.id.survey_layout_modosalida);
        p23.setVisibility(GONE);

        //P24
        RelativeLayout p24 = (RelativeLayout) activity.findViewById(R.id.survey_layout_cdedadtrab);
        p24.setVisibility(GONE);

        //P25
        RelativeLayout p25 = (RelativeLayout) activity.findViewById(R.id.survey_layout_cdsexo);
        p25.setVisibility(GONE);

        //P26
        RelativeLayout p26 = (RelativeLayout) activity.findViewById(R.id.survey_layout_cdslab);
        p26.setVisibility(GONE);

        //P27
        RelativeLayout p27 = (RelativeLayout) activity.findViewById(R.id.survey_layout_puesto);
        p27.setVisibility(GONE);

    }

    public int showNextQuestion(int show) {

        boolean activated = true;

        int checkedId;

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
                show = showQuestion(4);
                break;
            case 4:
                //P4
                if (activated) {
                    RadioGroup rgJornada = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_jornada);
                    checkedId = rgJornada.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_jornada_option1:
                                show = showQuestion(6); //>P6
                                break;
                            case R.id.survey_radio_jornada_option2:
                                show = showQuestion(6); //>P6
                                break;
                            case R.id.survey_radio_jornada_option3:
                                show = showQuestion(6); //>P6
                                break;
                            case R.id.survey_radio_jornada_option4:
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_jornada_option5:
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_jornada_option6:
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_jornada_option9:
                                show = showQuestion(5); //>P5
                                break;
                            default:
                                show = showQuestion(5); //>P5
                                break;
                        }
                    } else {
                        show = showQuestion(5); //>P5
                    }
                } else {
                    show = showQuestion(5);
                }
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
                RadioGroup rgNmodos= (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
                checkedId = rgNmodos.getCheckedRadioButtonId();

                if (checkedId > 0) {
                    switch (checkedId) {
                        case R.id.survey_radio_nmodos_option1:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_layout_ultimodo_umodo).setVisibility(VISIBLE);
                            break;
                        case R.id.survey_radio_nmodos_option2:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(GONE);
                            activity.findViewById(R.id.survey_layout_ultimodo_umodo).setVisibility(VISIBLE);
                            break;
                        case R.id.survey_radio_nmodos_nmodos:
                            activity.findViewById(R.id.survey_layout_ultimodo_1modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_2modo).setVisibility(VISIBLE);
                            activity.findViewById(R.id.survey_layout_ultimodo_umodo).setVisibility(VISIBLE);
                            break;
                    }
                }

                show = showQuestion(9);
                break;
            case 9:
                //P9
                if (activated) {
                    RadioGroup rgUltimoModo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_umodo);
                    checkedId = rgUltimoModo.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_ultimodo_umodo_option1:
                                show = showQuestion(14); //>P14
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option2:
                                show = showQuestion(14); //>P14
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option3:
                                show = showQuestion(14); //>P14
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option4:
                                show = showQuestion(14); //>P14
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option5:
                                show = showQuestion(14); //>P14
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option6:
                                show = showQuestion(10); //>P10
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option7:
                                show = showQuestion(11); //>P11
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option8:
                                show = showQuestion(11); //>P11
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option9:
                                show = showQuestion(22); //>P22
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option10:
                                show = showQuestion(11); //>P11
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option11:
                                show = showQuestion(11); //>P11
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option13:
                                show = showQuestion(22); //>P22
                                break;
                            default:
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
                show = showQuestion(14);
                break;
            case 11:
                //P11
                show = showQuestion(12);
                break;
            case 12:
                //P12
                show = showQuestion(13);
                break;
            case 13:
                //P13
                show = showQuestion(22); //>P22
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
                if (activated) {
                    RadioGroup rgDistptranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_disptranspubli);
                    checkedId = rgDistptranspubli.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_disptranspubli_option1:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_disptranspubli_option2:
                                show = showQuestion(19); //>P19
                                break;
                            case R.id.survey_radio_disptranspubli_option3:
                                show = showQuestion(19); //>P19
                                break;
                            case R.id.survey_radio_disptranspubli_option4:
                                show = showQuestion(19); //>P19
                                break;
                            case R.id.survey_radio_disptranspubli_option5:
                                show = showQuestion(19); //>P19
                                break;
                            case R.id.survey_radio_disptranspubli_option6:
                                show = showQuestion(19); //>P19
                                break;
                            case R.id.survey_radio_disptranspubli_option7:
                                show = showQuestion(19); //>P19
                                break;
                            default:
                                show = showQuestion(18); //>P18
                                break;
                        }
                    } else {
                        show = showQuestion(18); //>P18
                    }
                } else {
                    show = showQuestion(18);
                }
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
                show = showQuestion(finCue); //>FIN
                break;
        }

        return show;
    }

    public CueTrabajadores fillQuest(CueTrabajadores quest) {
        int selectedCode = -1;
        int checkedId = -1;

        //P1
        SearchableSpinner spEmpresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);
        String textSpEmpresa = spEmpresa.getSelectedItem().toString();
        EditText etEmpresa = (EditText) activity.findViewById(R.id.survey_edit_empresa);
        if(!textSpEmpresa.contains("...")){
            if(textSpEmpresa.contains("OTROS")){
                quest.setEmpresa(etEmpresa.getText().toString());
            } else {
                quest.setEmpresa(textSpEmpresa);
            }
        } else {
            quest.setEmpresa("-1");
        }

        //P2
        SearchableSpinner spActempre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_actempre);
        String textSpActempre = spActempre.getSelectedItem().toString().substring(0,3);
        EditText etActEmpOtros = (EditText) activity.findViewById(R.id.survey_edit_actempre);
        if(!textSpActempre.contains("000")){
            quest.setActempre(textSpActempre);

            if(textSpActempre.contains("024")){
                quest.setActempreotro(etActEmpOtros.getText().toString());
            }
        } else {
            quest.setActempre("-1");
        }

        //P3
        SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
        String textSpCdlocado = sp_cdlocado.getSelectedItem().toString().substring(0,5);
        if(!textSpCdlocado.contains("00000")){
            quest.setCdlocado(textSpCdlocado);
        } else {
            quest.setCdlocado("-1");
        }

        SearchableSpinner sp_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        String textSpDistres = sp_distres.getSelectedItem().toString().substring(0,5);
        EditText etDistresOtros = (EditText) activity.findViewById(R.id.survey_edit_distres_otros);
        if(!textSpDistres.contains("00000") && textSpCdlocado.contains("28079")){
            quest.setDistres(textSpDistres);

            if(textSpDistres.contains("99999")){
                quest.setDistresotro(etDistresOtros.getText().toString());
            }
        } else {
            quest.setDistres("-1");
        }

        //P4
        RadioGroup rgJornada = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_jornada);
        EditText etJornadaOtros = (EditText) activity.findViewById(R.id.survey_edit_jornada_otros);

        selectedCode = -1;
        checkedId = rgJornada.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_jornada_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_jornada_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_jornada_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_jornada_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_jornada_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_jornada_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_jornada_option9:
                    selectedCode = 9;
                    quest.setJornadaotro(etJornadaOtros.getText().toString());
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setJornada(String.valueOf(selectedCode));

        //P5
        RadioGroup rgNdiastrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_ndiastrab);

        selectedCode = -1;
        checkedId = rgNdiastrab.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_ndiastrab_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_ndiastrab_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_ndiastrab_option3:
                    selectedCode = 3;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setNdiastrab(String.valueOf(selectedCode));

        //P6
        RadioGroup rgZonatrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_zonatrab);

        selectedCode = -1;
        checkedId = rgZonatrab.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_zonatrab_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_zonatrab_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_zonatrab_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_zonatrab_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_zonatrab_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_zonatrab_option6:
                    selectedCode = 6;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setZonatrab(String.valueOf(selectedCode));

        //P7
        TimePicker etHoraent1 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent1);
        TimePicker etHoraent2 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent2);
        TimePicker etHoraent3 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent3);
        TimePicker etHorasal1 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal1);
        TimePicker etHorasal2 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal2);
        TimePicker etHorasal3 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal3);

        String stHoraent1 = replicate(String.valueOf(etHoraent1.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(etHoraent1.getCurrentMinute()), "0", 2, 2);
        String stHorasal1 = replicate(String.valueOf(etHorasal1.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(etHorasal1.getCurrentMinute()), "0", 2, 2);
        String stHoraent2 = replicate(String.valueOf(etHoraent2.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(etHoraent2.getCurrentMinute()), "0", 2, 2);
        String stHorasal2 = replicate(String.valueOf(etHorasal2.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(etHorasal2.getCurrentMinute()), "0", 2, 2);
        String stHoraent3 = replicate(String.valueOf(etHoraent3.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(etHoraent3.getCurrentMinute()), "0", 2, 2);
        String stHorasal3 = replicate(String.valueOf(etHorasal3.getCurrentHour()), "0", 2, 1) + ":" + replicate(String.valueOf(etHorasal3.getCurrentMinute()), "0", 2, 2);

        if(!stHoraent1.equals("00:00") || !stHorasal1.equals("00:00")){
            quest.setHoraent1(stHoraent1);
            quest.setHorasal1(stHorasal1);
        }

        if(!stHoraent2.equals("00:00") || !stHorasal2.equals("00:00")) {
            quest.setHoraent2(stHoraent2);
            quest.setHorasal2(stHorasal2);
        }

        if(!stHoraent3.equals("00:00") || !stHorasal3.equals("00:00")) {
            quest.setHoraent3(stHoraent3);
            quest.setHorasal3(stHorasal3);
        }

        //P8
        RadioGroup rgNmodos = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nmodos);
        EditText etNmodos = (EditText) activity.findViewById(R.id.survey_edit_nmodos_otros);

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
                case R.id.survey_radio_nmodos_nmodos:
                    selectedCode = stringToInt(etNmodos.getText().toString());
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }

        quest.setNmodos(String.valueOf(selectedCode));

        //P9
        RadioGroup rgModo1 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_1modo);
        RadioGroup rgModo2 = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_2modo);
        RadioGroup rgUModo = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_umodo);

        selectedCode = -1;
        checkedId = rgModo1.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_ultimodo_1modo_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option7:
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option8:
                    selectedCode = 8;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option9:
                    selectedCode = 9;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option10:
                    selectedCode = 10;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option11:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_ultimodo_1modo_option13:
                    selectedCode = 13;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }

        quest.setModo1(String.valueOf(selectedCode));

        selectedCode = -1;
        checkedId = rgModo2.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_ultimodo_2modo_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option7:
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option8:
                    selectedCode = 8;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option9:
                    selectedCode = 9;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option10:
                    selectedCode = 10;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option11:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_ultimodo_2modo_option13:
                    selectedCode = 13;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }

        quest.setModo2(String.valueOf(selectedCode));

        selectedCode = -1;
        checkedId = rgUModo.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_ultimodo_umodo_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option7:
                    selectedCode = 7;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option8:
                    selectedCode = 8;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option9:
                    selectedCode = 9;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option10:
                    selectedCode = 10;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option11:
                    selectedCode = 11;
                    break;
                case R.id.survey_radio_ultimodo_umodo_option13:
                    selectedCode = 13;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }

        quest.setUltimodo(String.valueOf(selectedCode));

        //P10
        RadioGroup rgNocucoche = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_nocucoche);

        selectedCode = -1;
        checkedId = rgNocucoche.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_nocucoche_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_nocucoche_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_nocucoche_option3:
                    selectedCode = 3;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setNocucoche(String.valueOf(selectedCode));

        //P11
        RadioGroup rgSatistranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_satistranspubli);

        selectedCode = -1;
        checkedId = rgSatistranspubli.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_satistranspubli_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_satistranspubli_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_satistranspubli_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_satistranspubli_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_satistranspubli_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_satistranspubli_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setSatistranspubli(String.valueOf(selectedCode));

        //P12
        EditText etValtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli1);
        EditText etValtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli2);
        EditText etValtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli3);
        EditText etValtranspubli_otro = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli);

        if (!etValtranspubli_1.getText().toString().equals("")) {
            quest.setValtranspubli1(etValtranspubli_1.getText().toString());
        } else {
            quest.setValtranspubli1("-1");
        }

        if (!etValtranspubli_2.getText().toString().equals("")) {
            quest.setValtranspubli2(etValtranspubli_2.getText().toString());
        } else {
            quest.setValtranspubli2("-1");
        }

        if (!etValtranspubli_3.getText().toString().equals("")) {
            quest.setValtranspubli3(etValtranspubli_3.getText().toString());
        } else {
            quest.setValtranspubli3("-1");
        }

        if (etValtranspubli_1.getText().toString().equals("9") || etValtranspubli_2.getText().toString().equals("9") || etValtranspubli_3.getText().toString().equals("9")) {
            quest.setValtranspubliotro(etValtranspubli_otro.getText().toString());
        }

        //P13
        EditText etMejtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli1);
        EditText etMejtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli2);
        EditText etMejtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli3);
        EditText etMejtranspubli_otro = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli);

        if (!etMejtranspubli_1.getText().toString().equals("")) {
            quest.setMejtranspubli1(etMejtranspubli_1.getText().toString());
        } else {
            quest.setMejtranspubli1("-1");
        }

        if (!etMejtranspubli_2.getText().toString().equals("")) {
            quest.setMejtranspubli2(etMejtranspubli_2.getText().toString());
        } else {
            quest.setMejtranspubli2("-1");
        }

        if (!etMejtranspubli_3.getText().toString().equals("")) {
            quest.setMejtranspubli3(etMejtranspubli_3.getText().toString());
        } else {
            quest.setMejtranspubli3("-1");
        }

        if (etMejtranspubli_1.getText().toString().equals("9") || etMejtranspubli_2.getText().toString().equals("9") || etMejtranspubli_3.getText().toString().equals("9")) {
            quest.setMejtranspubliotro(etMejtranspubli_otro.getText().toString());
        }

        //P14
        EditText etDesplazatrab = (EditText) activity.findViewById(R.id.survey_edit_desplazatrab);

        quest.setDesplazatrab(etDesplazatrab.getText().toString());

        //P15
        RadioGroup rgAparcatrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_aparctrab);

        selectedCode = -1;
        checkedId = rgAparcatrab.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_aparctrab_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_aparctrab_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_aparctrab_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_aparctrab_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_aparctrab_option9:
                    selectedCode = 9;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setAparctrab(String.valueOf(selectedCode));

        //P16
        EditText etNotranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli1);
        EditText etNotranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli2);
        EditText etNotranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli3);
        EditText etNotranspubli_otro = (EditText) activity.findViewById(R.id.survey_edit_notranspubli);

        if (!etNotranspubli_1.getText().toString().equals("")) {
            quest.setNotranspubli1(etNotranspubli_1.getText().toString());
        } else {
            quest.setNotranspubli1("-1");
        }

        if (!etNotranspubli_2.getText().toString().equals("")) {
            quest.setNotranspubli2(etNotranspubli_2.getText().toString());
        } else {
            quest.setNotranspubli2("-1");
        }

        if (!etNotranspubli_3.getText().toString().equals("")) {
            quest.setNotranspubli3(etNotranspubli_3.getText().toString());
        } else {
            quest.setNotranspubli3("-1");
        }

        if (etNotranspubli_1.getText().toString().equals("10") || etNotranspubli_2.getText().toString().equals("10") || etNotranspubli_3.getText().toString().equals("10")) {
            quest.setNotranspubliotro(etNotranspubli_otro.getText().toString());
        }

        //P17
        RadioGroup rgDisptranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_disptranspubli);

        selectedCode = -1;
        checkedId = rgDisptranspubli.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_disptranspubli_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_disptranspubli_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_disptranspubli_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_disptranspubli_option4:
                    selectedCode = 4;
                    break;
                case R.id.survey_radio_disptranspubli_option5:
                    selectedCode = 5;
                    break;
                case R.id.survey_radio_disptranspubli_option6:
                    selectedCode = 6;
                    break;
                case R.id.survey_radio_disptranspubli_option7:
                    selectedCode = 7;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setDisptranspubli(String.valueOf(selectedCode));

        //P18
        EditText etImportranspubli = (EditText) activity.findViewById(R.id.survey_edit_importranspubli);

        quest.setImportranspubli(etImportranspubli.getText().toString());

        //P19
        EditText etMedtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli1);
        EditText etMedtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli2);
        EditText etMedtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli3);
        EditText etMedtranspubli_otro = (EditText) activity.findViewById(R.id.survey_edit_medtranspubli);

        if (!etMedtranspubli_1.getText().toString().equals("")) {
            quest.setMedtranspubli1(etMedtranspubli_1.getText().toString());
        } else {
            quest.setMedtranspubli1("-1");
        }

        if (!etMedtranspubli_2.getText().toString().equals("")) {
            quest.setMedtranspubli2(etMedtranspubli_2.getText().toString());
        } else {
            quest.setMedtranspubli2("-1");
        }

        if (!etMedtranspubli_3.getText().toString().equals("")) {
            quest.setMedtranspubli3(etMedtranspubli_3.getText().toString());
        } else {
            quest.setMedtranspubli3("-1");
        }

        if (etMedtranspubli_1.getText().toString().equals("99") || etMedtranspubli_2.getText().toString().equals("99") || etMedtranspubli_3.getText().toString().equals("99")) {
            quest.setMedtranspubliotro(etMedtranspubli_otro.getText().toString());
        }

        //P20
        EditText etTiempotranspubli = (EditText) activity.findViewById(R.id.survey_edit_tiempotranspubli);

        quest.setTiempotranspubli(etTiempotranspubli.getText().toString());

        //P21
        EditText etCompartcoche_1 = (EditText) activity.findViewById(R.id.survey_edit_compartcoche1);
        EditText etCompartcoche_2 = (EditText) activity.findViewById(R.id.survey_edit_compartcoche2);
        EditText etCompartcoche_3 = (EditText) activity.findViewById(R.id.survey_edit_compartcoche3);
        EditText etCompartcoche_otro = (EditText) activity.findViewById(R.id.survey_edit_compartcoche);

        if (!etCompartcoche_1.getText().toString().equals("")) {
            quest.setCompartcoche1(etCompartcoche_1.getText().toString());
        } else {
            quest.setCompartcoche1("-1");
        }

        if (!etCompartcoche_2.getText().toString().equals("")) {
            quest.setCompartcoche2(etCompartcoche_2.getText().toString());
        } else {
            quest.setCompartcoche2("-1");
        }

        if (!etCompartcoche_3.getText().toString().equals("")) {
            quest.setCompartcoche3(etCompartcoche_3.getText().toString());
        } else {
            quest.setCompartcoche3("-1");
        }

        if (etCompartcoche_1.getText().toString().equals("5") || etCompartcoche_2.getText().toString().equals("5") || etCompartcoche_3.getText().toString().equals("5")) {
            quest.setCompartcocheotro(etCompartcoche_otro.getText().toString());
        }

        //P22
        EditText etDispbici_1 = (EditText) activity.findViewById(R.id.survey_edit_dispbici1);
        EditText etDispbici_2 = (EditText) activity.findViewById(R.id.survey_edit_dispbici2);
        EditText etDispbici_3 = (EditText) activity.findViewById(R.id.survey_edit_dispbici3);
        EditText etDispbici_otro = (EditText) activity.findViewById(R.id.survey_edit_dispbici);

        if (!etDispbici_1.getText().toString().equals("")) {
            quest.setDispbici1(etDispbici_1.getText().toString());
        } else {
            quest.setDispbici1("-1");
        }

        if (!etDispbici_2.getText().toString().equals("")) {
            quest.setDispbici2(etDispbici_2.getText().toString());
        } else {
            quest.setDispbici2("-1");
        }

        if (!etDispbici_3.getText().toString().equals("")) {
            quest.setDispbici3(etDispbici_3.getText().toString());
        } else {
            quest.setDispbici3("-1");
        }

        if (etDispbici_1.getText().toString().equals("6") || etDispbici_2.getText().toString().equals("6") || etDispbici_3.getText().toString().equals("6")) {
            quest.setDispbiciotro(etDispbici_otro.getText().toString());
        }

        //P23
        RadioGroup rgModosalida = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_modosalida);
        EditText etModosalida_otro = (EditText) activity.findViewById(R.id.survey_edit_modosalida_indique);

        selectedCode = -1;
        checkedId = rgModosalida.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_modosalida_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_modosalida_option2:
                    selectedCode = 2;
                    quest.setModosalidaotro(etModosalida_otro.getText().toString());
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setModosalida(String.valueOf(selectedCode));

        //P24
        RadioGroup rgCdedadtrab = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_cdedadtrab);

        selectedCode = -1;
        checkedId = rgCdedadtrab.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_cdedadtrab_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_cdedadtrab_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_cdedadtrab_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_cdedadtrab_option4:
                    selectedCode = 4;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setCdedadtrab(String.valueOf(selectedCode));

        //P25
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

        //P26
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
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setCdslab(String.valueOf(selectedCode));

        //P27
        RadioGroup rgPuesto = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_puesto);

        selectedCode = -1;
        checkedId = rgPuesto.getCheckedRadioButtonId();

        if (checkedId > 0) {
            switch (checkedId) {
                case R.id.survey_radio_puesto_option1:
                    selectedCode = 1;
                    break;
                case R.id.survey_radio_puesto_option2:
                    selectedCode = 2;
                    break;
                case R.id.survey_radio_puesto_option3:
                    selectedCode = 3;
                    break;
                case R.id.survey_radio_puesto_option4:
                    selectedCode = 4;
                    break;
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setPuesto(String.valueOf(selectedCode));

        return quest;
    }

    private boolean guardaDB(String nombreBD, String valor) {
        DBHelper conn = new DBHelper(activity.getApplicationContext());
        SQLiteDatabase db = conn.getWritableDatabase();

        System.out.println("UPDATE " + Contracts.TABLE_CUETRABAJADORES  + " SET " + nombreBD + " = '" + valor + "', " + Contracts.COLUMN_CUETRABAJADORES_PREGUNTA + " = " + pregunta + ", " + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + " = 0 WHERE " + Contracts.COLUMN_CUETRABAJADORES_IDEN + " = " + idCue);
        db.execSQL("UPDATE " + Contracts.TABLE_CUETRABAJADORES + " SET " + nombreBD + " = '" + valor + "', " + Contracts.COLUMN_CUETRABAJADORES_PREGUNTA + " = " + pregunta + ", " + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + " = 0 WHERE " + Contracts.COLUMN_CUETRABAJADORES_IDEN + " = " + idCue);

        return true;
    }

    private boolean borraDB(String nombreBD) {
        DBHelper conn = new DBHelper(activity.getApplicationContext());
        SQLiteDatabase db = conn.getWritableDatabase();

        db.execSQL("UPDATE " + Contracts.TABLE_CUETRABAJADORES + " SET " + nombreBD + " = NULL, " + Contracts.COLUMN_CUETRABAJADORES_PREGUNTA + " = " + pregunta + ", " + Contracts.COLUMN_CUETRABAJADORES_ENVIADO + " = 0 WHERE " + Contracts.COLUMN_CUETRABAJADORES_IDEN + " = " + idCue);

        return true;
    }

    public void setCue(CueTrabajadores cue){

        this.cue = cue;
    }

    private void setPreguntaAnterior(int pregunta){

        preguntaAnterior = pregunta;
    }

    public int getPreguntaAnterior(){

        return preguntaAnterior;
    }

    private List<String> getDiccionario(String provincia) {
        List<String> getDiccionario = new ArrayList<String>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT T.iden, T.codigo ||'. '|| T.descripcion " +
                                        "FROM " +
                                        "        (SELECT iden, codigo, descripcion, 1 AS aux " +
                                        "                FROM TipoMunicipios " +
                                        "                WHERE iden = 0 " +
                                        "                UNION " +
                                        "                SELECT iden, codigo, descripcion, 2 AS aux " +
                                        "                FROM TipoMunicipios " +
                                        "                WHERE provincia = " + provincia + " " +
                                        "                UNION " +
                                        "                SELECT iden, codigo, descripcion, 3 AS aux " +
                                        "                FROM TipoMunicipios " +
                                        "                WHERE provincia <> " + provincia + " AND iden <> 0) AS T " +
                                        "ORDER BY T.aux" , parametros);

        while (cursor.moveToNext()) {
            getDiccionario.add(cursor.getString(1));
        }

        return getDiccionario;
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

    private List<String> getDiccionarioNoCod(String tabla, String campoIden, String campoCod, String campoValor, String campoOrden, String filtro) {
        List<String> getDiccionario = new ArrayList<String>();
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = null;

        Cursor cursor = db.rawQuery("SELECT " + campoIden + ", " + campoValor +
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

    public String replicate (String origen, String relleno, int max, int tipo){
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

    private void setTimePickerInterval(TimePicker timePicker) {
        try {
            int TIME_PICKER_INTERVAL = 15;
            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }
}
