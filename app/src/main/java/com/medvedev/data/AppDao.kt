package com.medvedev.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT*FROM car_items")
    fun getCarsList(): LiveData<List<CarItemDbModel>>

    @Query("SELECT*FROM driver_items")
    fun getDriversList(): LiveData<List<DriverItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCarItem(carItemDbModel: CarItemDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDriverItem(driverItemDbModel: DriverItemDbModel)

    @Delete
    fun deleteCarItem(carItemDbModel: CarItemDbModel)

    @Delete
    fun deleteDriverItem(driverItemDbModel: DriverItemDbModel)
}