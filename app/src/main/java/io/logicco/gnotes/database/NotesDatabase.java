package io.logicco.gnotes.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import io.logicco.gnotes.models.Note;

@Database(entities = {Note.class}, version = 3)
@TypeConverters({NotesTypeConverter.class})
public abstract class NotesDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "notes";
    public abstract NotesDao noteDao();

}
