package com.imranmelikov.codsoft_todolistapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.imranmelikov.codsoft_todolistapp.db.CodSoftDatabase
import com.imranmelikov.codsoft_todolistapp.db.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application):AndroidViewModel(application) {
   @SuppressLint("StaticFieldLeak")
   private val context=application.applicationContext
   private val taskDb=Room.databaseBuilder(context,CodSoftDatabase::class.java,"CodSoftDb").build()
   private val taskDao=taskDb.dao()

    private val taskMutableLiveData=MutableLiveData<List<Task>>()
   val taskLiveData:LiveData<List<Task>>
      get()=taskMutableLiveData

   fun insertTask(task: Task){
      viewModelScope.launch {
         taskDao.insertTask(task)
      }
      getTaskList()
   }
   fun updateTask(task: Task){
      viewModelScope.launch {
         taskDao.updateTask(task)
      }
      getTaskList()
   }

   fun deleteTask(task: Task){
      viewModelScope.launch {
         taskDao.deleteTask(task)
      }
      getTaskList()
   }
   fun getTaskList(){
      viewModelScope.launch {
        taskMutableLiveData.value=taskDao.getTaskList()
      }
   }
}