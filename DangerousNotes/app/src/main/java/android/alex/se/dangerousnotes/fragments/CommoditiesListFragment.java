package android.alex.se.dangerousnotes.fragments;

import android.alex.se.dangerousnotes.R;
import android.alex.se.dangerousnotes.adapters.CommoditiesListAdapter;
import android.alex.se.dangerousnotes.common.AppConstants;
import android.alex.se.dangerousnotes.common.Utils;
import android.alex.se.dangerousnotes.model.*;
import android.alex.se.dangerousnotes.model.System;
import android.alex.se.dangerousnotes.persistence.Storage;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class CommoditiesListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private CommoditiesListAdapter adapter;
    private View inflatedView;


    public CommoditiesListFragment() {
        Log.d("commoditieslistfragment says", "hi");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CommoditiesListAdapter(getActivity());

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

        System currentSystem = Storage.loadSystem(systemName);
        ArrayList<Station> loadedStations = currentSystem.getStations();

        if(loadedStations != null && loadedStations.size() > 0) {
            Station[] stationArray = new Station[loadedStations.size()];
            stationArray = loadedStations.toArray(stationArray);

            Station stationWeWantToLookAt = Utils.findStation(stationArray, stationName);

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
