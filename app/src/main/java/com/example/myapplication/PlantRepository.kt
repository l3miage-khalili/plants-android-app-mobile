package com.example.myapplication

import android.net.Uri
import com.example.myapplication.PlantRepository.Singleton.databaseRef
import com.example.myapplication.PlantRepository.Singleton.downloadUri
import com.example.myapplication.PlantRepository.Singleton.plantList
import com.example.myapplication.PlantRepository.Singleton.storageRef
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.UUID

// Gère l'intégration avec la base des données
class PlantRepository {

    // utilisation d'une classe Singleton pour éviter de recharger la list à chaque appel de PlantRepository
    object Singleton {
        // connexion à l'étape de storage
        // 1. donne le lien pour accéder au bucket
        private val BUCKET_URL: String = "gs://naturecollection-96105.appspot.com"

        // 2. se connecter à notre espace de stockage
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // connection avec la référence "plants" dans notre base de données
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // création d'une liste qui va contenir les plantes
        val plantList = arrayListOf<PlantModel>()

        // lien de l'image courante
        var downloadUri: Uri? = null
    }

    // charge et met à jour les données depuis la databaseRef et les donnes à liste plantList
    fun updateData(callback: () -> Unit) {
        databaseRef.addValueEventListener(object: ValueEventListener {
            // DataSnapshot contient toutes les données recupérées mais qui sont pas encore
            // sous la forme d'un PlantModel
            override fun onDataChange(snapshot: DataSnapshot) {

                // retirer les anciennes plantes quand on fait un mise à jour
                plantList.clear()

                // recolte la liste
                for (ds in snapshot.children){
                    // construction d'un PlantModel
                    val plant = ds.getValue(PlantModel::class.java)
                    // vérifier si la récupération a été faite avec succès
                    if(plant != null){
                        // Ajout la plant à la liste des plantes
                        plantList.add(plant)
                    }
                }

                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    // envoie des fichiers images sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit){
        // vérifie que le fichier est valide
        if(file != null){
            // génération d'un identifiant sous forme de texte (nom du fichier)
            val fileName = UUID.randomUUID().toString() + ".jpg"
            // indique à quel endroit de la BDD on veut ranger le nom du fichier
            val ref = storageRef.child(fileName)
            // envoie le fichier dans une tâche d'envoie, indique quel est le contenu qu'on veut soumettre
            val uploadTask = ref.putFile(file)

            // demarrage de la tâche d'envoie
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                // vérifie s'iy a eu d'erreur lors de l'envoie du fichier
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                // vérifie si tout a bien fonctionné
                if(task.isSuccessful){
                    // recupération de l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    // met à jour un objet PlantModel dans la base des données s'il existe déjà
    // sinon elle l'insert tout simplement
    fun updatePlant(plant:PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // supprime une plante de la base
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
}