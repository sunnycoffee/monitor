package me.coffee.monitor

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.rxbus.RxBus
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.TimeUtils
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import me.coffee.monitor.db.DBHelper
import java.security.Permissions

class MainActivity : AppCompatActivity() {

    val list = ArrayList<Sms>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        updatePermission()
    }

    private fun initView() {
        apply_btn.setOnClickListener {
            PermissionUtils
                .permission(PermissionConstants.SMS)
                .callback(object : PermissionUtils.SimpleCallback {
                    override fun onGranted() {
                        updatePermission()
                    }

                    override fun onDenied() {

                    }

                })
                .request()
        }
        updatePermission()
        rv.adapter = SmsAdapter()
        rv.layoutManager = LinearLayoutManager(this)
    }

    private fun updatePermission() {
        val isGranted = PermissionUtils.isGranted(Manifest.permission.READ_SMS)
        if (isGranted) apply_btn.text = "已授权"
        apply_btn.isEnabled = !isGranted
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onStart() {
        super.onStart()
        RxBus.getDefault().subscribeSticky(this, object : RxBus.Callback<Sms>() {
            override fun onEvent(t: Sms?) = loadData()
        })
    }

    override fun onStop() {
        super.onStop()
        RxBus.getDefault().unregister(this)
    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        DBHelper.db.smsDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                list.clear()
                list.addAll(it)
                rv.adapter?.notifyDataSetChanged()
            }, {})
    }


    inner class SmsAdapter : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_sms, parent, false)

            return VH(view).apply {
                senderTv = view.findViewById(R.id.sender_tv)
                contentTv = view.findViewById(R.id.content_tv)
                timeTv = view.findViewById(R.id.time_tv)
            }
        }

        override fun getItemCount() = list.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = list[position]
            holder.senderTv.text = "发件人:${item.sender}"
            holder.timeTv.text = TimeUtils.millis2String(item.time, "MM月dd日 HH:mm:ss")
            holder.contentTv.text = item.content
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var senderTv: TextView
        lateinit var timeTv: TextView
        lateinit var contentTv: TextView
    }

}
