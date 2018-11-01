package com.sporksoft.fitbodtest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class FeedItem (var name: String,
                     var workouts: ArrayList<Workout>,
                     var max: Int) : Parcelable

@Parcelize
data class Workout (var date: Date = Date(),
                    var name: String = "",
                    var sets: Int = 0,
                    var reps: Int = 0,
                    var weight: Int = 0,
                    var max: Int = 0) : Parcelable
