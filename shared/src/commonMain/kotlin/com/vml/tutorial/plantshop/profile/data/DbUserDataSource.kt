package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.profile.domain.Address
import com.vml.tutorial.plantshop.profile.domain.PaymentMethod
import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import database.UserQueries

class DbUserDataSource(db: PlantDatabase) : UserDataSource {
    private val queries: UserQueries = db.userQueries
    override suspend fun insertToDatabase(user: User) {
        queries.insertUser(
            cId = user.cId?.trim(),
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
            additionalDescription = user.address?.additionalDescription.orEmpty(),
            cardHolderName = user.paymentMethod?.cardHolderName.orEmpty(),
            creditCardNumber = user.paymentMethod?.creditCardNumber.orEmpty(),
            cvv = user.paymentMethod?.cvv.orEmpty(),
            expirationDate = user.paymentMethod?.expirationDate.orEmpty()
        )
    }

    override suspend fun removeFromDatabase() {
        queries.removeUser()
    }

    override suspend fun getUser(email: String?): User {
        val user = queries.getUser().executeAsOne()
        return User(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            birthDate = user.birthDate,
            phoneNumber = user.phoneNumber,
            address = Address(
                streetName = user.streetName,
                doorNumber = user.doorNumber?.toInt(),
                city = user.city,
                postalCode = user.postalCode?.toInt(),
                country = user.country,
                additionalDescription = user.additionalDescription
            ),
            paymentMethod = PaymentMethod(
                cardHolderName = user.cardHolderName,
                creditCardNumber = user.creditCardNumber,
                cvv = user.cvv,
                expirationDate = user.expirationDate
            )
        )
    }

    override suspend fun isThereUser(): Boolean {
        return queries.getUserCount().executeAsOne().toInt() > 0
    }
}
