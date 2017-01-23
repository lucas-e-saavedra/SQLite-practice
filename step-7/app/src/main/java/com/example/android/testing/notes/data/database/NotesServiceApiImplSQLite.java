/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesServiceApi;
import com.example.android.testing.notes.data.NotesServiceApiEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Notes Service API that adds a latency simulating network.
 */
public class NotesServiceApiImplSQLite implements NotesServiceApi {
    private static NoteDBHelper mDbHelper;
    public static void createDbHelper(Context ctx){
        mDbHelper = new NoteDBHelper(ctx);
    }

    @Override
    public void getAllNotes(Context ctx, final NotesServiceCallback callback) {
        if(mDbHelper==null)
            createDbHelper(ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                NoteContract.NoteEntry.COLUMN_NAME_NOTEID,
                NoteContract.NoteEntry.COLUMN_NAME_TITLE,
                NoteContract.NoteEntry.COLUMN_NAME_DESCRIPTION,
                NoteContract.NoteEntry.COLUMN_NAME_IMAGEURL};

        String selection = null;//NoteContract.NoteEntry.TABLE_NAME + " = ?";
        String[] selectionArgs = null;//{"My Title"};

        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_TITLE + " DESC";
        Cursor cursor = db.query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        ArrayList<Note> data = new ArrayList<>();
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String id = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_NOTEID));
                    String title = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_DESCRIPTION));
                    String imageurl = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_IMAGEURL));
                    data.add(new Note(id, title, description, imageurl));
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        callback.onLoaded(data);
    }

    @Override
    public void getNote(Context ctx, final String pNoteId, final NotesServiceCallback callback) {
        if(mDbHelper==null)
            createDbHelper(ctx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                NoteContract.NoteEntry.COLUMN_NAME_NOTEID,
                NoteContract.NoteEntry.COLUMN_NAME_TITLE,
                NoteContract.NoteEntry.COLUMN_NAME_DESCRIPTION,
                NoteContract.NoteEntry.COLUMN_NAME_IMAGEURL};

        String selection = NoteContract.NoteEntry.COLUMN_NAME_NOTEID + " = ?";
        String[] selectionArgs = {pNoteId};

        String sortOrder = NoteContract.NoteEntry.COLUMN_NAME_TITLE + " DESC";
        Cursor cursor = db.query(
                NoteContract.NoteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        Note oneNote = null;
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String noteId = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_NOTEID));
                    String title = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_TITLE));
                    String description = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_DESCRIPTION));
                    String imageurl = cursor.getString(cursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_NAME_IMAGEURL));
                    oneNote = new Note(noteId, title, description, imageurl);
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        callback.onLoaded(oneNote);
    }

    @Override
    public void saveNote(Context ctx, Note note) {
        if(mDbHelper==null)
            createDbHelper(ctx);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NoteContract.NoteEntry.COLUMN_NAME_NOTEID, note.getId());
        values.put(NoteContract.NoteEntry.COLUMN_NAME_TITLE, note.getTitle());
        values.put(NoteContract.NoteEntry.COLUMN_NAME_DESCRIPTION, note.getDescription());
        values.put(NoteContract.NoteEntry.COLUMN_NAME_IMAGEURL, note.getImageUrl());

        long bla =  db.insert(NoteContract.NoteEntry.TABLE_NAME, null, values);
        Log.d("bla", String.valueOf(bla));
    }

}
