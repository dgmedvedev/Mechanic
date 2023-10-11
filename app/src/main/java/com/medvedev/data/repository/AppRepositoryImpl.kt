package com.medvedev.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.medvedev.data.database.AppDatabase
import com.medvedev.data.mapper.AppMapper
import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.pojo.Driver

class AppRepositoryImpl(private val application: Application) : AppRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val mapper = AppMapper()

    override fun getCarsList(): LiveData<List<Car>> = MediatorLiveData<List<Car>>().apply {
        addSource(appDao.getCarsList()) {
            value = mapper.mapCarsListDbModelToCarsList(it)
        }
    }

    override fun getDriversList(): LiveData<List<Driver>> {
        TODO("Not yet implemented")
    }

    override fun getStateNumbersList(): List<String> {
        return appDao.getStateNumbersList()
    }

    override fun getSurnamesList(): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCarItem(car: Car) {
        appDao.insertCarItem(mapper.mapCarToCarDbModel(car))
    }

    override fun insertDriverItem(driver: Driver) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCarItem(car: Car) {
        appDao.deleteCarItem(mapper.mapCarToCarDbModel(car))
    }

    override fun deleteDriverItem(driver: Driver) {
        TODO("Not yet implemented")
    }
}