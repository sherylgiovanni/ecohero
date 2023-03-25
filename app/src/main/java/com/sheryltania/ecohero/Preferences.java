package com.sheryltania.ecohero;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

public class Preferences extends PreferenceActivity {

    private MusicPlayer musicPlayer;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);
        musicPlayer = MusicPlayer.getInstance(this);

        // Sound preference
        SwitchPreference music = new SwitchPreference(this);
        music.setTitle("Background music");
        music.setSummaryOn("The new setting will take effect after this screen is closed.");
        music.setSummaryOff("The new setting will take effect after this screen is closed.");
        music.setDefaultValue(true);
        music.setKey("MUSIC_PREF");

        // Difficulty preference
        ListPreference difficulty = new ListPreference(this);
        difficulty.setTitle("Difficulty");
        difficulty.setSummary("Pick your desired difficulty level.");
        difficulty.setKey("DIFFICULTY_PREF");
        String[] difficultyEntries = {"Hard", "Medium", "Easy"};
        difficulty.setEntries(difficultyEntries);
        String[] difficultyLevels = {"3", "2", "1"};
        difficulty.setEntryValues(difficultyLevels);
        difficulty.setDefaultValue("1");


        // Configuring preferences
        ps.addPreference(difficulty);
        ps.addPreference(music);
        setPreferenceScreen(ps);
    }

    public static int getDifficulty(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("DIFFICULTY_PREF", "1"));
    }

    public static boolean soundOn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MUSIC_PREF", true);
    }

}