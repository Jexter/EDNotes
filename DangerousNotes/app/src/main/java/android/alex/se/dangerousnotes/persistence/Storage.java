package android.alex.se.dangerousnotes.persistence;

import android.alex.se.dangerousnotes.common.AppConstants;
import android.alex.se.dangerousnotes.common.DNApplication;
import android.alex.se.dangerousnotes.common.StationFactory;
import android.alex.se.dangerousnotes.model.MiniSystem;
import android.alex.se.dangerousnotes.model.Station;
import android.alex.se.dangerousnotes.model.System;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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


    public static void createAndSaveNewSystem(String systemName) {
        ArrayList<MiniSystem> miniSystems = loadMiniSystems();

        if(miniSystems == null) {
            miniSystems = new ArrayList<MiniSystem>();
        }

        miniSystems.add(new MiniSystem(systemName));
        System newSystem = new System(null, systemName);

        saveMiniSystems(miniSystems);
        saveSystem(newSystem);
    }

    public static void createAndSaveNewStationForSystem(String systemName, String stationName) {
        System system = loadSystem(systemName);
        ArrayList<Station> stationsInSystem = system.getStations();

        if(stationsInSystem == null) {
            stationsInSystem= new ArrayList<Station>();
        }

        Station newStation = StationFactory.createNewStationWithMarket(stationName);
        stationsInSystem.add(newStation);

        system.setStations(stationsInSystem);

        saveSystem(system);
    }


    public static void updateAndSaveStationInSystem(System system, Station station) {
        ArrayList <Station> stationsInSystem = system.getStations();
        stationsInSystem.remove(station);
        saveSystem(system);
    }

    public static void deleteSystem(MiniSystem miniSystem) {
        String systemName = miniSystem.getName();
        deleteFile(AppConstants.SYSTEM_BASE_FILENAME + systemName);

        ArrayList<MiniSystem> miniSystems = loadMiniSystems();

        for(int i = 0; i < miniSystems.size(); i++) {
            if(miniSystem.getName().equals(miniSystems.get(i).getName())) {
                miniSystems.remove(i);
            }
        }

        saveMiniSystems(miniSystems);

    }
}









