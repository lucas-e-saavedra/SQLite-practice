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

import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Immutable model class for a Note.
 */
public final class NoteContract {
    private NoteContract() {}

    public static class NoteEntry implements BaseColumns{
        public static final String TABLE_NAME = "NOTES";
        public static final String COLUMN_NAME_NOTEID = "noteid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGEURL= "imageurl";
    }



    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_PRIMARYKEY_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String COMMA_SEP = " ,";
    private static final String CREATE_TABLE = "CREATE TABLE table01 (fields)";
    private static final String SQL_TABLE_FIELDS = NoteEntry._ID+TEXT_PRIMARYKEY_TYPE+COMMA_SEP
                                                +NoteEntry.COLUMN_NAME_NOTEID+TEXT_TYPE+COMMA_SEP
                                                +NoteEntry.COLUMN_NAME_TITLE+TEXT_TYPE+COMMA_SEP
                                                +NoteEntry.COLUMN_NAME_DESCRIPTION+TEXT_TYPE+COMMA_SEP
                                                +NoteEntry.COLUMN_NAME_IMAGEURL+TEXT_TYPE;
    public static final String SQL_CREATE_ENTRIES = CREATE_TABLE.replace("table01", NoteEntry.TABLE_NAME).replace("fields", SQL_TABLE_FIELDS);
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+NoteEntry.TABLE_NAME;
}
