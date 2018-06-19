package com.example.user.movielookup;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import static com.example.user.movielookup.Contract.path_Tasks;

public class MyContentProvider extends ContentProvider {
    public static final int Table_Code=200;
    public static final int Row_Code=201;
    private static final UriMatcher uriMatcher=buildUriMatcher();
    private DbHelper dbHelper;
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        String authority=Contract.AUTHORITY;
        matcher.addURI(authority,path_Tasks,Table_Code);
        matcher.addURI(authority,path_Tasks+"/*",Row_Code);
        return matcher;
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implemented this to handle requests to delete one or more rows.
        int rowDeleted=0;
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        switch(uriMatcher.match(uri)){

            case Row_Code:
                rowDeleted=database.delete(Contract.tablename,selection,selectionArgs);
                break;
            default:throw new UnsupportedOperationException("Not yet implemented");
        }
        if(rowDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // Implemented this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        long id;
        switch(uriMatcher.match(uri)){

            case Row_Code:
                id=database.insert(Contract.tablename,null,values);
                break;
            case Table_Code:
                id=database.insert(Contract.tablename,null,values);
                break;
            default:throw new UnsupportedOperationException("Not yet implemented");
        }
        Uri u=Uri.parse(""+id);
        return u;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper=new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Cursor dbcursor;
        switch (uriMatcher.match(uri)){
            case Table_Code:
                dbcursor=dbHelper.getReadableDatabase().query(Contract.tablename,null,null,null,null,null,null);
                break;
            case Row_Code:
                dbcursor=dbHelper.getReadableDatabase().query(Contract.tablename,null,selection,selectionArgs,null,null,null);
                break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented");
        }
        dbcursor.setNotificationUri(getContext().getContentResolver(),uri);
        return dbcursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
