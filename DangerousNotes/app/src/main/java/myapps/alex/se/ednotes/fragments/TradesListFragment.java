package myapps.alex.se.ednotes.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.adapters.TradesListAdapter;
import myapps.alex.se.ednotes.model.CommodityTradeRoute;

public class TradesListFragment extends Fragment {

    private TradesListAdapter adapter;

    public TradesListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        adapter = new TradesListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.trade_list_fragment, container, false);

        return inflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((ListView)view.findViewById(R.id.station_listview)).setAdapter(adapter);
    }

    public void setTrades(ArrayList<CommodityTradeRoute> trades) {
        adapter.setTrades(trades);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
