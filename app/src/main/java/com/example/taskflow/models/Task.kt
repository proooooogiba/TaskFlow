package com.example.taskflow.models

import android.os.Parcel
import android.os.Parcelable
import com.example.taskflow.models.Card

data class Task (
    var title: String = "",
    val createdBy: String = "",
    var cards: ArrayList<Card> = ArrayList()
): Parcelable {
    constructor(parcel: Parcel): this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createTypedArrayList(Card.CREATOR)!!
    )

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(createdBy)
        writeTypedList(cards)
    }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Task> = object : Parcelable.Creator<Task> {
            override fun createFromParcel(source: Parcel): Task = Task(source)
            override fun newArray(size: Int): Array<Task?> = arrayOfNulls(size)
        }
    }


}