package com.medvedev.mechanic.data.local.datasource

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.data.local.dao.AppDao
import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.data.local.entity.DriverEntity
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val appDao: AppDao
) : LocalDataSource {

    override fun getCars(): Flow<List<CarEntity>> =
        appDao.getCarsList()

    override fun getDrivers(): Flow<List<DriverEntity>> =
        appDao.getDriversList()

    override suspend fun getCarById(id: String): Result<CarEntity, DataError> {
        return try {
            val entity = appDao.getCarById(id = id)
                ?: return Result.Error(DataError.NotFound)
            Result.Success(entity)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun getDriverById(id: String): Result<DriverEntity, DataError> {
        return try {
            val entity = appDao.getDriverById(id = id)
                ?: return Result.Error(DataError.NotFound)
            Result.Success(entity)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun insertCar(car: CarEntity): Result<Unit, DataError> {
        return try {
            appDao.insertCarItem(carEntity = car)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun insertDriver(driver: DriverEntity): Result<Unit, DataError> {
        return try {
            appDao.insertDriverItem(driverEntity = driver)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun deleteCar(car: CarEntity): Result<Unit, DataError> {
        return try {
            appDao.deleteCarItem(carEntity = car)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun deleteDriver(driver: DriverEntity): Result<Unit, DataError> {
        return try {
            appDao.deleteDriverItem(driverEntity = driver)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }
}
