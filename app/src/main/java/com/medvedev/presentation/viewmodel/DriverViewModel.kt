package com.medvedev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medvedev.domain.pojo.Driver
import com.medvedev.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.domain.usecase.driver.GetDriversUseCase
import com.medvedev.domain.usecase.driver.InsertDriverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DriverViewModel @Inject constructor(
    getDriversUseCase: GetDriversUseCase,
    private val getDriverByIdUseCase: GetDriverByIdUseCase,
    private val insertDriverUseCase: InsertDriverUseCase,
    private val deleteDriverUseCase: DeleteDriverUseCase,
) : ViewModel() {

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