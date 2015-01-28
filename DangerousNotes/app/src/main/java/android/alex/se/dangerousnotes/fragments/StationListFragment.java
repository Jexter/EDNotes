package android.alex.se.dangerousnotes.fragments;

import android.alex.se.dangerousnotes.R;
import android.alex.se.dangerousnotes.adapters.StationListAdapter;
import android.alex.se.dangerousnotes.common.AppConstants;
import android.alex.se.dangerousnotes.common.Utils;
import android.alex.se.dangerousnotes.model.Station;
import android.alex.se.dangerousnotes.persistence.Storage;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class StationListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private StationListAdapter adapter;
    private View inflatedView;
    private String systemName;
    //private ArrayList<Station> loadedStations;
    private android.alex.se.dangerousnotes.model.System loadedSystem;

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
            new_station_popup_title_textview.setText("NEW STATION IN " + systemName);

            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/eurostile.TTF");
            userInput.setTypeface(font);
            new_station_popup_title_textview.setTypeface(font);
            new_station_title_textview.setTypeface(font);


            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String stationName = userInput.getText().toString();
                                    boolean isValid = Utils.validateStationName(stationName);

                                    if (isValid) {
                                        Storage.createAndSaveNewStationForSystem(systemName, stationName);

                                        loadedSystem = Storage.loadSystem(systemName);
                                        adapter.setSystem(loadedSystem);
                                        adapter.notifyDataSetChanged();

                                    } else {
                                        Log.d("Dialog for new systems says:", "input name invalid");
                                    }
                                }
                            })
                    .setNegativeButton("CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

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
