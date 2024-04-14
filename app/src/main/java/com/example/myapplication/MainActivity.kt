package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.fragments.HomeFragment

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

        // injecter le fragment (fragment_container) dans notre boite
        /*
        * supportFragmentManager gère les fragments sur android
        * beginTransaction() commence une série des opérations pour pouvoir manipuler
        * les fragments
        * */
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, HomeFragment(this))
        transaction.addToBackStack(null)
        transaction.commit()
    }
}