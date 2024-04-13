package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class PlantAdapter(private val layoutId : Int) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    // boite pour ranger tous les composants à controler
    // autrement dit elle contient les références aux vues de chaque élément de la liste
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        // image de la plante
        // cette ppté contient la référence à la vue correspondant à l'ImageView du layout image_item
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
}