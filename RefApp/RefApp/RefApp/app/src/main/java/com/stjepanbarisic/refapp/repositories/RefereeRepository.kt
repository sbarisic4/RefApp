package com.stjepanbarisic.refapp.repositories

import com.google.firebase.database.FirebaseDatabase
import com.stjepanbarisic.refapp.models.Referee
import kotlinx.coroutines.tasks.await

private const val REFEREE_ROOT_NAME = "Referees"

class RefereeRepository {

    private val firebaseInstance = FirebaseDatabase.getInstance()

    suspend fun addReferee(referee: Referee) {
        val refereeReference = firebaseInstance.getReference(REFEREE_ROOT_NAME).push()
        val id = refereeReference.key
        referee.refereeId = id.toString()
        refereeReference.setValue(referee).await()
    }

    suspend fun getReferees(): List<Referee> {
        val refereeList = mutableListOf<Referee>()
        val refereeData = firebaseInstance.getReference(REFEREE_ROOT_NAME).get().await()
        for (child in refereeData.children) {
            val referee = child.getValue(Referee::class.java)
            if (referee != null) {
                refereeList.add(referee)
            }
        }
        return refereeList
    }

}