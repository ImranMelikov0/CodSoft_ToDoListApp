package com.imranmelikov.codsoft_todolistapp.alertdialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.imranmelikov.codsoft_todolistapp.R
import com.imranmelikov.codsoft_todolistapp.db.Task
import com.imranmelikov.codsoft_todolistapp.viewmodel.TaskViewModel

class AlertDialogDelete {
    lateinit var viewModel: TaskViewModel
    fun alertDialog(context: Context, layout:Int,selectedItem: Task,type: Int,view: View){
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

        deleteCategory(dialogView,alertDialog, selectedItem, type,view)
        alertDialog.show()
    }

    private fun deleteCategory(dialogView: View, alertDialog: AlertDialog,selectedItem:Task,type:Int,view: View){
        val deleteBtn = dialogView.findViewById<Button>(R.id.delete_btn)
        val cancelBtn=dialogView.findViewById<Button>(R.id.cancel_btn)

        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            viewModel.deleteTask(selectedItem)
            if (type==1){
                Navigation.findNavController(view).popBackStack()
            }
            alertDialog.dismiss()
            viewModel.getTaskList()
        }
    }
}