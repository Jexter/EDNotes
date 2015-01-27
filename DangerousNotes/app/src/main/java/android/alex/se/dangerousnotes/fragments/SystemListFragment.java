package android.alex.se.dangerousnotes.fragments;

import android.alex.se.dangerousnotes.R;
import android.alex.se.dangerousnotes.adapters.SystemListAdapter;
import android.alex.se.dangerousnotes.common.Utils;
import android.alex.se.dangerousnotes.model.MiniSystem;
import android.alex.se.dangerousnotes.persistence.Storage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.ArrayList;

public class SystemListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private SystemListAdapter adapter;
    private View inflatedView;

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

        ArrayList<MiniSystem> loadedMiniSystems = Storage.loadMiniSystems();

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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_new_system) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.commodity_info_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.price_edittext);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String systemName = userInput.getText().toString();
                                    boolean isValid = Utils.validateSystemName(systemName);

                                    if (isValid) {
                                        Storage.createAndSaveNewSystem(systemName);
                                        ArrayList<MiniSystem> minis = Storage.loadMiniSystems();
                                        MiniSystem[] miniSystems = new MiniSystem[minis.size()];
                                        miniSystems = minis.toArray(miniSystems);

                                        adapter.setSystems(miniSystems);
                                        adapter.notifyDataSetChanged();
                                        //dialog.cancel();
                                    }
                                    else {
                                        Log.d("Dialog for new systems says:", "input name invalid");
                                    }
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
