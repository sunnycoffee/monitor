package me.coffee.monitor

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * @author kongfei
 */
@Entity
data class Sms(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val sender: String?,
    val content: String,
    val time: Long,
    val upload: Boolean? = null
) {
}