package com.medvedev.data.local.datasource

import androidx.lifecycle.LiveData
import com.medvedev.data.local.dao.AppDao
import com.medvedev.data.local.entity.CarEntity
import com.medvedev.data.local.entity.DriverEntity
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val appDao: AppDao
) : LocalDataSource {

    override fun getCars(): LiveData<List<CarEntity>> {
        return appDao.getCarsList()
    }

    override fun getDrivers(): LiveData<List<DriverEntity>> {
        return appDao.getDriversList()
    }

    override fun getStateNumbers(): List<String> {
        return appDao.getStateNumbersList()
    }

    override fun getSurnames(): List<String> {
        return appDao.getSurnamesList()
    }

    override suspend fun getCarById(id: String): CarEntity {
        return appDao.getCarById(id = id)
    }

    override suspend fun getDriverById(id: String): DriverEntity {
        return appDao.getDriverById(id = id)
    }

    override suspend fun insertCar(car: CarEntity) {
        appDao.insertCarItem(carEntity = car)
    }

    override suspend fun insertDriver(driver: DriverEntity) {
        appDao.insertDriverItem(driverEntity = driver)
    }

    override suspend fun deleteCar(car: CarEntity) {
        appDao.deleteCarItem(carEntity = car)
    }

    override suspend fun deleteDriver(driver: DriverEntity) {
        appDao.deleteDriverItem(driverEntity = driver)
    }
}
