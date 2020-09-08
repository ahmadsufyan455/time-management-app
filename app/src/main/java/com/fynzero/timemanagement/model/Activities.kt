package com.fynzero.timemanagement.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Activities(
    var id: Int = 0,
    var activities: String? = null,
    var type: String? = null,
    var startAt: String? = null,
    var endAt: String? = null,
    var duration: String? = null,
    var date: String? = null,
    var durationFormat: String? = null
) : Parcelable