package com.imranmelikov.codsoft_todolistapp.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.codsoft_todolistapp.R
import com.imranmelikov.codsoft_todolistapp.alertdialog.AlertDialogUpdate
import com.imranmelikov.codsoft_todolistapp.databinding.TaskRvBinding
import com.imranmelikov.codsoft_todolistapp.db.Task
import com.imranmelikov.codsoft_todolistapp.viewmodel.TaskViewModel

class TaskAdapter(private val viewModel: TaskViewModel):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(val binding:TaskRvBinding):RecyclerView.ViewHolder(binding.root)

    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter list task
    var taskList:List<Task>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding=TaskRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task=taskList[position]
        holder.binding.apply {
            if (task.completed){
                complete.setImageResource(R.drawable.baseline_completed_circle_24)
            }else{
                complete.setImageResource(R.drawable.baseline_radio_button_active_24)
            }

            titleText.text=task.title
            descriptionText.text=task.description
            val parts = task.dueDate.split(" ")
            if (parts.size == 2) {
                val datePart = parts[0]
                val timePart = parts[1]

                dueDate.text=datePart
                dueDateHour.text=timePart
            }
          clickComplete(task,holder)
        }

        holder.itemView.setOnClickListener {
            val bundle=Bundle()
            bundle.putInt("Insert",1)
            bundle.putSerializable("task",task)
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_taskFragment,bundle)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clickComplete( task: Task,holder: TaskViewHolder){
        holder.binding.apply {
            complete.setOnClickListener {
                val alertDialogUpdate=AlertDialogUpdate()
                alertDialogUpdate.viewModel=viewModel
                alertDialogUpdate.alertDialog(holder.itemView.context,R.layout.active_complete_alert_dialog,task,complete)
                notifyDataSetChanged()
            }
        }
    }
}