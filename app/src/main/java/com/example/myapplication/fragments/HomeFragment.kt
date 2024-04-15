package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.PlantRepository.Singleton.plantList
import com.example.myapplication.R
import com.example.myapplication.adapter.PlantAdapter
import com.example.myapplication.adapter.PlantItemDecoration

class HomeFragment(
    private val context: MainActivity
) : Fragment() {

    /* Crée ou instancie la vue associée à fragment_home.xml */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /*
        utilise un LayoutInflater (ici inflater) qui permet de créer la vue
        sa fonction inflate permet de transformer un fichier xml (ici fragment_home.xml) en une
        représentation que le système Android peut comprendre et afficher à l'utilisateur à l'écran
        */
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        // recupération du recyclerView horizontal
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = PlantAdapter(context, plantList.filter {!it.isLiked}, R.layout.item_horizontal_plant)

        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = PlantAdapter(context, plantList, R.layout.item_vertical_plant)
        verticalRecyclerView.addItemDecoration(PlantItemDecoration())

        return view
    }
}