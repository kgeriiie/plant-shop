package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.profile.domain.Address
import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import database.UserQueries

class DbUserDataSource(db: PlantDatabase) : UserDataSource {
    private val queries: UserQueries = db.userQueries
    override suspend fun insertToDatabase(user: User) {
        queries.insertUser(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            birthDate = user.birthDate,
            phoneNumber = user.phoneNumber,
            streetName = user.address.streetName,
            doorNumber = user.address.doorNumber.toLong(),
            city = user.address.city,
            postalCode = user.address.postalCode.toLong(),
            country = user.address.country,
            additionalDescription = user.address.additionalDescription
        )
    }

    override suspend fun removeFromDatabase() {
        queries.removeUser()
    }

    override suspend fun getUser(): User {
        val user = queries.getUser().executeAsOne()
        return User(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            birthDate = user.birthDate,
            phoneNumber = user.phoneNumber,
            Address(
                streetName = user.streetName,
                doorNumber = user.doorNumber.toInt(),
                city = user.city,
                postalCode = user.postalCode.toInt(),
                country = user.country,
                additionalDescription = user.additionalDescription
            )
        )
    }

    override suspend fun isThereUser(): Boolean {
        return queries.getUserCount().executeAsOne().toInt() > 0
    }
}
