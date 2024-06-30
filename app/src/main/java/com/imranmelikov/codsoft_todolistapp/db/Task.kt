package com.imranmelikov.codsoft_todolistapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Task(val title:String,val description:String,val dueDate:String,var completed:Boolean):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int?= null
}