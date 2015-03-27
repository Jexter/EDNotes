package myapps.alex.se.ednotes.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
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
import myapps.alex.se.ednotes.adapters.SystemListAdapter;
import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.model.MiniSystem;
import myapps.alex.se.ednotes.persistence.Storage;

public class SystemListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private SystemListAdapter adapter;
    private View inflatedView;
    ArrayList<MiniSystem> loadedMiniSystems;

    // TODO: Rename and change types and number of parameters
    public static SystemListFragment newInstance() {
        SystemListFragment fragment = new SystemListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);

        return fragment;
    }

    public SystemListFragment() {
        Log.d("fragment says", "hi");
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        adapter = new SystemListAdapter(getActivity());

        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.system_list_fragment, container, false);
        return inflatedView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ((ListView)view.findViewById(R.id.systems_listview)).setAdapter(adapter);


    }

    @Override
    public void onResume() {
        super.onResume();


        loadedMiniSystems = Storage.loadMiniSystems();
        Utils.sortSystems(loadedMiniSystems);


        if(loadedMiniSystems != null && loadedMiniSystems.size() > 0) {
            MiniSystem[] miniSystems = new MiniSystem[loadedMiniSystems.size()];
            miniSystems = loadedMiniSystems.toArray(miniSystems);
            adapter.setSystems(miniSystems);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu != null) {
            menu.clear();
        }

        inflater.inflate(R.menu.main, menu);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
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
        public void onFragmentInteraction(Uri uri);
    }

    private void loadAndSortMiniSystems() {
        loadedMiniSystems = Storage.loadMiniSystems();

        if(loadedMiniSystems != null) {
            Utils.sortSystems(loadedMiniSystems);
            MiniSystem[] miniSystems = new MiniSystem[loadedMiniSystems.size()];
            miniSystems = loadedMiniSystems.toArray(miniSystems);
            adapter.setSystems(miniSystems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            View sortButton = getActivity().findViewById(R.id.sort);

            PopupMenu popupMenu = new PopupMenu(getActivity(), sortButton);
            popupMenu.inflate(R.menu.list_sort);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId() == R.id.alpha) {
                        Storage.setSortType(AppConstants.SORT_ALPHA);
                        loadAndSortMiniSystems();

                        return true;
                    }

                    if(menuItem.getItemId() == R.id.last_edited) {
                        Storage.setSortType(AppConstants.SORT_LAST_EDITED);
                        loadAndSortMiniSystems();

                        return true;
                    }

                    return false;
                }
            });


            popupMenu.show();

            return true;
        }

        if (item.getItemId() == R.id.add_new_system) {
            Utils.showSystemDialog(getActivity(), adapter, null);
            return true;
        }




        return super.onOptionsItemSelected(item);
    }

}
