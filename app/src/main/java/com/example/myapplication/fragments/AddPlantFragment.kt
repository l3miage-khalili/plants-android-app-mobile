package com.example.myapplication.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity
import com.example.myapplication.PlantRepository
import com.example.myapplication.R

class AddPlantFragment(
    private val context: MainActivity
) : Fragment() {

    private var uploadedImage: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_plant, container, false)

        // récupération de uploadedImage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // récupération du boutton qui charge l'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)
        // association d'une interaction au boutton
        pickupImageButton.setOnClickListener{
            pickupImage()
        }
        return view
    }

    private fun pickupImage() {
        // créaction d'un intent
        val intent = Intent()
        // type d'action
        intent.type = "image/"
        // définition du type d'action à faire avec l'image (récupère l'image suite au click)
        intent.action = Intent.ACTION_GET_CONTENT
        // demarrage de l'action
        // requestCode = numéro qui indique l'action qui va être faite
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 47 && resultCode == Activity.RESULT_OK){
            // vérifie si la réception a été faite avec succès ou pas
            if(data == null || data.data == null){
                return
            }
            else{
                val selectedImage = data.data
                // mise à jour de l'aperçu de l'image
                uploadedImage?.setImageURI(selectedImage)

                //hebergement sur le bucket
                val repo = PlantRepository()
                repo.uploadImage(selectedImage!!)
            }
        }
    }
}