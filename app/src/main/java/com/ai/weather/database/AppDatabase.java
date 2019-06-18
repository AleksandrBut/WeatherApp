package com.ai.weather.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ai.weather.dao.CityDAO;
import com.ai.weather.dao.CityInfoDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Database(entities = {City.class, CityInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "appdb.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract CityDAO cityDAO();

    public abstract CityInfoDAO cityInfoDAO();

    public static AppDatabase getInstance(Context context) {
        final File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            AssetManager assetManager = context.getAssets();
            try {
                InputStream inputStream = assetManager.open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream("data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                Log.d(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ", "Database copied successfully");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ERROR", "Can't copy database");
            }
        }

        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
