package com.example.myapplication

import com.example.myapplication.PlantRepository.Singleton.databaseRef
import com.example.myapplication.PlantRepository.Singleton.plantList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Gère l'intégration avec la base des données
class PlantRepository {

    // utilisation d'une classe Singleton pour éviter de recharger la list à chaque appel de PlantRepository
    object Singleton {
        // connection avec la référence "plants" dans notre base de données
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // création d'une liste qui va contenir les plantes
        val plantList = arrayListOf<PlantModel>()
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

    // met à jour un objet PlantModel dans la base des données
    fun updatePlant(plant:PlantModel) = databaseRef.child(plant.id).setValue(plant)
}