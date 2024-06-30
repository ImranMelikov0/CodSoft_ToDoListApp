package com.imranmelikov.codsoft_todolistapp.alertdialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.imranmelikov.codsoft_todolistapp.R
import com.imranmelikov.codsoft_todolistapp.db.Task
import com.imranmelikov.codsoft_todolistapp.viewmodel.TaskViewModel

class AlertDialogUpdate {
    lateinit var viewModel: TaskViewModel
    fun alertDialog(context: Context, layout:Int,task: Task,image:ImageView){
        val builder= AlertDialog.Builder(context, R.style.RoundedAlertDialog)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView: View = inflater.inflate(layout, null)


        val alertDialog = builder.setView(dialogView).create()
        val window: Window = alertDialog.window!!

        //Adjust the position of the window and set the bottom margin
        val params = window.attributes
        params.gravity = Gravity.BOTTOM
        params.y = context.resources.getDimensionPixelSize(R.dimen._40dp)


        //Make the window full screen
        val displayMetrics = context.resources.displayMetrics
        val width = displayMetrics.widthPixels
        params.width = width
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        // Update window
        window.attributes = params

        updateCategory(dialogView,alertDialog, task,image)
        alertDialog.show()
    }

    private fun updateCategory(dialogView: View, alertDialog: AlertDialog,task:Task,image: ImageView){
        val updateBtn = dialogView.findViewById<Button>(R.id.update_btn)
        val cancelBtn=dialogView.findViewById<Button>(R.id.cancel_btn)

        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        updateBtn.setOnClickListener {
            task.completed=!task.completed
            if (task.completed){
                image.setImageResource(R.drawable.baseline_completed_circle_24)
            }else{
                image.setImageResource(R.drawable.baseline_radio_button_active_24)
            }
            task.id=task.id
            viewModel.updateTask(task)
            alertDialog.dismiss()
            viewModel.getTaskList()
        }
    }
}