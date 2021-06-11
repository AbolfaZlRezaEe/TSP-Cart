package com.abproject.tsp_cart.model.repository.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abproject.tsp_cart.model.database.dao.UserDao
import com.abproject.tsp_cart.model.dataclass.User
import com.abproject.tsp_cart.model.dataclass.UserData
import com.abproject.tsp_cart.model.repository.BaseRepository
import com.abproject.tsp_cart.model.repository.BaseRepositoryImpl
import com.abproject.tsp_cart.util.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    private val baseRepository: BaseRepository,
) : AuthRepository {

    override suspend fun insertUser(
        user: User,
    ): Boolean {
        val checkUsernameIsExist = baseRepository.getUserInformationWithUsername(user.username)
        if (checkUsernameIsExist != null)
            return false

        val result = baseRepository.encryptPassword(user)
        dao.insertUser(result)

        baseRepository.saveUserInformationIntoSharedPrefs(
            username = result.username,
            email = result.email,
            fullname = result.fullname,
            profile = result.profile,
            isAdmin = result.isManager,
            isUser = !result.isManager
        )
        baseRepository.saveUserInformationIntoUserDataClass(
            username = result.username,
            email = result.email,
            fullname = result.fullname,
            profile = result.profile,
            isAdmin = result.isManager,
            isUser = !result.isManager
        )
        return true
    }

    override suspend fun updateUser(
        user: User,
    ) {
        val result = baseRepository.encryptPassword(user)

        baseRepository.saveUserInformationIntoSharedPrefs(
            username = result.username,
            email = result.email,
            fullname = result.fullname,
            profile = result.profile,
            isAdmin = result.isManager,
            isUser = !result.isManager
        )
        baseRepository.saveUserInformationIntoUserDataClass(
            username = result.username,
            email = result.email,
            fullname = result.fullname,
            profile = result.profile,
            isAdmin = result.isManager,
            isUser = !result.isManager
        )
        return dao.updateUser(result)
    }

    override suspend fun checkUserExisting(
        username: String,
        password: String,
        isAdmin: Boolean,
    ): LiveData<Resource<Boolean>> {
        val status = MutableLiveData<Resource<Boolean>>()
        try {
            status.postValue(Resource.Loading())
            val user = baseRepository.getUserInformationWithUsername(username)
            if (user != null) {
                if (user.password == password) {

                    baseRepository.saveUserInformationIntoSharedPrefs(
                        username = user.username,
                        email = user.email,
                        fullname = user.fullname,
                        profile = user.profile,
                        isAdmin = isAdmin,
                        isUser = !isAdmin
                    )
                    baseRepository.saveUserInformationIntoUserDataClass(
                        username = user.username,
                        email = user.email,
                        fullname = user.fullname,
                        profile = user.profile,
                        isAdmin = isAdmin,
                        isUser = !isAdmin
                    )

                    status.postValue(Resource.Success(true, null))
                } else
                    status.postValue(Resource.Error(null,
                        "Password is incorrect!"))
            } else
                status.postValue(Resource.Error(null,
                    "Username isn't valid!"))
        } catch (e: Exception) {
            status.postValue(Resource.Error(null,
                "Unexpected error occurred!"))
        }
        return status
    }

    override fun checkDataIsExist(): Boolean {
        return baseRepository.checkUserInformationFromSharedIsExist()
    }

    override fun checkUserDataIsAdmin(): Boolean {
        return baseRepository.checkInformationFromSharedIsAdminOrUser()
    }

    override suspend fun getUserInformation(): User? {
        return baseRepository.getUserInformationWithUsername(UserData.username!!)
    }
}