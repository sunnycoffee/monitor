package me.coffee.monitor.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.coffee.monitor.Sms
import me.coffee.monitor.db.dao.SmsDao

/**
 *
 * @author kongfei
 */

@Database(entities = [Sms::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun smsDao(): SmsDao

}