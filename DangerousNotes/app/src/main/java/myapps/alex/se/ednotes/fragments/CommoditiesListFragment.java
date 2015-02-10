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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.adapters.CommoditiesListAdapter;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.model.CommodityTradeRoute;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.model.System;
import myapps.alex.se.ednotes.persistence.Storage;

public class CommoditiesListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private CommoditiesListAdapter adapter;
    private Station stationWeWantToLookAt;
    private System currentSystem;
    private ArrayList<System> systemsToLookIn;

    public CommoditiesListFragment() {
        Log.d("commoditieslistfragment says", "hi");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        adapter = new CommoditiesListAdapter(getActivity());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null) {
            menu.clear();
        }

        inflater.inflate(R.menu.commodities_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.find_matches) {

            long timeStamp = new Date().getTime();
            if(systemsToLookIn == null) {
                systemsToLookIn = Storage.loadAllSystemsForTrade();
            }

//            Log.d("LOADING ALL SYSTEMS:", "Took " + timeStamp + "ms");

            ArrayList<CommodityTradeRoute> trades = Utils.getStationToGalaxyTrades(stationWeWantToLookAt, currentSystem, systemsToLookIn);
            timeStamp = (new Date().getTime() - timeStamp);

            Log.d("TRADES", "Took " + timeStamp + "ms");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.commodities_list_fragment, container, false);

        return inflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ((ListView)view.findViewById(R.id.commodities_listview)).setAdapter(adapter);

        String systemName = getActivity().getIntent().getStringExtra(AppConstants.SYSTEM_NAME);
        String stationName = getActivity().getIntent().getStringExtra(AppConstants.STATION_NAME);

        currentSystem = Storage.loadSystem(systemName);
        ArrayList<Station> loadedStations = currentSystem.getStations();

        if(loadedStations != null && loadedStations.size() > 0) {
            Station[] stationArray = new Station[loadedStations.size()];
            stationArray = loadedStations.toArray(stationArray);

            stationWeWantToLookAt = Utils.findStation(stationArray, stationName);

            adapter.setStationAndSystem(stationWeWantToLookAt, currentSystem);
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

}
