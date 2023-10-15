package com.medvedev.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.medvedev.data.repository.AppRepositoryImpl
import com.medvedev.presentation.pojo.Driver
import com.medvedev.presentation.usecases.*
import java.util.Locale

class DriverViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepositoryImpl(application)

    private val getDriversListUseCase = GetDriversListUseCase(repository)
    private val getDriverByIdUseCase = GetDriverByIdUseCase(repository)
    private val insertDriverItemUseCase = InsertDriverItemUseCase(repository)
    private val deleteDriverItemUseCase = DeleteDriverItemUseCase(repository)

    val driverListLD = getDriversListUseCase()

    suspend fun getDriverById(id: String) = getDriverByIdUseCase(id)
    suspend fun insertDriver(driver: Driver) = insertDriverItemUseCase(driver)
    suspend fun deleteDriver(driver: Driver) = deleteDriverItemUseCase(driver)

    fun filter(list: List<Driver>, desired: String): List<Driver> {
        return list.filter {
            it.surname
                .uppercase(Locale.getDefault())
                .contains(desired.uppercase(Locale.getDefault()))
        }
    }
}