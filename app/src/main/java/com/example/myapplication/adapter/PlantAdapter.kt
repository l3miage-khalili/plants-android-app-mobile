package com.example.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity
import com.example.myapplication.PlantModel
import com.example.myapplication.PlantPopup
import com.example.myapplication.PlantRepository
import com.example.myapplication.R

class PlantAdapter(
    val context: MainActivity,
    private val plantList: List<PlantModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    // boite pour ranger tous les composants à controler
    // autrement dit elle contient les références aux vues de chaque élément de la liste
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        // image de la plante
        // cette ppté contient la référence à la vue correspondant à l'ImageView du layout image_item
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
        val plantName:TextView? = view.findViewById(R.id.name_item)
        val plantDescription:TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    // Gère et met à jour les données de la vue
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recupération de la plante à la position position
        val currentPlant = plantList[position]

        // récupération du repository
        val repo = PlantRepository()

        // utilisation de glide afin de récupérer l'image correspondant à la plante
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        // mise à jour du nom de la plante s'il existe (existe pour item_vertical)
        holder.plantName?.text = currentPlant.name

        // mise à jour de la description de la plante s'il existe(existe pour item_vertical)
        holder.plantDescription?.text = currentPlant.description

        // vérifie si l'image a été likée
        if(currentPlant.isLiked){
            holder.starIcon.setImageResource(R.drawable.ic_like)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unlike)
        }

        // rajout de l'interaction sur l'étoile
        holder.starIcon.setOnClickListener{
            // inverse l'état de l'étoile
            currentPlant.isLiked = !currentPlant.isLiked
            // met à jour l'objet plant
            repo.updatePlant(currentPlant)
        }

        // interaction lors d'un clique sur une plante
        holder.itemView.setOnClickListener{
            // affichage de la popup
            PlantPopup(this, currentPlant).show()
        }
    }

    override fun getItemCount(): Int = plantList.size

}