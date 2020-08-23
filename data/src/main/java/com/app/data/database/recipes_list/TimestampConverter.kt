package com.app.data.database.recipes_list

import androidx.room.TypeConverter
import java.util.*

class TimestampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}