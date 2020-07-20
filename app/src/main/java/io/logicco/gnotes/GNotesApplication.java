package io.logicco.gnotes;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import io.logicco.gnotes.repositories.NotesRepository;
import io.logicco.gnotes.utils.ThemeProvider;

public class GNotesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotesRepository.initialize(this);

        int theme = new ThemeProvider(this).getThemeFromPreferences();
        AppCompatDelegate.setDefaultNightMode(theme);
    }
}
