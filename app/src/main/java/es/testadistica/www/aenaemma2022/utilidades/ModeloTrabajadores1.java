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
    private int finCue = 41;
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
        final RadioButton rbJornada_7 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option7);

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

        rbJornada_7.setOnClickListener(new View.OnClickListener() {
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
                    final RadioButton rbJornada_7 = (RadioButton) activity.findViewById(R.id.survey_radio_jornada_option7);

                    if (!rbJornada_1.isChecked() && !rbJornada_2.isChecked() && !rbJornada_3.isChecked() && !rbJornada_4.isChecked() && !rbJornada_5.isChecked()
                            && !rbJornada_6.isChecked() && !rbJornada_7.isChecked()) {
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
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_jornada_option2:
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_jornada_option3:
                                show = showQuestion(5); //>P5
                                break;
                            case R.id.survey_radio_jornada_option4:
                                show = showQuestion(6); //>P6
                                break;
                            case R.id.survey_radio_jornada_option5:
                                show = showQuestion(6); //>P6
                                break;
                            case R.id.survey_radio_jornada_option6:
                                show = showQuestion(6); //>P6
                                break;
                            case R.id.survey_radio_jornada_option7:
                                show = showQuestion(6); //>P6
                                break;
                            default:
                                show = showQuestion(6); //>P6
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
                                show = showQuestion(6); //>P6
                                break;
                        }
                    } else {
                        show = showQuestion(10); //>P10
                    }
                } else {
                    show = showQuestion(10);
                }
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
                case R.id.survey_radio_jornada_option7:
                    selectedCode = 7;
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
                case R.id.survey_radio_ultimodo_1modo_option12:
                    selectedCode = 12;
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
                case R.id.survey_radio_ultimodo_2modo_option12:
                    selectedCode = 12;
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
                case R.id.survey_radio_ultimodo_umodo_option12:
                    selectedCode = 12;
                    break;
                default:
                    selectedCode = 99;
                    break;
            }
        }

        quest.setUltimodo(String.valueOf(selectedCode));

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
