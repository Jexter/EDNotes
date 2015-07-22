package myapps.alex.se.ednotes.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.DNApplication;
import myapps.alex.se.ednotes.model.MiniSystem;
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
        String notes = mNotesEditText.getText().toString();
        mLoadedSystem.setNotes(notes);
        Storage.setNotesForSystem(notes, mLoadedSystem);
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

        inflater.inflate(R.menu.notes_menu, menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            saveNotes();
            Toast.makeText(getActivity(), "Notes saved", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == R.id.delete) {
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/eurostile.TTF");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage("This will erase your notes.");
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   mLoadedSystem.setNotes(null);
                   mNotesEditText.setText(null);
                   Storage.saveSystem(mLoadedSystem);
                   Storage.setNotesForSystem(null, mLoadedSystem);
               }
            });
            alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Do nothing
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(font);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(font);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNotesEditText.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

}
