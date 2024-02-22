package com.vml.tutorial.plantshop.profilePreferences.data

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.profilePreferences.domain.Address
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.domain.UserDataSource
import database.UserQueries

class DbUserDataSource(db: PlantDatabase) : UserDataSource {
    private val queries: UserQueries = db.userQueries
    override suspend fun insertToDatabase(user: User) {
        queries.insertUser(
            cId = user.cId.orEmpty().trim(),
            firstName = user.firstName.orEmpty(),
            lastName = user.lastName.orEmpty(),
            email = user.email,
            birthDate = user.birthDate.orZero(),
            phoneNumber = user.phoneNumber.orEmpty(),
            streetName = user.address?.streetName.orEmpty(),
            doorNumber = user.address?.doorNumber?.toLong().orZero(),
            city = user.address?.city.orEmpty(),
            postalCode = user.address?.postalCode?.toLong().orZero(),
            country = user.address?.country.orEmpty(),
            additionalDescription = user.address?.additionalDescription.orEmpty()
        )
    }

    override suspend fun removeFromDatabase(cId: String?) {
        queries.removeUser()
    }

    override suspend fun getUser(email: String?): User? = try {
        val user = queries.getUser().executeAsOne()
        User(
            cId = user.cId.trim(),
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
    } catch (err: Exception) {
        err.printStackTrace()
        null
    }

    override suspend fun isThereUser(): Boolean {
        return queries.getUserCount().executeAsOne().toInt() > 0
    }

    override suspend fun updateUserInfo(user: User) {
        // INSERT OR REPLACE doesn't work for some reason
        removeFromDatabase(null)
        insertToDatabase(user)
    }
}
