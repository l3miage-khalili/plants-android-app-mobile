package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.AddPlantFragment
import com.example.myapplication.fragments.CollectionFragment
import com.example.myapplication.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    /* Cette méthode est l'une des méthodes de cycle de vie les plus importantes d'une activité.
       Elle est appelée lorsque l'activité est créée pour la première fois. C'est ici qu'on configure
       généralement l'interface utilisateur de notre activité en chargeant une vue (layout) à l'aide
       de setContentView() et en initialisant d'autres composants nécessaires.
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadFragment(HomeFragment(this), R.string.home_page_title)

        // importation du botton navigation view
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_page -> {
                    loadFragment(HomeFragment(this), R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this), R.string.collection_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_plant_page -> {
                    loadFragment(AddPlantFragment(this), R.string.add_plant_page_title)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {false}
            }
        }
        loadFragment(HomeFragment(this), R.string.home_page_title)
    }

    private fun loadFragment(fragment: Fragment, string: Int) {

        // chargement du repository
        val repo = PlantRepository()

        // actualisation du titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // charge et met à jour la liste des plantes, son contenu est excécuté comme callback après le chargement de la liste
        repo.updateData{
            // injecte le fragment (fragment_container) dans notre boite
            /*
            * supportFragmentManager gère les fragments sur android
            * beginTransaction() commence une série des opérations pour pouvoir manipuler les fragments
            * */
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}