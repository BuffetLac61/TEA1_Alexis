package com.example.tea1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.preference.PreferenceManager

class SettingsActivity : PreferenceActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Définir la vue de préférence depuis le fichier XML
        addPreferencesFromResource(R.xml.preferences)

        // Initialisation des SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        // Gestion du clic sur le bouton "Réinitialiser les données"
        val resetDataPreference = findPreference("reset_data")
        resetDataPreference.setOnPreferenceClickListener {
            resetData()
            true
        }
    }

    private fun resetData() {
        // Effacer toutes les données enregistrées dans SharedPreferences
        sharedPreferences.edit().clear().apply()
    }
}