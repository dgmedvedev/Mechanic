package com.medvedev.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.medvedev.data.database.AppDatabase
import com.medvedev.data.mapper.AppMapper
import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Car
import com.medvedev.domain.pojo.Driver

class AppRepositoryImpl(private val application: Application) : AppRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val mapper = AppMapper()

    override fun getCarsList(): LiveData<List<Car>> = MediatorLiveData<List<Car>>().apply {
        addSource(appDao.getCarsList()) {
            value = mapper.mapCarsListDbModelToCarsList(it)
        }
    }

    override fun getDriversList(): LiveData<List<Driver>> = MediatorLiveData<List<Driver>>().apply {
        addSource(appDao.getDriversList()) {
            value = mapper.mapDriversListDbModelToDriversList(it)
        }
    }

    override fun getStateNumbersList(): List<String> {
        return appDao.getStateNumbersList()
    }

    override fun getSurnamesList(): List<String> {
        return appDao.getSurnamesList()
    }

    override suspend fun getCarById(id: String): Car {
        val dbModel = appDao.getCarById(id)
        return mapper.mapCarDbModelToCar(dbModel)
    }

    override suspend fun getDriverById(id: String): Driver {
        val dbModel = appDao.getDriverById(id)
        return mapper.mapDriverDbModelToDriver(dbModel)
    }

    override suspend fun insertCarItem(car: Car) {
        appDao.insertCarItem(mapper.mapCarToCarDbModel(car))
    }

    override suspend fun insertDriverItem(driver: Driver) {
        appDao.insertDriverItem(mapper.mapDriverToDriverDbModel(driver))
    }

    override suspend fun deleteCarItem(car: Car) {
        appDao.deleteCarItem(mapper.mapCarToCarDbModel(car))
    }

    override suspend fun deleteDriverItem(driver: Driver) {
        appDao.deleteDriverItem(mapper.mapDriverToDriverDbModel(driver))
    }
}