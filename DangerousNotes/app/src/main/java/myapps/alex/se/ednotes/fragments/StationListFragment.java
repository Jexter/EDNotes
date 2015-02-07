package myapps.alex.se.ednotes.fragments;

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
import android.widget.ListView;

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
            Utils.showStationDialog(getActivity(), loadedSystem, null, adapter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("systemName", systemName);


    }




}
