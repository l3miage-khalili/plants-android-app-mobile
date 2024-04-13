package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class HomeFragment : Fragment() {

    // Crée ou instancie la vue associée à fragment_home.xml
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // utilise un LayoutInflater (ici inflater) qui permet de créer la vue
        // sa fonction inflate permet de transformer un fichier xml (ici fragment_home.xml) en une
        // représentation que le système Android peut comprendre et afficher à l'utilisateur à l'écran
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}