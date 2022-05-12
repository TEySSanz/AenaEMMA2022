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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.testadistica.www.aenaemma2022.R;
import es.testadistica.www.aenaemma2022.entidades.CueTrabajadores;

public class ModeloTrabajadores1 extends FormTrab {

    private int preguntaAnterior = 1;
    private int idCue;
    private int finCue = 27;
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
        ArrayAdapter<String> empresasAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOPAISES,"iden", "codigo","descripcion", "codigo"));
        empresasAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_empresa;
        survey_spinner_empresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);
        survey_spinner_empresa.setAdapter(empresasAdapter);
        survey_spinner_empresa.setTitle(activity.getString(R.string.survey_text_empresa));
        survey_spinner_empresa.setPositiveButton(activity.getString(R.string.spinner_close));

        //P2
        ArrayAdapter<String> actempreAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOPAISES,"iden", "codigo","descripcion", "codigo"));
        actempreAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_actempre;
        survey_spinner_actempre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_actempre);
        survey_spinner_actempre.setAdapter(actempreAdapter);
        survey_spinner_actempre.setTitle(activity.getString(R.string.survey_text_actempre));
        survey_spinner_actempre.setPositiveButton(activity.getString(R.string.spinner_close));

        //P3
        ArrayAdapter<String> cdlocadoAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPOMUNICIPIOS,"iden", "codigo","descripcion", "codigo"));
        cdlocadoAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_cdlocado;
        survey_spinner_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);
        survey_spinner_cdlocado.setAdapter(cdlocadoAdapter);
        survey_spinner_cdlocado.setTitle(activity.getString(R.string.survey_text_cdlocado_loc));
        survey_spinner_cdlocado.setPositiveButton(activity.getString(R.string.spinner_close));

        ArrayAdapter<String> distresAdapter = new ArrayAdapter<String>(activity, R.layout.selection_spinner_item_small, getDiccionario(Contracts.TABLE_TIPODISTRITOS,"iden", "codigo","descripcion", "codigo", " ciudad like '%MAD%' "));
        distresAdapter.setDropDownViewResource(R.layout.selection_spinner_item);

        SearchableSpinner survey_spinner_distres;
        survey_spinner_distres = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_distres);
        survey_spinner_distres.setAdapter(distresAdapter);
        survey_spinner_distres.setTitle(activity.getString(R.string.survey_text_distres));
        survey_spinner_distres.setPositiveButton(activity.getString(R.string.spinner_close));
    }

    private void condicionesSpinners() {

        //P3
        final SearchableSpinner sp_cdlocado = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_cdlocado);

        sp_cdlocado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
    }

    private void condicionesRadioButton() {

        //P4
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
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
            }
        });

        rbJornada_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
            }
        });

        rbJornada_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
            }
        });

        rbJornada_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
            }
        });

        rbJornada_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
            }
        });

        rbJornada_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(GONE);
            }
        });

        rbJornada_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_jornada_otros).setVisibility(VISIBLE);
            }
        });


        //P8
        final RadioButton rbNmodos_1 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_option1);
        final RadioButton rbNmodos_2 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_option2);
        final RadioButton rbNmodos_3 = (RadioButton) activity.findViewById(R.id.survey_radio_nmodos_nmodos);

        rbNmodos_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_nmodos_otros).setVisibility(GONE);
            }
        });

        rbNmodos_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_nmodos_otros).setVisibility(GONE);
            }
        });

        rbNmodos_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.findViewById(R.id.survey_edit_nmodos_otros).setVisibility(VISIBLE);
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
    }

    private void iniciarCheckBox(){

        //P12
        final CheckBox cbValtranspubli_1 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option1);
        final CheckBox cbValtranspubli_2 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option2);
        final CheckBox cbValtranspubli_3 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option3);
        final CheckBox cbValtranspubli_4 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option4);
        final CheckBox cbValtranspubli_5 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option5);
        final CheckBox cbValtranspubli_6 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option6);
        final CheckBox cbValtranspubli_7 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option7);
        final CheckBox cbValtranspubli_8 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option8);
        final CheckBox cbValtranspubli_9 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option9);
        final CheckBox cbValtranspubli_10 = (CheckBox) activity.findViewById(R.id.check_valtranspubli_option10);

        final ArrayList<String> listValtranspubli = new ArrayList<String>();

        final EditText etValtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli1);
        final EditText etValtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli2);
        final EditText etValtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli3);

        cbValtranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

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

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_9.setChecked(false);
                }

                if(cbValtranspubli_9.isChecked()) {
                    listValtranspubli.add("9");
                } else {
                    listValtranspubli.remove("9");
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

        cbValtranspubli_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = (cbValtranspubli_1.isChecked()? 1:0) + (cbValtranspubli_2.isChecked()? 1:0) + (cbValtranspubli_3.isChecked()? 1:0) + (cbValtranspubli_4.isChecked()? 1:0) +
                        (cbValtranspubli_5.isChecked()? 1:0) + (cbValtranspubli_6.isChecked()? 1:0) + (cbValtranspubli_7.isChecked()? 1:0) + (cbValtranspubli_8.isChecked()? 1:0) +
                        (cbValtranspubli_9.isChecked()? 1:0) + (cbValtranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbValtranspubli_10.setChecked(false);
                }

                if(cbValtranspubli_10.isChecked()) {
                    listValtranspubli.add("10");
                } else {
                    listValtranspubli.remove("10");
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
        final CheckBox cbMejtranspubli_1 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option1);
        final CheckBox cbMejtranspubli_2 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option2);
        final CheckBox cbMejtranspubli_3 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option3);
        final CheckBox cbMejtranspubli_4 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option4);
        final CheckBox cbMejtranspubli_5 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option5);
        final CheckBox cbMejtranspubli_6 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option6);
        final CheckBox cbMejtranspubli_9 = (CheckBox) activity.findViewById(R.id.check_mejtranspubli_option9);

        final ArrayList<String> listMejtranspubli = new ArrayList<String>();

        final EditText etMejtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli1);
        final EditText etMejtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli2);
        final EditText etMejtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_mejtranspubli3);

        cbMejtranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                int count = (cbMejtranspubli_1.isChecked()? 1:0) + (cbMejtranspubli_2.isChecked()? 1:0) + (cbMejtranspubli_3.isChecked()? 1:0) + (cbMejtranspubli_4.isChecked()? 1:0) +
                        (cbMejtranspubli_5.isChecked()? 1:0) + (cbMejtranspubli_6.isChecked()? 1:0) + (cbMejtranspubli_9.isChecked()? 1:0);

                if (count > 3){
                    cbMejtranspubli_9.setChecked(false);
                }

                if(cbMejtranspubli_9.isChecked()) {
                    listMejtranspubli.add("9");
                } else {
                    listMejtranspubli.remove("9");
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

        //P15
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

        final ArrayList<String> listNotranspubli = new ArrayList<String>();

        final EditText etNotranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli1);
        final EditText etNotranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli2);
        final EditText etNotranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_notranspubli3);

        cbNotranspubli_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                int count = (cbNotranspubli_1.isChecked()? 1:0) + (cbNotranspubli_2.isChecked()? 1:0) + (cbNotranspubli_3.isChecked()? 1:0) + (cbNotranspubli_4.isChecked()? 1:0) +
                        (cbNotranspubli_5.isChecked()? 1:0) + (cbNotranspubli_6.isChecked()? 1:0) + (cbNotranspubli_7.isChecked()? 1:0) + (cbNotranspubli_8.isChecked()? 1:0) +
                        (cbNotranspubli_9.isChecked()? 1:0) + (cbNotranspubli_10.isChecked()? 1:0);

                if (count > 3){
                    cbNotranspubli_10.setChecked(false);
                }

                if(cbNotranspubli_10.isChecked()) {
                    listNotranspubli.add("10");
                } else {
                    listNotranspubli.remove("10");
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

        //P18
        final CheckBox cbMedtranspubli_7 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option7);
        final CheckBox cbMedtranspubli_8 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option8);
        final CheckBox cbMedtranspubli_10 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option10);
        final CheckBox cbMedtranspubli_11 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option11);
        final CheckBox cbMedtranspubli_99 = (CheckBox) activity.findViewById(R.id.check_medtranspubli_option99);

        final ArrayList<String> listMedtranspubli = new ArrayList<String>();

        final EditText etMedtranspubli_1 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli1);
        final EditText etMedtranspubli_2 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli2);
        final EditText etMedtranspubli_3 = (EditText) activity.findViewById(R.id.survey_edit_valtranspubli3);

        cbMedtranspubli_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                int count = (cbMedtranspubli_7.isChecked()? 1:0) + (cbMedtranspubli_8.isChecked()? 1:0) + (cbMedtranspubli_10.isChecked()? 1:0) + (cbMedtranspubli_11.isChecked()? 1:0) +
                        (cbMedtranspubli_99.isChecked()? 1:0);

                if (count > 3){
                    cbMedtranspubli_99.setChecked(false);
                }

                if(cbMedtranspubli_99.isChecked()) {
                    listMedtranspubli.add("99");
                } else {
                    listMedtranspubli.remove("99");
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
                LinearLayout p15 = (LinearLayout) activity.findViewById(R.id.survey_layout_notranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p15.setVisibility(VISIBLE);
                break;
            case 16:
                //P16
                RelativeLayout p16 = (RelativeLayout) activity.findViewById(R.id.survey_layout_disptranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p16.setVisibility(VISIBLE);
                break;
            case 17:
                //P17
                LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_importranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p17.setVisibility(VISIBLE);
                break;
            case 18:
                //P18
                LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_medtranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p18.setVisibility(VISIBLE);
                break;
            case 19:
                //P19
                LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_tiempotranspubli);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p19.setVisibility(VISIBLE);
                break;
            case 20:
                //P20
                RelativeLayout p20 = (RelativeLayout) activity.findViewById(R.id.survey_layout_aparctrab);
                previo.setVisibility(VISIBLE);
                save.setVisibility(VISIBLE);
                next.setVisibility(VISIBLE);
                p20.setVisibility(VISIBLE);
                break;
            case 27:
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

                    break;
                case 2:
                    //P2

                    break;
                case 3:
                    //P3

                    break;
                case 4:
                    //P4
                    final RadioButton rbJornada_1 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option1);
                    final RadioButton rbJornada_2 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option2);
                    final RadioButton rbJornada_3 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option3);
                    final RadioButton rbJornada_4 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option4);
                    final RadioButton rbJornada_5 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option5);
                    final RadioButton rbJornada_6 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option6);
                    final RadioButton rbJornada_9 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option9);

                    if (!rbJornada_1.isChecked() && !rbJornada_2.isChecked() && !rbJornada_3.isChecked() && !rbJornada_4.isChecked() && !rbJornada_5.isChecked()
                            && !rbJornada_6.isChecked() && !rbJornada_9.isChecked()) {
                        Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.survey_text_selectOption), Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }
                    break;
                case 5:
                    //P5

                    break;
                case 6:
                    //P6

                    break;
                case 7:
                    //P7

                    break;
                case 8:
                    //P8

                    break;
                case 9:
                    //P9

                    break;
                case 10:
                    //P10
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
                    final RadioButton rbUmodo_12 = (RadioButton) activity.findViewById(R.id.survey_radio_ultimodo_umodo_option12);

                    if (!rbUmodo_1.isChecked() && !rbUmodo_2.isChecked() && !rbUmodo_3.isChecked() && !rbUmodo_4.isChecked() && !rbUmodo_5.isChecked()
                            && !rbUmodo_6.isChecked() && !rbUmodo_7.isChecked() && !rbUmodo_8.isChecked() && !rbUmodo_9.isChecked() && !rbUmodo_10.isChecked()
                            && !rbUmodo_11.isChecked() && !rbUmodo_12.isChecked()) {
                        Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.survey_text_selectOption), Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }
                    break;
                case 11:
                    //P11

                    break;
                case 12:
                    //P12

                    break;
                case 13:
                    //P13

                    break;
                case 14:
                    //P14

                    break;
                case 15:
                    //P15

                    break;
                case 16:
                    //P16
                    final RadioButton rbDisptranspubli_1 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option1);
                    final RadioButton rbDisptranspubli_2 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option2);
                    final RadioButton rbDisptranspubli_3 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option3);
                    final RadioButton rbDisptranspubli_4 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option4);
                    final RadioButton rbDisptranspubli_5 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option5);
                    final RadioButton rbDisptranspubli_6 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option6);
                    final RadioButton rbDisptranspubli_7 = (RadioButton) activity.findViewById(R.id.survey_radio_disptranspubli_option7);

                    if (!rbDisptranspubli_1.isChecked() && !rbDisptranspubli_2.isChecked() && !rbDisptranspubli_3.isChecked() && !rbDisptranspubli_4.isChecked() &&
                            !rbDisptranspubli_5.isChecked() && !rbDisptranspubli_6.isChecked() && !rbDisptranspubli_7.isChecked()) {
                        Toast toast = Toast.makeText(activity, activity.getResources().getString(R.string.survey_text_selectOption), Toast.LENGTH_LONG);
                        toast.show();
                        return false;
                    }
                    break;
                case 17:
                    //P17

                    break;
                case 18:
                    //P18

                    break;
                case 19:
                    //P19

                    break;
                case 20:
                    //P20

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

                    break;
                case 3:
                    //P3
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_CDLOCADO, cue.getCdlocado());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISTRES, cue.getDistres());

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
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI1, cue.getNotranspubli1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI2, cue.getNotranspubli2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI3, cue.getNotranspubli3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO, cue.getNotranspubliotro());

                    break;
                case 16:
                    //P16
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLI, cue.getDisptranspubli());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO, cue.getDisptranspubliotro());

                    break;
                case 17:
                    //P17
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI, cue.getImportranspubli());

                    break;
                case 18:
                    //P18
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1, cue.getMedtranspubli1());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2, cue.getMedtranspubli2());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3, cue.getMedtranspubli3());
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO, cue.getMedtranspubliotro());

                    break;
                case 19:
                    //P19
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI, cue.getTiempotranspubli());

                    break;
                case 20:
                    //P20
                    guardaDB(Contracts.COLUMN_CUETRABAJADORES_APARCTRAB, cue.getAparctrab());

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

                    break;
                case 3:
                    //P3
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_CDLOCADO);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISTRES);

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
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_NOTRANSPUBLIOTRO);

                    break;
                case 16:
                    //P16
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLI);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_DISPTRANSPUBLIOTRO);

                    break;
                case 17:
                    //P17
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_IMPORTRANSPUBLI);

                    break;
                case 18:
                    //P18
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI1);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI2);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLI3);
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_MEDTRANSPUBLIOTRO);

                    break;
                case 19:
                    //P19
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_TIEMPOTRANSPUBLI);

                    break;
                case 20:
                    //P20
                    borraDB(Contracts.COLUMN_CUETRABAJADORES_APARCTRAB);

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
        LinearLayout p15 = (LinearLayout) activity.findViewById(R.id.survey_layout_notranspubli);
        p15.setVisibility(GONE);

        //P16
        RelativeLayout p16 = (RelativeLayout) activity.findViewById(R.id.survey_layout_disptranspubli);
        p16.setVisibility(GONE);

        //P17
        LinearLayout p17 = (LinearLayout) activity.findViewById(R.id.survey_layout_importranspubli);
        p17.setVisibility(GONE);

        //P18
        LinearLayout p18 = (LinearLayout) activity.findViewById(R.id.survey_layout_medtranspubli);
        p18.setVisibility(GONE);

        //P19
        LinearLayout p19 = (LinearLayout) activity.findViewById(R.id.survey_layout_tiempotranspubli);
        p19.setVisibility(GONE);

        //P20
        RelativeLayout p20 = (RelativeLayout) activity.findViewById(R.id.survey_layout_aparctrab);
        p20.setVisibility(GONE);

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
                                show = showQuestion(22); //>P22
                                break;
                            case R.id.survey_radio_ultimodo_umodo_option2:
                                show = showQuestion(22); //>P22
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
                            case R.id.survey_radio_ultimodo_umodo_option12:
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
                if (activated) {
                    RadioGroup rgDistptranspubli = (RadioGroup) activity.findViewById(R.id.survey_radiogroup_disptranspubli);
                    checkedId = rgDistptranspubli.getCheckedRadioButtonId();

                    if (checkedId > 0) {
                        switch (checkedId) {
                            case R.id.survey_radio_disptranspubli_option1:
                                show = showQuestion(17); //>P17
                                break;
                            case R.id.survey_radio_disptranspubli_option2:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_disptranspubli_option3:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_disptranspubli_option4:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_disptranspubli_option5:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_disptranspubli_option6:
                                show = showQuestion(18); //>P18
                                break;
                            case R.id.survey_radio_disptranspubli_option7:
                                show = showQuestion(18); //>P18
                                break;
                            default:
                                show = showQuestion(17); //>P17
                                break;
                        }
                    } else {
                        show = showQuestion(17); //>P17
                    }
                } else {
                    show = showQuestion(17);
                }
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
        }

        return show;
    }

    public CueTrabajadores fillQuest(CueTrabajadores quest) {
        int selectedCode = -1;
        int checkedId = -1;

        //P1
        SearchableSpinner spEmpresa = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_empresa);
        String textSpEmpresa = spEmpresa.getSelectedItem().toString().substring(0,3);
        if(!textSpEmpresa.contains("000")){
            quest.setEmpresa(textSpEmpresa);
        } else {
            quest.setEmpresa("-1");
        }

        //P2
        SearchableSpinner spActempre = (SearchableSpinner) activity.findViewById(R.id.survey_spinner_actempre);
        String textSpActempre = spActempre.getSelectedItem().toString().substring(0,3);
        if(!textSpActempre.contains("000")){
            quest.setActempre(textSpActempre);
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
        String textSpDistres = sp_distres.getSelectedItem().toString().substring(0,2);
        if(!textSpDistres.contains("00") && textSpCdlocado.contains("28079")){
            quest.setDistres(textSpDistres);
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
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setZonatrab(String.valueOf(selectedCode));

        //P7
        /*
        EditText etHoraent1 = (EditText) activity.findViewById(R.id.survey_edit_horaent1);
        EditText etHoraent2 = (EditText) activity.findViewById(R.id.survey_edit_horaent2);
        EditText etHoraent3 = (EditText) activity.findViewById(R.id.survey_edit_horaent3);
        EditText etHorasal1 = (EditText) activity.findViewById(R.id.survey_edit_horasal1);
        EditText etHorasal2 = (EditText) activity.findViewById(R.id.survey_edit_horasal2);
        EditText etHorasal3 = (EditText) activity.findViewById(R.id.survey_edit_horasal3);
        */
        TimePicker etHoraent1 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent1);
        TimePicker etHoraent2 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent2);
        TimePicker etHoraent3 = (TimePicker) activity.findViewById(R.id.survey_edit_horaent3);
        TimePicker etHorasal1 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal1);
        TimePicker etHorasal2 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal2);
        TimePicker etHorasal3 = (TimePicker) activity.findViewById(R.id.survey_edit_horasal3);

        quest.setHoraent1(etHoraent1.toString());
        quest.setHoraent2(etHoraent2.toString());
        quest.setHoraent3(etHoraent3.toString());
        quest.setHorasal1(etHorasal1.toString());
        quest.setHorasal2(etHorasal2.toString());
        quest.setHorasal3(etHorasal3.toString());

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
                    selectedCode = Integer.valueOf(etNmodos.getText().toString());
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

        if (etValtranspubli_1.getText().toString().equals("10") || etValtranspubli_2.getText().toString().equals("10") || etValtranspubli_3.getText().toString().equals("10")) {
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

        //P16
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

        //P17
        EditText etImportranspubli = (EditText) activity.findViewById(R.id.survey_edit_importranspubli);

        quest.setImportranspubli(etImportranspubli.getText().toString());

        //P18
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

        //P19
        EditText etTiempotranspubli = (EditText) activity.findViewById(R.id.survey_edit_tiempotranspubli);

        quest.setTiempotranspubli(etTiempotranspubli.getText().toString());

        //P20
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
                default:
                    selectedCode = 9;
                    break;
            }
        }

        quest.setAparctrab(String.valueOf(selectedCode));

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
