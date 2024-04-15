package com.example.myapplication.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.myapplication.MainActivity
import com.example.myapplication.PlantModel
import com.example.myapplication.PlantRepository
import com.example.myapplication.PlantRepository.Singleton.downloadUri
import com.example.myapplication.R
import java.util.UUID

class AddPlantFragment(
    private val context: MainActivity
) : Fragment() {

    private var uploadedImage: ImageView? = null
    private var file: Uri? = null

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

        // récupération du bouton qui confirme l'envoie du formulaire
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        // association d'une interaction au boutton
        confirmButton.setOnClickListener{ sendForm(view) }
        return view
    }

    private fun sendForm(view: View) {
        val repo = PlantRepository()
        repo.uploadImage(file!!) {
            // recupération du nom de la plante
            val plantName = view.findViewById<EditText>(R.id.name_input).text.toString()
            // recupération de la description de la plante
            val plantDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            // recupération de la croissance de la plante
            val grow = view.findViewById<Spinner>(R.id.grow_spinner).selectedItem.toString()
            // recupération de la consommation de la plante
            val water = view.findViewById<Spinner>(R.id.water_spinner).selectedItem.toString()
            // recupération de l'url de l'image de la plante
            val downloadImgUrl = downloadUri

            // création d'un nouvel objet PlantModel
            val plant = PlantModel(
                UUID.randomUUID().toString(),
                plantName,
                plantDescription,
                downloadImgUrl.toString(),
                grow,
                water
            )

            // envoie de la nouvelle plante en BDD
            repo.updatePlant(plant)
        }

    }

    // recupère une image depuis la galerie Photo
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 47 && resultCode == Activity.RESULT_OK){
            // vérifie si la réception a été faite avec succès ou pas
            if(data == null || data.data == null){
                return
            }
            else{
                val file = data.data
                // mise à jour de l'aperçu de l'image
                uploadedImage?.setImageURI(file)

                //hebergement sur le bucket
                val repo = PlantRepository()
                repo.uploadImage(file!!){}
            }
        }
    }
}