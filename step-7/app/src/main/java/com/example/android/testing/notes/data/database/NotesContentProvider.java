package com.example.android.testing.notes.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Lucas.Saavedra on 23/1/2017.
 */

public class NotesContentProvider extends ContentProvider {
    static final String AUTHORITY = "com.example.android.testing.notes.NotesProvider";
    static final String ACTIVIDAD = "mynotes";
    static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ACTIVIDAD);

    static final int ALLROWS = 1;
    static final int SINGLEROW = 2;
    /** Tipo MIME que retorna la consulta de una sola fila */
    public final static String SINGLE_MIME =  "vnd.android.cursor.item/vnd." + AUTHORITY + ACTIVIDAD;
    /** Tipo MIME que retorna la consulta de varias filas */
    public final static String MULTIPLE_MIME =  "vnd.android.cursor.dir/vnd." + AUTHORITY + ACTIVIDAD;
    private static final UriMatcher sUriMatcher =  new UriMatcher(UriMatcher.NO_MATCH);
    private static NoteDBHelper mDbHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, ACTIVIDAD, ALLROWS);
        sUriMatcher.addURI(AUTHORITY, ACTIVIDAD+"/#", SINGLEROW);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new NoteDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case ALLROWS:
                return MULTIPLE_MIME;
            case SINGLEROW:
                return SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de actividad desconocida: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selectionClause, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)){
            case ALLROWS:
                if(TextUtils.isEmpty(sortOrder))
                    sortOrder = "_ID ASC";
                break;
            case SINGLEROW:
                selectionClause = "_ID = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor oneCursor = db.query(NoteContract.NoteEntry.TABLE_NAME,
                                projection,
                                selectionClause,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
        oneCursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        return oneCursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if(sUriMatcher.match(uri) != ALLROWS)
            throw new IllegalArgumentException("URI desconocida : " + uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insert(NoteContract.NoteEntry.TABLE_NAME, null, contentValues);
        if(rowId > 0 ){
            Uri uriResult = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uriResult, null);
            return uriResult;

        }
        throw new SQLException("Falla al insertar fila en: " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
