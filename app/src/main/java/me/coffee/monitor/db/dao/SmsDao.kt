package me.coffee.monitor.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import me.coffee.monitor.Sms

/**
 * 短信dao
 *
 * @author kongfei
 */
@Dao
interface SmsDao {

    @Query("SELECT * FROM sms order by time DESC")
    fun getAll(): Flowable<List<Sms>>

    @Insert
    fun insertAll(vararg users: Sms): Completable
}