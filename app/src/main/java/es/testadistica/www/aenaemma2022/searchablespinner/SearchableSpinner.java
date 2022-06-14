package es.testadistica.www.aenaemma2022.searchablespinner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;

import es.testadistica.www.aenaemma2022.R;

public class SearchableSpinner extends AppCompatSpinner implements View.OnTouchListener,
        SearchableListDialog.SearchableItem {

    public static final int NO_ITEM_SELECTED = -1;
    private Context _context;
    private SearchableListDialog _searchableListDialog;

    private boolean _isDirty;
    private String _strHintText;
    private String _strTitle;
    private String _strPositiveButtonText;
    ListAdapterSpinner adapter;
    private  int numberSelect,typeSpinner;
    private  ArrayList<mListString> adapterList;


    public SearchableSpinner(Context context) {
        super(context);
        this._context = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                _strHintText = a.getString(attr);
            }
        }
        a.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._context = context;
        init();
    }

    private void init() {
        _searchableListDialog = SearchableListDialog.newInstance
                (adapterList);
        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {

            if(_searchableListDialog!=null) _searchableListDialog=null;
            _searchableListDialog = SearchableListDialog.newInstance
                    (adapterList,typeSpinner,numberSelect,_strTitle,_strPositiveButtonText);
            _searchableListDialog.setOnSearchableItemClickListener(this);
            setOnTouchListener(this);
            if(adapter!=null) _searchableListDialog.show(scanForActivity(_context).getFragmentManager(), "TAG");
        }
        return true;
    }



    public void setAdapter(ArrayList<mListString> adapterlist,int typespinner,int numberselect, String title, String positiveButton) {
        adapter=new ListAdapterSpinner(_context, adapterlist,typespinner,_strTitle);
        super.setAdapter(adapter);
        this.numberSelect=numberselect;
        this.typeSpinner=typespinner;
        this.adapterList=adapterlist;
        this._strTitle=title;
        this._strPositiveButtonText=positiveButton;
        _searchableListDialog = SearchableListDialog.newInstance
                (adapterlist,typespinner,numberselect,_strTitle,_strPositiveButtonText);
        _searchableListDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);
    }



    @Override
    public void onSearchableItemClicked(int position) {
        setSelection(position);

        if (!_isDirty) {
            _isDirty = true;
            setSelection(position);
        }
    }

    public  void setTitleList(String title){
        _strHintText=title;
    }
    public void setTitle(String strTitle) {
        _searchableListDialog.setTitle(strTitle);
    }

    public void setPositiveButton(String strPositiveButtonText) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText);
    }

    public void setPositiveButton(String strPositiveButtonText, DialogInterface.OnClickListener onClickListener) {
        _searchableListDialog.setPositiveButton(strPositiveButtonText, onClickListener);
    }

    public void setOnSearchTextChangedListener(SearchableListDialog.OnSearchTextChanged onSearchTextChanged) {
        _searchableListDialog.setOnSearchTextChangedListener(onSearchTextChanged);
    }

    private Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());

        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(_strHintText) && !_isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }

}