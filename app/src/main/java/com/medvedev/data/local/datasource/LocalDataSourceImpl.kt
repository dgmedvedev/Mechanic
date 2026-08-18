package com.medvedev.data.local.datasource

import com.medvedev.data.local.dao.AppDao
import com.medvedev.data.local.entity.CarEntity
import com.medvedev.data.local.entity.DriverEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val appDao: AppDao
) : LocalDataSource {

    override fun getCars(): Flow<List<CarEntity>> =
        appDao.getCarsList()

    override fun getDrivers(): Flow<List<DriverEntity>> =
        appDao.getDriversList()

    override fun getStateNumbers(): List<String> =
        appDao.getStateNumbersList()

    override fun getSurnames(): List<String> =
        appDao.getSurnamesList()

    override suspend fun getCarById(id: String): CarEntity =
        appDao.getCarById(id = id)

    override suspend fun getDriverById(id: String): DriverEntity =
        appDao.getDriverById(id = id)

    override suspend fun insertCar(car: CarEntity) =
        appDao.insertCarItem(carEntity = car)

    override suspend fun insertDriver(driver: DriverEntity) =
        appDao.insertDriverItem(driverEntity = driver)

    override suspend fun deleteCar(car: CarEntity) =
        appDao.deleteCarItem(carEntity = car)

    override suspend fun deleteDriver(driver: DriverEntity) =
        appDao.deleteDriverItem(driverEntity = driver)
}
