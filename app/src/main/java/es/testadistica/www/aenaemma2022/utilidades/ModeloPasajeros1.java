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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

                System.out.println(filtro);

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
        if(!textSpDistres.contains("00") && textSpCdpaisre.contains("28079")){
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
