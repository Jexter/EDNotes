package android.alex.se.dangerousnotes.common;

import android.alex.se.dangerousnotes.model.Commodity;
import android.alex.se.dangerousnotes.model.CommodityCategory;
import android.alex.se.dangerousnotes.model.MiniSystem;
import android.alex.se.dangerousnotes.model.Station;
import android.alex.se.dangerousnotes.persistence.Storage;
import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.Date;

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

    public static boolean validateSystemName(String systemName) {
        ArrayList<MiniSystem> miniSystems = Storage.loadMiniSystems();

        if(miniSystems != null) {
            for (MiniSystem miniSystem : miniSystems) {
                if (miniSystem.getName().equals(systemName)) {
                    return false;
                }
            }
        }

        return systemName.matches(AppConstants.SYSTEM_NAME_REGEX);
    }

    public static SpannableString getTitleWithFont(Activity context, CharSequence titleText) {
        SpannableString s = new SpannableString(titleText);
        TypefaceSpan ts = new TypefaceSpan(context, "eurostile.TTF");
        s.setSpan(ts, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }


    public static boolean validateStationName(String stationName, String systemName) {

        ArrayList<Station> stationsAlreadyInSystem = Storage.loadStationsForSystem(systemName);

        if(stationsAlreadyInSystem != null) {
            for (Station station : stationsAlreadyInSystem) {
                if (stationName.equals(station.getName())) {
                    return false;
                }
            }
        }

        return stationName.matches(AppConstants.STATION_NAME_REGEX);
    }


    public static boolean validateCommodityPrice(String price) {

        boolean regexMatches = price.matches(AppConstants.COMMODITY_PRICE_REGEX);

        if(!regexMatches) {
            return false;
        }
        else {
            return Integer.valueOf(price) < AppConstants.MAX_COMMODITY_PRICE ? true : false;
        }

    }



}







