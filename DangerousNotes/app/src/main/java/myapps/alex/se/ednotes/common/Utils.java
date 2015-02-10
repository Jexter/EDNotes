package myapps.alex.se.ednotes.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import myapps.alex.se.ednotes.R;
import myapps.alex.se.ednotes.adapters.StationListAdapter;
import myapps.alex.se.ednotes.adapters.SystemListAdapter;
import myapps.alex.se.ednotes.model.*;
import myapps.alex.se.ednotes.model.System;
import myapps.alex.se.ednotes.persistence.Storage;

/**
 * Created by atkinson on 19/12/14.
 */
public class Utils {

    public static String getDateAsTimePassed(Date dateVisited) {
        if(dateVisited == null) {
            return "Never visited";
        }


        Date dateNow = new Date();
        Long nowDateMillis = dateNow.getTime();
        Long visitedDateMillis = dateVisited.getTime();
        Long differenceMillis = nowDateMillis - visitedDateMillis;

        int minutes = (int) (differenceMillis / (1000 * 60));

        if(minutes > 60) {
            int hours = minutes / 60;
            int minutesLeftOver = minutes % (hours * 60);
            String text = hours + "h " + minutesLeftOver + "m";

            return text;
        }

        return minutes + "m";
    }

    public static Station findStation(Station[] stationArray, String stationName) {
        for(Station s : stationArray) {
            if(s.getName().equals(stationName)) {
                return s;
            }
        }

        return null;
    }

    public static int getTotalRowCountForCommoditiesListInStation(Station station) {
        int rowCount = 0;

        ArrayList<CommodityCategory> categories = station.getCategories();

        if(categories == null || categories.size() == 0) {
            return 0;
        }

        for(CommodityCategory cat : categories) {
            rowCount++;

            for(Commodity com : cat.getCommodities()) {
                rowCount++;
            }
        }

        return rowCount;
    }

    public static int[] getPositionsForHeaders(Station station) {
        ArrayList<CommodityCategory> categories = station.getCategories();

        if(categories == null || categories.size() == 0) {
            return null;
        }

        int numberOfHeaders = categories.size();
        int[] rowsForHeaders = new int[numberOfHeaders];
        int rowsSoFar = 0;

        for(int i = 0; i < numberOfHeaders; i++) {
            rowsForHeaders[i] = rowsSoFar;
            rowsSoFar += categories.get(i).getCommodities().size() + 1;
        }

        return rowsForHeaders;
    }

    public static int calculateHighestHeaderIndexForThisPosition(int[] positionsForHeaders, int position) {
        int count = -1;

        for(int i : positionsForHeaders) {
            if(position > i) {
                count++;
            }
        }

        return count;
    }

    public static int calculateWhichHeaderThisIs(int[] positionsForHeaders, int position) {
        int headerIndex = 0;

        for(int i : positionsForHeaders) {
            if(i == position) {
                return headerIndex;
            }

            headerIndex++;
        }

       return -1;
    }

    public static boolean validateSystemNameold(String systemName, boolean editing) {
        ArrayList<MiniSystem> miniSystems = Storage.loadMiniSystems();

        if(miniSystems != null && (editing == false)) {
            for (MiniSystem miniSystem : miniSystems) {
                if (miniSystem.getName().equals(systemName)) {
                    return false;
                }
            }
        }

        return systemName.matches(AppConstants.SYSTEM_NAME_REGEX);
    }

    public static String validateSystemName(String systemName, boolean editing) {
        String errorMessage = "OK";

        ArrayList<MiniSystem> miniSystems = Storage.loadMiniSystems();

        if(miniSystems != null && (editing == false)) {
            for (MiniSystem miniSystem : miniSystems) {
                if (miniSystem.getName().equals(systemName)) {
                    return "name already in use";
                }
            }
        }

        boolean regExpOK = systemName.matches(AppConstants.SYSTEM_NAME_REGEX);

        if(!regExpOK) {
            errorMessage = "Please use only real system names";
        }

        return errorMessage;
    }


    public static SpannableString getTitleWithFont(Activity context, CharSequence titleText) {
        SpannableString s = new SpannableString(titleText);
        TypefaceSpan ts = new TypefaceSpan(context, "eurostile.TTF");
        s.setSpan(ts, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }


    public static String validateStationName(String stationName, String systemName, boolean editing) {
        String errorMessage = "OK";
        ArrayList<Station> stationsAlreadyInSystem = Storage.loadStationsForSystem(systemName);

        if(stationsAlreadyInSystem != null && (editing == false)) {
            for (Station station : stationsAlreadyInSystem) {
                if (stationName.equals(station.getName())) {
                    return "name already in use";
                }
            }
        }

        boolean regExpOK = stationName.matches(AppConstants.STATION_NAME_REGEX);

        if(!regExpOK) {
            errorMessage = "funny characters detected";
        }

        return errorMessage;
    }


    public static boolean validateCommodityPrice(String price) {

        boolean regexMatches = price.matches(AppConstants.COMMODITY_PRICE_REGEX);

        if(!regexMatches) {
            return false;
        }
        else {
            Integer priceInt;

            try {
                priceInt = Integer.valueOf(price);
            }
            catch(Exception e) {
                return false;
            }

            return priceInt < AppConstants.MAX_COMMODITY_PRICE ? true : false;
        }
    }

    private static ArrayList<Commodity> getFlatCommodityList(Station station) {
        ArrayList<Commodity> commodities = new ArrayList<Commodity>();

        for(CommodityCategory cat : station.getCategories()) {
            for(Commodity com : cat.getCommodities()) {
                commodities.add(com);
            }
        }

        return commodities;
    }


    public static ArrayList<CommodityTradeRoute> getTradesForTwoStations(Station fromStation, Station toStation, int minimumProfit) {
        ArrayList<CommodityTradeRoute> commodityTradeRoutes = new ArrayList<CommodityTradeRoute>();

        HashMap<Integer, Commodity> fromSupply = new HashMap<Integer, Commodity>();
        HashMap<Integer, Commodity> fromDemand = new HashMap<Integer, Commodity>();

        for(CommodityCategory cat : fromStation.getCategories()) {
            for(Commodity com : cat.getCommodities()) {
                if(com.getAvailability() != null) {
                    if (com.getAvailability() == Availability.DEMAND) {
                        fromDemand.put(com.getId(), com);
                    }
                    if (com.getAvailability() == Availability.SUPPLY) {
                        fromSupply.put(com.getId(), com);
                    }
                }
            }
        }

        for(CommodityCategory cat : toStation.getCategories()) {
            for(Commodity com : cat.getCommodities()) {
                if(com.getAvailability() != null) {
                    Commodity fromCom;

                    if(com.getAvailability() == Availability.DEMAND) {
                        fromCom = fromDemand.get(com.getId());

                        if(fromCom != null) {
                            if(com.getPrice() - fromCom.getPrice() >= minimumProfit) {
                                CommodityTradeRoute trade = new CommodityTradeRoute();
                                // Continue here
                            }
                        }
                    }

                    if(com.getAvailability() == Availability.SUPPLY) {
                        fromCom = fromDemand.get(com.getId());

                        if(fromCom != null) {
                            if(fromCom.getPrice() - com.getPrice() >= minimumProfit) {
                                // Add to results
                            }
                        }
                    }

                }
            }
        }



        return commodityTradeRoutes;
    }


    public static ArrayList<CommodityTradeRoute> getStationToGalaxyTrades(Station fromStation, System fromSystem, ArrayList<System> systemsToLookIn) {
        ArrayList<CommodityTradeRoute> trades = new ArrayList<CommodityTradeRoute>();

        HashMap<Integer, Commodity> fromSupply = new HashMap<Integer, Commodity>();
        HashMap<Integer, Commodity> fromDemand = new HashMap<Integer, Commodity>();

        for(CommodityCategory cat : fromStation.getCategories()) {
            for(Commodity com : cat.getCommodities()) {
                if(com.getAvailability() != null) {
                    if (com.getAvailability() == Availability.DEMAND) {
                        fromDemand.put(com.getId(), com);
                    }
                    if (com.getAvailability() == Availability.SUPPLY) {
                        fromSupply.put(com.getId(), com);
                    }
                }
            }
        }

        for(System toSystem : systemsToLookIn) {
            for(Station toStation : toSystem.getStations()) {
                for (CommodityCategory cat : toStation.getCategories()) {
                    for (Commodity toCom : cat.getCommodities()) {
                        if (toCom.getAvailability() != null) {
                            Commodity fromCom;

                            if (toCom.getAvailability() == Availability.DEMAND) {
                                fromCom = fromSupply.get(toCom.getId());

                                if (fromCom != null) {
                                    int profit = toCom.getPrice() - fromCom.getPrice();
                                    if (profit > 0) {
                                        CommodityTradeRoute trade = new CommodityTradeRoute();
                                        trade.setFromStation(fromStation);
                                        trade.setFromSystem(fromSystem);
                                        trade.setFromStationCommodity(fromCom);
                                        trade.setToStation(toStation);
                                        trade.setToSystem(toSystem);
                                        trade.setToStationCommodity(toCom);
                                        trade.setProfit(profit);
                                        trades.add(trade);
                                    }
                                }
                            }

                            if (toCom.getAvailability() == Availability.SUPPLY) {
                                fromCom = fromDemand.get(toCom.getId());

                                if (fromCom != null) {
                                    int profit = fromCom.getPrice() - toCom.getPrice();
                                    if (profit > 0) {
                                        CommodityTradeRoute trade = new CommodityTradeRoute();
                                        trade.setFromStation(toStation);
                                        trade.setFromSystem(toSystem);
                                        trade.setFromStationCommodity(toCom);
                                        trade.setToStation(fromStation);
                                        trade.setToSystem(fromSystem);
                                        trade.setToStationCommodity(fromCom);
                                        trade.setProfit(profit);
                                        trades.add(trade);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Log.d("TradeHelper found:", trades.size()==0?"nothing":trades.size()+" possible trades (omg!)");

        return trades;
    }




    public static void showSystemDialog(Activity activity, final SystemListAdapter adapter, final MiniSystem miniSystem) {

        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.new_system_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.price_edittext);
        TextView new_system_popup_title_textview = (TextView) promptsView.findViewById(R.id.new_system_popup_title_textview);
        TextView new_system_title_textview = (TextView) promptsView.findViewById(R.id.new_system_title_textview);
        TextView allegiance_title_textview = (TextView) promptsView.findViewById(R.id.allegiance_title_textview);
        final Spinner allegiance_spinner = (Spinner) promptsView.findViewById(R.id.allegiance_spinner);

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
        userInput.setTypeface(font);
        new_system_popup_title_textview.setTypeface(font);
        new_system_title_textview.setTypeface(font);
        allegiance_title_textview.setTypeface(font);



        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setNegativeButton("CANCEL", null);

        final boolean editing = miniSystem != null;
        new_system_popup_title_textview.setText("NEW SYSTEM");

        if(editing) {
            new_system_popup_title_textview.setText("EDIT SYSTEM");

            alertDialogBuilder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ArrayList<MiniSystem> minis = Storage.deleteSystem(miniSystem);
                    MiniSystem[] miniSystems = new MiniSystem[minis.size()];
                    miniSystems = minis.toArray(miniSystems);
                    adapter.setSystems(miniSystems);
                    adapter.notifyDataSetChanged();
                }
            });

            userInput.setText(miniSystem.getName());
            Object currentAllegianceObject = miniSystem.getMisc().get(AppConstants.ALLEGIANCE_MISC_KEY);
            int foundIndex = -1;
            if(currentAllegianceObject != null) {
                String currentAllegianceText = (String) currentAllegianceObject;

                int allegienceSpinnerCount = allegiance_spinner.getCount();
                for(int i=0; i<allegienceSpinnerCount; i++) {
                    if(currentAllegianceText.equals(allegiance_spinner.getItemAtPosition(i))) {
                        foundIndex = i;
                    }
                }

                if(foundIndex != -1) {
                    allegiance_spinner.setSelection(foundIndex);
                }
            }
        }




        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button pos = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                pos.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String systemName = userInput.getText().toString();
                        String errorMessage = Utils.validateSystemName(systemName, editing);
                        String allegianceString = (String) allegiance_spinner.getSelectedItem();

                        if (errorMessage.equals("OK")) {
                            ArrayList<MiniSystem> minis;
                            MiniSystem[] miniSystems;

                            if(editing) {
                                minis = Storage.updateSystem(miniSystem.getName(), systemName, allegianceString);
                                miniSystems = new MiniSystem[minis.size()];
                                miniSystems = minis.toArray(miniSystems);
                            }
                            else {
                                minis = Storage.createAndSaveNewSystem(systemName, allegianceString);
                                miniSystems = new MiniSystem[minis.size()];
                                miniSystems = minis.toArray(miniSystems);
                            }

                            adapter.setSystems(miniSystems);
                            adapter.notifyDataSetChanged();

                            alertDialog.dismiss();
                        }
                        else {
                            alertDialog.findViewById(R.id.system_name_popup_error_message).setVisibility(View.VISIBLE);
                            ((TextView) (alertDialog.findViewById(R.id.system_name_popup_error_message))).setText(errorMessage);
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



    }



    // Massive function for the popup for creating or editing a station
    public static void showStationDialog(Activity activity, final myapps.alex.se.ednotes.model.System system, final Station station, final StationListAdapter adapter) {
        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.new_station_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.price_edittext);
        TextView new_station_popup_title_textview = (TextView) promptsView.findViewById(R.id.new_station_popup_title_textview);
        TextView new_station_title_textview = (TextView) promptsView.findViewById(R.id.new_station_title_textview);
        final Button station_button = (Button) promptsView.findViewById(R.id.station_button);
        final Button outpost_button = (Button) promptsView.findViewById(R.id.outpost_button);
        final CheckBox black_market_checkbox = (CheckBox) promptsView.findViewById(R.id.black_market_checkbox);

        station_button.setPressed(true);

        new_station_popup_title_textview.setText("NEW STATION IN " + system.getName());

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/eurostile.TTF");
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



        if(station != null) {
            new_station_popup_title_textview.setText("EDIT STATION IN " + system.getName());

            alertDialogBuilder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    adapter.setSystem(Storage.deleteStationInSystem(system, station));
                    adapter.notifyDataSetChanged();
                }
            });

            userInput.setText(station.getName());


            Object hasBlackMarketObject = station.getMisc().get(AppConstants.BLACK_MARKET);
            if (hasBlackMarketObject != null) {
                boolean hasBlackMarket = (boolean) hasBlackMarketObject;
                black_market_checkbox.setChecked(hasBlackMarket);
            }


            Object stationTypeObject = station.getMisc().get(AppConstants.STATION_TYPE);

            if (stationTypeObject != null) {
                String stationType = (String) stationTypeObject;
                station_button.setPressed("station".equals(stationType));
                outpost_button.setPressed("outpost".equals(stationType));
            }


        }




        final String oldStationName = userInput.getText().toString();

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
                        String errorMessage = Utils.validateStationName(stationName, system.getName(), station != null);



                        if ("OK".equals(errorMessage)) {
                            boolean isStation = station_button.isPressed();
                            boolean isOutpost = outpost_button.isPressed();
                            if(station == null) {

                                adapter.setSystem(Storage.createAndSaveNewStationForSystem(system.getName(), isStation ? "station" : (isOutpost?"outpost":null), black_market_checkbox.isChecked(), stationName));
                            }
                            else {
                                adapter.setSystem(Storage.updateStationForSystem(system.getName(), isStation ? "station" : (isOutpost?"outpost":null), black_market_checkbox.isChecked(), oldStationName, stationName));
                            }


                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                        else {
                            alertDialog.findViewById(R.id.station_name_popup_error_message).setVisibility(View.VISIBLE);
                            ((TextView)(alertDialog.findViewById(R.id.station_name_popup_error_message))).setText(errorMessage);
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

    }


}







