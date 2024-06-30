package com.imranmelikov.codsoft_todolistapp.fragments

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.codsoft_todolistapp.R
import com.imranmelikov.codsoft_todolistapp.adapter.TaskAdapter
import com.imranmelikov.codsoft_todolistapp.alertdialog.AlertDialogDelete
import com.imranmelikov.codsoft_todolistapp.databinding.FragmentHomeBinding
import com.imranmelikov.codsoft_todolistapp.viewmodel.TaskViewModel

class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var alertDialogDelete: AlertDialogDelete
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        viewModel.getTaskList()

        alertDialogDelete=AlertDialogDelete()
        alertDialogDelete.viewModel=viewModel

        val bundle=Bundle()
        binding.floatingActionButton.setOnClickListener {
            bundle.putInt("Insert",0)
            findNavController().navigate(R.id.action_homeFragment_to_taskFragment,bundle)
        }
        taskAdapter=TaskAdapter(viewModel)
        initializeRv()
        observeTaskList()
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.taskRv)
        return binding.root
    }

    private fun observeTaskList(){
        viewModel.taskLiveData.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                binding.emptyText.visibility=View.VISIBLE
            }else{
                binding.emptyText.visibility=View.GONE
            }
            taskAdapter.taskList=it
        }
    }
    private fun initializeRv(){
        binding.taskRv.adapter=taskAdapter
        binding.taskRv.layoutManager=LinearLayoutManager(requireContext())
    }


    private val swipeCallback=object : ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition=viewHolder.layoutPosition
                        val selectedItem=taskAdapter.taskList[layoutPosition]
                        viewModel.deleteTask(selectedItem)
                        taskAdapter.notifyDataSetChanged()
                        alertDialogDelete.alertDialog(requireContext(),R.layout.delete_alert_dialog,selectedItem,0,binding.view)
        }
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive

            if (isCanceled) {
                clearCanvas(
                    c,
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }

            val background = ColorDrawable(ContextCompat.getColor(requireContext(),R.color.red))
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)

            val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.delete_white)
            val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0
            val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconTop = itemView.top + deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon?.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun clearCanvas(c: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
            val paint = Paint()
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            c.drawRect(left, top, right, bottom, paint)
        }
    }
}