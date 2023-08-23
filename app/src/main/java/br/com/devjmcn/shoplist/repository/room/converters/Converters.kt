package br.com.devjmcn.shoplist.repository.room.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.math.BigDecimal
import java.util.Date

@TypeConverters
class Converters {
    @TypeConverter
    fun longToDate(dateLong:Long):Date{
        return Date(dateLong)
    }

    @TypeConverter
    fun dateToLong(date: Date):Long{
        return date.time
    }

    @TypeConverter
    fun toBigDecimal(value:Double?):BigDecimal{
        return value?.let { BigDecimal(it.toString())}?:BigDecimal.ZERO
    }

    @TypeConverter
    fun toDouble(value: BigDecimal?):Double?{
        return value?.toDouble()
    }
}