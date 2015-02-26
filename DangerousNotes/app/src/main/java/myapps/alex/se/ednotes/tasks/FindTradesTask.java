package myapps.alex.se.ednotes.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import myapps.alex.se.ednotes.common.Utils;
import myapps.alex.se.ednotes.model.CommodityTradeRoute;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.model.System;
import myapps.alex.se.ednotes.persistence.Storage;

public class FindTradesTask extends AsyncTask<Object, Integer, ArrayList<CommodityTradeRoute>> {
    long millis;
    ProgressDialog pd;

    protected void onPreExecute (){
        Log.d("FindTradesTask", "Looking for trades...");

        millis = new Date().getTime();
    }

    protected ArrayList<CommodityTradeRoute> doInBackground(Object...params) {
        ArrayList<CommodityTradeRoute> trades;
        Station stationWeWantToLookAt = (Station) params[0];
        System currentSystem = (System) params[1];
        ArrayList<System> systemsToLookIn = (ArrayList<System>) params[2];
        final Activity activity = (Activity) params[3];
        final FindTradesTask task = this;

        activity.runOnUiThread(new Runnable() {
            public void run() {
                pd = new ProgressDialog(activity);
                pd.setMessage("Finding trades...");
                pd.setCancelable(true);
                pd.setCanceledOnTouchOutside(true);
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Log.d("TradeFinderTask", "Cancelled!!!");
                        task.cancel(true);
                    }
                });
                pd.show();
            }
        });




        // If we got an empty galaxy we need to load it all
        //if(systemsToLookIn == null) {
            systemsToLookIn = Storage.loadAllSystemsForTrade();
        //}

        if(isCancelled()) {
            return null;
        }

        if(pd != null && pd.isShowing()) {
            trades = Utils.getStationToGalaxyTrades(stationWeWantToLookAt, currentSystem, systemsToLookIn);
        }
        else {
            return null;
        }

        millis = new Date().getTime() - millis;

        Log.d("FindTradesTask", "... found " + trades.size() + " trades after " + millis + "ms");

        if(pd != null && pd.isShowing()){
            pd.dismiss();
        }

        if(isCancelled()) {
            return null;
        }

        return trades;
    }

    protected void onProgressUpdate(Integer...a){
        Log.d("FindTradesTask says", "You are in progress update ... " + a[0]);
    }


}