package com.example.tea1

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var etPseudo: EditText
    private lateinit var btnValider : Button
    private lateinit var tvDernierPseudo: TextView
    private lateinit var btnSettings : Button
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPseudo = findViewById(R.id.etPseudo)
        btnValider = findViewById(R.id.btnValider)
        tvDernierPseudo = findViewById(R.id.tvDernierPseudo)
        btnSettings = findViewById(R.id.btnSettings)

        // Initialisation des SharedPreferences
        sharedPreferences = getPreferences(MODE_PRIVATE)

        // Récupération du dernier pseudo enregistré
        val dernierPseudo = sharedPreferences.getString("LAST_PSEUDO", "")
        tvDernierPseudo.text = "Dernier pseudo enregistré : $dernierPseudo"

        // Pré-remplissage du champ de saisie avec le dernier pseudo
        etPseudo.setText(dernierPseudo)

        // Gestion du clic sur le bouton Valider
        btnValider.setOnClickListener {
            val pseudo = etPseudo.text.toString()

            // Enregistrement du pseudo dans les SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("LAST_PSEUDO", pseudo)
            editor.apply()

            // Affichage du pseudo enregistré
            tvDernierPseudo.text = "Dernier pseudo enregistré : $pseudo"

            // Lancement de la nouvelle activité avec le pseudo en tant qu'extra
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("PSEUDO", pseudo)
            startActivity(intent)
        }

        // Gestion du clic sur le bouton Settings
        btnSettings.setOnClickListener {

            // Lancement de la nouvelle activité
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
