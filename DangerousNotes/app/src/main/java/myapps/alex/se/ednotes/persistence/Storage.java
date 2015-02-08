package myapps.alex.se.ednotes.persistence;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import myapps.alex.se.ednotes.common.AppConstants;
import myapps.alex.se.ednotes.common.DNApplication;
import myapps.alex.se.ednotes.common.StationFactory;
import myapps.alex.se.ednotes.model.MiniSystem;
import myapps.alex.se.ednotes.model.Station;
import myapps.alex.se.ednotes.model.System;

/**
 * Created by atkinson on 19/12/14.
 */
public class Storage {

    public static System loadSystem(String systemName) {
        System system = (System) deserializeObject(AppConstants.SYSTEM_BASE_FILENAME + systemName);

        return system;
    }

    public static void saveSystem(System system) {
        String fileName = AppConstants.SYSTEM_BASE_FILENAME + system.getName();
        serializeObject(system, fileName);
    }

    public static ArrayList<Station> loadStationsForSystem(String systemName) {
        System sys = loadSystem(systemName);
        ArrayList<Station> stationList = sys.getStations();

        return stationList;
    }

    public static ArrayList<MiniSystem> loadMiniSystems() {

        ArrayList<MiniSystem> miniSystems = (ArrayList<MiniSystem>) deserializeObject(AppConstants.MINI_SYSTEMS_FILENAME);
/*
        if(miniSystems == null) {
            Log.d("Storage says", "miniSystems was null, saving some premade ones");
            ArrayList<MiniSystem> newMiniSystems = new ArrayList<MiniSystem>();

            MiniSystem ms1 = new MiniSystem("Leesti");
            MiniSystem ms2 = new MiniSystem("Reorte");

            newMiniSystems.add(ms1);
            newMiniSystems.add(ms2);

            saveMiniSystems(newMiniSystems);
            miniSystems = (ArrayList<MiniSystem>) deserializeObject(AppConstants.MINI_SYSTEMS_FILENAME);
        }
        else {
            Log.d("Storage says", "found some miniSystems (" + miniSystems.size() + ") on disk!");
        }
*/
        return miniSystems;
    }

    private static void saveMiniSystems(ArrayList<MiniSystem> miniSystems) {
        serializeObject(miniSystems, AppConstants.MINI_SYSTEMS_FILENAME);
    }








    public static void serializeObject(Object object, String fileName) {
        File file = new File(DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE), fileName);

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
        File file = new File(DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE), fileName);

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
        File file = new File(DNApplication.getContext().getDir(AppConstants.APPDATA_FOLDER, Context.MODE_PRIVATE), fileName);

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
                renameFile(AppConstants.SYSTEM_BASE_FILENAME + oldSystemName, AppConstants.SYSTEM_BASE_FILENAME + newSystemName);
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

        return system;
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
}









