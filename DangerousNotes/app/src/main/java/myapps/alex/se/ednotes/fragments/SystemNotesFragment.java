package myapps.alex.se.ednotes.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.persistence.Storage;

public class SystemNotesFragment extends Fragment {
    private String mSystemName;
    private myapps.alex.se.ednotes.model.System mLoadedSystem;
    private EditText mNotesEditText;

    public SystemNotesFragment() {
        Log.d("SystemNotesFragment says", "hi");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSystemName = getActivity().getIntent().getStringExtra(AppConstants.SYSTEM_NAME);
        mLoadedSystem = Storage.loadSystem(mSystemName);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.system_notes_fragment, container, false);
        mNotesEditText = (EditText) inflatedView.findViewById(R.id.notes_edittext);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/eurostile.TTF");
        mNotesEditText.setTypeface(font);

        View scrollView = inflatedView.findViewById(R.id.notes_scrollview);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mNotesEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                return false;
            }
        });


        return inflatedView;
    }

    private void saveNotes(){
        mLoadedSystem.setNotes(mNotesEditText.getText().toString());
        Storage.saveSystem(mLoadedSystem);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String systemNotes  = mLoadedSystem.getNotes();

        if (systemNotes != null) {
            mNotesEditText.setText(systemNotes);
            mNotesEditText.setSelection(systemNotes.length());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null) {
            menu.clear();
        }

        inflater.inflate(R.menu.notes, menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            return true;
        }

        if (item.getItemId() == R.id.erase) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveNotes();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNotesEditText.getWindowToken(), 0);
    }

}
