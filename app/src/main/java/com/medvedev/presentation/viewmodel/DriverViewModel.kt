package com.medvedev.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medvedev.data.local.datasource.LocalDataSourceImpl
import com.medvedev.data.repository.DriverRepositoryImpl
import com.medvedev.domain.pojo.Driver
import com.medvedev.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.domain.usecase.driver.GetDriversUseCase
import com.medvedev.domain.usecase.driver.InsertDriverUseCase
import java.util.Locale

class DriverViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DriverRepositoryImpl(LocalDataSourceImpl(application))

    private val getDriversUseCase = GetDriversUseCase(repository)
    private val getDriverByIdUseCase = GetDriverByIdUseCase(repository)
    private val insertDriverUseCase = InsertDriverUseCase(repository)
    private val deleteDriverUseCase = DeleteDriverUseCase(repository)

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    val driverListLD = getDriversUseCase()

    suspend fun getDriverById(id: String) = getDriverByIdUseCase(id)
    suspend fun insertDriver(driver: Driver) = insertDriverUseCase(driver)
    suspend fun deleteDriver(driver: Driver) = deleteDriverUseCase(driver)

    fun filter(list: List<Driver>, desired: String): List<Driver> {
        return list.filter {
            it.surname.uppercase(Locale.getDefault())
                .plus(it.name.uppercase(Locale.getDefault()))
                .plus(it.middleName.uppercase(Locale.getDefault()))
                .contains(desired.uppercase(Locale.getDefault()))
        }
    }

    fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}