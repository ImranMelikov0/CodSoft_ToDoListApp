package com.imranmelikov.codsoft_todolistapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.imranmelikov.codsoft_todolistapp.R
import com.imranmelikov.codsoft_todolistapp.alertdialog.AlertDialogDelete
import com.imranmelikov.codsoft_todolistapp.databinding.FragmentTaskBinding
import com.imranmelikov.codsoft_todolistapp.db.Task
import com.imranmelikov.codsoft_todolistapp.viewmodel.TaskViewModel
import java.util.Calendar

@Suppress("DEPRECATION")
class TaskFragment : Fragment() {
    private lateinit var binding:FragmentTaskBinding
    private lateinit var viewModel: TaskViewModel
    private var task: Task?=null
    private var checkStatus:Boolean=false
    private lateinit var alertDialogDelete: AlertDialogDelete

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentTaskBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        alertDialogDelete= AlertDialogDelete()
        alertDialogDelete.viewModel=viewModel

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        val checkFragment=arguments?.getInt("Insert")
        if (checkFragment==0){
            binding.apply {
                deleteBtn.visibility=View.GONE
                status.visibility=View.GONE
                activeLinear.visibility=View.GONE
                completedLinear.visibility=View.GONE
            }
        }else{
            task=arguments?.getSerializable("task") as Task
            binding.addBtn.text="Update"
            binding.dueDateText.text=task!!.dueDate
            binding.titleEdittext.setText(task!!.title)
            binding.descriptionEdittext.setText(task!!.description)
            deleteTask(checkFragment!!)
            checkStatus=task!!.completed
            checkComplete()
            clickComplete()
        }
        dueDate()
        clickBtn()
        return binding.root
    }

    private fun deleteTask(checkFragment:Int){
        binding.deleteBtn.setOnClickListener {
            alertDialogDelete.alertDialog(requireContext(),R.layout.delete_alert_dialog,task!!,checkFragment,binding.view)
        }
    }

    private fun checkComplete(){
        binding.apply {
            if (checkStatus){
                activeRadioBtn.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
                completedRadioBtn.setImageResource(R.drawable.baseline_check_circle_24)
            }else{
                activeRadioBtn.setImageResource(R.drawable.baseline_check_circle_24)
                completedRadioBtn.setImageResource(R.drawable.baseline_radio_button_unchecked_24)
            }
        }
    }

    private fun clickComplete(){
        binding.apply {
            activeRadioBtn.setOnClickListener {
                checkStatus=false
                checkComplete()
            }
            completedRadioBtn.setOnClickListener {
                checkStatus=true
                checkComplete()
            }
        }
    }
    private fun clickBtn(){
        binding.apply {
            addBtn.setOnClickListener {
                descriptionEdittext.text?.let {
                    val title = titleEdittext.text.trim()
                    val description = it.trim()
                    if (title.isEmpty() && description.isEmpty()&&dueDateText.text.isEmpty()) {
                        Toast.makeText(requireContext(), "Fill in all field", Toast.LENGTH_SHORT).show()
                    } else if (title.length > 50) {
                        Toast.makeText(requireContext(), "You can enter a maximum of 50 characters!", Toast.LENGTH_SHORT).show()
                    }  else if (description.length > 200) {
                        Toast.makeText(requireContext(), "You can enter a maximum of 200 characters!", Toast.LENGTH_SHORT).show()
                    }else{
                        val newTask= Task(title.toString(),description.toString(),binding.dueDateText.text.toString(),checkStatus)
                        if (task!=null){
                            newTask.id=task?.id
                            viewModel.updateTask(newTask)
                        }else{
                            viewModel.insertTask(newTask)
                        }
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

    private fun dueDate(){
        binding.dueDateText.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val datePicker = DatePickerDialog(requireContext(), { _, y, m, d ->
                val timePicker = TimePickerDialog(requireContext(), { _, h, min ->
                    "$d/${m+1}/$y $h:$min".also { binding.dueDateText.text = it }
                }, hour, minute, true)

                timePicker.setButton(DialogInterface.BUTTON_POSITIVE, "Set", timePicker)
                timePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", timePicker)
                timePicker.show()
            }, year, month, day)

            datePicker.setButton(DialogInterface.BUTTON_POSITIVE, "Set", datePicker)
            datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", datePicker)
            datePicker.show()
        }
    }

}