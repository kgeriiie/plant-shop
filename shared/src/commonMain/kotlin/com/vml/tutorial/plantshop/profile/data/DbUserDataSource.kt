package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.utils.exts.orZero
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
            streetName = user.address?.streetName.orEmpty(),
            doorNumber = user.address?.doorNumber.orZero().toLong(),
            city = user.address?.city.orEmpty(),
            postalCode = user.address?.postalCode.orZero().toLong(),
            country = user.address?.country.orEmpty(),
            additionalDescription = user.address?.additionalDescription.orEmpty()
        )
    }

    override suspend fun removeFromDatabase() {
        queries.removeUser()
    }

    override suspend fun getUser(email: String?): User? {
        return queries.getUser().executeAsOneOrNull()?.let { user ->
            User(
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
    }

    override suspend fun isThereUser(): Boolean {
        return queries.getUserCount().executeAsOne().toInt() > 0
    }
}
