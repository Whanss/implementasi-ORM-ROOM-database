package com.ikhwan.roomdatabse.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStatusKomputer(status: StatusKomputer): String {
        return status.value
    }

    @TypeConverter
    fun toStatusKomputer(value: String): StatusKomputer {
        return StatusKomputer.fromValue(value)
    }
}
