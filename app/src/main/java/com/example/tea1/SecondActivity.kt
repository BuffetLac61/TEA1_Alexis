package com.example.tea1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast


class SecondActivity : AppCompatActivity() {
    private lateinit var etListName: EditText
    private lateinit var btnAddList: Button
    private lateinit var listView: ListView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listAdapter: ArrayAdapter<String>
    private lateinit var userList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        etListName = findViewById(R.id.etListName)
        btnAddList = findViewById(R.id.btnAddList)
        listView = findViewById(R.id.listView)

        // Initialisation des SharedPreferences
        sharedPreferences = getSharedPreferences("UserLists", MODE_PRIVATE)

        // Récupération du pseudo enregistré
        val pseudo = intent.getStringExtra("PSEUDO")

        // Affichage du pseudo précédemment enregistré
        Toast.makeText(this, "Pseudo : $pseudo", Toast.LENGTH_SHORT).show()

        // Récupération de la liste des listes de l'utilisateur
        val userKey = "UserLists_$pseudo" // Clé unique pour chaque pseudo
        userList = ArrayList(sharedPreferences.getStringSet(userKey, emptySet()))

        // Initialisation de l'adapter pour la ListView
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)

        // Affichage de la liste des listes
        listView.adapter = listAdapter

        // Gestion du clic sur une liste
        listView.setOnItemClickListener { _, _, position, _ ->
            // Lancement de l'activité ShowListActivity
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("PSEUDO", pseudo)
            intent.putExtra("LIST_INDEX", position)
            startActivity(intent)
        }

        // Gestion du clic sur le bouton Ajouter
        btnAddList.setOnClickListener {
            val newList = etListName.text.toString().trim()

            if (newList.isNotEmpty()) {
                // Ajout de la nouvelle liste
                userList.add(newList)
                listAdapter.notifyDataSetChanged()

                // Mise à jour des SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putStringSet(userKey, userList.toSet())
                editor.apply()

                // Effacement du champ de saisie
                etListName.text.clear()

                Toast.makeText(this, "Nouvelle liste ajoutée", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Veuillez entrer un nom de liste", Toast.LENGTH_SHORT).show()
            }
        }
    }
}