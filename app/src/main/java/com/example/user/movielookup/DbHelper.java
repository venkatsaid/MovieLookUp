package com.example.user.movielookup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.user.movielookup.Contract.Databasename;
import static com.example.user.movielookup.Contract.movie_backdroppath;
import static com.example.user.movielookup.Contract.movie_id;
import static com.example.user.movielookup.Contract.movie_overview;
import static com.example.user.movielookup.Contract.movie_posterpath;
import static com.example.user.movielookup.Contract.movie_rating;
import static com.example.user.movielookup.Contract.movie_releasedate;
import static com.example.user.movielookup.Contract.movie_title;
import static com.example.user.movielookup.Contract.rowid;
import static com.example.user.movielookup.Contract.tablename;
import static com.example.user.movielookup.Contract.version;

public class DbHelper extends SQLiteOpenHelper{
    public Context c;
    public DbHelper(Context context) {
        super(context,Databasename, null, version);
        c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        final String query="CREATE TABLE "+tablename+"("+rowid+" INTEGER PRIMARY KEY AUTOINCREMENT,"+movie_id+" INTEGER, "+
                movie_title+" TEXT,"+movie_backdroppath+" TEXT,"+movie_posterpath+" TEXT,"+movie_rating+" INTEGER,"+
                movie_releasedate+" TEXT,"+movie_overview+" TEXT );";
        database.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS "+tablename);
        onCreate(database);
    }

}
