package myapps.alex.se.ednotes.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.DNApplication;
import myapps.alex.se.ednotes.common.StationFactory;
import myapps.alex.se.ednotes.model.CommodityTradeRoute;
import myapps.alex.se.ednotes.model.MiniSystem;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.model.System;

/**
 * Created by atkinson on 19/12/14.
 */
public class Storage {

    private static ArrayList<CommodityTradeRoute> trades;

    public static ArrayList<CommodityTradeRoute> getTrades() {
        return trades;
    }

    public static void setTrades(ArrayList<CommodityTradeRoute> trades) {
        Storage.trades = trades;
    }

    private static File mBaseDirectory;

    public static System loadSystem(String systemName) {
        return loadSystem(getBaseDirectory(), systemName);
    }

    public static System loadSystem(File directory, String systemName) {
        System system = (System) deserializeObject(directory, AppConstants.SYSTEM_BASE_FILENAME + systemName);

        return system;
    }

    public static void saveSystem(System system) {
        String fileName = AppConstants.SYSTEM_BASE_FILENAME + system.getName();
        serializeObject(system, fileName);
    }

    public static void saveSystem(File directory, System system) {
        String fileName = AppConstants.SYSTEM_BASE_FILENAME + system.getName();
        serializeObject(directory, system, fileName);
    }

    public static void saveAllSystems(ArrayList<System> systems) {
        saveAllSystems(getBaseDirectory(), systems);
    }

    public static void saveAllSystems(File directory, ArrayList<System> systems) {
        if (systems == null || directory == null) {
            return;
        }

        for (System system : systems) {
            saveSystem(directory, system);
        }

        Log.d("Saved systems:", ""+systems.size());
    }

    public static void saveTrades(ArrayList<CommodityTradeRoute> trades) {
        String fileName = AppConstants.TRADE_ROUTES_FILENAME;
        serializeObject(trades, fileName);
    }

    public static ArrayList<Station> loadStationsForSystem(String systemName) {
        System sys = loadSystem(systemName);
        ArrayList<Station> stationList = sys.getStations();

        return stationList;
    }

    public static ArrayList<CommodityTradeRoute> loadTradeRoutes() {
        String fileName = AppConstants.TRADE_ROUTES_FILENAME;
        ArrayList<CommodityTradeRoute> trades = (ArrayList<CommodityTradeRoute>) deserializeObject(getBaseDirectory(), AppConstants.TRADE_ROUTES_FILENAME);

        return trades;
    }

    public static ArrayList<MiniSystem> loadMiniSystems() {
        return loadMiniSystems(getBaseDirectory());
    }

    public static ArrayList<MiniSystem> loadMiniSystems(File directory) {
        ArrayList<MiniSystem> miniSystems = (ArrayList<MiniSystem>) deserializeObject(directory, AppConstants.MINI_SYSTEMS_FILENAME);

        return miniSystems;
    }

    private static void deleteMiniSystems() {
        deleteMiniSystems(getBaseDirectory());
    }

    private static void deleteMiniSystems(File directory) {
        deleteFile(directory, AppConstants.MINI_SYSTEMS_FILENAME);
    }

    private static void deleteAllSystems(ArrayList<System> systems) {
        deleteAllSystems(getBaseDirectory(), systems);
    }

    private static void deleteAllSystems(File directory, ArrayList<System> systems) {
        if (systems == null || directory == null) {
            return;
        }

        for (System system : systems) {
            deleteFile(directory, AppConstants.SYSTEM_BASE_FILENAME + system.getName());
        }
    }

    public static boolean dataExistsInOldDirectory() {
        File file = new File(DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE), AppConstants.MINI_SYSTEMS_FILENAME);

        try {
            if (file.exists()) {
                return true;
            } else {
                return false;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private static void saveMiniSystems(ArrayList<MiniSystem> miniSystems) {
        saveMiniSystems(getBaseDirectory(), miniSystems);
    }

    private static void saveMiniSystems(File directory, ArrayList<MiniSystem> miniSystems) {
        serializeObject(directory, miniSystems, AppConstants.MINI_SYSTEMS_FILENAME);
    }


    public static ArrayList<System> loadAllSystems() {
        return loadAllSystems(getBaseDirectory());
    }


    public static ArrayList<System> loadAllSystems(File directory) {
        ArrayList<System> allSystems = new ArrayList<>();
        ArrayList<MiniSystem> miniSystems = loadMiniSystems(directory);

        for(MiniSystem miniSystem : miniSystems) {
            System system = loadSystem(directory, miniSystem.getName());

            if(system.getStations() != null && system.getStations().size() > 0) {
                allSystems.add(system);
            }
        }

        return allSystems;
    }

    private static File getBaseDirectory() {
        if (mBaseDirectory != null) {
            return mBaseDirectory;
        }

//        File oldDirectory = DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE);
//        File newDirectory = DNApplication.getContext().getExternalFilesDir(null);

        mBaseDirectory = DNApplication.getContext().getExternalFilesDir(null);

        return mBaseDirectory;
    }

    public static void exposeData() {

        boolean weHaveDataInOldDirectory = dataExistsInOldDirectory();

        if (!weHaveDataInOldDirectory) {
            return;
        }

        File fromDirectory = DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE);
        File toDirectory = DNApplication.getContext().getExternalFilesDir(null);

        moveFiles(fromDirectory, toDirectory);
    }

    private static void moveFiles(File fromDirectory, File toDirectory) {

        // Load everything
        ArrayList<MiniSystem> miniSystems = loadMiniSystems(fromDirectory);
        ArrayList<System> systems = loadAllSystems(fromDirectory);

        // Save everything again in a new place
        saveAllSystems(toDirectory, systems);
        saveMiniSystems(toDirectory, miniSystems);

        // Delete everything in the old directory
        deleteMiniSystems(fromDirectory);
        deleteAllSystems(fromDirectory, systems);

    }

    public static void serializeObject(Object object, String fileName) {
        serializeObject(getBaseDirectory(), object, fileName);
    }

    public static void serializeObject(File directory, Object object, String fileName) {
        File file = new File(directory, fileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutput out = new ObjectOutputStream(fout);
            out.writeObject(object);
            out.close();

            Log.d("Storage says", "just saved file [" + fileName + "]");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Object deserializeObject(String fileName) {
        return deserializeObject(getBaseDirectory(), fileName);
    }

    public static Object deserializeObject(File directory, String fileName) {
        File file = new File(directory, fileName);

        Log.d("Storage says", "going to load file [" + fileName + "]");

        try {
            if (file.exists()) {
                FileInputStream fin = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fin);
                Object object = ois.readObject();
                ois.close();

                Log.d("Storage says", "just loaded file [" + fileName + "]");

                return (object == null) ? null : object;
            }
            else {
                Log.d("Storage says", "could NOT load file [" + fileName + "]");
                return null;
            }
        }
        catch (Exception e) {
            Log.d("Storage says", "BANG while doing load on [" + fileName + "]");
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(String fileName) {
        deleteFile(getBaseDirectory(), fileName);
    }

    public static void deleteFile(File directory, String fileName) {
        File file = new File(directory, fileName);

        try {
            if (file.exists()) {
                file.delete();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renameFile(String oldFileName, String newFileName) {
        File oldFile = new File(DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE), oldFileName);
        File newFile = new File(DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE), newFileName);

        try {
            if (oldFile.exists()) {
                oldFile.renameTo(newFile);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<MiniSystem> updateSystem(String oldSystemName, String newSystemName, String allegianceString) {
        ArrayList<MiniSystem> miniSystems = loadMiniSystems();

        for(MiniSystem miniSystem : miniSystems) {
            if(oldSystemName.equals(miniSystem.getName())) {
                miniSystem.setName(newSystemName);
                miniSystem.getMisc().put(AppConstants.ALLEGIANCE_MISC_KEY, allegianceString);

                System system = loadSystem(oldSystemName);
                system.setName(newSystemName);
                deleteFile(AppConstants.SYSTEM_BASE_FILENAME + oldSystemName);
                saveSystem(system);

                break;
            }
        }

        saveMiniSystems(miniSystems);

        return miniSystems;
    }


    public static ArrayList<MiniSystem> createAndSaveNewSystem(String systemName, String allegianceString) {
        ArrayList<MiniSystem> miniSystems = loadMiniSystems();

        if(miniSystems == null) {
            miniSystems = new ArrayList<MiniSystem>();
        }

        MiniSystem newMiniSystem = new MiniSystem(systemName);
        newMiniSystem.getMisc().put(AppConstants.ALLEGIANCE_MISC_KEY, allegianceString);
        miniSystems.add(0, newMiniSystem);

        System newSystem = new System(null, systemName);
        newSystem.getMisc().put(AppConstants.ALLEGIANCE_MISC_KEY, allegianceString);

        saveMiniSystems(miniSystems);
        saveSystem(newSystem);

        return miniSystems;
    }

    public static System createAndSaveNewStationForSystem(String systemName, String stationType, boolean hasBlackMarket, String stationName) {
        System system = loadSystem(systemName);


        ArrayList<Station> stationsInSystem = system.getStations();

        if(stationsInSystem == null) {
            stationsInSystem = new ArrayList<Station>();
        }

        Station newStation = StationFactory.createNewStationWithMarket(stationName);
        newStation.getMisc().put(AppConstants.STATION_TYPE, stationType);
        newStation.getMisc().put(AppConstants.BLACK_MARKET, hasBlackMarket);

        stationsInSystem.add(0, newStation);


        system.setStations(stationsInSystem);

        saveSystem(system);

        updateMiniSystemLastVisited(system.getName());

        return system;
    }

    public static void updateMiniSystemLastVisited(String name) {
        ArrayList<MiniSystem> miniSystems = loadMiniSystems();

        for(MiniSystem mini : miniSystems) {
            if(mini.getName().equals(name)) {
                mini.touch();
            }
        }

        saveMiniSystems(miniSystems);

    }

    public static System updateStationForSystem(String systemName, String stationType, boolean hasBlackMarket, String oldStationName, String newStationName) {
        System system = loadSystem(systemName);

        ArrayList<Station> stationsInSystem = system.getStations();


        for(Station station : stationsInSystem) {
            if(station.getName().equals(oldStationName)) {
                station.setName(newStationName);
                station.getMisc().put(AppConstants.STATION_TYPE, stationType);
                station.getMisc().put(AppConstants.BLACK_MARKET, hasBlackMarket);

                break;
            }
        }

        system.setStations(stationsInSystem);

        saveSystem(system);

        updateMiniSystemLastVisited(system.getName());

        return system;
    }

    public static System deleteStationInSystem(System system, Station station) {
        ArrayList <Station> stationsInSystem = system.getStations();
        stationsInSystem.remove(station);
        saveSystem(system);

        return system;
    }

    public static ArrayList<MiniSystem> deleteSystem(MiniSystem miniSystem) {
        String systemName = miniSystem.getName();
        deleteFile(AppConstants.SYSTEM_BASE_FILENAME + systemName);

        ArrayList<MiniSystem> miniSystems = loadMiniSystems();

        for(int i = 0; i < miniSystems.size(); i++) {
            if(miniSystem.getName().equals(miniSystems.get(i).getName())) {
                miniSystems.remove(i);
            }
        }

        saveMiniSystems(miniSystems);

        return miniSystems;
    }

    public static String getSortType() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DNApplication.getContext());
        return preferences.getString(AppConstants.SORT_TYPE, null);
    }

    public static void setSortType(String sortType) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DNApplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.SORT_TYPE, sortType);
        editor.apply();
    }

    public static boolean getCommodityHide() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DNApplication.getContext());
        return preferences.getBoolean(AppConstants.HIDE_COMMODITIES, false);
    }

    public static void setCommodityHide(boolean hide) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DNApplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AppConstants.HIDE_COMMODITIES, hide);
        editor.apply();
    }


    public static void resetPrefsProperty(String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DNApplication.getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, null);
        editor.apply();
    }
}









