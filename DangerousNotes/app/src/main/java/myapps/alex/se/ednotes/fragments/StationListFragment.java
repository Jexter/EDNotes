package myapps.alex.se.ednotes.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.adapters.StationListAdapter;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.persistence.Storage;

public class StationListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private StationListAdapter adapter;
    private View inflatedView;
    private String systemName;
    //private ArrayList<Station> loadedStations;
    private myapps.alex.se.ednotes.model.System loadedSystem;

    public StationListFragment() {
        Log.d("stationlistfragment says", "hi");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState != null) {
            Log.d("StationListFragment", "savedInstanceState IS NOT NULL! (got name: " + savedInstanceState.getString("systemName") + ")");
        }

        adapter = new StationListAdapter(getActivity());

        systemName = getActivity().getIntent().getStringExtra(AppConstants.SYSTEM_NAME);
        loadedSystem = Storage.loadSystem(systemName);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null) {
            menu.clear();
        }

        inflater.inflate(R.menu.station_list_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.station_list_fragment, container, false);

        return inflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ((ListView)view.findViewById(R.id.station_listview)).setAdapter(adapter);

        ArrayList<Station> stations = loadedSystem.getStations();

        if(stations != null && stations.size() > 0) {
            adapter.setSystem(loadedSystem);
            adapter.notifyDataSetChanged();
        }


    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_new_station) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.new_station_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.price_edittext);
            TextView new_station_popup_title_textview = (TextView) promptsView.findViewById(R.id.new_station_popup_title_textview);
            TextView new_station_title_textview = (TextView) promptsView.findViewById(R.id.new_station_title_textview);
            final Button station_button = (Button) promptsView.findViewById(R.id.station_button);
            final Button outpost_button = (Button) promptsView.findViewById(R.id.outpost_button);
            final CheckBox black_market_checkbox = (CheckBox) promptsView.findViewById(R.id.black_market_checkbox);

            station_button.setPressed(true);

            new_station_popup_title_textview.setText("NEW STATION IN " + systemName);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/eurostile.TTF");
            userInput.setTypeface(font);
            new_station_popup_title_textview.setTypeface(font);
            outpost_button.setTypeface(font);
            station_button.setTypeface(font);
            new_station_title_textview.setTypeface(font);

            station_button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        station_button.setPressed(true);
                        outpost_button.setPressed(false);
                    }


                    return true;
                }
            });

            outpost_button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        station_button.setPressed(false);
                        outpost_button.setPressed(true);
                    }

                    return true;
                }
            });

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .setNegativeButton("CANCEL", null);

            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button pos = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    pos.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String stationName = userInput.getText().toString();
                            boolean isValid = Utils.validateStationName(stationName, systemName);

                            if (isValid) {
                                String station_type_string = station_button.isPressed() ? "station" : "outpost";
                                boolean hasBlackMarket = black_market_checkbox.isChecked();

                                loadedSystem = Storage.createAndSaveNewStationForSystem(systemName, station_type_string, hasBlackMarket, stationName);

                                adapter.setSystem(loadedSystem);
                                adapter.notifyDataSetChanged();

                                alertDialog.dismiss();
                            }
                            else {
                                alertDialog.findViewById(R.id.station_name_popup_error_message).setVisibility(View.VISIBLE);
                            }


                        }
                    });

                    // Cencel button
                    Button neg = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    neg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });


            // show it
            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(font);
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(font);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("systemName", systemName);


    }

}
