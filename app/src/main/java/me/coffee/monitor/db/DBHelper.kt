package me.coffee.monitor.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room

/**
 * 数据库辅助类
 *
 * @author kongfei
 */
@SuppressLint("StaticFieldLeak")
object DBHelper {

    private lateinit var context: Context

    val db by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "coffee.monitor"
        ).build()
    }

    fun init(context: Context) {
        this.context = context.applicationContext
    }


}