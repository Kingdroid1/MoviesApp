package com.company.moviesapp.movieDatabase;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieEntity.class}, version = 1, exportSchema = false)

public abstract class MovieDB extends RoomDatabase {
    private static final String LOG_TAG = MovieDB.class.getSimpleName ();
    private static final Object LOCK = new Object ();
    private static final String DATABASE_NAME = "movies";
    private static MovieDB sInstance;

    public static MovieDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d ( LOG_TAG, "Creating new database instance" );
                sInstance = Room.databaseBuilder ( context.getApplicationContext (),
                        MovieDB.class, MovieDB.DATABASE_NAME )
                        .fallbackToDestructiveMigration ()
                        .build ();
            }
        }
        Log.d ( LOG_TAG, "Getting the database instance" );
        return sInstance;
    }

    public abstract MovieDao MovieDao();
}
