package com.example.tea1

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowListActivity : AppCompatActivity() {
    private lateinit var etNewItem: EditText
    private lateinit var btnAddItem: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemList: ArrayList<Item>
    private lateinit var username: String
    private var listIndex: Int = 0

    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        etNewItem = findViewById(R.id.etNewItem)
        btnAddItem = findViewById(R.id.btnAddItem)
        recyclerView = findViewById(R.id.recyclerViewItems)

        // Récupération des données de l'intent
        username = intent.getStringExtra("PSEUDO") ?: "Default"
        listIndex = intent.getIntExtra("LIST_INDEX", 0)

        // Affichage du pseudo et de l'index de la liste
        Toast.makeText(this, "Pseudo : $username, Index de la liste : $listIndex", Toast.LENGTH_SHORT).show()

        // Récupération de la liste d'items de l'utilisateur
        sharedPreferences = getSharedPreferences("UserItems", MODE_PRIVATE)
        val listKey = "UserItems_${username}_$listIndex"
        itemList = ArrayList()

        // Chargement des items à partir des SharedPreferences
        val savedItems = sharedPreferences.getStringSet(listKey, emptySet())
        savedItems?.forEach { itemText ->
            val item = Item(itemText, false)
            itemList.add(item)
        }

        // Initialisation de l'adapter RecyclerView
        itemAdapter = ItemAdapter(itemList)

        // Configuration du RecyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ShowListActivity)
            adapter = itemAdapter
        }

        // Gestion du clic sur le bouton Ajouter
        btnAddItem.setOnClickListener {
            val newItem = etNewItem.text.toString().trim()

            if (newItem.isNotEmpty()) {
                // Ajout du nouvel item
                val item = Item(newItem, false)
                itemList.add(item)
                itemAdapter.notifyItemInserted(itemList.size - 1)

                // Mise à jour des SharedPreferences
                saveItemsToSharedPreferences(listKey)

                // Effacement du champ de saisie
                etNewItem.text.clear()

                Toast.makeText(this, "Nouvel item ajouté", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Veuillez entrer un nouvel item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveItemsToSharedPreferences(listKey: String) {
        val editor = sharedPreferences.edit()
        val itemSet = HashSet<String>()

        itemList.forEach { item ->
            itemSet.add(item.text)
        }

        editor.putStringSet(listKey, itemSet)
        editor.apply()
    }
}

// Modèle d'Item
data class Item(val text: String, var isChecked: Boolean)

// Adaptateur RecyclerView
class ItemAdapter(private val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBoxItem: CheckBox = itemView.findViewById(R.id.checkBoxItem)

        fun bind(item: Item) {
            checkBoxItem.text = item.text
            checkBoxItem.isChecked = item.isChecked

            checkBoxItem.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
            }
        }
    }
}