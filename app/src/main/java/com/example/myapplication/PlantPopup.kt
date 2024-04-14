package com.example.myapplication

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.myapplication.adapter.PlantAdapter

class PlantPopup(
    private val adapter: PlantAdapter,
    private val currentPlant: PlantModel
) : Dialog(adapter.context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_plant_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }

    private fun updateStar(button : ImageView){
        if(currentPlant.isLiked){
            button.setImageResource(R.drawable.ic_like)
        }
        else{
            button.setImageResource(R.drawable.ic_unlike)
        }
    }

    private fun setupStarButton() {
        // récupération de la vue de l'étoile
        val starButton = findViewById<ImageView>(R.id.star_button)
        // on vérifie si la plante a été likée ou pas pour afficher la bonne étoile
        if(currentPlant.isLiked){
            starButton.setImageResource(R.drawable.ic_like)
        }
        else{
            starButton.setImageResource(R.drawable.ic_unlike)
        }
        // interaction
        starButton.setOnClickListener{
            currentPlant.isLiked = !currentPlant.isLiked
            // mise à jour au niveau de la BD
            val repo = PlantRepository()
            repo.updatePlant(currentPlant)
            // misa à jour de la vue
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener{
            // suppression de la plante de la BDD
            val repo = PlantRepository()
            repo.deletePlant(currentPlant)
            // fermeture de la fenêtre
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener{
            // fermeture de la fenêtre
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualisation de l'image de la plante
        val plantImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentPlant.imageUrl)).into(plantImage)
        // actualisation du nom de la plante
        findViewById<TextView>(R.id.popup_plant_name).text = currentPlant.name

        // actualisation de la description de la plante
        findViewById<TextView>(R.id.popup_plant_description_subtitle).text = currentPlant.description

        // actualisation de la croissance de la plante
        findViewById<TextView>(R.id.popup_plant_grow_subtitle).text = currentPlant.grow

        // actualisation de la consommation de la plante
        findViewById<TextView>(R.id.popup_plant_water_subtitle).text = currentPlant.water
    }
}