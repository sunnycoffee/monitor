package me.coffee.monitor

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION
import android.telephony.SmsMessage
import android.util.Log
import com.blankj.rxbus.RxBus
import com.blankj.utilcode.util.PathUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.coffee.monitor.db.DBHelper


/**
 * 短信广播监听
 *
 * @author kongfei
 */
class SmsReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.extras == null || SMS_RECEIVED_ACTION != intent.action) return
        val pdus = intent.extras!!.get("pdus") as Array<*>?
        pdus?.map { SmsMessage.createFromPdu(it as ByteArray) }
            ?.firstOrNull()
            ?.also {
                Log.d("sms", "收到短信：${it.messageBody}")
                uploadIfNeed(
                    Sms(
                        sender = it.originatingAddress,
                        content = it.messageBody,
                        time = it.timestampMillis
                    )
                )
            }
    }

    @SuppressLint("CheckResult")
    private fun uploadIfNeed(sms: Sms) {
        DBHelper.db.smsDao().insertAll(sms)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                RxBus.getDefault().postSticky(sms)
            }, {

            })
    }
}
