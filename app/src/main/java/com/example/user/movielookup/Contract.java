package com.example.user.movielookup;

import android.net.Uri;

public class Contract {
    public static String scheme="content://";
    public static final String AUTHORITY="com.example.user.movielookup";
    public static final Uri BASE_CONTENT_URI=Uri.parse(scheme+AUTHORITY);
    public static final String path_Tasks="favorites";
    public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(path_Tasks).build();
    public static final String Databasename="MovieDatabase";
    public static final String tablename="favorites";
    public static final int version=1;
    public static final String rowid="rowid";
    public static final String movie_id="id";
    public static final String movie_title="originaltitle";
    public static final String movie_backdroppath="backdroppath";
    public static final String movie_posterpath="posterpath";
    public static final String movie_rating="rating";
    public static final String movie_releasedate="releasedate";
    public static final String movie_overview="overview";

}
