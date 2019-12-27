package me.coffee.monitor

import android.app.Application
import me.coffee.monitor.db.DBHelper

/**
 * 应用
 *
 * @author kongfei
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DBHelper.init(this)
    }
}